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

#include "facedetectcnn.h"
#include <cmath>
#include <float.h> //for FLT_EPSION
#include <algorithm>//for stable_sort, sort

typedef struct NormalizedBBox_
{
    float xmin;
    float ymin;
    float xmax;
    float ymax;
    float lm[10];
} NormalizedBBox;


void* myAlloc(size_t size)
{
    char *ptr, *ptr0;
	ptr0 = (char*)malloc(
		(size_t)(size + _MALLOC_ALIGN * ((size >= 4096) + 1L) + sizeof(char*)));

	if (!ptr0)
		return 0;

	// align the pointer
	ptr = (char*)(((size_t)(ptr0 + sizeof(char*) + 1) + _MALLOC_ALIGN - 1) & ~(size_t)(_MALLOC_ALIGN - 1));
	*(char**)(ptr - sizeof(char*)) = ptr0;
    
	return ptr;
}

void myFree_(void* ptr)
{
	// Pointer must be aligned by _MALLOC_ALIGN
	if (ptr)
	{
		if (((size_t)ptr & (_MALLOC_ALIGN - 1)) != 0)
			return;
		free(*((char**)ptr - 1));
	}
}


CDataBlob<float> setDataFrom3x3S2P1to1x1S1P0FromImage(const unsigned char* inputData, int imgWidth, int imgHeight, int imgChannels, int imgWidthStep, int padDivisor) {
    if (imgChannels != 3) {
        std::cerr << __FUNCTION__ << ": The input image must be a 3-channel RGB image." << std::endl;
        exit(1);
    }
    if (padDivisor != 32) {
        std::cerr << __FUNCTION__ << ": This version need pad of 32" << std::endl;
        exit(1);
    }
    int rows = ((imgHeight - 1) / padDivisor + 1) * padDivisor / 2;
    int cols = ((imgWidth - 1) / padDivisor + 1 ) * padDivisor / 2;
    int channels = 32;
    CDataBlob<float> outBlob(rows, cols, channels);

#if defined(_OPENMP)
#pragma omp parallel for
#endif
    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {
            float* pData = outBlob.ptr(r, c);
            for (int fy = -1; fy <= 1; fy++) {
                int srcy = r * 2 + fy;
                
                if (srcy < 0 || srcy >= imgHeight) //out of the range of the image
                    continue;

                for (int fx = -1; fx <= 1; fx++) {
                    int srcx = c * 2 + fx;

                    if (srcx < 0 || srcx >= imgWidth) //out of the range of the image
                        continue;

                    const unsigned char * pImgData = inputData + size_t(imgWidthStep) * srcy + imgChannels * srcx;

                    int output_channel_offset = ((fy + 1) * 3 + fx + 1) ; //3x3 filters, 3-channel image
                    pData[output_channel_offset * imgChannels] = pImgData[0];
                    pData[output_channel_offset * imgChannels + 1] = pImgData[1];
                    pData[output_channel_offset * imgChannels + 2] = pImgData[2];
                }
            }                    
        }
    } 
    return outBlob;
}

//p1 and p2 must be 512-bit aligned (16 float numbers)
inline float dotProduct(const float * p1, const float * p2, int num)
{
    float sum = 0.f;

#if defined(_ENABLE_AVX512)
    __m512 a_float_x16, b_float_x16;
    __m512 sum_float_x16 = _mm512_setzero_ps();
    for (int i = 0; i < num; i += 16)
    {
        a_float_x16 = _mm512_load_ps(p1 + i);
        b_float_x16 = _mm512_load_ps(p2 + i);
        sum_float_x16 = _mm512_add_ps(sum_float_x16, _mm512_mul_ps(a_float_x16, b_float_x16));
    }
   sum = _mm512_reduce_add_ps(sum_float_x16);
#elif defined(_ENABLE_AVX2)
    __m256 a_float_x8, b_float_x8;
    __m256 sum_float_x8 = _mm256_setzero_ps();
    for (int i = 0; i < num; i += 8)
    {
        a_float_x8 = _mm256_load_ps(p1 + i);
        b_float_x8 = _mm256_load_ps(p2 + i);
        sum_float_x8 = _mm256_add_ps(sum_float_x8, _mm256_mul_ps(a_float_x8, b_float_x8));
    }
   sum_float_x8 = _mm256_hadd_ps(sum_float_x8, sum_float_x8);
   sum_float_x8 = _mm256_hadd_ps(sum_float_x8, sum_float_x8);
   sum = ((float*)&sum_float_x8)[0] + ((float*)&sum_float_x8)[4];
#elif defined(_ENABLE_NEON)
    float32x4_t a_float_x4, b_float_x4;
    float32x4_t sum_float_x4;
    sum_float_x4 = vdupq_n_f32(0);
    for (int i = 0; i < num; i+=4)
    {
        a_float_x4 = vld1q_f32(p1 + i);
        b_float_x4 = vld1q_f32(p2 + i);
        sum_float_x4 = vaddq_f32(sum_float_x4, vmulq_f32(a_float_x4, b_float_x4));
    }
    sum += vgetq_lane_f32(sum_float_x4, 0);
    sum += vgetq_lane_f32(sum_float_x4, 1);
    sum += vgetq_lane_f32(sum_float_x4, 2);
    sum += vgetq_lane_f32(sum_float_x4, 3);
#else
    for(int i = 0; i < num; i++)
    {
        sum += (p1[i] * p2[i]);
    }
#endif

    return sum;
}

inline bool vecMulAdd(const float * p1, const float * p2, float * p3, int num)
{
#if defined(_ENABLE_AVX512)
    __m512 a_float_x16, b_float_x16, c_float_x16;
    for (int i = 0; i < num; i += 16)
    {
        a_float_x16 = _mm512_load_ps(p1 + i);
        b_float_x16 = _mm512_load_ps(p2 + i);
        c_float_x16 = _mm512_load_ps(p3 + i);
        c_float_x16 = _mm512_add_ps(c_float_x16, _mm512_mul_ps(a_float_x16, b_float_x16));
        _mm512_store_ps(p3 + i, c_float_x16);
    }
#elif defined(_ENABLE_AVX2)
    __m256 a_float_x8, b_float_x8, c_float_x8;
    for (int i = 0; i < num; i += 8)
    {
        a_float_x8 = _mm256_load_ps(p1 + i);
        b_float_x8 = _mm256_load_ps(p2 + i);
        c_float_x8 = _mm256_load_ps(p3 + i);
        c_float_x8 = _mm256_add_ps(c_float_x8, _mm256_mul_ps(a_float_x8, b_float_x8));
        _mm256_store_ps(p3 + i, c_float_x8);
    }
#elif defined(_ENABLE_NEON)
    float32x4_t a_float_x4, b_float_x4, c_float_x4;
    for (int i = 0; i < num; i+=4)
    {
        a_float_x4 = vld1q_f32(p1 + i);
        b_float_x4 = vld1q_f32(p2 + i);
        c_float_x4 = vld1q_f32(p3 + i);
        c_float_x4 = vaddq_f32(c_float_x4, vmulq_f32(a_float_x4, b_float_x4));
        vst1q_f32(p3 + i, c_float_x4);
    }
#else
    for(int i = 0; i < num; i++)
        p3[i] += (p1[i] * p2[i]);
#endif

    return true;
}

inline bool vecAdd(const float * p1, float * p2, int num)
{
#if defined(_ENABLE_AVX512)
    __m512 a_float_x16, b_float_x16;
    for (int i = 0; i < num; i += 16)
    {
        a_float_x16 = _mm512_load_ps(p1 + i);
        b_float_x16 = _mm512_load_ps(p2 + i);
        b_float_x16 = _mm512_add_ps(a_float_x16, b_float_x16);
        _mm512_store_ps(p2 + i, b_float_x16);
    }
#elif defined(_ENABLE_AVX2)
    __m256 a_float_x8, b_float_x8;
    for (int i = 0; i < num; i += 8)
    {
        a_float_x8 = _mm256_load_ps(p1 + i);
        b_float_x8 = _mm256_load_ps(p2 + i);
        b_float_x8 = _mm256_add_ps(a_float_x8, b_float_x8);
        _mm256_store_ps(p2 + i, b_float_x8);
    }
#elif defined(_ENABLE_NEON)
    float32x4_t a_float_x4, b_float_x4, c_float_x4;
    for (int i = 0; i < num; i+=4)
    {
        a_float_x4 = vld1q_f32(p1 + i);
        b_float_x4 = vld1q_f32(p2 + i);
        c_float_x4 = vaddq_f32(a_float_x4, b_float_x4);
        vst1q_f32(p2 + i, c_float_x4);
    }
#else
    for(int i = 0; i < num; i++)
    {
        p2[i] += p1[i];
    }
#endif
    return true;
}

inline bool vecAdd(const float * p1, const float * p2, float* p3, int num)
{
#if defined(_ENABLE_AVX512)
    __m512 a_float_x16, b_float_x16;
    for (int i = 0; i < num; i += 16)
    {
        a_float_x16 = _mm512_load_ps(p1 + i);
        b_float_x16 = _mm512_load_ps(p2 + i);
        b_float_x16 = _mm512_add_ps(a_float_x16, b_float_x16);
        _mm512_store_ps(p3 + i, b_float_x16);
    }
#elif defined(_ENABLE_AVX2)
    __m256 a_float_x8, b_float_x8;
    for (int i = 0; i < num; i += 8)
    {
        a_float_x8 = _mm256_load_ps(p1 + i);
        b_float_x8 = _mm256_load_ps(p2 + i);
        b_float_x8 = _mm256_add_ps(a_float_x8, b_float_x8);
        _mm256_store_ps(p3 + i, b_float_x8);
    }
#elif defined(_ENABLE_NEON)
    float32x4_t a_float_x4, b_float_x4, c_float_x4;
    for (int i = 0; i < num; i+=4)
    {
        a_float_x4 = vld1q_f32(p1 + i);
        b_float_x4 = vld1q_f32(p2 + i);
        c_float_x4 = vaddq_f32(a_float_x4, b_float_x4);
        vst1q_f32(p3 + i, c_float_x4);
    }
#else
    for(int i = 0; i < num; i++)
    {
        p3[i] = p1[i] + p2[i];
    }
#endif
    return true;
}

bool convolution_1x1pointwise(const CDataBlob<float> & inputData, const Filters<float> & filters, CDataBlob<float> & outputData)
{
// #if defined(_OPENMP)
// #pragma omp parallel for
// #endif
    for (int row = 0; row < outputData.rows; row++)
    {
        for (int col = 0; col < outputData.cols; col++)
        {
            float * pOut = outputData.ptr(row, col);
            const float * pIn = inputData.ptr(row, col);            
            for (int ch = 0; ch < outputData.channels; ch++)
            {
                const float * pF = filters.weights.ptr(0, ch);
                pOut[ch] = dotProduct(pIn, pF, inputData.channels);
                pOut[ch] += filters.biases.data[ch];
            }
        }
    }
    return true;
}

bool convolution_3x3depthwise(const CDataBlob<float> & inputData, const Filters<float> & filters, CDataBlob<float> & outputData)
{
    //set all elements in outputData to zeros
    outputData.setZero();
// #if defined(_OPENMP)
// #pragma omp parallel for
// #endif
    for (int row = 0; row < outputData.rows; row++) 
    {  
        int srcy_start = row - 1;
        int srcy_end = srcy_start + 3;
        srcy_start = MAX(0, srcy_start);
        srcy_end = MIN(srcy_end, inputData.rows);

        for (int col = 0; col < outputData.cols; col++)
        { 
            float * pOut = outputData.ptr(row, col);
            int srcx_start = col - 1;
            int srcx_end = srcx_start + 3;
            srcx_start = MAX(0, srcx_start);
            srcx_end = MIN(srcx_end, inputData.cols);


            for ( int r = srcy_start; r < srcy_end; r++)
                for( int c = srcx_start; c < srcx_end; c++)
                {
                    int filter_r = r - row + 1;
                    int filter_c = c - col + 1;
                    int filter_idx = filter_r * 3 + filter_c;
                    vecMulAdd(inputData.ptr(r, c), filters.weights.ptr(0, filter_idx), pOut, filters.num_filters);
                }
            vecAdd(filters.biases.ptr(0,0), pOut, filters.num_filters);
        }
    }
     return true;
}

bool relu(CDataBlob<float> & inputoutputData)
{
    if( inputoutputData.isEmpty() )
    {
        std::cerr << __FUNCTION__ << ": The input data is empty." << std::endl;
        return false;
    }
    
    int len = inputoutputData.cols * inputoutputData.rows * inputoutputData.channelStep / sizeof(float);


#if defined(_ENABLE_AVX512)
    __m512 a, bzeros;
    bzeros = _mm512_setzero_ps(); //zeros
    for( int i = 0; i < len; i+=16)
    {
        a = _mm512_load_ps(inputoutputData.data + i);
        a = _mm512_max_ps(a, bzeros);
        _mm512_store_ps(inputoutputData.data + i, a);
    }
#elif defined(_ENABLE_AVX2)
    __m256 a, bzeros;
    bzeros = _mm256_setzero_ps(); //zeros
    for( int i = 0; i < len; i+=8)
    {
        a = _mm256_load_ps(inputoutputData.data + i);
        a = _mm256_max_ps(a, bzeros);
        _mm256_store_ps(inputoutputData.data + i, a);
    }
#else    
    for( int i = 0; i < len; i++)
        inputoutputData.data[i] *= (inputoutputData.data[i] >0);
#endif

    return true;
}

void IntersectBBox(const NormalizedBBox& bbox1, const NormalizedBBox& bbox2,
                   NormalizedBBox* intersect_bbox) 
{
    if (bbox2.xmin > bbox1.xmax || bbox2.xmax < bbox1.xmin ||
        bbox2.ymin > bbox1.ymax || bbox2.ymax < bbox1.ymin) 
    {
        // Return [0, 0, 0, 0] if there is no intersection.
        intersect_bbox->xmin = 0;
        intersect_bbox->ymin = 0;
        intersect_bbox->xmax = 0;
        intersect_bbox->ymax = 0;
    }
    else
    {
        intersect_bbox->xmin = (std::max(bbox1.xmin, bbox2.xmin));
        intersect_bbox->ymin = (std::max(bbox1.ymin, bbox2.ymin));
        intersect_bbox->xmax = (std::min(bbox1.xmax, bbox2.xmax));
        intersect_bbox->ymax = (std::min(bbox1.ymax, bbox2.ymax));
    }
}

float JaccardOverlap(const NormalizedBBox& bbox1, const NormalizedBBox& bbox2)
{
    NormalizedBBox intersect_bbox;
    IntersectBBox(bbox1, bbox2, &intersect_bbox);
    float intersect_width, intersect_height;
    intersect_width = intersect_bbox.xmax - intersect_bbox.xmin;
    intersect_height = intersect_bbox.ymax - intersect_bbox.ymin;

    if (intersect_width > 0 && intersect_height > 0) 
    {
        float intersect_size = intersect_width * intersect_height;
        float bsize1 = (bbox1.xmax - bbox1.xmin)*(bbox1.ymax - bbox1.ymin);
        float bsize2 = (bbox2.xmax - bbox2.xmin)*(bbox2.ymax - bbox2.ymin);
        return intersect_size / ( bsize1 + bsize2 - intersect_size);
    }
    else 
    {
        return 0.f;
    }
}

bool SortScoreBBoxPairDescend(const std::pair<float, NormalizedBBox>& pair1,   const std::pair<float, NormalizedBBox>& pair2) 
{
    return pair1.first > pair2.first;
}


CDataBlob<float> upsampleX2(const CDataBlob<float>& inputData) {
    if (inputData.isEmpty()) {
        std::cerr << __FUNCTION__ << ": The input data is empty." << std::endl;
        exit(1);
    }

    CDataBlob<float> outData(inputData.rows * 2, inputData.cols * 2, inputData.channels);

    for (int r = 0; r < inputData.rows; r++) {
        for (int c = 0; c < inputData.cols; c++) {
            const float * pIn = inputData.ptr(r, c);
            int outr = r * 2;
            int outc = c * 2;
            for (int ch = 0; ch < inputData.channels; ++ch) {
                outData.ptr(outr, outc)[ch] = pIn[ch];
                outData.ptr(outr, outc + 1)[ch] = pIn[ch];
                outData.ptr(outr + 1, outc)[ch] = pIn[ch];
                outData.ptr(outr + 1, outc + 1)[ch] = pIn[ch];
            }
        }
    }
    return outData;
}

CDataBlob<float> elementAdd(const CDataBlob<float>& inputData1, const CDataBlob<float>& inputData2) {
    if (inputData1.rows != inputData2.rows || inputData1.cols != inputData2.cols || inputData1.channels != inputData2.channels) {
        std::cerr << __FUNCTION__ << ": The two input datas must be in the same shape." << std::endl;
        exit(1);
    }
    CDataBlob<float> outData(inputData1.rows, inputData1.cols, inputData1.channels);
    for (int r = 0; r < inputData1.rows; r++) {
        for (int c = 0; c < inputData1.cols; c++) {
            const float * pIn1 = inputData1.ptr(r, c);
            const float * pIn2 = inputData2.ptr(r, c);
            float* pOut = outData.ptr(r, c);
            vecAdd(pIn1, pIn2, pOut, inputData1.channels);
        }
    }
    return outData;
}

CDataBlob<float> convolution(const CDataBlob<float>& inputData, const Filters<float>& filters, bool do_relu)
{
    if( inputData.isEmpty() || filters.weights.isEmpty() || filters.biases.isEmpty())
    {
        std::cerr << __FUNCTION__ << ": The input data or filter data is empty" << std::endl;
        exit(1);
    }
    if( inputData.channels != filters.channels)
    {
        std::cerr << __FUNCTION__ << ": The input data dimension cannot meet filters: " << inputData.channels << " vs " << filters.channels << std::endl;
        exit(1);
    }
    CDataBlob<float> outputData(inputData.rows, inputData.cols, filters.num_filters);
    if(filters.is_pointwise && !filters.is_depthwise)
        convolution_1x1pointwise(inputData, filters, outputData);
    else if(!filters.is_pointwise && filters.is_depthwise)
        convolution_3x3depthwise(inputData, filters, outputData);
    else
    {
        std::cerr << __FUNCTION__ << ": Unsupported filter type." << std::endl;
        exit(1);
    }

    if(do_relu)
        relu(outputData);

    return outputData;
}

CDataBlob<float> convolutionDP(const CDataBlob<float>& inputData, 
                const Filters<float>& filtersP, const Filters<float>& filtersD, bool do_relu)
{
    CDataBlob<float> tmp = convolution(inputData, filtersP, false);
    CDataBlob<float> out = convolution(tmp, filtersD, do_relu);
    return out;
}

CDataBlob<float> convolution4layerUnit(const CDataBlob<float>& inputData, 
                const Filters<float>& filtersP1, const Filters<float>& filtersD1, 
                const Filters<float>& filtersP2, const Filters<float>& filtersD2, bool do_relu)
{
    CDataBlob<float> tmp = convolutionDP(inputData, filtersP1, filtersD1, true);
    CDataBlob<float> out = convolutionDP(tmp, filtersP2, filtersD2, do_relu);
    return out;
}


//only 2X2 S2 is supported
CDataBlob<float> maxpooling2x2S2(const CDataBlob<float>&inputData)
{
    if (inputData.isEmpty())
    {
        std::cerr << __FUNCTION__ << ": The input data is empty." << std::endl;
        exit(1);
    }
    int outputR = static_cast<int>(ceil((inputData.rows - 3.0f) / 2)) + 1;
    int outputC = static_cast<int>(ceil((inputData.cols - 3.0f) / 2)) + 1;
    int outputCH = inputData.channels;

    if (outputR < 1 || outputC < 1)
    {
        std::cerr << __FUNCTION__ << ": The size of the output is not correct. (" << outputR << ", " << outputC << ")." << std::endl;
        exit(1);        
    }

    CDataBlob<float> outputData(outputR, outputC, outputCH);
    outputData.setZero();

    for (int row = 0; row < outputData.rows; row++)
    {
        for (int col = 0; col < outputData.cols; col++)
        {
            size_t inputMatOffsetsInElement[4];
            int elementCount = 0;

            int rstart = row * 2;
            int cstart = col * 2;
            int rend = MIN(rstart + 2, inputData.rows);
            int cend = MIN(cstart + 2, inputData.cols);

            for (int fr = rstart; fr < rend; fr++)
            {
                for (int fc = cstart; fc < cend; fc++)
                {
                    inputMatOffsetsInElement[elementCount++] = (size_t(fr) * inputData.cols + fc) * inputData.channelStep / sizeof(float);
                }
            }

            float * pOut = outputData.ptr(row, col);
            float * pIn = inputData.data;

#if defined(_ENABLE_NEON)
            for (int ch = 0; ch < outputData.channels; ch += 4)
            {
                float32x4_t tmp;
                float32x4_t maxVal = vld1q_f32(pIn + ch + inputMatOffsetsInElement[0]);
                for (int ec = 1; ec < elementCount; ec++)
                {
                    tmp = vld1q_f32(pIn + ch + inputMatOffsetsInElement[ec]);
                    maxVal = vmaxq_f32(maxVal, tmp);
                }
                vst1q_f32(pOut + ch, maxVal);
            }
#elif defined(_ENABLE_AVX512)
            for (int ch = 0; ch < outputData.channels; ch += 16)
            {
                __m512 tmp;
                __m512 maxVal = _mm512_load_ps((__m512 const*)(pIn + ch + inputMatOffsetsInElement[0]));
                for (int ec = 1; ec < elementCount; ec++)
                {
                    tmp = _mm512_load_ps((__m512 const*)(pIn + ch + inputMatOffsetsInElement[ec]));
                    maxVal = _mm512_max_ps(maxVal, tmp);
                }
                _mm512_store_ps((__m512*)(pOut + ch), maxVal);
            }
#elif defined(_ENABLE_AVX2)
            for (int ch = 0; ch < outputData.channels; ch += 8)
            {
                __m256 tmp;
                __m256 maxVal = _mm256_load_ps((float const*)(pIn + ch + inputMatOffsetsInElement[0]));
                for (int ec = 1; ec < elementCount; ec++)
                {
                    tmp = _mm256_load_ps((float const*)(pIn + ch + inputMatOffsetsInElement[ec]));
                    maxVal = _mm256_max_ps(maxVal, tmp);
                }
                _mm256_store_ps(pOut + ch, maxVal);
            }
#else
            for (int ch = 0; ch < outputData.channels; ch++)
            {
                float maxVal = pIn[ch + inputMatOffsetsInElement[0]];
                for (int ec = 1; ec < elementCount; ec++)
                {
                    maxVal = MAX(maxVal, pIn[ch + inputMatOffsetsInElement[ec]]);
                }
                pOut[ch] = maxVal;
            }
#endif
        }
    }
    return outputData;
}

CDataBlob<float> meshgrid(int feature_width, int feature_height, int stride, float offset) {
    CDataBlob<float> out(feature_height, feature_width, 2);
    for(int r = 0; r < feature_height; ++r) {
        float rx = (float)(r * stride) + offset;
        for(int c = 0; c < feature_width; ++c) {
            float* p = out.ptr(r, c);
            p[0] = (float)(c * stride) + offset;
            p[1] = rx;
        }
    }
    return out;
}

void bbox_decode(CDataBlob<float>& bbox_pred, const CDataBlob<float>& priors, int stride) {
    if(bbox_pred.cols != priors.cols || bbox_pred.rows != priors.rows) {
        std::cerr << __FUNCTION__ << ": Mismatch between feature map and anchor size. (" \
        << std::to_string(bbox_pred.rows) << ", " << std::to_string(bbox_pred.cols) << ") vs (" \
        << std::to_string(priors.rows) << ", " << std::to_string(priors.cols) << ")." << std::endl;
    }
    if(bbox_pred.channels != 4) {
        std::cerr << __FUNCTION__ << ": The bbox dim must be 4."  << std::endl;
    }
    float fstride = (float)stride;
    for(int r = 0; r < bbox_pred.rows; ++r) {
        for(int c = 0; c < bbox_pred.cols; ++c) {
            float* pb = bbox_pred.ptr(r, c);
            const float* pp = priors.ptr(r, c);
            float cx = pb[0] * fstride + pp[0];
            float cy = pb[1] * fstride + pp[1];
            float w = std::exp(pb[2]) * fstride;
            float h = std::exp(pb[3]) * fstride;
            pb[0] = cx - w / 2.f;
            pb[1] = cy - h / 2.f;
            pb[2] = cx + w / 2.f;
            pb[3] = cy + h / 2.f;
        }
    }
}

void kps_decode(CDataBlob<float>& kps_pred, const CDataBlob<float>& priors, int stride) {
    if(kps_pred.cols != priors.cols || kps_pred.rows != priors.rows) {
        std::cerr << __FUNCTION__ << ": Mismatch between feature map and anchor size." << std::endl;
        exit(1);
    }
    if(kps_pred.channels & 1) {
        std::cerr << __FUNCTION__ << ": The kps dim must be even." << std::endl;
        exit(1);
    }
    float fstride = (float)stride;
    int num_points = kps_pred.channels >> 1;

    for(int r = 0; r < kps_pred.rows; ++r) {
        for(int c = 0; c < kps_pred.cols; ++c) {
            float* pb = kps_pred.ptr(r, c);
            const float* pp = priors.ptr(r, c);
            for(int n = 0; n < num_points; ++n) {
                pb[2 * n] = pb[2 * n] * fstride + pp[0];
                pb[2 * n + 1] = pb[2 * n + 1] * fstride + pp[1];           
            }
        }
    }
}

template<typename T>
CDataBlob<T> concat3(const CDataBlob<T>& inputData1, const CDataBlob<T>& inputData2, const CDataBlob<T>& inputData3)
{
    if ((inputData1.isEmpty()) || (inputData2.isEmpty()) || (inputData3.isEmpty()))
    {
        std::cerr << __FUNCTION__ << ": The input data is empty." << std::endl;
        exit(1);
    }

    if ((inputData1.cols != inputData2.cols) ||
        (inputData1.rows != inputData2.rows) ||
        (inputData1.cols != inputData3.cols) ||
        (inputData1.rows != inputData3.rows))
    {
        std::cerr << __FUNCTION__ << ": The three inputs must have the same size." << std::endl;
        exit(1);
    }
    int outputR = inputData1.rows;
    int outputC = inputData1.cols;
    int outputCH = inputData1.channels + inputData2.channels + inputData3.channels;

    if (outputR < 1 || outputC < 1 || outputCH < 1)
    {
        std::cerr << __FUNCTION__ << ": The size of the output is not correct. (" << outputR << ", " << outputC << ", " << outputCH << ")." << std::endl;
        exit(1);
    }

    CDataBlob<T> outputData(outputR, outputC, outputCH);

    for (int row = 0; row < outputData.rows; row++)
    {
        for (int col = 0; col < outputData.cols; col++)
        {
            T * pOut = outputData.ptr(row, col);
            const T * pIn1 = inputData1.ptr(row, col);
            const T * pIn2 = inputData2.ptr(row, col);
            const T * pIn3 = inputData3.ptr(row, col);

            memcpy(pOut, pIn1, sizeof(T)* inputData1.channels);
            memcpy(pOut + inputData1.channels, pIn2, sizeof(T)* inputData2.channels);
            memcpy(pOut + inputData1.channels + inputData2.channels, pIn3, sizeof(T)* inputData3.channels);
        }
    }
    return outputData;
}
template CDataBlob<float> concat3(const CDataBlob<float>& inputData1, const CDataBlob<float>& inputData2, const CDataBlob<float>& inputData3);

template<typename T>
CDataBlob<T> blob2vector(const CDataBlob<T> &inputData)
{
    if (inputData.isEmpty())
    {
        std::cerr << __FUNCTION__ << ": The input data is empty." << std::endl;
        exit(1);
    }

    CDataBlob<T> outputData(1, 1, inputData.cols * inputData.rows * inputData.channels);

    int bytesOfAChannel = inputData.channels * sizeof(T);
    T * pOut = outputData.ptr(0,0);
    for (int row = 0; row < inputData.rows; row++)
    {
        for (int col = 0; col < inputData.cols; col++)
        {
            const T * pIn = inputData.ptr(row, col);
            memcpy(pOut, pIn, bytesOfAChannel);
            pOut += inputData.channels;
        }
    }

    return outputData;
}
template CDataBlob<float> blob2vector(const CDataBlob<float>& inputData);

void sigmoid(CDataBlob<float>& inputData) {
    for(int r = 0; r < inputData.rows; ++r) {
        for(int c = 0; c < inputData.cols; ++c) {
            float* pIn = inputData.ptr(r, c);
            for(int ch = 0; ch < inputData.channels; ++ch) {
                float v = pIn[ch];
                v = std::min(v, 88.3762626647949f);
                v = std::max(v, -88.3762626647949f);
                pIn[ch] = static_cast<float>(1.f / (1.f + exp(-v)));
            }
        }
    }
}

std::vector<FaceRect> detection_output(const CDataBlob<float>& cls,
                      const CDataBlob<float>& reg,
                      const CDataBlob<float>& kps,
                      const CDataBlob<float>& obj,
                      float overlap_threshold,
                      float confidence_threshold,
                      int top_k,
                      int keep_top_k)
{
    if (reg.isEmpty() || cls.isEmpty() || kps.isEmpty() || obj.isEmpty())//|| iou.isEmpty())
    {
        std::cerr << __FUNCTION__ << ": The input data is null." << std::endl;
        exit(1);
    }
    if(reg.cols != 1 || reg.rows!= 1 || cls.cols != 1 || cls.rows!= 1 || kps.cols != 1 || kps.rows!= 1 || obj.cols != 1 || obj.rows!= 1) {
        std::cerr << __FUNCTION__ << ": Only support vector format." << std::endl;
        exit(1);
    }

    if((int)(kps.channels / obj.channels) != 10) {
        std::cerr << __FUNCTION__ << ": Only support 5 keypoints. (" << kps.channels << ")" << std::endl;
        exit(1);
    }

    const float* pCls = cls.ptr(0,0);
    const float* pReg = reg.ptr(0,0);
    const float* pObj = obj.ptr(0,0);
    const float* pKps = kps.ptr(0, 0);

    std::vector<std::pair<float, NormalizedBBox> > score_bbox_vec;
    std::vector<std::pair<float, NormalizedBBox> > final_score_bbox_vec;

    //get the candidates those are > confidence_threshold
    for(int i = 0; i < cls.channels; ++i)
    {
        float conf = std::sqrt(pCls[i] * pObj[i]);
        // float conf = pCls[i] * pObj[i];

        if(conf >= confidence_threshold)
        {
            NormalizedBBox bb;
            bb.xmin = pReg[4 * i];
            bb.ymin = pReg[4 * i + 1];
            bb.xmax = pReg[4 * i + 2];
            bb.ymax = pReg[4 * i + 3];

            //store the five landmarks
            memcpy(bb.lm, pKps + 10 * i, 10 * sizeof(float));
            score_bbox_vec.push_back(std::make_pair(conf, bb));
        }
    }

    //Sort the score pair according to the scores in descending order
    std::stable_sort(score_bbox_vec.begin(), score_bbox_vec.end(), SortScoreBBoxPairDescend);

    // Keep top_k scores if needed.
    if (top_k > -1 && size_t(top_k) < score_bbox_vec.size()) {
        score_bbox_vec.resize(top_k);
    }

    //Do NMS
    final_score_bbox_vec.clear();
    while (score_bbox_vec.size() != 0) {
        const NormalizedBBox bb1 = score_bbox_vec.front().second;
        bool keep = true;
        for (size_t k = 0; k < final_score_bbox_vec.size(); k++)
        {
            if (keep) 
            {
                const NormalizedBBox bb2 = final_score_bbox_vec[k].second;
                float overlap = JaccardOverlap(bb1, bb2);
                keep = (overlap <= overlap_threshold);
            }
            else 
            {
                break;
            }
        }
        if (keep) {
            final_score_bbox_vec.push_back(score_bbox_vec.front());
        }
        score_bbox_vec.erase(score_bbox_vec.begin());
    }
    if (keep_top_k > -1 && size_t(keep_top_k) < final_score_bbox_vec.size()) {
        final_score_bbox_vec.resize(keep_top_k);
    }

    //copy the results to the output blob
    int num_faces = (int)final_score_bbox_vec.size();

    std::vector<FaceRect> facesInfo;
    for (int fi = 0; fi < num_faces; fi++)
    {
        std::pair<float, NormalizedBBox> pp = final_score_bbox_vec[fi];

        FaceRect r;
        r.score = pp.first;
        r.x = pp.second.xmin;
        r.y = pp.second.ymin;
        r.w = pp.second.xmax - pp.second.xmin;
        r.h = pp.second.ymax - pp.second.ymin;
        //copy landmark data
        for(int i = 0; i < 10; ++i) {
            r.lm[i] = pp.second.lm[i];
        }
        facesInfo.emplace_back(r);
    }

    return facesInfo;
}
