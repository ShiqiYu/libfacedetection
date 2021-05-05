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

bool convolution_1x1pointwise( CDataBlob<float> & inputData,  Filters<float> & filters, CDataBlob<float> & outputData)
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

bool convolution_3x3depthwise(CDataBlob<float> & inputData,  Filters<float> & filters, CDataBlob<float> & outputData)
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
            int srcx_start = col - 1;
            int srcx_end = srcx_start + 3;
            srcx_start = MAX(0, srcx_start);
            srcx_end = MIN(srcx_end, inputData.cols);

            float * pOut = outputData.ptr(row, col);

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
        cerr << __FUNCTION__ << ": The input data is empty." << endl;
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

bool convolution(CDataBlob<float> & inputData, Filters<float> & filters, CDataBlob<float> & outputData, bool do_relu)
{
    if( inputData.isEmpty() || filters.weights.isEmpty() || filters.biases.isEmpty())
    {
        cerr << __FUNCTION__ << ": The input data or filter data is empty" << endl;
        return false;
    }
    if( inputData.channels != filters.channels)
    {
        cerr << __FUNCTION__ << ": The input data dimension cannot meet filters." << endl;
        return false;
    }

    outputData.create(inputData.rows, inputData.cols, filters.num_filters);

    if(filters.is_pointwise && !filters.is_depthwise)
        convolution_1x1pointwise(inputData, filters, outputData);
    else if(!filters.is_pointwise && filters.is_depthwise)
        convolution_3x3depthwise(inputData, filters, outputData);
    else
    {
        cerr << __FUNCTION__ << ": Unsupported filter type." << endl;
        return false;
    }

    if(do_relu)
        return relu(outputData);

    return true;
}

bool convolutionDP(CDataBlob<float> & inputData, 
                Filters<float> & filtersP, Filters<float> & filtersD, 
                CDataBlob<float> & outputData, bool do_relu)
{
    CDataBlob<float> tmp;
    bool r1 = convolution(inputData, filtersP, tmp, false);
    bool r2 = convolution(tmp, filtersD, outputData, do_relu);
    return r1 && r2;
}

bool convolution4layerUnit(CDataBlob<float> & inputData, 
                Filters<float> & filtersP1, Filters<float> & filtersD1, 
                Filters<float> & filtersP2, Filters<float> & filtersD2, 
                CDataBlob<float> & outputData, bool do_relu)
{
    CDataBlob<float> tmp;
    bool r1 = convolutionDP(inputData, filtersP1, filtersD1, tmp, true);
    bool r2 = convolutionDP(tmp, filtersP2, filtersD2, outputData, do_relu);
    return r1 && r2;
}


//only 2X2 S2 is supported
bool maxpooling2x2S2(CDataBlob<float> &inputData, CDataBlob<float> &outputData)
{
    if (inputData.isEmpty())
    {
        cerr << __FUNCTION__ << ": The input data is empty." << endl;
        return false;
    }
    int outputR = static_cast<int>(ceil((inputData.rows - 3.0f) / 2)) + 1;
    int outputC = static_cast<int>(ceil((inputData.cols - 3.0f) / 2)) + 1;
    int outputCH = inputData.channels;

    if (outputR < 1 || outputC < 1)
    {
        cerr << __FUNCTION__ << ": The size of the output is not correct. (" << outputR << ", " << outputC << ")." << endl;
        return false;
    }

    outputData.create(outputR, outputC, outputCH);

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
    return true;
}


template<typename T>
bool concat4(CDataBlob<T> &inputData1, CDataBlob<T> &inputData2, CDataBlob<T> &inputData3, CDataBlob<T> &inputData4, CDataBlob<T> &outputData)
{
    if ((inputData1.isEmpty()) || (inputData2.isEmpty()) || (inputData3.isEmpty()) || (inputData4.isEmpty()))
    {
        cerr << __FUNCTION__ << ": The input data is empty." << endl;
        return false;
    }

    if ((inputData1.cols != inputData2.cols) ||
        (inputData1.rows != inputData2.rows) ||
        (inputData1.cols != inputData3.cols) ||
        (inputData1.rows != inputData3.rows) ||
        (inputData1.cols != inputData4.cols) ||
        (inputData1.rows != inputData4.rows))
    {
        cerr << __FUNCTION__ << ": The three inputs must have the same size." << endl;
        return false;
    }
    int outputR = inputData1.rows;
    int outputC = inputData1.cols;
    int outputCH = inputData1.channels + inputData2.channels + inputData3.channels + inputData4.channels;

    if (outputR < 1 || outputC < 1 || outputCH < 1)
    {
        cerr << __FUNCTION__ << ": The size of the output is not correct. (" << outputR << ", " << outputC << ", " << outputCH << ")." << endl;
        return false;
    }

    outputData.create(outputR, outputC, outputCH);

    for (int row = 0; row < outputData.rows; row++)
    {
        for (int col = 0; col < outputData.cols; col++)
        {
            T * pOut = outputData.ptr(row, col);
            T * pIn1 = inputData1.ptr(row, col);
            T * pIn2 = inputData2.ptr(row, col);
            T * pIn3 = inputData3.ptr(row, col);
            T * pIn4 = inputData4.ptr(row, col);

            memcpy(pOut, pIn1, sizeof(T)* inputData1.channels);
            memcpy(pOut + inputData1.channels, pIn2, sizeof(T)* inputData2.channels);
            memcpy(pOut + inputData1.channels + inputData2.channels, pIn3, sizeof(T)* inputData3.channels);
            memcpy(pOut + inputData1.channels + inputData2.channels + inputData3.channels, pIn4, sizeof(T)* inputData4.channels);
        }
    }
    return true;
}
template bool concat4( CDataBlob<float> &inputData1, CDataBlob<float> &inputData2, CDataBlob<float> &inputData3, CDataBlob<float> &inputData4, CDataBlob<float> &outputData);

template<typename T>
bool extract(CDataBlob<T> &inputData, CDataBlob<T> &loc, CDataBlob<T> &conf, CDataBlob<T> &iou, int num_priors)
{
    if (inputData.isEmpty())
    {
        cerr << __FUNCTION__ << ": The input data is empty." << endl;
        return false;
    }

    int output_r = inputData.rows;
    int output_c = inputData.cols;
    int output_ch_iou = num_priors;
    int output_ch_loc = output_ch_iou * 14;
    int output_ch_conf = output_ch_iou * 2;
    // cout << inputData.rows << ", " << inputData.cols << ", " << inputData.channels << ", " << num_priors << endl;

    loc.create(output_r, output_c, output_ch_loc);
    conf.create(output_r, output_c, output_ch_conf);
    iou.create(output_r, output_c, output_ch_iou);

    for (int row = 0; row < output_r; row++)
    {
        for (int col = 0; col < output_c; col++)
        {
            T * p_in = inputData.ptr(row, col);
            T * p_loc = loc.ptr(row, col);
            T * p_conf = conf.ptr(row, col);
            T * p_iou = iou.ptr(row, col);

            for (int n = 0; n < num_priors; n++)
            {
                // box coords
                p_loc[n*14] = p_in[n*17];       p_loc[n*14+1] = p_in[n*17+1];
                p_loc[n*14+2] = p_in[n*17+2];   p_loc[n*14+3] = p_in[n*17+3];
                // landmark coords
                p_loc[n*14+4] = p_in[n*17+4];       p_loc[n*14+5] = p_in[n*17+5];
                p_loc[n*14+6] = p_in[n*17+6];       p_loc[n*14+7] = p_in[n*17+7];
                p_loc[n*14+8] = p_in[n*17+8];       p_loc[n*14+9] = p_in[n*17+9];
                p_loc[n*14+10] = p_in[n*17+10];     p_loc[n*14+11] = p_in[n*17+11];
                p_loc[n*14+12] = p_in[n*17+12];     p_loc[n*14+13] = p_in[n*17+13];
                // conf
                p_conf[n*2] = p_in[n*17+14];       p_conf[n*2+1] = p_in[n*17+15];
                // iou
                p_iou[n] = p_in[n*17+16];
            }
        }
    }
    return true;
}
template bool extract(CDataBlob<float> &inputData, CDataBlob<float> &loc, CDataBlob<float> &conf, CDataBlob<float> &iou, int num_priors);

bool priorbox( int feature_width, int feature_height, 
                int img_width, int img_height, 
                int step, int num_sizes, 
                float * pWinSizes, CDataBlob<float> & outputData)
{
    outputData.create(feature_height, feature_width, num_sizes * 4);

	for (int r = 0; r < outputData.rows; r++) 
	{
		for (int c = 0; c < outputData.cols; c++) 
		{
            float * pOut = outputData.ptr(r, c);
            int idx = 0;
            //priorbox
			for (int s = 0; s < num_sizes; s++) 
			{
				float min_size_ = pWinSizes[s];
                
                float center_x = (c + 0.5f) * step;
                float center_y = (r + 0.5f) * step;
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

bool softmax1vector2class(CDataBlob<float> &inputOutputData)
{
    if (inputOutputData.isEmpty() )
    {
        cerr << __FUNCTION__ << ": The input data is empty." << endl;
        return false;
    }

    if(inputOutputData.cols != 1 || inputOutputData.rows != 1)
    {
        cerr << __FUNCTION__ << ": The input data must be Cx1x1." << endl;
        return false;
    }

    int num = inputOutputData.channels;
    float * pData = inputOutputData.data;

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
bool clamp1vector(CDataBlob<float> &inputOutputData)
{
    if (inputOutputData.isEmpty() )
    {
        cerr << __FUNCTION__ << ": The input data is empty." << endl;
        return false;
    }

    if(inputOutputData.cols != 1 || inputOutputData.rows != 1)
    {
        cerr << __FUNCTION__ << ": The input data must be Cx1x1." << endl;
        return false;
    }

    int num = inputOutputData.channels;
    float * pData = inputOutputData.data;

    for (int i = 0; i < num; i++)
    {
        float& v = pData[i];
        if (v < 0) { v = 0.f; }
        else if (v > 1) { v = 1.f; }
        else { continue; }
    }
    return true;
}

template<typename T>
bool blob2vector(CDataBlob<T> &inputData, CDataBlob<T> & outputData)
{
    if (inputData.isEmpty())
    {
        cerr << __FUNCTION__ << ": The input data is empty." << endl;
        return false;
    }

    outputData.create(1, 1, inputData.cols * inputData.rows * inputData.channels);

    int bytesOfAChannel = inputData.channels * sizeof(T);
    T * pOut = outputData.ptr(0,0);
    for (int row = 0; row < inputData.rows; row++)
    {
        for (int col = 0; col < inputData.cols; col++)
        {
            T * pIn = inputData.ptr(row, col);
            memcpy(pOut, pIn, bytesOfAChannel);
            pOut += inputData.channels;
        }
    }

    return true;
}
template bool blob2vector(CDataBlob<float> &inputData, CDataBlob<float> &outputData);

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


bool detection_output(CDataBlob<float> & priorbox,
                      CDataBlob<float> & loc,
                      CDataBlob<float> & conf,
                      CDataBlob<float> & iou,
                      float overlap_threshold,
                      float confidence_threshold,
                      int top_k,
                      int keep_top_k,
                      CDataBlob<float> & outputData)
{
    if (priorbox.isEmpty() || loc.isEmpty() || conf.isEmpty() )//|| iou.isEmpty())
    {
        cerr << __FUNCTION__ << ": The input data is null." << endl;
        return 0;
    }

    if (priorbox.channels != conf.channels * 2 || loc.channels != conf.channels*7 )//|| conf.channels != iou.channels*2)
    {
        cerr << __FUNCTION__ << ": The sizes of the inputs are not match." << endl;
        cerr << "priorbox channels=" << priorbox.channels << ", loc channels=" << loc.channels << ", conf channels=" << conf.channels  << endl;
        return 0;
    }

    float prior_variance[4] = {0.1f, 0.1f, 0.2f, 0.2f};
    float * pPriorBox = priorbox.ptr(0,0);
    float * pLoc = loc.ptr(0,0);
    float * pConf = conf.ptr(0,0);
    float * pIoU = iou.ptr(0,0);

    vector<pair<float, NormalizedBBox> > score_bbox_vec;
    vector<pair<float, NormalizedBBox> > final_score_bbox_vec;

    //get the candidates those are > confidence_threshold
    for(int i = 0; i < conf.channels; i+=2)
    {
        float cls_score = pConf[i + 1];
        int face_idx = i / 2;
        float iou_score = pIoU[face_idx];
        // clamp
        if (iou_score < 0.f) {
            iou_score = 0.f;
        }
        else if (iou_score > 1.f) {
            iou_score = 1.f;
        }
        float conf = sqrtf(cls_score * iou_score);

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
    if (num_faces == 0)
        outputData.setNULL();
    else
    {
        outputData.create(1, num_faces, 15);
        for (int fi = 0; fi < num_faces; fi++)
        {
            pair<float, NormalizedBBox> pp = final_score_bbox_vec[fi];
            float * pOut = outputData.ptr(0, fi);
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

