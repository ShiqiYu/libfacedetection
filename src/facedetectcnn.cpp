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

#include "facedetectcnn.h"
#include <string.h>
#include <cmath>
#include <vector>
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

inline int dotProductUint8Int8(unsigned char * p1, signed char * p2, int num)
{
    int sum = 0;
    
#if defined(_ENABLE_NEON)
    int8x8x2_t a, b;
    int32x4_t mul_s32x4;
    int32x4_t result_vec;
    result_vec = vdupq_n_s32(0); //zeros

    for (int i = 0; i < num; i += 16)
    {
        a = vld2_s8((signed char*)p1 + i);
        b = vld2_s8(p2 + i);
        mul_s32x4 = vpaddlq_s16(vaddq_s16(vmull_s8(a.val[0], b.val[0]), vmull_s8(a.val[1], b.val[1])));
        result_vec = vaddq_s32(result_vec, mul_s32x4);     
    }
    sum += vgetq_lane_s32(result_vec, 0);
    sum += vgetq_lane_s32(result_vec, 1);
    sum += vgetq_lane_s32(result_vec, 2);
    sum += vgetq_lane_s32(result_vec, 3);
    /*
#if defined(_ENABLE_NEON)
    int8x16_t a, b;
    int32x4_t result_vec;
    result_vec = vdupq_n_s32(0); //zeros

    for (int i = 0; i < num; i += 16)
    {
        a = vld1q_s8((signed char*)p1 + i);
        b = vld1q_s8(p2 + i);
        result_vec = vdotq_s32(result_vec, a, b);
       
    }
    sum += vgetq_lane_s32(result_vec, 0);
    sum += vgetq_lane_s32(result_vec, 1);
    sum += vgetq_lane_s32(result_vec, 2);
    sum += vgetq_lane_s32(result_vec, 3);
*/
#elif defined(_ENABLE_AVX512)
    __m512i sum_int16x32;
    __m512i tmp_int32x16;
    __m512i a_uint8x64, b_int8x64;
    __m512i ones16 = _mm512_set1_epi16(1);
    __m512i sum_int32x16 = _mm512_setzero_si512();
    for (int i = 0; i < num; i += 64)
    {
        a_uint8x64 = _mm512_load_si512((__m256i const*)(p1 + i));
        b_int8x64 = _mm512_load_si512((__m256i const*)(p2 + i));
        sum_int16x32 = _mm512_maddubs_epi16(a_uint8x64, b_int8x64);
        tmp_int32x16 = _mm512_madd_epi16(sum_int16x32, ones16);
        sum_int32x16 = _mm512_add_epi32(sum_int32x16, tmp_int32x16);
    }
    sum += _mm512_reduce_add_epi32(sum_int32x16);
#elif defined(_ENABLE_AVX2)
    __m256i sum_int16x16;
    __m256i tmp_int32x8;
    __m256i a_uint8x32, b_int8x32;
    __m256i ones16 = _mm256_set1_epi16(1);
    __m256i sum_int32x8 = _mm256_setzero_si256();
    for (int i = 0; i < num; i += 32)
    {
        a_uint8x32 = _mm256_load_si256((__m256i const*)(p1 + i));
        b_int8x32 = _mm256_load_si256((__m256i const*)(p2 + i));
        sum_int16x16 = _mm256_maddubs_epi16(a_uint8x32, b_int8x32);
        tmp_int32x8 = _mm256_madd_epi16(sum_int16x16, ones16);
        sum_int32x8 = _mm256_add_epi32(sum_int32x8, tmp_int32x8);
    }
    sum_int32x8 = _mm256_hadd_epi32(sum_int32x8, sum_int32x8);
    sum_int32x8 = _mm256_hadd_epi32(sum_int32x8, sum_int32x8);
    sum = ((int*)&sum_int32x8)[0] + ((int*)&sum_int32x8)[4];
       
#else

    for (int i = 0; i < num; i++)
    {
        sum += (int(p1[i]) * int(p2[i]));
    }

#endif
    return sum;
}

bool convolution1x1P0S1(const CDataBlob<unsigned char> *inputData, const Filters* filters, CDataBlob<int> *outputData)
{
#if defined(_OPENMP)
#pragma omp parallel for
#endif
    for (int row = 0; row < outputData->height; row++)
    {
        for (int col = 0; col < outputData->width; col++)
        {
            int * pOut = (outputData->data + (row*outputData->width + col)*outputData->channelStep / sizeof(int));
            unsigned char * pIn = (inputData->data + (row*inputData->width + col)*inputData->channelStep / sizeof(unsigned char));
            for (int ch = 0; ch < outputData->channels; ch++)
            {
                signed char * pF = (filters->filters[ch]->data);
                pOut[ch] = dotProductUint8Int8(pIn, pF, inputData->channels);
                pOut[ch] += (inputData->bias * filters->filters[ch]->bias);
            }
        }
    }
    return true;
}


bool convolution3x3P0(const CDataBlob<unsigned char> *inputData, const Filters* filters, CDataBlob<int> *outputData)
{ 
#if defined(_OPENMP)
#pragma omp parallel for
#endif
    for (int row = 0; row < outputData->height; row++) 
    {  
        int elementStep = inputData->channelStep;
        int stride = filters->stride;
        int src_centery = row * stride;
        for (int col = 0; col < outputData->width; col++)
        { 
            int srcx_start = col * stride - 1;
            int srcx_end = srcx_start + 3;
            srcx_start = MAX(0, srcx_start);
            srcx_end = MIN(srcx_end, inputData->width);
            int num_pixels_inbytes = (srcx_end - srcx_start) * elementStep;

            for (int ch = 0; ch < outputData->channels; ch++)
            {
                int srcy = src_centery - 1;

                unsigned char* pIn = (inputData->data + (srcy * inputData->width + srcx_start) * elementStep);
                signed char* pF = (filters->filters[ch]->data) + (srcx_start - col * stride + 1) * elementStep;
                int* pOut = (outputData->data + (row * outputData->width + col) * outputData->channelStep / sizeof(int));
                pOut[ch] = 0;//the new created blob is not zeros, clear it first

                {
                    if (srcy >= 0)
                    {
                        pOut[ch] += dotProductUint8Int8(pIn,
                            pF,
                            num_pixels_inbytes);
                    }
                }
                {
                    srcy++;
                    {
                        pIn += (inputData->width * elementStep);
                        pOut[ch] += dotProductUint8Int8(pIn,
                            pF + (3 * elementStep),
                            num_pixels_inbytes);
                    }
                }
                {
                    srcy++;
                    if (srcy < inputData->height)
                    {
                        pIn += (inputData->width * elementStep);
                        pOut[ch] += dotProductUint8Int8(pIn,
                            pF + (6 * elementStep),
                            num_pixels_inbytes);
                    }
                }
                pOut[ch] += (inputData->bias * filters->filters[ch]->bias);
            }
        }
    }
    return true; 
}


bool convolution(CDataBlob<unsigned char> *inputData, const Filters* filters, CDataBlob<int> *outputData) 
{
    if (inputData->data == NULL)
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return false;
    }
    if (filters->filters.size() == 0)
    {
        cerr << __FUNCTION__ << ": There is not filters." << endl;
        return false;
    }
    //check filters' dimensions
    int filterW = filters->filters[0]->width;
    int filterH = filters->filters[0]->height;
    int filterC = filters->filters[0]->channels;
    int filterS = filters->stride;
    int filterP = filters->pad;

    int outputW = 0;
    int outputH = 0;
    int outputC = (int)filters->filters.size();

    for (int i = 1; i < outputC; i++)
    {
        if ((filterW != filters->filters[i]->width) ||
            (filterH != filters->filters[i]->height) ||
            (filterC != filters->filters[i]->channels))
        {
            cerr << __FUNCTION__ << ": The filters must be the same size." << endl;
            return false;
        }
    }

    if (filterC != inputData->channels)
    {
        cerr << __FUNCTION__ << ": The number of channels of filters must be the same with the input data. " << filterC << " vs " << inputData->channels << endl;
        return false;
    }

    //calculate the output dimension
    if (filterW == 1 && filterH == 1) //1x1 filters
    {
        if (filterS != 1)
        {
            cerr << __FUNCTION__ << ": Onle stride = 1 is supported for 1x1 filters." << endl;
            return false;
        }
        if (filterP != 0)
        {
            cerr << __FUNCTION__ << ": Onle pad = 0 is supported for 1x1 filters." << endl;
            return false;
        }
        outputW = inputData->width;
        outputH = inputData->height;

    }
    else if (filterW == 3 && filterH == 3) //3x3 filters
    {
        if (filterS == 1 && filterP == 1)
        {
            outputW = inputData->width;
            outputH = inputData->height;
        }
        else if (filterS == 2 && filterP == 1)
        {
            outputW = (inputData->width + 1) / 2;
            outputH = (inputData->height + 1) / 2;
        }
        else
        {
            cerr << __FUNCTION__ << ": Unspported filter stride=" << filterS << " or pad=" << filterP << endl;
            cerr << __FUNCTION__ << ": For 3x3 filters, only pad=1 and stride={1,2} are supported." << endl;
            return false;
        }
    }
    else
    {
        cerr << __FUNCTION__ << ": Unsported filter size." << endl;
        return false;
    }

    if (outputW < 1 || outputH < 1)
    {
        cerr << __FUNCTION__ << ": The size of the output is not correct. (" << outputW << ", " << outputH << ")." << endl;
        return false;
    }

    outputData->create(outputW, outputH, outputC);

    if (filterW == 1 && filterH == 1) //1x1 filters
    {
        convolution1x1P0S1(inputData, filters, outputData);
    }
    else if (filterW == 3 && filterH == 3) //3x3 filters
    {
        convolution3x3P0(inputData, filters, outputData);
    }

    outputData->scale = inputData->scale * filters->scale;
    outputData->bias = static_cast<int>(round(outputData->scale)); 

	return true;
}

bool convolution_relu(CDataBlob<unsigned char> *inputData, const Filters* filters, CDataBlob<unsigned char> *outputData)
{
    //do convolution first
    CDataBlob<int> tmpOutputData;
    bool bFlag = convolution(inputData, filters, &tmpOutputData);
    if (bFlag == false)
        return false;

    //set negative values to zeros, 
    //and find the max value
    int nMaxValue = 0;
#if defined(_ENABLE_NEON)
    int32x4_t max_int32x4 = vdupq_n_s32(0); //init to zeros
    int32x4_t zeros = vdupq_n_s32(0); //zeros
#elif defined(_ENABLE_AVX512)
    __m512i max_int32x16 = _mm512_setzero_si512();
#elif defined(_ENABLE_AVX2)
    __m256i max_int32x8 = _mm256_setzero_si256();
#endif

    for (int row = 0; row < tmpOutputData.height; row++)
    {
        for (int col = 0; col < tmpOutputData.width; col++)
        {
            int * pData = (tmpOutputData.data + (row*tmpOutputData.width + col)*tmpOutputData.channelStep / sizeof(int));
#if defined(_ENABLE_NEON)
            int32x4_t a;
            int32x4_t result_vec;

            for (int ch = 0; ch < tmpOutputData.channels; ch += 4)
            {
                a = vld1q_s32(pData + ch);
                result_vec = vmaxq_s32(a, zeros);
                max_int32x4 = vmaxq_s32(result_vec, max_int32x4);
                vst1q_s32(pData + ch, result_vec);
            }
#elif defined(_ENABLE_AVX512)
            __m512i a, bzeros;
            bzeros = _mm512_setzero_si512(); //zeros

            for (int ch = 0; ch < tmpOutputData.channels; ch += 16)
            {
                a = _mm512_load_si512((__m256i const*)(pData + ch));
                a = _mm512_max_epi32(a, bzeros);
                max_int32x16 = _mm512_max_epi32(a, max_int32x16);
                _mm512_store_si512((__m512i*)(pData + ch), a);
            }
#elif defined(_ENABLE_AVX2)
            __m256i a, bzeros;
            bzeros = _mm256_setzero_si256(); //zeros

            for (int ch = 0; ch < tmpOutputData.channels; ch += 8)
            {
                a = _mm256_load_si256((__m256i const*)(pData + ch));
                a = _mm256_max_epi32(a, bzeros);
                max_int32x8 = _mm256_max_epi32(a, max_int32x8);
                _mm256_store_si256((__m256i*)(pData + ch), a);
            }
#else
            for (int ch = 0; ch < tmpOutputData.channels; ch++)
            {
                pData[ch] = MAX(pData[ch], 0);
                nMaxValue = MAX(pData[ch], nMaxValue);
            }
#endif
        }
    }
#if defined(_ENABLE_NEON)
    {
        int maxarray_int32x4[4];
        vst1q_s32(maxarray_int32x4, max_int32x4);
        for (int i = 0; i < 4; i++)
            nMaxValue = MAX(maxarray_int32x4[i], nMaxValue);
    }
#elif defined(_ENABLE_AVX512)
    {
        int maxarray_int32x16[16];
        _mm512_store_si512((__m256i*)maxarray_int32x16, max_int32x16);
        for (int i = 0; i < 16; i++)
            nMaxValue = MAX(maxarray_int32x16[i], nMaxValue);
    }
#elif defined(_ENABLE_AVX2)
    {
        int maxarray_int32x8[8];
        _mm256_store_si256((__m256i*)maxarray_int32x8, max_int32x8);
        for (int i = 0; i < 8; i++)
            nMaxValue = MAX(maxarray_int32x8[i], nMaxValue);
    }
#endif

    //scale the data to uint8 or int8
    float fCurrentScale = (_MAX_UINT8_VALUE) / float(nMaxValue);
    outputData->create(tmpOutputData.width, tmpOutputData.height, tmpOutputData.channels);
    outputData->scale = tmpOutputData.scale * fCurrentScale;
    outputData->bias = static_cast<int>(round(tmpOutputData.bias * fCurrentScale ));

    for (int row = 0; row < outputData->height; row++)
    {
        for (int col = 0; col < outputData->width; col++)
        {
            int * pInt32Data = (tmpOutputData.data + (size_t(row) * tmpOutputData.width + col)*tmpOutputData.channelStep / sizeof(int));
            unsigned char * pUInt8Data = (outputData->data + (size_t(row) *outputData->width + col)*outputData->channelStep / sizeof(unsigned char));

            for (int ch = 0; ch < outputData->channels; ch++)
            {
                pUInt8Data[ch] = (unsigned char)(pInt32Data[ch] * fCurrentScale +0.499f);
            }
        }
    }
    return true;
}

//only 2X2 S2 is supported
bool maxpooling2x2S2(const CDataBlob<unsigned char> *inputData, CDataBlob<unsigned char> *outputData)
{
    if (inputData->data == NULL)
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return false;
    }
    int outputW = static_cast<int>(ceil((inputData->width - 3.0f) / 2)) + 1;
    int outputH = static_cast<int>(ceil((inputData->height - 3.0f) / 2)) + 1;
    int outputC = inputData->channels;

    if (outputW < 1 || outputH < 1)
    {
        cerr << __FUNCTION__ << ": The size of the output is not correct. (" << outputW << ", " << outputH << ")." << endl;
        return false;
    }

    int lineElementStep = inputData->width * inputData->channelStep;

    outputData->create(outputW, outputH, outputC);
    outputData->scale = inputData->scale;
    outputData->bias = inputData->bias;

    for (int row = 0; row < outputData->height; row++)
    {
        for (int col = 0; col < outputData->width; col++)
        {
            size_t inputMatOffsetsInElement[4];
            int elementCount = 0;

            int hstart = row * 2;
            int wstart = col * 2;
            int hend = MIN(hstart + 2, inputData->height);
            int wend = MIN(wstart + 2, inputData->width);

            for (int fy = hstart; fy < hend; fy++)
                for (int fx = wstart; fx < wend; fx++)
                {
                    inputMatOffsetsInElement[elementCount++] = (size_t(fy) *inputData->width + fx) * inputData->channelStep / sizeof(unsigned char);
                }

            unsigned char * pOut = outputData->data + (size_t(row) * outputData->width + col) * outputData->channelStep / sizeof(unsigned char);
            unsigned char * pIn = inputData->data;

#if defined(_ENABLE_NEON)
            for (int ch = 0; ch < outputData->channels; ch += 16)
            {
                uint8x16_t a;
                uint8x16_t maxval = vld1q_u8(pIn + ch + inputMatOffsetsInElement[0]);
                for (int el = 1; el < elementCount; el++)
                {
                    a = vld1q_u8(pIn + ch + inputMatOffsetsInElement[el]);
                    maxval = vmaxq_u8(maxval, a);
                }
                vst1q_u8(pOut + ch, maxval);
            }

#elif defined(_ENABLE_AVX512)
            for (int ch = 0; ch < outputData->channels; ch += 64)
            {
                __m512i a;
                __m512i maxval_uint8x64 = _mm512_load_si512((__m512i const*)(pIn + ch + inputMatOffsetsInElement[0]));
                for (int el = 1; el < elementCount; el++)
                {
                    a = _mm512_load_si512((__m512i const*)(pIn + ch + inputMatOffsetsInElement[el]));
                    maxval_uint8x64 = _mm512_max_epu8(maxval_uint8x64, a);
                }
                _mm512_store_si512((__m512i*)(pOut + ch), maxval_uint8x64);
            }
#elif defined(_ENABLE_AVX2)
            for (int ch = 0; ch < outputData->channels; ch += 32)
            {
                __m256i a;
                __m256i maxval_uint8x32 = _mm256_load_si256((__m256i const*)(pIn + ch + inputMatOffsetsInElement[0]));
                for (int el = 1; el < elementCount; el++)
                {
                    a = _mm256_load_si256((__m256i const*)(pIn + ch + inputMatOffsetsInElement[el]));
                    maxval_uint8x32 = _mm256_max_epu8(maxval_uint8x32, a);
                }
                _mm256_store_si256((__m256i*)(pOut + ch), maxval_uint8x32);
            }
#else

            for (int ch = 0; ch < outputData->channels; ch++)
            {
                unsigned char maxval = pIn[ch + inputMatOffsetsInElement[0]];

                for (int el = 1; el < elementCount; el++)
                {
                    maxval = MAX(maxval, pIn[ch + inputMatOffsetsInElement[el]]);
                }
                pOut[ch] = maxval;
            }
#endif
        }
    }

    return true;
}


template<typename T>
bool concat4(const CDataBlob<T> *inputData1, const CDataBlob<T> *inputData2, const CDataBlob<T> *inputData3, const CDataBlob<T> *inputData4, CDataBlob<T> *outputData)
{
    if ((inputData1->data == NULL) || (inputData2->data == NULL) || (inputData3->data == NULL) || (inputData4->data == NULL))
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return false;
    }

    if ((inputData1->width != inputData2->width) ||
        (inputData1->height != inputData2->height) ||
        (inputData1->width != inputData3->width) ||
        (inputData1->height != inputData3->height) ||
        (inputData1->width != inputData4->width) ||
        (inputData1->height != inputData4->height))
    {
        cerr << __FUNCTION__ << ": The three inputs must have the same size." << endl;
        return false;
    }
    int outputW = inputData1->width;
    int outputH = inputData1->height;
    int outputC = inputData1->channels + inputData2->channels + inputData3->channels + inputData4->channels;

    if (outputW < 1 || outputH < 1 || outputC < 1)
    {
        cerr << __FUNCTION__ << ": The size of the output is not correct. (" << outputW << ", " << outputH << ", " << outputC << ")." << endl;
        return false;
    }

    outputData->create(outputW, outputH, outputC);

    for (int row = 0; row < outputData->height; row++)
    {
        for (int col = 0; col < outputData->width; col++)
        {
            T * pOut = (outputData->data + (row*outputData->width + col)*outputData->channelStep / sizeof(T));
            T * pIn1 = (inputData1->data + (row*inputData1->width + col)*inputData1->channelStep / sizeof(T));
            T * pIn2 = (inputData2->data + (row*inputData2->width + col)*inputData2->channelStep / sizeof(T));
            T * pIn3 = (inputData3->data + (row*inputData3->width + col)*inputData3->channelStep / sizeof(T));
            T * pIn4 = (inputData4->data + (row*inputData4->width + col)*inputData4->channelStep / sizeof(T));

            memcpy(pOut, pIn1, sizeof(T)* inputData1->channels);
            memcpy(pOut + inputData1->channels, pIn2, sizeof(T)* inputData2->channels);
            memcpy(pOut + inputData1->channels + inputData2->channels, pIn3, sizeof(T)* inputData3->channels);
            memcpy(pOut + inputData1->channels + inputData2->channels + inputData3->channels, pIn4, sizeof(T)* inputData4->channels);
        }
    }
    return true;
}
template bool concat4(const CDataBlob<float> *inputData1, const CDataBlob<float> *inputData2, const CDataBlob<float> *inputData3, const CDataBlob<float> *inputData4, CDataBlob<float> *outputData);

bool convertInt2Float(CDataBlob<int> * inputData, CDataBlob<float> * outputData)
{
    if (inputData == NULL || outputData == NULL)
    {
        cerr << __FUNCTION__ << ": The input or output data is null." << endl;
        return false;
    }

    outputData->create(inputData->width, inputData->height, inputData->channels);
    float s = 1.0f / inputData->scale;
    for (int row = 0; row < outputData->height; row++)
    {
        for (int col = 0; col < outputData->width; col++)
        {
            int * pInData = (inputData->data + (row*inputData->width + col)*inputData->channelStep / sizeof(int));
            float * pOutData = (outputData->data + (row*outputData->width + col)*outputData->channelStep / sizeof(float));

            for (int ch = 0; ch < outputData->channels; ch++)
            {
                pOutData[ch] = pInData[ch] * s;
            }
        }
    }
    outputData->scale = 1.0f;
    outputData->bias = (int)roundf(inputData->bias * s);
    return true;
}


bool priorbox(const CDataBlob<unsigned char> * featureData, int img_width, int img_height, int step, int num_sizes, float * pWinSizes, CDataBlob<float> * outputData)
{
    if ((featureData->data == NULL) ||
        pWinSizes == NULL)
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return false;
    }

    int feature_width = featureData->width;
    int feature_height = featureData->height;

    outputData->create(feature_width, feature_height, num_sizes * 4);

	for (int h = 0; h < feature_height; ++h) 
	{
		for (int w = 0; w < feature_width; ++w) 
		{
            float * pOut = (float*)(outputData->data + ( size_t(h) * outputData->width + w) * outputData->channelStep / sizeof(float));
            int idx = 0;
            //priorbox
			for (int s = 0; s < num_sizes; s++) 
			{
				float min_size_ = pWinSizes[s];
                
                float center_x = (w + 0.5f) * step;
                float center_y = (h + 0.5f) * step;
                // xmin
                pOut[idx++] = (center_x - min_size_ / 2.f) / img_width;
                // ymin
                pOut[idx++] = (center_y - min_size_ / 2.f) / img_height;
                // xmax
                pOut[idx++] = (center_x + min_size_ / 2.f) / img_width;
                // ymax
                pOut[idx++] = (center_y + min_size_ / 2.f) / img_height;

			}
		}
	}
    
    return true;
}

bool softmax1vector2class(CDataBlob<float> *inputOutputData)
{
    if (inputOutputData == NULL )
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return false;
    }

    if(inputOutputData->width != 1 || inputOutputData->height != 1)
    {
        cerr << __FUNCTION__ << ": The input data must be Cx1x1." << endl;
        return false;
    }

    int num = inputOutputData->channels;
    float * pData = inputOutputData->data;

//#if defined(_OPENMP)
//#pragma omp parallel for
//#endif
    for(int i = 0; i < num; i+= 2)
    {
        float v1 = pData[i];
        float v2 = pData[i + 1];
        float vm = MAX(v1, v2);
        v1 -= vm;
        v2 -= vm;
        v1 = expf(v1);
        v2 = expf(v2);
        vm = v1 + v2;
        pData[i] = v1/vm;
        pData[i+1] = v2/vm;
    }
    return true;
}

template<typename T>
bool blob2vector(const CDataBlob<T> * inputData, CDataBlob<T> * outputData)
{
    if (inputData->data == NULL || outputData == NULL)
    {
        cerr << __FUNCTION__ << ": The input or output data is null." << endl;
        return false;
    }

    outputData->create(1, 1, inputData->width * inputData->height * inputData->channels);
    outputData->scale = inputData->scale;
    outputData->bias = inputData->bias;

    int bytesOfAChannel = inputData->channels * sizeof(T);
    T * pOut = outputData->data;
    for (int row = 0; row < inputData->height; row++)
    {
        for (int col = 0; col < inputData->width; col++)
        {
            T * pIn = (inputData->data + (size_t(row) * inputData->width + col) * inputData->channelStep / sizeof(T));
            memcpy(pOut, pIn, bytesOfAChannel);
            pOut += inputData->channels;
        }
    }

    return true;
}
template bool blob2vector(const CDataBlob<signed char> * inputData, CDataBlob<signed char> * outputData);
template bool blob2vector(const CDataBlob<int> * inputData, CDataBlob<int> * outputData);
template bool blob2vector(const CDataBlob<float> * inputData, CDataBlob<float> * outputData);

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

bool SortScoreBBoxPairDescend(const pair<float, NormalizedBBox>& pair1,   const pair<float, NormalizedBBox>& pair2) 
{
    return pair1.first > pair2.first;
}


bool detection_output(const CDataBlob<float> * priorbox, const CDataBlob<float> * loc, const CDataBlob<float> * conf, float overlap_threshold, float confidence_threshold, int top_k, int keep_top_k, CDataBlob<float> * outputData)
{
    if (priorbox->data == NULL || loc->data == NULL || conf->data == NULL)
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return 0;
    }

    if (priorbox->channels != conf->channels * 2 || loc->channels != conf->channels*7 )
    {
        cerr << __FUNCTION__ << ": The sizes of the inputs are not match." << endl;
        cerr << "priorbox channels=" << priorbox->channels << ", loc channels=" << loc->channels << ", conf channels=" << conf->channels << endl;
        return 0;
    }

    float prior_variance[4] = {0.1f, 0.1f, 0.2f, 0.2f};
    float * pPriorBox = priorbox->data;
    float * pLoc = loc->data;
    float * pConf = conf->data;

    vector<pair<float, NormalizedBBox> > score_bbox_vec;
    vector<pair<float, NormalizedBBox> > final_score_bbox_vec;

    //get the candidates those are > confidence_threshold
    for(int i = 0; i < conf->channels; i+=2)
    {
        float conf = pConf[i + 1];
        int face_idx = i / 2;
        if(conf > confidence_threshold)
        {
            float fBox_x1 = pPriorBox[face_idx * 4];
            float fBox_y1 = pPriorBox[face_idx * 4 + 1];
            float fBox_x2 = pPriorBox[face_idx * 4 + 2];
            float fBox_y2 = pPriorBox[face_idx * 4 + 3];

            float locx1 = pLoc[face_idx * 14];
            float locy1 = pLoc[face_idx * 14 + 1];
            float locx2 = pLoc[face_idx * 14 + 2];
            float locy2 = pLoc[face_idx * 14 + 3];

            float prior_width = fBox_x2 - fBox_x1;
            float prior_height = fBox_y2 - fBox_y1;
            float prior_center_x = (fBox_x1 + fBox_x2)/2;
            float prior_center_y = (fBox_y1 + fBox_y2)/2;

            float box_centerx = prior_variance[0] * locx1 * prior_width + prior_center_x;
            float box_centery = prior_variance[1] * locy1 * prior_height + prior_center_y;
            float box_width = expf(prior_variance[2] * locx2) * prior_width;
            float box_height = expf(prior_variance[3] * locy2) * prior_height;

            fBox_x1 = box_centerx - box_width / 2.f;
            fBox_y1 = box_centery - box_height /2.f;
            fBox_x2 = box_centerx + box_width / 2.f;
            fBox_y2 = box_centery + box_height /2.f;

            fBox_x1 = MAX(0, fBox_x1);
            fBox_y1 = MAX(0, fBox_y1);
            fBox_x2 = MIN(1.f, fBox_x2);
            fBox_y2 = MIN(1.f, fBox_y2);

            NormalizedBBox bb;
            bb.xmin = fBox_x1;
            bb.ymin = fBox_y1;
            bb.xmax = fBox_x2;
            bb.ymax = fBox_y2;
            //store the five landmarks
            for (int i = 0; i < 5; i++)
            {
                float lmx = pLoc[face_idx * 14 + 4 + i * 2];
                float lmy = pLoc[face_idx * 14 + 4 + i * 2 + 1];
                lmx = prior_variance[0] * lmx * prior_width + prior_center_x;
                lmy = prior_variance[1] * lmy * prior_height + prior_center_y;
                bb.lm[i * 2] = lmx;
                bb.lm[i * 2 + 1] = lmy;
            }

            score_bbox_vec.push_back(std::make_pair(conf, bb));
        }
    }

    //Sort the score pair according to the scores in descending order
    std::stable_sort(score_bbox_vec.begin(), score_bbox_vec.end(), SortScoreBBoxPairDescend);

    // Keep top_k scores if needed.
    if (top_k > -1 && top_k < score_bbox_vec.size()) {
        score_bbox_vec.resize(top_k);
    }


    //Do NMS
    final_score_bbox_vec.clear();
    while (score_bbox_vec.size() != 0) {
        const NormalizedBBox bb1 = score_bbox_vec.front().second;
        bool keep = true;
        for (int k = 0; k < final_score_bbox_vec.size(); ++k)
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
    if (keep_top_k > -1 && keep_top_k < final_score_bbox_vec.size()) {
        final_score_bbox_vec.resize(keep_top_k);
    }

    //copy the results to the output blob
    int num_faces = (int)final_score_bbox_vec.size();
    if (num_faces == 0)
        outputData->setNULL();
    else
    {
        outputData->create(num_faces, 1, 15);
        for (int fi = 0; fi < num_faces; fi++)
        {
            pair<float, NormalizedBBox> pp = final_score_bbox_vec[fi];
            float * pOut = (outputData->data + size_t(fi) * outputData->channelStep / sizeof(float));
            pOut[0] = pp.first;
            pOut[1] = pp.second.xmin;
            pOut[2] = pp.second.ymin;
            pOut[3] = pp.second.xmax;
            pOut[4] = pp.second.ymax;
            //copy landmark data
            for (int lm = 0; lm < 10; lm++)
                pOut[5 + lm] = pp.second.lm[lm];

        }
    }

    return true;
}

