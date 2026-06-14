#pragma once

#include "hw_layers.h"
#include "hw_model.h"

#include <array>

namespace fdt {
namespace hw {

struct HwBackboneOutputs {
    HwBlob fb1;
    HwBlob fb2;
    HwBlob fb3;
};

struct HwHeadOutputs {
    std::array<HwBlob, 3> cls;
    std::array<HwBlob, 3> reg;
    std::array<HwBlob, 3> kps;
    std::array<HwBlob, 3> obj;
};

struct HwNetworkWorkspace {
    HwBackboneOutputs backbone_outputs;
    HwBlob backbone_fx;
    HwBlob backbone_pointwise;
    HwBlob backbone_block;
    HwBlob backbone_output;
    HwBlob head_pointwise;
    HwBlob head_branch;
};

HwBlob Convolution4LayerUnitHw(const HwBlob& input,
                               const HwFilter& pointwise1,
                               const HwFilter& depthwise1,
                               const HwFilter& pointwise2,
                               const HwFilter& depthwise2,
                               bool do_relu);

HwBackboneOutputs ForwardBackboneHw(const HwBlob& input, const HwModel& model);
HwBackboneOutputs ForwardBackboneHw(const HwBlob& input,
                                    const HwModel& model,
                                    HwNetworkWorkspace* workspace);
void ForwardBackboneHwTo(const HwBlob& input,
                         const HwModel& model,
                         HwNetworkWorkspace* workspace,
                         HwBackboneOutputs* outputs);
HwHeadOutputs ForwardHeadsHw(const HwBackboneOutputs& backbone,
                             const HwModel& model);
HwHeadOutputs ForwardHeadsHw(HwBackboneOutputs&& backbone,
                             const HwModel& model);
HwHeadOutputs ForwardHeadsHw(HwBackboneOutputs&& backbone,
                             const HwModel& model,
                             HwNetworkWorkspace* workspace);
void ForwardHeadsHwTo(HwBackboneOutputs* backbone,
                      const HwModel& model,
                      HwNetworkWorkspace* workspace,
                      HwHeadOutputs* outputs);
HwHeadOutputs ForwardNetworkHw(const HwBlob& input, const HwModel& model);
HwHeadOutputs ForwardNetworkHw(const HwBlob& input,
                               const HwModel& model,
                               HwNetworkWorkspace* workspace);
void ForwardNetworkHwTo(const HwBlob& input,
                        const HwModel& model,
                        HwNetworkWorkspace* workspace,
                        HwHeadOutputs* outputs);

}  // namespace hw
}  // namespace fdt
