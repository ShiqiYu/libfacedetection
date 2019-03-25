//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.imgproc;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.core.Size;

// C++: class LineSegmentDetector
//javadoc: LineSegmentDetector

public class LineSegmentDetector extends Algorithm {

    protected LineSegmentDetector(long addr) { super(addr); }

    // internal usage only
    public static LineSegmentDetector __fromPtr__(long addr) { return new LineSegmentDetector(addr); }

    //
    // C++:  int cv::LineSegmentDetector::compareSegments(Size size, Mat lines1, Mat lines2, Mat& _image = Mat())
    //

    //javadoc: LineSegmentDetector::compareSegments(size, lines1, lines2, _image)
    public  int compareSegments(Size size, Mat lines1, Mat lines2, Mat _image)
    {
        
        int retVal = compareSegments_0(nativeObj, size.width, size.height, lines1.nativeObj, lines2.nativeObj, _image.nativeObj);
        
        return retVal;
    }

    //javadoc: LineSegmentDetector::compareSegments(size, lines1, lines2)
    public  int compareSegments(Size size, Mat lines1, Mat lines2)
    {
        
        int retVal = compareSegments_1(nativeObj, size.width, size.height, lines1.nativeObj, lines2.nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::LineSegmentDetector::detect(Mat _image, Mat& _lines, Mat& width = Mat(), Mat& prec = Mat(), Mat& nfa = Mat())
    //

    //javadoc: LineSegmentDetector::detect(_image, _lines, width, prec, nfa)
    public  void detect(Mat _image, Mat _lines, Mat width, Mat prec, Mat nfa)
    {
        
        detect_0(nativeObj, _image.nativeObj, _lines.nativeObj, width.nativeObj, prec.nativeObj, nfa.nativeObj);
        
        return;
    }

    //javadoc: LineSegmentDetector::detect(_image, _lines, width, prec)
    public  void detect(Mat _image, Mat _lines, Mat width, Mat prec)
    {
        
        detect_1(nativeObj, _image.nativeObj, _lines.nativeObj, width.nativeObj, prec.nativeObj);
        
        return;
    }

    //javadoc: LineSegmentDetector::detect(_image, _lines, width)
    public  void detect(Mat _image, Mat _lines, Mat width)
    {
        
        detect_2(nativeObj, _image.nativeObj, _lines.nativeObj, width.nativeObj);
        
        return;
    }

    //javadoc: LineSegmentDetector::detect(_image, _lines)
    public  void detect(Mat _image, Mat _lines)
    {
        
        detect_3(nativeObj, _image.nativeObj, _lines.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::LineSegmentDetector::drawSegments(Mat& _image, Mat lines)
    //

    //javadoc: LineSegmentDetector::drawSegments(_image, lines)
    public  void drawSegments(Mat _image, Mat lines)
    {
        
        drawSegments_0(nativeObj, _image.nativeObj, lines.nativeObj);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  int cv::LineSegmentDetector::compareSegments(Size size, Mat lines1, Mat lines2, Mat& _image = Mat())
    private static native int compareSegments_0(long nativeObj, double size_width, double size_height, long lines1_nativeObj, long lines2_nativeObj, long _image_nativeObj);
    private static native int compareSegments_1(long nativeObj, double size_width, double size_height, long lines1_nativeObj, long lines2_nativeObj);

    // C++:  void cv::LineSegmentDetector::detect(Mat _image, Mat& _lines, Mat& width = Mat(), Mat& prec = Mat(), Mat& nfa = Mat())
    private static native void detect_0(long nativeObj, long _image_nativeObj, long _lines_nativeObj, long width_nativeObj, long prec_nativeObj, long nfa_nativeObj);
    private static native void detect_1(long nativeObj, long _image_nativeObj, long _lines_nativeObj, long width_nativeObj, long prec_nativeObj);
    private static native void detect_2(long nativeObj, long _image_nativeObj, long _lines_nativeObj, long width_nativeObj);
    private static native void detect_3(long nativeObj, long _image_nativeObj, long _lines_nativeObj);

    // C++:  void cv::LineSegmentDetector::drawSegments(Mat& _image, Mat lines)
    private static native void drawSegments_0(long nativeObj, long _image_nativeObj, long lines_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
