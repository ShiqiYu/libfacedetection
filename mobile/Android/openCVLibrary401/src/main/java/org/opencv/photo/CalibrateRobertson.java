//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.photo;

import org.opencv.core.Mat;
import org.opencv.photo.CalibrateCRF;

// C++: class CalibrateRobertson
//javadoc: CalibrateRobertson

public class CalibrateRobertson extends CalibrateCRF {

    protected CalibrateRobertson(long addr) { super(addr); }

    // internal usage only
    public static CalibrateRobertson __fromPtr__(long addr) { return new CalibrateRobertson(addr); }

    //
    // C++:  Mat cv::CalibrateRobertson::getRadiance()
    //

    //javadoc: CalibrateRobertson::getRadiance()
    public  Mat getRadiance()
    {
        
        Mat retVal = new Mat(getRadiance_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  float cv::CalibrateRobertson::getThreshold()
    //

    //javadoc: CalibrateRobertson::getThreshold()
    public  float getThreshold()
    {
        
        float retVal = getThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::CalibrateRobertson::getMaxIter()
    //

    //javadoc: CalibrateRobertson::getMaxIter()
    public  int getMaxIter()
    {
        
        int retVal = getMaxIter_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::CalibrateRobertson::setMaxIter(int max_iter)
    //

    //javadoc: CalibrateRobertson::setMaxIter(max_iter)
    public  void setMaxIter(int max_iter)
    {
        
        setMaxIter_0(nativeObj, max_iter);
        
        return;
    }


    //
    // C++:  void cv::CalibrateRobertson::setThreshold(float threshold)
    //

    //javadoc: CalibrateRobertson::setThreshold(threshold)
    public  void setThreshold(float threshold)
    {
        
        setThreshold_0(nativeObj, threshold);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  Mat cv::CalibrateRobertson::getRadiance()
    private static native long getRadiance_0(long nativeObj);

    // C++:  float cv::CalibrateRobertson::getThreshold()
    private static native float getThreshold_0(long nativeObj);

    // C++:  int cv::CalibrateRobertson::getMaxIter()
    private static native int getMaxIter_0(long nativeObj);

    // C++:  void cv::CalibrateRobertson::setMaxIter(int max_iter)
    private static native void setMaxIter_0(long nativeObj, int max_iter);

    // C++:  void cv::CalibrateRobertson::setThreshold(float threshold)
    private static native void setThreshold_0(long nativeObj, float threshold);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
