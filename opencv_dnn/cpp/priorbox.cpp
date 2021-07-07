#include "priorbox.hpp"

PriorBox::PriorBox(const cv::Size& input_shape,
                   const cv::Size& output_shape) {
    // initialize
    in_w = input_shape.width;
    in_h = input_shape.height;
    out_w = output_shape.width;
    out_h = output_shape.height;

    cv::Size feature_map_2nd = {
        int(int((in_w+1)/2)/2), int(int((in_h+1)/2)/2)
    };
    cv::Size feature_map_3rd = {
        int(feature_map_2nd.width/2), int(feature_map_2nd.height/2)
    };
    cv::Size feature_map_4th = {
        int(feature_map_3rd.width/2), int(feature_map_3rd.height/2)
    };
    cv::Size feature_map_5th = {
        int(feature_map_4th.width/2), int(feature_map_4th.height/2)
    };
    cv::Size feature_map_6th = {
        int(feature_map_5th.width/2), int(feature_map_5th.height/2)
    };

    // feature_map_sizes.push_back(feature_map_2nd);
    feature_map_sizes.push_back(feature_map_3rd);
    feature_map_sizes.push_back(feature_map_4th);
    feature_map_sizes.push_back(feature_map_5th);
    feature_map_sizes.push_back(feature_map_6th);

    priors = generate_priors();
}

PriorBox::~PriorBox() {}

std::vector<Box> PriorBox::generate_priors() {
    std::vector<Box> anchors;
    for (auto i = 0; i < feature_map_sizes.size(); ++i) {
        cv::Size feature_map_size = feature_map_sizes[i];
        std::vector<float> min_size = min_sizes[i];

        for (auto _h = 0; _h < feature_map_size.height; ++_h) {
            for (auto _w = 0; _w < feature_map_size.width; ++_w) {
                for (auto j = 0; j < min_size.size(); ++j) {
                    float s_kx = min_size[j] / in_w;
                    float s_ky = min_size[j] / in_h;

                    float cx = (_w + 0.5) * steps[i] / in_w;
                    float cy = (_h + 0.5) * steps[i] / in_h;

                    Box anchor = { cx, cy, s_kx, s_ky };
                    anchors.push_back(anchor);
                }
            }
        }
    }
    return anchors;
}

std::vector<Face> PriorBox::decode(const cv::Mat& loc,
                                   const cv::Mat& conf,
                                   const cv::Mat& iou,
                                   const float ignore_score) {
    std::vector<Face> dets; // num * [x1, y1, x2, y2, x_re, y_re, x_le, y_le, x_ml, y_ml, x_n, y_n, x_mr, y_ml]

    float* loc_v = (float*)(loc.data);
    float* conf_v = (float*)(conf.data);
    float* iou_v = (float*)(iou.data);
    for (auto i = 0; i < priors.size(); ++i) {
        // get score
        float cls_score = conf_v[i*2+1];
        float iou_score = iou_v[i];
        // clamp
        if (iou_score < 0.f) {
            iou_score = 0.f;
        }
        else if (iou_score > 1.f) {
            iou_score = 1.f;
        }
        float score = std::sqrt(cls_score * iou_score);

        // ignore low scores
        if (score < ignore_score) { continue; }

        Face face;
        face.score = score;
        // get bounding box
        float cx = (priors[i].x + loc_v[i*14+0] * variance[0] * priors[i].width) * out_w;
        float cy = (priors[i].y + loc_v[i*14+1] * variance[0] * priors[i].height) * out_h;
        float w  = priors[i].width * exp(loc_v[i*14+2] * variance[0]) * out_w;
        float h  = priors[i].height * exp(loc_v[i*14+3] * variance[1]) * out_h;
        float x1 = cx - w / 2;
        float y1 = cy - h / 2;
        face.bbox_tlwh = { x1, y1, w, h };

        // get landmarks, loc->[right_eye, left_eye, mouth_left, nose, mouth_right]
        float x_re = (priors[i].x + loc_v[i*14+ 4] * variance[0] * priors[i].width) *  out_w;
        float y_re = (priors[i].y + loc_v[i*14+ 5] * variance[0] * priors[i].height) * out_h;
        float x_le = (priors[i].x + loc_v[i*14+ 6] * variance[0] * priors[i].width) *  out_w;
        float y_le = (priors[i].y + loc_v[i*14+ 7] * variance[0] * priors[i].height) * out_h;
        float x_n =  (priors[i].x + loc_v[i*14+ 8] * variance[0] * priors[i].width) *  out_w;
        float y_n =  (priors[i].y + loc_v[i*14+ 9] * variance[0] * priors[i].height) * out_h;
        float x_mr = (priors[i].x + loc_v[i*14+10] * variance[0] * priors[i].width) *  out_w;
        float y_mr = (priors[i].y + loc_v[i*14+11] * variance[0] * priors[i].height) * out_h;
        float x_ml = (priors[i].x + loc_v[i*14+12] * variance[0] * priors[i].width) *  out_w;
        float y_ml = (priors[i].y + loc_v[i*14+13] * variance[0] * priors[i].height) * out_h;
        face.landmarks = {
            {x_re, y_re},  // right eye
            {x_le, y_le},  // left eye
            {x_n,  y_n },  // nose
            {x_mr, y_mr},  // mouth right
            {x_ml, y_ml}   // mouth left
        };

        dets.push_back(face);
    }

    return dets;
}
