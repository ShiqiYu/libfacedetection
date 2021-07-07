#ifndef PRIORBOX_HPP
#define PRIORBOX_HPP

#include <vector>
#include <iostream>
#include <cmath>
#include <algorithm>
#include "face_def.hpp"

class PriorBox {
private:
    const std::vector<std::vector<float>> min_sizes = {
        {10.0f,  16.0f,  24.0f},
        {32.0f,  48.0f},
        {64.0f,  96.0f},
        {128.0f, 192.0f, 256.0f}
    };
    const std::vector<int> steps = { 8, 16, 32, 64 };
    const std::vector<float> variance = {0.1, 0.2};

    int in_w;
    int in_h;
    int out_w;
    int out_h;

    std::vector<cv::Size> feature_map_sizes;
    std::vector<Box> priors;
private:
    std::vector<Box> generate_priors();
public:
    PriorBox(const cv::Size& input_shape,
             const cv::Size& output_shape);
    ~PriorBox();
    std::vector<Face> decode(const cv::Mat& loc, const cv::Mat& conf, const cv::Mat& iou, const float ignore_score=0.3);
};

#endif