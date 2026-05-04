#pragma once

#include "hw_filter.h"

namespace fdt {
namespace hw {

HwBlob ConvolutionScalar(const HwBlob& input, const HwFilter& filter);
HwBlob ConvolutionScalar(const HwBlob& input,
                         const HwFilter& filter,
                         bool do_relu);
HwBlob ConvolutionHw(const HwBlob& input, const HwFilter& filter);
HwBlob ConvolutionHw(const HwBlob& input, const HwFilter& filter, bool do_relu);
void ConvolutionHwTo(const HwBlob& input,
                     const HwFilter& filter,
                     bool do_relu,
                     HwBlob* output);
HwBlob ConvolutionIntrinsics(const HwBlob& input, const HwFilter& filter);
HwBlob ConvolutionIntrinsics(const HwBlob& input,
                             const HwFilter& filter,
                             bool do_relu);
HwBlob ConvolutionDepthwisePointwiseHw(const HwBlob& input,
                                       const HwFilter& pointwise,
                                       const HwFilter& depthwise);
HwBlob ConvolutionDepthwisePointwiseHw(const HwBlob& input,
                                       const HwFilter& pointwise,
                                       const HwFilter& depthwise,
                                       bool do_relu);
void ConvolutionDepthwisePointwiseHwTo(const HwBlob& input,
                                       const HwFilter& pointwise,
                                       const HwFilter& depthwise,
                                       bool do_relu,
                                       HwBlob* pointwise_workspace,
                                       HwBlob* output);
HwBlob MaxPooling2x2S2Hw(const HwBlob& input);
void MaxPooling2x2S2HwTo(const HwBlob& input, HwBlob* output);
HwBlob ElementAddHw(const HwBlob& lhs, const HwBlob& rhs);
HwBlob UpsampleX2Hw(const HwBlob& input);
HwBlob UpsampleX2AddHw(const HwBlob& input, const HwBlob& lateral);
void UpsampleX2AddHwTo(const HwBlob& input,
                       const HwBlob& lateral,
                       HwBlob* output);

}  // namespace hw
}  // namespace fdt
