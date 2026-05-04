#pragma once

#include <stddef.h>

#define FACEDETECTION_HW_RESULT_BUFFER_SIZE 0x9000
#define FACEDETECTION_HW_RESULT_MAX_FACES 1024
#define FACEDETECTION_HW_RESULT_STRIDE_SHORTS 16

#ifdef __cplusplus
extern "C" {
#endif

int* facedetect_hw_cnn(unsigned char* result_buffer,
                       unsigned char* bgr_image_data,
                       int width,
                       int height,
                       int step);

#ifdef __cplusplus
}
#endif

