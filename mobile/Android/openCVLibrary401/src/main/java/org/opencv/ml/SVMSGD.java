//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.ml;

import java.lang.String;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.SVMSGD;
import org.opencv.ml.StatModel;

// C++: class SVMSGD
//javadoc: SVMSGD

public class SVMSGD extends StatModel {

    protected SVMSGD(long addr) { super(addr); }

    // internal usage only
    public static SVMSGD __fromPtr__(long addr) { return new SVMSGD(addr); }

    // C++: enum SvmsgdType
    public static final int
            SGD = 0,
            ASGD = 1;


    // C++: enum MarginType
    public static final int
            SOFT_MARGIN = 0,
            HARD_MARGIN = 1;


    //
    // C++:  Mat cv::ml::SVMSGD::getWeights()
    //

    //javadoc: SVMSGD::getWeights()
    public  Mat getWeights()
    {
        
        Mat retVal = new Mat(getWeights_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: static Ptr_SVMSGD cv::ml::SVMSGD::create()
    //

    //javadoc: SVMSGD::create()
    public static SVMSGD create()
    {
        
        SVMSGD retVal = SVMSGD.__fromPtr__(create_0());
        
        return retVal;
    }


    //
    // C++: static Ptr_SVMSGD cv::ml::SVMSGD::load(String filepath, String nodeName = String())
    //

    //javadoc: SVMSGD::load(filepath, nodeName)
    public static SVMSGD load(String filepath, String nodeName)
    {
        
        SVMSGD retVal = SVMSGD.__fromPtr__(load_0(filepath, nodeName));
        
        return retVal;
    }

    //javadoc: SVMSGD::load(filepath)
    public static SVMSGD load(String filepath)
    {
        
        SVMSGD retVal = SVMSGD.__fromPtr__(load_1(filepath));
        
        return retVal;
    }


    //
    // C++:  TermCriteria cv::ml::SVMSGD::getTermCriteria()
    //

    //javadoc: SVMSGD::getTermCriteria()
    public  TermCriteria getTermCriteria()
    {
        
        TermCriteria retVal = new TermCriteria(getTermCriteria_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  float cv::ml::SVMSGD::getInitialStepSize()
    //

    //javadoc: SVMSGD::getInitialStepSize()
    public  float getInitialStepSize()
    {
        
        float retVal = getInitialStepSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::ml::SVMSGD::getMarginRegularization()
    //

    //javadoc: SVMSGD::getMarginRegularization()
    public  float getMarginRegularization()
    {
        
        float retVal = getMarginRegularization_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::ml::SVMSGD::getShift()
    //

    //javadoc: SVMSGD::getShift()
    public  float getShift()
    {
        
        float retVal = getShift_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::ml::SVMSGD::getStepDecreasingPower()
    //

    //javadoc: SVMSGD::getStepDecreasingPower()
    public  float getStepDecreasingPower()
    {
        
        float retVal = getStepDecreasingPower_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::SVMSGD::getMarginType()
    //

    //javadoc: SVMSGD::getMarginType()
    public  int getMarginType()
    {
        
        int retVal = getMarginType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::SVMSGD::getSvmsgdType()
    //

    //javadoc: SVMSGD::getSvmsgdType()
    public  int getSvmsgdType()
    {
        
        int retVal = getSvmsgdType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::ml::SVMSGD::setInitialStepSize(float InitialStepSize)
    //

    //javadoc: SVMSGD::setInitialStepSize(InitialStepSize)
    public  void setInitialStepSize(float InitialStepSize)
    {
        
        setInitialStepSize_0(nativeObj, InitialStepSize);
        
        return;
    }


    //
    // C++:  void cv::ml::SVMSGD::setMarginRegularization(float marginRegularization)
    //

    //javadoc: SVMSGD::setMarginRegularization(marginRegularization)
    public  void setMarginRegularization(float marginRegularization)
    {
        
        setMarginRegularization_0(nativeObj, marginRegularization);
        
        return;
    }


    //
    // C++:  void cv::ml::SVMSGD::setMarginType(int marginType)
    //

    //javadoc: SVMSGD::setMarginType(marginType)
    public  void setMarginType(int marginType)
    {
        
        setMarginType_0(nativeObj, marginType);
        
        return;
    }


    //
    // C++:  void cv::ml::SVMSGD::setOptimalParameters(int svmsgdType = SVMSGD::ASGD, int marginType = SVMSGD::SOFT_MARGIN)
    //

    //javadoc: SVMSGD::setOptimalParameters(svmsgdType, marginType)
    public  void setOptimalParameters(int svmsgdType, int marginType)
    {
        
        setOptimalParameters_0(nativeObj, svmsgdType, marginType);
        
        return;
    }

    //javadoc: SVMSGD::setOptimalParameters(svmsgdType)
    public  void setOptimalParameters(int svmsgdType)
    {
        
        setOptimalParameters_1(nativeObj, svmsgdType);
        
        return;
    }

    //javadoc: SVMSGD::setOptimalParameters()
    public  void setOptimalParameters()
    {
        
        setOptimalParameters_2(nativeObj);
        
        return;
    }


    //
    // C++:  void cv::ml::SVMSGD::setStepDecreasingPower(float stepDecreasingPower)
    //

    //javadoc: SVMSGD::setStepDecreasingPower(stepDecreasingPower)
    public  void setStepDecreasingPower(float stepDecreasingPower)
    {
        
        setStepDecreasingPower_0(nativeObj, stepDecreasingPower);
        
        return;
    }


    //
    // C++:  void cv::ml::SVMSGD::setSvmsgdType(int svmsgdType)
    //

    //javadoc: SVMSGD::setSvmsgdType(svmsgdType)
    public  void setSvmsgdType(int svmsgdType)
    {
        
        setSvmsgdType_0(nativeObj, svmsgdType);
        
        return;
    }


    //
    // C++:  void cv::ml::SVMSGD::setTermCriteria(TermCriteria val)
    //

    //javadoc: SVMSGD::setTermCriteria(val)
    public  void setTermCriteria(TermCriteria val)
    {
        
        setTermCriteria_0(nativeObj, val.type, val.maxCount, val.epsilon);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  Mat cv::ml::SVMSGD::getWeights()
    private static native long getWeights_0(long nativeObj);

    // C++: static Ptr_SVMSGD cv::ml::SVMSGD::create()
    private static native long create_0();

    // C++: static Ptr_SVMSGD cv::ml::SVMSGD::load(String filepath, String nodeName = String())
    private static native long load_0(String filepath, String nodeName);
    private static native long load_1(String filepath);

    // C++:  TermCriteria cv::ml::SVMSGD::getTermCriteria()
    private static native double[] getTermCriteria_0(long nativeObj);

    // C++:  float cv::ml::SVMSGD::getInitialStepSize()
    private static native float getInitialStepSize_0(long nativeObj);

    // C++:  float cv::ml::SVMSGD::getMarginRegularization()
    private static native float getMarginRegularization_0(long nativeObj);

    // C++:  float cv::ml::SVMSGD::getShift()
    private static native float getShift_0(long nativeObj);

    // C++:  float cv::ml::SVMSGD::getStepDecreasingPower()
    private static native float getStepDecreasingPower_0(long nativeObj);

    // C++:  int cv::ml::SVMSGD::getMarginType()
    private static native int getMarginType_0(long nativeObj);

    // C++:  int cv::ml::SVMSGD::getSvmsgdType()
    private static native int getSvmsgdType_0(long nativeObj);

    // C++:  void cv::ml::SVMSGD::setInitialStepSize(float InitialStepSize)
    private static native void setInitialStepSize_0(long nativeObj, float InitialStepSize);

    // C++:  void cv::ml::SVMSGD::setMarginRegularization(float marginRegularization)
    private static native void setMarginRegularization_0(long nativeObj, float marginRegularization);

    // C++:  void cv::ml::SVMSGD::setMarginType(int marginType)
    private static native void setMarginType_0(long nativeObj, int marginType);

    // C++:  void cv::ml::SVMSGD::setOptimalParameters(int svmsgdType = SVMSGD::ASGD, int marginType = SVMSGD::SOFT_MARGIN)
    private static native void setOptimalParameters_0(long nativeObj, int svmsgdType, int marginType);
    private static native void setOptimalParameters_1(long nativeObj, int svmsgdType);
    private static native void setOptimalParameters_2(long nativeObj);

    // C++:  void cv::ml::SVMSGD::setStepDecreasingPower(float stepDecreasingPower)
    private static native void setStepDecreasingPower_0(long nativeObj, float stepDecreasingPower);

    // C++:  void cv::ml::SVMSGD::setSvmsgdType(int svmsgdType)
    private static native void setSvmsgdType_0(long nativeObj, int svmsgdType);

    // C++:  void cv::ml::SVMSGD::setTermCriteria(TermCriteria val)
    private static native void setTermCriteria_0(long nativeObj, int val_type, int val_maxCount, double val_epsilon);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
