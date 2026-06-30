#include "facedetect_hw.h"
#include "facedetectcnn.h"
#include "hw_image.h"
#include "hw_model.h"
#include "hw_network.h"
#include "hw_postprocess.h"

#include <opencv2/imgcodecs.hpp>
#include <opencv2/core.hpp>

#include <algorithm>
#include <chrono>
#include <cstdio>
#include <numeric>
#include <string>
#include <vector>

namespace {

template <typename Fn>
std::vector<double> Measure(Fn fn, int warmup, int iterations) {
    for (int i = 0; i < warmup; ++i) {
        fn();
    }
    std::vector<double> times;
    times.reserve(static_cast<size_t>(iterations));
    for (int i = 0; i < iterations; ++i) {
        const auto start = std::chrono::high_resolution_clock::now();
        fn();
        const auto stop = std::chrono::high_resolution_clock::now();
        times.push_back(
            std::chrono::duration<double, std::milli>(stop - start).count());
    }
    return times;
}

void PrintStats(const char* name, std::vector<double> times) {
    std::sort(times.begin(), times.end());
    const double sum = std::accumulate(times.begin(), times.end(), 0.0);
    const double avg = sum / static_cast<double>(times.size());
    const double min = times.front();
    const double p50 = times[times.size() / 2];
    const double p95 = times[static_cast<size_t>(times.size() * 95 / 100)];
    std::printf("%-24s min=%8.4f avg=%8.4f p50=%8.4f p95=%8.4f ms\n",
                name, min, avg, p50, p95);
}

void PrintFaces(const char* name, const std::vector<unsigned char>& buffer) {
    const int* count = reinterpret_cast<const int*>(buffer.data());
    std::printf("%s faces=%d\n", name, count[0]);
    const int faces_to_print = std::min(count[0], 5);
    for (int i = 0; i < faces_to_print; ++i) {
        const short* face =
            reinterpret_cast<const short*>(buffer.data() + 4) +
            FACEDETECTION_RESULT_STRIDE_SHORTS * static_cast<size_t>(i);
        std::printf("  #%d score=%d rect=(%d,%d,%d,%d) lm0=(%d,%d)\n", i,
                    face[0], face[1], face[2], face[3], face[4], face[5],
                    face[6]);
    }
}

void BenchmarkHwStages(const cv::Mat& image) {
    const int warmup = 2;
    const int iterations = 10;
    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    const fdt::hw::HwBlob input = fdt::hw::ImageToInitialBlobHw(
        image.data, image.cols, image.rows, 3, static_cast<int>(image.step));
    const fdt::hw::HwBackboneOutputs backbone =
        fdt::hw::ForwardBackboneHw(input, model);
    const fdt::hw::HwHeadOutputs heads =
        fdt::hw::ForwardHeadsHw(backbone, model);
    const fdt::hw::HwDecodedOutputs decoded =
        fdt::hw::DecodeAndConcatHw(heads);
    std::vector<fdt::hw::HwFaceRect> faces;
    fdt::hw::HwBlob image_workspace;
    fdt::hw::HwNetworkWorkspace network_workspace;

    std::printf("\nhw stage breakdown\n");
    PrintStats("image transform",
               Measure([&] {
                   fdt::hw::ImageToInitialBlobHwTo(
                       image.data, image.cols, image.rows, 3,
                       static_cast<int>(image.step), &image_workspace);
               }, warmup, iterations));
    PrintStats("backbone",
               Measure([&] {
                   const fdt::hw::HwBackboneOutputs tmp =
                       fdt::hw::ForwardBackboneHw(input, model,
                                                  &network_workspace);
               }, warmup, iterations));
    PrintStats("fpn + raw heads",
               Measure([&] {
                   fdt::hw::HwBackboneOutputs tmp_backbone = backbone;
                   const fdt::hw::HwHeadOutputs tmp =
                       fdt::hw::ForwardHeadsHw(std::move(tmp_backbone), model,
                                               &network_workspace);
               }, warmup, iterations));
    PrintStats("network workspace",
               Measure([&] {
                   const fdt::hw::HwHeadOutputs tmp =
                       fdt::hw::ForwardNetworkHw(input, model,
                                                 &network_workspace);
               }, warmup, iterations));
    PrintStats("decode + concat",
               Measure([&] {
                   const fdt::hw::HwDecodedOutputs tmp =
                       fdt::hw::DecodeAndConcatHw(heads);
               }, warmup, iterations));
    PrintStats("nms",
               Measure([&] {
                   faces = fdt::hw::DetectionOutputHw(decoded, 0.45f, 0.2f,
                                                      1000, 512);
               }, warmup, iterations));
    std::printf("stage faces=%d input=%dx%dx%d fb1=%dx%dx%d fb2=%dx%dx%d fb3=%dx%dx%d\n",
                static_cast<int>(faces.size()), input.rows(), input.cols(),
                input.channels(), backbone.fb1.rows(), backbone.fb1.cols(),
                backbone.fb1.channels(), backbone.fb2.rows(),
                backbone.fb2.cols(), backbone.fb2.channels(),
                backbone.fb3.rows(), backbone.fb3.cols(),
                backbone.fb3.channels());
}

void BenchmarkBackboneBlocks(const cv::Mat& image) {
    const int warmup = 2;
    const int iterations = 10;
    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    const fdt::hw::HwBlob input = fdt::hw::ImageToInitialBlobHw(
        image.data, image.cols, image.rows, 3, static_cast<int>(image.step));

    const fdt::hw::HwBlob conv_head =
        fdt::hw::ConvolutionHw(input, model.Filter(0), true);
    const fdt::hw::HwBlob conv0 = fdt::hw::ConvolutionDepthwisePointwiseHw(
        conv_head, model.Filter(1), model.Filter(2), true);
    const fdt::hw::HwBlob pool0 = fdt::hw::MaxPooling2x2S2Hw(conv0);
    const fdt::hw::HwBlob conv1 = fdt::hw::Convolution4LayerUnitHw(
        pool0, model.Filter(3), model.Filter(4), model.Filter(5),
        model.Filter(6), true);
    const fdt::hw::HwBlob conv2 = fdt::hw::Convolution4LayerUnitHw(
        conv1, model.Filter(7), model.Filter(8), model.Filter(9),
        model.Filter(10), true);
    const fdt::hw::HwBlob pool3 = fdt::hw::MaxPooling2x2S2Hw(conv2);
    const fdt::hw::HwBlob conv3 = fdt::hw::Convolution4LayerUnitHw(
        pool3, model.Filter(11), model.Filter(12), model.Filter(13),
        model.Filter(14), true);
    const fdt::hw::HwBlob pool4 = fdt::hw::MaxPooling2x2S2Hw(conv3);
    const fdt::hw::HwBlob conv4 = fdt::hw::Convolution4LayerUnitHw(
        pool4, model.Filter(15), model.Filter(16), model.Filter(17),
        model.Filter(18), true);
    const fdt::hw::HwBlob pool5 = fdt::hw::MaxPooling2x2S2Hw(conv4);

    std::printf("\nhw backbone block breakdown\n");
    PrintStats("conv_head",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(input, model.Filter(0), true);
               }, warmup, iterations));
    PrintStats("conv0",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionDepthwisePointwiseHw(
                           conv_head, model.Filter(1), model.Filter(2), true);
               }, warmup, iterations));
    PrintStats("pool0",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::MaxPooling2x2S2Hw(conv0);
               }, warmup, iterations));
    PrintStats("conv1",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::Convolution4LayerUnitHw(
                           pool0, model.Filter(3), model.Filter(4),
                           model.Filter(5), model.Filter(6), true);
               }, warmup, iterations));
    PrintStats("conv2",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::Convolution4LayerUnitHw(
                           conv1, model.Filter(7), model.Filter(8),
                           model.Filter(9), model.Filter(10), true);
               }, warmup, iterations));
    PrintStats("pool3",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::MaxPooling2x2S2Hw(conv2);
               }, warmup, iterations));
    PrintStats("conv3",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::Convolution4LayerUnitHw(
                           pool3, model.Filter(11), model.Filter(12),
                           model.Filter(13), model.Filter(14), true);
               }, warmup, iterations));
    PrintStats("pool4",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::MaxPooling2x2S2Hw(conv3);
               }, warmup, iterations));
    PrintStats("conv4",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::Convolution4LayerUnitHw(
                           pool4, model.Filter(15), model.Filter(16),
                           model.Filter(17), model.Filter(18), true);
               }, warmup, iterations));
    PrintStats("pool5",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::MaxPooling2x2S2Hw(conv4);
               }, warmup, iterations));
    PrintStats("conv5",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::Convolution4LayerUnitHw(
                           pool5, model.Filter(19), model.Filter(20),
                           model.Filter(21), model.Filter(22), true);
               }, warmup, iterations));
    std::printf("block shapes conv_head=%dx%dx%d conv1=%dx%dx%d conv3=%dx%dx%d conv5=%dx%dx%d\n",
                conv_head.rows(), conv_head.cols(), conv_head.channels(),
                conv1.rows(), conv1.cols(), conv1.channels(), conv3.rows(),
                conv3.cols(), conv3.channels(), pool5.rows(), pool5.cols(),
                pool5.channels());
}

void BenchmarkDepthwisePointwiseParts(const cv::Mat& image) {
    const int warmup = 2;
    const int iterations = 10;
    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    const fdt::hw::HwBlob input = fdt::hw::ImageToInitialBlobHw(
        image.data, image.cols, image.rows, 3, static_cast<int>(image.step));

    const fdt::hw::HwBlob conv_head =
        fdt::hw::ConvolutionHw(input, model.Filter(0), true);
    const fdt::hw::HwBlob conv0_pw =
        fdt::hw::ConvolutionHw(conv_head, model.Filter(1), false);
    const fdt::hw::HwBlob conv0 =
        fdt::hw::ConvolutionHw(conv0_pw, model.Filter(2), true);
    const fdt::hw::HwBlob pool0 = fdt::hw::MaxPooling2x2S2Hw(conv0);

    const fdt::hw::HwBlob conv1_pw0 =
        fdt::hw::ConvolutionHw(pool0, model.Filter(3), false);
    const fdt::hw::HwBlob conv1_dw0 =
        fdt::hw::ConvolutionHw(conv1_pw0, model.Filter(4), true);
    const fdt::hw::HwBlob conv1_pw1 =
        fdt::hw::ConvolutionHw(conv1_dw0, model.Filter(5), false);
    const fdt::hw::HwBlob conv1 =
        fdt::hw::ConvolutionHw(conv1_pw1, model.Filter(6), true);

    const fdt::hw::HwBlob conv2_pw0 =
        fdt::hw::ConvolutionHw(conv1, model.Filter(7), false);
    const fdt::hw::HwBlob conv2_dw0 =
        fdt::hw::ConvolutionHw(conv2_pw0, model.Filter(8), true);
    const fdt::hw::HwBlob conv2_pw1 =
        fdt::hw::ConvolutionHw(conv2_dw0, model.Filter(9), false);
    const fdt::hw::HwBlob conv2 =
        fdt::hw::ConvolutionHw(conv2_pw1, model.Filter(10), true);
    const fdt::hw::HwBlob pool3 = fdt::hw::MaxPooling2x2S2Hw(conv2);

    const fdt::hw::HwBlob conv3_pw0 =
        fdt::hw::ConvolutionHw(pool3, model.Filter(11), false);
    const fdt::hw::HwBlob conv3_dw0 =
        fdt::hw::ConvolutionHw(conv3_pw0, model.Filter(12), true);
    const fdt::hw::HwBlob conv3_pw1 =
        fdt::hw::ConvolutionHw(conv3_dw0, model.Filter(13), false);

    std::printf("\nhw backbone pointwise/depthwise breakdown\n");
    PrintStats("conv0 pointwise",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(conv_head, model.Filter(1),
                                              false);
               }, warmup, iterations));
    PrintStats("conv0 depthwise",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(conv0_pw, model.Filter(2), true);
               }, warmup, iterations));

    PrintStats("conv1 pointwise0",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(pool0, model.Filter(3), false);
               }, warmup, iterations));
    PrintStats("conv1 depthwise0",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(conv1_pw0, model.Filter(4),
                                              true);
               }, warmup, iterations));
    PrintStats("conv1 pointwise1",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(conv1_dw0, model.Filter(5),
                                              false);
               }, warmup, iterations));
    PrintStats("conv1 depthwise1",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(conv1_pw1, model.Filter(6),
                                              true);
               }, warmup, iterations));

    PrintStats("conv2 pointwise0",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(conv1, model.Filter(7), false);
               }, warmup, iterations));
    PrintStats("conv2 depthwise0",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(conv2_pw0, model.Filter(8),
                                              true);
               }, warmup, iterations));
    PrintStats("conv2 pointwise1",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(conv2_dw0, model.Filter(9),
                                              false);
               }, warmup, iterations));
    PrintStats("conv2 depthwise1",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(conv2_pw1, model.Filter(10),
                                              true);
               }, warmup, iterations));

    PrintStats("conv3 pointwise0",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(pool3, model.Filter(11), false);
               }, warmup, iterations));
    PrintStats("conv3 depthwise0",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(conv3_pw0, model.Filter(12),
                                              true);
               }, warmup, iterations));
    PrintStats("conv3 pointwise1",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(conv3_dw0, model.Filter(13),
                                              false);
               }, warmup, iterations));
    PrintStats("conv3 depthwise1",
               Measure([&] {
                   const fdt::hw::HwBlob tmp =
                       fdt::hw::ConvolutionHw(conv3_pw1, model.Filter(14),
                                              true);
               }, warmup, iterations));
}

void BenchmarkHotspotKernels(const cv::Mat& image) {
    const int warmup = 5;
    const int iterations = 40;
    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    fdt::hw::HwBlob input;
    fdt::hw::ImageToInitialBlobHwTo(image.data, image.cols, image.rows, 3,
                                    static_cast<int>(image.step), &input);

    const fdt::hw::HwBlob conv_head =
        fdt::hw::ConvolutionHw(input, model.Filter(0), true);
    const fdt::hw::HwBlob conv0_pw =
        fdt::hw::ConvolutionHw(conv_head, model.Filter(1), false);
    const fdt::hw::HwBlob conv0 =
        fdt::hw::ConvolutionHw(conv0_pw, model.Filter(2), true);
    const fdt::hw::HwBlob pool0 = fdt::hw::MaxPooling2x2S2Hw(conv0);
    const fdt::hw::HwBlob conv1 = fdt::hw::Convolution4LayerUnitHw(
        pool0, model.Filter(3), model.Filter(4), model.Filter(5),
        model.Filter(6), true);
    const fdt::hw::HwBlob conv2_pw0 =
        fdt::hw::ConvolutionHw(conv1, model.Filter(7), false);
    const fdt::hw::HwBlob conv2_dw0 =
        fdt::hw::ConvolutionHw(conv2_pw0, model.Filter(8), true);
    const fdt::hw::HwBlob conv2_pw1 =
        fdt::hw::ConvolutionHw(conv2_dw0, model.Filter(9), false);

    fdt::hw::HwBlob out;

    std::printf("\nhw hotspot kernel-only breakdown\n");
    PrintStats("conv_head pw+relu",
               Measure([&] {
                   fdt::hw::ConvolutionHwTo(input, model.Filter(0), true,
                                            &out);
               }, warmup, iterations));
    PrintStats("conv0 pointwise",
               Measure([&] {
                   fdt::hw::ConvolutionHwTo(conv_head, model.Filter(1), false,
                                            &out);
               }, warmup, iterations));
    PrintStats("conv0 depthwise",
               Measure([&] {
                   fdt::hw::ConvolutionHwTo(conv0_pw, model.Filter(2), true,
                                            &out);
               }, warmup, iterations));
    PrintStats("conv2 pointwise0",
               Measure([&] {
                   fdt::hw::ConvolutionHwTo(conv1, model.Filter(7), false,
                                            &out);
               }, warmup, iterations));
    PrintStats("conv2 depthwise0",
               Measure([&] {
                   fdt::hw::ConvolutionHwTo(conv2_pw0, model.Filter(8), true,
                                            &out);
               }, warmup, iterations));
    PrintStats("conv2 pointwise1",
               Measure([&] {
                   fdt::hw::ConvolutionHwTo(conv2_dw0, model.Filter(9), false,
                                            &out);
               }, warmup, iterations));
    PrintStats("conv2 depthwise1",
               Measure([&] {
                   fdt::hw::ConvolutionHwTo(conv2_pw1, model.Filter(10), true,
                                            &out);
               }, warmup, iterations));
}

int CountMismatchedShorts(const std::vector<unsigned char>& lhs,
                          const std::vector<unsigned char>& rhs) {
    const int lhs_count = reinterpret_cast<const int*>(lhs.data())[0];
    const int rhs_count = reinterpret_cast<const int*>(rhs.data())[0];
    if (lhs_count != rhs_count) {
        return -1;
    }

    const int shorts_to_compare =
        lhs_count * FACEDETECTION_RESULT_STRIDE_SHORTS;
    const short* lhs_faces = reinterpret_cast<const short*>(lhs.data() + 4);
    const short* rhs_faces = reinterpret_cast<const short*>(rhs.data() + 4);
    int mismatches = 0;
    for (int i = 0; i < shorts_to_compare; ++i) {
        if (lhs_faces[i] != rhs_faces[i]) {
            ++mismatches;
        }
    }
    return mismatches;
}

}  // namespace

int main(int argc, char** argv) {
    const std::string image_path =
        argc > 1 ? argv[1] : "images/cnnresult.png";
    cv::Mat image = cv::imread(image_path, cv::IMREAD_COLOR);
    if (image.empty()) {
        std::fprintf(stderr, "failed to load image: %s\n", image_path.c_str());
        return 1;
    }

    std::vector<unsigned char> original_result(FACEDETECTION_RESULT_BUFFER_SIZE);
    std::vector<unsigned char> hw_result(FACEDETECTION_HW_RESULT_BUFFER_SIZE);
    int* original = facedetect_cnn(original_result.data(), image.data,
                                   image.cols, image.rows,
                                   static_cast<int>(image.step));
    int* hw = facedetect_hw_cnn(hw_result.data(), image.data, image.cols,
                                image.rows, static_cast<int>(image.step));
    if (!original || !hw) {
        std::fprintf(stderr, "detector returned null\n");
        return 2;
    }

    std::printf("image=%s size=%dx%d step=%d\n", image_path.c_str(),
                image.cols, image.rows, static_cast<int>(image.step));
    PrintFaces("original", original_result);
    PrintFaces("hw", hw_result);
    const int mismatches = CountMismatchedShorts(original_result, hw_result);
    if (mismatches < 0) {
        std::printf("result counts differ\n");
    } else {
        std::printf("result short mismatches=%d\n", mismatches);
    }

    const int warmup = 3;
    const int iterations = 20;
    PrintStats("original facedetect_cnn",
               Measure([&] {
                   facedetect_cnn(original_result.data(), image.data,
                                  image.cols, image.rows,
                                  static_cast<int>(image.step));
               }, warmup, iterations));
    PrintStats("hw facedetect_hw_cnn",
               Measure([&] {
                   facedetect_hw_cnn(hw_result.data(), image.data, image.cols,
                                     image.rows,
                                     static_cast<int>(image.step));
               }, warmup, iterations));
    BenchmarkHwStages(image);
    BenchmarkBackboneBlocks(image);
    BenchmarkDepthwisePointwiseParts(image);
    BenchmarkHotspotKernels(image);
    return 0;
}
