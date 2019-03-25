//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;

import org.opencv.core.Mat;
import org.opencv.video.BackgroundSubtractor;

// C++: class BackgroundSubtractorMOG2
//javadoc: BackgroundSubtractorMOG2

public class BackgroundSubtractorMOG2 extends BackgroundSubtractor {

    protected BackgroundSubtractorMOG2(long addr) { super(addr); }

    // internal usage only
    public static BackgroundSubtractorMOG2 __fromPtr__(long addr) { return new BackgroundSubtractorMOG2(addr); }

    //
    // C++:  bool cv::BackgroundSubtractorMOG2::getDetectShadows()
    //

    //javadoc: BackgroundSubtractorMOG2::getDetectShadows()
    public  boolean getDetectShadows()
    {
        
        boolean retVal = getDetectShadows_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::BackgroundSubtractorMOG2::getBackgroundRatio()
    //

    //javadoc: BackgroundSubtractorMOG2::getBackgroundRatio()
    public  double getBackgroundRatio()
    {
        
        double retVal = getBackgroundRatio_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::BackgroundSubtractorMOG2::getComplexityReductionThreshold()
    //

    //javadoc: BackgroundSubtractorMOG2::getComplexityReductionThreshold()
    public  double getComplexityReductionThreshold()
    {
        
        double retVal = getComplexityReductionThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::BackgroundSubtractorMOG2::getShadowThreshold()
    //

    //javadoc: BackgroundSubtractorMOG2::getShadowThreshold()
    public  double getShadowThreshold()
    {
        
        double retVal = getShadowThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::BackgroundSubtractorMOG2::getVarInit()
    //

    //javadoc: BackgroundSubtractorMOG2::getVarInit()
    public  double getVarInit()
    {
        
        double retVal = getVarInit_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::BackgroundSubtractorMOG2::getVarMax()
    //

    //javadoc: BackgroundSubtractorMOG2::getVarMax()
    public  double getVarMax()
    {
        
        double retVal = getVarMax_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::BackgroundSubtractorMOG2::getVarMin()
    //

    //javadoc: BackgroundSubtractorMOG2::getVarMin()
    public  double getVarMin()
    {
        
        double retVal = getVarMin_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::BackgroundSubtractorMOG2::getVarThreshold()
    //

    //javadoc: BackgroundSubtractorMOG2::getVarThreshold()
    public  double getVarThreshold()
    {
        
        double retVal = getVarThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::BackgroundSubtractorMOG2::getVarThresholdGen()
    //

    //javadoc: BackgroundSubtractorMOG2::getVarThresholdGen()
    public  double getVarThresholdGen()
    {
        
        double retVal = getVarThresholdGen_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::BackgroundSubtractorMOG2::getHistory()
    //

    //javadoc: BackgroundSubtractorMOG2::getHistory()
    public  int getHistory()
    {
        
        int retVal = getHistory_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::BackgroundSubtractorMOG2::getNMixtures()
    //

    //javadoc: BackgroundSubtractorMOG2::getNMixtures()
    public  int getNMixtures()
    {
        
        int retVal = getNMixtures_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::BackgroundSubtractorMOG2::getShadowValue()
    //

    //javadoc: BackgroundSubtractorMOG2::getShadowValue()
    public  int getShadowValue()
    {
        
        int retVal = getShadowValue_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::apply(Mat image, Mat& fgmask, double learningRate = -1)
    //

    //javadoc: BackgroundSubtractorMOG2::apply(image, fgmask, learningRate)
    public  void apply(Mat image, Mat fgmask, double learningRate)
    {
        
        apply_0(nativeObj, image.nativeObj, fgmask.nativeObj, learningRate);
        
        return;
    }

    //javadoc: BackgroundSubtractorMOG2::apply(image, fgmask)
    public  void apply(Mat image, Mat fgmask)
    {
        
        apply_1(nativeObj, image.nativeObj, fgmask.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::setBackgroundRatio(double ratio)
    //

    //javadoc: BackgroundSubtractorMOG2::setBackgroundRatio(ratio)
    public  void setBackgroundRatio(double ratio)
    {
        
        setBackgroundRatio_0(nativeObj, ratio);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::setComplexityReductionThreshold(double ct)
    //

    //javadoc: BackgroundSubtractorMOG2::setComplexityReductionThreshold(ct)
    public  void setComplexityReductionThreshold(double ct)
    {
        
        setComplexityReductionThreshold_0(nativeObj, ct);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::setDetectShadows(bool detectShadows)
    //

    //javadoc: BackgroundSubtractorMOG2::setDetectShadows(detectShadows)
    public  void setDetectShadows(boolean detectShadows)
    {
        
        setDetectShadows_0(nativeObj, detectShadows);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::setHistory(int history)
    //

    //javadoc: BackgroundSubtractorMOG2::setHistory(history)
    public  void setHistory(int history)
    {
        
        setHistory_0(nativeObj, history);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::setNMixtures(int nmixtures)
    //

    //javadoc: BackgroundSubtractorMOG2::setNMixtures(nmixtures)
    public  void setNMixtures(int nmixtures)
    {
        
        setNMixtures_0(nativeObj, nmixtures);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::setShadowThreshold(double threshold)
    //

    //javadoc: BackgroundSubtractorMOG2::setShadowThreshold(threshold)
    public  void setShadowThreshold(double threshold)
    {
        
        setShadowThreshold_0(nativeObj, threshold);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::setShadowValue(int value)
    //

    //javadoc: BackgroundSubtractorMOG2::setShadowValue(value)
    public  void setShadowValue(int value)
    {
        
        setShadowValue_0(nativeObj, value);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::setVarInit(double varInit)
    //

    //javadoc: BackgroundSubtractorMOG2::setVarInit(varInit)
    public  void setVarInit(double varInit)
    {
        
        setVarInit_0(nativeObj, varInit);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::setVarMax(double varMax)
    //

    //javadoc: BackgroundSubtractorMOG2::setVarMax(varMax)
    public  void setVarMax(double varMax)
    {
        
        setVarMax_0(nativeObj, varMax);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::setVarMin(double varMin)
    //

    //javadoc: BackgroundSubtractorMOG2::setVarMin(varMin)
    public  void setVarMin(double varMin)
    {
        
        setVarMin_0(nativeObj, varMin);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::setVarThreshold(double varThreshold)
    //

    //javadoc: BackgroundSubtractorMOG2::setVarThreshold(varThreshold)
    public  void setVarThreshold(double varThreshold)
    {
        
        setVarThreshold_0(nativeObj, varThreshold);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorMOG2::setVarThresholdGen(double varThresholdGen)
    //

    //javadoc: BackgroundSubtractorMOG2::setVarThresholdGen(varThresholdGen)
    public  void setVarThresholdGen(double varThresholdGen)
    {
        
        setVarThresholdGen_0(nativeObj, varThresholdGen);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  bool cv::BackgroundSubtractorMOG2::getDetectShadows()
    private static native boolean getDetectShadows_0(long nativeObj);

    // C++:  double cv::BackgroundSubtractorMOG2::getBackgroundRatio()
    private static native double getBackgroundRatio_0(long nativeObj);

    // C++:  double cv::BackgroundSubtractorMOG2::getComplexityReductionThreshold()
    private static native double getComplexityReductionThreshold_0(long nativeObj);

    // C++:  double cv::BackgroundSubtractorMOG2::getShadowThreshold()
    private static native double getShadowThreshold_0(long nativeObj);

    // C++:  double cv::BackgroundSubtractorMOG2::getVarInit()
    private static native double getVarInit_0(long nativeObj);

    // C++:  double cv::BackgroundSubtractorMOG2::getVarMax()
    private static native double getVarMax_0(long nativeObj);

    // C++:  double cv::BackgroundSubtractorMOG2::getVarMin()
    private static native double getVarMin_0(long nativeObj);

    // C++:  double cv::BackgroundSubtractorMOG2::getVarThreshold()
    private static native double getVarThreshold_0(long nativeObj);

    // C++:  double cv::BackgroundSubtractorMOG2::getVarThresholdGen()
    private static native double getVarThresholdGen_0(long nativeObj);

    // C++:  int cv::BackgroundSubtractorMOG2::getHistory()
    private static native int getHistory_0(long nativeObj);

    // C++:  int cv::BackgroundSubtractorMOG2::getNMixtures()
    private static native int getNMixtures_0(long nativeObj);

    // C++:  int cv::BackgroundSubtractorMOG2::getShadowValue()
    private static native int getShadowValue_0(long nativeObj);

    // C++:  void cv::BackgroundSubtractorMOG2::apply(Mat image, Mat& fgmask, double learningRate = -1)
    private static native void apply_0(long nativeObj, long image_nativeObj, long fgmask_nativeObj, double learningRate);
    private static native void apply_1(long nativeObj, long image_nativeObj, long fgmask_nativeObj);

    // C++:  void cv::BackgroundSubtractorMOG2::setBackgroundRatio(double ratio)
    private static native void setBackgroundRatio_0(long nativeObj, double ratio);

    // C++:  void cv::BackgroundSubtractorMOG2::setComplexityReductionThreshold(double ct)
    private static native void setComplexityReductionThreshold_0(long nativeObj, double ct);

    // C++:  void cv::BackgroundSubtractorMOG2::setDetectShadows(bool detectShadows)
    private static native void setDetectShadows_0(long nativeObj, boolean detectShadows);

    // C++:  void cv::BackgroundSubtractorMOG2::setHistory(int history)
    private static native void setHistory_0(long nativeObj, int history);

    // C++:  void cv::BackgroundSubtractorMOG2::setNMixtures(int nmixtures)
    private static native void setNMixtures_0(long nativeObj, int nmixtures);

    // C++:  void cv::BackgroundSubtractorMOG2::setShadowThreshold(double threshold)
    private static native void setShadowThreshold_0(long nativeObj, double threshold);

    // C++:  void cv::BackgroundSubtractorMOG2::setShadowValue(int value)
    private static native void setShadowValue_0(long nativeObj, int value);

    // C++:  void cv::BackgroundSubtractorMOG2::setVarInit(double varInit)
    private static native void setVarInit_0(long nativeObj, double varInit);

    // C++:  void cv::BackgroundSubtractorMOG2::setVarMax(double varMax)
    private static native void setVarMax_0(long nativeObj, double varMax);

    // C++:  void cv::BackgroundSubtractorMOG2::setVarMin(double varMin)
    private static native void setVarMin_0(long nativeObj, double varMin);

    // C++:  void cv::BackgroundSubtractorMOG2::setVarThreshold(double varThreshold)
    private static native void setVarThreshold_0(long nativeObj, double varThreshold);

    // C++:  void cv::BackgroundSubtractorMOG2::setVarThresholdGen(double varThresholdGen)
    private static native void setVarThresholdGen_0(long nativeObj, double varThresholdGen);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
