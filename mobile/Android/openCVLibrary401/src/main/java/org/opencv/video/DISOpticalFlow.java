//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;

import org.opencv.video.DISOpticalFlow;
import org.opencv.video.DenseOpticalFlow;

// C++: class DISOpticalFlow
//javadoc: DISOpticalFlow

public class DISOpticalFlow extends DenseOpticalFlow {

    protected DISOpticalFlow(long addr) { super(addr); }

    // internal usage only
    public static DISOpticalFlow __fromPtr__(long addr) { return new DISOpticalFlow(addr); }

    // C++: enum <unnamed>
    public static final int
            PRESET_ULTRAFAST = 0,
            PRESET_FAST = 1,
            PRESET_MEDIUM = 2;


    //
    // C++: static Ptr_DISOpticalFlow cv::DISOpticalFlow::create(int preset = DISOpticalFlow::PRESET_FAST)
    //

    //javadoc: DISOpticalFlow::create(preset)
    public static DISOpticalFlow create(int preset)
    {
        
        DISOpticalFlow retVal = DISOpticalFlow.__fromPtr__(create_0(preset));
        
        return retVal;
    }

    //javadoc: DISOpticalFlow::create()
    public static DISOpticalFlow create()
    {
        
        DISOpticalFlow retVal = DISOpticalFlow.__fromPtr__(create_1());
        
        return retVal;
    }


    //
    // C++:  bool cv::DISOpticalFlow::getUseMeanNormalization()
    //

    //javadoc: DISOpticalFlow::getUseMeanNormalization()
    public  boolean getUseMeanNormalization()
    {
        
        boolean retVal = getUseMeanNormalization_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::DISOpticalFlow::getUseSpatialPropagation()
    //

    //javadoc: DISOpticalFlow::getUseSpatialPropagation()
    public  boolean getUseSpatialPropagation()
    {
        
        boolean retVal = getUseSpatialPropagation_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::DISOpticalFlow::getVariationalRefinementAlpha()
    //

    //javadoc: DISOpticalFlow::getVariationalRefinementAlpha()
    public  float getVariationalRefinementAlpha()
    {
        
        float retVal = getVariationalRefinementAlpha_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::DISOpticalFlow::getVariationalRefinementDelta()
    //

    //javadoc: DISOpticalFlow::getVariationalRefinementDelta()
    public  float getVariationalRefinementDelta()
    {
        
        float retVal = getVariationalRefinementDelta_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::DISOpticalFlow::getVariationalRefinementGamma()
    //

    //javadoc: DISOpticalFlow::getVariationalRefinementGamma()
    public  float getVariationalRefinementGamma()
    {
        
        float retVal = getVariationalRefinementGamma_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::DISOpticalFlow::getFinestScale()
    //

    //javadoc: DISOpticalFlow::getFinestScale()
    public  int getFinestScale()
    {
        
        int retVal = getFinestScale_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::DISOpticalFlow::getGradientDescentIterations()
    //

    //javadoc: DISOpticalFlow::getGradientDescentIterations()
    public  int getGradientDescentIterations()
    {
        
        int retVal = getGradientDescentIterations_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::DISOpticalFlow::getPatchSize()
    //

    //javadoc: DISOpticalFlow::getPatchSize()
    public  int getPatchSize()
    {
        
        int retVal = getPatchSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::DISOpticalFlow::getPatchStride()
    //

    //javadoc: DISOpticalFlow::getPatchStride()
    public  int getPatchStride()
    {
        
        int retVal = getPatchStride_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::DISOpticalFlow::getVariationalRefinementIterations()
    //

    //javadoc: DISOpticalFlow::getVariationalRefinementIterations()
    public  int getVariationalRefinementIterations()
    {
        
        int retVal = getVariationalRefinementIterations_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::DISOpticalFlow::setFinestScale(int val)
    //

    //javadoc: DISOpticalFlow::setFinestScale(val)
    public  void setFinestScale(int val)
    {
        
        setFinestScale_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DISOpticalFlow::setGradientDescentIterations(int val)
    //

    //javadoc: DISOpticalFlow::setGradientDescentIterations(val)
    public  void setGradientDescentIterations(int val)
    {
        
        setGradientDescentIterations_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DISOpticalFlow::setPatchSize(int val)
    //

    //javadoc: DISOpticalFlow::setPatchSize(val)
    public  void setPatchSize(int val)
    {
        
        setPatchSize_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DISOpticalFlow::setPatchStride(int val)
    //

    //javadoc: DISOpticalFlow::setPatchStride(val)
    public  void setPatchStride(int val)
    {
        
        setPatchStride_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DISOpticalFlow::setUseMeanNormalization(bool val)
    //

    //javadoc: DISOpticalFlow::setUseMeanNormalization(val)
    public  void setUseMeanNormalization(boolean val)
    {
        
        setUseMeanNormalization_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DISOpticalFlow::setUseSpatialPropagation(bool val)
    //

    //javadoc: DISOpticalFlow::setUseSpatialPropagation(val)
    public  void setUseSpatialPropagation(boolean val)
    {
        
        setUseSpatialPropagation_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DISOpticalFlow::setVariationalRefinementAlpha(float val)
    //

    //javadoc: DISOpticalFlow::setVariationalRefinementAlpha(val)
    public  void setVariationalRefinementAlpha(float val)
    {
        
        setVariationalRefinementAlpha_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DISOpticalFlow::setVariationalRefinementDelta(float val)
    //

    //javadoc: DISOpticalFlow::setVariationalRefinementDelta(val)
    public  void setVariationalRefinementDelta(float val)
    {
        
        setVariationalRefinementDelta_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DISOpticalFlow::setVariationalRefinementGamma(float val)
    //

    //javadoc: DISOpticalFlow::setVariationalRefinementGamma(val)
    public  void setVariationalRefinementGamma(float val)
    {
        
        setVariationalRefinementGamma_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::DISOpticalFlow::setVariationalRefinementIterations(int val)
    //

    //javadoc: DISOpticalFlow::setVariationalRefinementIterations(val)
    public  void setVariationalRefinementIterations(int val)
    {
        
        setVariationalRefinementIterations_0(nativeObj, val);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_DISOpticalFlow cv::DISOpticalFlow::create(int preset = DISOpticalFlow::PRESET_FAST)
    private static native long create_0(int preset);
    private static native long create_1();

    // C++:  bool cv::DISOpticalFlow::getUseMeanNormalization()
    private static native boolean getUseMeanNormalization_0(long nativeObj);

    // C++:  bool cv::DISOpticalFlow::getUseSpatialPropagation()
    private static native boolean getUseSpatialPropagation_0(long nativeObj);

    // C++:  float cv::DISOpticalFlow::getVariationalRefinementAlpha()
    private static native float getVariationalRefinementAlpha_0(long nativeObj);

    // C++:  float cv::DISOpticalFlow::getVariationalRefinementDelta()
    private static native float getVariationalRefinementDelta_0(long nativeObj);

    // C++:  float cv::DISOpticalFlow::getVariationalRefinementGamma()
    private static native float getVariationalRefinementGamma_0(long nativeObj);

    // C++:  int cv::DISOpticalFlow::getFinestScale()
    private static native int getFinestScale_0(long nativeObj);

    // C++:  int cv::DISOpticalFlow::getGradientDescentIterations()
    private static native int getGradientDescentIterations_0(long nativeObj);

    // C++:  int cv::DISOpticalFlow::getPatchSize()
    private static native int getPatchSize_0(long nativeObj);

    // C++:  int cv::DISOpticalFlow::getPatchStride()
    private static native int getPatchStride_0(long nativeObj);

    // C++:  int cv::DISOpticalFlow::getVariationalRefinementIterations()
    private static native int getVariationalRefinementIterations_0(long nativeObj);

    // C++:  void cv::DISOpticalFlow::setFinestScale(int val)
    private static native void setFinestScale_0(long nativeObj, int val);

    // C++:  void cv::DISOpticalFlow::setGradientDescentIterations(int val)
    private static native void setGradientDescentIterations_0(long nativeObj, int val);

    // C++:  void cv::DISOpticalFlow::setPatchSize(int val)
    private static native void setPatchSize_0(long nativeObj, int val);

    // C++:  void cv::DISOpticalFlow::setPatchStride(int val)
    private static native void setPatchStride_0(long nativeObj, int val);

    // C++:  void cv::DISOpticalFlow::setUseMeanNormalization(bool val)
    private static native void setUseMeanNormalization_0(long nativeObj, boolean val);

    // C++:  void cv::DISOpticalFlow::setUseSpatialPropagation(bool val)
    private static native void setUseSpatialPropagation_0(long nativeObj, boolean val);

    // C++:  void cv::DISOpticalFlow::setVariationalRefinementAlpha(float val)
    private static native void setVariationalRefinementAlpha_0(long nativeObj, float val);

    // C++:  void cv::DISOpticalFlow::setVariationalRefinementDelta(float val)
    private static native void setVariationalRefinementDelta_0(long nativeObj, float val);

    // C++:  void cv::DISOpticalFlow::setVariationalRefinementGamma(float val)
    private static native void setVariationalRefinementGamma_0(long nativeObj, float val);

    // C++:  void cv::DISOpticalFlow::setVariationalRefinementIterations(int val)
    private static native void setVariationalRefinementIterations_0(long nativeObj, int val);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
