#define DOCTEST_CONFIG_IMPLEMENT_WITH_MAIN
#include "doctest.h"

#include "facedetect_hw.h"
#include "hw_filter.h"
#include "hw_image.h"
#include "hw_kernels.h"
#include "hw_layers.h"
#include "hw_model.h"
#include "hw_network.h"
#include "hw_postprocess.h"

#include "facedetectcnn.h"

#include <algorithm>
#include <cmath>
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

void FillBlob(fdt::hw::HwBlob& blob) {
    for (size_t i = 0; i < blob.data().size(); ++i) {
        blob.data()[i] = ValueAt(static_cast<int>(i));
    }
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

void FillCDataBlob(CDataBlob<float>& blob) {
    for (int row = 0; row < blob.rows; ++row) {
        for (int col = 0; col < blob.cols; ++col) {
            float* pixel = blob.ptr(row, col);
            for (int ch = 0; ch < blob.channels; ++ch) {
                const int index =
                    (row * blob.cols + col) * blob.channels + ch;
                pixel[ch] = ValueAt(index);
            }
        }
    }
}

void CheckBlobClose(const CDataBlob<float>& expected,
                    const fdt::hw::HwBlob& actual,
                    float epsilon = 1e-3f) {
    REQUIRE(expected.rows == actual.rows());
    REQUIRE(expected.cols == actual.cols());
    REQUIRE(expected.channels == actual.channels());
    for (int row = 0; row < expected.rows; ++row) {
        for (int col = 0; col < expected.cols; ++col) {
            const float* expected_pixel = expected.ptr(row, col);
            const float* actual_pixel = actual.Ptr(row, col);
            for (int ch = 0; ch < expected.channels; ++ch) {
                CHECK(std::fabs(expected_pixel[ch] - actual_pixel[ch]) <=
                      epsilon);
            }
        }
    }
}

void FillImage(std::vector<unsigned char>& image) {
    for (size_t i = 0; i < image.size(); ++i) {
        image[i] = static_cast<unsigned char>((i * 13 + 17) % 251);
    }
}

void CheckClose(const std::vector<float>& expected,
                const std::vector<float>& actual,
                float epsilon = 1e-4f) {
    REQUIRE(expected.size() == actual.size());
    for (size_t i = 0; i < expected.size(); ++i) {
        CHECK(std::fabs(expected[i] - actual[i]) <= epsilon);
    }
}

}  // namespace

void CheckPackedPointwiseCase(int rows,
                              int cols,
                              int channels,
                              int out_channels) {
    std::vector<float> input(static_cast<size_t>(rows) * cols * channels);
    std::vector<float> weights(static_cast<size_t>(out_channels) * channels);
    std::vector<float> bias(out_channels);
    std::vector<float> scalar(static_cast<size_t>(rows) * cols * out_channels);
    std::vector<float> packed_scalar(scalar.size());
    std::vector<float> packed_hw(scalar.size());
    std::vector<float> packed_intrinsics(scalar.size());
    std::vector<float> planned_hw(scalar.size());
    std::vector<float> planned_intrinsics(scalar.size());
    Fill(input);
    Fill(weights);
    Fill(bias);

    const fdt::hw::ConstBlobView input_view{
        input.data(), rows, cols, channels, channels};
    const fdt::hw::ConstBlobView weights_view{
        weights.data(), 1, out_channels, channels, channels};
    fdt::hw::BlobView scalar_view{
        scalar.data(), rows, cols, out_channels, out_channels};
    fdt::hw::BlobView packed_scalar_view{
        packed_scalar.data(), rows, cols, out_channels, out_channels};
    fdt::hw::BlobView packed_hw_view{
        packed_hw.data(), rows, cols, out_channels, out_channels};
    fdt::hw::BlobView packed_intrinsics_view{
        packed_intrinsics.data(), rows, cols, out_channels, out_channels};
    fdt::hw::BlobView planned_hw_view{
        planned_hw.data(), rows, cols, out_channels, out_channels};
    fdt::hw::BlobView planned_intrinsics_view{
        planned_intrinsics.data(), rows, cols, out_channels, out_channels};

    fdt::hw::Pointwise1x1Scalar(input_view, weights_view, bias.data(),
                                scalar_view);

    const fdt::hw::PackedPointwiseFilter scalar_filter =
        fdt::hw::PackPointwiseFilter(weights_view, bias.data(), 1);
    const fdt::hw::PackedPointwiseFilter hw_filter =
        fdt::hw::PackPointwiseFilter(weights_view, bias.data(),
                                     fdt::hw::HwFloatLanes());
    const fdt::hw::PackedPointwiseFilter intrinsics_filter =
        fdt::hw::PackPointwiseFilter(weights_view, bias.data(),
                                     fdt::hw::IntrinsicsFloatLanes());

    fdt::hw::Pointwise1x1PackedScalar(input_view, scalar_filter,
                                      packed_scalar_view);
    fdt::hw::Pointwise1x1PackedHw(input_view, hw_filter, packed_hw_view);
    fdt::hw::Pointwise1x1PackedIntrinsics(input_view, intrinsics_filter,
                                          packed_intrinsics_view);
    const fdt::hw::PointwisePlan plan_hw =
        fdt::hw::CreatePointwisePlanHw(weights_view, bias.data());
    const fdt::hw::PointwisePlan plan_intrinsics =
        fdt::hw::CreatePointwisePlanIntrinsics(weights_view, bias.data());
    fdt::hw::Pointwise1x1PlannedHw(input_view, weights_view, bias.data(),
                                   plan_hw, planned_hw_view);
    fdt::hw::Pointwise1x1PlannedIntrinsics(
        input_view, weights_view, bias.data(), plan_intrinsics,
        planned_intrinsics_view);

    CheckClose(scalar, packed_scalar, 1e-3f);
    CheckClose(scalar, packed_hw, 1e-3f);
    CheckClose(scalar, packed_intrinsics, 1e-3f);
    CheckClose(scalar, planned_hw, 1e-3f);
    CheckClose(scalar, planned_intrinsics, 1e-3f);
}

TEST_CASE("hw primitives match scalar reference") {
    std::vector<float> a(64);
    std::vector<float> b(64);
    std::vector<float> scalar(64);
    std::vector<float> hwy(64);
    Fill(a);
    Fill(b);
    Fill(scalar);
    hwy = scalar;

    CHECK(std::fabs(fdt::hw::DotProductScalar(a.data(), b.data(), 64) -
                    fdt::hw::DotProductHw(a.data(), b.data(), 64)) <= 1e-4f);
    CHECK(std::fabs(fdt::hw::DotProductScalar(a.data(), b.data(), 64) -
                    fdt::hw::DotProductIntrinsics(a.data(), b.data(), 64)) <=
          1e-4f);

    fdt::hw::MulAddScalar(a.data(), b.data(), scalar.data(), 64);
    fdt::hw::MulAddHw(a.data(), b.data(), hwy.data(), 64);
    CheckClose(scalar, hwy);
    Fill(hwy);
    fdt::hw::MulAddIntrinsics(a.data(), b.data(), hwy.data(), 64);
    CheckClose(scalar, hwy);

    fdt::hw::AddScalar(a.data(), b.data(), scalar.data(), 64);
    fdt::hw::AddHw(a.data(), b.data(), hwy.data(), 64);
    CheckClose(scalar, hwy);
    fdt::hw::AddIntrinsics(a.data(), b.data(), hwy.data(), 64);
    CheckClose(scalar, hwy);

    Fill(scalar);
    hwy = scalar;
    fdt::hw::ReluScalar(scalar.data(), 64);
    fdt::hw::ReluHw(hwy.data(), 64);
    CheckClose(scalar, hwy);
    Fill(hwy);
    fdt::hw::ReluIntrinsics(hwy.data(), 64);
    CheckClose(scalar, hwy);
}

TEST_CASE("hw pointwise and depthwise kernels match scalar reference") {
    const int rows = 5;
    const int cols = 7;
    const int channels = 16;
    const int out_channels = 32;
    const int input_step = channels;
    const int output_step = out_channels;

    std::vector<float> input(rows * cols * input_step);
    std::vector<float> weights_pw(out_channels * channels);
    std::vector<float> bias_pw(out_channels);
    std::vector<float> scalar_pw(rows * cols * output_step);
    std::vector<float> hwy_pw(rows * cols * output_step);
    Fill(input);
    Fill(weights_pw);
    Fill(bias_pw);

    const fdt::hw::ConstBlobView input_view{
        input.data(), rows, cols, channels, input_step};
    const fdt::hw::ConstBlobView weights_pw_view{
        weights_pw.data(), 1, out_channels, channels, channels};
    fdt::hw::BlobView scalar_pw_view{
        scalar_pw.data(), rows, cols, out_channels, output_step};
    fdt::hw::BlobView hwy_pw_view{
        hwy_pw.data(), rows, cols, out_channels, output_step};

    fdt::hw::Pointwise1x1Scalar(input_view, weights_pw_view, bias_pw.data(),
                                scalar_pw_view);
    fdt::hw::Pointwise1x1Hw(input_view, weights_pw_view, bias_pw.data(),
                            hwy_pw_view);
    CheckClose(scalar_pw, hwy_pw, 1e-3f);
    std::fill(hwy_pw.begin(), hwy_pw.end(), 0.0f);
    fdt::hw::Pointwise1x1Intrinsics(input_view, weights_pw_view, bias_pw.data(),
                                    hwy_pw_view);
    CheckClose(scalar_pw, hwy_pw, 1e-3f);

    const int dw_channels = 16;
    std::vector<float> weights_dw(9 * dw_channels);
    std::vector<float> bias_dw(dw_channels);
    std::vector<float> scalar_dw(rows * cols * dw_channels);
    std::vector<float> hwy_dw(rows * cols * dw_channels);
    Fill(weights_dw);
    Fill(bias_dw);
    const fdt::hw::ConstBlobView weights_dw_view{
        weights_dw.data(), 1, 9, dw_channels, dw_channels};
    fdt::hw::BlobView scalar_dw_view{
        scalar_dw.data(), rows, cols, dw_channels, dw_channels};
    fdt::hw::BlobView hwy_dw_view{
        hwy_dw.data(), rows, cols, dw_channels, dw_channels};

    fdt::hw::Depthwise3x3Scalar(input_view, weights_dw_view, bias_dw.data(),
                                scalar_dw_view);
    fdt::hw::Depthwise3x3Hw(input_view, weights_dw_view, bias_dw.data(),
                            hwy_dw_view);
    CheckClose(scalar_dw, hwy_dw, 1e-3f);
    std::fill(hwy_dw.begin(), hwy_dw.end(), 0.0f);
    fdt::hw::Depthwise3x3Intrinsics(input_view, weights_dw_view, bias_dw.data(),
                                    hwy_dw_view);
    CheckClose(scalar_dw, hwy_dw, 1e-3f);
}

TEST_CASE("packed pointwise kernels match scalar reference") {
    CheckPackedPointwiseCase(5, 7, 16, 32);
    CheckPackedPointwiseCase(5, 7, 64, 10);
}

TEST_CASE("pointwise strategy selector chooses packed only when useful") {
    CHECK(fdt::hw::ShouldUsePackedPointwise(16, 16));
    CHECK(fdt::hw::ShouldUsePackedPointwise(64, 64));
    CHECK_FALSE(fdt::hw::ShouldUsePackedPointwise(64, 10));
    CHECK_FALSE(fdt::hw::ShouldUsePackedPointwise(64, 4));
    CHECK_FALSE(fdt::hw::ShouldUsePackedPointwise(1, 1));
}

TEST_CASE("hw filter copies conv info and creates persistent pointwise plans") {
    const int rows = 5;
    const int cols = 7;
    const int channels = 16;
    const int out_channels = 32;

    std::vector<float> input(static_cast<size_t>(rows) * cols * channels);
    std::vector<float> weights(static_cast<size_t>(out_channels) * channels);
    std::vector<float> bias(out_channels);
    std::vector<float> scalar(static_cast<size_t>(rows) * cols * out_channels);
    std::vector<float> planned(static_cast<size_t>(rows) * cols * out_channels);
    Fill(input);
    Fill(weights);
    Fill(bias);

    const fdt::hw::HwConvInfo info{
        channels, out_channels, false, true, true, weights.data(), bias.data()};
    const fdt::hw::HwFilter filter(info);
    CHECK(filter.IsSupported());
    CHECK(filter.IsPointwise());
    CHECK_FALSE(filter.IsDepthwise());
    CHECK(filter.channels() == channels);
    CHECK(filter.num_filters() == out_channels);
    CHECK(filter.with_relu());
    CHECK(filter.HwPlan().strategy == fdt::hw::PointwiseStrategy::kPacked);
    CHECK(filter.HwPlan().packed.channels == channels);
    CHECK(filter.HwPlan().packed.out_channels == out_channels);

    const fdt::hw::ConstBlobView weights_view = filter.WeightsView();
    CHECK(weights_view.rows == 1);
    CHECK(weights_view.cols == out_channels);
    CHECK(weights_view.channels == channels);
    for (int oc = 0; oc < out_channels; ++oc) {
        for (int ic = 0; ic < channels; ++ic) {
            CHECK(fdt::hw::Ptr(weights_view, 0, oc)[ic] ==
                  weights[static_cast<size_t>(oc) * channels + ic]);
        }
    }
    for (int oc = 0; oc < out_channels; ++oc) {
        CHECK(filter.Biases()[oc] == bias[oc]);
    }

    const fdt::hw::ConstBlobView input_view{
        input.data(), rows, cols, channels, channels};
    fdt::hw::BlobView scalar_view{
        scalar.data(), rows, cols, out_channels, out_channels};
    fdt::hw::BlobView planned_view{
        planned.data(), rows, cols, out_channels, out_channels};

    fdt::hw::Pointwise1x1Scalar(input_view, weights_view, filter.Biases(),
                                scalar_view);
    fdt::hw::Pointwise1x1PlannedHw(input_view, weights_view, filter.Biases(),
                                   filter.HwPlan(), planned_view);
    CheckClose(scalar, planned, 1e-3f);
}

TEST_CASE("hw filter preserves depthwise layout") {
    const int channels = 16;
    std::vector<float> weights(9 * channels);
    std::vector<float> bias(channels);
    Fill(weights);
    Fill(bias);

    const fdt::hw::HwConvInfo info{
        channels, channels, true, false, true, weights.data(), bias.data()};
    const fdt::hw::HwFilter filter(info);
    CHECK(filter.IsSupported());
    CHECK_FALSE(filter.IsPointwise());
    CHECK(filter.IsDepthwise());
    CHECK(filter.HwPlan().strategy == fdt::hw::PointwiseStrategy::kPrimitive);

    const fdt::hw::ConstBlobView weights_view = filter.WeightsView();
    CHECK(weights_view.rows == 1);
    CHECK(weights_view.cols == 9);
    CHECK(weights_view.channels == channels);
    for (int kernel_index = 0; kernel_index < 9; ++kernel_index) {
        for (int channel = 0; channel < channels; ++channel) {
            CHECK(fdt::hw::Ptr(weights_view, 0, kernel_index)[channel] ==
                  weights[static_cast<size_t>(kernel_index) * channels +
                          channel]);
        }
    }
    for (int channel = 0; channel < channels; ++channel) {
        CHECK(filter.Biases()[channel] == bias[channel]);
    }
}

TEST_CASE("hw layer wrappers match scalar reference") {
    const int rows = 5;
    const int cols = 7;
    const int channels = 16;
    const int pointwise_out = 32;

    fdt::hw::HwBlob input(rows, cols, channels);
    FillBlob(input);

    std::vector<float> pointwise_weights(
        static_cast<size_t>(pointwise_out) * channels);
    std::vector<float> pointwise_bias(pointwise_out);
    std::vector<float> depthwise_weights(9 * pointwise_out);
    std::vector<float> depthwise_bias(pointwise_out);
    Fill(pointwise_weights);
    Fill(pointwise_bias);
    Fill(depthwise_weights);
    Fill(depthwise_bias);

    const fdt::hw::HwConvInfo pointwise_info{
        channels,
        pointwise_out,
        false,
        true,
        false,
        pointwise_weights.data(),
        pointwise_bias.data()};
    const fdt::hw::HwConvInfo depthwise_info{
        pointwise_out,
        pointwise_out,
        true,
        false,
        true,
        depthwise_weights.data(),
        depthwise_bias.data()};
    const fdt::hw::HwFilter pointwise(pointwise_info);
    const fdt::hw::HwFilter depthwise(depthwise_info);

    const fdt::hw::HwBlob scalar_pw =
        fdt::hw::ConvolutionScalar(input, pointwise);
    const fdt::hw::HwBlob hw_pw = fdt::hw::ConvolutionHw(input, pointwise);
    const fdt::hw::HwBlob intrinsics_pw =
        fdt::hw::ConvolutionIntrinsics(input, pointwise);
    CheckClose(scalar_pw.data(), hw_pw.data(), 1e-3f);
    CheckClose(scalar_pw.data(), intrinsics_pw.data(), 1e-3f);

    const fdt::hw::HwBlob scalar_dp =
        fdt::hw::ConvolutionScalar(scalar_pw, depthwise);
    const fdt::hw::HwBlob hw_dp = fdt::hw::ConvolutionHw(hw_pw, depthwise);
    const fdt::hw::HwBlob intrinsics_dp =
        fdt::hw::ConvolutionIntrinsics(intrinsics_pw, depthwise);
    CheckClose(scalar_dp.data(), hw_dp.data(), 1e-3f);
    CheckClose(scalar_dp.data(), intrinsics_dp.data(), 1e-3f);

    const fdt::hw::HwBlob pooled_scalar =
        fdt::hw::MaxPooling2x2S2Hw(scalar_dp);
    const fdt::hw::HwBlob pooled_hw = fdt::hw::MaxPooling2x2S2Hw(hw_dp);
    CheckClose(pooled_scalar.data(), pooled_hw.data(), 1e-3f);

    const fdt::hw::HwBlob added = fdt::hw::ElementAddHw(pooled_hw, pooled_hw);
    for (int row = 0; row < pooled_hw.rows(); ++row) {
        for (int col = 0; col < pooled_hw.cols(); ++col) {
            for (int ch = 0; ch < pooled_hw.channels(); ++ch) {
                CHECK(added.Ptr(row, col)[ch] ==
                      doctest::Approx(pooled_hw.Ptr(row, col)[ch] * 2.0f));
            }
        }
    }

    const fdt::hw::HwBlob upsampled = fdt::hw::UpsampleX2Hw(pooled_hw);
    CHECK(upsampled.rows() == pooled_hw.rows() * 2);
    CHECK(upsampled.cols() == pooled_hw.cols() * 2);
    CHECK(upsampled.channels() == pooled_hw.channels());
    for (int row = 0; row < pooled_hw.rows(); ++row) {
        for (int col = 0; col < pooled_hw.cols(); ++col) {
            for (int ch = 0; ch < pooled_hw.channels(); ++ch) {
                const float value = pooled_hw.Ptr(row, col)[ch];
                CHECK(upsampled.Ptr(row * 2, col * 2)[ch] ==
                      doctest::Approx(value));
                CHECK(upsampled.Ptr(row * 2, col * 2 + 1)[ch] ==
                      doctest::Approx(value));
                CHECK(upsampled.Ptr(row * 2 + 1, col * 2)[ch] ==
                      doctest::Approx(value));
                CHECK(upsampled.Ptr(row * 2 + 1, col * 2 + 1)[ch] ==
                      doctest::Approx(value));
            }
        }
    }
}

TEST_CASE("hw model imports the static 53-layer filter table") {
    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    CHECK(model.size() == fdt::hw::kHwConvLayerCount);

    const fdt::hw::HwFilter& first = model.Filter(0);
    CHECK(first.channels() == 32);
    CHECK(first.num_filters() == 16);
    CHECK(first.IsPointwise());
    CHECK(first.with_relu());
    CHECK(first.HwPlan().strategy == fdt::hw::PointwiseStrategy::kPacked);

    const fdt::hw::HwFilter& first_depthwise = model.Filter(2);
    CHECK(first_depthwise.channels() == 16);
    CHECK(first_depthwise.num_filters() == 16);
    CHECK(first_depthwise.IsDepthwise());

    const fdt::hw::HwFilter& tiny_head = model.Filter(34);
    CHECK(tiny_head.IsDepthwise());
    CHECK(tiny_head.num_filters() == 1);
}

TEST_CASE("hw image transform matches original initial blob") {
    const int width = 63;
    const int height = 47;
    const int channels = 3;
    const int step = width * channels;
    std::vector<unsigned char> image(static_cast<size_t>(height) * step);
    FillImage(image);

    const CDataBlob<float> expected = setDataFrom3x3S2P1to1x1S1P0FromImage(
        image.data(), width, height, channels, step);
    const fdt::hw::HwBlob actual = fdt::hw::ImageToInitialBlobHw(
        image.data(), width, height, channels, step);

    CheckBlobClose(expected, actual);
}

TEST_CASE("hw backbone matches original CDataBlob flow through conv5") {
    extern ConvInfoStruct param_pConvInfo[fdt::hw::kHwConvLayerCount];

    Filters<float> filters[fdt::hw::kHwConvLayerCount];
    for (int i = 0; i < fdt::hw::kHwConvLayerCount; ++i) {
        filters[i] = param_pConvInfo[i];
    }

    CDataBlob<float> original_input(48, 64, 32);
    FillCDataBlob(original_input);
    fdt::hw::HwBlob hw_input(48, 64, 32);
    FillBlobValid(hw_input);

    CDataBlob<float> fx = convolution(original_input, filters[0]);
    fx = convolutionDP(fx, filters[1], filters[2]);
    fx = maxpooling2x2S2(fx);
    fx = convolution4layerUnit(fx, filters[3], filters[4], filters[5],
                               filters[6]);
    fx = convolution4layerUnit(fx, filters[7], filters[8], filters[9],
                               filters[10]);
    fx = maxpooling2x2S2(fx);
    CDataBlob<float> fb1 =
        convolution4layerUnit(fx, filters[11], filters[12], filters[13],
                              filters[14]);
    fx = maxpooling2x2S2(fb1);
    CDataBlob<float> fb2 =
        convolution4layerUnit(fx, filters[15], filters[16], filters[17],
                              filters[18]);
    fx = maxpooling2x2S2(fb2);
    CDataBlob<float> fb3 =
        convolution4layerUnit(fx, filters[19], filters[20], filters[21],
                              filters[22]);

    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    const fdt::hw::HwBackboneOutputs outputs =
        fdt::hw::ForwardBackboneHw(hw_input, model);

    CheckBlobClose(fb1, outputs.fb1, 2e-3f);
    CheckBlobClose(fb2, outputs.fb2, 2e-3f);
    CheckBlobClose(fb3, outputs.fb3, 2e-3f);
}

TEST_CASE("hw raw heads match original CDataBlob branch flow") {
    extern ConvInfoStruct param_pConvInfo[fdt::hw::kHwConvLayerCount];

    Filters<float> filters[fdt::hw::kHwConvLayerCount];
    for (int i = 0; i < fdt::hw::kHwConvLayerCount; ++i) {
        filters[i] = param_pConvInfo[i];
    }

    CDataBlob<float> original_input(48, 64, 32);
    FillCDataBlob(original_input);
    fdt::hw::HwBlob hw_input(48, 64, 32);
    FillBlobValid(hw_input);

    CDataBlob<float> fx = convolution(original_input, filters[0]);
    fx = convolutionDP(fx, filters[1], filters[2]);
    fx = maxpooling2x2S2(fx);
    fx = convolution4layerUnit(fx, filters[3], filters[4], filters[5],
                               filters[6]);
    fx = convolution4layerUnit(fx, filters[7], filters[8], filters[9],
                               filters[10]);
    fx = maxpooling2x2S2(fx);
    CDataBlob<float> fb1 =
        convolution4layerUnit(fx, filters[11], filters[12], filters[13],
                              filters[14]);
    fx = maxpooling2x2S2(fb1);
    CDataBlob<float> fb2 =
        convolution4layerUnit(fx, filters[15], filters[16], filters[17],
                              filters[18]);
    fx = maxpooling2x2S2(fb2);
    CDataBlob<float> fb3 =
        convolution4layerUnit(fx, filters[19], filters[20], filters[21],
                              filters[22]);

    CDataBlob<float> pred_reg[3], pred_cls[3], pred_kps[3], pred_obj[3];
    fb3 = convolutionDP(fb3, filters[27], filters[28]);
    pred_cls[2] = convolutionDP(fb3, filters[33], filters[34], false);
    pred_reg[2] = convolutionDP(fb3, filters[39], filters[40], false);
    pred_kps[2] = convolutionDP(fb3, filters[51], filters[52], false);
    pred_obj[2] = convolutionDP(fb3, filters[45], filters[46], false);

    fb2 = elementAdd(upsampleX2(fb3), fb2);
    fb2 = convolutionDP(fb2, filters[25], filters[26]);
    pred_cls[1] = convolutionDP(fb2, filters[31], filters[32], false);
    pred_reg[1] = convolutionDP(fb2, filters[37], filters[38], false);
    pred_kps[1] = convolutionDP(fb2, filters[49], filters[50], false);
    pred_obj[1] = convolutionDP(fb2, filters[43], filters[44], false);

    fb1 = elementAdd(upsampleX2(fb2), fb1);
    fb1 = convolutionDP(fb1, filters[23], filters[24]);
    pred_cls[0] = convolutionDP(fb1, filters[29], filters[30], false);
    pred_reg[0] = convolutionDP(fb1, filters[35], filters[36], false);
    pred_kps[0] = convolutionDP(fb1, filters[47], filters[48], false);
    pred_obj[0] = convolutionDP(fb1, filters[41], filters[42], false);

    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    const fdt::hw::HwHeadOutputs outputs =
        fdt::hw::ForwardNetworkHw(hw_input, model);

    for (int level = 0; level < 3; ++level) {
        CheckBlobClose(pred_cls[level], outputs.cls[level], 3e-3f);
        CheckBlobClose(pred_reg[level], outputs.reg[level], 3e-3f);
        CheckBlobClose(pred_kps[level], outputs.kps[level], 3e-3f);
        CheckBlobClose(pred_obj[level], outputs.obj[level], 3e-3f);
    }
}

TEST_CASE("hw decoded and concatenated tensors match original flow") {
    extern ConvInfoStruct param_pConvInfo[fdt::hw::kHwConvLayerCount];

    Filters<float> filters[fdt::hw::kHwConvLayerCount];
    for (int i = 0; i < fdt::hw::kHwConvLayerCount; ++i) {
        filters[i] = param_pConvInfo[i];
    }

    CDataBlob<float> original_input(48, 64, 32);
    FillCDataBlob(original_input);
    fdt::hw::HwBlob hw_input(48, 64, 32);
    FillBlobValid(hw_input);

    CDataBlob<float> fx = convolution(original_input, filters[0]);
    fx = convolutionDP(fx, filters[1], filters[2]);
    fx = maxpooling2x2S2(fx);
    fx = convolution4layerUnit(fx, filters[3], filters[4], filters[5],
                               filters[6]);
    fx = convolution4layerUnit(fx, filters[7], filters[8], filters[9],
                               filters[10]);
    fx = maxpooling2x2S2(fx);
    CDataBlob<float> fb1 =
        convolution4layerUnit(fx, filters[11], filters[12], filters[13],
                              filters[14]);
    fx = maxpooling2x2S2(fb1);
    CDataBlob<float> fb2 =
        convolution4layerUnit(fx, filters[15], filters[16], filters[17],
                              filters[18]);
    fx = maxpooling2x2S2(fb2);
    CDataBlob<float> fb3 =
        convolution4layerUnit(fx, filters[19], filters[20], filters[21],
                              filters[22]);

    CDataBlob<float> pred_reg[3], pred_cls[3], pred_kps[3], pred_obj[3];
    fb3 = convolutionDP(fb3, filters[27], filters[28]);
    pred_cls[2] = convolutionDP(fb3, filters[33], filters[34], false);
    pred_reg[2] = convolutionDP(fb3, filters[39], filters[40], false);
    pred_kps[2] = convolutionDP(fb3, filters[51], filters[52], false);
    pred_obj[2] = convolutionDP(fb3, filters[45], filters[46], false);

    fb2 = elementAdd(upsampleX2(fb3), fb2);
    fb2 = convolutionDP(fb2, filters[25], filters[26]);
    pred_cls[1] = convolutionDP(fb2, filters[31], filters[32], false);
    pred_reg[1] = convolutionDP(fb2, filters[37], filters[38], false);
    pred_kps[1] = convolutionDP(fb2, filters[49], filters[50], false);
    pred_obj[1] = convolutionDP(fb2, filters[43], filters[44], false);

    fb1 = elementAdd(upsampleX2(fb2), fb1);
    fb1 = convolutionDP(fb1, filters[23], filters[24]);
    pred_cls[0] = convolutionDP(fb1, filters[29], filters[30], false);
    pred_reg[0] = convolutionDP(fb1, filters[35], filters[36], false);
    pred_kps[0] = convolutionDP(fb1, filters[47], filters[48], false);
    pred_obj[0] = convolutionDP(fb1, filters[41], filters[42], false);

    auto prior3 = meshgrid(fb1.cols, fb1.rows, 8);
    auto prior4 = meshgrid(fb2.cols, fb2.rows, 16);
    auto prior5 = meshgrid(fb3.cols, fb3.rows, 32);
    bbox_decode(pred_reg[0], prior3, 8);
    bbox_decode(pred_reg[1], prior4, 16);
    bbox_decode(pred_reg[2], prior5, 32);
    kps_decode(pred_kps[0], prior3, 8);
    kps_decode(pred_kps[1], prior4, 16);
    kps_decode(pred_kps[2], prior5, 32);

    auto cls = concat3(blob2vector(pred_cls[0]), blob2vector(pred_cls[1]),
                       blob2vector(pred_cls[2]));
    auto reg = concat3(blob2vector(pred_reg[0]), blob2vector(pred_reg[1]),
                       blob2vector(pred_reg[2]));
    auto kps = concat3(blob2vector(pred_kps[0]), blob2vector(pred_kps[1]),
                       blob2vector(pred_kps[2]));
    auto obj = concat3(blob2vector(pred_obj[0]), blob2vector(pred_obj[1]),
                       blob2vector(pred_obj[2]));
    sigmoid(cls);
    sigmoid(obj);

    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    const fdt::hw::HwDecodedOutputs hw_outputs =
        fdt::hw::DecodeAndConcatHw(fdt::hw::ForwardNetworkHw(hw_input, model));

    CheckBlobClose(cls, hw_outputs.cls, 3e-3f);
    CheckBlobClose(reg, hw_outputs.reg, 5e-2f);
    CheckBlobClose(kps, hw_outputs.kps, 5e-2f);
    CheckBlobClose(obj, hw_outputs.obj, 3e-3f);
}

TEST_CASE("hw detection output applies confidence, sorting and nms") {
    fdt::hw::HwBlob cls(1, 1, 3);
    fdt::hw::HwBlob reg(1, 1, 12);
    fdt::hw::HwBlob kps(1, 1, 30);
    fdt::hw::HwBlob obj(1, 1, 3);

    cls.Ptr(0, 0)[0] = 0.81f;
    cls.Ptr(0, 0)[1] = 0.64f;
    cls.Ptr(0, 0)[2] = 0.04f;
    obj.Ptr(0, 0)[0] = 1.0f;
    obj.Ptr(0, 0)[1] = 1.0f;
    obj.Ptr(0, 0)[2] = 1.0f;

    const float boxes[12] = {
        10.0f, 10.0f, 30.0f, 30.0f,
        12.0f, 12.0f, 31.0f, 31.0f,
        80.0f, 80.0f, 90.0f, 90.0f};
    std::copy(boxes, boxes + 12, reg.Ptr(0, 0));
    for (int i = 0; i < 30; ++i) {
        kps.Ptr(0, 0)[i] = static_cast<float>(i);
    }

    const std::vector<fdt::hw::HwFaceRect> faces = fdt::hw::DetectionOutputHw(
        cls, reg, kps, obj, 0.45f, 0.5f, 100, 100);

    REQUIRE(faces.size() == 1);
    CHECK(faces[0].score == doctest::Approx(0.9f));
    CHECK(faces[0].x == 10);
    CHECK(faces[0].y == 10);
    CHECK(faces[0].w == 20);
    CHECK(faces[0].h == 20);
    CHECK(faces[0].lm[0] == 0);
    CHECK(faces[0].lm[9] == 9);
}

TEST_CASE("public hw cnn api writes a result buffer") {
    const int width = 96;
    const int height = 96;
    const int channels = 3;
    const int step = width * channels;
    std::vector<unsigned char> image(static_cast<size_t>(height) * step);
    FillImage(image);
    std::vector<unsigned char> result(FACEDETECTION_HW_RESULT_BUFFER_SIZE);

    int* count = facedetect_hw_cnn(result.data(), image.data(), width, height,
                                   step);

    REQUIRE(count == reinterpret_cast<int*>(result.data()));
    CHECK(count[0] >= 0);
    CHECK(count[0] <= FACEDETECTION_HW_RESULT_MAX_FACES);
}

TEST_CASE("hw image-to-decoded tensor path matches original flow") {
    extern ConvInfoStruct param_pConvInfo[fdt::hw::kHwConvLayerCount];

    Filters<float> filters[fdt::hw::kHwConvLayerCount];
    for (int i = 0; i < fdt::hw::kHwConvLayerCount; ++i) {
        filters[i] = param_pConvInfo[i];
    }

    const int width = 96;
    const int height = 96;
    const int channels = 3;
    const int step = width * channels;
    std::vector<unsigned char> image(static_cast<size_t>(height) * step);
    FillImage(image);

    CDataBlob<float> fx = setDataFrom3x3S2P1to1x1S1P0FromImage(
        image.data(), width, height, channels, step);
    fx = convolution(fx, filters[0]);
    fx = convolutionDP(fx, filters[1], filters[2]);
    fx = maxpooling2x2S2(fx);
    fx = convolution4layerUnit(fx, filters[3], filters[4], filters[5],
                               filters[6]);
    fx = convolution4layerUnit(fx, filters[7], filters[8], filters[9],
                               filters[10]);
    fx = maxpooling2x2S2(fx);
    CDataBlob<float> fb1 =
        convolution4layerUnit(fx, filters[11], filters[12], filters[13],
                              filters[14]);
    fx = maxpooling2x2S2(fb1);
    CDataBlob<float> fb2 =
        convolution4layerUnit(fx, filters[15], filters[16], filters[17],
                              filters[18]);
    fx = maxpooling2x2S2(fb2);
    CDataBlob<float> fb3 =
        convolution4layerUnit(fx, filters[19], filters[20], filters[21],
                              filters[22]);

    CDataBlob<float> pred_reg[3], pred_cls[3], pred_kps[3], pred_obj[3];
    fb3 = convolutionDP(fb3, filters[27], filters[28]);
    pred_cls[2] = convolutionDP(fb3, filters[33], filters[34], false);
    pred_reg[2] = convolutionDP(fb3, filters[39], filters[40], false);
    pred_kps[2] = convolutionDP(fb3, filters[51], filters[52], false);
    pred_obj[2] = convolutionDP(fb3, filters[45], filters[46], false);

    fb2 = elementAdd(upsampleX2(fb3), fb2);
    fb2 = convolutionDP(fb2, filters[25], filters[26]);
    pred_cls[1] = convolutionDP(fb2, filters[31], filters[32], false);
    pred_reg[1] = convolutionDP(fb2, filters[37], filters[38], false);
    pred_kps[1] = convolutionDP(fb2, filters[49], filters[50], false);
    pred_obj[1] = convolutionDP(fb2, filters[43], filters[44], false);

    fb1 = elementAdd(upsampleX2(fb2), fb1);
    fb1 = convolutionDP(fb1, filters[23], filters[24]);
    pred_cls[0] = convolutionDP(fb1, filters[29], filters[30], false);
    pred_reg[0] = convolutionDP(fb1, filters[35], filters[36], false);
    pred_kps[0] = convolutionDP(fb1, filters[47], filters[48], false);
    pred_obj[0] = convolutionDP(fb1, filters[41], filters[42], false);

    auto prior3 = meshgrid(fb1.cols, fb1.rows, 8);
    auto prior4 = meshgrid(fb2.cols, fb2.rows, 16);
    auto prior5 = meshgrid(fb3.cols, fb3.rows, 32);
    bbox_decode(pred_reg[0], prior3, 8);
    bbox_decode(pred_reg[1], prior4, 16);
    bbox_decode(pred_reg[2], prior5, 32);
    kps_decode(pred_kps[0], prior3, 8);
    kps_decode(pred_kps[1], prior4, 16);
    kps_decode(pred_kps[2], prior5, 32);

    auto cls = concat3(blob2vector(pred_cls[0]), blob2vector(pred_cls[1]),
                       blob2vector(pred_cls[2]));
    auto reg = concat3(blob2vector(pred_reg[0]), blob2vector(pred_reg[1]),
                       blob2vector(pred_reg[2]));
    auto kps = concat3(blob2vector(pred_kps[0]), blob2vector(pred_kps[1]),
                       blob2vector(pred_kps[2]));
    auto obj = concat3(blob2vector(pred_obj[0]), blob2vector(pred_obj[1]),
                       blob2vector(pred_obj[2]));
    sigmoid(cls);
    sigmoid(obj);

    const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    const fdt::hw::HwBlob hw_initial = fdt::hw::ImageToInitialBlobHw(
        image.data(), width, height, channels, step);
    const fdt::hw::HwDecodedOutputs hw_outputs =
        fdt::hw::DecodeAndConcatHw(
            fdt::hw::ForwardNetworkHw(hw_initial, model));

    CheckBlobClose(cls, hw_outputs.cls, 3e-3f);
    CheckBlobClose(reg, hw_outputs.reg, 5e-2f);
    CheckBlobClose(kps, hw_outputs.kps, 5e-2f);
    CheckBlobClose(obj, hw_outputs.obj, 3e-3f);
}

TEST_CASE("hw maxpool matches scalar reference") {
    const int rows = 8;
    const int cols = 10;
    const int channels = 32;
    const int output_rows = 4;
    const int output_cols = 5;

    std::vector<float> input(rows * cols * channels);
    std::vector<float> scalar(output_rows * output_cols * channels);
    std::vector<float> hwy(output_rows * output_cols * channels);
    Fill(input);

    const fdt::hw::ConstBlobView input_view{
        input.data(), rows, cols, channels, channels};
    fdt::hw::BlobView scalar_view{
        scalar.data(), output_rows, output_cols, channels, channels};
    fdt::hw::BlobView hwy_view{
        hwy.data(), output_rows, output_cols, channels, channels};

    fdt::hw::MaxPool2x2S2Scalar(input_view, scalar_view);
    fdt::hw::MaxPool2x2S2Hw(input_view, hwy_view);
    CheckClose(scalar, hwy);
    std::fill(hwy.begin(), hwy.end(), 0.0f);
    fdt::hw::MaxPool2x2S2Intrinsics(input_view, hwy_view);
    CheckClose(scalar, hwy);
}
