#include "hw_filter.h"

#include <algorithm>
#include <cstring>

namespace fdt {
namespace hw {

HwFilter::HwFilter()
    : channels_(0),
      num_filters_(0),
      is_depthwise_(false),
      is_pointwise_(false),
      with_relu_(true),
      weights_(),
      biases_(),
      hw_plan_(),
      intrinsics_plan_() {}

HwFilter::HwFilter(const HwConvInfo& info) : HwFilter() {
    Load(info);
}

void HwFilter::Load(const HwConvInfo& info) {
    channels_ = info.channels;
    num_filters_ = info.num_filters;
    is_depthwise_ = info.is_depthwise;
    is_pointwise_ = info.is_pointwise;
    with_relu_ = info.with_relu;

    const int weight_cols = IsPointwise() ? num_filters_ : 9;
    weights_.Resize(1, weight_cols, channels_);
    biases_.Resize(1, 1, num_filters_);

    for (int col = 0; col < weight_cols; ++col) {
        std::memcpy(weights_.Ptr(0, col), info.weights + channels_ * col,
                    static_cast<size_t>(channels_) * sizeof(float));
    }
    std::memcpy(biases_.Ptr(0, 0), info.biases,
                static_cast<size_t>(num_filters_) * sizeof(float));

    hw_plan_ = PointwisePlan();
    intrinsics_plan_ = PointwisePlan();
    if (IsPointwise()) {
        hw_plan_ = CreatePointwisePlanHw(WeightsView(), Biases());
        intrinsics_plan_ = CreatePointwisePlanIntrinsics(WeightsView(), Biases());
    }
}

bool HwFilter::IsSupported() const {
    return IsPointwise() || IsDepthwise();
}

bool HwFilter::IsPointwise() const {
    return is_pointwise_ && !is_depthwise_;
}

bool HwFilter::IsDepthwise() const {
    return is_depthwise_ && !is_pointwise_;
}

ConstBlobView HwFilter::WeightsView() const {
    return weights_.View();
}

const float* HwFilter::Biases() const {
    return biases_.Ptr(0, 0);
}

const PointwisePlan& HwFilter::HwPlan() const {
    return hw_plan_;
}

const PointwisePlan& HwFilter::IntrinsicsPlan() const {
    return intrinsics_plan_;
}

}  // namespace hw
}  // namespace fdt
