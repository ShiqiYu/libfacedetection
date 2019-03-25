//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;

import java.lang.String;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.KAZE;

// C++: class KAZE
//javadoc: KAZE

public class KAZE extends Feature2D {

    protected KAZE(long addr) { super(addr); }

    // internal usage only
    public static KAZE __fromPtr__(long addr) { return new KAZE(addr); }

    // C++: enum DiffusivityType
    public static final int
            DIFF_PM_G1 = 0,
            DIFF_PM_G2 = 1,
            DIFF_WEICKERT = 2,
            DIFF_CHARBONNIER = 3;


    //
    // C++:  KAZE_DiffusivityType cv::KAZE::getDiffusivity()
    //

    //javadoc: KAZE::getDiffusivity()
    public  int getDiffusivity()
    {
        
        int retVal = getDiffusivity_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: static Ptr_KAZE cv::KAZE::create(bool extended = false, bool upright = false, float threshold = 0.001f, int nOctaves = 4, int nOctaveLayers = 4, KAZE_DiffusivityType diffusivity = KAZE::DIFF_PM_G2)
    //

    //javadoc: KAZE::create(extended, upright, threshold, nOctaves, nOctaveLayers, diffusivity)
    public static KAZE create(boolean extended, boolean upright, float threshold, int nOctaves, int nOctaveLayers, int diffusivity)
    {
        
        KAZE retVal = KAZE.__fromPtr__(create_0(extended, upright, threshold, nOctaves, nOctaveLayers, diffusivity));
        
        return retVal;
    }

    //javadoc: KAZE::create(extended, upright, threshold, nOctaves, nOctaveLayers)
    public static KAZE create(boolean extended, boolean upright, float threshold, int nOctaves, int nOctaveLayers)
    {
        
        KAZE retVal = KAZE.__fromPtr__(create_1(extended, upright, threshold, nOctaves, nOctaveLayers));
        
        return retVal;
    }

    //javadoc: KAZE::create(extended, upright, threshold, nOctaves)
    public static KAZE create(boolean extended, boolean upright, float threshold, int nOctaves)
    {
        
        KAZE retVal = KAZE.__fromPtr__(create_2(extended, upright, threshold, nOctaves));
        
        return retVal;
    }

    //javadoc: KAZE::create(extended, upright, threshold)
    public static KAZE create(boolean extended, boolean upright, float threshold)
    {
        
        KAZE retVal = KAZE.__fromPtr__(create_3(extended, upright, threshold));
        
        return retVal;
    }

    //javadoc: KAZE::create(extended, upright)
    public static KAZE create(boolean extended, boolean upright)
    {
        
        KAZE retVal = KAZE.__fromPtr__(create_4(extended, upright));
        
        return retVal;
    }

    //javadoc: KAZE::create(extended)
    public static KAZE create(boolean extended)
    {
        
        KAZE retVal = KAZE.__fromPtr__(create_5(extended));
        
        return retVal;
    }

    //javadoc: KAZE::create()
    public static KAZE create()
    {
        
        KAZE retVal = KAZE.__fromPtr__(create_6());
        
        return retVal;
    }


    //
    // C++:  String cv::KAZE::getDefaultName()
    //

    //javadoc: KAZE::getDefaultName()
    public  String getDefaultName()
    {
        
        String retVal = getDefaultName_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::KAZE::getExtended()
    //

    //javadoc: KAZE::getExtended()
    public  boolean getExtended()
    {
        
        boolean retVal = getExtended_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::KAZE::getUpright()
    //

    //javadoc: KAZE::getUpright()
    public  boolean getUpright()
    {
        
        boolean retVal = getUpright_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::KAZE::getThreshold()
    //

    //javadoc: KAZE::getThreshold()
    public  double getThreshold()
    {
        
        double retVal = getThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::KAZE::getNOctaveLayers()
    //

    //javadoc: KAZE::getNOctaveLayers()
    public  int getNOctaveLayers()
    {
        
        int retVal = getNOctaveLayers_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::KAZE::getNOctaves()
    //

    //javadoc: KAZE::getNOctaves()
    public  int getNOctaves()
    {
        
        int retVal = getNOctaves_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::KAZE::setDiffusivity(KAZE_DiffusivityType diff)
    //

    //javadoc: KAZE::setDiffusivity(diff)
    public  void setDiffusivity(int diff)
    {
        
        setDiffusivity_0(nativeObj, diff);
        
        return;
    }


    //
    // C++:  void cv::KAZE::setExtended(bool extended)
    //

    //javadoc: KAZE::setExtended(extended)
    public  void setExtended(boolean extended)
    {
        
        setExtended_0(nativeObj, extended);
        
        return;
    }


    //
    // C++:  void cv::KAZE::setNOctaveLayers(int octaveLayers)
    //

    //javadoc: KAZE::setNOctaveLayers(octaveLayers)
    public  void setNOctaveLayers(int octaveLayers)
    {
        
        setNOctaveLayers_0(nativeObj, octaveLayers);
        
        return;
    }


    //
    // C++:  void cv::KAZE::setNOctaves(int octaves)
    //

    //javadoc: KAZE::setNOctaves(octaves)
    public  void setNOctaves(int octaves)
    {
        
        setNOctaves_0(nativeObj, octaves);
        
        return;
    }


    //
    // C++:  void cv::KAZE::setThreshold(double threshold)
    //

    //javadoc: KAZE::setThreshold(threshold)
    public  void setThreshold(double threshold)
    {
        
        setThreshold_0(nativeObj, threshold);
        
        return;
    }


    //
    // C++:  void cv::KAZE::setUpright(bool upright)
    //

    //javadoc: KAZE::setUpright(upright)
    public  void setUpright(boolean upright)
    {
        
        setUpright_0(nativeObj, upright);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  KAZE_DiffusivityType cv::KAZE::getDiffusivity()
    private static native int getDiffusivity_0(long nativeObj);

    // C++: static Ptr_KAZE cv::KAZE::create(bool extended = false, bool upright = false, float threshold = 0.001f, int nOctaves = 4, int nOctaveLayers = 4, KAZE_DiffusivityType diffusivity = KAZE::DIFF_PM_G2)
    private static native long create_0(boolean extended, boolean upright, float threshold, int nOctaves, int nOctaveLayers, int diffusivity);
    private static native long create_1(boolean extended, boolean upright, float threshold, int nOctaves, int nOctaveLayers);
    private static native long create_2(boolean extended, boolean upright, float threshold, int nOctaves);
    private static native long create_3(boolean extended, boolean upright, float threshold);
    private static native long create_4(boolean extended, boolean upright);
    private static native long create_5(boolean extended);
    private static native long create_6();

    // C++:  String cv::KAZE::getDefaultName()
    private static native String getDefaultName_0(long nativeObj);

    // C++:  bool cv::KAZE::getExtended()
    private static native boolean getExtended_0(long nativeObj);

    // C++:  bool cv::KAZE::getUpright()
    private static native boolean getUpright_0(long nativeObj);

    // C++:  double cv::KAZE::getThreshold()
    private static native double getThreshold_0(long nativeObj);

    // C++:  int cv::KAZE::getNOctaveLayers()
    private static native int getNOctaveLayers_0(long nativeObj);

    // C++:  int cv::KAZE::getNOctaves()
    private static native int getNOctaves_0(long nativeObj);

    // C++:  void cv::KAZE::setDiffusivity(KAZE_DiffusivityType diff)
    private static native void setDiffusivity_0(long nativeObj, int diff);

    // C++:  void cv::KAZE::setExtended(bool extended)
    private static native void setExtended_0(long nativeObj, boolean extended);

    // C++:  void cv::KAZE::setNOctaveLayers(int octaveLayers)
    private static native void setNOctaveLayers_0(long nativeObj, int octaveLayers);

    // C++:  void cv::KAZE::setNOctaves(int octaves)
    private static native void setNOctaves_0(long nativeObj, int octaves);

    // C++:  void cv::KAZE::setThreshold(double threshold)
    private static native void setThreshold_0(long nativeObj, double threshold);

    // C++:  void cv::KAZE::setUpright(bool upright)
    private static native void setUpright_0(long nativeObj, boolean upright);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
