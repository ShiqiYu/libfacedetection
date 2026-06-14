#include "hw_network.h"

#include <utility>

namespace fdt {
namespace hw {
namespace {

void Convolution4LayerUnitHwTo(const HwBlob& input,
                               const HwFilter& pointwise1,
                               const HwFilter& depthwise1,
                               const HwFilter& pointwise2,
                               const HwFilter& depthwise2,
                               bool do_relu,
                               HwBlob* pointwise_workspace,
                               HwBlob* block_workspace,
                               HwBlob* output) {
    ConvolutionDepthwisePointwiseHwTo(input, pointwise1, depthwise1, true,
                                      pointwise_workspace, block_workspace);
    ConvolutionDepthwisePointwiseHwTo(*block_workspace, pointwise2, depthwise2,
                                      do_relu, pointwise_workspace, output);
}

void ForwardHeadsFromBlobsTo(HwBlob* fb1,
                             HwBlob* fb2,
                             HwBlob* fb3,
                             const HwModel& model,
                             HwNetworkWorkspace* workspace,
                             HwHeadOutputs* outputs) {
    HwNetworkWorkspace local_workspace;
    HwNetworkWorkspace& ws = workspace ? *workspace : local_workspace;

    ConvolutionDepthwisePointwiseHwTo(*fb3, model.Filter(27), model.Filter(28),
                                      true, &ws.head_pointwise,
                                      &ws.head_branch);
    std::swap(*fb3, ws.head_branch);
    ConvolutionDepthwisePointwiseHwTo(*fb3, model.Filter(33), model.Filter(34),
                                      false, &ws.head_pointwise,
                                      &outputs->cls[2]);
    ConvolutionDepthwisePointwiseHwTo(*fb3, model.Filter(39), model.Filter(40),
                                      false, &ws.head_pointwise,
                                      &outputs->reg[2]);
    ConvolutionDepthwisePointwiseHwTo(*fb3, model.Filter(51), model.Filter(52),
                                      false, &ws.head_pointwise,
                                      &outputs->kps[2]);
    ConvolutionDepthwisePointwiseHwTo(*fb3, model.Filter(45), model.Filter(46),
                                      false, &ws.head_pointwise,
                                      &outputs->obj[2]);

    UpsampleX2AddHwTo(*fb3, *fb2, &ws.head_branch);
    std::swap(*fb2, ws.head_branch);
    ConvolutionDepthwisePointwiseHwTo(*fb2, model.Filter(25), model.Filter(26),
                                      true, &ws.head_pointwise,
                                      &ws.head_branch);
    std::swap(*fb2, ws.head_branch);
    ConvolutionDepthwisePointwiseHwTo(*fb2, model.Filter(31), model.Filter(32),
                                      false, &ws.head_pointwise,
                                      &outputs->cls[1]);
    ConvolutionDepthwisePointwiseHwTo(*fb2, model.Filter(37), model.Filter(38),
                                      false, &ws.head_pointwise,
                                      &outputs->reg[1]);
    ConvolutionDepthwisePointwiseHwTo(*fb2, model.Filter(49), model.Filter(50),
                                      false, &ws.head_pointwise,
                                      &outputs->kps[1]);
    ConvolutionDepthwisePointwiseHwTo(*fb2, model.Filter(43), model.Filter(44),
                                      false, &ws.head_pointwise,
                                      &outputs->obj[1]);

    UpsampleX2AddHwTo(*fb2, *fb1, &ws.head_branch);
    std::swap(*fb1, ws.head_branch);
    ConvolutionDepthwisePointwiseHwTo(*fb1, model.Filter(23), model.Filter(24),
                                      true, &ws.head_pointwise,
                                      &ws.head_branch);
    std::swap(*fb1, ws.head_branch);
    ConvolutionDepthwisePointwiseHwTo(*fb1, model.Filter(29), model.Filter(30),
                                      false, &ws.head_pointwise,
                                      &outputs->cls[0]);
    ConvolutionDepthwisePointwiseHwTo(*fb1, model.Filter(35), model.Filter(36),
                                      false, &ws.head_pointwise,
                                      &outputs->reg[0]);
    ConvolutionDepthwisePointwiseHwTo(*fb1, model.Filter(47), model.Filter(48),
                                      false, &ws.head_pointwise,
                                      &outputs->kps[0]);
    ConvolutionDepthwisePointwiseHwTo(*fb1, model.Filter(41), model.Filter(42),
                                      false, &ws.head_pointwise,
                                      &outputs->obj[0]);
}

HwHeadOutputs ForwardHeadsFromBlobs(HwBlob fb1,
                                    HwBlob fb2,
                                    HwBlob fb3,
                                    const HwModel& model,
                                    HwNetworkWorkspace* workspace) {
    HwHeadOutputs outputs;
    ForwardHeadsFromBlobsTo(&fb1, &fb2, &fb3, model, workspace, &outputs);
    return outputs;
}

}  // namespace

HwBlob Convolution4LayerUnitHw(const HwBlob& input,
                               const HwFilter& pointwise1,
                               const HwFilter& depthwise1,
                               const HwFilter& pointwise2,
                               const HwFilter& depthwise2,
                               bool do_relu) {
    HwBlob tmp =
        ConvolutionDepthwisePointwiseHw(input, pointwise1, depthwise1, true);
    return ConvolutionDepthwisePointwiseHw(tmp, pointwise2, depthwise2,
                                           do_relu);
}

HwBackboneOutputs ForwardBackboneHw(const HwBlob& input, const HwModel& model) {
    return ForwardBackboneHw(input, model, 0);
}

HwBackboneOutputs ForwardBackboneHw(const HwBlob& input,
                                    const HwModel& model,
                                    HwNetworkWorkspace* workspace) {
    HwBackboneOutputs outputs;
    ForwardBackboneHwTo(input, model, workspace, &outputs);
    return outputs;
}

void ForwardBackboneHwTo(const HwBlob& input,
                         const HwModel& model,
                         HwNetworkWorkspace* workspace,
                         HwBackboneOutputs* outputs) {
    HwNetworkWorkspace local_workspace;
    HwNetworkWorkspace& ws = workspace ? *workspace : local_workspace;
    HwBlob& fx = ws.backbone_fx;

    ConvolutionHwTo(input, model.Filter(0), true, &fx);
    ConvolutionDepthwisePointwiseHwTo(fx, model.Filter(1), model.Filter(2),
                                      true, &ws.backbone_pointwise,
                                      &ws.backbone_block);
    std::swap(fx, ws.backbone_block);
    MaxPooling2x2S2HwTo(fx, &ws.backbone_block);
    std::swap(fx, ws.backbone_block);

    Convolution4LayerUnitHwTo(fx, model.Filter(3), model.Filter(4),
                              model.Filter(5), model.Filter(6), true,
                              &ws.backbone_pointwise, &ws.backbone_block,
                              &ws.backbone_output);
    std::swap(fx, ws.backbone_output);
    Convolution4LayerUnitHwTo(fx, model.Filter(7), model.Filter(8),
                              model.Filter(9), model.Filter(10), true,
                              &ws.backbone_pointwise, &ws.backbone_block,
                              &ws.backbone_output);
    std::swap(fx, ws.backbone_output);

    MaxPooling2x2S2HwTo(fx, &ws.backbone_block);
    std::swap(fx, ws.backbone_block);
    Convolution4LayerUnitHwTo(fx, model.Filter(11), model.Filter(12),
                              model.Filter(13), model.Filter(14), true,
                              &ws.backbone_pointwise, &ws.backbone_block,
                              &outputs->fb1);

    MaxPooling2x2S2HwTo(outputs->fb1, &fx);
    Convolution4LayerUnitHwTo(fx, model.Filter(15), model.Filter(16),
                              model.Filter(17), model.Filter(18), true,
                              &ws.backbone_pointwise, &ws.backbone_block,
                              &outputs->fb2);

    MaxPooling2x2S2HwTo(outputs->fb2, &fx);
    Convolution4LayerUnitHwTo(fx, model.Filter(19), model.Filter(20),
                              model.Filter(21), model.Filter(22), true,
                              &ws.backbone_pointwise, &ws.backbone_block,
                              &outputs->fb3);
}

HwHeadOutputs ForwardHeadsHw(const HwBackboneOutputs& backbone,
                             const HwModel& model) {
    return ForwardHeadsFromBlobs(backbone.fb1, backbone.fb2, backbone.fb3,
                                 model, 0);
}

HwHeadOutputs ForwardHeadsHw(HwBackboneOutputs&& backbone,
                             const HwModel& model) {
    return ForwardHeadsFromBlobs(std::move(backbone.fb1),
                                 std::move(backbone.fb2),
                                 std::move(backbone.fb3), model, 0);
}

HwHeadOutputs ForwardHeadsHw(HwBackboneOutputs&& backbone,
                             const HwModel& model,
                             HwNetworkWorkspace* workspace) {
    return ForwardHeadsFromBlobs(std::move(backbone.fb1),
                                 std::move(backbone.fb2),
                                 std::move(backbone.fb3), model, workspace);
}

void ForwardHeadsHwTo(HwBackboneOutputs* backbone,
                      const HwModel& model,
                      HwNetworkWorkspace* workspace,
                      HwHeadOutputs* outputs) {
    ForwardHeadsFromBlobsTo(&backbone->fb1, &backbone->fb2, &backbone->fb3,
                            model, workspace, outputs);
}

HwHeadOutputs ForwardNetworkHw(const HwBlob& input, const HwModel& model) {
    return ForwardHeadsHw(ForwardBackboneHw(input, model), model);
}

HwHeadOutputs ForwardNetworkHw(const HwBlob& input,
                               const HwModel& model,
                               HwNetworkWorkspace* workspace) {
    return ForwardHeadsHw(ForwardBackboneHw(input, model, workspace), model,
                          workspace);
}

void ForwardNetworkHwTo(const HwBlob& input,
                        const HwModel& model,
                        HwNetworkWorkspace* workspace,
                        HwHeadOutputs* outputs) {
    HwNetworkWorkspace local_workspace;
    HwNetworkWorkspace& ws = workspace ? *workspace : local_workspace;
    ForwardBackboneHwTo(input, model, &ws, &ws.backbone_outputs);
    ForwardHeadsHwTo(&ws.backbone_outputs, model, &ws, outputs);
}

}  // namespace hw
}  // namespace fdt
