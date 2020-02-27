/*
By downloading, copying, installing or using the software you agree to this license.
If you do not agree to this license, do not download, install,
copy or use the software.


                  License Agreement For libfacedetection
                     (3-clause BSD License)

Copyright (c) 2018-2020, Shiqi Yu, all rights reserved.
shiqi.yu@gmail.com

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

  * Redistributions of source code must retain the above copyright notice,
    this list of conditions and the following disclaimer.

  * Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

  * Neither the names of the copyright holders nor the names of the contributors
    may be used to endorse or promote products derived from this software
    without specific prior written permission.

This software is provided by the copyright holders and contributors "as is" and
any express or implied warranties, including, but not limited to, the implied
warranties of merchantability and fitness for a particular purpose are disclaimed.
In no event shall copyright holders or contributors be liable for any direct,
indirect, incidental, special, exemplary, or consequential damages
(including, but not limited to, procurement of substitute goods or services;
loss of use, data, or profits; or business interruption) however caused
and on any theory of liability, whether in contract, strict liability,
or tort (including negligence or otherwise) arising in any way out of
the use of this software, even if advised of the possibility of such damage.
*/

#pragma once

//#define _ENABLE_AVX512 //Please enable it if X64 CPU
//#define _ENABLE_AVX2 //Please enable it if X64 CPU
//#define _ENABLE_NEON //Please enable it if ARM CPU


int * facedetect_cnn(unsigned char * result_buffer, //buffer memory for storing face detection results, !!its size must be 0x20000 Bytes!!
                    unsigned char * rgb_image_data, int width, int height, int step); //input image, it must be BGR (three channels) insteed of RGB image!

/*
DO NOT EDIT the following code if you don't really understand it.
*/
#if defined(_ENABLE_AVX512) || defined(_ENABLE_AVX2)
#include <immintrin.h>
#endif


#if defined(_ENABLE_NEON)
#include "arm_neon.h"
//NEON does not support UINT8*INT8 dot product
//to conver the input data to range [0, 127],
//and then use INT8*INT8 dot product
#define _MAX_UINT8_VALUE 127
#else
#define _MAX_UINT8_VALUE 255
#endif

#if defined(_ENABLE_AVX512) 
#define _MALLOC_ALIGN 512
#elif defined(_ENABLE_AVX2) 
#define _MALLOC_ALIGN 256
#else
#define _MALLOC_ALIGN 128
#endif

#if defined(_ENABLE_AVX512)&& defined(_ENABLE_NEON)
#error Cannot enable the two of AVX512 and NEON at the same time.
#endif
#if defined(_ENABLE_AVX2)&& defined(_ENABLE_NEON)
#error Cannot enable the two of AVX and NEON at the same time.
#endif
#if defined(_ENABLE_AVX512)&& defined(_ENABLE_AVX2)
#error Cannot enable the two of AVX512 and AVX2 at the same time.
#endif


#if defined(_OPENMP)
#include <omp.h>
#endif

#include <string.h>
#include <vector>
#include <iostream>
#include <typeinfo>

using namespace std;

void* myAlloc(size_t size);
void myFree_(void* ptr);
#define myFree(ptr) (myFree_(*(ptr)), *(ptr)=0);

#ifndef MIN
#  define MIN(a,b)  ((a) > (b) ? (b) : (a))
#endif

#ifndef MAX
#  define MAX(a,b)  ((a) < (b) ? (b) : (a))
#endif

typedef struct FaceRect_
{
    float score;
    int x;
    int y;
    int w;
    int h;
    int lm[10];
}FaceRect;

typedef struct ConvInfoStruct_ {
    int pad;
    int stride;
    int kernel_size;
    int channels;
    int num;
    float scale;
    signed char* pWeights;
    signed int* pBias;
}ConvInfoStruct;


template <class T>
class CDataBlob
{
public:
    T * data;
	int width;
	int height;
	int channels;
    int channelStep;
    float scale;
    //when the datablob is a filter, the bias is 0 by default
    //if it is the filted data, the bias is 1 by default
    int bias; 
public:
	CDataBlob() {
        data = 0;
		width = 0;
		height = 0;
        channels = 0;
        channelStep = 0;
        scale = 1.0f;
        bias = 0;
	}
	CDataBlob(int w, int h, int c)
	{
        data = 0;
        create(w, h, c);
	}
	~CDataBlob()
	{
        setNULL();
	}

    void setNULL()
    {
        if (data)
            myFree(&data);
        width = height = channels = channelStep = 0;
        scale = 1.0f;
    }
	bool create(int w, int h, int c)
	{
        setNULL();

		width = w;
		height = h;
        channels = c;
        bias = 0;

        //alloc space for int8 array
        int remBytes = (sizeof(T)* channels) % (_MALLOC_ALIGN / 8);
        if (remBytes == 0)
            this->channelStep = channels * sizeof(T);
        else
            this->channelStep = (channels * sizeof(T)) + (_MALLOC_ALIGN / 8) - remBytes;
        data = (T*)myAlloc(size_t(width) * height * this->channelStep);

        if (data == NULL)
        {
            cerr << "Failed to alloc memeory for uint8 data blob: "
                << width << "*"
                << height << "*"
                << channels << endl;
            return false;
        }
        
        //memset(data, 0, width * height * channelStep);
        
        //the following code is faster than memset
        //but not only the padding bytes are set to zero.
        //BE CAREFUL!!!
//#if defined(_OPENMP)
//#pragma omp parallel for
//#endif
        for (int r = 0; r < this->height; r++)
        {
            for (int c = 0; c < this->width; c++)
            {
                int pixel_end = this->channelStep / sizeof(T);
                T * pI = (this->data + (size_t(r) * this->width + c) * this->channelStep /sizeof(T));
                for (int ch = this->channels; ch < pixel_end; ch++)
                    pI[ch] = 0;
            }
        }
        
        return true;
	}

    bool setInt8FilterData(signed char * pData, int bias, int dataWidth, int dataHeight, int dataChannels)
    {
        if (pData == NULL)
        {
            cerr << "The input image data is null." << endl;
            return false;
        }

        if (typeid(signed char) != typeid(T))
        {
            cerr << "Data must be signed char, the same with the source data." << endl;
            return false;
        }

        if (dataWidth != this->width ||
            dataHeight != this->height ||
            dataChannels != this->channels)
        {
            cerr << "The dimension of the data can not match that of the Blob." << endl;
            return false;
        }

        for(int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
            {
                T * p = (this->data + (size_t(width) * row + col) * channelStep /sizeof(T));
                for (int ch = 0; ch < channels; ch++)
                {
                    p[ch] = pData[ch * height * width + row * width + col];
                }
            }
        
        this->bias = bias;
        return true;
    }

    bool setDataFrom3x3S2P1to1x1S1P0FromImage(const unsigned char * imgData, int imgWidth, int imgHeight, int imgChannels, int imgWidthStep)
    {
        if (imgData == NULL)
        {
            cerr << "The input image data is null." << endl;
            return false;
        }
        if (typeid(unsigned char) != typeid(T))
        {
            cerr << "Data must be unsigned char, the same with the source data." << endl;
            return false;
        }
        if (imgChannels != 3)
        {
            cerr << "The input image must be a 3-channel RGB image." << endl;
            return false;
        }

        create((imgWidth+1)/2, (imgHeight+1)/2, 27);
        //since the pixel assignment cannot fill all the elements in the blob. 
        //some elements in the blob should be initialized to 0
        memset(data, 0, size_t(width) * height * channelStep);

#if defined(_OPENMP)
#pragma omp parallel for
#endif
        for (int r = 0; r < this->height; r++)
        {
            for (int c = 0; c < this->width; c++)
            {
                T * pData = (unsigned char*)this->data + (size_t(r) * this->width + c) * this->channelStep;
                for (int fy = -1; fy <= 1; fy++)
                {
                    int srcy = r * 2 + fy;
                    
                    if (srcy < 0 || srcy >= imgHeight) //out of the range of the image
                        continue;

                    for (int fx = -1; fx <= 1; fx++)
                    {
                        int srcx = c * 2 + fx;

                        if (srcx < 0 || srcx >= imgWidth) //out of the range of the image
                            continue;

                        const unsigned char * pImgData = imgData + size_t(imgWidthStep) * srcy + imgChannels * srcx;

                        int output_channel_offset = ((fy + 1) * 3 + fx + 1) * 3; //3x3 filters, 3-channel image
#if defined(_ENABLE_NEON)
                        pData[output_channel_offset] = (pImgData[0] / 2);
                        pData[output_channel_offset + 1] = (pImgData[1] / 2);
                        pData[output_channel_offset + 2] = (pImgData[2] / 2);
#else
                        pData[output_channel_offset] = (pImgData[0]);
                        pData[output_channel_offset+1] = (pImgData[1]);
                        pData[output_channel_offset+2] = (pImgData[2]);
#endif

                    }

                }
            }
        }
#if defined(_ENABLE_NEON)
        this->bias = 1; // 1/2 = 0
        this->scale = 0.5f;
#else
        this->bias = 1; 
        this->scale = 1.0f;
#endif
        return true;
    }
    T getElement(int x, int y, int channel)
    {
        if (this->data)
        {
            if (x >= 0 && x < this->width &&
                y >= 0 && y < this->height &&
                channel >= 0 && channel < this->channels)
            {
                T * p = this->data + (size_t(y) * this->width + x) * this->channelStep/sizeof(T);
                return (p[channel]);
            }
        }
        
        return (T)(0);
    }

    friend ostream &operator<<(ostream &output, const CDataBlob &dataBlob)
    {
        output << "DataBlob Size (Width, Height, Channel, scale) = ("
            << dataBlob.width
            << ", " << dataBlob.height
            << ", " << dataBlob.channels
            << ", " << dataBlob.scale
            << ", " << dataBlob.bias
            << ")" << endl;
        for (int ch = 0; ch < dataBlob.channels; ch++)
        {
            output << "Channel " << ch << ": " << endl;

            for (int row = 0; row < dataBlob.height; row++)
            {
                output << "(";
                for (int col = 0; col < dataBlob.width; col++)
                {
                    T * p = (dataBlob.data + (dataBlob.width * row + col) * dataBlob.channelStep /sizeof(T) );

                    if(sizeof(T)<4)
                        output << (int)(p[ch]);
                    else
                        output << p[ch];

                    if (col != dataBlob.width - 1)
                        output << ", ";
                }
                output << ")" << endl;
            }
        }

        return output;
    }
};

class Filters {
public:
	vector<CDataBlob<signed char> *> filters;
	int pad;
	int stride;
    float scale; //element * scale = original value
    Filters()
    {
        pad = 0;
        stride = 0;
        scale = 0;
    }
};

bool convertInt2Float(CDataBlob<int> * inputData, CDataBlob<float> * outputData);

bool convolution(CDataBlob<unsigned char> *inputData, const Filters* filters, CDataBlob<int> *outputData);

bool convolution_relu(CDataBlob<unsigned char> *inputData, const Filters* filters, CDataBlob<unsigned char> *outputData);

bool maxpooling2x2S2(const CDataBlob<unsigned char> *inputData, CDataBlob<unsigned char> *outputData);

bool priorbox(const CDataBlob<unsigned char> * featureData, int img_width, int img_height, int step, int num_sizes, float * pWinSizes, CDataBlob<float> * outputData);

template<typename T>
bool concat4(const CDataBlob<T> *inputData1, const CDataBlob<T> *inputData2, const CDataBlob<T> *inputData3, const CDataBlob<T> *inputData4, CDataBlob<T> *outputData);

/* the input data for softmax must be a vector, the data stored in a multi-channel blob with size 1x1 */
template<typename T>
bool blob2vector(const CDataBlob<T> * inputData, CDataBlob<T> * outputData);

bool softmax1vector2class(CDataBlob<float> *inputOutputData);

bool detection_output(const CDataBlob<float> * priorbox, const CDataBlob<float> * loc, const CDataBlob<float> * conf, float overlap_threshold, float confidence_threshold, int top_k, int keep_top_k, CDataBlob<float> * outputData);

vector<FaceRect> objectdetect_cnn(unsigned char * rgbImageData, int with, int height, int step);
