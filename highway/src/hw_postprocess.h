#pragma once

#include "hw_blob.h"
#include "hw_network.h"

#include <vector>

namespace fdt {
namespace hw {

struct HwFaceRect {
    float score;
    int x;
    int y;
    int w;
    int h;
    int lm[10];
};

struct HwDecodedOutputs {
    HwBlob cls;
    HwBlob reg;
    HwBlob kps;
    HwBlob obj;
};

HwBlob MeshgridHw(int feature_width,
                  int feature_height,
                  int stride,
                  float offset = 0.0f);
void BboxDecodeHw(HwBlob& bbox_pred, const HwBlob& priors, int stride);
void KpsDecodeHw(HwBlob& kps_pred, const HwBlob& priors, int stride);
void SigmoidHw(HwBlob& input);
HwBlob BlobToVectorHw(const HwBlob& input);
HwBlob Concat3Hw(const HwBlob& input1,
                 const HwBlob& input2,
                 const HwBlob& input3);
HwDecodedOutputs DecodeAndConcatHw(HwHeadOutputs heads);
void DecodeAndConcatHwTo(HwHeadOutputs heads, HwDecodedOutputs* decoded);
void DecodeAndConcatHwTo(HwHeadOutputs* heads, HwDecodedOutputs* decoded);
std::vector<HwFaceRect> DetectionOutputHw(const HwBlob& cls,
                                          const HwBlob& reg,
                                          const HwBlob& kps,
                                          const HwBlob& obj,
                                          float overlap_threshold,
                                          float confidence_threshold,
                                          int top_k,
                                          int keep_top_k);
std::vector<HwFaceRect> DetectionOutputHw(const HwDecodedOutputs& decoded,
                                          float overlap_threshold,
                                          float confidence_threshold,
                                          int top_k,
                                          int keep_top_k);

}  // namespace hw
}  // namespace fdt
