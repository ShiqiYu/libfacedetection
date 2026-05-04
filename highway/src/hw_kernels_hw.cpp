#include "hw_kernels.h"

#include <algorithm>
#include <cstring>

#include "hwy/highway.h"

namespace fdt {
namespace hw {
namespace {

namespace hn = hwy::HWY_NAMESPACE;

template <typename D>
void MulAddLoop(D d, const float* a, const float* b, float* acc, int n) {
    const int lanes = static_cast<int>(hn::Lanes(d));
    int i = 0;
    for (; i + lanes <= n; i += lanes) {
        const auto va = hn::Load(d, a + i);
        const auto vb = hn::Load(d, b + i);
        const auto vacc = hn::Load(d, acc + i);
        hn::Store(hn::MulAdd(va, vb, vacc), d, acc + i);
    }
    for (; i < n; ++i) {
        acc[i] += a[i] * b[i];
    }
}

}  // namespace

int HwFloatLanes() {
    const hn::ScalableTag<float> d;
    return static_cast<int>(hn::Lanes(d));
}

float DotProductHw(const float* a, const float* b, int n) {
    const hn::ScalableTag<float> d;
    const int lanes = static_cast<int>(hn::Lanes(d));
    auto sum = hn::Zero(d);
    int i = 0;
    for (; i + lanes <= n; i += lanes) {
        sum = hn::MulAdd(hn::Load(d, a + i), hn::Load(d, b + i), sum);
    }
    float result = hn::ReduceSum(d, sum);
    for (; i < n; ++i) {
        result += a[i] * b[i];
    }
    return result;
}

void MulAddHw(const float* a, const float* b, float* acc, int n) {
    const hn::ScalableTag<float> d;
    MulAddLoop(d, a, b, acc, n);
}

void AddHw(const float* a, const float* b, float* out, int n) {
    const hn::ScalableTag<float> d;
    const int lanes = static_cast<int>(hn::Lanes(d));
    int i = 0;
    for (; i + lanes <= n; i += lanes) {
        hn::Store(hn::Add(hn::Load(d, a + i), hn::Load(d, b + i)), d, out + i);
    }
    for (; i < n; ++i) {
        out[i] = a[i] + b[i];
    }
}

void AddInplaceHw(const float* a, float* out, int n) {
    const hn::ScalableTag<float> d;
    const int lanes = static_cast<int>(hn::Lanes(d));
    int i = 0;
    for (; i + lanes <= n; i += lanes) {
        hn::Store(hn::Add(hn::Load(d, a + i), hn::Load(d, out + i)), d, out + i);
    }
    for (; i < n; ++i) {
        out[i] += a[i];
    }
}

void ReluHw(float* data, int n) {
    const hn::ScalableTag<float> d;
    const auto zero = hn::Zero(d);
    const int lanes = static_cast<int>(hn::Lanes(d));
    int i = 0;
    for (; i + lanes <= n; i += lanes) {
        hn::Store(hn::Max(hn::Load(d, data + i), zero), d, data + i);
    }
    for (; i < n; ++i) {
        data[i] = std::max(data[i], 0.0f);
    }
}

void Pointwise1x1Hw(const ConstBlobView& input,
                    const ConstBlobView& weights,
                    const float* biases,
                    BlobView& output) {
    for (int row = 0; row < output.rows; ++row) {
        for (int col = 0; col < output.cols; ++col) {
            const float* in = Ptr(input, row, col);
            float* out = Ptr(output, row, col);
            for (int ch = 0; ch < output.channels; ++ch) {
                out[ch] = DotProductHw(in, Ptr(weights, 0, ch), input.channels) +
                          biases[ch];
            }
        }
    }
}

void Pointwise1x1PackedHw(const ConstBlobView& input,
                          const PackedPointwiseFilter& filter,
                          BlobView& output);

namespace {

void Pointwise1x1PackedHwImpl(const ConstBlobView& input,
                              const PackedPointwiseFilter& filter,
                              BlobView& output,
                              bool do_relu) {
    const hn::ScalableTag<float> d;
    const int lanes = static_cast<int>(hn::Lanes(d));
    const auto zero = hn::Zero(d);
    HWY_ALIGN float tail[64];

    for (int row = 0; row < output.rows; ++row) {
        for (int col = 0; col < output.cols; ++col) {
            const float* in = Ptr(input, row, col);
            float* out = Ptr(output, row, col);
            int oc = 0;
            for (; oc + 8 * lanes <= filter.out_channels; oc += 8 * lanes) {
                auto acc0 = hn::LoadU(d, filter.biases.data() + oc);
                auto acc1 = hn::LoadU(d, filter.biases.data() + oc + lanes);
                auto acc2 = hn::LoadU(d, filter.biases.data() + oc + 2 * lanes);
                auto acc3 = hn::LoadU(d, filter.biases.data() + oc + 3 * lanes);
                auto acc4 = hn::LoadU(d, filter.biases.data() + oc + 4 * lanes);
                auto acc5 = hn::LoadU(d, filter.biases.data() + oc + 5 * lanes);
                auto acc6 = hn::LoadU(d, filter.biases.data() + oc + 6 * lanes);
                auto acc7 = hn::LoadU(d, filter.biases.data() + oc + 7 * lanes);
                for (int ic = 0; ic < filter.channels; ++ic) {
                    const auto input_value = hn::Set(d, in[ic]);
                    const float* weight_base =
                        filter.weights.data() +
                        static_cast<size_t>(ic) * filter.padded_out_channels +
                        oc;
                    acc0 = hn::MulAdd(input_value, hn::LoadU(d, weight_base),
                                      acc0);
                    acc1 = hn::MulAdd(input_value,
                                      hn::LoadU(d, weight_base + lanes), acc1);
                    acc2 = hn::MulAdd(input_value,
                                      hn::LoadU(d, weight_base + 2 * lanes), acc2);
                    acc3 = hn::MulAdd(input_value,
                                      hn::LoadU(d, weight_base + 3 * lanes), acc3);
                    acc4 = hn::MulAdd(input_value,
                                      hn::LoadU(d, weight_base + 4 * lanes), acc4);
                    acc5 = hn::MulAdd(input_value,
                                      hn::LoadU(d, weight_base + 5 * lanes), acc5);
                    acc6 = hn::MulAdd(input_value,
                                      hn::LoadU(d, weight_base + 6 * lanes), acc6);
                    acc7 = hn::MulAdd(input_value,
                                      hn::LoadU(d, weight_base + 7 * lanes), acc7);
                }
                if (do_relu) {
                    acc0 = hn::Max(acc0, zero);
                    acc1 = hn::Max(acc1, zero);
                    acc2 = hn::Max(acc2, zero);
                    acc3 = hn::Max(acc3, zero);
                    acc4 = hn::Max(acc4, zero);
                    acc5 = hn::Max(acc5, zero);
                    acc6 = hn::Max(acc6, zero);
                    acc7 = hn::Max(acc7, zero);
                }
                hn::StoreU(acc0, d, out + oc);
                hn::StoreU(acc1, d, out + oc + lanes);
                hn::StoreU(acc2, d, out + oc + 2 * lanes);
                hn::StoreU(acc3, d, out + oc + 3 * lanes);
                hn::StoreU(acc4, d, out + oc + 4 * lanes);
                hn::StoreU(acc5, d, out + oc + 5 * lanes);
                hn::StoreU(acc6, d, out + oc + 6 * lanes);
                hn::StoreU(acc7, d, out + oc + 7 * lanes);
            }
            for (; oc + 4 * lanes <= filter.out_channels; oc += 4 * lanes) {
                auto acc0 = hn::LoadU(d, filter.biases.data() + oc);
                auto acc1 = hn::LoadU(d, filter.biases.data() + oc + lanes);
                auto acc2 = hn::LoadU(d, filter.biases.data() + oc + 2 * lanes);
                auto acc3 = hn::LoadU(d, filter.biases.data() + oc + 3 * lanes);
                for (int ic = 0; ic < filter.channels; ++ic) {
                    const auto input_value = hn::Set(d, in[ic]);
                    const float* weight_base =
                        filter.weights.data() +
                        static_cast<size_t>(ic) * filter.padded_out_channels +
                        oc;
                    acc0 = hn::MulAdd(input_value, hn::LoadU(d, weight_base),
                                      acc0);
                    acc1 = hn::MulAdd(input_value,
                                      hn::LoadU(d, weight_base + lanes), acc1);
                    acc2 = hn::MulAdd(input_value,
                                      hn::LoadU(d, weight_base + 2 * lanes), acc2);
                    acc3 = hn::MulAdd(input_value,
                                      hn::LoadU(d, weight_base + 3 * lanes), acc3);
                }
                if (do_relu) {
                    acc0 = hn::Max(acc0, zero);
                    acc1 = hn::Max(acc1, zero);
                    acc2 = hn::Max(acc2, zero);
                    acc3 = hn::Max(acc3, zero);
                }
                hn::StoreU(acc0, d, out + oc);
                hn::StoreU(acc1, d, out + oc + lanes);
                hn::StoreU(acc2, d, out + oc + 2 * lanes);
                hn::StoreU(acc3, d, out + oc + 3 * lanes);
            }
            for (; oc + 2 * lanes <= filter.out_channels; oc += 2 * lanes) {
                auto acc0 = hn::LoadU(d, filter.biases.data() + oc);
                auto acc1 = hn::LoadU(d, filter.biases.data() + oc + lanes);
                for (int ic = 0; ic < filter.channels; ++ic) {
                    const auto input_value = hn::Set(d, in[ic]);
                    const float* weight_base =
                        filter.weights.data() +
                        static_cast<size_t>(ic) * filter.padded_out_channels +
                        oc;
                    acc0 = hn::MulAdd(input_value, hn::LoadU(d, weight_base),
                                      acc0);
                    acc1 = hn::MulAdd(input_value,
                                      hn::LoadU(d, weight_base + lanes), acc1);
                }
                if (do_relu) {
                    acc0 = hn::Max(acc0, zero);
                    acc1 = hn::Max(acc1, zero);
                }
                hn::StoreU(acc0, d, out + oc);
                hn::StoreU(acc1, d, out + oc + lanes);
            }
            for (; oc < filter.padded_out_channels; oc += lanes) {
                auto acc = hn::LoadU(d, filter.biases.data() + oc);
                for (int ic = 0; ic < filter.channels; ++ic) {
                    const auto input_value = hn::Set(d, in[ic]);
                    const auto weight = hn::LoadU(
                        d, filter.weights.data() +
                               static_cast<size_t>(ic) *
                                   filter.padded_out_channels +
                               oc);
                    acc = hn::MulAdd(input_value, weight, acc);
                }
                if (do_relu) {
                    acc = hn::Max(acc, zero);
                }
                if (oc + lanes <= filter.out_channels) {
                    hn::StoreU(acc, d, out + oc);
                } else {
                    hn::StoreU(acc, d, tail);
                    for (int i = 0; oc + i < filter.out_channels; ++i) {
                        out[oc + i] = tail[i];
                    }
                }
            }
        }
    }
}

}  // namespace

void Pointwise1x1PackedHw(const ConstBlobView& input,
                          const PackedPointwiseFilter& filter,
                          BlobView& output) {
    Pointwise1x1PackedHwImpl(input, filter, output, false);
}

void Pointwise1x1PlannedHw(const ConstBlobView& input,
                           const ConstBlobView& weights,
                           const float* biases,
                           const PointwisePlan& plan,
                           BlobView& output) {
    if (plan.strategy == PointwiseStrategy::kPacked) {
        Pointwise1x1PackedHw(input, plan.packed, output);
        return;
    }
    Pointwise1x1Hw(input, weights, biases, output);
}

void Pointwise1x1PlannedHwRelu(const ConstBlobView& input,
                               const ConstBlobView& weights,
                               const float* biases,
                               const PointwisePlan& plan,
                               BlobView& output) {
    if (plan.strategy == PointwiseStrategy::kPacked) {
        Pointwise1x1PackedHwImpl(input, plan.packed, output, true);
        return;
    }
    for (int row = 0; row < output.rows; ++row) {
        for (int col = 0; col < output.cols; ++col) {
            const float* in = Ptr(input, row, col);
            float* out = Ptr(output, row, col);
            for (int ch = 0; ch < output.channels; ++ch) {
                out[ch] = std::max(
                    DotProductHw(in, Ptr(weights, 0, ch), input.channels) +
                        biases[ch],
                    0.0f);
            }
        }
    }
}

void Depthwise3x3Hw(const ConstBlobView& input,
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
                    MulAddHw(Ptr(input, r, c), Ptr(weights, 0, filter_idx),
                             out, output.channels);
                }
            }
            AddInplaceHw(biases, out, output.channels);
        }
    }
}

void MaxPool2x2S2Hw(const ConstBlobView& input, BlobView& output) {
    const hn::ScalableTag<float> d;
    const int lanes = static_cast<int>(hn::Lanes(d));

    for (int row = 0; row < output.rows; ++row) {
        for (int col = 0; col < output.cols; ++col) {
            const int rstart = row * 2;
            const int cstart = col * 2;
            const int rend = std::min(rstart + 2, input.rows);
            const int cend = std::min(cstart + 2, input.cols);
            float* out = Ptr(output, row, col);
            int ch = 0;
            for (; ch + lanes <= output.channels; ch += lanes) {
                auto max_value = hn::Load(d, Ptr(input, rstart, cstart) + ch);
                for (int r = rstart; r < rend; ++r) {
                    for (int c = cstart; c < cend; ++c) {
                        max_value =
                            hn::Max(max_value, hn::Load(d, Ptr(input, r, c) + ch));
                    }
                }
                hn::Store(max_value, d, out + ch);
            }
            for (; ch < output.channels; ++ch) {
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
