#include <jni.h>
#include <string>
#include <android/log.h>
#include "facedetectcnn.h"
#include <opencv2/opencv.hpp>

//define the buffer size. Do not change the size!
#define DETECT_BUFFER_SIZE 0x20000
using namespace cv;

extern "C"  {
char *JNITag = const_cast<char *>("facedetection-jni");

JNIEXPORT jobjectArray JNICALL
Java_org_dp_facedetection_MainActivity_facedetect(JNIEnv *env,jobject /* this */,jlong matAddr)
{
    jobjectArray faceArgs = nullptr;
    Mat& img  = *(Mat*)matAddr;
    Mat bgr = img.clone();
    cvtColor(img, bgr, COLOR_RGBA2BGR);
    __android_log_print(ANDROID_LOG_ERROR, JNITag,"convert RGBA to BGR");
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
    //!!! The input image must be a BGR one (three-channel)
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
    jfieldID faceLandmarks = env->GetFieldID(faceClass, "faceLandmarks", "[Lorg/opencv/core/Point;");//获取List类型参数landmarks
    jfieldID faceRect = env->GetFieldID(faceClass, "faceRect","Lorg/opencv/core/Rect;");//获取faceRect签名
    /**
     * 获取RECT类以及对应参数的签名
     */
    jclass rectClass = env->FindClass("org/opencv/core/Rect");//获取到RECT类
    jmethodID rectClassInitID = (env)->GetMethodID(rectClass, "<init>", "()V");
    jfieldID rect_x = env->GetFieldID(rectClass, "x", "I");//获取x的签名
    jfieldID rect_y = env->GetFieldID(rectClass, "y", "I");//获取y的签名
    jfieldID rect_width = env->GetFieldID(rectClass, "width", "I");//获取width的签名
    jfieldID rect_height = env->GetFieldID(rectClass, "height", "I");//获取height的签名

    /**
    * 获取Point类以及对应参数的签名
    */
    jclass pointClass = env->FindClass("org/opencv/core/Point");//获取到Point类
    jmethodID pointClassInitID = (env)->GetMethodID(pointClass, "<init>", "()V");
    jfieldID point_x = env->GetFieldID(pointClass, "x", "D");//获取x的签名
    jfieldID point_y = env->GetFieldID(pointClass, "y", "D");//获取y的签名


    faceArgs = (env)->NewObjectArray(numOfFaces, faceClass, 0);
    //print the detection results
    for(int i = 0; i < (pResults ? *pResults : 0); i++)
    {
        short * p = ((short*)(pResults+1))+142*i;
        int confidence = p[0];
        int x = p[1];
        int y = p[2];
        int w = p[3];
        int h = p[4];

        __android_log_print(ANDROID_LOG_ERROR, JNITag,"face %d rect=[%d, %d, %d, %d], confidence=%d\n",i,x,y,w,h,confidence);
        jobject  newFace = (env)->NewObject(faceClass, faceClassInitID);
        jobject  newRect = (env)->NewObject(rectClass, rectClassInitID);

        (env)->SetIntField(newRect, rect_x, x);
        (env)->SetIntField(newRect, rect_y, y);
        (env)->SetIntField(newRect, rect_width, w);
        (env)->SetIntField(newRect, rect_height, h);
        (env)->SetObjectField(newFace,faceRect,newRect);
        env->DeleteLocalRef(newRect);

        jobjectArray newPoints = (env)->NewObjectArray(5, pointClass, 0);
        for (int j = 5; j < 14; j += 2){
            int p_x = p[j];
            int p_y = p[j+1];
            jobject  newPoint = (env)->NewObject(pointClass, pointClassInitID);
            (env)->SetDoubleField(newPoint, point_x, (double)p_x);
            (env)->SetDoubleField(newPoint, point_y, (double)p_y);
            int index = (j-5)/2;
            (env)->SetObjectArrayElement(newPoints, index, newPoint);
            env->DeleteLocalRef(newPoint);
            __android_log_print(ANDROID_LOG_ERROR, JNITag,"landmark %d =[%f, %f]\n",index,(double)p_x,(double)p_y);
        }
        (env)->SetObjectField(newFace,faceLandmarks,newPoints);
        env->DeleteLocalRef(newPoints);

        (env)->SetIntField(newFace,faceConfidence,confidence);

        (env)->SetObjectArrayElement(faceArgs, i, newFace);
        env->DeleteLocalRef(newFace);

    }

    //release the buffer
    free(pBuffer);

    return faceArgs;
}
}