#include "hw_layers.h"

#include <algorithm>
#include <cmath>
#include <cstring>

namespace fdt {
namespace hw {

namespace {

void ApplyReluHw(HwBlob& blob) {
    ReluHw(blob.data().data(), static_cast<int>(blob.data().size()));
}

void ApplyReluIntrinsics(HwBlob& blob) {
    ReluIntrinsics(blob.data().data(), static_cast<int>(blob.data().size()));
}

void ApplyReluScalar(HwBlob& blob) {
    ReluScalar(blob.data().data(), static_cast<int>(blob.data().size()));
}

int MaxPoolOutputSize(int input_size) {
    return static_cast<int>(std::ceil((input_size - 3.0f) / 2.0f)) + 1;
}

void ClearPadding(BlobView& blob) {
    if (blob.channel_step == blob.channels) {
        return;
    }
    const int padding = blob.channel_step - blob.channels;
    const size_t padding_bytes = static_cast<size_t>(padding) * sizeof(float);
    for (int row = 0; row < blob.rows; ++row) {
        for (int col = 0; col < blob.cols; ++col) {
            std::memset(Ptr(blob, row, col) + blob.channels, 0, padding_bytes);
        }
    }
}

}  // namespace

HwBlob ConvolutionScalar(const HwBlob& input, const HwFilter& filter) {
    return ConvolutionScalar(input, filter, filter.with_relu());
}

HwBlob ConvolutionScalar(const HwBlob& input,
                         const HwFilter& filter,
                         bool do_relu) {
    HwBlob output(input.rows(), input.cols(), filter.num_filters());
    BlobView output_view = output.View();
    if (filter.IsPointwise()) {
        Pointwise1x1Scalar(input.View(), filter.WeightsView(), filter.Biases(),
                           output_view);
    } else if (filter.IsDepthwise()) {
        Depthwise3x3Scalar(input.View(), filter.WeightsView(), filter.Biases(),
                           output_view);
    }
    if (do_relu) {
        ApplyReluScalar(output);
    }
    return output;
}

HwBlob ConvolutionHw(const HwBlob& input, const HwFilter& filter) {
    return ConvolutionHw(input, filter, filter.with_relu());
}

HwBlob ConvolutionHw(const HwBlob& input, const HwFilter& filter, bool do_relu) {
    HwBlob output(input.rows(), input.cols(), filter.num_filters());
    ConvolutionHwTo(input, filter, do_relu, &output);
    return output;
}

void ConvolutionHwTo(const HwBlob& input,
                     const HwFilter& filter,
                     bool do_relu,
                     HwBlob* output) {
    output->ResizeForOverwrite(input.rows(), input.cols(), filter.num_filters());
    BlobView output_view = output->View();
    if (filter.IsPointwise()) {
        if (do_relu) {
            Pointwise1x1PlannedHwRelu(input.View(), filter.WeightsView(),
                                      filter.Biases(), filter.HwPlan(),
                                      output_view);
            ClearPadding(output_view);
            return;
        }
        Pointwise1x1PlannedHw(input.View(), filter.WeightsView(),
                              filter.Biases(), filter.HwPlan(), output_view);
        ClearPadding(output_view);
    } else if (filter.IsDepthwise()) {
#if defined(FDT_HW_ENABLE_HYBRID_CEILING)
        if (do_relu) {
            Depthwise3x3IntrinsicsRelu(input.View(), filter.WeightsView(),
                                       filter.Biases(), output_view);
            return;
        }
        Depthwise3x3Intrinsics(input.View(), filter.WeightsView(),
                               filter.Biases(), output_view);
#else
        if (do_relu) {
            Depthwise3x3HwRelu(input.View(), filter.WeightsView(),
                               filter.Biases(), output_view);
            return;
        }
        Depthwise3x3Hw(input.View(), filter.WeightsView(), filter.Biases(),
                       output_view);
#endif
    }
    if (do_relu) {
        ApplyReluHw(*output);
    }
}

HwBlob ConvolutionIntrinsics(const HwBlob& input, const HwFilter& filter) {
    return ConvolutionIntrinsics(input, filter, filter.with_relu());
}

HwBlob ConvolutionIntrinsics(const HwBlob& input,
                             const HwFilter& filter,
                             bool do_relu) {
    HwBlob output(input.rows(), input.cols(), filter.num_filters());
    BlobView output_view = output.View();
    if (filter.IsPointwise()) {
        Pointwise1x1PlannedIntrinsics(input.View(), filter.WeightsView(),
                                      filter.Biases(),
                                      filter.IntrinsicsPlan(), output_view);
    } else if (filter.IsDepthwise()) {
        if (do_relu) {
            Depthwise3x3IntrinsicsRelu(input.View(), filter.WeightsView(),
                                       filter.Biases(), output_view);
            return output;
        }
        Depthwise3x3Intrinsics(input.View(), filter.WeightsView(),
                               filter.Biases(), output_view);
    }
    if (do_relu) {
        ApplyReluIntrinsics(output);
    }
    return output;
}

HwBlob ConvolutionDepthwisePointwiseHw(const HwBlob& input,
                                       const HwFilter& pointwise,
                                       const HwFilter& depthwise) {
    return ConvolutionDepthwisePointwiseHw(input, pointwise, depthwise,
                                           depthwise.with_relu());
}

HwBlob ConvolutionDepthwisePointwiseHw(const HwBlob& input,
                                       const HwFilter& pointwise,
                                       const HwFilter& depthwise,
                                       bool do_relu) {
    HwBlob tmp = ConvolutionHw(input, pointwise, false);
    return ConvolutionHw(tmp, depthwise, do_relu);
}

void ConvolutionDepthwisePointwiseHwTo(const HwBlob& input,
                                       const HwFilter& pointwise,
                                       const HwFilter& depthwise,
                                       bool do_relu,
                                       HwBlob* pointwise_workspace,
                                       HwBlob* output) {
    ConvolutionHwTo(input, pointwise, false, pointwise_workspace);
    ConvolutionHwTo(*pointwise_workspace, depthwise, do_relu, output);
}

HwBlob MaxPooling2x2S2Hw(const HwBlob& input) {
    HwBlob output(MaxPoolOutputSize(input.rows()), MaxPoolOutputSize(input.cols()),
                  input.channels());
    MaxPooling2x2S2HwTo(input, &output);
    return output;
}

void MaxPooling2x2S2HwTo(const HwBlob& input, HwBlob* output) {
    output->ResizeForOverwrite(MaxPoolOutputSize(input.rows()),
                               MaxPoolOutputSize(input.cols()),
                               input.channels());
    BlobView output_view = output->View();
#if defined(FDT_HW_ENABLE_HYBRID_CEILING)
    MaxPool2x2S2Intrinsics(input.View(), output_view);
#else
    MaxPool2x2S2Hw(input.View(), output_view);
#endif
}

HwBlob ElementAddHw(const HwBlob& lhs, const HwBlob& rhs) {
    HwBlob output(lhs.rows(), lhs.cols(), lhs.channels());
    for (int row = 0; row < lhs.rows(); ++row) {
        for (int col = 0; col < lhs.cols(); ++col) {
            AddHw(lhs.Ptr(row, col), rhs.Ptr(row, col), output.Ptr(row, col),
                  lhs.channels());
        }
    }
    return output;
}

HwBlob UpsampleX2Hw(const HwBlob& input) {
    HwBlob output(input.rows() * 2, input.cols() * 2, input.channels());
    const size_t bytes = static_cast<size_t>(input.channels()) * sizeof(float);
    for (int row = 0; row < input.rows(); ++row) {
        for (int col = 0; col < input.cols(); ++col) {
            const float* in = input.Ptr(row, col);
            const int out_row = row * 2;
            const int out_col = col * 2;
            std::memcpy(output.Ptr(out_row, out_col), in, bytes);
            std::memcpy(output.Ptr(out_row, out_col + 1), in, bytes);
            std::memcpy(output.Ptr(out_row + 1, out_col), in, bytes);
            std::memcpy(output.Ptr(out_row + 1, out_col + 1), in, bytes);
        }
    }
    return output;
}

HwBlob UpsampleX2AddHw(const HwBlob& input, const HwBlob& lateral) {
    HwBlob output(lateral.rows(), lateral.cols(), lateral.channels());
    UpsampleX2AddHwTo(input, lateral, &output);
    return output;
}

void UpsampleX2AddHwTo(const HwBlob& input,
                       const HwBlob& lateral,
                       HwBlob* output) {
    output->ResizeForOverwrite(lateral.rows(), lateral.cols(),
                               lateral.channels());
    for (int row = 0; row < input.rows(); ++row) {
        for (int col = 0; col < input.cols(); ++col) {
            const float* in = input.Ptr(row, col);
            const int out_row = row * 2;
            const int out_col = col * 2;
            AddHw(in, lateral.Ptr(out_row, out_col),
                  output->Ptr(out_row, out_col), input.channels());
            AddHw(in, lateral.Ptr(out_row, out_col + 1),
                  output->Ptr(out_row, out_col + 1), input.channels());
            AddHw(in, lateral.Ptr(out_row + 1, out_col),
                  output->Ptr(out_row + 1, out_col), input.channels());
            AddHw(in, lateral.Ptr(out_row + 1, out_col + 1),
                  output->Ptr(out_row + 1, out_col + 1), input.channels());
        }
    }
}

}  // namespace hw
}  // namespace fdt
