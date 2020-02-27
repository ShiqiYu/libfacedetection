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
#include <stdio.h>
#include <string.h>

#if 0
#include <opencv2/opencv.hpp>
cv::TickMeter cvtm;
#define TIME_START cvtm.reset();cvtm.start();
#define TIME_END(FUNCNAME) cvtm.stop(); printf(FUNCNAME);printf("=%g\n", cvtm.getTimeMilli());
#else
#define TIME_START
#define TIME_END(FUNCNAME)
#endif


#define NUM_CONV_LAYER 24

extern ConvInfoStruct param_pConvInfo[NUM_CONV_LAYER];
Filters g_pFilters[NUM_CONV_LAYER]; //NUM_CONV_LAYER conv layers

bool param_initialized = false;

void init_parameters()
{
    //set filters 0
    {
        int i = 0;
        g_pFilters[i].stride = 1;// param_pConvInfo[i].stride;
        g_pFilters[i].pad = 0;// param_pConvInfo[i].pad;
        g_pFilters[i].scale = param_pConvInfo[i].scale;
        int offset = param_pConvInfo[i].kernel_size * param_pConvInfo[i].kernel_size * param_pConvInfo[i].channels;
        
        for(int ff = 0; ff < param_pConvInfo[i].num; ff++)
        {
            CDataBlob<signed char> * b3x3 = new CDataBlob<signed char>(param_pConvInfo[i].kernel_size, param_pConvInfo[i].kernel_size, param_pConvInfo[i].channels);
            CDataBlob<signed char> * b1x1 = new CDataBlob<signed char>();
            b3x3->setInt8FilterData(param_pConvInfo[i].pWeights + size_t(ff) * offset, param_pConvInfo[i].pBias[ff],
                                            param_pConvInfo[i].kernel_size, param_pConvInfo[i].kernel_size, param_pConvInfo[i].channels);
            blob2vector<signed char>(b3x3, b1x1);
            delete b3x3;
            b3x3 = 0;
            g_pFilters[i].filters.push_back(b1x1);            
        }
    }
    //set the rest
    for(int i = 1; i < NUM_CONV_LAYER; i++)
    {
        g_pFilters[i].stride = param_pConvInfo[i].stride;
        g_pFilters[i].pad = param_pConvInfo[i].pad;
        g_pFilters[i].scale = param_pConvInfo[i].scale;
        int offset = param_pConvInfo[i].kernel_size * param_pConvInfo[i].kernel_size * param_pConvInfo[i].channels;
        
        for(int ff = 0; ff < param_pConvInfo[i].num; ff++)
        {
            CDataBlob<signed char> * b = new CDataBlob<signed char>(param_pConvInfo[i].kernel_size, param_pConvInfo[i].kernel_size, param_pConvInfo[i].channels);
            b->setInt8FilterData(param_pConvInfo[i].pWeights + size_t(ff) * offset, param_pConvInfo[i].pBias[ff],
                                            param_pConvInfo[i].kernel_size, param_pConvInfo[i].kernel_size, param_pConvInfo[i].channels);
            g_pFilters[i].filters.push_back(b);
        }
    }
}

vector<FaceRect> objectdetect_cnn(unsigned char * rgbImageData, int width, int height, int step)
{
    CDataBlob<unsigned char> inputImage;
    CDataBlob<unsigned char> pConvDataBlobs[NUM_CONV_LAYER-8];
    CDataBlob<int> pConvDataBlobsBranch[8];
    CDataBlob<unsigned char> pool1, pool2, pool3, pool4, pool5;
    CDataBlob<float> conv3priorbox, conv4priorbox, conv5priorbox, conv6priorbox;
    CDataBlob<float> conv3priorbox_flat, conv4priorbox_flat, conv5priorbox_flat, conv6priorbox_flat, mbox_priorbox;
    CDataBlob<int> conv3loc_flat, conv4loc_flat, conv5loc_flat, conv6loc_flat;
    CDataBlob<float> conv3loc_flat_float, conv4loc_flat_float, conv5loc_flat_float, conv6loc_flat_float;
    CDataBlob<float> mbox_loc_float;        ;
    CDataBlob<int> conv3conf_flat, conv4conf_flat, conv5conf_flat, conv6conf_flat;
    CDataBlob<float> conv3conf_flat_float, conv4conf_flat_float, conv5conf_flat_float, conv6conf_flat_float;
    CDataBlob<float> mbox_conf_float;

    TIME_START;
    if (!param_initialized)
    {
        init_parameters();
        param_initialized = true;
    }
    TIME_END("init");

 
    TIME_START;
    inputImage.setDataFrom3x3S2P1to1x1S1P0FromImage(rgbImageData, width, height, 3, step);
    TIME_END("convert data");


/***************CONV1*********************/
    int convidx = 0;
    TIME_START;
    convolution_relu(&inputImage, g_pFilters + convidx, pConvDataBlobs + convidx);
    TIME_END("conv11");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv12");

    TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool1);
    TIME_END("pool1");

/***************CONV2*********************/
    convidx++;
    TIME_START;
    convolution_relu(&pool1, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv21");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv22");

    TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool2);
    TIME_END("pool2");

/***************CONV3*********************/
    convidx++;
    TIME_START;
    convolution_relu(&pool2, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv31");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv32");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv33");

    TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool3);
    TIME_END("pool3");

/***************CONV4*********************/
    convidx++;
    TIME_START;
    convolution_relu(&pool3, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv41");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv42");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv43");

    TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool4);
    TIME_END("pool4");

/***************CONV5*********************/
    convidx++;
    TIME_START;
    convolution_relu(&pool4, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv51");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv52");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv53");

    TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool5);
    TIME_END("pool5");

/***************CONV6*********************/
    convidx++;
    TIME_START;
    convolution_relu(&pool5, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv61");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv62");


    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, g_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv63");


    /***************PRIORBOX3*********************/
    int conv3idx = 6;

    convidx++;
    TIME_START
    convolution(pConvDataBlobs+ conv3idx, g_pFilters+convidx, pConvDataBlobsBranch + 0);
    TIME_END("prior3 loc");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs+ conv3idx, g_pFilters+convidx, pConvDataBlobsBranch + 1);
    TIME_END("prior3 conf");

    TIME_START;
    float pSizes3[3] = {10, 16, 24};
    priorbox(pConvDataBlobs+ conv3idx, width, height, 8, 3, pSizes3, &conv3priorbox);
    TIME_END("prior3");

    /***************PRIORBOX4*********************/
    int conv4idx = 9;

    convidx++;
    TIME_START
    convolution(pConvDataBlobs + conv4idx, g_pFilters + convidx, pConvDataBlobsBranch + 2);
    TIME_END("prior4 loc");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs + conv4idx, g_pFilters + convidx, pConvDataBlobsBranch + 3);
    TIME_END("prior4 conf");

    TIME_START;
    float pSizes4[2] = { 32, 48};
    priorbox(pConvDataBlobs + conv4idx, width, height, 16, 2, pSizes4, &conv4priorbox);
    TIME_END("prior4");

    /***************PRIORBOX5*********************/
    int conv5idx = 12;

    convidx++;
    TIME_START
    convolution(pConvDataBlobs + conv5idx, g_pFilters + convidx, pConvDataBlobsBranch + 4);
    TIME_END("prior5 loc");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs + conv5idx, g_pFilters + convidx, pConvDataBlobsBranch + 5);
    TIME_END("prior5 conf");

    TIME_START;
    float pSizes5[2] = { 64, 96 };
    priorbox(pConvDataBlobs + conv5idx, width, height, 32, 2, pSizes5, &conv5priorbox);
    TIME_END("prior5");

    /***************PRIORBOX6*********************/
    int conv6idx = 15;

    convidx++;
    TIME_START
    convolution(pConvDataBlobs + conv6idx, g_pFilters + convidx, pConvDataBlobsBranch + 6);
    TIME_END("prior6 loc");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs + conv6idx, g_pFilters + convidx, pConvDataBlobsBranch + 7);
    TIME_END("prior6 conf");

    TIME_START;
    float pSizes6[3] = { 128, 192, 256 };
    priorbox(pConvDataBlobs + conv6idx, width, height, 64, 3, pSizes6, &conv6priorbox);
    TIME_END("prior6");



    TIME_START;
    blob2vector(&conv3priorbox, &conv3priorbox_flat);
    blob2vector(pConvDataBlobsBranch + 0, &conv3loc_flat);
    blob2vector(pConvDataBlobsBranch + 1, &conv3conf_flat);

    blob2vector(&conv4priorbox, &conv4priorbox_flat);
    blob2vector(pConvDataBlobsBranch + 2, &conv4loc_flat);
    blob2vector(pConvDataBlobsBranch + 3, &conv4conf_flat);

    blob2vector(&conv5priorbox, &conv5priorbox_flat);
    blob2vector(pConvDataBlobsBranch + 4, &conv5loc_flat);
    blob2vector(pConvDataBlobsBranch + 5, &conv5conf_flat);

    blob2vector(&conv6priorbox, &conv6priorbox_flat);
    blob2vector(pConvDataBlobsBranch + 6, &conv6loc_flat);
    blob2vector(pConvDataBlobsBranch + 7, &conv6conf_flat);
    TIME_END("prior flat");

    TIME_START
    convertInt2Float(&conv3loc_flat, &conv3loc_flat_float);
    convertInt2Float(&conv4loc_flat, &conv4loc_flat_float);
    convertInt2Float(&conv5loc_flat, &conv5loc_flat_float);
    convertInt2Float(&conv6loc_flat, &conv6loc_flat_float);
    convertInt2Float(&conv3conf_flat, &conv3conf_flat_float);
    convertInt2Float(&conv4conf_flat, &conv4conf_flat_float);
    convertInt2Float(&conv5conf_flat, &conv5conf_flat_float);
    convertInt2Float(&conv6conf_flat, &conv6conf_flat_float);
    TIME_END("convert int to float");

    TIME_START
    concat4(&conv3priorbox_flat, &conv4priorbox_flat, &conv5priorbox_flat, &conv6priorbox_flat, &mbox_priorbox);
    concat4(&conv3loc_flat_float, &conv4loc_flat_float, &conv5loc_flat_float, &conv6loc_flat_float, &mbox_loc_float);
    concat4(&conv3conf_flat_float, &conv4conf_flat_float, &conv5conf_flat_float, &conv6conf_flat_float, &mbox_conf_float);
    TIME_END("concat prior")

    TIME_START
    softmax1vector2class(&mbox_conf_float);
    TIME_END("softmax")


    CDataBlob<float> facesInfo;
    TIME_START;
    detection_output(&mbox_priorbox, &mbox_loc_float, &mbox_conf_float, 0.3f, 0.5f, 1000, 100, &facesInfo);
    TIME_END("detection output")


    TIME_START;
    std::vector<FaceRect> faces;
    for (int i = 0; i < facesInfo.width; i++)
    {
        float score = facesInfo.getElement(i, 0, 0);
        float bbxmin = facesInfo.getElement(i, 0, 1);
        float bbymin = facesInfo.getElement(i, 0, 2);
        float bbxmax = facesInfo.getElement(i, 0, 3);
        float bbymax = facesInfo.getElement(i, 0, 4);
        FaceRect r;
        r.score = score;
        r.x = int(bbxmin * width + 0.5f);
        r.y = int(bbymin * height + 0.5f);
        r.w = int((bbxmax - bbxmin) * width + 0.5f);
        r.h = int((bbymax - bbymin) * height + 0.5f);

        //// convert the rectangle to a square
        //r.w = int( ((bbxmax - bbxmin) * width + (bbymax - bbymin) * height + 1) / 2);
        //r.h = r.w;
        //r.x = int(((bbxmin + bbxmax) * width - r.w + 0.5f) / 2);
        //r.y = int(((bbymin + bbymax) * height - r.h + 0.5f) / 2);

        for (int lmidx = 0; lmidx < 5; lmidx++)
        {
            r.lm[lmidx * 2] = int(facesInfo.getElement(i, 0, 5 + lmidx * 2) * width + 0.5f);
            r.lm[lmidx * 2 + 1] = int(facesInfo.getElement(i, 0, 5 + lmidx * 2 + 1) * height + 0.5f);
        }

        faces.push_back(r);
    }
    TIME_END("copy result");


    return faces;
}
int * facedetect_cnn(unsigned char * result_buffer, //buffer memory for storing face detection results, !!its size must be 0x20000 Bytes!!
    unsigned char * rgb_image_data, int width, int height, int step) //input image, it must be RGB (three-channel) image!
{

    if (!result_buffer)
    {
        fprintf(stderr, "%s: null buffer memory.\n", __FUNCTION__);
        return NULL;
    }
    //clear memory
    //memset(result_buffer, 0, 0x20000);
    result_buffer[0] = 0;
    result_buffer[1] = 0;
    result_buffer[2] = 0;
    result_buffer[3] = 0;

    vector<FaceRect> faces = objectdetect_cnn(rgb_image_data, width, height, step);

    int num_faces =(int)faces.size();
    num_faces = MIN(num_faces, 256);

    int * pCount = (int *)result_buffer;
    pCount[0] = num_faces;

    for (int i = 0; i < num_faces; i++)
    {
        //copy data
        short * p = ((short*)(result_buffer + 4)) + 142 * size_t(i);
        p[0] = (short)(faces[i].score * faces[i].score * 100);
        p[1] = (short)faces[i].x;
        p[2] = (short)faces[i].y;
        p[3] = (short)faces[i].w;
        p[4] = (short)faces[i].h;
        //copy landmarks
        for (int lmidx = 0; lmidx < 10; lmidx++)
        {
            p[5 + lmidx] = (short)faces[i].lm[lmidx];
        }
    }

    return pCount;
}
