//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.ml;

import java.lang.String;
import org.opencv.core.Mat;
import org.opencv.ml.DTrees;
import org.opencv.ml.StatModel;

// C++: class DTrees
//javadoc: DTrees

public class DTrees extends StatModel {

    protected DTrees(long addr) { super(addr); }

    // internal usage only
    public static DTrees __fromPtr__(long addr) { return new DTrees(addr); }

    // C++: enum Flags
    public static final int
            PREDICT_AUTO = 0,
            PREDICT_SUM = (1<<8),
            PREDICT_MAX_VOTE = (2<<8),
            PREDICT_MASK = (3<<8);


    //
    // C++:  Mat cv::ml::DTrees::getPriors()
    //

    //javadoc: DTrees::getPriors()
    public  Mat getPriors()
    {
        
        Mat retVal = new Mat(getPriors_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: static Ptr_DTrees cv::ml::DTrees::create()
    //

    //javadoc: DTrees::create()
    public static DTrees create()
    {
        
        DTrees retVal = DTrees.__fromPtr__(create_0());
        
        return retVal;
    }


    //
    // C++: static Ptr_DTrees cv::ml::DTrees::load(String filepath, String nodeName = String())
    //

    //javadoc: DTrees::load(filepath, nodeName)
    public static DTrees load(String filepath, String nodeName)
    {
        
        DTrees retVal = DTrees.__fromPtr__(load_0(filepath, nodeName));
        
        return retVal;
    }

    //javadoc: DTrees::load(filepath)
    public static DTrees load(String filepath)
    {
        
        DTrees retVal = DTrees.__fromPtr__(load_1(filepath));
        
        return retVal;
    }


    //
    // C++:  bool cv::ml::DTrees::getTruncatePrunedTree()
    //

    //javadoc: DTrees::getTruncatePrunedTree()
    public  boolean getTruncatePrunedTree()
    {
        
        boolean retVal = getTruncatePrunedTree_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::ml::DTrees::getUse1SERule()
    //

    //javadoc: DTrees::getUse1SERule()
    public  boolean getUse1SERule()
    {
        
        boolean retVal = getUse1SERule_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::ml::DTrees::getUseSurrogates()
    //

    //javadoc: DTrees::getUseSurrogates()
    public  boolean getUseSurrogates()
    {
        
        boolean retVal = getUseSurrogates_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::ml::DTrees::getRegressionAccuracy()
    //

    //javadoc: DTrees::getRegressionAccuracy()
    public  float getRegressionAccuracy()
    {
        
        float retVal = getRegressionAccuracy_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::DTrees::getCVFolds()
    //

    //javadoc: DTrees::getCVFolds()
    public  int getCVFolds()
    {
        
        int retVal = getCVFolds_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::DTrees::getMaxCategories()
    //

    //javadoc: DTrees::getMaxCategories()
    public  int getMaxCategories()
    {
        
        int retVal = getMaxCategories_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::DTrees::getMaxDepth()
    //

    //javadoc: DTrees::getMaxDepth()
    public  int getMaxDepth()
    {
        
        int retVal = getMaxDepth_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::DTrees::getMinSampleCount()
    //

    //javadoc: DTrees::getMinSampleCount()
    public  int getMinSampleCount()
    {
        
        int retVal = getMinSampleCount_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::ml::DTrees::setCVFolds(int val)
    //

    //javadoc: DTrees::setCVFolds(val)
    public  void setCVFolds(int val)
    {
        
        setCVFolds_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::DTrees::setMaxCategories(int val)
    //

    //javadoc: DTrees::setMaxCategories(val)
    public  void setMaxCategories(int val)
    {
        
        setMaxCategories_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::DTrees::setMaxDepth(int val)
    //

    //javadoc: DTrees::setMaxDepth(val)
    public  void setMaxDepth(int val)
    {
        
        setMaxDepth_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::DTrees::setMinSampleCount(int val)
    //

    //javadoc: DTrees::setMinSampleCount(val)
    public  void setMinSampleCount(int val)
    {
        
        setMinSampleCount_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::DTrees::setPriors(Mat val)
    //

    //javadoc: DTrees::setPriors(val)
    public  void setPriors(Mat val)
    {
        
        setPriors_0(nativeObj, val.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::ml::DTrees::setRegressionAccuracy(float val)
    //

    //javadoc: DTrees::setRegressionAccuracy(val)
    public  void setRegressionAccuracy(float val)
    {
        
        setRegressionAccuracy_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::DTrees::setTruncatePrunedTree(bool val)
    //

    //javadoc: DTrees::setTruncatePrunedTree(val)
    public  void setTruncatePrunedTree(boolean val)
    {
        
        setTruncatePrunedTree_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::DTrees::setUse1SERule(bool val)
    //

    //javadoc: DTrees::setUse1SERule(val)
    public  void setUse1SERule(boolean val)
    {
        
        setUse1SERule_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::DTrees::setUseSurrogates(bool val)
    //

    //javadoc: DTrees::setUseSurrogates(val)
    public  void setUseSurrogates(boolean val)
    {
        
        setUseSurrogates_0(nativeObj, val);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  Mat cv::ml::DTrees::getPriors()
    private static native long getPriors_0(long nativeObj);

    // C++: static Ptr_DTrees cv::ml::DTrees::create()
    private static native long create_0();

    // C++: static Ptr_DTrees cv::ml::DTrees::load(String filepath, String nodeName = String())
    private static native long load_0(String filepath, String nodeName);
    private static native long load_1(String filepath);

    // C++:  bool cv::ml::DTrees::getTruncatePrunedTree()
    private static native boolean getTruncatePrunedTree_0(long nativeObj);

    // C++:  bool cv::ml::DTrees::getUse1SERule()
    private static native boolean getUse1SERule_0(long nativeObj);

    // C++:  bool cv::ml::DTrees::getUseSurrogates()
    private static native boolean getUseSurrogates_0(long nativeObj);

    // C++:  float cv::ml::DTrees::getRegressionAccuracy()
    private static native float getRegressionAccuracy_0(long nativeObj);

    // C++:  int cv::ml::DTrees::getCVFolds()
    private static native int getCVFolds_0(long nativeObj);

    // C++:  int cv::ml::DTrees::getMaxCategories()
    private static native int getMaxCategories_0(long nativeObj);

    // C++:  int cv::ml::DTrees::getMaxDepth()
    private static native int getMaxDepth_0(long nativeObj);

    // C++:  int cv::ml::DTrees::getMinSampleCount()
    private static native int getMinSampleCount_0(long nativeObj);

    // C++:  void cv::ml::DTrees::setCVFolds(int val)
    private static native void setCVFolds_0(long nativeObj, int val);

    // C++:  void cv::ml::DTrees::setMaxCategories(int val)
    private static native void setMaxCategories_0(long nativeObj, int val);

    // C++:  void cv::ml::DTrees::setMaxDepth(int val)
    private static native void setMaxDepth_0(long nativeObj, int val);

    // C++:  void cv::ml::DTrees::setMinSampleCount(int val)
    private static native void setMinSampleCount_0(long nativeObj, int val);

    // C++:  void cv::ml::DTrees::setPriors(Mat val)
    private static native void setPriors_0(long nativeObj, long val_nativeObj);

    // C++:  void cv::ml::DTrees::setRegressionAccuracy(float val)
    private static native void setRegressionAccuracy_0(long nativeObj, float val);

    // C++:  void cv::ml::DTrees::setTruncatePrunedTree(bool val)
    private static native void setTruncatePrunedTree_0(long nativeObj, boolean val);

    // C++:  void cv::ml::DTrees::setUse1SERule(bool val)
    private static native void setUse1SERule_0(long nativeObj, boolean val);

    // C++:  void cv::ml::DTrees::setUseSurrogates(bool val)
    private static native void setUseSurrogates_0(long nativeObj, boolean val);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
