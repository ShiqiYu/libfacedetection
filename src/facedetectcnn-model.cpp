/*
The MIT License (MIT)

Copyright (c) 2018-2019 Shiqi Yu
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


#include "facedetectcnn.h"
#include <stdio.h>
#include <string.h>

#if 0
#include <opencv2/opencv.hpp>
#define TIME_START t=(double)cvGetTickCount();
#define TIME_END(FUNCNAME)     t=((double)cvGetTickCount()-t)/((double)cvGetTickFrequency()*1000.); printf(FUNCNAME);printf("=%g\n", t); total+=t;
#define TIME_TOTAL(tt) printf("total time=%g\n", (tt));
//#define TIME_END(FUNCNAME)     t=((double)cvGetTickCount()-t)/((double)cvGetTickFrequency()*1000.); total+=t;
#else
#define TIME_START
#define TIME_END(FUNCNAME)
#define TIME_TOTAL(tt)
#endif


#define NUM_CONV_LAYER 24

#if defined(_ENABLE_INT8_CONV)
extern signed char * param_ppConvCoefInt8[NUM_CONV_LAYER];
#else
extern float * param_ppConvCoefFloat[NUM_CONV_LAYER];
#endif
extern float param_pConvCoefScales[NUM_CONV_LAYER];


typedef struct SConvInfo_{
	int pad;
	int stride;
	int width;
	int height;
	int channels;
	int num;
	//float scale;
}SConvInfo;

Filters param_pFilters[NUM_CONV_LAYER]; //NUM_CONV_LAYER conv layers

int param_pMean[3] = { 104,117,123 };
float param_pConv3Norm[32] = { 6.592306137084961f, 6.558613300323486f, 6.324647903442383f, 6.125812530517578f, 6.558647632598877f, 6.526843547821045f, 6.558306694030762f, 6.556366443634033f, 6.638453483581543f, 6.558482646942139f, 6.631646633148193f, 6.536031246185303f, 6.456478118896484f, 6.558736801147461f, 6.567635536193848f, 5.369370937347412f, 6.567112445831299f, 6.5589985847473145f, 6.602363586425781f, 6.558731555938721f, 6.419933795928955f, 6.138179779052734f, 6.250294208526611f, 6.562124252319336f, 6.579089164733887f, 6.553215503692627f, 6.191119194030762f, 3.9663331508636475f, 6.459974765777588f, 6.555095195770264f, 6.545119762420654f, 6.626718997955322f };
float param_pConv4Norm[64] = { 5.24936056137085f, 5.249833106994629f, 5.201494216918945f, 5.252224445343018f, 5.2574462890625f, 5.226278305053711f, 5.258802890777588f, 5.254302024841309f, 5.1779465675354f, 5.24658203125f, 5.252774238586426f, 5.248640060424805f, 5.209632396697998f, 5.256057262420654f, 5.198976039886475f, 5.259532928466797f, 5.150023460388184f, 5.225643634796143f, 5.25822114944458f, 5.247387886047363f, 5.2590227127075195f, 5.25047492980957f, 5.264795303344727f, 5.24699592590332f, 5.249448299407959f, 5.205463409423828f, 5.252189636230469f, 5.255984783172607f, 5.209300994873047f, 5.261776447296143f, 5.251255035400391f, 5.254635810852051f, 5.25943660736084f, 5.248631477355957f, 5.285300254821777f, 5.254481792449951f, 5.248084545135498f, 5.249952793121338f, 5.234015941619873f, 5.263407230377197f, 5.245810508728027f, 5.2482500076293945f, 5.102107048034668f, 5.221688747406006f, 5.245429515838623f, 5.251964569091797f, 5.305455684661865f, 5.1277570724487305f, 5.28142786026001f, 5.235960006713867f, 5.205482482910156f, 5.251782417297363f, 5.182538032531738f, 5.27116584777832f, 5.198187351226807f, 5.248366832733154f, 5.254012584686279f, 5.251954555511475f, 5.259944438934326f, 5.248141288757324f, 5.183408737182617f, 5.265050888061523f, 5.24997615814209f, 5.262747287750244f };
float param_pConv5Norm[128] = { 3.2798357009887695f, 3.279853582382202f, 3.316532850265503f, 3.2797908782958984f, 3.283681631088257f, 3.292001962661743f, 3.2796599864959717f, 3.301004409790039f, 3.279745101928711f, 3.2916154861450195f, 3.304828405380249f, 3.2860844135284424f, 3.2962393760681152f, 3.2914977073669434f, 3.311239719390869f, 3.2796378135681152f, 3.30684757232666f, 3.2795956134796143f, 3.3049705028533936f, 3.2912471294403076f, 3.2799410820007324f, 3.280548095703125f, 3.279585838317871f, 3.3002915382385254f, 3.351128339767456f, 3.285721778869629f, 3.308690071105957f, 3.286360025405884f, 3.286612033843994f, 3.323331117630005f, 3.280801296234131f, 3.279557228088379f, 3.325319290161133f, 3.292274236679077f, 3.284330129623413f, 3.3016438484191895f, 3.305274248123169f, 3.293483257293701f, 3.2965309619903564f, 3.343932628631592f, 3.2799618244171143f, 3.281886100769043f, 3.2855217456817627f, 3.283858299255371f, 3.299082040786743f, 3.295645236968994f, 3.291444778442383f, 3.279492139816284f, 3.2956502437591553f, 3.3284802436828613f, 3.2878544330596924f, 3.291749954223633f, 3.3015894889831543f, 3.2998600006103516f, 3.2887396812438965f, 3.2832252979278564f, 3.285311698913574f, 3.30757474899292f, 3.284590721130371f, 3.3025388717651367f, 3.2936882972717285f, 3.279754877090454f, 3.307007312774658f, 3.2958528995513916f, 3.3630170822143555f, 3.326841354370117f, 3.2800698280334473f, 3.2920491695404053f, 3.2991254329681396f, 3.309135913848877f, 3.2799878120422363f, 3.2878851890563965f, 3.302861452102661f, 3.315964698791504f, 3.279761791229248f, 3.3086979389190674f, 3.2836644649505615f, 3.29606294631958f, 3.2939038276672363f, 3.296156883239746f, 3.300607204437256f, 3.3293192386627197f, 3.2886781692504883f, 3.292102098464966f, 3.279629945755005f, 3.2798566818237305f, 3.2876806259155273f, 3.281590223312378f, 3.281094789505005f, 3.2978975772857666f, 3.2799761295318604f, 3.3351552486419678f, 3.2866907119750977f, 3.338275671005249f, 3.2797188758850098f, 3.280174493789673f, 3.296318531036377f, 3.281552314758301f, 3.2805323600769043f, 3.294194459915161f, 3.279611349105835f, 3.3100433349609375f, 3.2793779373168945f, 3.2797317504882812f, 3.2823593616485596f, 3.2944772243499756f, 3.280740976333618f, 3.2863688468933105f, 3.299750804901123f, 3.282517194747925f, 3.2863147258758545f, 3.286238193511963f, 3.280881881713867f, 3.2873098850250244f, 3.2873218059539795f, 3.2804245948791504f, 3.306823968887329f, 3.312803030014038f, 3.2798690795898438f, 3.288886785507202f, 3.3185698986053467f, 3.299873113632202f, 3.279698610305786f, 3.3057520389556885f, 3.3125710487365723f, 3.2796096801757812f, 3.2905843257904053f, 3.2902579307556152f };
float param_pConv6Norm[128] = { 3.2981998920440674f, 3.2801780700683594f, 3.2922229766845703f, 3.2955803871154785f, 3.294820547103882f, 3.292754888534546f, 3.2865407466888428f, 3.286324977874756f, 3.2815260887145996f, 3.2840657234191895f, 3.282493829727173f, 3.333282470703125f, 3.289398193359375f, 3.2884361743927f, 3.2864975929260254f, 3.2839083671569824f, 3.2875373363494873f, 3.322467565536499f, 3.28637433052063f, 3.286494731903076f, 3.291119337081909f, 3.322329044342041f, 3.2931485176086426f, 3.279733896255493f, 3.2803969383239746f, 3.295511245727539f, 3.2980639934539795f, 3.280717134475708f, 3.2957653999328613f, 3.317964553833008f, 3.2798070907592773f, 3.3057520389556885f, 3.305992841720581f, 3.348924160003662f, 3.291982650756836f, 3.3030447959899902f, 3.288562774658203f, 3.2987096309661865f, 3.2800376415252686f, 3.336385488510132f, 3.2968106269836426f, 3.296558380126953f, 3.2796730995178223f, 3.286644458770752f, 3.2797164916992188f, 3.2899343967437744f, 3.3156163692474365f, 3.3474082946777344f, 3.291513442993164f, 3.3053133487701416f, 3.283517360687256f, 3.305210828781128f, 3.303983211517334f, 3.282759189605713f, 3.280306816101074f, 3.2799665927886963f, 3.285292387008667f, 3.3079776763916016f, 3.29679274559021f, 3.280120849609375f, 3.2796525955200195f, 3.28070068359375f, 3.2877960205078125f, 3.302424192428589f, 3.2920310497283936f, 3.286226511001587f, 3.2799699306488037f, 3.2863574028015137f, 3.324301242828369f, 3.291632890701294f, 3.294870376586914f, 3.289574146270752f, 3.297481060028076f, 3.304020881652832f, 3.2841410636901855f, 3.2966504096984863f, 3.280271530151367f, 3.2840888500213623f, 3.3100359439849854f, 3.282552719116211f, 3.2812161445617676f, 3.3106632232666016f, 3.2951345443725586f, 3.280796766281128f, 3.283688545227051f, 3.328918695449829f, 3.2930126190185547f, 3.291832685470581f, 3.2913215160369873f, 3.282050609588623f, 3.280987501144409f, 3.303039073944092f, 3.2960453033447266f, 3.2923476696014404f, 3.2912893295288086f, 3.283773422241211f, 3.308786392211914f, 3.294309377670288f, 3.2953407764434814f, 3.2821991443634033f, 3.2892892360687256f, 3.2869884967803955f, 3.3056561946868896f, 3.281874895095825f, 3.337285280227661f, 3.2868685722351074f, 3.2931010723114014f, 3.279754638671875f, 3.2797317504882812f, 3.3095757961273193f, 3.2800772190093994f, 3.299697160720825f, 3.2798924446105957f, 3.2848010063171387f, 3.2815306186676025f, 3.29996919631958f, 3.29498553276062f, 3.317089557647705f, 3.2876529693603516f, 3.2813613414764404f, 3.290708065032959f, 3.2828986644744873f, 3.2800583839416504f, 3.3065834045410156f, 3.287909746170044f, 3.2797391414642334f, 3.3226301670074463f, 3.323007345199585f };

SConvInfo param_pConvInfo[NUM_CONV_LAYER] = {
    //conv1_1
    { 1, 2, 3, 3, 3,  16 },
    //conv1_2
    { 0, 1, 1, 1, 16, 16 },
    //conv2_1
    { 1, 1, 3, 3, 16, 16 },
    //conv2_2
    { 0, 1, 1, 1, 16, 16 },
    //conv3_1
    { 1, 1, 3, 3, 16, 32 },
    //conv2_2
    { 0, 1, 1, 1, 32, 32 },
    //conv3_3
    { 1, 1, 3, 3, 32, 32 },
    //conv4_1
    { 1, 1, 3, 3, 32, 64 },
    //conv4_2
    { 0, 1, 1, 1, 64, 64 },
    //conv4_3
    { 1, 1, 3, 3, 64, 64 },
    //conv5_1
    { 1, 1, 3, 3, 64,128 },
    //conv5_2
    { 0, 1, 1, 1,128,128 },
    //conv5_3
    { 1, 1, 3, 3,128,128 },
    //conv6_1
    { 1, 1, 3, 3,128,128 },
    //conv6_2
    { 0, 1, 1, 1,128,128 },
    //conv6_3
    { 1, 1, 3, 3,128,128 },
    //loc3
    { 1, 1, 3, 3, 32, 12 },
    //conf3
    { 1, 1, 3, 3, 32,  6 },
    //loc4
    { 1, 1, 3, 3, 64,  8 },
    //conf4
    { 1, 1, 3, 3, 64,  4 },
    //loc5
    { 1, 1, 3, 3,128,  8 },
    //conf5
    { 1, 1, 3, 3,128,  4 },
    //loc6
    { 1, 1, 3, 3,128, 12 },
    //conf6
    { 1, 1, 3, 3,128,  6 },
};

bool param_initialized = false;

void init_parameters()
{
    //set filters 0
    {
        int i = 0;
        param_pFilters[i].stride = 1;// param_pConvInfo[i].stride;
        param_pFilters[i].pad = 0;// param_pConvInfo[i].pad;
        param_pFilters[i].scale = param_pConvCoefScales[i];
        int offset = param_pConvInfo[i].width * param_pConvInfo[i].height * param_pConvInfo[i].channels;
        
        for(int ff = 0; ff < param_pConvInfo[i].num; ff++)
        {
            CDataBlob * b3x3 = new CDataBlob(param_pConvInfo[i].width, param_pConvInfo[i].height, param_pConvInfo[i].channels);
            CDataBlob * b1x1 = new CDataBlob();
#if defined(_ENABLE_INT8_CONV)
            b3x3->setInt8DataFromCaffeFormat(param_ppConvCoefInt8[i] + ff * offset, 
                                            param_pConvInfo[i].width, param_pConvInfo[i].height, param_pConvInfo[i].channels);
            blob2vector(b3x3, b1x1, false);
#else
            b3x3->setFloatDataFromCaffeFormat(param_ppConvCoefFloat[i] + ff * offset,
                                            param_pConvInfo[i].width, param_pConvInfo[i].height, param_pConvInfo[i].channels);
            blob2vector(b3x3, b1x1, true);
#endif
            delete b3x3;
            b3x3 = 0;
            param_pFilters[i].filters.push_back(b1x1);            
        }
    }
    //set the rest
    for(int i = 1; i < NUM_CONV_LAYER; i++)
    {
        param_pFilters[i].stride = param_pConvInfo[i].stride;
        param_pFilters[i].pad = param_pConvInfo[i].pad;
        param_pFilters[i].scale = param_pConvCoefScales[i];
        int offset = param_pConvInfo[i].width * param_pConvInfo[i].height * param_pConvInfo[i].channels;
        
        for(int ff = 0; ff < param_pConvInfo[i].num; ff++)
        {
            CDataBlob * b = new CDataBlob(param_pConvInfo[i].width, param_pConvInfo[i].height, param_pConvInfo[i].channels);
#if defined(_ENABLE_INT8_CONV)
            b->setInt8DataFromCaffeFormat(param_ppConvCoefInt8[i] + ff * offset, 
                                            param_pConvInfo[i].width, param_pConvInfo[i].height, param_pConvInfo[i].channels);
#else
            b->setFloatDataFromCaffeFormat(param_ppConvCoefFloat[i] + ff * offset,
                                            param_pConvInfo[i].width, param_pConvInfo[i].height, param_pConvInfo[i].channels);
#endif
            param_pFilters[i].filters.push_back(b);            
        }
    }
}

vector<FaceRect> objectdetect_cnn(unsigned char * rgbImageData, int width, int height, int step)
{
    CDataBlob inputImage;
    CDataBlob pConvDataBlobs[NUM_CONV_LAYER];
    CDataBlob pool1, pool2, pool3, pool4, pool5;
    CDataBlob conv3norm, conv4norm, conv5norm, conv6norm;
    CDataBlob conv3priorbox, conv4priorbox, conv5priorbox, conv6priorbox;
    CDataBlob conv3priorbox_flat, conv4priorbox_flat, conv5priorbox_flat, conv6priorbox_flat, mbox_priorbox;
    CDataBlob conv3loc_flat, conv4loc_flat, conv5loc_flat, conv6loc_flat, mbox_loc;
    CDataBlob conv3conf_flat, conv4conf_flat, conv5conf_flat, conv6conf_flat, mbox_conf;

    double total = 0.0;
    double t = 0.0;

    TIME_START;
    if (!param_initialized)
    {
        init_parameters();
        param_initialized = true;
    }
    TIME_END("init");

  
    total = 0.0;
  
    TIME_START;
    //inputImage.setDataFromImage(rgbImageData, width, height, 3, step, param_pMean);
    inputImage.setDataFrom3x3S2P1to1x1S1P0FromImage(rgbImageData, width, height, 3, step, param_pMean);
    TIME_END("convert data");


/***************CONV1*********************/
    int convidx = 0;
    TIME_START;
    convolution(&inputImage, param_pFilters + convidx, pConvDataBlobs + convidx);
    TIME_END("conv11");
    TIME_START;
    relu(pConvDataBlobs+convidx);
    TIME_END("relu11");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv12");
    TIME_START;
    relu(pConvDataBlobs+convidx);
    TIME_END("relu12");

    TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool1);
    TIME_END("pool1");

/***************CONV2*********************/
    convidx++;
TIME_START;
    convolution(&pool1, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv21");
TIME_START;
    relu(pConvDataBlobs+convidx);
TIME_END("relu21");

convidx++;
TIME_START;
    convolution(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv22");
TIME_START
    relu(pConvDataBlobs+convidx);
TIME_END("relu22");

TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool2);
TIME_END("pool2");

/***************CONV3*********************/
    convidx++;
TIME_START;
    convolution(&pool2, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv31");
TIME_START;
    relu(pConvDataBlobs+convidx);
TIME_END("relu31");

convidx++;
TIME_START;
    convolution(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv32");
TIME_START;
    relu(pConvDataBlobs+convidx);
TIME_END("relu32");

convidx++;
TIME_START;
    convolution(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv33");
TIME_START;
    relu(pConvDataBlobs+convidx);
TIME_END("relu33");

TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool3);
TIME_END("pool3");

/***************CONV4*********************/
    convidx++;
TIME_START;
    convolution(&pool3, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv41");
TIME_START
    relu(pConvDataBlobs+convidx);
TIME_END("relu41");

convidx++;
TIME_START;
    convolution(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv42");
TIME_START;
    relu(pConvDataBlobs+convidx);
TIME_END("relu42");

convidx++;
TIME_START;
    convolution(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv43");
TIME_START;
    relu(pConvDataBlobs+convidx);
TIME_END("relu43");

TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool4);
TIME_END("pool4");

/***************CONV5*********************/
    convidx++;
TIME_START;
    convolution(&pool4, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv51");
TIME_START;
    relu(pConvDataBlobs+convidx);
TIME_END("relu51");

convidx++;
TIME_START;
    convolution(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv52");
TIME_START
    relu(pConvDataBlobs+convidx);
TIME_END("relu52");

convidx++;
TIME_START;
    convolution(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv53");
TIME_START;
    relu(pConvDataBlobs+convidx);
TIME_END("relu53");

TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool5);
TIME_END("pool5");

/***************CONV6*********************/
    convidx++;
TIME_START;
    convolution(&pool5, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv61");
TIME_START;
    relu(pConvDataBlobs+convidx);
TIME_END("relu61");

convidx++;
TIME_START;
    convolution(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv62");
TIME_START
    relu(pConvDataBlobs+convidx);
TIME_END("relu62");

convidx++;
TIME_START;
    convolution(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
TIME_END("conv63");
TIME_START;
    relu(pConvDataBlobs+convidx);
TIME_END("relu63");

    /***************PRIORBOX3*********************/
    int conv3idx = 6;
    TIME_START;
    normalize(pConvDataBlobs+ conv3idx, param_pConv3Norm);
    TIME_END("norm3");

    convidx++;
    TIME_START
    convolution(pConvDataBlobs+ conv3idx, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("prior3 loc");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs+ conv3idx, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("prior3 conf");

    TIME_START;
    float pSizes3[3] = {10, 16, 24};
    priorbox(pConvDataBlobs+ conv3idx, &inputImage, 3, pSizes3, &conv3priorbox);
    TIME_END("prior3");

    /***************PRIORBOX4*********************/
    int conv4idx = 9;
    TIME_START;
    normalize(pConvDataBlobs + conv4idx, param_pConv4Norm);
    TIME_END("norm4");

    convidx++;
    TIME_START
        convolution(pConvDataBlobs + conv4idx, param_pFilters + convidx, pConvDataBlobs + convidx);
    TIME_END("prior4 loc");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs + conv4idx, param_pFilters + convidx, pConvDataBlobs + convidx);
    TIME_END("prior4 conf");

    TIME_START;
    float pSizes4[2] = { 32, 48};
    priorbox(pConvDataBlobs + conv4idx, &inputImage, 2, pSizes4, &conv4priorbox);
    TIME_END("prior4");

    /***************PRIORBOX5*********************/
    int conv5idx = 12;
    TIME_START;
    normalize(pConvDataBlobs + conv5idx, param_pConv5Norm);
    TIME_END("norm5");

    convidx++;
    TIME_START
        convolution(pConvDataBlobs + conv5idx, param_pFilters + convidx, pConvDataBlobs + convidx);
    TIME_END("prior5 loc");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs + conv5idx, param_pFilters + convidx, pConvDataBlobs + convidx);
    TIME_END("prior5 conf");

    TIME_START;
    float pSizes5[2] = { 64, 96 };
    priorbox(pConvDataBlobs + conv5idx, &inputImage, 2, pSizes5, &conv5priorbox);
    TIME_END("prior5");

    /***************PRIORBOX6*********************/
    int conv6idx = 15;
    TIME_START;
    normalize(pConvDataBlobs + conv6idx, param_pConv5Norm);
    TIME_END("norm6");

    convidx++;
    TIME_START
        convolution(pConvDataBlobs + conv6idx, param_pFilters + convidx, pConvDataBlobs + convidx);
    TIME_END("prior6 loc");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs + conv6idx, param_pFilters + convidx, pConvDataBlobs + convidx);
    TIME_END("prior6 conf");

    TIME_START;
    float pSizes6[3] = { 128, 192, 256 };
    priorbox(pConvDataBlobs + conv6idx, &inputImage, 3, pSizes6, &conv6priorbox);
    TIME_END("prior6");



TIME_START;
    blob2vector(&conv3priorbox, &conv3priorbox_flat, true);
    blob2vector(pConvDataBlobs + 16, &conv3loc_flat, true);
    blob2vector(pConvDataBlobs + 17, &conv3conf_flat, true);

    blob2vector(&conv4priorbox, &conv4priorbox_flat, true);
    blob2vector(pConvDataBlobs + 18, &conv4loc_flat, true);
    blob2vector(pConvDataBlobs + 19, &conv4conf_flat, true);

    blob2vector(&conv5priorbox, &conv5priorbox_flat, true);
    blob2vector(pConvDataBlobs + 20, &conv5loc_flat, true);
    blob2vector(pConvDataBlobs + 21, &conv5conf_flat, true);

    blob2vector(&conv6priorbox, &conv6priorbox_flat, true);
    blob2vector(pConvDataBlobs + 22, &conv6loc_flat, true);
    blob2vector(pConvDataBlobs + 23, &conv6conf_flat, true);
TIME_END("prior flat");



TIME_START
    concat4(&conv3priorbox_flat, &conv4priorbox_flat, &conv5priorbox_flat, &conv6priorbox_flat, &mbox_priorbox);
    concat4(&conv3loc_flat, &conv4loc_flat, &conv5loc_flat, &conv6loc_flat, &mbox_loc);
    concat4(&conv3conf_flat, &conv4conf_flat, &conv5conf_flat, &conv6conf_flat, &mbox_conf);
TIME_END("concat prior")

TIME_START
    softmax1vector2class(&mbox_conf);
TIME_END("softmax")


    CDataBlob facesInfo;
    TIME_START;
    detection_output(&mbox_priorbox, &mbox_loc, &mbox_conf, 0.3f, 0.5f, 100, 50, &facesInfo);
    TIME_END("detection output")



    TIME_START;
    std::vector<FaceRect> faces;
    for (int i = 0; i < facesInfo.width; i++)
    {
        float score = facesInfo.getElementFloat(i, 0, 0);
        float bbxmin = facesInfo.getElementFloat(i, 0, 1);
        float bbymin = facesInfo.getElementFloat(i, 0, 2);
        float bbxmax = facesInfo.getElementFloat(i, 0, 3);
        float bbymax = facesInfo.getElementFloat(i, 0, 4);
        FaceRect r;
        r.score = score;
        //r.x = int(bbxmin * width + 0.5f);
        //r.y = int(bbymin * height + 0.5f);
        //r.w = int((bbxmax - bbxmin) * width + 0.5f);
        //r.h = int((bbymax - bbymin) * height + 0.5f);

        r.w = int( ((bbxmax - bbxmin) * width + (bbymax - bbymin) * height + 1) / 2);
        r.h = r.w;
        r.x = int(((bbxmin + bbxmax) * width - r.w + 0.5f) / 2);
        r.y = int(((bbymin + bbymax) * height - r.h + 0.5f) / 2);

        faces.push_back(r);
    }
    TIME_END("copy result");
    
    TIME_TOTAL(total);

    return faces;
}
int * facedetect_cnn(unsigned char * result_buffer, //buffer memory for storing face detection results, !!its size must be 0x20000 Bytes!!
    unsigned char * rgb_image_data, int width, int height, int step) //input image, it must be RGB (three-channel) image!
{
#ifdef	__CALL_LIMIT__
    static int call_count = 0;
#endif

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

    double t, total=0;
    TIME_START;

    int num_faces =(int)faces.size();
    num_faces = MIN(num_faces, 256);

    int * pCount = (int *)result_buffer;
    pCount[0] = num_faces;

    for (int i = 0; i < num_faces; i++)
    {
        short * p = ((short*)(result_buffer + 4)) + 142 * i;
        p[0] = (short)faces[i].x;
        p[1] = (short)faces[i].y;
        p[2] = (short)faces[i].w;
        p[3] = (short)faces[i].h;
        p[4] = (short)(faces[i].score * faces[i].score * 100);
    }
#ifdef	__CALL_LIMIT__
    if(call_count>1814403)
    {
        memset(result_buffer, 0 , 4+284*num_faces);
    }
    else
        call_count++;
#endif

    TIME_END("call detection");
    return pCount;
}
