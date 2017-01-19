/*
The MIT License (MIT)

Copyright (c) 2016 Shiqi Yu
shiqi.yu@gmail.com

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

#include <stdio.h>
#include <opencv2/opencv.hpp>
#include <omp.h>

#include "facedetect-dll.h"

//#pragma comment(lib,"libfacedetect.lib")
#pragma comment(lib,"libfacedetect-x64.lib")

//define the buffer size. Do not change the size!
#define DETECT_BUFFER_SIZE 0x20000
using namespace cv;

int main(int argc, char* argv[])
{
    if(argc != 2)
    {
        printf("Usage: %s <image_file_name>\n", argv[0]);
        return -1;
    }
	//load an image and convert it to gray (single-channel)
	Mat gray = imread(argv[1], CV_LOAD_IMAGE_GRAYSCALE); 
	if(gray.empty())
	{
		fprintf(stderr, "Can not load the image file %s.\n", argv[1]);
		return -1;
	}

#ifdef _OPENMP
    int num_thread = omp_get_num_procs();
    omp_set_num_threads(num_thread);
#else
    int num_thread = 1;
#endif
    printf("There are %d threads, %d processors.\n", num_thread, omp_get_num_procs());

    int * pResults = NULL; 
    unsigned char * pBuffers[1024];//large enough

    //pBuffer is used in the detection functions.
    //If you call functions in multiple threads, please create one buffer for each thread!
    unsigned char * p = (unsigned char *)malloc(DETECT_BUFFER_SIZE * num_thread);
    if(!p)
    {
        fprintf(stderr, "Can not alloc buffer.\n");
        return -1;
    }

	for(int i = 0; i < num_thread; i++)
        pBuffers[i] = p+(DETECT_BUFFER_SIZE)*i;

    int total_count = 4096;
    double t;

    t = (double)cvGetTickCount();
#ifdef _OPENMP
    #pragma omp parallel for
#endif
    for(int i = 0; i < total_count; i++)
    {
        int idx = omp_get_thread_num();
	    pResults = facedetect_frontal(pBuffers[idx], (unsigned char*)(gray.ptr(0)), gray.cols, gray.rows, (int)gray.step,
											   1.2f, 2,  48);
    }
	t = (double)cvGetTickCount() - t;
    t =  t/((double)cvGetTickFrequency()*1000.)/total_count;
    printf( "frontal average time = %g ms; FPS = %.1f\n", t, 1000.0/t);

    t = (double)cvGetTickCount();
#ifdef _OPENMP
    #pragma omp parallel for
#endif
    for(int i = 0; i < total_count; i++)
    {
        int idx = omp_get_thread_num();
	    pResults = facedetect_frontal_surveillance(pBuffers[idx], (unsigned char*)(gray.ptr(0)), gray.cols, gray.rows, (int)gray.step,
											   1.2f, 2,  48);
    }
	t = (double)cvGetTickCount() - t;
    t =  t/((double)cvGetTickFrequency()*1000.)/total_count;
    printf( "frontal_surveillance average time = %g ms; FPS = %.1f\n", t, 1000.0/t);

    t = (double)cvGetTickCount();
#ifdef _OPENMP
    #pragma omp parallel for
#endif
    for(int i = 0; i < total_count; i++)
    {
        int idx = omp_get_thread_num();
	    pResults = facedetect_multiview(pBuffers[idx], (unsigned char*)(gray.ptr(0)), gray.cols, gray.rows, (int)gray.step,
											   1.2f, 4,  48);
    }
	t = (double)cvGetTickCount() - t;
    t =  t/((double)cvGetTickFrequency()*1000.)/total_count;
    printf( "multiview average time = %g ms; FPS = %.1f\n", t, 1000.0/t);

    t = (double)cvGetTickCount();
#ifdef _OPENMP
    #pragma omp parallel for
#endif
    for(int i = 0; i < total_count; i++)
    {
        int idx = omp_get_thread_num();
	    pResults = facedetect_multiview_reinforce(pBuffers[idx], (unsigned char*)(gray.ptr(0)), gray.cols, gray.rows, (int)gray.step,
											   1.2f, 5,  48);
    }
	t = (double)cvGetTickCount() - t;
    t =  t/((double)cvGetTickFrequency()*1000.)/total_count;
    printf( "multiview_reinforce average time = %g ms; FPS = %.1f\n", t, 1000.0/t);

    //release the buffer
    free(p);

	return 0;
}

