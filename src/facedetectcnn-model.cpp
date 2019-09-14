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

extern signed char * param_ppConvCoefInt8[NUM_CONV_LAYER];
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

float param_pConv3Norm[64] = { 6.067704200744629f, 6.086832523345947f, 6.075444221496582f, 4.864563465118408f, 4.982297897338867f, 6.065876483917236f, 6.065804958343506f, 6.066604137420654f, 6.091238498687744f, 6.066442489624023f, 6.067852020263672f, 6.065065383911133f, 6.111854076385498f, 5.854933738708496f, 5.094123363494873f, 5.611084938049316f, 5.638108253479004f, 6.075394153594971f, 6.086704730987549f, 6.040279865264893f, 5.760599136352539f, 6.066614627838135f, 6.067028999328613f, 6.090893268585205f, 6.067197799682617f, 6.0027570724487305f, 6.122867107391357f, 5.662094593048096f, 6.065771102905273f, 6.100675106048584f, 6.06779670715332f, 6.067671298980713f, 6.066753387451172f, 5.737717151641846f, 4.2541680335998535f, 6.091390609741211f, 6.082061767578125f, 6.048476696014404f, 5.471676349639893f, 6.0827765464782715f, 5.785499572753906f, 6.0660271644592285f, 6.075671195983887f, 6.097797393798828f, 6.047597885131836f, 5.906824111938477f, 5.394303798675537f, 6.067469596862793f, 6.061819553375244f, 5.82515811920166f, 5.5635809898376465f, 6.114983081817627f, 5.813036918640137f, 6.084056854248047f, 5.945978164672852f, 5.97323751449585f, 6.09974479675293f, 5.745806694030762f, 5.416194915771484f, 5.367262363433838f, 6.10166072845459f, 6.092775344848633f, 6.067975044250488f, 5.756527900695801f };
float param_pConv4Norm[128] = { 4.853384494781494f, 4.889324188232422f, 4.874561786651611f, 4.852031230926514f, 4.922458648681641f, 4.826481342315674f, 4.852572441101074f, 4.854414939880371f, 4.861706256866455f, 4.847333908081055f, 4.850497722625732f, 4.8718366622924805f, 4.846635818481445f, 4.853606700897217f, 4.8193840980529785f, 4.8496294021606445f, 4.852749824523926f, 4.856253147125244f, 4.873744964599609f, 4.82371187210083f, 4.8532867431640625f, 4.856527328491211f, 4.85302734375f, 4.852819919586182f, 4.8583760261535645f, 4.854391574859619f, 4.8661627769470215f, 4.872909069061279f, 4.860682010650635f, 4.870107173919678f, 4.864952087402344f, 4.8647074699401855f, 4.875209331512451f, 4.841148376464844f, 4.853340148925781f, 4.815730094909668f, 4.8606181144714355f, 4.864851474761963f, 4.853943347930908f, 4.857072353363037f, 4.865845203399658f, 4.83903169631958f, 4.839323997497559f, 4.853547096252441f, 4.861319065093994f, 4.854262351989746f, 4.85335111618042f, 4.848951816558838f, 4.857367038726807f, 4.8540472984313965f, 4.854204177856445f, 4.853390693664551f, 4.850429058074951f, 4.8542256355285645f, 4.84539270401001f, 4.872445106506348f, 4.8350419998168945f, 4.855948448181152f, 4.791098594665527f, 4.860246658325195f, 4.85080099105835f, 4.824630260467529f, 4.852724552154541f, 4.83675479888916f, 4.853806495666504f, 4.831771373748779f, 4.839821815490723f, 4.856444835662842f, 4.853408336639404f, 4.839377403259277f, 4.888157367706299f, 4.847803592681885f, 4.926937580108643f, 4.86123514175415f, 4.8530097007751465f, 4.843791484832764f, 4.8553466796875f, 4.862685203552246f, 4.836782932281494f, 4.829556941986084f, 4.865046977996826f, 4.949220180511475f, 4.853907585144043f, 4.858654022216797f, 4.852842330932617f, 4.852590084075928f, 4.853270053863525f, 4.857703685760498f, 4.831657409667969f, 4.831831455230713f, 4.835714817047119f, 4.873287677764893f, 4.861383438110352f, 4.849035263061523f, 4.826011657714844f, 4.859689712524414f, 4.840816497802734f, 4.861761093139648f, 4.822288513183594f, 4.901276588439941f, 4.821767330169678f, 4.8713788986206055f, 4.8542914390563965f, 4.85370397567749f, 4.84406042098999f, 4.8527445793151855f, 4.876774311065674f, 4.875062942504883f, 4.885288238525391f, 4.8349103927612305f, 4.853190898895264f, 4.860854148864746f, 4.837437152862549f, 4.8570475578308105f, 4.856215953826904f, 4.8456034660339355f, 4.860386371612549f, 4.851935386657715f, 4.846256256103516f, 4.8642497062683105f, 4.861553192138672f, 4.850989818572998f, 4.867442607879639f, 4.852613925933838f, 4.856968879699707f, 4.855709552764893f, 4.85834264755249f, 4.854826927185059f };
float param_pConv5Norm[128] = { 3.079456090927124f, 3.039461135864258f, 3.0351147651672363f, 3.037583589553833f, 3.048778533935547f, 3.0725064277648926f, 3.0622661113739014f, 3.064093828201294f, 3.048044443130493f, 3.036616086959839f, 3.1058716773986816f, 3.048161745071411f, 3.058666467666626f, 3.045867919921875f, 3.043402671813965f, 3.053333282470703f, 3.045125722885132f, 3.04072642326355f, 3.050602436065674f, 3.0699708461761475f, 3.0647435188293457f, 3.0702977180480957f, 3.061201810836792f, 3.0459437370300293f, 3.0622904300689697f, 3.067882776260376f, 3.0438363552093506f, 3.0610761642456055f, 3.044037103652954f, 3.0485501289367676f, 3.0330629348754883f, 3.048556327819824f, 3.0330440998077393f, 3.0585777759552f, 3.0413095951080322f, 3.0553483963012695f, 3.0651161670684814f, 3.0932703018188477f, 3.0698587894439697f, 3.067293167114258f, 3.040133237838745f, 3.0340847969055176f, 3.042184591293335f, 3.044334650039673f, 3.033623695373535f, 3.0592658519744873f, 3.0473034381866455f, 3.0568149089813232f, 3.0512683391571045f, 3.0338335037231445f, 3.0865068435668945f, 3.0527186393737793f, 3.035172700881958f, 3.0331568717956543f, 3.0637283325195312f, 3.0530037879943848f, 3.041080951690674f, 3.054175853729248f, 3.045405387878418f, 3.060072898864746f, 3.052427291870117f, 3.0673320293426514f, 3.0618369579315186f, 3.056979179382324f, 3.0732641220092773f, 3.049957275390625f, 3.033128261566162f, 3.0706357955932617f, 3.060206651687622f, 3.058335304260254f, 3.0502429008483887f, 3.033816337585449f, 3.0882437229156494f, 3.039879083633423f, 3.064310312271118f, 3.0645105838775635f, 3.0497498512268066f, 3.0598857402801514f, 3.0345287322998047f, 3.0501859188079834f, 3.0384273529052734f, 3.0725810527801514f, 3.089094638824463f, 3.074632406234741f, 3.0437800884246826f, 3.05190110206604f, 3.0339508056640625f, 3.033148765563965f, 3.0332436561584473f, 3.0683743953704834f, 3.075549602508545f, 3.057891845703125f, 3.0438103675842285f, 3.037956476211548f, 3.0511362552642822f, 3.0402984619140625f, 3.0735790729522705f, 3.068497896194458f, 3.039294719696045f, 3.0881500244140625f, 3.047109842300415f, 3.0450427532196045f, 3.0337936878204346f, 3.035036087036133f, 3.0530760288238525f, 3.034095048904419f, 3.074824571609497f, 3.0406930446624756f, 3.0669476985931396f, 3.049168109893799f, 3.076056480407715f, 3.046610116958618f, 3.083836317062378f, 3.080292224884033f, 3.0445592403411865f, 3.0415334701538086f, 3.057447671890259f, 3.0389440059661865f, 3.058000326156616f, 3.033161163330078f, 3.0331573486328125f, 3.044421911239624f, 3.039666175842285f, 3.054525375366211f, 3.098982572555542f, 3.0593252182006836f, 3.038623094558716f, 3.0782241821289062f };
float param_pConv6Norm[128] = { 3.0690505504608154f, 3.0386440753936768f, 3.0784153938293457f, 3.064088821411133f, 3.058891773223877f, 3.036539316177368f, 3.0579793453216553f, 3.053611993789673f, 3.050058603286743f, 3.0333425998687744f, 3.097348213195801f, 3.06592059135437f, 3.068408489227295f, 3.0698347091674805f, 3.0762569904327393f, 3.054143190383911f, 3.054262399673462f, 3.0519206523895264f, 3.0427865982055664f, 3.046510934829712f, 3.054152250289917f, 3.101977586746216f, 3.0366146564483643f, 3.06791615486145f, 3.044861316680908f, 3.05081844329834f, 3.0827019214630127f, 3.046961784362793f, 3.0690267086029053f, 3.073493242263794f, 3.036958932876587f, 3.0742595195770264f, 3.0346856117248535f, 3.044112205505371f, 3.0804336071014404f, 3.0559825897216797f, 3.039485454559326f, 3.0536017417907715f, 3.035005569458008f, 3.0331103801727295f, 3.0532901287078857f, 3.041443347930908f, 3.075648307800293f, 3.033519744873047f, 3.057023525238037f, 3.0344996452331543f, 3.069003105163574f, 3.041029453277588f, 3.055204153060913f, 3.0332140922546387f, 3.058738946914673f, 3.033165454864502f, 3.041567802429199f, 3.0333807468414307f, 3.0331976413726807f, 3.0732359886169434f, 3.0440356731414795f, 3.0499985218048096f, 3.0332562923431396f, 3.0454604625701904f, 3.044590473175049f, 3.0364391803741455f, 3.034428596496582f, 3.040388584136963f, 3.0335984230041504f, 3.0358216762542725f, 3.037226676940918f, 3.0331900119781494f, 3.033982992172241f, 3.0461173057556152f, 3.0425851345062256f, 3.0333290100097656f, 3.0733861923217773f, 3.0609347820281982f, 3.0564520359039307f, 3.0463502407073975f, 3.066772699356079f, 3.0337183475494385f, 3.0710198879241943f, 3.0378026962280273f, 3.0489232540130615f, 3.0642757415771484f, 3.062023878097534f, 3.0553982257843018f, 3.0353715419769287f, 3.0331521034240723f, 3.033599615097046f, 3.038581609725952f, 3.0468997955322266f, 3.070789098739624f, 3.0823018550872803f, 3.045168399810791f, 3.050402879714966f, 3.04396390914917f, 3.053098678588867f, 3.073054790496826f, 3.036590814590454f, 3.0514144897460938f, 3.067574977874756f, 3.0603926181793213f, 3.04386568069458f, 3.069430112838745f, 3.0422122478485107f, 3.0464704036712646f, 3.0503995418548584f, 3.03448486328125f, 3.047955274581909f, 3.034306287765503f, 3.034529447555542f, 3.0360774993896484f, 3.0508804321289062f, 3.0470383167266846f, 3.05137038230896f, 3.0441949367523193f, 3.065099000930786f, 3.0329136848449707f, 3.0651464462280273f, 3.075334310531616f, 3.03696870803833f, 3.033154249191284f, 3.0385067462921143f, 3.0376787185668945f, 3.0448710918426514f, 3.0523617267608643f, 3.0374507904052734f, 3.0359244346618652f, 3.040173292160034f, 3.03313946723938f };

SConvInfo param_pConvInfo[NUM_CONV_LAYER] = {
    //conv1_1
    { 1, 2, 3, 3, 3,  32 },
    //conv1_2
    { 0, 1, 1, 1, 32, 16 },
    //conv2_1
    { 1, 1, 3, 3, 16, 32 },
    //conv2_2
    { 0, 1, 1, 1, 32, 16 },
    //conv3_1
    { 1, 1, 3, 3, 16, 64 },
    //conv2_2
    { 0, 1, 1, 1, 64, 32 },
    //conv3_3
    { 1, 1, 3, 3, 32, 64 },
    //conv4_1
    { 1, 1, 3, 3, 64, 128 },
    //conv4_2
    { 0, 1, 1, 1, 128, 64 },
    //conv4_3
    { 1, 1, 3, 3, 64, 128 },
    //conv5_1
    { 1, 1, 3, 3, 128,128 },
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
    { 1, 1, 3, 3, 64, 12 },
    //conf3
    { 1, 1, 3, 3, 64,  6 },
    //loc4
    { 1, 1, 3, 3, 128,  8 },
    //conf4
    { 1, 1, 3, 3, 128,  4 },
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
            CDataBlob<signed char> * b3x3 = new CDataBlob<signed char>(param_pConvInfo[i].width, param_pConvInfo[i].height, param_pConvInfo[i].channels);
            CDataBlob<signed char> * b1x1 = new CDataBlob<signed char>();
            b3x3->setInt8DataFromCaffeFormat(param_ppConvCoefInt8[i] + ff * offset,
                                            param_pConvInfo[i].width, param_pConvInfo[i].height, param_pConvInfo[i].channels);
            blob2vector<signed char>(b3x3, b1x1);
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
            CDataBlob<signed char> * b = new CDataBlob<signed char>(param_pConvInfo[i].width, param_pConvInfo[i].height, param_pConvInfo[i].channels);
            b->setInt8DataFromCaffeFormat(param_ppConvCoefInt8[i] + ff * offset, 
                                            param_pConvInfo[i].width, param_pConvInfo[i].height, param_pConvInfo[i].channels);
            param_pFilters[i].filters.push_back(b);            
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
    inputImage.setDataFrom3x3S2P1to1x1S1P0FromImage(rgbImageData, width, height, 3, step);
    TIME_END("convert data");


/***************CONV1*********************/
    int convidx = 0;
    TIME_START;
    convolution_relu(&inputImage, param_pFilters + convidx, pConvDataBlobs + convidx);
    TIME_END("conv11");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv12");

    TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool1);
    TIME_END("pool1");

/***************CONV2*********************/
    convidx++;
    TIME_START;
    convolution_relu(&pool1, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv21");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv22");

    TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool2);
    TIME_END("pool2");

/***************CONV3*********************/
    convidx++;
    TIME_START;
    convolution_relu(&pool2, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv31");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv32");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv33");

    TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool3);
    TIME_END("pool3");

/***************CONV4*********************/
    convidx++;
    TIME_START;
    convolution_relu(&pool3, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv41");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv42");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv43");

    TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool4);
    TIME_END("pool4");

/***************CONV5*********************/
    convidx++;
    TIME_START;
    convolution_relu(&pool4, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv51");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv52");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv53");

    TIME_START;
    maxpooling2x2S2(pConvDataBlobs+convidx, &pool5);
    TIME_END("pool5");

/***************CONV6*********************/
    convidx++;
    TIME_START;
    convolution_relu(&pool5, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv61");

    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv62");


    convidx++;
    TIME_START;
    convolution_relu(pConvDataBlobs+convidx-1, param_pFilters+convidx, pConvDataBlobs+convidx);
    TIME_END("conv63");


    /***************PRIORBOX3*********************/
    int conv3idx = 6;
    TIME_START;
    normalize(pConvDataBlobs+ conv3idx, param_pConv3Norm);
    TIME_END("norm3");

    convidx++;
    TIME_START
    convolution(pConvDataBlobs+ conv3idx, param_pFilters+convidx, pConvDataBlobsBranch + 0);
    TIME_END("prior3 loc");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs+ conv3idx, param_pFilters+convidx, pConvDataBlobsBranch + 1);
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
    convolution(pConvDataBlobs + conv4idx, param_pFilters + convidx, pConvDataBlobsBranch + 2);
    TIME_END("prior4 loc");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs + conv4idx, param_pFilters + convidx, pConvDataBlobsBranch + 3);
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
    convolution(pConvDataBlobs + conv5idx, param_pFilters + convidx, pConvDataBlobsBranch + 4);
    TIME_END("prior5 loc");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs + conv5idx, param_pFilters + convidx, pConvDataBlobsBranch + 5);
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
    convolution(pConvDataBlobs + conv6idx, param_pFilters + convidx, pConvDataBlobsBranch + 6);
    TIME_END("prior6 loc");

    convidx++;
    TIME_START;
    convolution(pConvDataBlobs + conv6idx, param_pFilters + convidx, pConvDataBlobsBranch + 7);
    TIME_END("prior6 conf");

    TIME_START;
    float pSizes6[3] = { 128, 192, 256 };
    priorbox(pConvDataBlobs + conv6idx, &inputImage, 3, pSizes6, &conv6priorbox);
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
    detection_output(&mbox_priorbox, &mbox_loc_float, &mbox_conf_float, 0.3f, 0.5f, 100, 50, &facesInfo);
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

    TIME_END("call detection");
    return pCount;
}
