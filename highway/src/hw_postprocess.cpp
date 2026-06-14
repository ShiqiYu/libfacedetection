#include "hw_postprocess.h"

#include <algorithm>
#include <cmath>
#include <cstring>
#include <utility>

namespace fdt {
namespace hw {
namespace {

struct HwNormalizedBBox {
    float xmin;
    float ymin;
    float xmax;
    float ymax;
    float lm[10];
};

void IntersectBBoxHw(const HwNormalizedBBox& bbox1,
                     const HwNormalizedBBox& bbox2,
                     HwNormalizedBBox* intersect_bbox) {
    if (bbox2.xmin > bbox1.xmax || bbox2.xmax < bbox1.xmin ||
        bbox2.ymin > bbox1.ymax || bbox2.ymax < bbox1.ymin) {
        intersect_bbox->xmin = 0.0f;
        intersect_bbox->ymin = 0.0f;
        intersect_bbox->xmax = 0.0f;
        intersect_bbox->ymax = 0.0f;
    } else {
        intersect_bbox->xmin = std::max(bbox1.xmin, bbox2.xmin);
        intersect_bbox->ymin = std::max(bbox1.ymin, bbox2.ymin);
        intersect_bbox->xmax = std::min(bbox1.xmax, bbox2.xmax);
        intersect_bbox->ymax = std::min(bbox1.ymax, bbox2.ymax);
    }
}

float JaccardOverlapHw(const HwNormalizedBBox& bbox1,
                       const HwNormalizedBBox& bbox2) {
    HwNormalizedBBox intersect_bbox;
    IntersectBBoxHw(bbox1, bbox2, &intersect_bbox);
    const float intersect_width = intersect_bbox.xmax - intersect_bbox.xmin;
    const float intersect_height = intersect_bbox.ymax - intersect_bbox.ymin;
    if (intersect_width <= 0.0f || intersect_height <= 0.0f) {
        return 0.0f;
    }

    const float intersect_size = intersect_width * intersect_height;
    const float bbox1_size = (bbox1.xmax - bbox1.xmin) *
                             (bbox1.ymax - bbox1.ymin);
    const float bbox2_size = (bbox2.xmax - bbox2.xmin) *
                             (bbox2.ymax - bbox2.ymin);
    return intersect_size / (bbox1_size + bbox2_size - intersect_size);
}

bool SortScoreBBoxPairDescendHw(
    const std::pair<float, HwNormalizedBBox>& lhs,
    const std::pair<float, HwNormalizedBBox>& rhs) {
    return lhs.first > rhs.first;
}

void AppendFlattened(const HwBlob& input, float** out) {
    for (int row = 0; row < input.rows(); ++row) {
        for (int col = 0; col < input.cols(); ++col) {
            const float* in = input.Ptr(row, col);
            std::memcpy(*out, in,
                        static_cast<size_t>(input.channels()) * sizeof(float));
            *out += input.channels();
        }
    }
}

void Flatten3To(const HwBlob& input1,
                const HwBlob& input2,
                const HwBlob& input3,
                HwBlob* output) {
    const int channels = input1.rows() * input1.cols() * input1.channels() +
                         input2.rows() * input2.cols() * input2.channels() +
                         input3.rows() * input3.cols() * input3.channels();
    output->ResizeForOverwrite(1, 1, channels);
    float* out = output->Ptr(0, 0);
    AppendFlattened(input1, &out);
    AppendFlattened(input2, &out);
    AppendFlattened(input3, &out);
    std::fill(out, output->Ptr(0, 0) + output->channel_step(), 0.0f);
}

}  // namespace

HwBlob MeshgridHw(int feature_width,
                  int feature_height,
                  int stride,
                  float offset) {
    HwBlob output(feature_height, feature_width, 2);
    for (int row = 0; row < feature_height; ++row) {
        const float y = static_cast<float>(row * stride) + offset;
        for (int col = 0; col < feature_width; ++col) {
            float* out = output.Ptr(row, col);
            out[0] = static_cast<float>(col * stride) + offset;
            out[1] = y;
        }
    }
    return output;
}

void BboxDecodeHw(HwBlob& bbox_pred, const HwBlob& priors, int stride) {
    const float fstride = static_cast<float>(stride);
    for (int row = 0; row < bbox_pred.rows(); ++row) {
        for (int col = 0; col < bbox_pred.cols(); ++col) {
            float* bbox = bbox_pred.Ptr(row, col);
            const float* prior = priors.Ptr(row, col);
            const float cx = bbox[0] * fstride + prior[0];
            const float cy = bbox[1] * fstride + prior[1];
            const float width = std::exp(bbox[2]) * fstride;
            const float height = std::exp(bbox[3]) * fstride;
            bbox[0] = cx - width / 2.0f;
            bbox[1] = cy - height / 2.0f;
            bbox[2] = cx + width / 2.0f;
            bbox[3] = cy + height / 2.0f;
        }
    }
}

void KpsDecodeHw(HwBlob& kps_pred, const HwBlob& priors, int stride) {
    const float fstride = static_cast<float>(stride);
    const int num_points = kps_pred.channels() >> 1;
    for (int row = 0; row < kps_pred.rows(); ++row) {
        for (int col = 0; col < kps_pred.cols(); ++col) {
            float* kps = kps_pred.Ptr(row, col);
            const float* prior = priors.Ptr(row, col);
            for (int point = 0; point < num_points; ++point) {
                kps[2 * point] = kps[2 * point] * fstride + prior[0];
                kps[2 * point + 1] =
                    kps[2 * point + 1] * fstride + prior[1];
            }
        }
    }
}

void SigmoidHw(HwBlob& input) {
    for (int row = 0; row < input.rows(); ++row) {
        for (int col = 0; col < input.cols(); ++col) {
            float* values = input.Ptr(row, col);
            for (int ch = 0; ch < input.channels(); ++ch) {
                float value = std::min(values[ch], 88.3762626647949f);
                value = std::max(value, -88.3762626647949f);
                values[ch] =
                    static_cast<float>(1.0f / (1.0f + std::exp(-value)));
            }
        }
    }
}

HwBlob BlobToVectorHw(const HwBlob& input) {
    HwBlob output(1, 1, input.rows() * input.cols() * input.channels());
    float* out = output.Ptr(0, 0);
    for (int row = 0; row < input.rows(); ++row) {
        for (int col = 0; col < input.cols(); ++col) {
            const float* in = input.Ptr(row, col);
            std::memcpy(out, in,
                        static_cast<size_t>(input.channels()) * sizeof(float));
            out += input.channels();
        }
    }
    return output;
}

HwBlob Concat3Hw(const HwBlob& input1,
                 const HwBlob& input2,
                 const HwBlob& input3) {
    HwBlob output(input1.rows(), input1.cols(),
                  input1.channels() + input2.channels() + input3.channels());
    for (int row = 0; row < output.rows(); ++row) {
        for (int col = 0; col < output.cols(); ++col) {
            float* out = output.Ptr(row, col);
            const float* in1 = input1.Ptr(row, col);
            const float* in2 = input2.Ptr(row, col);
            const float* in3 = input3.Ptr(row, col);
            std::memcpy(out, in1,
                        static_cast<size_t>(input1.channels()) * sizeof(float));
            out += input1.channels();
            std::memcpy(out, in2,
                        static_cast<size_t>(input2.channels()) * sizeof(float));
            out += input2.channels();
            std::memcpy(out, in3,
                        static_cast<size_t>(input3.channels()) * sizeof(float));
        }
    }
    return output;
}

HwDecodedOutputs DecodeAndConcatHw(HwHeadOutputs heads) {
    HwDecodedOutputs decoded;
    DecodeAndConcatHwTo(std::move(heads), &decoded);
    return decoded;
}

void DecodeAndConcatHwTo(HwHeadOutputs heads, HwDecodedOutputs* decoded) {
    DecodeAndConcatHwTo(&heads, decoded);
}

void DecodeAndConcatHwTo(HwHeadOutputs* heads, HwDecodedOutputs* decoded) {
    HwBlob prior3 =
        MeshgridHw(heads->cls[0].cols(), heads->cls[0].rows(), 8);
    HwBlob prior4 =
        MeshgridHw(heads->cls[1].cols(), heads->cls[1].rows(), 16);
    HwBlob prior5 =
        MeshgridHw(heads->cls[2].cols(), heads->cls[2].rows(), 32);

    BboxDecodeHw(heads->reg[0], prior3, 8);
    BboxDecodeHw(heads->reg[1], prior4, 16);
    BboxDecodeHw(heads->reg[2], prior5, 32);
    KpsDecodeHw(heads->kps[0], prior3, 8);
    KpsDecodeHw(heads->kps[1], prior4, 16);
    KpsDecodeHw(heads->kps[2], prior5, 32);

    Flatten3To(heads->cls[0], heads->cls[1], heads->cls[2], &decoded->cls);
    Flatten3To(heads->reg[0], heads->reg[1], heads->reg[2], &decoded->reg);
    Flatten3To(heads->kps[0], heads->kps[1], heads->kps[2], &decoded->kps);
    Flatten3To(heads->obj[0], heads->obj[1], heads->obj[2], &decoded->obj);
    SigmoidHw(decoded->cls);
    SigmoidHw(decoded->obj);
}

std::vector<HwFaceRect> DetectionOutputHw(const HwBlob& cls,
                                          const HwBlob& reg,
                                          const HwBlob& kps,
                                          const HwBlob& obj,
                                          float overlap_threshold,
                                          float confidence_threshold,
                                          int top_k,
                                          int keep_top_k) {
    const float* cls_data = cls.Ptr(0, 0);
    const float* reg_data = reg.Ptr(0, 0);
    const float* kps_data = kps.Ptr(0, 0);
    const float* obj_data = obj.Ptr(0, 0);

    std::vector<std::pair<float, HwNormalizedBBox> > score_bbox_vec;
    std::vector<std::pair<float, HwNormalizedBBox> > final_score_bbox_vec;

    for (int i = 0; i < cls.channels(); ++i) {
        const float confidence = std::sqrt(cls_data[i] * obj_data[i]);
        if (confidence >= confidence_threshold) {
            HwNormalizedBBox bbox;
            bbox.xmin = reg_data[4 * i];
            bbox.ymin = reg_data[4 * i + 1];
            bbox.xmax = reg_data[4 * i + 2];
            bbox.ymax = reg_data[4 * i + 3];
            std::memcpy(bbox.lm, kps_data + 10 * i, 10 * sizeof(float));
            score_bbox_vec.push_back(std::make_pair(confidence, bbox));
        }
    }

    std::stable_sort(score_bbox_vec.begin(), score_bbox_vec.end(),
                     SortScoreBBoxPairDescendHw);
    if (top_k > -1 && static_cast<size_t>(top_k) < score_bbox_vec.size()) {
        score_bbox_vec.resize(static_cast<size_t>(top_k));
    }

    while (!score_bbox_vec.empty()) {
        const HwNormalizedBBox bbox = score_bbox_vec.front().second;
        bool keep = true;
        for (size_t i = 0; i < final_score_bbox_vec.size(); ++i) {
            if (!keep) {
                break;
            }
            keep = JaccardOverlapHw(bbox, final_score_bbox_vec[i].second) <=
                   overlap_threshold;
        }
        if (keep) {
            final_score_bbox_vec.push_back(score_bbox_vec.front());
        }
        score_bbox_vec.erase(score_bbox_vec.begin());
    }

    if (keep_top_k > -1 &&
        static_cast<size_t>(keep_top_k) < final_score_bbox_vec.size()) {
        final_score_bbox_vec.resize(static_cast<size_t>(keep_top_k));
    }

    std::vector<HwFaceRect> faces;
    faces.reserve(final_score_bbox_vec.size());
    for (size_t i = 0; i < final_score_bbox_vec.size(); ++i) {
        const std::pair<float, HwNormalizedBBox>& item = final_score_bbox_vec[i];
        HwFaceRect rect;
        rect.score = item.first;
        rect.x = static_cast<int>(item.second.xmin);
        rect.y = static_cast<int>(item.second.ymin);
        rect.w = static_cast<int>(item.second.xmax - item.second.xmin);
        rect.h = static_cast<int>(item.second.ymax - item.second.ymin);
        for (int lm = 0; lm < 10; ++lm) {
            rect.lm[lm] = static_cast<int>(item.second.lm[lm]);
        }
        faces.push_back(rect);
    }
    return faces;
}

std::vector<HwFaceRect> DetectionOutputHw(const HwDecodedOutputs& decoded,
                                          float overlap_threshold,
                                          float confidence_threshold,
                                          int top_k,
                                          int keep_top_k) {
    return DetectionOutputHw(decoded.cls, decoded.reg, decoded.kps, decoded.obj,
                             overlap_threshold, confidence_threshold, top_k,
                             keep_top_k);
}

}  // namespace hw
}  // namespace fdt
