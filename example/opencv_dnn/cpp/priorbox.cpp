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

    // std::cout << "Before priors gen!" << std::endl;
    priors = generate_priors();
    // std::cout << "Finished priors gen!" << std::endl;
}

PriorBox::~PriorBox() {}

std::vector<BndBox_xywh> PriorBox::generate_priors() {
    // std::cout << "In!" << std::endl;
    std::vector<BndBox_xywh> anchors;
    // std::cout << "Before loop!" << std::endl;
    // std::cout << feature_map_sizes.size() << std::endl;
    for (auto i = 0; i < feature_map_sizes.size(); ++i) {
        // std::cout << "In for loop! " << i << std::endl;
        cv::Size feature_map_size = feature_map_sizes[i];
        std::vector<float> min_size = min_sizes[i];
        // std::cout << feature_map_size.height << " " << feature_map_size.width << std::endl;

        for (auto _h = 0; _h < feature_map_size.height; ++_h) {
            for (auto _w = 0; _w < feature_map_size.width; ++_w) {
                for (auto j = 0; j < min_size.size(); ++j) {
                    float s_kx = min_size[j] / in_w;
                    float s_ky = min_size[j] / in_h;

                    float cx = (_w + 0.5) * steps[i] / in_w;
                    float cy = (_h + 0.5) * steps[i] / in_h;

                    // if (i == 3) {
                    //     std::cout << _h << " " << _w << std::endl;
                    // }

                    BndBox_xywh anchor = { {cx, cy}, s_kx, s_ky };
                    anchors.push_back(anchor);
                }
            }
        }
    }
    // std::cout << "Out!" << std::endl;
    return anchors;
}

std::vector<Face> PriorBox::decode(const cv::Mat& loc,
                                   const cv::Mat& conf) {
    std::vector<Face> dets; // num * [x1, y1, x2, y2, x_re, y_re, x_le, y_le, x_ml, y_ml, x_n, y_n, x_mr, y_ml]

    float* loc_v = (float*)(loc.data);
    float* conf_v = (float*)(conf.data);
    for (auto i = 0; i < priors.size(); ++i) {
        float cx = priors[i].center.x + loc_v[i*14+0] * variance[0] * priors[i].w;
        float cy = priors[i].center.y + loc_v[i*14+1] * variance[0] * priors[i].h;
        float w  = priors[i].w * exp(loc_v[i*14+2] * variance[0]);
        float h  = priors[i].h * exp(loc_v[i*14+3] * variance[1]);

        // get bounding box
        float x1 = (cx - w / 2) * out_w;
        float y1 = (cy - h / 2) * out_h;
        float x2 = (cx + w / 2) * out_w;
        float y2 = (cy + h / 2) * out_h;
        BndBox_xyxy bbox = { {x1, y1}, {x2, y2} };
        // get landmarks, loc->[right_eye, left_eye, mouth_left, nose, mouth_right]
        float x_re = (priors[i].center.x + loc_v[i*14+ 4] * variance[0] * priors[i].w) * out_w;
        float y_re = (priors[i].center.y + loc_v[i*14+ 5] * variance[0] * priors[i].h) * out_h;
        float x_le = (priors[i].center.x + loc_v[i*14+ 6] * variance[0] * priors[i].w) * out_w;
        float y_le = (priors[i].center.y + loc_v[i*14+ 7] * variance[0] * priors[i].h) * out_h;
        float x_ml = (priors[i].center.x + loc_v[i*14+ 8] * variance[0] * priors[i].w) * out_w;
        float y_ml = (priors[i].center.y + loc_v[i*14+ 9] * variance[0] * priors[i].h) * out_h;
        float x_n  = (priors[i].center.x + loc_v[i*14+10] * variance[0] * priors[i].w) * out_w;
        float y_n  = (priors[i].center.y + loc_v[i*14+11] * variance[0] * priors[i].h) * out_h;
        float x_mr = (priors[i].center.x + loc_v[i*14+12] * variance[0] * priors[i].w) * out_w;
        float y_mr = (priors[i].center.y + loc_v[i*14+13] * variance[0] * priors[i].h) * out_h;
        Landmarks_5 landmarks = { {x_re, y_re}, // right eye
                                   {x_le, y_le}, // left eye
                                   {x_ml, y_ml}, // mouth left
                                   {x_n,  y_n },  // nose
                                   {x_mr, y_mr}  // mouth right
        };
        // get score
        float score = conf_v[i*2+1];


        Face det = { bbox, landmarks, score };
        dets.push_back(det);
    }

    return dets;
}