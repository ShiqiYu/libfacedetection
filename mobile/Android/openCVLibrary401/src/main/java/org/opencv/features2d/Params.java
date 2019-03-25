//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;



// C++: class Params
//javadoc: Params

public class Params {

    protected final long nativeObj;
    protected Params(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static Params __fromPtr__(long addr) { return new Params(addr); }

    //
    // C++:   cv::SimpleBlobDetector::Params::Params()
    //

    //javadoc: Params::Params()
    public   Params()
    {
        
        nativeObj = Params_0();
        
        return;
    }


    //
    // C++: float Params::thresholdStep
    //

    //javadoc: Params::get_thresholdStep()
    public  float get_thresholdStep()
    {
        
        float retVal = get_thresholdStep_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::thresholdStep
    //

    //javadoc: Params::set_thresholdStep(thresholdStep)
    public  void set_thresholdStep(float thresholdStep)
    {
        
        set_thresholdStep_0(nativeObj, thresholdStep);
        
        return;
    }


    //
    // C++: float Params::minThreshold
    //

    //javadoc: Params::get_minThreshold()
    public  float get_minThreshold()
    {
        
        float retVal = get_minThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::minThreshold
    //

    //javadoc: Params::set_minThreshold(minThreshold)
    public  void set_minThreshold(float minThreshold)
    {
        
        set_minThreshold_0(nativeObj, minThreshold);
        
        return;
    }


    //
    // C++: float Params::maxThreshold
    //

    //javadoc: Params::get_maxThreshold()
    public  float get_maxThreshold()
    {
        
        float retVal = get_maxThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::maxThreshold
    //

    //javadoc: Params::set_maxThreshold(maxThreshold)
    public  void set_maxThreshold(float maxThreshold)
    {
        
        set_maxThreshold_0(nativeObj, maxThreshold);
        
        return;
    }


    //
    // C++: size_t Params::minRepeatability
    //

    //javadoc: Params::get_minRepeatability()
    public  long get_minRepeatability()
    {
        
        long retVal = get_minRepeatability_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::minRepeatability
    //

    //javadoc: Params::set_minRepeatability(minRepeatability)
    public  void set_minRepeatability(long minRepeatability)
    {
        
        set_minRepeatability_0(nativeObj, minRepeatability);
        
        return;
    }


    //
    // C++: float Params::minDistBetweenBlobs
    //

    //javadoc: Params::get_minDistBetweenBlobs()
    public  float get_minDistBetweenBlobs()
    {
        
        float retVal = get_minDistBetweenBlobs_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::minDistBetweenBlobs
    //

    //javadoc: Params::set_minDistBetweenBlobs(minDistBetweenBlobs)
    public  void set_minDistBetweenBlobs(float minDistBetweenBlobs)
    {
        
        set_minDistBetweenBlobs_0(nativeObj, minDistBetweenBlobs);
        
        return;
    }


    //
    // C++: bool Params::filterByColor
    //

    //javadoc: Params::get_filterByColor()
    public  boolean get_filterByColor()
    {
        
        boolean retVal = get_filterByColor_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::filterByColor
    //

    //javadoc: Params::set_filterByColor(filterByColor)
    public  void set_filterByColor(boolean filterByColor)
    {
        
        set_filterByColor_0(nativeObj, filterByColor);
        
        return;
    }


    //
    // C++: uchar Params::blobColor
    //

    // Return type 'uchar' is not supported, skipping the function


    //
    // C++: void Params::blobColor
    //

    // Unknown type 'uchar' (I), skipping the function


    //
    // C++: bool Params::filterByArea
    //

    //javadoc: Params::get_filterByArea()
    public  boolean get_filterByArea()
    {
        
        boolean retVal = get_filterByArea_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::filterByArea
    //

    //javadoc: Params::set_filterByArea(filterByArea)
    public  void set_filterByArea(boolean filterByArea)
    {
        
        set_filterByArea_0(nativeObj, filterByArea);
        
        return;
    }


    //
    // C++: float Params::minArea
    //

    //javadoc: Params::get_minArea()
    public  float get_minArea()
    {
        
        float retVal = get_minArea_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::minArea
    //

    //javadoc: Params::set_minArea(minArea)
    public  void set_minArea(float minArea)
    {
        
        set_minArea_0(nativeObj, minArea);
        
        return;
    }


    //
    // C++: float Params::maxArea
    //

    //javadoc: Params::get_maxArea()
    public  float get_maxArea()
    {
        
        float retVal = get_maxArea_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::maxArea
    //

    //javadoc: Params::set_maxArea(maxArea)
    public  void set_maxArea(float maxArea)
    {
        
        set_maxArea_0(nativeObj, maxArea);
        
        return;
    }


    //
    // C++: bool Params::filterByCircularity
    //

    //javadoc: Params::get_filterByCircularity()
    public  boolean get_filterByCircularity()
    {
        
        boolean retVal = get_filterByCircularity_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::filterByCircularity
    //

    //javadoc: Params::set_filterByCircularity(filterByCircularity)
    public  void set_filterByCircularity(boolean filterByCircularity)
    {
        
        set_filterByCircularity_0(nativeObj, filterByCircularity);
        
        return;
    }


    //
    // C++: float Params::minCircularity
    //

    //javadoc: Params::get_minCircularity()
    public  float get_minCircularity()
    {
        
        float retVal = get_minCircularity_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::minCircularity
    //

    //javadoc: Params::set_minCircularity(minCircularity)
    public  void set_minCircularity(float minCircularity)
    {
        
        set_minCircularity_0(nativeObj, minCircularity);
        
        return;
    }


    //
    // C++: float Params::maxCircularity
    //

    //javadoc: Params::get_maxCircularity()
    public  float get_maxCircularity()
    {
        
        float retVal = get_maxCircularity_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::maxCircularity
    //

    //javadoc: Params::set_maxCircularity(maxCircularity)
    public  void set_maxCircularity(float maxCircularity)
    {
        
        set_maxCircularity_0(nativeObj, maxCircularity);
        
        return;
    }


    //
    // C++: bool Params::filterByInertia
    //

    //javadoc: Params::get_filterByInertia()
    public  boolean get_filterByInertia()
    {
        
        boolean retVal = get_filterByInertia_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::filterByInertia
    //

    //javadoc: Params::set_filterByInertia(filterByInertia)
    public  void set_filterByInertia(boolean filterByInertia)
    {
        
        set_filterByInertia_0(nativeObj, filterByInertia);
        
        return;
    }


    //
    // C++: float Params::minInertiaRatio
    //

    //javadoc: Params::get_minInertiaRatio()
    public  float get_minInertiaRatio()
    {
        
        float retVal = get_minInertiaRatio_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::minInertiaRatio
    //

    //javadoc: Params::set_minInertiaRatio(minInertiaRatio)
    public  void set_minInertiaRatio(float minInertiaRatio)
    {
        
        set_minInertiaRatio_0(nativeObj, minInertiaRatio);
        
        return;
    }


    //
    // C++: float Params::maxInertiaRatio
    //

    //javadoc: Params::get_maxInertiaRatio()
    public  float get_maxInertiaRatio()
    {
        
        float retVal = get_maxInertiaRatio_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::maxInertiaRatio
    //

    //javadoc: Params::set_maxInertiaRatio(maxInertiaRatio)
    public  void set_maxInertiaRatio(float maxInertiaRatio)
    {
        
        set_maxInertiaRatio_0(nativeObj, maxInertiaRatio);
        
        return;
    }


    //
    // C++: bool Params::filterByConvexity
    //

    //javadoc: Params::get_filterByConvexity()
    public  boolean get_filterByConvexity()
    {
        
        boolean retVal = get_filterByConvexity_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::filterByConvexity
    //

    //javadoc: Params::set_filterByConvexity(filterByConvexity)
    public  void set_filterByConvexity(boolean filterByConvexity)
    {
        
        set_filterByConvexity_0(nativeObj, filterByConvexity);
        
        return;
    }


    //
    // C++: float Params::minConvexity
    //

    //javadoc: Params::get_minConvexity()
    public  float get_minConvexity()
    {
        
        float retVal = get_minConvexity_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::minConvexity
    //

    //javadoc: Params::set_minConvexity(minConvexity)
    public  void set_minConvexity(float minConvexity)
    {
        
        set_minConvexity_0(nativeObj, minConvexity);
        
        return;
    }


    //
    // C++: float Params::maxConvexity
    //

    //javadoc: Params::get_maxConvexity()
    public  float get_maxConvexity()
    {
        
        float retVal = get_maxConvexity_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: void Params::maxConvexity
    //

    //javadoc: Params::set_maxConvexity(maxConvexity)
    public  void set_maxConvexity(float maxConvexity)
    {
        
        set_maxConvexity_0(nativeObj, maxConvexity);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::SimpleBlobDetector::Params::Params()
    private static native long Params_0();

    // C++: float Params::thresholdStep
    private static native float get_thresholdStep_0(long nativeObj);

    // C++: void Params::thresholdStep
    private static native void set_thresholdStep_0(long nativeObj, float thresholdStep);

    // C++: float Params::minThreshold
    private static native float get_minThreshold_0(long nativeObj);

    // C++: void Params::minThreshold
    private static native void set_minThreshold_0(long nativeObj, float minThreshold);

    // C++: float Params::maxThreshold
    private static native float get_maxThreshold_0(long nativeObj);

    // C++: void Params::maxThreshold
    private static native void set_maxThreshold_0(long nativeObj, float maxThreshold);

    // C++: size_t Params::minRepeatability
    private static native long get_minRepeatability_0(long nativeObj);

    // C++: void Params::minRepeatability
    private static native void set_minRepeatability_0(long nativeObj, long minRepeatability);

    // C++: float Params::minDistBetweenBlobs
    private static native float get_minDistBetweenBlobs_0(long nativeObj);

    // C++: void Params::minDistBetweenBlobs
    private static native void set_minDistBetweenBlobs_0(long nativeObj, float minDistBetweenBlobs);

    // C++: bool Params::filterByColor
    private static native boolean get_filterByColor_0(long nativeObj);

    // C++: void Params::filterByColor
    private static native void set_filterByColor_0(long nativeObj, boolean filterByColor);

    // C++: bool Params::filterByArea
    private static native boolean get_filterByArea_0(long nativeObj);

    // C++: void Params::filterByArea
    private static native void set_filterByArea_0(long nativeObj, boolean filterByArea);

    // C++: float Params::minArea
    private static native float get_minArea_0(long nativeObj);

    // C++: void Params::minArea
    private static native void set_minArea_0(long nativeObj, float minArea);

    // C++: float Params::maxArea
    private static native float get_maxArea_0(long nativeObj);

    // C++: void Params::maxArea
    private static native void set_maxArea_0(long nativeObj, float maxArea);

    // C++: bool Params::filterByCircularity
    private static native boolean get_filterByCircularity_0(long nativeObj);

    // C++: void Params::filterByCircularity
    private static native void set_filterByCircularity_0(long nativeObj, boolean filterByCircularity);

    // C++: float Params::minCircularity
    private static native float get_minCircularity_0(long nativeObj);

    // C++: void Params::minCircularity
    private static native void set_minCircularity_0(long nativeObj, float minCircularity);

    // C++: float Params::maxCircularity
    private static native float get_maxCircularity_0(long nativeObj);

    // C++: void Params::maxCircularity
    private static native void set_maxCircularity_0(long nativeObj, float maxCircularity);

    // C++: bool Params::filterByInertia
    private static native boolean get_filterByInertia_0(long nativeObj);

    // C++: void Params::filterByInertia
    private static native void set_filterByInertia_0(long nativeObj, boolean filterByInertia);

    // C++: float Params::minInertiaRatio
    private static native float get_minInertiaRatio_0(long nativeObj);

    // C++: void Params::minInertiaRatio
    private static native void set_minInertiaRatio_0(long nativeObj, float minInertiaRatio);

    // C++: float Params::maxInertiaRatio
    private static native float get_maxInertiaRatio_0(long nativeObj);

    // C++: void Params::maxInertiaRatio
    private static native void set_maxInertiaRatio_0(long nativeObj, float maxInertiaRatio);

    // C++: bool Params::filterByConvexity
    private static native boolean get_filterByConvexity_0(long nativeObj);

    // C++: void Params::filterByConvexity
    private static native void set_filterByConvexity_0(long nativeObj, boolean filterByConvexity);

    // C++: float Params::minConvexity
    private static native float get_minConvexity_0(long nativeObj);

    // C++: void Params::minConvexity
    private static native void set_minConvexity_0(long nativeObj, float minConvexity);

    // C++: float Params::maxConvexity
    private static native float get_maxConvexity_0(long nativeObj);

    // C++: void Params::maxConvexity
    private static native void set_maxConvexity_0(long nativeObj, float maxConvexity);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
