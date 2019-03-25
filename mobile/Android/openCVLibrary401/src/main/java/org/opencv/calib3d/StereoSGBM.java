//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.calib3d;

import org.opencv.calib3d.StereoMatcher;
import org.opencv.calib3d.StereoSGBM;

// C++: class StereoSGBM
//javadoc: StereoSGBM

public class StereoSGBM extends StereoMatcher {

    protected StereoSGBM(long addr) { super(addr); }

    // internal usage only
    public static StereoSGBM __fromPtr__(long addr) { return new StereoSGBM(addr); }

    // C++: enum <unnamed>
    public static final int
            MODE_SGBM = 0,
            MODE_HH = 1,
            MODE_SGBM_3WAY = 2,
            MODE_HH4 = 3;


    //
    // C++: static Ptr_StereoSGBM cv::StereoSGBM::create(int minDisparity = 0, int numDisparities = 16, int blockSize = 3, int P1 = 0, int P2 = 0, int disp12MaxDiff = 0, int preFilterCap = 0, int uniquenessRatio = 0, int speckleWindowSize = 0, int speckleRange = 0, int mode = StereoSGBM::MODE_SGBM)
    //

    //javadoc: StereoSGBM::create(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff, preFilterCap, uniquenessRatio, speckleWindowSize, speckleRange, mode)
    public static StereoSGBM create(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff, int preFilterCap, int uniquenessRatio, int speckleWindowSize, int speckleRange, int mode)
    {
        
        StereoSGBM retVal = StereoSGBM.__fromPtr__(create_0(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff, preFilterCap, uniquenessRatio, speckleWindowSize, speckleRange, mode));
        
        return retVal;
    }

    //javadoc: StereoSGBM::create(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff, preFilterCap, uniquenessRatio, speckleWindowSize, speckleRange)
    public static StereoSGBM create(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff, int preFilterCap, int uniquenessRatio, int speckleWindowSize, int speckleRange)
    {
        
        StereoSGBM retVal = StereoSGBM.__fromPtr__(create_1(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff, preFilterCap, uniquenessRatio, speckleWindowSize, speckleRange));
        
        return retVal;
    }

    //javadoc: StereoSGBM::create(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff, preFilterCap, uniquenessRatio, speckleWindowSize)
    public static StereoSGBM create(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff, int preFilterCap, int uniquenessRatio, int speckleWindowSize)
    {
        
        StereoSGBM retVal = StereoSGBM.__fromPtr__(create_2(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff, preFilterCap, uniquenessRatio, speckleWindowSize));
        
        return retVal;
    }

    //javadoc: StereoSGBM::create(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff, preFilterCap, uniquenessRatio)
    public static StereoSGBM create(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff, int preFilterCap, int uniquenessRatio)
    {
        
        StereoSGBM retVal = StereoSGBM.__fromPtr__(create_3(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff, preFilterCap, uniquenessRatio));
        
        return retVal;
    }

    //javadoc: StereoSGBM::create(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff, preFilterCap)
    public static StereoSGBM create(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff, int preFilterCap)
    {
        
        StereoSGBM retVal = StereoSGBM.__fromPtr__(create_4(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff, preFilterCap));
        
        return retVal;
    }

    //javadoc: StereoSGBM::create(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff)
    public static StereoSGBM create(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff)
    {
        
        StereoSGBM retVal = StereoSGBM.__fromPtr__(create_5(minDisparity, numDisparities, blockSize, P1, P2, disp12MaxDiff));
        
        return retVal;
    }

    //javadoc: StereoSGBM::create(minDisparity, numDisparities, blockSize, P1, P2)
    public static StereoSGBM create(int minDisparity, int numDisparities, int blockSize, int P1, int P2)
    {
        
        StereoSGBM retVal = StereoSGBM.__fromPtr__(create_6(minDisparity, numDisparities, blockSize, P1, P2));
        
        return retVal;
    }

    //javadoc: StereoSGBM::create(minDisparity, numDisparities, blockSize, P1)
    public static StereoSGBM create(int minDisparity, int numDisparities, int blockSize, int P1)
    {
        
        StereoSGBM retVal = StereoSGBM.__fromPtr__(create_7(minDisparity, numDisparities, blockSize, P1));
        
        return retVal;
    }

    //javadoc: StereoSGBM::create(minDisparity, numDisparities, blockSize)
    public static StereoSGBM create(int minDisparity, int numDisparities, int blockSize)
    {
        
        StereoSGBM retVal = StereoSGBM.__fromPtr__(create_8(minDisparity, numDisparities, blockSize));
        
        return retVal;
    }

    //javadoc: StereoSGBM::create(minDisparity, numDisparities)
    public static StereoSGBM create(int minDisparity, int numDisparities)
    {
        
        StereoSGBM retVal = StereoSGBM.__fromPtr__(create_9(minDisparity, numDisparities));
        
        return retVal;
    }

    //javadoc: StereoSGBM::create(minDisparity)
    public static StereoSGBM create(int minDisparity)
    {
        
        StereoSGBM retVal = StereoSGBM.__fromPtr__(create_10(minDisparity));
        
        return retVal;
    }

    //javadoc: StereoSGBM::create()
    public static StereoSGBM create()
    {
        
        StereoSGBM retVal = StereoSGBM.__fromPtr__(create_11());
        
        return retVal;
    }


    //
    // C++:  int cv::StereoSGBM::getMode()
    //

    //javadoc: StereoSGBM::getMode()
    public  int getMode()
    {
        
        int retVal = getMode_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoSGBM::getP1()
    //

    //javadoc: StereoSGBM::getP1()
    public  int getP1()
    {
        
        int retVal = getP1_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoSGBM::getP2()
    //

    //javadoc: StereoSGBM::getP2()
    public  int getP2()
    {
        
        int retVal = getP2_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoSGBM::getPreFilterCap()
    //

    //javadoc: StereoSGBM::getPreFilterCap()
    public  int getPreFilterCap()
    {
        
        int retVal = getPreFilterCap_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoSGBM::getUniquenessRatio()
    //

    //javadoc: StereoSGBM::getUniquenessRatio()
    public  int getUniquenessRatio()
    {
        
        int retVal = getUniquenessRatio_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::StereoSGBM::setMode(int mode)
    //

    //javadoc: StereoSGBM::setMode(mode)
    public  void setMode(int mode)
    {
        
        setMode_0(nativeObj, mode);
        
        return;
    }


    //
    // C++:  void cv::StereoSGBM::setP1(int P1)
    //

    //javadoc: StereoSGBM::setP1(P1)
    public  void setP1(int P1)
    {
        
        setP1_0(nativeObj, P1);
        
        return;
    }


    //
    // C++:  void cv::StereoSGBM::setP2(int P2)
    //

    //javadoc: StereoSGBM::setP2(P2)
    public  void setP2(int P2)
    {
        
        setP2_0(nativeObj, P2);
        
        return;
    }


    //
    // C++:  void cv::StereoSGBM::setPreFilterCap(int preFilterCap)
    //

    //javadoc: StereoSGBM::setPreFilterCap(preFilterCap)
    public  void setPreFilterCap(int preFilterCap)
    {
        
        setPreFilterCap_0(nativeObj, preFilterCap);
        
        return;
    }


    //
    // C++:  void cv::StereoSGBM::setUniquenessRatio(int uniquenessRatio)
    //

    //javadoc: StereoSGBM::setUniquenessRatio(uniquenessRatio)
    public  void setUniquenessRatio(int uniquenessRatio)
    {
        
        setUniquenessRatio_0(nativeObj, uniquenessRatio);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_StereoSGBM cv::StereoSGBM::create(int minDisparity = 0, int numDisparities = 16, int blockSize = 3, int P1 = 0, int P2 = 0, int disp12MaxDiff = 0, int preFilterCap = 0, int uniquenessRatio = 0, int speckleWindowSize = 0, int speckleRange = 0, int mode = StereoSGBM::MODE_SGBM)
    private static native long create_0(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff, int preFilterCap, int uniquenessRatio, int speckleWindowSize, int speckleRange, int mode);
    private static native long create_1(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff, int preFilterCap, int uniquenessRatio, int speckleWindowSize, int speckleRange);
    private static native long create_2(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff, int preFilterCap, int uniquenessRatio, int speckleWindowSize);
    private static native long create_3(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff, int preFilterCap, int uniquenessRatio);
    private static native long create_4(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff, int preFilterCap);
    private static native long create_5(int minDisparity, int numDisparities, int blockSize, int P1, int P2, int disp12MaxDiff);
    private static native long create_6(int minDisparity, int numDisparities, int blockSize, int P1, int P2);
    private static native long create_7(int minDisparity, int numDisparities, int blockSize, int P1);
    private static native long create_8(int minDisparity, int numDisparities, int blockSize);
    private static native long create_9(int minDisparity, int numDisparities);
    private static native long create_10(int minDisparity);
    private static native long create_11();

    // C++:  int cv::StereoSGBM::getMode()
    private static native int getMode_0(long nativeObj);

    // C++:  int cv::StereoSGBM::getP1()
    private static native int getP1_0(long nativeObj);

    // C++:  int cv::StereoSGBM::getP2()
    private static native int getP2_0(long nativeObj);

    // C++:  int cv::StereoSGBM::getPreFilterCap()
    private static native int getPreFilterCap_0(long nativeObj);

    // C++:  int cv::StereoSGBM::getUniquenessRatio()
    private static native int getUniquenessRatio_0(long nativeObj);

    // C++:  void cv::StereoSGBM::setMode(int mode)
    private static native void setMode_0(long nativeObj, int mode);

    // C++:  void cv::StereoSGBM::setP1(int P1)
    private static native void setP1_0(long nativeObj, int P1);

    // C++:  void cv::StereoSGBM::setP2(int P2)
    private static native void setP2_0(long nativeObj, int P2);

    // C++:  void cv::StereoSGBM::setPreFilterCap(int preFilterCap)
    private static native void setPreFilterCap_0(long nativeObj, int preFilterCap);

    // C++:  void cv::StereoSGBM::setUniquenessRatio(int uniquenessRatio)
    private static native void setUniquenessRatio_0(long nativeObj, int uniquenessRatio);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
