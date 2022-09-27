/*
By downloading, copying, installing or using the software you agree to this license.
If you do not agree to this license, do not download, install,
copy or use the software.


                  License Agreement For libfacedetection
                     (3-clause BSD License)

Copyright (c) 2018-2021, Shiqi Yu, all rights reserved.
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

#include "facedetection_export.h"

//#define _ENABLE_AVX512 //Please enable it if X64 CPU
//#define _ENABLE_AVX2 //Please enable it if X64 CPU
//#define _ENABLE_NEON //Please enable it if ARM CPU


FACEDETECTION_EXPORT int * facedetect_cnn(unsigned char * result_buffer, //buffer memory for storing face detection results, !!its size must be 0x20000 Bytes!!
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
    int channels;
    int num_filters;
    bool is_depthwise;
    bool is_pointwise;
    bool with_relu;
    float* pWeights;
    float* pBiases;
}ConvInfoStruct;



template <typename T>
class CDataBlob
{
public:
	int rows;
	int cols;
	int channels; //in element
    int channelStep; //in byte
    T * data;

public:
	CDataBlob() {
		rows = 0;
		cols = 0;
        channels = 0;
        channelStep = 0;
        data = nullptr;
	}
	CDataBlob(int r, int c, int ch)
	{
        data = nullptr;
        create(r, c, ch);
        //#warning "confirm later"
        setZero();
	}
	~CDataBlob()
	{
        setNULL();
	}

    CDataBlob(CDataBlob<T> &&other) {
        data = other.data;
        other.data = nullptr;
        rows = other.rows;
        cols = other.cols;
        channels = other.channels;
        channelStep = other.channelStep;
    }

    CDataBlob<T> &operator=(CDataBlob<T> &&other) {
        this->~CDataBlob();
        new (this) CDataBlob<T>(std::move(other));
        return *this;
    }

    void setNULL()
    {
        if (data)
            myFree(&data);
        rows = cols = channels = channelStep = 0;
        data = nullptr;
    }

    void setZero()
    {
        if(data)
            memset(data, 0, channelStep * rows * cols);
    }

    inline bool isEmpty() const
    {
        return (rows <= 0 || cols <= 0 || channels == 0 || data == nullptr);
    }

	bool create(int r, int c, int ch)
	{
        setNULL();

		rows = r;
		cols = c;
        channels = ch;

        //alloc space for int8 array
        int remBytes = (sizeof(T)* channels) % (_MALLOC_ALIGN / 8);
        if (remBytes == 0)
            this->channelStep = channels * sizeof(T);
        else
            this->channelStep = (channels * sizeof(T)) + (_MALLOC_ALIGN / 8) - remBytes;
        data = (T*)myAlloc(size_t(rows) * cols * this->channelStep);

        if (data == nullptr)
        {
            std::cerr << "Failed to alloc memeory for uint8 data blob: "
                << rows << "*"
                << cols << "*"
                << channels << std::endl;
            return false;
        }
        
        //memset(data, 0, width * height * channelStep);
        
        //the following code is faster than memset
        //but not only the padding bytes are set to zero.
        //BE CAREFUL!!!
//#if defined(_OPENMP)
//#pragma omp parallel for
//#endif
        // for (int r = 0; r < this->rows; r++)
        // {
        //     for (int c = 0; c < this->cols; c++)
        //     {
        //         int pixel_end = this->channelStep / sizeof(T);
        //         T * pI = this->ptr(r, c);
        //         for (int ch = this->channels; ch < pixel_end; ch++)
        //             pI[ch] = 0;
        //     }
        // }
        
        return true;
	}

    inline T * ptr(int r, int c)
    {
        if( r < 0 || r >= this->rows || c < 0 || c >= this->cols )
            return nullptr;

        return (this->data + (size_t(r) * this->cols + c) * this->channelStep /sizeof(T));
    }
    inline const T * ptr(int r, int c) const
    {
        if( r < 0 || r >= this->rows || c < 0 || c >= this->cols )
            return nullptr;

        return (this->data + (size_t(r) * this->cols + c) * this->channelStep /sizeof(T));
    }

    inline const T getElement(int r, int c, int ch) const
    {
        if (this->data)
        {
            if (r >= 0 && r < this->rows &&
                c >= 0 && c < this->cols &&
                ch >= 0 && ch < this->channels)
            {
                const T * p = this->ptr(r, c);
                return (p[ch]);
            }
        }
        
        return (T)(0);
    }

    friend std::ostream &operator<<(std::ostream &output, CDataBlob &dataBlob)
    {
        output << "DataBlob Size (channels, rows, cols) = ("
            << dataBlob.channels
            << ", " << dataBlob.rows
            << ", " << dataBlob.cols
            << ")" << std::endl;
        if( dataBlob.rows * dataBlob.cols * dataBlob.channels <= 16)
        { //print the elements only when the total number is less than 64
            for (int ch = 0; ch < dataBlob.channels; ch++)
            {
                output << "Channel " << ch << ": " << std::endl;

                for (int r = 0; r < dataBlob.rows; r++)
                {
                    output << "(";
                    for (int c = 0; c < dataBlob.cols; c++)
                    {
                        T * p = dataBlob.ptr(r, c);

                        if(sizeof(T)<4)
                            output << (int)(p[ch]);
                        else
                            output << p[ch];

                        if (c != dataBlob.cols - 1)
                            output << ", ";
                    }
                    output << ")" << std::endl;
                }
            }
        }
        else{
            output << "(" ;
            int idx = 0;
            bool outloop = false;
            for(int r = 0; r < dataBlob.rows && !outloop; ++r) {
                for(int c = 0; c < dataBlob.cols && !outloop; ++c) {
                    for(int ch = 0; ch < dataBlob.channels && !outloop; ++ch) {
                       output << dataBlob.getElement(r, c, ch) << ", ";
                       ++idx;
                       if(idx >= 16) {
                            outloop = true;
                       }
                    }
                }
            } 
            output << "..., " 
                    << dataBlob.getElement(dataBlob.rows-1, dataBlob.cols-1, dataBlob.channels-1) << ")"
                    << std::endl; 
            float max_it = -500.f;
            float min_it = 500.f;
            for(int r = 0; r < dataBlob.rows; ++r) {
                for(int c = 0; c < dataBlob.cols; ++c) {
                    for(int ch = 0; ch < dataBlob.channels; ++ch) {
                        max_it = std::max(max_it, dataBlob.getElement(r, c, ch));
                        min_it = std::min(min_it, dataBlob.getElement(r, c, ch));
                    }
                }
            }   
            output << "max_it: " << max_it << "    min_it: " << min_it << std::endl;        
        }
        return output;
    }
};

template <typename T>
class Filters{
  public:
    int channels;
    int num_filters;
    bool is_depthwise;
    bool is_pointwise;
    bool with_relu;
    CDataBlob<T> weights;
    CDataBlob<T> biases;

    Filters()
    {
        channels = 0;
        num_filters = 0;
        is_depthwise = false;
        is_pointwise = false;
        with_relu = true;
    }

    Filters & operator=(ConvInfoStruct & convinfo)
    {
        if (typeid(float) != typeid(T))
        {
            std::cerr << "The data type must be float in this version." << std::endl;
            return *this;
        }
        if (typeid(float*) != typeid(convinfo.pWeights) ||
            typeid(float*) != typeid(convinfo.pBiases))
        {
            std::cerr << "The data type of the filter parameters must be float in this version." << std::endl;
            return *this;
        }

        this->channels = convinfo.channels;
        this->num_filters =  convinfo.num_filters;
        this->is_depthwise = convinfo.is_depthwise;
        this->is_pointwise = convinfo.is_pointwise;
        this->with_relu = convinfo.with_relu;

        if(!this->is_depthwise && this->is_pointwise) //1x1 point wise
        {
            this->weights.create(1, num_filters, channels);
        }
        else if(this->is_depthwise && !this->is_pointwise) //3x3 depth wise
        {
            this->weights.create(1, 9, channels);
        }
        else 
        {
            std::cerr << "Unsupported filter type. Only 1x1 point-wise and 3x3 depth-wise are supported." << std::endl;
            return *this;
        }

        this->biases.create(1, 1, num_filters);

        //the format of convinfo.pWeights/biases must meet the format in this->weigths/biases
        for(int fidx = 0; fidx < this->weights.cols; fidx++)
            memcpy(this->weights.ptr(0,fidx), 
                    convinfo.pWeights + channels * fidx , 
                    channels * sizeof(T));
        memcpy(this->biases.ptr(0,0), convinfo.pBiases, sizeof(T) * this->num_filters);

        return *this;
    }

};

std::vector<FaceRect> objectdetect_cnn(const unsigned char* rgbImageData, int with, int height, int step);

CDataBlob<float> setDataFrom3x3S2P1to1x1S1P0FromImage(const unsigned char* inputData, int imgWidth, int imgHeight, int imgChannels, int imgWidthStep, int padDivisor=32);
CDataBlob<float> convolution(const CDataBlob<float>& inputData, const Filters<float>& filters, bool do_relu = true);
CDataBlob<float> convolutionDP(const CDataBlob<float>& inputData, 
                const Filters<float>& filtersP, const Filters<float>& filtersD, bool do_relu = true);
CDataBlob<float> convolution4layerUnit(const CDataBlob<float>& inputData, 
                const Filters<float>& filtersP1, const Filters<float>& filtersD1, 
                const Filters<float>& filtersP2, const Filters<float>& filtersD2, bool do_relu = true);
CDataBlob<float> maxpooling2x2S2(const CDataBlob<float>& inputData);

CDataBlob<float> elementAdd(const CDataBlob<float>& inputData1, const CDataBlob<float>& inputData2);
CDataBlob<float> upsampleX2(const CDataBlob<float>& inputData);

CDataBlob<float> meshgrid(int feature_width, int feature_height, int stride, float offset=0.0f);

// TODO implement in SIMD
void bbox_decode(CDataBlob<float>& bbox_pred, const CDataBlob<float>& priors, int stride);
void kps_decode(CDataBlob<float>& bbox_pred, const CDataBlob<float>& priors, int stride);

template<typename T>
CDataBlob<T> blob2vector(const CDataBlob<T> &inputData);

template<typename T>
CDataBlob<T> concat3(const CDataBlob<T>& inputData1, const CDataBlob<T>& inputData2, const CDataBlob<T>& inputData3);

// TODO implement in SIMD
void sigmoid(CDataBlob<float>& inputData);

std::vector<FaceRect> detection_output(const CDataBlob<float>& cls,
                const CDataBlob<float>& reg,
                const CDataBlob<float>& kps,
                const CDataBlob<float>& obj,
                float overlap_threshold, float confidence_threshold, int top_k, int keep_top_k);