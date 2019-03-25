//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.calib3d;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;

// C++: class StereoMatcher
//javadoc: StereoMatcher

public class StereoMatcher extends Algorithm {

    protected StereoMatcher(long addr) { super(addr); }

    // internal usage only
    public static StereoMatcher __fromPtr__(long addr) { return new StereoMatcher(addr); }

    // C++: enum <unnamed>
    public static final int
            DISP_SHIFT = 4,
            DISP_SCALE = (1 << DISP_SHIFT);


    //
    // C++:  int cv::StereoMatcher::getBlockSize()
    //

    //javadoc: StereoMatcher::getBlockSize()
    public  int getBlockSize()
    {
        
        int retVal = getBlockSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoMatcher::getDisp12MaxDiff()
    //

    //javadoc: StereoMatcher::getDisp12MaxDiff()
    public  int getDisp12MaxDiff()
    {
        
        int retVal = getDisp12MaxDiff_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoMatcher::getMinDisparity()
    //

    //javadoc: StereoMatcher::getMinDisparity()
    public  int getMinDisparity()
    {
        
        int retVal = getMinDisparity_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoMatcher::getNumDisparities()
    //

    //javadoc: StereoMatcher::getNumDisparities()
    public  int getNumDisparities()
    {
        
        int retVal = getNumDisparities_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoMatcher::getSpeckleRange()
    //

    //javadoc: StereoMatcher::getSpeckleRange()
    public  int getSpeckleRange()
    {
        
        int retVal = getSpeckleRange_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoMatcher::getSpeckleWindowSize()
    //

    //javadoc: StereoMatcher::getSpeckleWindowSize()
    public  int getSpeckleWindowSize()
    {
        
        int retVal = getSpeckleWindowSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::StereoMatcher::compute(Mat left, Mat right, Mat& disparity)
    //

    //javadoc: StereoMatcher::compute(left, right, disparity)
    public  void compute(Mat left, Mat right, Mat disparity)
    {
        
        compute_0(nativeObj, left.nativeObj, right.nativeObj, disparity.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::StereoMatcher::setBlockSize(int blockSize)
    //

    //javadoc: StereoMatcher::setBlockSize(blockSize)
    public  void setBlockSize(int blockSize)
    {
        
        setBlockSize_0(nativeObj, blockSize);
        
        return;
    }


    //
    // C++:  void cv::StereoMatcher::setDisp12MaxDiff(int disp12MaxDiff)
    //

    //javadoc: StereoMatcher::setDisp12MaxDiff(disp12MaxDiff)
    public  void setDisp12MaxDiff(int disp12MaxDiff)
    {
        
        setDisp12MaxDiff_0(nativeObj, disp12MaxDiff);
        
        return;
    }


    //
    // C++:  void cv::StereoMatcher::setMinDisparity(int minDisparity)
    //

    //javadoc: StereoMatcher::setMinDisparity(minDisparity)
    public  void setMinDisparity(int minDisparity)
    {
        
        setMinDisparity_0(nativeObj, minDisparity);
        
        return;
    }


    //
    // C++:  void cv::StereoMatcher::setNumDisparities(int numDisparities)
    //

    //javadoc: StereoMatcher::setNumDisparities(numDisparities)
    public  void setNumDisparities(int numDisparities)
    {
        
        setNumDisparities_0(nativeObj, numDisparities);
        
        return;
    }


    //
    // C++:  void cv::StereoMatcher::setSpeckleRange(int speckleRange)
    //

    //javadoc: StereoMatcher::setSpeckleRange(speckleRange)
    public  void setSpeckleRange(int speckleRange)
    {
        
        setSpeckleRange_0(nativeObj, speckleRange);
        
        return;
    }


    //
    // C++:  void cv::StereoMatcher::setSpeckleWindowSize(int speckleWindowSize)
    //

    //javadoc: StereoMatcher::setSpeckleWindowSize(speckleWindowSize)
    public  void setSpeckleWindowSize(int speckleWindowSize)
    {
        
        setSpeckleWindowSize_0(nativeObj, speckleWindowSize);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  int cv::StereoMatcher::getBlockSize()
    private static native int getBlockSize_0(long nativeObj);

    // C++:  int cv::StereoMatcher::getDisp12MaxDiff()
    private static native int getDisp12MaxDiff_0(long nativeObj);

    // C++:  int cv::StereoMatcher::getMinDisparity()
    private static native int getMinDisparity_0(long nativeObj);

    // C++:  int cv::StereoMatcher::getNumDisparities()
    private static native int getNumDisparities_0(long nativeObj);

    // C++:  int cv::StereoMatcher::getSpeckleRange()
    private static native int getSpeckleRange_0(long nativeObj);

    // C++:  int cv::StereoMatcher::getSpeckleWindowSize()
    private static native int getSpeckleWindowSize_0(long nativeObj);

    // C++:  void cv::StereoMatcher::compute(Mat left, Mat right, Mat& disparity)
    private static native void compute_0(long nativeObj, long left_nativeObj, long right_nativeObj, long disparity_nativeObj);

    // C++:  void cv::StereoMatcher::setBlockSize(int blockSize)
    private static native void setBlockSize_0(long nativeObj, int blockSize);

    // C++:  void cv::StereoMatcher::setDisp12MaxDiff(int disp12MaxDiff)
    private static native void setDisp12MaxDiff_0(long nativeObj, int disp12MaxDiff);

    // C++:  void cv::StereoMatcher::setMinDisparity(int minDisparity)
    private static native void setMinDisparity_0(long nativeObj, int minDisparity);

    // C++:  void cv::StereoMatcher::setNumDisparities(int numDisparities)
    private static native void setNumDisparities_0(long nativeObj, int numDisparities);

    // C++:  void cv::StereoMatcher::setSpeckleRange(int speckleRange)
    private static native void setSpeckleRange_0(long nativeObj, int speckleRange);

    // C++:  void cv::StereoMatcher::setSpeckleWindowSize(int speckleWindowSize)
    private static native void setSpeckleWindowSize_0(long nativeObj, int speckleWindowSize);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
