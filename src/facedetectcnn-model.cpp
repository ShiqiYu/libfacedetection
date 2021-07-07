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


#define NUM_CONV_LAYER 43

extern ConvInfoStruct param_pConvInfo[NUM_CONV_LAYER];
Filters<float> g_pFilters[NUM_CONV_LAYER];

bool param_initialized = false;

void init_parameters()
{
    for(int i = 0; i < NUM_CONV_LAYER; i++)
        g_pFilters[i] = param_pConvInfo[i];
}

vector<FaceRect> objectdetect_cnn(unsigned char * rgbImageData, int width, int height, int step)
{
    CDataBlob<float> dataBlobs[18];
    CDataBlob<float> conv3priorbox, conv4priorbox, conv5priorbox, conv6priorbox;
    CDataBlob<float> conv3priorbox_flat, conv4priorbox_flat, conv5priorbox_flat, conv6priorbox_flat, mbox_priorbox;

    CDataBlob<float> conv3_loc, conv3_conf, conv3_iou;
    CDataBlob<float> conv3_loc_flat, conv3_conf_flat, conv3_iou_flat;

    CDataBlob<float> conv4_loc, conv4_conf, conv4_iou;
    CDataBlob<float> conv4_loc_flat, conv4_conf_flat, conv4_iou_flat;

    CDataBlob<float> conv5_loc, conv5_conf, conv5_iou;
    CDataBlob<float> conv5_loc_flat, conv5_conf_flat, conv5_iou_flat;

    CDataBlob<float> conv6_loc, conv6_conf, conv6_iou;
    CDataBlob<float> conv6_loc_flat, conv6_conf_flat, conv6_iou_flat;

    CDataBlob<float> mbox_loc, mbox_conf, mbox_iou;

    TIME_START;
    if (!param_initialized)
    {
        init_parameters();
        param_initialized = true;
    }
    TIME_END("init");

 
    TIME_START;
    dataBlobs[0].setDataFrom3x3S2P1to1x1S1P0FromImage(rgbImageData, width, height, 3, step);
    TIME_END("convert data");

    /***************CONV0*********************/
    TIME_START;
    convolution(dataBlobs[0], g_pFilters[0], dataBlobs[1]);
    TIME_END("conv_head");

    TIME_START;
    convolutionDP(dataBlobs[1], g_pFilters[1], g_pFilters[2], dataBlobs[2]);
    TIME_END("conv0");

    TIME_START;
    maxpooling2x2S2(dataBlobs[2], dataBlobs[3]);
    TIME_END("pool0");

    /***************CONV1*********************/
    TIME_START;
    convolution4layerUnit(dataBlobs[3], g_pFilters[3], g_pFilters[4], g_pFilters[5], g_pFilters[6], dataBlobs[4]);
    TIME_END("conv1");

    /***************CONV2*********************/
    TIME_START;
    convolution4layerUnit(dataBlobs[4], g_pFilters[7], g_pFilters[8], g_pFilters[9], g_pFilters[10], dataBlobs[5]);
    TIME_END("conv2");

    /***************CONV3*********************/
    TIME_START;
    maxpooling2x2S2(dataBlobs[5], dataBlobs[6]);
    TIME_END("pool3");
    TIME_START;
    convolution4layerUnit(dataBlobs[6], g_pFilters[11], g_pFilters[12], g_pFilters[13], g_pFilters[14], dataBlobs[7]);
    TIME_END("conv3");

    /***************CONV4*********************/
    TIME_START;
    maxpooling2x2S2(dataBlobs[7], dataBlobs[8]);
    TIME_END("pool4");
    TIME_START;
    convolution4layerUnit(dataBlobs[8], g_pFilters[15], g_pFilters[16], g_pFilters[17], g_pFilters[18], dataBlobs[9]);
    TIME_END("conv4");

    /***************CONV5*********************/
    TIME_START;
    maxpooling2x2S2(dataBlobs[9], dataBlobs[10]);
    TIME_END("pool5");
    TIME_START;
    convolution4layerUnit(dataBlobs[10], g_pFilters[19], g_pFilters[20], g_pFilters[21], g_pFilters[22], dataBlobs[11]);
    TIME_END("conv5");

    /***************CONV6*********************/
    TIME_START;
    maxpooling2x2S2(dataBlobs[11], dataBlobs[12]);
    TIME_END("pool6");
    TIME_START;
    convolution4layerUnit(dataBlobs[12], g_pFilters[23], g_pFilters[24], g_pFilters[25], g_pFilters[26], dataBlobs[13]);
    TIME_END("conv6");

    /***************branch3*********************/
    TIME_START;
    convolution4layerUnit(dataBlobs[7], g_pFilters[27], g_pFilters[28], g_pFilters[29], g_pFilters[30], dataBlobs[14], false);
    TIME_END("branch3");

    /***************branch4*********************/
    TIME_START;
    convolution4layerUnit(dataBlobs[9], g_pFilters[31], g_pFilters[32], g_pFilters[33], g_pFilters[34], dataBlobs[15], false);
    TIME_END("branch4");

    /***************branch5*********************/
    TIME_START;
    convolution4layerUnit(dataBlobs[11], g_pFilters[35], g_pFilters[36], g_pFilters[37], g_pFilters[38], dataBlobs[16], false);
    TIME_END("branch5");

    /***************branch6*********************/
    TIME_START;
    convolution4layerUnit(dataBlobs[13], g_pFilters[39], g_pFilters[40], g_pFilters[41], g_pFilters[42], dataBlobs[17], false);
    TIME_END("branch6");
    
    /***************PRIORBOX*********************/
    TIME_START;
    float pSizes3[3] = {10, 16, 24};
    priorbox(dataBlobs[7].cols, dataBlobs[7].rows, width, height, 8, 3, pSizes3, conv3priorbox);
    TIME_END("prior3");

    TIME_START;
    float pSizes4[2] = { 32, 48};
    priorbox(dataBlobs[9].cols, dataBlobs[9].rows, width, height, 16, 2, pSizes4, conv4priorbox);
    TIME_END("prior4");

    TIME_START;
    float pSizes5[2] = { 64, 96 };
    priorbox(dataBlobs[11].cols, dataBlobs[11].rows, width, height, 32, 2, pSizes5, conv5priorbox);
    TIME_END("prior5");

    TIME_START;
    float pSizes6[3] = { 128, 192, 256 };
    priorbox(dataBlobs[13].cols, dataBlobs[13].rows, width, height, 64, 3, pSizes6, conv6priorbox);
    TIME_END("prior6");

    /***************PRIORBOX*********************/
    TIME_START;
    blob2vector(conv3priorbox, conv3priorbox_flat);
    extract(dataBlobs[14], conv3_loc, conv3_conf, conv3_iou, 3);
    blob2vector(conv3_loc, conv3_loc_flat);
    blob2vector(conv3_conf, conv3_conf_flat);
    blob2vector(conv3_iou, conv3_iou_flat);

    blob2vector(conv4priorbox, conv4priorbox_flat);
    extract(dataBlobs[15], conv4_loc, conv4_conf, conv4_iou, 2);
    blob2vector(conv4_loc, conv4_loc_flat);
    blob2vector(conv4_conf, conv4_conf_flat);
    blob2vector(conv4_iou, conv4_iou_flat);

    blob2vector(conv5priorbox, conv5priorbox_flat);
    extract(dataBlobs[16], conv5_loc, conv5_conf, conv5_iou, 2);
    blob2vector(conv5_loc, conv5_loc_flat);
    blob2vector(conv5_conf, conv5_conf_flat);
    blob2vector(conv5_iou, conv5_iou_flat);

    blob2vector(conv6priorbox, conv6priorbox_flat);
    extract(dataBlobs[17], conv6_loc, conv6_conf, conv6_iou, 3);
    blob2vector(conv6_loc, conv6_loc_flat);
    blob2vector(conv6_conf, conv6_conf_flat);
    blob2vector(conv6_iou, conv6_iou_flat);
    TIME_END("prior flat");


    TIME_START
    concat4(conv3priorbox_flat, conv4priorbox_flat, conv5priorbox_flat, conv6priorbox_flat, mbox_priorbox);
    concat4(conv3_loc_flat, conv4_loc_flat, conv5_loc_flat, conv6_loc_flat, mbox_loc);
    concat4(conv3_conf_flat, conv4_conf_flat, conv5_conf_flat, conv6_conf_flat, mbox_conf);
    concat4(conv3_iou_flat, conv4_iou_flat, conv5_iou_flat, conv6_iou_flat, mbox_iou);
    TIME_END("concat prior")

    TIME_START
    softmax1vector2class(mbox_conf);
    clamp1vector(mbox_iou);
    TIME_END("softmax")

    CDataBlob<float> facesInfo;
    TIME_START;
    detection_output(mbox_priorbox, mbox_loc, mbox_conf, mbox_iou, 0.3f, 0.5f, 1000, 100, facesInfo);
    TIME_END("detection output")

    TIME_START;
    std::vector<FaceRect> faces;
    for (int i = 0; i < facesInfo.cols; i++)
    {
        float * pFaceData = facesInfo.ptr(0,i);
        float score = pFaceData[0];
        float bbxmin = pFaceData[1];
        float bbymin = pFaceData[2];
        float bbxmax = pFaceData[3];
        float bbymax = pFaceData[4];
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
            r.lm[lmidx * 2] = int(pFaceData[5 + lmidx * 2] * width + 0.5f);
            r.lm[lmidx * 2 + 1] = int(pFaceData[5 + lmidx * 2 + 1] * height + 0.5f);
        }

        faces.push_back(r);
    }
    TIME_END("copy result");

// int ii = 2;
// cv::Mat m1(dataBlobs[ii].rows, dataBlobs[ii].cols, CV_32FC1);
// for(int r=0; r < m1.rows; r++)
// {
//     float * p = (float*)m1.ptr(r);
//     for(int c=0; c < m1.cols; c++)
//         p[c]=(dataBlobs[ii].getElement(r, c, 0));
// }
// cv::imshow("x1", m1);
// cv::Mat m2(dataBlobs[ii].rows, dataBlobs[ii].cols, CV_32FC1);
// for(int r=0; r < m2.rows; r++)
// {
//     float * p = (float*)m2.ptr(r);
//     for(int c=0; c < m2.cols; c++)
//         p[c]=(dataBlobs[ii].getElement(r, c, 31));
// }
// cv::imshow("x2", m2);
// cv::waitKey(0);

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
