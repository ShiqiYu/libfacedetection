#include "hw_kernels.h"

#include <algorithm>
#include <cstring>

namespace fdt {
namespace hw {

PackedPointwiseFilter PackPointwiseFilter(const ConstBlobView& weights,
                                          const float* biases,
                                          int lane_count) {
    const int padded_out_channels =
        ((weights.cols + lane_count - 1) / lane_count) * lane_count;
    PackedPointwiseFilter packed;
    packed.channels = weights.channels;
    packed.out_channels = weights.cols;
    packed.padded_out_channels = padded_out_channels;
    packed.weights.assign(static_cast<size_t>(weights.channels) *
                              padded_out_channels,
                          0.0f);
    packed.biases.assign(static_cast<size_t>(padded_out_channels), 0.0f);

    for (int oc = 0; oc < weights.cols; ++oc) {
        packed.biases[oc] = biases[oc];
        const float* src = Ptr(weights, 0, oc);
        for (int ic = 0; ic < weights.channels; ++ic) {
            packed.weights[static_cast<size_t>(ic) * padded_out_channels + oc] =
                src[ic];
        }
    }
    return packed;
}

bool ShouldUsePackedPointwise(int channels, int out_channels) {
    return channels >= 16 && out_channels >= 16;
}

PointwisePlan CreatePointwisePlanHw(const ConstBlobView& weights,
                                    const float* biases) {
    PointwisePlan plan;
    plan.strategy = ShouldUsePackedPointwise(weights.channels, weights.cols)
                        ? PointwiseStrategy::kPacked
                        : PointwiseStrategy::kPrimitive;
    if (plan.strategy == PointwiseStrategy::kPacked) {
        plan.packed = PackPointwiseFilter(weights, biases, HwFloatLanes());
    }
    return plan;
}

PointwisePlan CreatePointwisePlanIntrinsics(const ConstBlobView& weights,
                                            const float* biases) {
    PointwisePlan plan;
    plan.strategy = ShouldUsePackedPointwise(weights.channels, weights.cols)
                        ? PointwiseStrategy::kPacked
                        : PointwiseStrategy::kPrimitive;
    if (plan.strategy == PointwiseStrategy::kPacked) {
        plan.packed =
            PackPointwiseFilter(weights, biases, IntrinsicsFloatLanes());
    }
    return plan;
}

float DotProductScalar(const float* a, const float* b, int n) {
    float sum = 0.0f;
    for (int i = 0; i < n; ++i) {
        sum += a[i] * b[i];
    }
    return sum;
}

void MulAddScalar(const float* a, const float* b, float* acc, int n) {
    for (int i = 0; i < n; ++i) {
        acc[i] += a[i] * b[i];
    }
}

void AddScalar(const float* a, const float* b, float* out, int n) {
    for (int i = 0; i < n; ++i) {
        out[i] = a[i] + b[i];
    }
}

void AddInplaceScalar(const float* a, float* out, int n) {
    for (int i = 0; i < n; ++i) {
        out[i] += a[i];
    }
}

void ReluScalar(float* data, int n) {
    for (int i = 0; i < n; ++i) {
        data[i] = std::max(data[i], 0.0f);
    }
}

void Pointwise1x1Scalar(const ConstBlobView& input,
                        const ConstBlobView& weights,
                        const float* biases,
                        BlobView& output) {
    for (int row = 0; row < output.rows; ++row) {
        for (int col = 0; col < output.cols; ++col) {
            const float* in = Ptr(input, row, col);
            float* out = Ptr(output, row, col);
            for (int ch = 0; ch < output.channels; ++ch) {
                const float* filter = Ptr(weights, 0, ch);
                out[ch] = DotProductScalar(in, filter, input.channels) + biases[ch];
            }
        }
    }
}

void Pointwise1x1PackedScalar(const ConstBlobView& input,
                              const PackedPointwiseFilter& filter,
                              BlobView& output) {
    for (int row = 0; row < output.rows; ++row) {
        for (int col = 0; col < output.cols; ++col) {
            const float* in = Ptr(input, row, col);
            float* out = Ptr(output, row, col);
            for (int oc = 0; oc < filter.out_channels; ++oc) {
                float sum = filter.biases[oc];
                for (int ic = 0; ic < filter.channels; ++ic) {
                    sum += in[ic] *
                           filter.weights[static_cast<size_t>(ic) *
                                              filter.padded_out_channels +
                                          oc];
                }
                out[oc] = sum;
            }
        }
    }
}

void Depthwise3x3Scalar(const ConstBlobView& input,
                        const ConstBlobView& weights,
                        const float* biases,
                        BlobView& output) {
    const size_t output_bytes =
        static_cast<size_t>(output.rows) * output.cols * output.channel_step *
        sizeof(float);
    std::memset(output.data, 0, output_bytes);

    for (int row = 0; row < output.rows; ++row) {
        const int srcy_start = std::max(0, row - 1);
        const int srcy_end = std::min(row + 2, input.rows);
        for (int col = 0; col < output.cols; ++col) {
            const int srcx_start = std::max(0, col - 1);
            const int srcx_end = std::min(col + 2, input.cols);
            float* out = Ptr(output, row, col);
            for (int r = srcy_start; r < srcy_end; ++r) {
                for (int c = srcx_start; c < srcx_end; ++c) {
                    const int filter_r = r - row + 1;
                    const int filter_c = c - col + 1;
                    const int filter_idx = filter_r * 3 + filter_c;
                    MulAddScalar(Ptr(input, r, c), Ptr(weights, 0, filter_idx),
                                 out, output.channels);
                }
            }
            AddInplaceScalar(biases, out, output.channels);
        }
    }
}

void MaxPool2x2S2Scalar(const ConstBlobView& input, BlobView& output) {
    for (int row = 0; row < output.rows; ++row) {
        for (int col = 0; col < output.cols; ++col) {
            const int rstart = row * 2;
            const int cstart = col * 2;
            const int rend = std::min(rstart + 2, input.rows);
            const int cend = std::min(cstart + 2, input.cols);
            float* out = Ptr(output, row, col);
            for (int ch = 0; ch < output.channels; ++ch) {
                float max_value = Ptr(input, rstart, cstart)[ch];
                for (int r = rstart; r < rend; ++r) {
                    for (int c = cstart; c < cend; ++c) {
                        max_value = std::max(max_value, Ptr(input, r, c)[ch]);
                    }
                }
                out[ch] = max_value;
            }
        }
    }
}

}  // namespace hw
}  // namespace fdt
