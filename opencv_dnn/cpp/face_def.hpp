#ifndef FACE_DEF_HPP
#define FACE_DEF_HPP

#pragma once

#include "opencv2/opencv.hpp"

// center/topleft: [x, y], width: w, height: h
typedef cv::Rect2f Box;

typedef struct Landmarks_5 {
    // right eye
    cv::Point2f right_eye;
    // left eye
    cv::Point2f left_eye;
    // mouth left
    cv::Point2f mouth_left;
    // nose
    cv::Point2f nose_tip;
    // mouth right
    cv::Point2f mouth_right;
} Landmarks_5;

typedef struct Face {
    Box bbox_tlwh;
    Landmarks_5 landmarks;
    float score;
} Face;

#endif 