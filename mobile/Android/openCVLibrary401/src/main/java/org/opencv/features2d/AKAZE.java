//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;

import java.lang.String;
import org.opencv.features2d.AKAZE;
import org.opencv.features2d.Feature2D;

// C++: class AKAZE
//javadoc: AKAZE

public class AKAZE extends Feature2D {

    protected AKAZE(long addr) { super(addr); }

    // internal usage only
    public static AKAZE __fromPtr__(long addr) { return new AKAZE(addr); }

    // C++: enum DescriptorType
    public static final int
            DESCRIPTOR_KAZE_UPRIGHT = 2,
            DESCRIPTOR_KAZE = 3,
            DESCRIPTOR_MLDB_UPRIGHT = 4,
            DESCRIPTOR_MLDB = 5;


    //
    // C++:  AKAZE_DescriptorType cv::AKAZE::getDescriptorType()
    //

    //javadoc: AKAZE::getDescriptorType()
    public  int getDescriptorType()
    {
        
        int retVal = getDescriptorType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  KAZE_DiffusivityType cv::AKAZE::getDiffusivity()
    //

    //javadoc: AKAZE::getDiffusivity()
    public  int getDiffusivity()
    {
        
        int retVal = getDiffusivity_0(nativeObj);
        
        return retVal;
    }


    //
    // C++: static Ptr_AKAZE cv::AKAZE::create(AKAZE_DescriptorType descriptor_type = AKAZE::DESCRIPTOR_MLDB, int descriptor_size = 0, int descriptor_channels = 3, float threshold = 0.001f, int nOctaves = 4, int nOctaveLayers = 4, KAZE_DiffusivityType diffusivity = KAZE::DIFF_PM_G2)
    //

    //javadoc: AKAZE::create(descriptor_type, descriptor_size, descriptor_channels, threshold, nOctaves, nOctaveLayers, diffusivity)
    public static AKAZE create(int descriptor_type, int descriptor_size, int descriptor_channels, float threshold, int nOctaves, int nOctaveLayers, int diffusivity)
    {
        
        AKAZE retVal = AKAZE.__fromPtr__(create_0(descriptor_type, descriptor_size, descriptor_channels, threshold, nOctaves, nOctaveLayers, diffusivity));
        
        return retVal;
    }

    //javadoc: AKAZE::create(descriptor_type, descriptor_size, descriptor_channels, threshold, nOctaves, nOctaveLayers)
    public static AKAZE create(int descriptor_type, int descriptor_size, int descriptor_channels, float threshold, int nOctaves, int nOctaveLayers)
    {
        
        AKAZE retVal = AKAZE.__fromPtr__(create_1(descriptor_type, descriptor_size, descriptor_channels, threshold, nOctaves, nOctaveLayers));
        
        return retVal;
    }

    //javadoc: AKAZE::create(descriptor_type, descriptor_size, descriptor_channels, threshold, nOctaves)
    public static AKAZE create(int descriptor_type, int descriptor_size, int descriptor_channels, float threshold, int nOctaves)
    {
        
        AKAZE retVal = AKAZE.__fromPtr__(create_2(descriptor_type, descriptor_size, descriptor_channels, threshold, nOctaves));
        
        return retVal;
    }

    //javadoc: AKAZE::create(descriptor_type, descriptor_size, descriptor_channels, threshold)
    public static AKAZE create(int descriptor_type, int descriptor_size, int descriptor_channels, float threshold)
    {
        
        AKAZE retVal = AKAZE.__fromPtr__(create_3(descriptor_type, descriptor_size, descriptor_channels, threshold));
        
        return retVal;
    }

    //javadoc: AKAZE::create(descriptor_type, descriptor_size, descriptor_channels)
    public static AKAZE create(int descriptor_type, int descriptor_size, int descriptor_channels)
    {
        
        AKAZE retVal = AKAZE.__fromPtr__(create_4(descriptor_type, descriptor_size, descriptor_channels));
        
        return retVal;
    }

    //javadoc: AKAZE::create(descriptor_type, descriptor_size)
    public static AKAZE create(int descriptor_type, int descriptor_size)
    {
        
        AKAZE retVal = AKAZE.__fromPtr__(create_5(descriptor_type, descriptor_size));
        
        return retVal;
    }

    //javadoc: AKAZE::create(descriptor_type)
    public static AKAZE create(int descriptor_type)
    {
        
        AKAZE retVal = AKAZE.__fromPtr__(create_6(descriptor_type));
        
        return retVal;
    }

    //javadoc: AKAZE::create()
    public static AKAZE create()
    {
        
        AKAZE retVal = AKAZE.__fromPtr__(create_7());
        
        return retVal;
    }


    //
    // C++:  String cv::AKAZE::getDefaultName()
    //

    //javadoc: AKAZE::getDefaultName()
    public  String getDefaultName()
    {
        
        String retVal = getDefaultName_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::AKAZE::getThreshold()
    //

    //javadoc: AKAZE::getThreshold()
    public  double getThreshold()
    {
        
        double retVal = getThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::AKAZE::getDescriptorChannels()
    //

    //javadoc: AKAZE::getDescriptorChannels()
    public  int getDescriptorChannels()
    {
        
        int retVal = getDescriptorChannels_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::AKAZE::getDescriptorSize()
    //

    //javadoc: AKAZE::getDescriptorSize()
    public  int getDescriptorSize()
    {
        
        int retVal = getDescriptorSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::AKAZE::getNOctaveLayers()
    //

    //javadoc: AKAZE::getNOctaveLayers()
    public  int getNOctaveLayers()
    {
        
        int retVal = getNOctaveLayers_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::AKAZE::getNOctaves()
    //

    //javadoc: AKAZE::getNOctaves()
    public  int getNOctaves()
    {
        
        int retVal = getNOctaves_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::AKAZE::setDescriptorChannels(int dch)
    //

    //javadoc: AKAZE::setDescriptorChannels(dch)
    public  void setDescriptorChannels(int dch)
    {
        
        setDescriptorChannels_0(nativeObj, dch);
        
        return;
    }


    //
    // C++:  void cv::AKAZE::setDescriptorSize(int dsize)
    //

    //javadoc: AKAZE::setDescriptorSize(dsize)
    public  void setDescriptorSize(int dsize)
    {
        
        setDescriptorSize_0(nativeObj, dsize);
        
        return;
    }


    //
    // C++:  void cv::AKAZE::setDescriptorType(AKAZE_DescriptorType dtype)
    //

    //javadoc: AKAZE::setDescriptorType(dtype)
    public  void setDescriptorType(int dtype)
    {
        
        setDescriptorType_0(nativeObj, dtype);
        
        return;
    }


    //
    // C++:  void cv::AKAZE::setDiffusivity(KAZE_DiffusivityType diff)
    //

    //javadoc: AKAZE::setDiffusivity(diff)
    public  void setDiffusivity(int diff)
    {
        
        setDiffusivity_0(nativeObj, diff);
        
        return;
    }


    //
    // C++:  void cv::AKAZE::setNOctaveLayers(int octaveLayers)
    //

    //javadoc: AKAZE::setNOctaveLayers(octaveLayers)
    public  void setNOctaveLayers(int octaveLayers)
    {
        
        setNOctaveLayers_0(nativeObj, octaveLayers);
        
        return;
    }


    //
    // C++:  void cv::AKAZE::setNOctaves(int octaves)
    //

    //javadoc: AKAZE::setNOctaves(octaves)
    public  void setNOctaves(int octaves)
    {
        
        setNOctaves_0(nativeObj, octaves);
        
        return;
    }


    //
    // C++:  void cv::AKAZE::setThreshold(double threshold)
    //

    //javadoc: AKAZE::setThreshold(threshold)
    public  void setThreshold(double threshold)
    {
        
        setThreshold_0(nativeObj, threshold);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  AKAZE_DescriptorType cv::AKAZE::getDescriptorType()
    private static native int getDescriptorType_0(long nativeObj);

    // C++:  KAZE_DiffusivityType cv::AKAZE::getDiffusivity()
    private static native int getDiffusivity_0(long nativeObj);

    // C++: static Ptr_AKAZE cv::AKAZE::create(AKAZE_DescriptorType descriptor_type = AKAZE::DESCRIPTOR_MLDB, int descriptor_size = 0, int descriptor_channels = 3, float threshold = 0.001f, int nOctaves = 4, int nOctaveLayers = 4, KAZE_DiffusivityType diffusivity = KAZE::DIFF_PM_G2)
    private static native long create_0(int descriptor_type, int descriptor_size, int descriptor_channels, float threshold, int nOctaves, int nOctaveLayers, int diffusivity);
    private static native long create_1(int descriptor_type, int descriptor_size, int descriptor_channels, float threshold, int nOctaves, int nOctaveLayers);
    private static native long create_2(int descriptor_type, int descriptor_size, int descriptor_channels, float threshold, int nOctaves);
    private static native long create_3(int descriptor_type, int descriptor_size, int descriptor_channels, float threshold);
    private static native long create_4(int descriptor_type, int descriptor_size, int descriptor_channels);
    private static native long create_5(int descriptor_type, int descriptor_size);
    private static native long create_6(int descriptor_type);
    private static native long create_7();

    // C++:  String cv::AKAZE::getDefaultName()
    private static native String getDefaultName_0(long nativeObj);

    // C++:  double cv::AKAZE::getThreshold()
    private static native double getThreshold_0(long nativeObj);

    // C++:  int cv::AKAZE::getDescriptorChannels()
    private static native int getDescriptorChannels_0(long nativeObj);

    // C++:  int cv::AKAZE::getDescriptorSize()
    private static native int getDescriptorSize_0(long nativeObj);

    // C++:  int cv::AKAZE::getNOctaveLayers()
    private static native int getNOctaveLayers_0(long nativeObj);

    // C++:  int cv::AKAZE::getNOctaves()
    private static native int getNOctaves_0(long nativeObj);

    // C++:  void cv::AKAZE::setDescriptorChannels(int dch)
    private static native void setDescriptorChannels_0(long nativeObj, int dch);

    // C++:  void cv::AKAZE::setDescriptorSize(int dsize)
    private static native void setDescriptorSize_0(long nativeObj, int dsize);

    // C++:  void cv::AKAZE::setDescriptorType(AKAZE_DescriptorType dtype)
    private static native void setDescriptorType_0(long nativeObj, int dtype);

    // C++:  void cv::AKAZE::setDiffusivity(KAZE_DiffusivityType diff)
    private static native void setDiffusivity_0(long nativeObj, int diff);

    // C++:  void cv::AKAZE::setNOctaveLayers(int octaveLayers)
    private static native void setNOctaveLayers_0(long nativeObj, int octaveLayers);

    // C++:  void cv::AKAZE::setNOctaves(int octaves)
    private static native void setNOctaves_0(long nativeObj, int octaves);

    // C++:  void cv::AKAZE::setThreshold(double threshold)
    private static native void setThreshold_0(long nativeObj, double threshold);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
