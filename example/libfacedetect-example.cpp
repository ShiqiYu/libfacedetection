/*
The MIT License (MIT)

Copyright (c) 2015-2016 Shiqi Yu
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
#include <vector>

#include "facedetect-dll.h"
//#pragma comment(lib,"libfacedetect.lib")
#pragma comment(lib,"libfacedetect-x64.lib")

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

    std::vector<FaceInfo> faces;
	int nResult = 0; 
	
	///////////////////////////////////////////
	// frontal face detection 
	// it's fast, but cannot detect side view faces
	//////////////////////////////////////////
	//!!! The input image must be a gray one (single-channel)
	//!!! DO NOT RELEASE pResults !!!
	nResult = facedetect_frontal(faces, (unsigned char*)(gray.ptr(0)), gray.cols, gray.rows, (int)gray.step,
											   1.2f, 2,  48);
	printf("%d frontal faces detected.\n", nResult);
	//print the detection results
	for(int i = 0; i < (int)faces.size(); i++)
	{
        FaceInfo fi = faces[i];
        printf("face_rect=[%d, %d, %d, %d], neighbors=%d\n", fi.x, fi.y, fi.width, fi.height, fi.neighbors);
	}


	///////////////////////////////////////////
	// multiview face detection 
	// it can detect side view faces, but slower than facedetect_frontal().
	//////////////////////////////////////////
	//!!! The input image must be a gray one (single-channel)
	//!!! DO NOT RELEASE pResults !!!
	nResult = facedetect_multiview(faces, (unsigned char*)(gray.ptr(0)), gray.cols, gray.rows, (int)gray.step,
											   1.2f, 5, 48);
    printf("%d frontal faces detected.\n", nResult);
	//print the detection results
	for(int i = 0; i < (int)faces.size(); i++)
	{
        FaceInfo fi = faces[i];
        printf("face_rect=[%d, %d, %d, %d], neighbors=%d, angle=%d\n", fi.x, fi.y, fi.width, fi.height, fi.neighbors, fi.angle);
	}


	///////////////////////////////////////////
	// reinforced multiview face detection 
	// it can detect side view faces, better but slower than facedetect_multiview().
	//////////////////////////////////////////
	//!!! The input image must be a gray one (single-channel)
	//!!! DO NOT RELEASE pResults !!!
	nResult = facedetect_multiview_reinforce(faces, (unsigned char*)(gray.ptr(0)), gray.cols, gray.rows, (int)gray.step,
											   1.2f, 5, 48);
    printf("%d frontal faces detected.\n", nResult);
	//print the detection results
	for(int i = 0; i < (int)faces.size(); i++)
	{
        FaceInfo fi = faces[i];
        printf("face_rect=[%d, %d, %d, %d], neighbors=%d, angle=%d\n", fi.x, fi.y, fi.width, fi.height, fi.neighbors, fi.angle);
	}


	///////////////////////////////////////////
	// frontal face detection designed for video surveillance
	// it can detect faces with bad illumination.
	//////////////////////////////////////////
	//!!! The input image must be a gray one (single-channel)
	//!!! DO NOT RELEASE pResults !!!
	nResult = facedetect_frontal_surveillance(faces, (unsigned char*)(gray.ptr(0)), gray.cols, gray.rows, (int)gray.step,
											   1.2f, 2, 48);
    printf("%d frontal faces detected.\n", nResult);
	//print the detection results
	for(int i = 0; i < (int)faces.size(); i++)
	{
        FaceInfo fi = faces[i];
        printf("face_rect=[%d, %d, %d, %d], neighbors=%d, angle=%d\n", fi.x, fi.y, fi.width, fi.height, fi.neighbors, fi.angle);
	}

    return 0;
}

