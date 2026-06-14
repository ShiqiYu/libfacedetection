#pragma once

#include <stddef.h>
#include <vector>

namespace fdt {
namespace hw {

struct BlobView {
    float* data;
    int rows;
    int cols;
    int channels;
    int channel_step;
};

struct ConstBlobView {
    const float* data;
    int rows;
    int cols;
    int channels;
    int channel_step;
};

struct PackedPointwiseFilter {
    int channels;
    int out_channels;
    int padded_out_channels;
    std::vector<float> weights;
    std::vector<float> biases;
};

enum class PointwiseStrategy {
    kPrimitive,
    kPacked,
};

struct PointwisePlan {
    PointwiseStrategy strategy;
    PackedPointwiseFilter packed;
};

inline float* Ptr(const BlobView& blob, int row, int col) {
    return blob.data + (static_cast<size_t>(row) * blob.cols + col) *
                           static_cast<size_t>(blob.channel_step);
}

inline const float* Ptr(const ConstBlobView& blob, int row, int col) {
    return blob.data + (static_cast<size_t>(row) * blob.cols + col) *
                           static_cast<size_t>(blob.channel_step);
}

PackedPointwiseFilter PackPointwiseFilter(const ConstBlobView& weights,
                                          const float* biases,
                                          int lane_count);
bool ShouldUsePackedPointwise(int channels, int out_channels);
PointwisePlan CreatePointwisePlanHw(const ConstBlobView& weights,
                                    const float* biases);
PointwisePlan CreatePointwisePlanIntrinsics(const ConstBlobView& weights,
                                            const float* biases);

float DotProductScalar(const float* a, const float* b, int n);
void MulAddScalar(const float* a, const float* b, float* acc, int n);
void AddScalar(const float* a, const float* b, float* out, int n);
void AddInplaceScalar(const float* a, float* out, int n);
void ReluScalar(float* data, int n);
void Pointwise1x1Scalar(const ConstBlobView& input,
                        const ConstBlobView& weights,
                        const float* biases,
                        BlobView& output);
void Pointwise1x1PackedScalar(const ConstBlobView& input,
                              const PackedPointwiseFilter& filter,
                              BlobView& output);
void Depthwise3x3Scalar(const ConstBlobView& input,
                        const ConstBlobView& weights,
                        const float* biases,
                        BlobView& output);
void MaxPool2x2S2Scalar(const ConstBlobView& input, BlobView& output);

float DotProductIntrinsics(const float* a, const float* b, int n);
int IntrinsicsFloatLanes();
void MulAddIntrinsics(const float* a, const float* b, float* acc, int n);
void AddIntrinsics(const float* a, const float* b, float* out, int n);
void AddInplaceIntrinsics(const float* a, float* out, int n);
void ReluIntrinsics(float* data, int n);
void Pointwise1x1Intrinsics(const ConstBlobView& input,
                            const ConstBlobView& weights,
                            const float* biases,
                            BlobView& output);
void Pointwise1x1PackedIntrinsics(const ConstBlobView& input,
                                  const PackedPointwiseFilter& filter,
                                  BlobView& output);
void Pointwise1x1PlannedIntrinsics(const ConstBlobView& input,
                                   const ConstBlobView& weights,
                                   const float* biases,
                                   const PointwisePlan& plan,
                                   BlobView& output);
void Depthwise3x3Intrinsics(const ConstBlobView& input,
                            const ConstBlobView& weights,
                            const float* biases,
                            BlobView& output);
void Depthwise3x3IntrinsicsRelu(const ConstBlobView& input,
                                const ConstBlobView& weights,
                                const float* biases,
                                BlobView& output);
void MaxPool2x2S2Intrinsics(const ConstBlobView& input, BlobView& output);

float DotProductHw(const float* a, const float* b, int n);
int HwFloatLanes();
void MulAddHw(const float* a, const float* b, float* acc, int n);
void AddHw(const float* a, const float* b, float* out, int n);
void AddInplaceHw(const float* a, float* out, int n);
void ReluHw(float* data, int n);
void Pointwise1x1Hw(const ConstBlobView& input,
                    const ConstBlobView& weights,
                    const float* biases,
                    BlobView& output);
void Pointwise1x1PackedHw(const ConstBlobView& input,
                          const PackedPointwiseFilter& filter,
                          BlobView& output);
void Pointwise1x1PlannedHw(const ConstBlobView& input,
                           const ConstBlobView& weights,
                           const float* biases,
                           const PointwisePlan& plan,
                           BlobView& output);
void Pointwise1x1PlannedHwRelu(const ConstBlobView& input,
                               const ConstBlobView& weights,
                               const float* biases,
                               const PointwisePlan& plan,
                               BlobView& output);
void Depthwise3x3Hw(const ConstBlobView& input,
                    const ConstBlobView& weights,
                    const float* biases,
                    BlobView& output);
void Depthwise3x3HwRelu(const ConstBlobView& input,
                        const ConstBlobView& weights,
                        const float* biases,
                        BlobView& output);
void MaxPool2x2S2Hw(const ConstBlobView& input, BlobView& output);

}  // namespace hw
}  // namespace fdt
