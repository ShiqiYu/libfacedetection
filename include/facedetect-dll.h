/*
The MIT License (MIT)

Copyright (c) 2015-2017 Shiqi Yu
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

#ifndef FACEDETECT_DLL_H
#define FACEDETECT_DLL_H

#ifdef FACEDETECTDLL_EXPORTS
#define FACEDETECTDLL_API __declspec(dllexport) 
#else
#ifdef __linux__
#define FACEDETECTDLL_API  
#else
#define FACEDETECTDLL_API __declspec(dllimport) 
#endif
#endif

FACEDETECTDLL_API int * facedetect_frontal(unsigned char * result_buffer, //buffer memory for storing face detection results, !!its size must be 0x20000 Bytes!!
                               unsigned char * gray_image_data, int width, int height, int step, //input image, it must be gray (single-channel) image!
                               float scale, //scale factor for scan windows
                               int min_neighbors, //how many neighbors each candidate rectangle should have to retain it
                               int min_object_width, //Minimum possible face size. Faces smaller than that are ignored.
                               int max_object_width = 0, //Maximum possible face size. Faces larger than that are ignored. It is the largest posible when max_object_width=0.
                               int doLandmark=0); // landmark detection

FACEDETECTDLL_API int * facedetect_frontal_surveillance(unsigned char * result_buffer, //buffer memory for storing face detection results, !!its size must be 0x20000 Bytes!!
                               unsigned char * gray_image_data, int width, int height, int step, //input image, it must be gray (single-channel) image!
                               float scale, //scale factor for scan windows
                               int min_neighbors, //how many neighbors each candidate rectangle should have to retain it
                               int min_object_width, //Minimum possible face size. Faces smaller than that are ignored.
                               int max_object_width = 0, //Maximum possible face size. Faces larger than that are ignored. It is the largest posible when max_object_width=0.
                               int doLandmark = 0); // landmark detection

FACEDETECTDLL_API int * facedetect_multiview(unsigned char * result_buffer, //buffer memory for storing face detection results, !!its size must be 0x20000 Bytes!!
                               unsigned char * gray_image_data, int width, int height, int step, //input image, it must be gray (single-channel) image!
                               float scale, //scale factor for scan windows
                               int min_neighbors, //how many neighbors each candidate rectangle should have to retain it
                               int min_object_width, //Minimum possible face size. Faces smaller than that are ignored.
                               int max_object_width = 0, //Maximum possible face size. Faces larger than that are ignored. It is the largest posible when max_object_width=0.
                               int doLandmark = 0); // landmark detection

FACEDETECTDLL_API int * facedetect_multiview_reinforce(unsigned char * result_buffer, //buffer memory for storing face detection results, !!its size must be 0x20000 Bytes!!
                               unsigned char * gray_image_data, int width, int height, int step, //input image, it must be gray (single-channel) image!
                               float scale, //scale factor for scan windows
                               int min_neighbors, //how many neighbors each candidate rectangle should have to retain it
                               int min_object_width, //Minimum possible face size. Faces smaller than that are ignored.
                               int max_object_width = 0, //Maximum possible face size. Faces larger than that are ignored. It is the largest posible when max_object_width=0.
                               int doLandmark = 0); // landmark detection


#endif
