#include <stdio.h>
#include <opencv2/opencv.hpp>
#include <omp.h>
#include "facedetectcnn.h"

using namespace cv;
using namespace std;

//define the buffer size. Do not change the size!
#define DETECT_BUFFER_SIZE 0x20000
using namespace cv;

int main(int argc, char* argv[])
{
    if (argc != 2)
    {
        printf("Usage: %s <image_file_name>\n", argv[0]);
        return -1;
    }
    //load an image and convert it to gray (single-channel)
    Mat image = imread(argv[1]);
    if (image.empty())
    {
        fprintf(stderr, "Can not load the image file %s.\n", argv[1]);
        return -1;
    }

#ifdef _OPENMP
    int num_thread = omp_get_num_procs();
    omp_set_num_threads(num_thread);
    printf("There are %d threads, %d processors.\n", num_thread, omp_get_num_procs());
#else
    int num_thread = 1;
    printf("There is %d thread.\n", num_thread);
#endif

    int * pResults = NULL;
    unsigned char * pBuffers[1024];//large enough

                                   //pBuffer is used in the detection functions.
                                   //If you call functions in multiple threads, please create one buffer for each thread!
    unsigned char * p = (unsigned char *)malloc(DETECT_BUFFER_SIZE * num_thread);
    if (!p)
    {
        fprintf(stderr, "Can not alloc buffer.\n");
        return -1;
    }

    for (int i = 0; i < num_thread; i++)
        pBuffers[i] = p + (DETECT_BUFFER_SIZE)*i;

    int total_count = 256;

    pResults = facedetect_cnn(pBuffers[0], image.ptr<unsigned char>(0), (int)image.cols, (int)image.rows, (int)image.step);

    TickMeter tm;
    tm.start();
#ifdef _OPENMP
#pragma omp parallel for
#endif
    for (int i = 0; i < total_count; i++)
    {
#ifdef _OPENMP
        int idx = omp_get_thread_num();
#else
        int idx = 0;
#endif
        pResults = facedetect_cnn(pBuffers[idx], image.ptr<unsigned char>(0), (int)image.cols, (int)image.rows, (int)image.step);
    }
    tm.stop();
    double t = tm.getTimeMilli();
    t /= total_count;
    printf("cnn facedetection average time = %.2fms | %.2f FPS\n", t, 1000.0 / t);

    //release the buffer
    free(p);

    return 0;
}

