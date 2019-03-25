//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;

import java.lang.String;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.features2d.Feature2D;

// C++: class FastFeatureDetector
//javadoc: FastFeatureDetector

public class FastFeatureDetector extends Feature2D {

    protected FastFeatureDetector(long addr) { super(addr); }

    // internal usage only
    public static FastFeatureDetector __fromPtr__(long addr) { return new FastFeatureDetector(addr); }

    // C++: enum DetectorType
    public static final int
            TYPE_5_8 = 0,
            TYPE_7_12 = 1,
            TYPE_9_16 = 2;


    // C++: enum <unnamed>
    public static final int
            THRESHOLD = 10000,
            NONMAX_SUPPRESSION = 10001,
            FAST_N = 10002;


    //
    // C++:  FastFeatureDetector_DetectorType cv::FastFeatureDetector::getType()
    //

    //javadoc: FastFeatureDetector::getType()
    public  int getType()
    {
        
        int retVal = getType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: static Ptr_FastFeatureDetector cv::FastFeatureDetector::create(int threshold = 10, bool nonmaxSuppression = true, FastFeatureDetector_DetectorType type = FastFeatureDetector::TYPE_9_16)
    //

    //javadoc: FastFeatureDetector::create(threshold, nonmaxSuppression, type)
    public static FastFeatureDetector create(int threshold, boolean nonmaxSuppression, int type)
    {
        
        FastFeatureDetector retVal = FastFeatureDetector.__fromPtr__(create_0(threshold, nonmaxSuppression, type));
        
        return retVal;
    }

    //javadoc: FastFeatureDetector::create(threshold, nonmaxSuppression)
    public static FastFeatureDetector create(int threshold, boolean nonmaxSuppression)
    {
        
        FastFeatureDetector retVal = FastFeatureDetector.__fromPtr__(create_1(threshold, nonmaxSuppression));
        
        return retVal;
    }

    //javadoc: FastFeatureDetector::create(threshold)
    public static FastFeatureDetector create(int threshold)
    {
        
        FastFeatureDetector retVal = FastFeatureDetector.__fromPtr__(create_2(threshold));
        
        return retVal;
    }

    //javadoc: FastFeatureDetector::create()
    public static FastFeatureDetector create()
    {
        
        FastFeatureDetector retVal = FastFeatureDetector.__fromPtr__(create_3());
        
        return retVal;
    }


    //
    // C++:  String cv::FastFeatureDetector::getDefaultName()
    //

    //javadoc: FastFeatureDetector::getDefaultName()
    public  String getDefaultName()
    {
        
        String retVal = getDefaultName_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::FastFeatureDetector::getNonmaxSuppression()
    //

    //javadoc: FastFeatureDetector::getNonmaxSuppression()
    public  boolean getNonmaxSuppression()
    {
        
        boolean retVal = getNonmaxSuppression_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::FastFeatureDetector::getThreshold()
    //

    //javadoc: FastFeatureDetector::getThreshold()
    public  int getThreshold()
    {
        
        int retVal = getThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::FastFeatureDetector::setNonmaxSuppression(bool f)
    //

    //javadoc: FastFeatureDetector::setNonmaxSuppression(f)
    public  void setNonmaxSuppression(boolean f)
    {
        
        setNonmaxSuppression_0(nativeObj, f);
        
        return;
    }


    //
    // C++:  void cv::FastFeatureDetector::setThreshold(int threshold)
    //

    //javadoc: FastFeatureDetector::setThreshold(threshold)
    public  void setThreshold(int threshold)
    {
        
        setThreshold_0(nativeObj, threshold);
        
        return;
    }


    //
    // C++:  void cv::FastFeatureDetector::setType(FastFeatureDetector_DetectorType type)
    //

    //javadoc: FastFeatureDetector::setType(type)
    public  void setType(int type)
    {
        
        setType_0(nativeObj, type);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  FastFeatureDetector_DetectorType cv::FastFeatureDetector::getType()
    private static native int getType_0(long nativeObj);

    // C++: static Ptr_FastFeatureDetector cv::FastFeatureDetector::create(int threshold = 10, bool nonmaxSuppression = true, FastFeatureDetector_DetectorType type = FastFeatureDetector::TYPE_9_16)
    private static native long create_0(int threshold, boolean nonmaxSuppression, int type);
    private static native long create_1(int threshold, boolean nonmaxSuppression);
    private static native long create_2(int threshold);
    private static native long create_3();

    // C++:  String cv::FastFeatureDetector::getDefaultName()
    private static native String getDefaultName_0(long nativeObj);

    // C++:  bool cv::FastFeatureDetector::getNonmaxSuppression()
    private static native boolean getNonmaxSuppression_0(long nativeObj);

    // C++:  int cv::FastFeatureDetector::getThreshold()
    private static native int getThreshold_0(long nativeObj);

    // C++:  void cv::FastFeatureDetector::setNonmaxSuppression(bool f)
    private static native void setNonmaxSuppression_0(long nativeObj, boolean f);

    // C++:  void cv::FastFeatureDetector::setThreshold(int threshold)
    private static native void setThreshold_0(long nativeObj, int threshold);

    // C++:  void cv::FastFeatureDetector::setType(FastFeatureDetector_DetectorType type)
    private static native void setType_0(long nativeObj, int type);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
