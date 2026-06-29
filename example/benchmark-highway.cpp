#include <stdio.h>
#include <stdlib.h>

#include <opencv2/opencv.hpp>

#include <facedetection/facedetect_hw.h>

#ifdef _OPENMP
#include <omp.h>
#endif

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

#ifdef _OPENMP
    const int num_threads = omp_get_num_procs();
    omp_set_num_threads(num_threads);
    printf("There are %d threads, %d processors.\n", num_threads,
           omp_get_num_procs());
#else
    const int num_threads = 1;
    printf("There is %d thread.\n", num_threads);
#endif

    unsigned char* buffers = static_cast<unsigned char*>(
        malloc(static_cast<size_t>(DETECT_BUFFER_SIZE) * num_threads));
    if (!buffers) {
        fprintf(stderr, "Can not alloc buffers.\n");
        return -1;
    }

    const int total_count = 256;
    facedetect_hw_cnn(buffers, image.ptr<unsigned char>(0), image.cols,
                      image.rows, static_cast<int>(image.step));

    cv::TickMeter timer;
    timer.start();
#ifdef _OPENMP
#pragma omp parallel for
#endif
    for (int i = 0; i < total_count; ++i) {
#ifdef _OPENMP
        const int thread_index = omp_get_thread_num();
#else
        const int thread_index = 0;
#endif
        unsigned char* buffer =
            buffers + static_cast<size_t>(DETECT_BUFFER_SIZE) * thread_index;
        facedetect_hw_cnn(buffer, image.ptr<unsigned char>(0), image.cols,
                          image.rows, static_cast<int>(image.step));
    }
    timer.stop();

    const double time_ms = timer.getTimeMilli() / total_count;
    printf("highway facedetection average time = %.2fms | %.2f FPS\n",
           time_ms, 1000.0 / time_ms);

    free(buffers);
    return 0;
}
