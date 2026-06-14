#pragma once

#include "hw_filter.h"

#include <vector>

namespace fdt {
namespace hw {

const int kHwConvLayerCount = 53;

class HwModel {
public:
    void AddFilter(const HwConvInfo& info);

    size_t size() const { return filters_.size(); }
    bool Empty() const { return filters_.empty(); }

    const HwFilter& Filter(int index) const;

private:
    std::vector<HwFilter> filters_;
};

HwModel LoadStaticModel();

}  // namespace hw
}  // namespace fdt
