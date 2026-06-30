#include "facedetect_hw.h"
#include "hw_kernels.h"
#include "hw_image.h"
#include "hw_network.h"
#include "hw_postprocess.h"

#include <algorithm>
#include <chrono>
#include <cstdio>
#include <numeric>
#include <vector>

namespace {

float ValueAt(int index) {
    return static_cast<float>(((index * 37) % 29) - 14) * 0.125f;
}

void Fill(std::vector<float>& values) {
    for (size_t i = 0; i < values.size(); ++i) {
        values[i] = ValueAt(static_cast<int>(i));
    }
}

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

void BenchmarkShape(int rows, int cols, int channels, int out_channels) {
    const int iterations = 100;
    const int warmup = 10;
    std::vector<float> input(rows * cols * channels);
    std::vector<float> weights_pw(out_channels * channels);
    std::vector<float> bias_pw(out_channels);
    std::vector<float> output_pw(rows * cols * out_channels);
    std::vector<float> weights_dw(9 * channels);
    std::vector<float> bias_dw(channels);
    std::vector<float> output_dw(rows * cols * channels);
    Fill(input);
    Fill(weights_pw);
    Fill(bias_pw);
    Fill(weights_dw);
    Fill(bias_dw);

    const fdt::hw::ConstBlobView input_view{
        input.data(), rows, cols, channels, channels};
    const fdt::hw::ConstBlobView weights_pw_view{
        weights_pw.data(), 1, out_channels, channels, channels};
    fdt::hw::BlobView output_pw_view{
        output_pw.data(), rows, cols, out_channels, out_channels};
    const fdt::hw::PackedPointwiseFilter packed_hw =
        fdt::hw::PackPointwiseFilter(weights_pw_view, bias_pw.data(),
                                     fdt::hw::HwFloatLanes());
    const fdt::hw::PackedPointwiseFilter packed_intrinsics =
        fdt::hw::PackPointwiseFilter(weights_pw_view, bias_pw.data(),
                                     fdt::hw::IntrinsicsFloatLanes());
    const fdt::hw::PointwisePlan plan_hw =
        fdt::hw::CreatePointwisePlanHw(weights_pw_view, bias_pw.data());
    const fdt::hw::PointwisePlan plan_intrinsics =
        fdt::hw::CreatePointwisePlanIntrinsics(weights_pw_view, bias_pw.data());
    const fdt::hw::ConstBlobView weights_dw_view{
        weights_dw.data(), 1, 9, channels, channels};
    fdt::hw::BlobView output_dw_view{
        output_dw.data(), rows, cols, channels, channels};

    std::printf("\nshape rows=%d cols=%d channels=%d out_channels=%d\n",
                rows, cols, channels, out_channels);
    PrintStats("scalar pointwise",
               Measure([&] {
                   fdt::hw::Pointwise1x1Scalar(input_view, weights_pw_view,
                                               bias_pw.data(), output_pw_view);
               }, warmup, iterations));
    PrintStats("hw pointwise",
               Measure([&] {
                   fdt::hw::Pointwise1x1Hw(input_view, weights_pw_view,
                                           bias_pw.data(), output_pw_view);
               }, warmup, iterations));
    PrintStats("hw packed pointwise",
               Measure([&] {
                   fdt::hw::Pointwise1x1PackedHw(input_view, packed_hw,
                                                 output_pw_view);
               }, warmup, iterations));
    PrintStats("hw planned pointwise",
               Measure([&] {
                   fdt::hw::Pointwise1x1PlannedHw(
                       input_view, weights_pw_view, bias_pw.data(), plan_hw,
                       output_pw_view);
               }, warmup, iterations));
    PrintStats("intrinsics pointwise",
               Measure([&] {
                   fdt::hw::Pointwise1x1Intrinsics(
                       input_view, weights_pw_view, bias_pw.data(), output_pw_view);
               }, warmup, iterations));
    PrintStats("intrinsics packed pw",
               Measure([&] {
                   fdt::hw::Pointwise1x1PackedIntrinsics(
                       input_view, packed_intrinsics, output_pw_view);
               }, warmup, iterations));
    PrintStats("intrinsics planned pw",
               Measure([&] {
                   fdt::hw::Pointwise1x1PlannedIntrinsics(
                       input_view, weights_pw_view, bias_pw.data(),
                       plan_intrinsics, output_pw_view);
               }, warmup, iterations));
    PrintStats("scalar depthwise",
               Measure([&] {
                   fdt::hw::Depthwise3x3Scalar(input_view, weights_dw_view,
                                               bias_dw.data(), output_dw_view);
               }, warmup, iterations));
    PrintStats("hw depthwise",
               Measure([&] {
                   fdt::hw::Depthwise3x3Hw(input_view, weights_dw_view,
                                           bias_dw.data(), output_dw_view);
               }, warmup, iterations));
    PrintStats("intrinsics depthwise",
               Measure([&] {
                   fdt::hw::Depthwise3x3Intrinsics(
                       input_view, weights_dw_view, bias_dw.data(), output_dw_view);
               }, warmup, iterations));
}

void FillBlobValid(fdt::hw::HwBlob& blob) {
    for (int row = 0; row < blob.rows(); ++row) {
        for (int col = 0; col < blob.cols(); ++col) {
            float* pixel = blob.Ptr(row, col);
            for (int ch = 0; ch < blob.channels(); ++ch) {
                const int index =
                    (row * blob.cols() + col) * blob.channels() + ch;
                pixel[ch] = ValueAt(index);
            }
        }
    }
}

void FillImage(std::vector<unsigned char>& image) {
    for (size_t i = 0; i < image.size(); ++i) {
        image[i] = static_cast<unsigned char>((i * 13 + 17) % 251);
    }
}

void BenchmarkBackbone(int rows, int cols) {
    const int iterations = 30;
    const int warmup = 3;
    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    fdt::hw::HwBlob input(rows, cols, 32);
    FillBlobValid(input);
    fdt::hw::HwBackboneOutputs outputs;

    std::printf("\nbackbone conv0-conv5 rows=%d cols=%d channels=32\n", rows,
                cols);
    PrintStats("hw backbone",
               Measure([&] {
                   outputs = fdt::hw::ForwardBackboneHw(input, model);
               }, warmup, iterations));
    std::printf("fb1=%dx%dx%d fb2=%dx%dx%d fb3=%dx%dx%d\n", outputs.fb1.rows(),
                outputs.fb1.cols(), outputs.fb1.channels(), outputs.fb2.rows(),
                outputs.fb2.cols(), outputs.fb2.channels(), outputs.fb3.rows(),
                outputs.fb3.cols(), outputs.fb3.channels());
}

void BenchmarkRawNetwork(int rows, int cols) {
    const int iterations = 30;
    const int warmup = 3;
    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    fdt::hw::HwBlob input(rows, cols, 32);
    FillBlobValid(input);
    fdt::hw::HwHeadOutputs outputs;

    std::printf("\nraw network rows=%d cols=%d channels=32\n", rows, cols);
    PrintStats("hw raw network",
               Measure([&] {
                   outputs = fdt::hw::ForwardNetworkHw(input, model);
               }, warmup, iterations));
    for (int level = 0; level < 3; ++level) {
        std::printf("level%d cls=%dx%dx%d reg=%dx%dx%d kps=%dx%dx%d obj=%dx%dx%d\n",
                    level, outputs.cls[level].rows(),
                    outputs.cls[level].cols(), outputs.cls[level].channels(),
                    outputs.reg[level].rows(), outputs.reg[level].cols(),
                    outputs.reg[level].channels(), outputs.kps[level].rows(),
                    outputs.kps[level].cols(), outputs.kps[level].channels(),
                    outputs.obj[level].rows(), outputs.obj[level].cols(),
                    outputs.obj[level].channels());
    }
}

void BenchmarkDecodedNetwork(int rows, int cols) {
    const int iterations = 30;
    const int warmup = 3;
    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    fdt::hw::HwBlob input(rows, cols, 32);
    FillBlobValid(input);
    fdt::hw::HwDecodedOutputs outputs;

    std::printf("\ndecoded network rows=%d cols=%d channels=32\n", rows, cols);
    PrintStats("hw decoded tensors",
               Measure([&] {
                   outputs = fdt::hw::DecodeAndConcatHw(
                       fdt::hw::ForwardNetworkHw(input, model));
               }, warmup, iterations));
    std::printf("cls=%dx%dx%d reg=%dx%dx%d kps=%dx%dx%d obj=%dx%dx%d\n",
                outputs.cls.rows(), outputs.cls.cols(), outputs.cls.channels(),
                outputs.reg.rows(), outputs.reg.cols(), outputs.reg.channels(),
                outputs.kps.rows(), outputs.kps.cols(), outputs.kps.channels(),
                outputs.obj.rows(), outputs.obj.cols(), outputs.obj.channels());
}

void BenchmarkImageToDecoded(int width, int height) {
    const int iterations = 30;
    const int warmup = 3;
    const int channels = 3;
    const int step = width * channels;
    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    std::vector<unsigned char> image(static_cast<size_t>(height) * step);
    FillImage(image);
    fdt::hw::HwDecodedOutputs outputs;

    std::printf("\nimage-to-decoded width=%d height=%d channels=3\n", width,
                height);
    PrintStats("hw image decoded",
               Measure([&] {
                   const fdt::hw::HwBlob input =
                       fdt::hw::ImageToInitialBlobHw(
                           image.data(), width, height, channels, step);
                   outputs = fdt::hw::DecodeAndConcatHw(
                       fdt::hw::ForwardNetworkHw(input, model));
               }, warmup, iterations));
    std::printf("cls=%dx%dx%d reg=%dx%dx%d kps=%dx%dx%d obj=%dx%dx%d\n",
                outputs.cls.rows(), outputs.cls.cols(), outputs.cls.channels(),
                outputs.reg.rows(), outputs.reg.cols(), outputs.reg.channels(),
                outputs.kps.rows(), outputs.kps.cols(), outputs.kps.channels(),
                outputs.obj.rows(), outputs.obj.cols(), outputs.obj.channels());
}

void BenchmarkPublicApi(int width, int height) {
    const int iterations = 30;
    const int warmup = 3;
    const int channels = 3;
    const int step = width * channels;
    std::vector<unsigned char> image(static_cast<size_t>(height) * step);
    std::vector<unsigned char> result(FACEDETECTION_HW_RESULT_BUFFER_SIZE);
    FillImage(image);
    int* output = 0;

    std::printf("\npublic api width=%d height=%d channels=3\n", width, height);
    PrintStats("facedetect_hw_cnn",
               Measure([&] {
                   output = facedetect_hw_cnn(result.data(), image.data(),
                                              width, height, step);
               }, warmup, iterations));
    std::printf("faces=%d\n", output ? output[0] : -1);
}

}  // namespace

int main() {
    BenchmarkShape(48, 64, 32, 16);
    BenchmarkShape(24, 32, 64, 64);
    BenchmarkShape(12, 16, 64, 64);
    BenchmarkShape(6, 8, 64, 10);
    BenchmarkBackbone(48, 64);
    BenchmarkRawNetwork(48, 64);
    BenchmarkDecodedNetwork(48, 64);
    BenchmarkImageToDecoded(96, 96);
    BenchmarkPublicApi(96, 96);
    return 0;
}
