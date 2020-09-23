#ifndef FACE_DEF_HPP
#define FACE_DEF_HPP

#pragma once

#include "opencv2/opencv.hpp"

// center: [x, y], width: w, height: h
typedef struct BndBox_xywh {
    cv::Point2f center;
    float w;
    float h;
} BndBox_xywh;

// top_left: [x1, y1], bottom_right: [x2, y2]
typedef struct BndBox_xyxy {
    cv::Point2f top_left;
    cv::Point2f bottom_right;
    float area() {
        return (bottom_right.x - top_left.x + 1) * (bottom_right.y - top_left.y + 1);
    }
} BndBox_xyxy;

typedef struct Landmarks_10 {
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
} Landmarks_10;

typedef struct Face {
    BndBox_xyxy bbox;
    Landmarks_10 landmarks;
    float score;
} Face;

#endif 