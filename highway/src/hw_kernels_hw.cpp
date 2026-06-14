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
#if defined(FDT_HW_FORCE_SCALAR)
    return 1;
#else
    const hn::ScalableTag<float> d;
    return static_cast<int>(hn::Lanes(d));
#endif
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

    if (filter.out_channels == 2 * lanes &&
        filter.padded_out_channels == 2 * lanes) {
        for (int row = 0; row < output.rows; ++row) {
            int col = 0;
            for (; col + 1 < output.cols; col += 2) {
                const float* in0 = Ptr(input, row, col);
                const float* in1 = Ptr(input, row, col + 1);
                float* out0 = Ptr(output, row, col);
                float* out1 = Ptr(output, row, col + 1);
                auto acc00 = hn::LoadU(d, filter.biases.data());
                auto acc01 = hn::LoadU(d, filter.biases.data() + lanes);
                auto acc10 = acc00;
                auto acc11 = acc01;
                for (int ic = 0; ic < filter.channels; ++ic) {
                    const float* weight_base =
                        filter.weights.data() +
                        static_cast<size_t>(ic) * filter.padded_out_channels;
                    const auto weight0 = hn::LoadU(d, weight_base);
                    const auto weight1 = hn::LoadU(d, weight_base + lanes);
                    const auto input0 = hn::Set(d, in0[ic]);
                    const auto input1 = hn::Set(d, in1[ic]);
                    acc00 = hn::MulAdd(input0, weight0, acc00);
                    acc01 = hn::MulAdd(input0, weight1, acc01);
                    acc10 = hn::MulAdd(input1, weight0, acc10);
                    acc11 = hn::MulAdd(input1, weight1, acc11);
                }
                if (do_relu) {
                    acc00 = hn::Max(acc00, zero);
                    acc01 = hn::Max(acc01, zero);
                    acc10 = hn::Max(acc10, zero);
                    acc11 = hn::Max(acc11, zero);
                }
                hn::StoreU(acc00, d, out0);
                hn::StoreU(acc01, d, out0 + lanes);
                hn::StoreU(acc10, d, out1);
                hn::StoreU(acc11, d, out1 + lanes);
            }
            for (; col < output.cols; ++col) {
                const float* in = Ptr(input, row, col);
                float* out = Ptr(output, row, col);
                auto acc0 = hn::LoadU(d, filter.biases.data());
                auto acc1 = hn::LoadU(d, filter.biases.data() + lanes);
                for (int ic = 0; ic < filter.channels; ++ic) {
                    const auto input_value = hn::Set(d, in[ic]);
                    const float* weight_base =
                        filter.weights.data() +
                        static_cast<size_t>(ic) * filter.padded_out_channels;
                    acc0 = hn::MulAdd(input_value, hn::LoadU(d, weight_base),
                                      acc0);
                    acc1 = hn::MulAdd(input_value,
                                      hn::LoadU(d, weight_base + lanes), acc1);
                }
                if (do_relu) {
                    acc0 = hn::Max(acc0, zero);
                    acc1 = hn::Max(acc1, zero);
                }
                hn::StoreU(acc0, d, out);
                hn::StoreU(acc1, d, out + lanes);
            }
        }
        return;
    }

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

template <typename D>
void Depthwise3x3InteriorLoop(D d,
                              const float* const* in_ptrs,
                              const float* const* weight_ptrs,
                              const float* biases,
                              float* out,
                              int channels,
                              bool do_relu) {
    const int lanes = static_cast<int>(hn::Lanes(d));
    const auto zero = hn::Zero(d);
    int ch = 0;
    if (channels == 64 && lanes * 2 <= 64) {
        for (; ch + 2 * lanes <= 64; ch += 2 * lanes) {
            auto acc0 = hn::LoadU(d, biases + ch);
            auto acc1 = hn::LoadU(d, biases + ch + lanes);
            acc0 = hn::MulAdd(hn::LoadU(d, in_ptrs[0] + ch),
                              hn::LoadU(d, weight_ptrs[0] + ch), acc0);
            acc1 = hn::MulAdd(hn::LoadU(d, in_ptrs[0] + ch + lanes),
                              hn::LoadU(d, weight_ptrs[0] + ch + lanes), acc1);
            acc0 = hn::MulAdd(hn::LoadU(d, in_ptrs[1] + ch),
                              hn::LoadU(d, weight_ptrs[1] + ch), acc0);
            acc1 = hn::MulAdd(hn::LoadU(d, in_ptrs[1] + ch + lanes),
                              hn::LoadU(d, weight_ptrs[1] + ch + lanes), acc1);
            acc0 = hn::MulAdd(hn::LoadU(d, in_ptrs[2] + ch),
                              hn::LoadU(d, weight_ptrs[2] + ch), acc0);
            acc1 = hn::MulAdd(hn::LoadU(d, in_ptrs[2] + ch + lanes),
                              hn::LoadU(d, weight_ptrs[2] + ch + lanes), acc1);
            acc0 = hn::MulAdd(hn::LoadU(d, in_ptrs[3] + ch),
                              hn::LoadU(d, weight_ptrs[3] + ch), acc0);
            acc1 = hn::MulAdd(hn::LoadU(d, in_ptrs[3] + ch + lanes),
                              hn::LoadU(d, weight_ptrs[3] + ch + lanes), acc1);
            acc0 = hn::MulAdd(hn::LoadU(d, in_ptrs[4] + ch),
                              hn::LoadU(d, weight_ptrs[4] + ch), acc0);
            acc1 = hn::MulAdd(hn::LoadU(d, in_ptrs[4] + ch + lanes),
                              hn::LoadU(d, weight_ptrs[4] + ch + lanes), acc1);
            acc0 = hn::MulAdd(hn::LoadU(d, in_ptrs[5] + ch),
                              hn::LoadU(d, weight_ptrs[5] + ch), acc0);
            acc1 = hn::MulAdd(hn::LoadU(d, in_ptrs[5] + ch + lanes),
                              hn::LoadU(d, weight_ptrs[5] + ch + lanes), acc1);
            acc0 = hn::MulAdd(hn::LoadU(d, in_ptrs[6] + ch),
                              hn::LoadU(d, weight_ptrs[6] + ch), acc0);
            acc1 = hn::MulAdd(hn::LoadU(d, in_ptrs[6] + ch + lanes),
                              hn::LoadU(d, weight_ptrs[6] + ch + lanes), acc1);
            acc0 = hn::MulAdd(hn::LoadU(d, in_ptrs[7] + ch),
                              hn::LoadU(d, weight_ptrs[7] + ch), acc0);
            acc1 = hn::MulAdd(hn::LoadU(d, in_ptrs[7] + ch + lanes),
                              hn::LoadU(d, weight_ptrs[7] + ch + lanes), acc1);
            acc0 = hn::MulAdd(hn::LoadU(d, in_ptrs[8] + ch),
                              hn::LoadU(d, weight_ptrs[8] + ch), acc0);
            acc1 = hn::MulAdd(hn::LoadU(d, in_ptrs[8] + ch + lanes),
                              hn::LoadU(d, weight_ptrs[8] + ch + lanes), acc1);
            if (do_relu) {
                acc0 = hn::Max(acc0, zero);
                acc1 = hn::Max(acc1, zero);
            }
            hn::StoreU(acc0, d, out + ch);
            hn::StoreU(acc1, d, out + ch + lanes);
        }
        return;
    }

    for (; ch + lanes <= channels; ch += lanes) {
        auto acc = hn::LoadU(d, biases + ch);
        acc = hn::MulAdd(hn::LoadU(d, in_ptrs[0] + ch),
                         hn::LoadU(d, weight_ptrs[0] + ch), acc);
        acc = hn::MulAdd(hn::LoadU(d, in_ptrs[1] + ch),
                         hn::LoadU(d, weight_ptrs[1] + ch), acc);
        acc = hn::MulAdd(hn::LoadU(d, in_ptrs[2] + ch),
                         hn::LoadU(d, weight_ptrs[2] + ch), acc);
        acc = hn::MulAdd(hn::LoadU(d, in_ptrs[3] + ch),
                         hn::LoadU(d, weight_ptrs[3] + ch), acc);
        acc = hn::MulAdd(hn::LoadU(d, in_ptrs[4] + ch),
                         hn::LoadU(d, weight_ptrs[4] + ch), acc);
        acc = hn::MulAdd(hn::LoadU(d, in_ptrs[5] + ch),
                         hn::LoadU(d, weight_ptrs[5] + ch), acc);
        acc = hn::MulAdd(hn::LoadU(d, in_ptrs[6] + ch),
                         hn::LoadU(d, weight_ptrs[6] + ch), acc);
        acc = hn::MulAdd(hn::LoadU(d, in_ptrs[7] + ch),
                         hn::LoadU(d, weight_ptrs[7] + ch), acc);
        acc = hn::MulAdd(hn::LoadU(d, in_ptrs[8] + ch),
                         hn::LoadU(d, weight_ptrs[8] + ch), acc);
        if (do_relu) {
            acc = hn::Max(acc, zero);
        }
        hn::StoreU(acc, d, out + ch);
    }
    for (; ch < channels; ++ch) {
        float acc = biases[ch];
        for (int i = 0; i < 9; ++i) {
            acc += in_ptrs[i][ch] * weight_ptrs[i][ch];
        }
        out[ch] = do_relu ? std::max(acc, 0.0f) : acc;
    }
}

template <typename D>
void Depthwise3x3BoundaryLoop(D d,
                              const ConstBlobView& input,
                              const float* const* weight_ptrs,
                              const float* biases,
                              BlobView& output,
                              int row,
                              int col,
                              bool do_relu) {
    const int lanes = static_cast<int>(hn::Lanes(d));
    const auto zero = hn::Zero(d);
    float* out = Ptr(output, row, col);
    const int srcy_start = std::max(0, row - 1);
    const int srcy_end = std::min(row + 2, input.rows);
    const int srcx_start = std::max(0, col - 1);
    const int srcx_end = std::min(col + 2, input.cols);
    int ch = 0;
    for (; ch + lanes <= output.channels; ch += lanes) {
        auto acc = hn::LoadU(d, biases + ch);
        for (int r = srcy_start; r < srcy_end; ++r) {
            for (int c = srcx_start; c < srcx_end; ++c) {
                const int filter_r = r - row + 1;
                const int filter_c = c - col + 1;
                const int filter_idx = filter_r * 3 + filter_c;
                acc = hn::MulAdd(hn::LoadU(d, Ptr(input, r, c) + ch),
                                 hn::LoadU(d, weight_ptrs[filter_idx] + ch),
                                 acc);
            }
        }
        if (do_relu) {
            acc = hn::Max(acc, zero);
        }
        hn::StoreU(acc, d, out + ch);
    }
    for (; ch < output.channels; ++ch) {
        float acc = biases[ch];
        for (int r = srcy_start; r < srcy_end; ++r) {
            for (int c = srcx_start; c < srcx_end; ++c) {
                const int filter_r = r - row + 1;
                const int filter_c = c - col + 1;
                const int filter_idx = filter_r * 3 + filter_c;
                acc += Ptr(input, r, c)[ch] * weight_ptrs[filter_idx][ch];
            }
        }
        out[ch] = do_relu ? std::max(acc, 0.0f) : acc;
    }
    for (; ch < output.channel_step; ++ch) {
        out[ch] = 0.0f;
    }
}

void Depthwise3x3HwImpl(const ConstBlobView& input,
                        const ConstBlobView& weights,
                        const float* biases,
                        BlobView& output,
                        bool do_relu) {
    const hn::ScalableTag<float> d;
    const float* weight_ptrs[9];
    for (int i = 0; i < 9; ++i) {
        weight_ptrs[i] = Ptr(weights, 0, i);
    }

    for (int row = 0; row < output.rows; ++row) {
        for (int col = 0; col < output.cols; ++col) {
            float* out = Ptr(output, row, col);
            const bool is_interior = row > 0 && row + 1 < input.rows &&
                                     col > 0 && col + 1 < input.cols;
            if (is_interior) {
                const float* in_ptrs[9] = {
                    Ptr(input, row - 1, col - 1),
                    Ptr(input, row - 1, col),
                    Ptr(input, row - 1, col + 1),
                    Ptr(input, row, col - 1),
                    Ptr(input, row, col),
                    Ptr(input, row, col + 1),
                    Ptr(input, row + 1, col - 1),
                    Ptr(input, row + 1, col),
                    Ptr(input, row + 1, col + 1)};
                Depthwise3x3InteriorLoop(d, in_ptrs, weight_ptrs, biases, out,
                                         output.channels, do_relu);
                for (int ch = output.channels; ch < output.channel_step; ++ch) {
                    out[ch] = 0.0f;
                }
            } else {
                Depthwise3x3BoundaryLoop(d, input, weight_ptrs, biases, output,
                                         row, col, do_relu);
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
    Depthwise3x3HwImpl(input, weights, biases, output, false);
}

void Depthwise3x3HwRelu(const ConstBlobView& input,
                        const ConstBlobView& weights,
                        const float* biases,
                        BlobView& output) {
    Depthwise3x3HwImpl(input, weights, biases, output, true);
}

void MaxPool2x2S2Hw(const ConstBlobView& input, BlobView& output) {
    const hn::ScalableTag<float> d;
    const int lanes = static_cast<int>(hn::Lanes(d));

    for (int row = 0; row < output.rows; ++row) {
        for (int col = 0; col < output.cols; ++col) {
            const int rstart = row * 2;
            const int cstart = col * 2;
            float* out = Ptr(output, row, col);

            if (rstart + 1 < input.rows && cstart + 1 < input.cols) {
                const float* top_left = Ptr(input, rstart, cstart);
                const float* top_right = Ptr(input, rstart, cstart + 1);
                const float* bottom_left = Ptr(input, rstart + 1, cstart);
                const float* bottom_right = Ptr(input, rstart + 1, cstart + 1);
                for (int ch = 0; ch < output.channel_step; ch += lanes) {
                    const auto top = hn::Max(hn::LoadU(d, top_left + ch),
                                             hn::LoadU(d, top_right + ch));
                    const auto bottom = hn::Max(hn::LoadU(d, bottom_left + ch),
                                                hn::LoadU(d, bottom_right + ch));
                    hn::StoreU(hn::Max(top, bottom), d, out + ch);
                }
            } else {
                const int rend = std::min(rstart + 2, input.rows);
                const int cend = std::min(cstart + 2, input.cols);
                int ch = 0;
                for (; ch + lanes <= output.channels; ch += lanes) {
                    auto max_value =
                        hn::LoadU(d, Ptr(input, rstart, cstart) + ch);
                    for (int r = rstart; r < rend; ++r) {
                        for (int c = cstart; c < cend; ++c) {
                            max_value = hn::Max(
                                max_value, hn::LoadU(d, Ptr(input, r, c) + ch));
                        }
                    }
                    hn::StoreU(max_value, d, out + ch);
                }
                for (; ch < output.channels; ++ch) {
                    float max_value = Ptr(input, rstart, cstart)[ch];
                    for (int r = rstart; r < rend; ++r) {
                        for (int c = cstart; c < cend; ++c) {
                            max_value =
                                std::max(max_value, Ptr(input, r, c)[ch]);
                        }
                    }
                    out[ch] = max_value;
                }
                for (; ch < output.channel_step; ++ch) {
                    out[ch] = 0.0f;
                }
            }
        }
    }
}

}  // namespace hw
}  // namespace fdt
