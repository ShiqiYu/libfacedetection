//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;

import org.opencv.video.BackgroundSubtractor;

// C++: class BackgroundSubtractorKNN
//javadoc: BackgroundSubtractorKNN

public class BackgroundSubtractorKNN extends BackgroundSubtractor {

    protected BackgroundSubtractorKNN(long addr) { super(addr); }

    // internal usage only
    public static BackgroundSubtractorKNN __fromPtr__(long addr) { return new BackgroundSubtractorKNN(addr); }

    //
    // C++:  bool cv::BackgroundSubtractorKNN::getDetectShadows()
    //

    //javadoc: BackgroundSubtractorKNN::getDetectShadows()
    public  boolean getDetectShadows()
    {
        
        boolean retVal = getDetectShadows_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::BackgroundSubtractorKNN::getDist2Threshold()
    //

    //javadoc: BackgroundSubtractorKNN::getDist2Threshold()
    public  double getDist2Threshold()
    {
        
        double retVal = getDist2Threshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::BackgroundSubtractorKNN::getShadowThreshold()
    //

    //javadoc: BackgroundSubtractorKNN::getShadowThreshold()
    public  double getShadowThreshold()
    {
        
        double retVal = getShadowThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::BackgroundSubtractorKNN::getHistory()
    //

    //javadoc: BackgroundSubtractorKNN::getHistory()
    public  int getHistory()
    {
        
        int retVal = getHistory_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::BackgroundSubtractorKNN::getNSamples()
    //

    //javadoc: BackgroundSubtractorKNN::getNSamples()
    public  int getNSamples()
    {
        
        int retVal = getNSamples_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::BackgroundSubtractorKNN::getShadowValue()
    //

    //javadoc: BackgroundSubtractorKNN::getShadowValue()
    public  int getShadowValue()
    {
        
        int retVal = getShadowValue_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::BackgroundSubtractorKNN::getkNNSamples()
    //

    //javadoc: BackgroundSubtractorKNN::getkNNSamples()
    public  int getkNNSamples()
    {
        
        int retVal = getkNNSamples_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::BackgroundSubtractorKNN::setDetectShadows(bool detectShadows)
    //

    //javadoc: BackgroundSubtractorKNN::setDetectShadows(detectShadows)
    public  void setDetectShadows(boolean detectShadows)
    {
        
        setDetectShadows_0(nativeObj, detectShadows);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorKNN::setDist2Threshold(double _dist2Threshold)
    //

    //javadoc: BackgroundSubtractorKNN::setDist2Threshold(_dist2Threshold)
    public  void setDist2Threshold(double _dist2Threshold)
    {
        
        setDist2Threshold_0(nativeObj, _dist2Threshold);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorKNN::setHistory(int history)
    //

    //javadoc: BackgroundSubtractorKNN::setHistory(history)
    public  void setHistory(int history)
    {
        
        setHistory_0(nativeObj, history);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorKNN::setNSamples(int _nN)
    //

    //javadoc: BackgroundSubtractorKNN::setNSamples(_nN)
    public  void setNSamples(int _nN)
    {
        
        setNSamples_0(nativeObj, _nN);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorKNN::setShadowThreshold(double threshold)
    //

    //javadoc: BackgroundSubtractorKNN::setShadowThreshold(threshold)
    public  void setShadowThreshold(double threshold)
    {
        
        setShadowThreshold_0(nativeObj, threshold);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorKNN::setShadowValue(int value)
    //

    //javadoc: BackgroundSubtractorKNN::setShadowValue(value)
    public  void setShadowValue(int value)
    {
        
        setShadowValue_0(nativeObj, value);
        
        return;
    }


    //
    // C++:  void cv::BackgroundSubtractorKNN::setkNNSamples(int _nkNN)
    //

    //javadoc: BackgroundSubtractorKNN::setkNNSamples(_nkNN)
    public  void setkNNSamples(int _nkNN)
    {
        
        setkNNSamples_0(nativeObj, _nkNN);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  bool cv::BackgroundSubtractorKNN::getDetectShadows()
    private static native boolean getDetectShadows_0(long nativeObj);

    // C++:  double cv::BackgroundSubtractorKNN::getDist2Threshold()
    private static native double getDist2Threshold_0(long nativeObj);

    // C++:  double cv::BackgroundSubtractorKNN::getShadowThreshold()
    private static native double getShadowThreshold_0(long nativeObj);

    // C++:  int cv::BackgroundSubtractorKNN::getHistory()
    private static native int getHistory_0(long nativeObj);

    // C++:  int cv::BackgroundSubtractorKNN::getNSamples()
    private static native int getNSamples_0(long nativeObj);

    // C++:  int cv::BackgroundSubtractorKNN::getShadowValue()
    private static native int getShadowValue_0(long nativeObj);

    // C++:  int cv::BackgroundSubtractorKNN::getkNNSamples()
    private static native int getkNNSamples_0(long nativeObj);

    // C++:  void cv::BackgroundSubtractorKNN::setDetectShadows(bool detectShadows)
    private static native void setDetectShadows_0(long nativeObj, boolean detectShadows);

    // C++:  void cv::BackgroundSubtractorKNN::setDist2Threshold(double _dist2Threshold)
    private static native void setDist2Threshold_0(long nativeObj, double _dist2Threshold);

    // C++:  void cv::BackgroundSubtractorKNN::setHistory(int history)
    private static native void setHistory_0(long nativeObj, int history);

    // C++:  void cv::BackgroundSubtractorKNN::setNSamples(int _nN)
    private static native void setNSamples_0(long nativeObj, int _nN);

    // C++:  void cv::BackgroundSubtractorKNN::setShadowThreshold(double threshold)
    private static native void setShadowThreshold_0(long nativeObj, double threshold);

    // C++:  void cv::BackgroundSubtractorKNN::setShadowValue(int value)
    private static native void setShadowValue_0(long nativeObj, int value);

    // C++:  void cv::BackgroundSubtractorKNN::setkNNSamples(int _nkNN)
    private static native void setkNNSamples_0(long nativeObj, int _nkNN);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
