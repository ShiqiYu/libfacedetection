//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.photo;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.photo.AlignExposures;
import org.opencv.utils.Converters;

// C++: class AlignMTB
//javadoc: AlignMTB

public class AlignMTB extends AlignExposures {

    protected AlignMTB(long addr) { super(addr); }

    // internal usage only
    public static AlignMTB __fromPtr__(long addr) { return new AlignMTB(addr); }

    //
    // C++:  Point cv::AlignMTB::calculateShift(Mat img0, Mat img1)
    //

    //javadoc: AlignMTB::calculateShift(img0, img1)
    public  Point calculateShift(Mat img0, Mat img1)
    {
        
        Point retVal = new Point(calculateShift_0(nativeObj, img0.nativeObj, img1.nativeObj));
        
        return retVal;
    }


    //
    // C++:  bool cv::AlignMTB::getCut()
    //

    //javadoc: AlignMTB::getCut()
    public  boolean getCut()
    {
        
        boolean retVal = getCut_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::AlignMTB::getExcludeRange()
    //

    //javadoc: AlignMTB::getExcludeRange()
    public  int getExcludeRange()
    {
        
        int retVal = getExcludeRange_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::AlignMTB::getMaxBits()
    //

    //javadoc: AlignMTB::getMaxBits()
    public  int getMaxBits()
    {
        
        int retVal = getMaxBits_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::AlignMTB::computeBitmaps(Mat img, Mat& tb, Mat& eb)
    //

    //javadoc: AlignMTB::computeBitmaps(img, tb, eb)
    public  void computeBitmaps(Mat img, Mat tb, Mat eb)
    {
        
        computeBitmaps_0(nativeObj, img.nativeObj, tb.nativeObj, eb.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::AlignMTB::process(vector_Mat src, vector_Mat dst, Mat times, Mat response)
    //

    //javadoc: AlignMTB::process(src, dst, times, response)
    public  void process(List<Mat> src, List<Mat> dst, Mat times, Mat response)
    {
        Mat src_mat = Converters.vector_Mat_to_Mat(src);
        Mat dst_mat = Converters.vector_Mat_to_Mat(dst);
        process_0(nativeObj, src_mat.nativeObj, dst_mat.nativeObj, times.nativeObj, response.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::AlignMTB::process(vector_Mat src, vector_Mat dst)
    //

    //javadoc: AlignMTB::process(src, dst)
    public  void process(List<Mat> src, List<Mat> dst)
    {
        Mat src_mat = Converters.vector_Mat_to_Mat(src);
        Mat dst_mat = Converters.vector_Mat_to_Mat(dst);
        process_1(nativeObj, src_mat.nativeObj, dst_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::AlignMTB::setCut(bool value)
    //

    //javadoc: AlignMTB::setCut(value)
    public  void setCut(boolean value)
    {
        
        setCut_0(nativeObj, value);
        
        return;
    }


    //
    // C++:  void cv::AlignMTB::setExcludeRange(int exclude_range)
    //

    //javadoc: AlignMTB::setExcludeRange(exclude_range)
    public  void setExcludeRange(int exclude_range)
    {
        
        setExcludeRange_0(nativeObj, exclude_range);
        
        return;
    }


    //
    // C++:  void cv::AlignMTB::setMaxBits(int max_bits)
    //

    //javadoc: AlignMTB::setMaxBits(max_bits)
    public  void setMaxBits(int max_bits)
    {
        
        setMaxBits_0(nativeObj, max_bits);
        
        return;
    }


    //
    // C++:  void cv::AlignMTB::shiftMat(Mat src, Mat& dst, Point shift)
    //

    //javadoc: AlignMTB::shiftMat(src, dst, shift)
    public  void shiftMat(Mat src, Mat dst, Point shift)
    {
        
        shiftMat_0(nativeObj, src.nativeObj, dst.nativeObj, shift.x, shift.y);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  Point cv::AlignMTB::calculateShift(Mat img0, Mat img1)
    private static native double[] calculateShift_0(long nativeObj, long img0_nativeObj, long img1_nativeObj);

    // C++:  bool cv::AlignMTB::getCut()
    private static native boolean getCut_0(long nativeObj);

    // C++:  int cv::AlignMTB::getExcludeRange()
    private static native int getExcludeRange_0(long nativeObj);

    // C++:  int cv::AlignMTB::getMaxBits()
    private static native int getMaxBits_0(long nativeObj);

    // C++:  void cv::AlignMTB::computeBitmaps(Mat img, Mat& tb, Mat& eb)
    private static native void computeBitmaps_0(long nativeObj, long img_nativeObj, long tb_nativeObj, long eb_nativeObj);

    // C++:  void cv::AlignMTB::process(vector_Mat src, vector_Mat dst, Mat times, Mat response)
    private static native void process_0(long nativeObj, long src_mat_nativeObj, long dst_mat_nativeObj, long times_nativeObj, long response_nativeObj);

    // C++:  void cv::AlignMTB::process(vector_Mat src, vector_Mat dst)
    private static native void process_1(long nativeObj, long src_mat_nativeObj, long dst_mat_nativeObj);

    // C++:  void cv::AlignMTB::setCut(bool value)
    private static native void setCut_0(long nativeObj, boolean value);

    // C++:  void cv::AlignMTB::setExcludeRange(int exclude_range)
    private static native void setExcludeRange_0(long nativeObj, int exclude_range);

    // C++:  void cv::AlignMTB::setMaxBits(int max_bits)
    private static native void setMaxBits_0(long nativeObj, int max_bits);

    // C++:  void cv::AlignMTB::shiftMat(Mat src, Mat& dst, Point shift)
    private static native void shiftMat_0(long nativeObj, long src_nativeObj, long dst_nativeObj, double shift_x, double shift_y);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
