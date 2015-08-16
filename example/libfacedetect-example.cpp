/*
The MIT License (MIT)

Copyright (c) 2015 Shiqi Yu
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

#include <opencv.hpp>

#include "facedetect-dll.h"
#pragma comment(lib,"libfacedetect.lib")

using namespace cv;

int main(int argc, char* argv[])
{
	//load an image and convert it to gray (single-channel)
	Mat gray = imread("lena.png", CV_LOAD_IMAGE_GRAYSCALE); 
	if(gray.empty())
	{
		fprintf(stderr, "Can not load the image file.\n");
		return -1;
	}

	int * pResults = NULL; 
	
	///////////////////////////////////////////
	// frontal face detection 
	// it's fast, but cannot detect side view faces
	//////////////////////////////////////////
	//!!! The input image must be a gray one (single-channel)
	//!!! DO NOT RELEASE pResults !!!
	pResults = facedetect_frontal((unsigned char*)(gray.ptr(0)), gray.cols, gray.rows, gray.step,
											   1.2f, 2,  24);
	printf("%d frontal faces detected.\n", (pResults ? *pResults : 0));
	//print the detection results
	for(int i = 0; i < (pResults ? *pResults : 0); i++)
	{
        short * p = ((short*)(pResults+1))+6*i;
		int x = p[0];
		int y = p[1];
		int w = p[2];
		int h = p[3];
		int neighbors = p[4];

		printf("face_rect=[%d, %d, %d, %d], neighbors=%d\n", x,y,w,h,neighbors);
	}

	///////////////////////////////////////////
	// multiview face detection 
	// it can detection side view faces, but slower than the frontal face detection.
	//////////////////////////////////////////
	//!!! The input image must be a gray one (single-channel)
	//!!! DO NOT RELEASE pResults !!!
	pResults = facedetect_multiview((unsigned char*)(gray.ptr(0)), gray.cols, gray.rows, gray.step,
											   1.2f, 2, 24);
	printf("%d faces detected.\n", (pResults ? *pResults : 0));

	//print the detection results
	for(int i = 0; i < (pResults ? *pResults : 0); i++)
	{
        short * p = ((short*)(pResults+1))+6*i;
		int x = p[0];
		int y = p[1];
		int w = p[2];
		int h = p[3];
		int neighbors = p[4];
		int angle = p[5];

		printf("face_rect=[%d, %d, %d, %d], neighbors=%d, angle=%d\n", x,y,w,h,neighbors, angle);
	}


	return 0;
}

