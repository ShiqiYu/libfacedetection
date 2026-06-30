#include <stdio.h>
#include <stdlib.h>

#include <opencv2/opencv.hpp>

#include <facedetection/facedetect_hw.h>

#define DETECT_BUFFER_SIZE FACEDETECTION_HW_RESULT_BUFFER_SIZE

int main(int argc, char* argv[]) {
    if (argc != 2) {
        printf("Usage: %s <image_file_name>\n", argv[0]);
        return -1;
    }

    cv::Mat image = cv::imread(argv[1]);
    if (image.empty()) {
        fprintf(stderr, "Can not load the image file %s.\n", argv[1]);
        return -1;
    }

    unsigned char* buffer =
        static_cast<unsigned char*>(malloc(DETECT_BUFFER_SIZE));
    if (!buffer) {
        fprintf(stderr, "Can not alloc buffer.\n");
        return -1;
    }

    cv::TickMeter timer;
    timer.start();
    int* results = facedetect_hw_cnn(buffer, image.ptr<unsigned char>(0),
                                     image.cols, image.rows,
                                     static_cast<int>(image.step));
    timer.stop();

    printf("highway time = %gms\n", timer.getTimeMilli());
    printf("%d faces detected.\n", results ? *results : 0);

    cv::Mat result_image = image.clone();
    for (int i = 0; i < (results ? *results : 0); ++i) {
        short* face = reinterpret_cast<short*>(results + 1) +
                      FACEDETECTION_HW_RESULT_STRIDE_SHORTS * i;
        const int confidence = face[0];
        const int x = face[1];
        const int y = face[2];
        const int w = face[3];
        const int h = face[4];

        char score[32];
        snprintf(score, sizeof(score), "%d", confidence);
        cv::putText(result_image, score, cv::Point(x, y - 3),
                    cv::FONT_HERSHEY_SIMPLEX, 0.5, cv::Scalar(0, 255, 0), 1);
        cv::rectangle(result_image, cv::Rect(x, y, w, h),
                      cv::Scalar(0, 255, 0), 2);

        cv::circle(result_image, cv::Point(face[5], face[6]), 1,
                   cv::Scalar(255, 0, 0), 2);
        cv::circle(result_image, cv::Point(face[7], face[8]), 1,
                   cv::Scalar(0, 0, 255), 2);
        cv::circle(result_image, cv::Point(face[9], face[10]), 1,
                   cv::Scalar(0, 255, 0), 2);
        cv::circle(result_image, cv::Point(face[11], face[12]), 1,
                   cv::Scalar(255, 0, 255), 2);
        cv::circle(result_image, cv::Point(face[13], face[14]), 1,
                   cv::Scalar(0, 255, 255), 2);

        printf("face %d: confidence=%d, [%d, %d, %d, %d]\n", i,
               confidence, x, y, w, h);
    }

    cv::imshow("highway result", result_image);
    cv::waitKey();

    free(buffer);
    return 0;
}
