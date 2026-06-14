#include "hw_kernels.h"

#include <algorithm>
#include <cstring>

#if defined(FDT_HW_ENABLE_X86_AVX2)
#include <immintrin.h>
#define FDT_HW_HAS_AVX2 1
#else
#define FDT_HW_HAS_AVX2 0
#endif

namespace fdt {
namespace hw {

int IntrinsicsFloatLanes() {
#if FDT_HW_HAS_AVX2
    return 8;
#else
    return 1;
#endif
}

float DotProductIntrinsics(const float* a, const float* b, int n) {
#if FDT_HW_HAS_AVX2
    __m256 sum = _mm256_setzero_ps();
    int i = 0;
    for (; i + 8 <= n; i += 8) {
        const __m256 va = _mm256_loadu_ps(a + i);
        const __m256 vb = _mm256_loadu_ps(b + i);
#if defined(__FMA__) || defined(_M_X64)
        sum = _mm256_fmadd_ps(va, vb, sum);
#else
        sum = _mm256_add_ps(sum, _mm256_mul_ps(va, vb));
#endif
    }
    sum = _mm256_hadd_ps(sum, sum);
    sum = _mm256_hadd_ps(sum, sum);
    float result = reinterpret_cast<float*>(&sum)[0] +
                   reinterpret_cast<float*>(&sum)[4];
    for (; i < n; ++i) {
        result += a[i] * b[i];
    }
    return result;
#else
    return DotProductScalar(a, b, n);
#endif
}

void MulAddIntrinsics(const float* a, const float* b, float* acc, int n) {
#if FDT_HW_HAS_AVX2
    int i = 0;
    for (; i + 8 <= n; i += 8) {
        const __m256 va = _mm256_loadu_ps(a + i);
        const __m256 vb = _mm256_loadu_ps(b + i);
        const __m256 vacc = _mm256_loadu_ps(acc + i);
#if defined(__FMA__) || defined(_M_X64)
        _mm256_storeu_ps(acc + i, _mm256_fmadd_ps(va, vb, vacc));
#else
        _mm256_storeu_ps(acc + i, _mm256_add_ps(vacc, _mm256_mul_ps(va, vb)));
#endif
    }
    for (; i < n; ++i) {
        acc[i] += a[i] * b[i];
    }
#else
    MulAddScalar(a, b, acc, n);
#endif
}

void AddIntrinsics(const float* a, const float* b, float* out, int n) {
#if FDT_HW_HAS_AVX2
    int i = 0;
    for (; i + 8 <= n; i += 8) {
        _mm256_storeu_ps(out + i,
                         _mm256_add_ps(_mm256_loadu_ps(a + i),
                                       _mm256_loadu_ps(b + i)));
    }
    for (; i < n; ++i) {
        out[i] = a[i] + b[i];
    }
#else
    AddScalar(a, b, out, n);
#endif
}

void AddInplaceIntrinsics(const float* a, float* out, int n) {
#if FDT_HW_HAS_AVX2
    int i = 0;
    for (; i + 8 <= n; i += 8) {
        _mm256_storeu_ps(out + i,
                         _mm256_add_ps(_mm256_loadu_ps(a + i),
                                       _mm256_loadu_ps(out + i)));
    }
    for (; i < n; ++i) {
        out[i] += a[i];
    }
#else
    AddInplaceScalar(a, out, n);
#endif
}

void ReluIntrinsics(float* data, int n) {
#if FDT_HW_HAS_AVX2
    const __m256 zero = _mm256_setzero_ps();
    int i = 0;
    for (; i + 8 <= n; i += 8) {
        _mm256_storeu_ps(data + i, _mm256_max_ps(_mm256_loadu_ps(data + i), zero));
    }
    for (; i < n; ++i) {
        data[i] = std::max(data[i], 0.0f);
    }
#else
    ReluScalar(data, n);
#endif
}

void Pointwise1x1Intrinsics(const ConstBlobView& input,
                            const ConstBlobView& weights,
                            const float* biases,
                            BlobView& output) {
    for (int row = 0; row < output.rows; ++row) {
        for (int col = 0; col < output.cols; ++col) {
            const float* in = Ptr(input, row, col);
            float* out = Ptr(output, row, col);
            for (int ch = 0; ch < output.channels; ++ch) {
                out[ch] = DotProductIntrinsics(in, Ptr(weights, 0, ch),
                                               input.channels) +
                          biases[ch];
            }
        }
    }
}

void Pointwise1x1PackedIntrinsics(const ConstBlobView& input,
                                  const PackedPointwiseFilter& filter,
                                  BlobView& output) {
#if FDT_HW_HAS_AVX2
    alignas(32) float tail[8];
    for (int row = 0; row < output.rows; ++row) {
        for (int col = 0; col < output.cols; ++col) {
            const float* in = Ptr(input, row, col);
            float* out = Ptr(output, row, col);
            int oc = 0;
            for (; oc + 32 <= filter.out_channels; oc += 32) {
                __m256 acc0 = _mm256_loadu_ps(filter.biases.data() + oc);
                __m256 acc1 = _mm256_loadu_ps(filter.biases.data() + oc + 8);
                __m256 acc2 = _mm256_loadu_ps(filter.biases.data() + oc + 16);
                __m256 acc3 = _mm256_loadu_ps(filter.biases.data() + oc + 24);
                for (int ic = 0; ic < filter.channels; ++ic) {
                    const __m256 input_value = _mm256_set1_ps(in[ic]);
                    const float* weight_base =
                        filter.weights.data() +
                        static_cast<size_t>(ic) * filter.padded_out_channels +
                        oc;
#if defined(__FMA__) || defined(_M_X64)
                    acc0 = _mm256_fmadd_ps(input_value,
                                           _mm256_loadu_ps(weight_base), acc0);
                    acc1 = _mm256_fmadd_ps(input_value,
                                           _mm256_loadu_ps(weight_base + 8), acc1);
                    acc2 = _mm256_fmadd_ps(input_value,
                                           _mm256_loadu_ps(weight_base + 16), acc2);
                    acc3 = _mm256_fmadd_ps(input_value,
                                           _mm256_loadu_ps(weight_base + 24), acc3);
#else
                    acc0 = _mm256_add_ps(
                        acc0, _mm256_mul_ps(input_value,
                                            _mm256_loadu_ps(weight_base)));
                    acc1 = _mm256_add_ps(
                        acc1, _mm256_mul_ps(input_value,
                                            _mm256_loadu_ps(weight_base + 8)));
                    acc2 = _mm256_add_ps(
                        acc2, _mm256_mul_ps(input_value,
                                            _mm256_loadu_ps(weight_base + 16)));
                    acc3 = _mm256_add_ps(
                        acc3, _mm256_mul_ps(input_value,
                                            _mm256_loadu_ps(weight_base + 24)));
#endif
                }
                _mm256_storeu_ps(out + oc, acc0);
                _mm256_storeu_ps(out + oc + 8, acc1);
                _mm256_storeu_ps(out + oc + 16, acc2);
                _mm256_storeu_ps(out + oc + 24, acc3);
            }
            for (; oc + 16 <= filter.out_channels; oc += 16) {
                __m256 acc0 = _mm256_loadu_ps(filter.biases.data() + oc);
                __m256 acc1 = _mm256_loadu_ps(filter.biases.data() + oc + 8);
                for (int ic = 0; ic < filter.channels; ++ic) {
                    const __m256 input_value = _mm256_set1_ps(in[ic]);
                    const float* weight_base =
                        filter.weights.data() +
                        static_cast<size_t>(ic) * filter.padded_out_channels +
                        oc;
#if defined(__FMA__) || defined(_M_X64)
                    acc0 = _mm256_fmadd_ps(input_value,
                                           _mm256_loadu_ps(weight_base), acc0);
                    acc1 = _mm256_fmadd_ps(input_value,
                                           _mm256_loadu_ps(weight_base + 8), acc1);
#else
                    acc0 = _mm256_add_ps(
                        acc0, _mm256_mul_ps(input_value,
                                            _mm256_loadu_ps(weight_base)));
                    acc1 = _mm256_add_ps(
                        acc1, _mm256_mul_ps(input_value,
                                            _mm256_loadu_ps(weight_base + 8)));
#endif
                }
                _mm256_storeu_ps(out + oc, acc0);
                _mm256_storeu_ps(out + oc + 8, acc1);
            }
            for (; oc < filter.padded_out_channels; oc += 8) {
                __m256 acc = _mm256_loadu_ps(filter.biases.data() + oc);
                for (int ic = 0; ic < filter.channels; ++ic) {
                    const __m256 input_value = _mm256_set1_ps(in[ic]);
                    const __m256 weight = _mm256_loadu_ps(
                        filter.weights.data() +
                        static_cast<size_t>(ic) * filter.padded_out_channels +
                        oc);
#if defined(__FMA__) || defined(_M_X64)
                    acc = _mm256_fmadd_ps(input_value, weight, acc);
#else
                    acc = _mm256_add_ps(acc, _mm256_mul_ps(input_value, weight));
#endif
                }
                if (oc + 8 <= filter.out_channels) {
                    _mm256_storeu_ps(out + oc, acc);
                } else {
                    _mm256_store_ps(tail, acc);
                    for (int i = 0; oc + i < filter.out_channels; ++i) {
                        out[oc + i] = tail[i];
                    }
                }
            }
        }
    }
#else
    Pointwise1x1PackedScalar(input, filter, output);
#endif
}

void Pointwise1x1PlannedIntrinsics(const ConstBlobView& input,
                                   const ConstBlobView& weights,
                                   const float* biases,
                                   const PointwisePlan& plan,
                                   BlobView& output) {
    if (plan.strategy == PointwiseStrategy::kPacked) {
        Pointwise1x1PackedIntrinsics(input, plan.packed, output);
        return;
    }
    Pointwise1x1Intrinsics(input, weights, biases, output);
}

namespace {

void Depthwise3x3IntrinsicsImpl(const ConstBlobView& input,
                                const ConstBlobView& weights,
                                const float* biases,
                                BlobView& output,
                                bool do_relu) {
#if FDT_HW_HAS_AVX2
    const float* weight_ptrs[9];
    for (int i = 0; i < 9; ++i) {
        weight_ptrs[i] = Ptr(weights, 0, i);
    }

    const __m256 zero = _mm256_setzero_ps();
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
                if (output.channels == 64 && output.channel_step == 64) {
                    for (int ch = 0; ch < 64; ch += 16) {
                        __m256 acc0 = _mm256_loadu_ps(biases + ch);
                        __m256 acc1 = _mm256_loadu_ps(biases + ch + 8);
#if defined(__FMA__) || defined(_M_X64)
                        acc0 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[0] + ch),
                            _mm256_loadu_ps(weight_ptrs[0] + ch), acc0);
                        acc1 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[0] + ch + 8),
                            _mm256_loadu_ps(weight_ptrs[0] + ch + 8), acc1);
                        acc0 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[1] + ch),
                            _mm256_loadu_ps(weight_ptrs[1] + ch), acc0);
                        acc1 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[1] + ch + 8),
                            _mm256_loadu_ps(weight_ptrs[1] + ch + 8), acc1);
                        acc0 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[2] + ch),
                            _mm256_loadu_ps(weight_ptrs[2] + ch), acc0);
                        acc1 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[2] + ch + 8),
                            _mm256_loadu_ps(weight_ptrs[2] + ch + 8), acc1);
                        acc0 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[3] + ch),
                            _mm256_loadu_ps(weight_ptrs[3] + ch), acc0);
                        acc1 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[3] + ch + 8),
                            _mm256_loadu_ps(weight_ptrs[3] + ch + 8), acc1);
                        acc0 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[4] + ch),
                            _mm256_loadu_ps(weight_ptrs[4] + ch), acc0);
                        acc1 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[4] + ch + 8),
                            _mm256_loadu_ps(weight_ptrs[4] + ch + 8), acc1);
                        acc0 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[5] + ch),
                            _mm256_loadu_ps(weight_ptrs[5] + ch), acc0);
                        acc1 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[5] + ch + 8),
                            _mm256_loadu_ps(weight_ptrs[5] + ch + 8), acc1);
                        acc0 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[6] + ch),
                            _mm256_loadu_ps(weight_ptrs[6] + ch), acc0);
                        acc1 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[6] + ch + 8),
                            _mm256_loadu_ps(weight_ptrs[6] + ch + 8), acc1);
                        acc0 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[7] + ch),
                            _mm256_loadu_ps(weight_ptrs[7] + ch), acc0);
                        acc1 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[7] + ch + 8),
                            _mm256_loadu_ps(weight_ptrs[7] + ch + 8), acc1);
                        acc0 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[8] + ch),
                            _mm256_loadu_ps(weight_ptrs[8] + ch), acc0);
                        acc1 = _mm256_fmadd_ps(
                            _mm256_loadu_ps(in_ptrs[8] + ch + 8),
                            _mm256_loadu_ps(weight_ptrs[8] + ch + 8), acc1);
#else
                        for (int k = 0; k < 9; ++k) {
                            acc0 = _mm256_add_ps(
                                acc0, _mm256_mul_ps(
                                          _mm256_loadu_ps(in_ptrs[k] + ch),
                                          _mm256_loadu_ps(weight_ptrs[k] + ch)));
                            acc1 = _mm256_add_ps(
                                acc1, _mm256_mul_ps(
                                          _mm256_loadu_ps(in_ptrs[k] + ch + 8),
                                          _mm256_loadu_ps(weight_ptrs[k] + ch + 8)));
                        }
#endif
                        if (do_relu) {
                            acc0 = _mm256_max_ps(acc0, zero);
                            acc1 = _mm256_max_ps(acc1, zero);
                        }
                        _mm256_storeu_ps(out + ch, acc0);
                        _mm256_storeu_ps(out + ch + 8, acc1);
                    }
                    continue;
                }
                for (int ch = 0; ch < output.channel_step; ch += 8) {
                    __m256 acc = _mm256_loadu_ps(biases + ch);
#if defined(__FMA__) || defined(_M_X64)
                    acc = _mm256_fmadd_ps(_mm256_loadu_ps(in_ptrs[0] + ch),
                                          _mm256_loadu_ps(weight_ptrs[0] + ch),
                                          acc);
                    acc = _mm256_fmadd_ps(_mm256_loadu_ps(in_ptrs[1] + ch),
                                          _mm256_loadu_ps(weight_ptrs[1] + ch),
                                          acc);
                    acc = _mm256_fmadd_ps(_mm256_loadu_ps(in_ptrs[2] + ch),
                                          _mm256_loadu_ps(weight_ptrs[2] + ch),
                                          acc);
                    acc = _mm256_fmadd_ps(_mm256_loadu_ps(in_ptrs[3] + ch),
                                          _mm256_loadu_ps(weight_ptrs[3] + ch),
                                          acc);
                    acc = _mm256_fmadd_ps(_mm256_loadu_ps(in_ptrs[4] + ch),
                                          _mm256_loadu_ps(weight_ptrs[4] + ch),
                                          acc);
                    acc = _mm256_fmadd_ps(_mm256_loadu_ps(in_ptrs[5] + ch),
                                          _mm256_loadu_ps(weight_ptrs[5] + ch),
                                          acc);
                    acc = _mm256_fmadd_ps(_mm256_loadu_ps(in_ptrs[6] + ch),
                                          _mm256_loadu_ps(weight_ptrs[6] + ch),
                                          acc);
                    acc = _mm256_fmadd_ps(_mm256_loadu_ps(in_ptrs[7] + ch),
                                          _mm256_loadu_ps(weight_ptrs[7] + ch),
                                          acc);
                    acc = _mm256_fmadd_ps(_mm256_loadu_ps(in_ptrs[8] + ch),
                                          _mm256_loadu_ps(weight_ptrs[8] + ch),
                                          acc);
#else
                    acc = _mm256_add_ps(
                        acc, _mm256_mul_ps(
                                 _mm256_loadu_ps(in_ptrs[0] + ch),
                                 _mm256_loadu_ps(weight_ptrs[0] + ch)));
                    acc = _mm256_add_ps(
                        acc, _mm256_mul_ps(
                                 _mm256_loadu_ps(in_ptrs[1] + ch),
                                 _mm256_loadu_ps(weight_ptrs[1] + ch)));
                    acc = _mm256_add_ps(
                        acc, _mm256_mul_ps(
                                 _mm256_loadu_ps(in_ptrs[2] + ch),
                                 _mm256_loadu_ps(weight_ptrs[2] + ch)));
                    acc = _mm256_add_ps(
                        acc, _mm256_mul_ps(
                                 _mm256_loadu_ps(in_ptrs[3] + ch),
                                 _mm256_loadu_ps(weight_ptrs[3] + ch)));
                    acc = _mm256_add_ps(
                        acc, _mm256_mul_ps(
                                 _mm256_loadu_ps(in_ptrs[4] + ch),
                                 _mm256_loadu_ps(weight_ptrs[4] + ch)));
                    acc = _mm256_add_ps(
                        acc, _mm256_mul_ps(
                                 _mm256_loadu_ps(in_ptrs[5] + ch),
                                 _mm256_loadu_ps(weight_ptrs[5] + ch)));
                    acc = _mm256_add_ps(
                        acc, _mm256_mul_ps(
                                 _mm256_loadu_ps(in_ptrs[6] + ch),
                                 _mm256_loadu_ps(weight_ptrs[6] + ch)));
                    acc = _mm256_add_ps(
                        acc, _mm256_mul_ps(
                                 _mm256_loadu_ps(in_ptrs[7] + ch),
                                 _mm256_loadu_ps(weight_ptrs[7] + ch)));
                    acc = _mm256_add_ps(
                        acc, _mm256_mul_ps(
                                 _mm256_loadu_ps(in_ptrs[8] + ch),
                                 _mm256_loadu_ps(weight_ptrs[8] + ch)));
#endif
                    if (do_relu) {
                        acc = _mm256_max_ps(acc, zero);
                    }
                    _mm256_storeu_ps(out + ch, acc);
                }
            } else {
                for (int ch = 0; ch < output.channel_step; ch += 8) {
                    __m256 acc = _mm256_loadu_ps(biases + ch);
                    const int srcy_start = std::max(0, row - 1);
                    const int srcy_end = std::min(row + 2, input.rows);
                    const int srcx_start = std::max(0, col - 1);
                    const int srcx_end = std::min(col + 2, input.cols);
                    for (int r = srcy_start; r < srcy_end; ++r) {
                        for (int c = srcx_start; c < srcx_end; ++c) {
                            const int filter_r = r - row + 1;
                            const int filter_c = c - col + 1;
                            const int filter_idx = filter_r * 3 + filter_c;
#if defined(__FMA__) || defined(_M_X64)
                            acc = _mm256_fmadd_ps(
                                _mm256_loadu_ps(Ptr(input, r, c) + ch),
                                _mm256_loadu_ps(weight_ptrs[filter_idx] + ch),
                                acc);
#else
                            acc = _mm256_add_ps(
                                acc, _mm256_mul_ps(
                                         _mm256_loadu_ps(Ptr(input, r, c) + ch),
                                         _mm256_loadu_ps(weight_ptrs[filter_idx] + ch)));
#endif
                        }
                    }
                    if (do_relu) {
                        acc = _mm256_max_ps(acc, zero);
                    }
                    _mm256_storeu_ps(out + ch, acc);
                }
            }
        }
    }
#else
    Depthwise3x3Scalar(input, weights, biases, output);
    if (do_relu) {
        ReluScalar(output.data,
                   output.rows * output.cols * output.channel_step);
    }
#endif
}

}  // namespace

void Depthwise3x3Intrinsics(const ConstBlobView& input,
                            const ConstBlobView& weights,
                            const float* biases,
                            BlobView& output) {
    Depthwise3x3IntrinsicsImpl(input, weights, biases, output, false);
}

void Depthwise3x3IntrinsicsRelu(const ConstBlobView& input,
                                const ConstBlobView& weights,
                                const float* biases,
                                BlobView& output) {
    Depthwise3x3IntrinsicsImpl(input, weights, biases, output, true);
}

void MaxPool2x2S2Intrinsics(const ConstBlobView& input, BlobView& output) {
#if FDT_HW_HAS_AVX2
    for (int row = 0; row < output.rows; ++row) {
        for (int col = 0; col < output.cols; ++col) {
            const int rstart = row * 2;
            const int cstart = col * 2;
            float* out = Ptr(output, row, col);
            if (rstart + 1 < input.rows && cstart + 1 < input.cols) {
                for (int ch = 0; ch < output.channel_step; ch += 8) {
                    const __m256 top = _mm256_max_ps(
                        _mm256_loadu_ps(Ptr(input, rstart, cstart) + ch),
                        _mm256_loadu_ps(Ptr(input, rstart, cstart + 1) + ch));
                    const __m256 bottom = _mm256_max_ps(
                        _mm256_loadu_ps(Ptr(input, rstart + 1, cstart) + ch),
                        _mm256_loadu_ps(Ptr(input, rstart + 1, cstart + 1) + ch));
                    _mm256_storeu_ps(out + ch, _mm256_max_ps(top, bottom));
                }
            } else {
                const int rend = std::min(rstart + 2, input.rows);
                const int cend = std::min(cstart + 2, input.cols);
                int ch = 0;
                for (; ch + 8 <= output.channels; ch += 8) {
                    __m256 max_value =
                        _mm256_loadu_ps(Ptr(input, rstart, cstart) + ch);
                    for (int r = rstart; r < rend; ++r) {
                        for (int c = cstart; c < cend; ++c) {
                            max_value = _mm256_max_ps(
                                max_value,
                                _mm256_loadu_ps(Ptr(input, r, c) + ch));
                        }
                    }
                    _mm256_storeu_ps(out + ch, max_value);
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
            }
        }
    }
#else
    MaxPool2x2S2Scalar(input, output);
#endif
}

}  // namespace hw
}  // namespace fdt
