/*
By downloading, copying, installing or using the software you agree to this license.
If you do not agree to this license, do not download, install,
copy or use the software.


                  License Agreement For libfacedetection
                     (3-clause BSD License)

Copyright (c) 2018-2019, Shiqi Yu, all rights reserved.
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

#if defined( __WIN__) || defined(_WINDOWS)
#define SSE_256ELEMENT(vec, idx) vec.m256_f32[(idx)]
#else
#define SSE_256ELEMENT(vec, idx) vec[(idx)]
#endif

#if !defined(_ENABLE_OPENMP_SIMD) && ((defined(_OPENMP) && (_OPENMP >= 201307L)))
#  define _ENABLE_OPENMP_SIMD
#elif defined(__cilk)
#  define _ENABLE_CILKPLUS
#endif

typedef struct NormalizedBBox_
{
    float xmin;
    float ymin;
    float xmax;
    float ymax;
} NormalizedBBox;


void* myAlloc(size_t size)
{
	char *ptr, *ptr0;
	ptr0 = (char*)malloc(
		(size_t)(size + _MALLOC_ALIGN * ((size >= 4096) + 1) + sizeof(char*)));

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


inline float dotProductFloatChGeneral(float* p1, float * p2, int num, int lengthInBytes)
{
#if defined(_ENABLE_NEON) && !defined(_ENABLE_INT8_CONV)
    float sum = 0.0f;
    float32x4_t a, b;
    float32x4_t result_vec;

    result_vec = vdupq_n_f32(0); //zeros
    for (int i = 0; i < num; i += 4)
    {
        a = vld1q_f32(p1 + i);
        b = vld1q_f32(p2 + i);
        result_vec = vmlaq_f32(result_vec, a, b);
    }
    sum += vgetq_lane_f32(result_vec, 0);
    sum += vgetq_lane_f32(result_vec, 1);
    sum += vgetq_lane_f32(result_vec, 2);
    sum += vgetq_lane_f32(result_vec, 3);

    return sum;
#elif defined(_ENABLE_AVX2) && !defined(_ENABLE_INT8_CONV)
    float sum = 0;
    int end = lengthInBytes / sizeof(float);

    __m256 sumvec = _mm256_setzero_ps();
    __m256 avec, bvec;
    for (int i = 0; i < end; i += 8)
    {
        avec = _mm256_load_ps(p1 + i);
        bvec = _mm256_load_ps(p2 + i);
        //_mm256_fmadd_ps needs FMA support 
        //but _mm256_add_ps and _mm256_mul_ps only need AVX

        //sumvec = _mm256_add_ps(sumvec, _mm256_mul_ps(avec, bvec));

        //fmadd is faster than add+mul
        sumvec = _mm256_fmadd_ps(avec, bvec, sumvec);

        //Note: _mm256_dp_ps is much slower than the previou line of code
    }
    sumvec = _mm256_hadd_ps(sumvec, sumvec);
    sumvec = _mm256_hadd_ps(sumvec, sumvec);
    sum += SSE_256ELEMENT(sumvec, 0);
    sum += SSE_256ELEMENT(sumvec, 4);

    return sum;

#else
    float sum = 0;

#if defined(_ENABLE_OPENMP_SIMD)
#pragma omp simd reduction(+:sum)
#endif
    for (int i = 0; i < num; i++)
    {
        sum += (p1[i] * p2[i]);
    }
    return sum;
#endif 
}

inline int dotProductInt8ChGeneral(signed char * p1, signed char * p2, int num, int lengthInBytes)
{
#if defined(_ENABLE_NEON) && defined(_ENABLE_INT8_CONV)
    //int sum = 0;
    //int16x8_t a, b;
    //int16x8_t result_vec;
    //int32x4_t d;
    //

    //result_vec = vdupq_n_s16(0); //zeros
    //for (int i = 0; i < num; i += 8)
    //{
    //    a = vld1q_s16(p1 + i);
    //    b = vld1q_s16(p2 + i);
    //    result_vec = vmlaq_s16(result_vec, a, b);
    //}
    //d = vpaddlq_s16(result_vec);
    //sum += vgetq_lane_s32(d, 0);
    //sum += vgetq_lane_s32(d, 1);
    //sum += vgetq_lane_s32(d, 2);
    //sum += vgetq_lane_s32(d, 3);

    //return sum;

    int sum = 0;
    int8x8x2_t a, b;
    int16x8_t result_vec;
    int32x4_t d;


    result_vec = vdupq_n_s16(0); //zeros
    for (int i = 0; i < num; i += 16)
    {
        a = vld2_s8(p1 + i);
        b = vld2_s8(p2 + i);
        result_vec = vmlal_s8(result_vec, a.val[0], b.val[0]);
        result_vec = vmlal_s8(result_vec, a.val[1], b.val[1]);
    }
    d = vpaddlq_s16(result_vec);
    sum += vgetq_lane_s32(d, 0);
    sum += vgetq_lane_s32(d, 1);
    sum += vgetq_lane_s32(d, 2);
    sum += vgetq_lane_s32(d, 3);

    return sum;

#elif defined(_ENABLE_AVX2) && defined(_ENABLE_INT8_CONV)
    int sum = 0;
    int i = 0;

    short sumarray[16];
   
    __m256i temp_sum;
    __m128i ac, bc;
    __m256i as, bs;
    for (; i < num; i += 16)
    {
        ac = _mm_load_si128((__m128i*)(p1 + i));
        bc = _mm_load_si128((__m128i*)(p2 + i));
        as = _mm256_cvtepi8_epi16(ac);
        bs = _mm256_cvtepi8_epi16(bc);
        temp_sum = _mm256_mullo_epi16(as, bs);
        temp_sum = _mm256_hadd_epi16(temp_sum, temp_sum);
        temp_sum = _mm256_hadd_epi16(temp_sum, temp_sum);
        //temp_sum = _mm256_hadd_epi16(temp_sum, temp_sum);
        _mm256_store_si256((__m256i*)sumarray, temp_sum);
        //sum += ((int)(sumarray[0]) + (int)(sumarray[8]));
        sum += ((int)(sumarray[0]) + (int)(sumarray[1]) + +(int)(sumarray[8]) + (int)(sumarray[9]));
    }
    return sum;
#else

    int sum = 0;

#if defined(_ENABLE_OPENMP_SIMD)
#pragma omp simd reduction(+:sum)
#endif
    for (int i = 0; i < num; i++)
    {
        sum += ( int(p1[i]) * int(p2[i]));
    }
    return sum;
#endif
}

bool convolutionFloat1x1P0S1(const CDataBlob *inputData, const Filters* filters, CDataBlob *outputData)
{
#if defined(_OPENMP)
#pragma omp parallel for
#endif
    for (int row = 0; row < outputData->height; row++)
    {
        for (int col = 0; col < outputData->width; col++)
        {
            float * pOut = (outputData->data_float + (row*outputData->width + col)*outputData->floatChannelStepInByte / sizeof(float));
            float * pIn = (inputData->data_float + (row*inputData->width + col)*inputData->floatChannelStepInByte / sizeof(float));
            for (int ch = 0; ch < outputData->channels; ch++)
            {
                float * pF = (float*)(filters->filters[ch]->data_float);
                pOut[ch] = dotProductFloatChGeneral(pIn, pF, inputData->channels, inputData->floatChannelStepInByte);
            }
        }
    }
    return true;
}

bool convolutionInt81x1P0S1(const CDataBlob *inputData, const Filters* filters, CDataBlob *outputData)
{
#if defined(_OPENMP)
#pragma omp parallel for
#endif
    for (int row = 0; row < outputData->height; row++)
    {
        for (int col = 0; col < outputData->width; col++)
        {
            float * pOut = (outputData->data_float + (row*outputData->width + col)*outputData->floatChannelStepInByte / sizeof(float));
            signed char * pIn = (inputData->data_int8 + (row*inputData->width + col)*inputData->int8ChannelStepInByte / sizeof(char));
            for (int ch = 0; ch < outputData->channels; ch++)
            {
                signed char * pF = (filters->filters[ch]->data_int8);
                pOut[ch] = (float)dotProductInt8ChGeneral(pIn, pF, inputData->channels, inputData->int8ChannelStepInByte);
            }
        }
    }
    return true;
}



bool convolutionFloat3x3P1ChGeneral(const CDataBlob *inputData, const Filters* filters, CDataBlob *outputData)
{
#if defined(_OPENMP)
#pragma omp parallel for
#endif
    for (int row = 0; row < outputData->height; row++)
    {
        int elementStepInFloat = inputData->floatChannelStepInByte/sizeof(float);
        int stride = filters->stride;
        int src_centery = row * stride;
        for (int col = 0; col < outputData->width; col++)
        {
            int srcx_start = col * stride - 1;
            int srcx_end = srcx_start + 3;
            srcx_start = MAX(0, srcx_start);
            srcx_end = MIN(srcx_end, inputData->width);
            int num_pixels = srcx_end - srcx_start;
            int num_pixels_infloat = (srcx_end - srcx_start) * elementStepInFloat;

            for (int ch = 0; ch < outputData->channels; ch++)
            {
                int srcy = src_centery - 1;

                float * pIn = (inputData->data_float + (srcy *inputData->width + srcx_start)*elementStepInFloat);
                float * pF = (filters->filters[ch]->data_float) + (srcx_start - col*stride + 1) * elementStepInFloat;
                float * pOut = (outputData->data_float + (row*outputData->width + col)*outputData->floatChannelStepInByte / sizeof(float));
                pOut[ch] = 0; //the new created blob is not zeros, clear it first

                {
                    if (srcy >= 0)
                    {
                        pOut[ch] += dotProductFloatChGeneral(pIn,
                            pF,
                            num_pixels_infloat,
                            num_pixels_infloat * sizeof(float));
                    }
                }
                {
                    srcy++;
                    {
                        pIn += (inputData->width * elementStepInFloat);
                        pOut[ch] += dotProductFloatChGeneral(pIn,
                            pF + ( 3 * elementStepInFloat),
                            num_pixels_infloat,
                            num_pixels_infloat * sizeof(float));
                    }
                }
                {
                    srcy++;
                    if (srcy < inputData->height)
                    {
                        pIn += (inputData->width * elementStepInFloat);
                        pOut[ch] += dotProductFloatChGeneral(pIn,
                            pF + ( 6 * elementStepInFloat ),
                            num_pixels_infloat,
                            num_pixels_infloat * sizeof(float));
                    }
                }
            }
        }
    }
    return true;
}

bool convolutionInt83x3P1ChGeneral(const CDataBlob *inputData, const Filters* filters, CDataBlob *outputData) 
{ 
#if defined(_OPENMP)
#pragma omp parallel for
#endif
    for (int row = 0; row < outputData->height; row++) 
    {  
        int elementStep = inputData->int8ChannelStepInByte;
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

                signed char * pIn = (inputData->data_int8 + (srcy *inputData->width + srcx_start)*elementStep);
                signed char * pF = (filters->filters[ch]->data_int8) + ( (srcx_start - col*stride + 1)) * elementStep;
                float * pOut = (outputData->data_float + (row*outputData->width + col)*outputData->floatChannelStepInByte / sizeof(float));
                pOut[ch] = 0;//the new created blob is not zeros, clear it first

                {
                    if (srcy >= 0)
                    {
                        pOut[ch] += dotProductInt8ChGeneral(pIn,
                            pF,
                            num_pixels_inbytes,
                            num_pixels_inbytes);
                    }
                }
                {
                    srcy++;
                    {
                        pIn += (inputData->width * elementStep);
                        pOut[ch] += dotProductInt8ChGeneral(pIn,
                            pF + (3 * elementStep),
                            num_pixels_inbytes,
                            num_pixels_inbytes);
                    }
                }
                {
                    srcy++;
                    if (srcy < inputData->height)
                    {
                        pIn += (inputData->width * elementStep);
                        pOut[ch] += dotProductInt8ChGeneral(pIn,
                            pF + (6 * elementStep),
                            num_pixels_inbytes,
                            num_pixels_inbytes);
                    }
                }
            }
        }
    }
    return true; 
}

bool convertFloat2Int8(CDataBlob * dataBlob)
{
    if (dataBlob->data_float == NULL || dataBlob->data_int8 == NULL)
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return false;
    }

    float maxval = -FLT_MAX;
#if defined(_ENABLE_NEON)
    float32x4_t maxvalvec = vdupq_n_f32(-FLT_MAX);
    float32x4_t scalevec;
#elif defined(_ENABLE_AVX2)
    //__m256 maxvalvec = _mm256_set1_ps(-FLT_MAX);
    __m256 scalevec;
#endif

    float scale = 1.f;

    if (dataBlob->int8_data_valid)
        return true;

    for (int row = 0; row < dataBlob->height; row++)
    {
        for (int col = 0; col < dataBlob->width; col++)
        {
            float * pF = (dataBlob->data_float + (row*dataBlob->width + col)*dataBlob->floatChannelStepInByte / sizeof(float));

#if defined(_ENABLE_NEON)
            for (int ch = 0; ch < dataBlob->channels; ch+=4)
            {
                float32x4_t a;
                a = vld1q_f32(pF + ch);
                a = vabsq_f32(a);
                maxvalvec = vmaxq_f32(maxvalvec, a);
            }
#else

#if defined(_ENABLE_OPENMP_SIMD)
#pragma omp simd reduction(max:maxval)
#endif
            for (int ch = 0; ch < dataBlob->channels; ch++)
            {
                float tmp;
                //tmp = fabs(pF[ch]);
                //maxval = MAX(maxval, tmp);
                tmp = pF[ch];
                tmp = tmp * ((tmp > 0) * 2 - 1);
                maxval = MAX(maxval, tmp);
            }
#endif
        }
    }
#if defined(_ENABLE_NEON)
    {
        float tmp;
        tmp = vgetq_lane_f32(maxvalvec, 0);
        maxval = MAX(maxval, tmp);
        tmp = vgetq_lane_f32(maxvalvec, 1);
        maxval = MAX(maxval, tmp);
        tmp = vgetq_lane_f32(maxvalvec, 2);
        maxval = MAX(maxval, tmp);
        tmp = vgetq_lane_f32(maxvalvec, 3);
        maxval = MAX(maxval, tmp);
    }
#endif
    scale = 127.f / (maxval + FLT_EPSILON);

#if defined(_ENABLE_NEON)
    scalevec = vdupq_n_f32(scale);
#elif defined(_ENABLE_AVX2)
    scalevec = _mm256_set1_ps(scale);
#endif

#if defined(_OPENMP)
#pragma omp parallel for
#endif
    for (int row = 0; row < dataBlob->height; row++)
    {
        for (int col = 0; col < dataBlob->width; col++)
        {
            float * pF = (dataBlob->data_float + (row*dataBlob->width + col)*dataBlob->floatChannelStepInByte / sizeof(float));
            signed char * pI = (dataBlob->data_int8 + (row*dataBlob->width + col)*dataBlob->int8ChannelStepInByte / sizeof(char));

#if defined(_ENABLE_NEON)
            for (int ch = 0; ch < dataBlob->channels; ch+=4)
            {
                float tmp;
                float32x4_t a = vld1q_f32(pF + ch);
                float32x4_t resultvec = vmulq_f32(a, scalevec);
                
                ////becuase Floating-point to integer conversions "vcvtq_s32_f32" use round towards zero.
                ////but we need round to nearest
                ////so we cannot use the following NEON instructions
                //int32x4_t int32resultvec = vcvtq_s32_f32(resultvec);
                //int16x4_t int16resultvec = vqmovn_s32(int32resultvec);
                //vst1_s16(pI + ch, int16resultvec);
                
                tmp = vgetq_lane_f32(resultvec, 0);
                pI[ch] = (signed char)(tmp + ((tmp>0) - 0.5f));
                tmp = vgetq_lane_f32(resultvec, 1);
                pI[ch+1] = (signed char)(tmp + ((tmp>0) - 0.5f));
                tmp = vgetq_lane_f32(resultvec, 2);
                pI[ch+2] = (signed char)(tmp + ((tmp>0) - 0.5f));
                tmp = vgetq_lane_f32(resultvec, 3);
                pI[ch+3] = (signed char)(tmp + ((tmp>0) - 0.5f));
            }
#else
#if defined(_ENABLE_OPENMP_SIMD)
#pragma omp simd
#endif
            for (int ch = 0; ch < dataBlob->channels; ch++)
            {
                float tmp;
                //pI[ch] = (signed char)round(pF[ch] * scale);
                //to speedup round() using the following code
                tmp = pF[ch];
                pI[ch] = (signed char)(tmp * scale + ((tmp>0)-0.5f));
            }
#endif
        }
    }
    dataBlob->int8float_scale = scale;
    dataBlob->int8_data_valid = true;

    return true;
}


bool convolution(CDataBlob *inputData, const Filters* filters, CDataBlob *outputData)
{
    if (inputData->data_float == NULL || inputData->data_int8 == NULL)
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

    /*
    {
        float maxval = -FLT_MAX;
        float minval = FLT_MAX;

        for (int row = 0; row < inputData->height; row++)
        {
            for (int col = 0; col < inputData->width; col++)
            {
                float * pF = (inputData->data_float + (row*inputData->width + col)*inputData->floatChannelStepInByte / sizeof(float));
                for (int ch = 0; ch < inputData->channels; ch++)
                {
                    maxval = MAX(maxval, pF[ch]);
                    minval = MIN(minval, pF[ch]);
                }
            }
        }
        cout << "\t\t\t\tconv range [min, max]=[" << minval << ",   " << maxval << "]" << endl;
    }
    */
#if defined(_ENABLE_INT8_CONV)
    convertFloat2Int8(inputData);
#endif

    if (filterW == 1 && filterH == 1) //1x1 filters
    {
#if defined(_ENABLE_INT8_CONV)
        convolutionInt81x1P0S1(inputData, filters, outputData);
#else
        convolutionFloat1x1P0S1(inputData, filters, outputData);
#endif
    }
    else if (filterW == 3 && filterH == 3) //3x3 filters
    {
#if defined(_ENABLE_INT8_CONV)
        convolutionInt83x3P1ChGeneral(inputData, filters, outputData);
#else
        convolutionFloat3x3P1ChGeneral(inputData, filters, outputData);
#endif
    }

#if defined(_ENABLE_INT8_CONV)
    scale(outputData, 1.0f / (inputData->int8float_scale * filters->scale));
#endif

	return true;
}

//only 2X2 S2 is supported
bool maxpooling2x2S2(const CDataBlob *inputData, CDataBlob *outputData)
{
    if (inputData->data_float == NULL)
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

    int elementStep = inputData->floatChannelStepInByte / sizeof(float);
    int lineElementStep = inputData->width * elementStep;

    outputData->create(outputW, outputH, outputC);

    for (int row = 0; row < outputData->height; row++)
    {
        for (int col = 0; col < outputData->width; col++)
        {
            int inputMatOffsetsInElement[4];
            int elementCount = 0;

            int hstart = row * 2;
            int wstart = col * 2;
            int hend = MIN(hstart + 2, inputData->height);
            int wend = MIN(wstart + 2, inputData->width);

            for (int fy = hstart; fy < hend; fy++)
                for (int fx = wstart; fx < wend; fx++)
                {
                    inputMatOffsetsInElement[elementCount++] = (fy *inputData->width + fx) * inputData->floatChannelStepInByte / sizeof(float);
                }

            float * pOut = outputData->data_float + (row*outputData->width + col) * outputData->floatChannelStepInByte / sizeof(float);
            float * pIn = inputData->data_float;

#if defined(_ENABLE_NEON)
            for (int ch = 0; ch < outputData->channels; ch += 4)
            {
                float32x4_t a;
                float32x4_t maxval = vld1q_f32(pIn + ch + inputMatOffsetsInElement[0]);
                for (int el = 1; el < elementCount; el++)
                {
                    a = vld1q_f32(pIn + ch + inputMatOffsetsInElement[el]);
                    maxval = vmaxq_f32(maxval, a);
                }
                vst1q_f32(pOut + ch, maxval);
            }
#elif defined(_ENABLE_AVX2)
            for (int ch = 0; ch < outputData->channels; ch += 8)
            {
                __m256 a;
                __m256 maxval = _mm256_load_ps(pIn + ch + inputMatOffsetsInElement[0]);
                for (int el = 1; el < elementCount; el++)
                {
                    a = _mm256_load_ps(pIn + ch + inputMatOffsetsInElement[el]);
                    maxval = _mm256_max_ps(maxval, a);
                }
                _mm256_store_ps(pOut + ch, maxval);
            }
#else

            for (int ch = 0; ch < outputData->channels; ch++)
            {
                float maxval = pIn[ch + inputMatOffsetsInElement[0]];
#if defined(_ENABLE_OPENMP_SIMD)
#pragma omp simd reduction(max:maxval)
#endif
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



bool concat4(const CDataBlob *inputData1, const CDataBlob *inputData2, const CDataBlob *inputData3, const CDataBlob *inputData4, CDataBlob *outputData)
{
    if ((inputData1->data_float == NULL) || (inputData2->data_float == NULL) || (inputData3->data_float == NULL) || (inputData4->data_float == NULL))
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
            float * pOut = (outputData->data_float + (row*outputData->width + col)*outputData->floatChannelStepInByte / sizeof(float));
            float * pIn1 = (inputData1->data_float + (row*inputData1->width + col)*inputData1->floatChannelStepInByte / sizeof(float));
            float * pIn2 = (inputData2->data_float + (row*inputData2->width + col)*inputData2->floatChannelStepInByte / sizeof(float));
            float * pIn3 = (inputData3->data_float + (row*inputData3->width + col)*inputData3->floatChannelStepInByte / sizeof(float));
            float * pIn4 = (inputData4->data_float + (row*inputData4->width + col)*inputData4->floatChannelStepInByte / sizeof(float));

            memcpy(pOut, pIn1, sizeof(float)* inputData1->channels);
            memcpy(pOut + inputData1->channels, pIn2, sizeof(float)* inputData2->channels);
            memcpy(pOut + inputData1->channels + inputData2->channels, pIn3, sizeof(float)* inputData3->channels);
            memcpy(pOut + inputData1->channels + inputData2->channels + inputData3->channels, pIn4, sizeof(float)* inputData4->channels);
        }
    }
    return true;
}

bool scale(CDataBlob * dataBlob, float scale)
{
    if (dataBlob->data_float == NULL || dataBlob->data_int8 == NULL)
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return false;
    }

    for (int row = 0; row < dataBlob->height; row++)
    {
        for (int col = 0; col < dataBlob->width; col++)
        {
            float * pF = (dataBlob->data_float + (row*dataBlob->width + col)*dataBlob->floatChannelStepInByte / sizeof(float));
#if defined(_ENABLE_NEON)
            float32x4_t a, bscale;
            float32x4_t result_vec;

            bscale = vdupq_n_f32(scale);
            for (int ch = 0; ch < dataBlob->channels; ch+=4)
            {
                a = vld1q_f32(pF + ch);
                result_vec = vmulq_f32(a, bscale);
                vst1q_f32(pF + ch, result_vec);
            }
#elif defined(_ENABLE_AVX2)
            __m256 a, bscale;

            bscale = _mm256_set1_ps(scale);
            for (int ch = 0; ch < dataBlob->channels; ch += 8)
            {
                a = _mm256_load_ps(pF + ch);
                a = _mm256_mul_ps(a, bscale);
                _mm256_store_ps(pF + ch, a);
            }

#else
#if defined(_ENABLE_OPENMP_SIMD)
#pragma omp simd
#endif
            for (int ch = 0; ch < dataBlob->channels; ch++)
            {
                pF[ch] *= scale;
            }
#endif
        }
    }
    return true;
}

bool relu(const CDataBlob *inputOutputData)
{
    if (inputOutputData->data_float == NULL)
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return false;
    }


    for (int row = 0; row < inputOutputData->height; row++)
    {
        for (int col = 0; col < inputOutputData->width; col++)
        {
            float * pData = (float*)(inputOutputData->data_float + (row*inputOutputData->width + col)*inputOutputData->floatChannelStepInByte / sizeof(float));

#if defined(_ENABLE_NEON)
            float32x4_t a, bzeros;
            float32x4_t result_vec;

            bzeros = vdupq_n_f32(0); //zeros
            for (int ch = 0; ch < inputOutputData->channels; ch += 4)
            {
                a = vld1q_f32(pData + ch);
                result_vec = vmaxq_f32(a, bzeros);
                vst1q_f32(pData + ch, result_vec);
            }
#elif defined(_ENABLE_AVX2)
            __m256 a, bzeros;

            bzeros = _mm256_setzero_ps(); //zeros
            for (int ch = 0; ch < inputOutputData->channels; ch += 8)
            {
                a = _mm256_load_ps(pData + ch);
                a = _mm256_max_ps(a, bzeros);
                _mm256_store_ps(pData + ch, a);
            }
#else
#if defined(_ENABLE_OPENMP_SIMD)
#pragma omp simd
#endif
            for (int ch = 0; ch < inputOutputData->channels; ch++)
                pData[ch] = MAX(pData[ch], 0);
#endif
        }
    }
    return true;
}

bool priorbox(const CDataBlob * featureData, const CDataBlob * imageData, int num_sizes, float * pWinSizes, CDataBlob * outputData)
{
    if ((featureData->data_float == NULL) ||
        imageData->data_float == NULL||
        pWinSizes == NULL)
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return false;
    }

    int feature_width = featureData->width;
    int feature_height = featureData->height;
    int image_width = imageData->width * 2;
    int image_height = imageData->height * 2;

	float step_w = static_cast<float>(image_width) / feature_width;
	float step_h = static_cast<float>(image_height) / feature_height;

	float * output_data = outputData->data_float;

//    outputData->create(feature_width, feature_height, num_sizes * 4 * 2);
    outputData->create(feature_width, feature_height, num_sizes * 4);

	for (int h = 0; h < feature_height; ++h) 
	{
		for (int w = 0; w < feature_width; ++w) 
		{
            float * pOut = (float*)(outputData->data_float + ( h * outputData->width + w) * outputData->floatChannelStepInByte / sizeof(float));
            int idx = 0;
            //priorbox
			for (int s = 0; s < num_sizes; s++) 
			{
				float min_size_ = pWinSizes[s];
                float box_width, box_height;
                box_width = box_height = min_size_;
                
                float center_x = w * step_w + step_w / 2.0f;
                float center_y = h * step_h + step_h / 2.0f;
                // xmin
                pOut[idx++] = (center_x - box_width / 2.f) / image_width;
                // ymin
                pOut[idx++] = (center_y - box_height / 2.f) / image_height;
                // xmax
                pOut[idx++] = (center_x + box_width / 2.f) / image_width;
                // ymax
                pOut[idx++] = (center_y + box_height / 2.f) / image_height;

			}
		}
	}
    
    
    return true;
}

bool normalize(CDataBlob * inputOutputData, float * pScale)
{
    if ((inputOutputData->data_float == NULL) || pScale == NULL)
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return false;
    }


    for (int row = 0; row < inputOutputData->height; row++)
    {
        for (int col = 0; col < inputOutputData->width; col++)
        {
            float * pData = (float*)(inputOutputData->data_float + (row*inputOutputData->width + col)*inputOutputData->floatChannelStepInByte / sizeof(float));
            float sum = FLT_EPSILON;
            float s = 0;
#if defined(_ENABLE_NEON)
            float32x4_t a, b, cscale;
            float32x4_t result_vec;
            for (int ch = 0; ch < inputOutputData->channels; ch += 4)
            {
                a = vld1q_f32(pData + ch);
                result_vec = vmulq_f32(a, a);
                sum += vgetq_lane_f32(result_vec, 0);
                sum += vgetq_lane_f32(result_vec, 1);
                sum += vgetq_lane_f32(result_vec, 2);
                sum += vgetq_lane_f32(result_vec, 3);
            }

            s = 1.0f/sqrt(sum);
            cscale = vdupq_n_f32(s);

            for (int ch = 0; ch < inputOutputData->channels; ch += 4)
            {
                a = vld1q_f32(pData + ch);
                b = vld1q_f32(pScale + ch);

                result_vec = vmulq_f32(a, b);
                result_vec = vmulq_f32(result_vec, cscale);
                vst1q_f32(pData + ch, result_vec);
            }
#elif defined(_ENABLE_AVX2)
            __m256 a, b, cscale;
            __m256 result_vec;
            for (int ch = 0; ch < inputOutputData->channels; ch += 8)
            {
                a = _mm256_load_ps(pData + ch);
                a = _mm256_mul_ps(a, a);
                a = _mm256_hadd_ps(a, a);
                a = _mm256_hadd_ps(a, a);
                sum += SSE_256ELEMENT(a, 0);
                sum += SSE_256ELEMENT(a, 4);
            }

            s = 1.0f / sqrt(sum);
            cscale = _mm256_set1_ps(s);

            for (int ch = 0; ch < inputOutputData->channels; ch += 8)
            {
                a = _mm256_load_ps(pData + ch);
                b = _mm256_load_ps(pScale + ch);

                result_vec = _mm256_mul_ps(a, b);
                result_vec = _mm256_mul_ps(result_vec, cscale);
                _mm256_store_ps(pData + ch, result_vec);
            }
#else

#if defined(_ENABLE_OPENMP_SIMD)
#pragma omp simd reduction(+:sum)
#endif
            for (int ch = 0; ch < inputOutputData->channels; ch++)
                sum += (pData[ch] * pData[ch]);

            s = 1.0f/sqrt(sum);
#if defined(_ENABLE_OPENMP_SIMD)
#pragma omp simd
#endif
            for (int ch = 0; ch < inputOutputData->channels; ch++)
                pData[ch] = pData[ch] * pScale[ch] * s;
#endif            
        }
    }
    return true;

}

bool softmax1vector2class(const CDataBlob *inputOutputData)
{
    if (inputOutputData->data_float == NULL)
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
    float * pData = (inputOutputData->data_float);

#if defined(_OPENMP)
#pragma omp parallel for
#endif
    for(int i = 0; i < num; i+= 2)
    {
        float v1 = pData[i];
        float v2 = pData[i+1];
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

bool blob2vector(const CDataBlob * inputData, CDataBlob * outputData, bool isFloat)
{
    if (inputData->data_float == NULL)
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return false;
    }

    outputData->create(1, 1, inputData->width * inputData->height * inputData->channels);

    if (isFloat)
    {
        int bytesOfAChannel = inputData->channels * sizeof(float);
        float * pOut = outputData->data_float;
        for (int row = 0; row < inputData->height; row++)
        {
            for (int col = 0; col < inputData->width; col++)
            {
                float * pIn = (inputData->data_float + (row*inputData->width + col)*inputData->floatChannelStepInByte / sizeof(float));
                memcpy(pOut, pIn, bytesOfAChannel);
                pOut += inputData->channels;
            }
        }
    }
    else
    {
        int bytesOfAChannel = inputData->channels * sizeof(char);
        signed char * pOut = outputData->data_int8;
        for (int row = 0; row < inputData->height; row++)
        {
            for (int col = 0; col < inputData->width; col++)
            {
                signed char * pIn = (inputData->data_int8 + (row*inputData->width + col)*inputData->int8ChannelStepInByte / sizeof(char));
                memcpy(pOut, pIn, bytesOfAChannel);
                pOut += inputData->channels;
            }
        }
    }

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

bool SortScoreBBoxPairDescend(const pair<float, NormalizedBBox>& pair1,   const pair<float, NormalizedBBox>& pair2) 
{
    return pair1.first > pair2.first;
}


bool detection_output(const CDataBlob * priorbox, const CDataBlob * loc, const CDataBlob * conf, float overlap_threshold, float confidence_threshold, int top_k, int keep_top_k, CDataBlob * outputData)
{
    if (priorbox->data_float == NULL || loc->data_float == NULL || conf->data_float == NULL)
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return 0;
    }

    if (priorbox->channels != loc->channels || loc->channels != conf->channels*2 )
    {
        cerr << __FUNCTION__ << ": The sizes of the inputs are not match." << endl;
        return 0;
    }

    float prior_variance[4] = {0.1f, 0.1f, 0.2f, 0.2f};
    float * pPriorBox = priorbox->data_float;
    float * pLoc = loc->data_float;
    float * pConf = conf->data_float;

    vector<pair<float, NormalizedBBox> > score_bbox_vec;
    vector<pair<float, NormalizedBBox> > final_score_bbox_vec;

    //get the candidates those are > confidence_threshold
    for(int i = 1; i < conf->channels; i+=2)
    {
        if(pConf[i] > confidence_threshold)
        {
            float fx1 = pPriorBox[i*2-2];
            float fy1 = pPriorBox[i*2-1];
            float fx2 = pPriorBox[i*2];
            float fy2 = pPriorBox[i*2+1];

            float locx1 = pLoc[i * 2 - 2];
            float locy1 = pLoc[i * 2 - 1];
            float locx2 = pLoc[i * 2];
            float locy2 = pLoc[i * 2 + 1];

            float prior_width = fx2 - fx1;
            float prior_height = fy2 - fy1;
            float prior_center_x = (fx1 + fx2)/2;
            float prior_center_y = (fy1 + fy2)/2;

            float box_centerx = prior_variance[0] * locx1 * prior_width + prior_center_x;
            float box_centery = prior_variance[1] * locy1 * prior_height + prior_center_y;
            float box_width = expf(prior_variance[2] * locx2) * prior_width;
            float box_height = expf(prior_variance[3] * locy2) * prior_height;

            fx1 = box_centerx - box_width / 2.f;
            fy1 = box_centery - box_height /2.f;
            fx2 = box_centerx + box_width / 2.f;
            fy2 = box_centery + box_height /2.f;

            fx1 = MAX(0, fx1);
            fy1 = MAX(0, fy1);
            fx2 = MIN(1.f, fx2);
            fy2 = MIN(1.f, fy2);

            NormalizedBBox bb;
            bb.xmin = fx1;
            bb.ymin = fy1;
            bb.xmax = fx2;
            bb.ymax = fy2;

            score_bbox_vec.push_back(std::make_pair(pConf[i], bb));
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
        outputData->create(num_faces, 1, 5);
        for (int fi = 0; fi < num_faces; fi++)
        {
            pair<float, NormalizedBBox> pp = final_score_bbox_vec[fi];
            float * pOut = (outputData->data_float + fi * outputData->floatChannelStepInByte / sizeof(float));
            pOut[0] = pp.first;
            pOut[1] = pp.second.xmin;
            pOut[2] = pp.second.ymin;
            pOut[3] = pp.second.xmax;
            pOut[4] = pp.second.ymax;
        }
    }

    return true;
}

