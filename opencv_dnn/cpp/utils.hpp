#ifndef UTILS_HPP
#define UTILS_HPP

#include <string>
#include <vector>
#include <algorithm>
#include "face_def.hpp"
#include "opencv2/opencv.hpp"

void draw(cv::Mat& img,
          const std::vector<Face>& faces);

#endif