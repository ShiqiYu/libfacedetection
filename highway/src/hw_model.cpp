#include "hw_model.h"

#include "facedetectcnn.h"

extern ConvInfoStruct param_pConvInfo[fdt::hw::kHwConvLayerCount];

namespace fdt {
namespace hw {

void HwModel::AddFilter(const HwConvInfo& info) {
    filters_.push_back(HwFilter(info));
}

const HwFilter& HwModel::Filter(int index) const {
    return filters_[static_cast<size_t>(index)];
}

HwModel LoadStaticModel() {
    HwModel model;
    for (int i = 0; i < kHwConvLayerCount; ++i) {
        const ConvInfoStruct& source = ::param_pConvInfo[i];
        const HwConvInfo info{
            source.channels,
            source.num_filters,
            source.is_depthwise,
            source.is_pointwise,
            source.with_relu,
            source.pWeights,
            source.pBiases};
        model.AddFilter(info);
    }
    return model;
}

}  // namespace hw
}  // namespace fdt
