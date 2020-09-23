#ifndef UTILS_HPP
#define UTILS_HPP

#include <string>
#include <vector>
#include <algorithm>
#include "face_def.hpp"
#include "opencv2/opencv.hpp"

void nms(std::vector<Face>& dets,
         const float thresh = 0.3);
void draw(cv::Mat& img,
          const std::vector<Face>& faces);

#endif