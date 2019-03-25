//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;

import java.lang.String;
import org.opencv.features2d.AgastFeatureDetector;
import org.opencv.features2d.Feature2D;

// C++: class AgastFeatureDetector
//javadoc: AgastFeatureDetector

public class AgastFeatureDetector extends Feature2D {

    protected AgastFeatureDetector(long addr) { super(addr); }

    // internal usage only
    public static AgastFeatureDetector __fromPtr__(long addr) { return new AgastFeatureDetector(addr); }

    // C++: enum <unnamed>
    public static final int
            THRESHOLD = 10000,
            NONMAX_SUPPRESSION = 10001;


    // C++: enum DetectorType
    public static final int
            AGAST_5_8 = 0,
            AGAST_7_12d = 1,
            AGAST_7_12s = 2,
            OAST_9_16 = 3;


    //
    // C++:  AgastFeatureDetector_DetectorType cv::AgastFeatureDetector::getType()
    //

    //javadoc: AgastFeatureDetector::getType()
    public  int getType()
    {
        
        int retVal = getType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: static Ptr_AgastFeatureDetector cv::AgastFeatureDetector::create(int threshold = 10, bool nonmaxSuppression = true, AgastFeatureDetector_DetectorType type = AgastFeatureDetector::OAST_9_16)
    //

    //javadoc: AgastFeatureDetector::create(threshold, nonmaxSuppression, type)
    public static AgastFeatureDetector create(int threshold, boolean nonmaxSuppression, int type)
    {
        
        AgastFeatureDetector retVal = AgastFeatureDetector.__fromPtr__(create_0(threshold, nonmaxSuppression, type));
        
        return retVal;
    }

    //javadoc: AgastFeatureDetector::create(threshold, nonmaxSuppression)
    public static AgastFeatureDetector create(int threshold, boolean nonmaxSuppression)
    {
        
        AgastFeatureDetector retVal = AgastFeatureDetector.__fromPtr__(create_1(threshold, nonmaxSuppression));
        
        return retVal;
    }

    //javadoc: AgastFeatureDetector::create(threshold)
    public static AgastFeatureDetector create(int threshold)
    {
        
        AgastFeatureDetector retVal = AgastFeatureDetector.__fromPtr__(create_2(threshold));
        
        return retVal;
    }

    //javadoc: AgastFeatureDetector::create()
    public static AgastFeatureDetector create()
    {
        
        AgastFeatureDetector retVal = AgastFeatureDetector.__fromPtr__(create_3());
        
        return retVal;
    }


    //
    // C++:  String cv::AgastFeatureDetector::getDefaultName()
    //

    //javadoc: AgastFeatureDetector::getDefaultName()
    public  String getDefaultName()
    {
        
        String retVal = getDefaultName_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::AgastFeatureDetector::getNonmaxSuppression()
    //

    //javadoc: AgastFeatureDetector::getNonmaxSuppression()
    public  boolean getNonmaxSuppression()
    {
        
        boolean retVal = getNonmaxSuppression_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::AgastFeatureDetector::getThreshold()
    //

    //javadoc: AgastFeatureDetector::getThreshold()
    public  int getThreshold()
    {
        
        int retVal = getThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::AgastFeatureDetector::setNonmaxSuppression(bool f)
    //

    //javadoc: AgastFeatureDetector::setNonmaxSuppression(f)
    public  void setNonmaxSuppression(boolean f)
    {
        
        setNonmaxSuppression_0(nativeObj, f);
        
        return;
    }


    //
    // C++:  void cv::AgastFeatureDetector::setThreshold(int threshold)
    //

    //javadoc: AgastFeatureDetector::setThreshold(threshold)
    public  void setThreshold(int threshold)
    {
        
        setThreshold_0(nativeObj, threshold);
        
        return;
    }


    //
    // C++:  void cv::AgastFeatureDetector::setType(AgastFeatureDetector_DetectorType type)
    //

    //javadoc: AgastFeatureDetector::setType(type)
    public  void setType(int type)
    {
        
        setType_0(nativeObj, type);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  AgastFeatureDetector_DetectorType cv::AgastFeatureDetector::getType()
    private static native int getType_0(long nativeObj);

    // C++: static Ptr_AgastFeatureDetector cv::AgastFeatureDetector::create(int threshold = 10, bool nonmaxSuppression = true, AgastFeatureDetector_DetectorType type = AgastFeatureDetector::OAST_9_16)
    private static native long create_0(int threshold, boolean nonmaxSuppression, int type);
    private static native long create_1(int threshold, boolean nonmaxSuppression);
    private static native long create_2(int threshold);
    private static native long create_3();

    // C++:  String cv::AgastFeatureDetector::getDefaultName()
    private static native String getDefaultName_0(long nativeObj);

    // C++:  bool cv::AgastFeatureDetector::getNonmaxSuppression()
    private static native boolean getNonmaxSuppression_0(long nativeObj);

    // C++:  int cv::AgastFeatureDetector::getThreshold()
    private static native int getThreshold_0(long nativeObj);

    // C++:  void cv::AgastFeatureDetector::setNonmaxSuppression(bool f)
    private static native void setNonmaxSuppression_0(long nativeObj, boolean f);

    // C++:  void cv::AgastFeatureDetector::setThreshold(int threshold)
    private static native void setThreshold_0(long nativeObj, int threshold);

    // C++:  void cv::AgastFeatureDetector::setType(AgastFeatureDetector_DetectorType type)
    private static native void setType_0(long nativeObj, int type);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
