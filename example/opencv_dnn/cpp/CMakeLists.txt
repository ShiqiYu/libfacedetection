cmake_minimum_required(VERSION 2.8)

project(libfacedetection_opencvdnn)

# OpenCV
find_package(OpenCV 4.5.1 REQUIRED)
include_directories(${OpenCV_INCLUDE_DIRS})

add_executable(detect detect.cpp priorbox.cpp utils.cpp)
target_link_libraries(detect ${OpenCV_LIBS})
