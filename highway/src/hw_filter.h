#pragma once

#include "hw_blob.h"

namespace fdt {
namespace hw {

struct HwConvInfo {
    int channels;
    int num_filters;
    bool is_depthwise;
    bool is_pointwise;
    bool with_relu;
    const float* weights;
    const float* biases;
};

class HwFilter {
public:
    HwFilter();
    explicit HwFilter(const HwConvInfo& info);

    void Load(const HwConvInfo& info);

    bool IsSupported() const;
    bool IsPointwise() const;
    bool IsDepthwise() const;

    ConstBlobView WeightsView() const;
    const float* Biases() const;
    const PointwisePlan& HwPlan() const;
    const PointwisePlan& IntrinsicsPlan() const;

    int channels() const { return channels_; }
    int num_filters() const { return num_filters_; }
    bool with_relu() const { return with_relu_; }

private:
    int channels_;
    int num_filters_;
    bool is_depthwise_;
    bool is_pointwise_;
    bool with_relu_;
    HwBlob weights_;
    HwBlob biases_;
    PointwisePlan hw_plan_;
    PointwisePlan intrinsics_plan_;
};

}  // namespace hw
}  // namespace fdt
