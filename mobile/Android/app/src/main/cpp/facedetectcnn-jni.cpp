#include <jni.h>
#include <string>
#include <android/log.h>
#include "facedetectcnn.h"
#include <opencv2/opencv.hpp>

//define the buffer size. Do not change the size!
#define DETECT_BUFFER_SIZE 0x20000
using namespace cv;

extern "C"  {
char *JNITag = const_cast<char *>("dp-jni");

JNIEXPORT jobjectArray JNICALL
Java_org_dp_facedetection_MainActivity_facedetect(JNIEnv *env,jobject /* this */,jlong matAddr)
{
    jobjectArray faceArgs = nullptr;
    Mat& img  = *(Mat*)matAddr;
    Mat bgr = img.clone();
    cvtColor(img, bgr, COLOR_RGBA2BGR);
    __android_log_print(ANDROID_LOG_ERROR, JNITag,"convert RGBA to RGB");
    //load an image and convert it to gray (single-channel)
    if(bgr.empty())
    {
        fprintf(stderr, "Can not convert image");
        return faceArgs;
    }

    int * pResults = NULL;
    //pBuffer is used in the detection functions.
    //If you call functions in multiple threads, please create one buffer for each thread!
    unsigned char * pBuffer = (unsigned char *)malloc(DETECT_BUFFER_SIZE);
    if(!pBuffer)
    {
        fprintf(stderr, "Can not alloc buffer.\n");
        return faceArgs;
    }


    ///////////////////////////////////////////
    // CNN face detection
    // Best detection rate
    //////////////////////////////////////////
    //!!! The input image must be a RGB one (three-channel)
    //!!! DO NOT RELEASE pResults !!!
    pResults = facedetect_cnn(pBuffer, (unsigned char*)(bgr.ptr(0)), bgr.cols, bgr.rows, (int)bgr.step);
    int numOfFaces = pResults ? *pResults : 0;
    __android_log_print(ANDROID_LOG_ERROR, JNITag,"%d faces detected.\n", numOfFaces);

    /**
     * 获取Face类以及其对于参数的签名
     */
    jclass faceClass = env->FindClass("org/dp/facedetection/Face");//获取Face类
    jmethodID faceClassInitID = (env)->GetMethodID(faceClass, "<init>", "()V");
    jfieldID faceConfidence = env->GetFieldID(faceClass, "faceConfidence", "I");//获取int类型参数confidence
    jfieldID faceAngle = env->GetFieldID(faceClass, "faceAngle", "I");//获取int数组类型参数angle
    jfieldID faceRect = env->GetFieldID(faceClass, "faceRect",
                                      "Lorg/opencv/core/Rect;");//获取faceRect的签名
    /**
     * 获取RECT类以及对应参数的签名
     */
    jclass rectClass = env->FindClass("org/opencv/core/Rect");//获取到RECT类
    jmethodID rectClassInitID = (env)->GetMethodID(rectClass, "<init>", "()V");
    jfieldID rect_x = env->GetFieldID(rectClass, "x", "I");//获取x的签名
    jfieldID rect_y = env->GetFieldID(rectClass, "y", "I");//获取y的签名
    jfieldID rect_width = env->GetFieldID(rectClass, "width", "I");//获取width的签名
    jfieldID rect_height = env->GetFieldID(rectClass, "height", "I");//获取height的签名

    faceArgs = (env)->NewObjectArray(numOfFaces, faceClass, 0);
    //print the detection results
    for(int i = 0; i < (pResults ? *pResults : 0); i++)
    {
        short * p = ((short*)(pResults+1))+142*i;
        int x = p[0];
        int y = p[1];
        int w = p[2];
        int h = p[3];
        int confidence = p[4];
        int angle = p[5];
        __android_log_print(ANDROID_LOG_ERROR, JNITag,"face_rect=[%d, %d, %d, %d], confidence=%d, angle=%d\n", x,y,w,h,confidence, angle);
        jobject  newFace = (env)->NewObject(faceClass, faceClassInitID);
        jobject  newRect = (env)->NewObject(rectClass, rectClassInitID);

        (env)->SetIntField(newRect, rect_x, x);
        (env)->SetIntField(newRect, rect_y, y);
        (env)->SetIntField(newRect, rect_width, w);
        (env)->SetIntField(newRect, rect_height, h);
        (env)->SetObjectField(newFace,faceRect,newRect);

        (env)->SetIntField(newFace,faceConfidence,confidence);
        (env)->SetIntField(newFace,faceAngle,angle);

        (env)->SetObjectArrayElement(faceArgs, i, newFace);
    }

    //release the buffer
    free(pBuffer);

    return faceArgs;
}
};