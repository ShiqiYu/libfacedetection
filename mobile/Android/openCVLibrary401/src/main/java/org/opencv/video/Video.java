//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.utils.Converters;
import org.opencv.video.BackgroundSubtractorKNN;
import org.opencv.video.BackgroundSubtractorMOG2;

// C++: class Video
//javadoc: Video

public class Video {

    private static final int
            CV_LKFLOW_INITIAL_GUESSES = 4,
            CV_LKFLOW_GET_MIN_EIGENVALS = 8;


    // C++: enum <unnamed>
    public static final int
            OPTFLOW_USE_INITIAL_FLOW = 4,
            OPTFLOW_LK_GET_MIN_EIGENVALS = 8,
            OPTFLOW_FARNEBACK_GAUSSIAN = 256,
            MOTION_TRANSLATION = 0,
            MOTION_EUCLIDEAN = 1,
            MOTION_AFFINE = 2,
            MOTION_HOMOGRAPHY = 3;


    //
    // C++:  Mat cv::readOpticalFlow(String path)
    //

    //javadoc: readOpticalFlow(path)
    public static Mat readOpticalFlow(String path)
    {
        
        Mat retVal = new Mat(readOpticalFlow_0(path));
        
        return retVal;
    }


    //
    // C++:  Ptr_BackgroundSubtractorKNN cv::createBackgroundSubtractorKNN(int history = 500, double dist2Threshold = 400.0, bool detectShadows = true)
    //

    //javadoc: createBackgroundSubtractorKNN(history, dist2Threshold, detectShadows)
    public static BackgroundSubtractorKNN createBackgroundSubtractorKNN(int history, double dist2Threshold, boolean detectShadows)
    {
        
        BackgroundSubtractorKNN retVal = BackgroundSubtractorKNN.__fromPtr__(createBackgroundSubtractorKNN_0(history, dist2Threshold, detectShadows));
        
        return retVal;
    }

    //javadoc: createBackgroundSubtractorKNN(history, dist2Threshold)
    public static BackgroundSubtractorKNN createBackgroundSubtractorKNN(int history, double dist2Threshold)
    {
        
        BackgroundSubtractorKNN retVal = BackgroundSubtractorKNN.__fromPtr__(createBackgroundSubtractorKNN_1(history, dist2Threshold));
        
        return retVal;
    }

    //javadoc: createBackgroundSubtractorKNN(history)
    public static BackgroundSubtractorKNN createBackgroundSubtractorKNN(int history)
    {
        
        BackgroundSubtractorKNN retVal = BackgroundSubtractorKNN.__fromPtr__(createBackgroundSubtractorKNN_2(history));
        
        return retVal;
    }

    //javadoc: createBackgroundSubtractorKNN()
    public static BackgroundSubtractorKNN createBackgroundSubtractorKNN()
    {
        
        BackgroundSubtractorKNN retVal = BackgroundSubtractorKNN.__fromPtr__(createBackgroundSubtractorKNN_3());
        
        return retVal;
    }


    //
    // C++:  Ptr_BackgroundSubtractorMOG2 cv::createBackgroundSubtractorMOG2(int history = 500, double varThreshold = 16, bool detectShadows = true)
    //

    //javadoc: createBackgroundSubtractorMOG2(history, varThreshold, detectShadows)
    public static BackgroundSubtractorMOG2 createBackgroundSubtractorMOG2(int history, double varThreshold, boolean detectShadows)
    {
        
        BackgroundSubtractorMOG2 retVal = BackgroundSubtractorMOG2.__fromPtr__(createBackgroundSubtractorMOG2_0(history, varThreshold, detectShadows));
        
        return retVal;
    }

    //javadoc: createBackgroundSubtractorMOG2(history, varThreshold)
    public static BackgroundSubtractorMOG2 createBackgroundSubtractorMOG2(int history, double varThreshold)
    {
        
        BackgroundSubtractorMOG2 retVal = BackgroundSubtractorMOG2.__fromPtr__(createBackgroundSubtractorMOG2_1(history, varThreshold));
        
        return retVal;
    }

    //javadoc: createBackgroundSubtractorMOG2(history)
    public static BackgroundSubtractorMOG2 createBackgroundSubtractorMOG2(int history)
    {
        
        BackgroundSubtractorMOG2 retVal = BackgroundSubtractorMOG2.__fromPtr__(createBackgroundSubtractorMOG2_2(history));
        
        return retVal;
    }

    //javadoc: createBackgroundSubtractorMOG2()
    public static BackgroundSubtractorMOG2 createBackgroundSubtractorMOG2()
    {
        
        BackgroundSubtractorMOG2 retVal = BackgroundSubtractorMOG2.__fromPtr__(createBackgroundSubtractorMOG2_3());
        
        return retVal;
    }


    //
    // C++:  RotatedRect cv::CamShift(Mat probImage, Rect& window, TermCriteria criteria)
    //

    //javadoc: CamShift(probImage, window, criteria)
    public static RotatedRect CamShift(Mat probImage, Rect window, TermCriteria criteria)
    {
        double[] window_out = new double[4];
        RotatedRect retVal = new RotatedRect(CamShift_0(probImage.nativeObj, window.x, window.y, window.width, window.height, window_out, criteria.type, criteria.maxCount, criteria.epsilon));
        if(window!=null){ window.x = (int)window_out[0]; window.y = (int)window_out[1]; window.width = (int)window_out[2]; window.height = (int)window_out[3]; } 
        return retVal;
    }


    //
    // C++:  bool cv::writeOpticalFlow(String path, Mat flow)
    //

    //javadoc: writeOpticalFlow(path, flow)
    public static boolean writeOpticalFlow(String path, Mat flow)
    {
        
        boolean retVal = writeOpticalFlow_0(path, flow.nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::findTransformECC(Mat templateImage, Mat inputImage, Mat& warpMatrix, int motionType = MOTION_AFFINE, TermCriteria criteria = TermCriteria(TermCriteria::COUNT+TermCriteria::EPS, 50, 0.001), Mat inputMask = Mat())
    //

    //javadoc: findTransformECC(templateImage, inputImage, warpMatrix, motionType, criteria, inputMask)
    public static double findTransformECC(Mat templateImage, Mat inputImage, Mat warpMatrix, int motionType, TermCriteria criteria, Mat inputMask)
    {
        
        double retVal = findTransformECC_0(templateImage.nativeObj, inputImage.nativeObj, warpMatrix.nativeObj, motionType, criteria.type, criteria.maxCount, criteria.epsilon, inputMask.nativeObj);
        
        return retVal;
    }

    //javadoc: findTransformECC(templateImage, inputImage, warpMatrix, motionType, criteria)
    public static double findTransformECC(Mat templateImage, Mat inputImage, Mat warpMatrix, int motionType, TermCriteria criteria)
    {
        
        double retVal = findTransformECC_1(templateImage.nativeObj, inputImage.nativeObj, warpMatrix.nativeObj, motionType, criteria.type, criteria.maxCount, criteria.epsilon);
        
        return retVal;
    }

    //javadoc: findTransformECC(templateImage, inputImage, warpMatrix, motionType)
    public static double findTransformECC(Mat templateImage, Mat inputImage, Mat warpMatrix, int motionType)
    {
        
        double retVal = findTransformECC_2(templateImage.nativeObj, inputImage.nativeObj, warpMatrix.nativeObj, motionType);
        
        return retVal;
    }

    //javadoc: findTransformECC(templateImage, inputImage, warpMatrix)
    public static double findTransformECC(Mat templateImage, Mat inputImage, Mat warpMatrix)
    {
        
        double retVal = findTransformECC_3(templateImage.nativeObj, inputImage.nativeObj, warpMatrix.nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::buildOpticalFlowPyramid(Mat img, vector_Mat& pyramid, Size winSize, int maxLevel, bool withDerivatives = true, int pyrBorder = BORDER_REFLECT_101, int derivBorder = BORDER_CONSTANT, bool tryReuseInputImage = true)
    //

    //javadoc: buildOpticalFlowPyramid(img, pyramid, winSize, maxLevel, withDerivatives, pyrBorder, derivBorder, tryReuseInputImage)
    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel, boolean withDerivatives, int pyrBorder, int derivBorder, boolean tryReuseInputImage)
    {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_0(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel, withDerivatives, pyrBorder, derivBorder, tryReuseInputImage);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        pyramid_mat.release();
        return retVal;
    }

    //javadoc: buildOpticalFlowPyramid(img, pyramid, winSize, maxLevel, withDerivatives, pyrBorder, derivBorder)
    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel, boolean withDerivatives, int pyrBorder, int derivBorder)
    {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_1(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel, withDerivatives, pyrBorder, derivBorder);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        pyramid_mat.release();
        return retVal;
    }

    //javadoc: buildOpticalFlowPyramid(img, pyramid, winSize, maxLevel, withDerivatives, pyrBorder)
    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel, boolean withDerivatives, int pyrBorder)
    {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_2(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel, withDerivatives, pyrBorder);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        pyramid_mat.release();
        return retVal;
    }

    //javadoc: buildOpticalFlowPyramid(img, pyramid, winSize, maxLevel, withDerivatives)
    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel, boolean withDerivatives)
    {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_3(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel, withDerivatives);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        pyramid_mat.release();
        return retVal;
    }

    //javadoc: buildOpticalFlowPyramid(img, pyramid, winSize, maxLevel)
    public static int buildOpticalFlowPyramid(Mat img, List<Mat> pyramid, Size winSize, int maxLevel)
    {
        Mat pyramid_mat = new Mat();
        int retVal = buildOpticalFlowPyramid_4(img.nativeObj, pyramid_mat.nativeObj, winSize.width, winSize.height, maxLevel);
        Converters.Mat_to_vector_Mat(pyramid_mat, pyramid);
        pyramid_mat.release();
        return retVal;
    }


    //
    // C++:  int cv::meanShift(Mat probImage, Rect& window, TermCriteria criteria)
    //

    //javadoc: meanShift(probImage, window, criteria)
    public static int meanShift(Mat probImage, Rect window, TermCriteria criteria)
    {
        double[] window_out = new double[4];
        int retVal = meanShift_0(probImage.nativeObj, window.x, window.y, window.width, window.height, window_out, criteria.type, criteria.maxCount, criteria.epsilon);
        if(window!=null){ window.x = (int)window_out[0]; window.y = (int)window_out[1]; window.width = (int)window_out[2]; window.height = (int)window_out[3]; } 
        return retVal;
    }


    //
    // C++:  void cv::calcOpticalFlowFarneback(Mat prev, Mat next, Mat& flow, double pyr_scale, int levels, int winsize, int iterations, int poly_n, double poly_sigma, int flags)
    //

    //javadoc: calcOpticalFlowFarneback(prev, next, flow, pyr_scale, levels, winsize, iterations, poly_n, poly_sigma, flags)
    public static void calcOpticalFlowFarneback(Mat prev, Mat next, Mat flow, double pyr_scale, int levels, int winsize, int iterations, int poly_n, double poly_sigma, int flags)
    {
        
        calcOpticalFlowFarneback_0(prev.nativeObj, next.nativeObj, flow.nativeObj, pyr_scale, levels, winsize, iterations, poly_n, poly_sigma, flags);
        
        return;
    }


    //
    // C++:  void cv::calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, vector_Point2f prevPts, vector_Point2f& nextPts, vector_uchar& status, vector_float& err, Size winSize = Size(21,21), int maxLevel = 3, TermCriteria criteria = TermCriteria(TermCriteria::COUNT+TermCriteria::EPS, 30, 0.01), int flags = 0, double minEigThreshold = 1e-4)
    //

    //javadoc: calcOpticalFlowPyrLK(prevImg, nextImg, prevPts, nextPts, status, err, winSize, maxLevel, criteria, flags, minEigThreshold)
    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize, int maxLevel, TermCriteria criteria, int flags, double minEigThreshold)
    {
        Mat prevPts_mat = prevPts;
        Mat nextPts_mat = nextPts;
        Mat status_mat = status;
        Mat err_mat = err;
        calcOpticalFlowPyrLK_0(prevImg.nativeObj, nextImg.nativeObj, prevPts_mat.nativeObj, nextPts_mat.nativeObj, status_mat.nativeObj, err_mat.nativeObj, winSize.width, winSize.height, maxLevel, criteria.type, criteria.maxCount, criteria.epsilon, flags, minEigThreshold);
        
        return;
    }

    //javadoc: calcOpticalFlowPyrLK(prevImg, nextImg, prevPts, nextPts, status, err, winSize, maxLevel, criteria, flags)
    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize, int maxLevel, TermCriteria criteria, int flags)
    {
        Mat prevPts_mat = prevPts;
        Mat nextPts_mat = nextPts;
        Mat status_mat = status;
        Mat err_mat = err;
        calcOpticalFlowPyrLK_1(prevImg.nativeObj, nextImg.nativeObj, prevPts_mat.nativeObj, nextPts_mat.nativeObj, status_mat.nativeObj, err_mat.nativeObj, winSize.width, winSize.height, maxLevel, criteria.type, criteria.maxCount, criteria.epsilon, flags);
        
        return;
    }

    //javadoc: calcOpticalFlowPyrLK(prevImg, nextImg, prevPts, nextPts, status, err, winSize, maxLevel, criteria)
    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize, int maxLevel, TermCriteria criteria)
    {
        Mat prevPts_mat = prevPts;
        Mat nextPts_mat = nextPts;
        Mat status_mat = status;
        Mat err_mat = err;
        calcOpticalFlowPyrLK_2(prevImg.nativeObj, nextImg.nativeObj, prevPts_mat.nativeObj, nextPts_mat.nativeObj, status_mat.nativeObj, err_mat.nativeObj, winSize.width, winSize.height, maxLevel, criteria.type, criteria.maxCount, criteria.epsilon);
        
        return;
    }

    //javadoc: calcOpticalFlowPyrLK(prevImg, nextImg, prevPts, nextPts, status, err, winSize, maxLevel)
    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize, int maxLevel)
    {
        Mat prevPts_mat = prevPts;
        Mat nextPts_mat = nextPts;
        Mat status_mat = status;
        Mat err_mat = err;
        calcOpticalFlowPyrLK_3(prevImg.nativeObj, nextImg.nativeObj, prevPts_mat.nativeObj, nextPts_mat.nativeObj, status_mat.nativeObj, err_mat.nativeObj, winSize.width, winSize.height, maxLevel);
        
        return;
    }

    //javadoc: calcOpticalFlowPyrLK(prevImg, nextImg, prevPts, nextPts, status, err, winSize)
    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err, Size winSize)
    {
        Mat prevPts_mat = prevPts;
        Mat nextPts_mat = nextPts;
        Mat status_mat = status;
        Mat err_mat = err;
        calcOpticalFlowPyrLK_4(prevImg.nativeObj, nextImg.nativeObj, prevPts_mat.nativeObj, nextPts_mat.nativeObj, status_mat.nativeObj, err_mat.nativeObj, winSize.width, winSize.height);
        
        return;
    }

    //javadoc: calcOpticalFlowPyrLK(prevImg, nextImg, prevPts, nextPts, status, err)
    public static void calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, MatOfPoint2f prevPts, MatOfPoint2f nextPts, MatOfByte status, MatOfFloat err)
    {
        Mat prevPts_mat = prevPts;
        Mat nextPts_mat = nextPts;
        Mat status_mat = status;
        Mat err_mat = err;
        calcOpticalFlowPyrLK_5(prevImg.nativeObj, nextImg.nativeObj, prevPts_mat.nativeObj, nextPts_mat.nativeObj, status_mat.nativeObj, err_mat.nativeObj);
        
        return;
    }




    // C++:  Mat cv::readOpticalFlow(String path)
    private static native long readOpticalFlow_0(String path);

    // C++:  Ptr_BackgroundSubtractorKNN cv::createBackgroundSubtractorKNN(int history = 500, double dist2Threshold = 400.0, bool detectShadows = true)
    private static native long createBackgroundSubtractorKNN_0(int history, double dist2Threshold, boolean detectShadows);
    private static native long createBackgroundSubtractorKNN_1(int history, double dist2Threshold);
    private static native long createBackgroundSubtractorKNN_2(int history);
    private static native long createBackgroundSubtractorKNN_3();

    // C++:  Ptr_BackgroundSubtractorMOG2 cv::createBackgroundSubtractorMOG2(int history = 500, double varThreshold = 16, bool detectShadows = true)
    private static native long createBackgroundSubtractorMOG2_0(int history, double varThreshold, boolean detectShadows);
    private static native long createBackgroundSubtractorMOG2_1(int history, double varThreshold);
    private static native long createBackgroundSubtractorMOG2_2(int history);
    private static native long createBackgroundSubtractorMOG2_3();

    // C++:  RotatedRect cv::CamShift(Mat probImage, Rect& window, TermCriteria criteria)
    private static native double[] CamShift_0(long probImage_nativeObj, int window_x, int window_y, int window_width, int window_height, double[] window_out, int criteria_type, int criteria_maxCount, double criteria_epsilon);

    // C++:  bool cv::writeOpticalFlow(String path, Mat flow)
    private static native boolean writeOpticalFlow_0(String path, long flow_nativeObj);

    // C++:  double cv::findTransformECC(Mat templateImage, Mat inputImage, Mat& warpMatrix, int motionType = MOTION_AFFINE, TermCriteria criteria = TermCriteria(TermCriteria::COUNT+TermCriteria::EPS, 50, 0.001), Mat inputMask = Mat())
    private static native double findTransformECC_0(long templateImage_nativeObj, long inputImage_nativeObj, long warpMatrix_nativeObj, int motionType, int criteria_type, int criteria_maxCount, double criteria_epsilon, long inputMask_nativeObj);
    private static native double findTransformECC_1(long templateImage_nativeObj, long inputImage_nativeObj, long warpMatrix_nativeObj, int motionType, int criteria_type, int criteria_maxCount, double criteria_epsilon);
    private static native double findTransformECC_2(long templateImage_nativeObj, long inputImage_nativeObj, long warpMatrix_nativeObj, int motionType);
    private static native double findTransformECC_3(long templateImage_nativeObj, long inputImage_nativeObj, long warpMatrix_nativeObj);

    // C++:  int cv::buildOpticalFlowPyramid(Mat img, vector_Mat& pyramid, Size winSize, int maxLevel, bool withDerivatives = true, int pyrBorder = BORDER_REFLECT_101, int derivBorder = BORDER_CONSTANT, bool tryReuseInputImage = true)
    private static native int buildOpticalFlowPyramid_0(long img_nativeObj, long pyramid_mat_nativeObj, double winSize_width, double winSize_height, int maxLevel, boolean withDerivatives, int pyrBorder, int derivBorder, boolean tryReuseInputImage);
    private static native int buildOpticalFlowPyramid_1(long img_nativeObj, long pyramid_mat_nativeObj, double winSize_width, double winSize_height, int maxLevel, boolean withDerivatives, int pyrBorder, int derivBorder);
    private static native int buildOpticalFlowPyramid_2(long img_nativeObj, long pyramid_mat_nativeObj, double winSize_width, double winSize_height, int maxLevel, boolean withDerivatives, int pyrBorder);
    private static native int buildOpticalFlowPyramid_3(long img_nativeObj, long pyramid_mat_nativeObj, double winSize_width, double winSize_height, int maxLevel, boolean withDerivatives);
    private static native int buildOpticalFlowPyramid_4(long img_nativeObj, long pyramid_mat_nativeObj, double winSize_width, double winSize_height, int maxLevel);

    // C++:  int cv::meanShift(Mat probImage, Rect& window, TermCriteria criteria)
    private static native int meanShift_0(long probImage_nativeObj, int window_x, int window_y, int window_width, int window_height, double[] window_out, int criteria_type, int criteria_maxCount, double criteria_epsilon);

    // C++:  void cv::calcOpticalFlowFarneback(Mat prev, Mat next, Mat& flow, double pyr_scale, int levels, int winsize, int iterations, int poly_n, double poly_sigma, int flags)
    private static native void calcOpticalFlowFarneback_0(long prev_nativeObj, long next_nativeObj, long flow_nativeObj, double pyr_scale, int levels, int winsize, int iterations, int poly_n, double poly_sigma, int flags);

    // C++:  void cv::calcOpticalFlowPyrLK(Mat prevImg, Mat nextImg, vector_Point2f prevPts, vector_Point2f& nextPts, vector_uchar& status, vector_float& err, Size winSize = Size(21,21), int maxLevel = 3, TermCriteria criteria = TermCriteria(TermCriteria::COUNT+TermCriteria::EPS, 30, 0.01), int flags = 0, double minEigThreshold = 1e-4)
    private static native void calcOpticalFlowPyrLK_0(long prevImg_nativeObj, long nextImg_nativeObj, long prevPts_mat_nativeObj, long nextPts_mat_nativeObj, long status_mat_nativeObj, long err_mat_nativeObj, double winSize_width, double winSize_height, int maxLevel, int criteria_type, int criteria_maxCount, double criteria_epsilon, int flags, double minEigThreshold);
    private static native void calcOpticalFlowPyrLK_1(long prevImg_nativeObj, long nextImg_nativeObj, long prevPts_mat_nativeObj, long nextPts_mat_nativeObj, long status_mat_nativeObj, long err_mat_nativeObj, double winSize_width, double winSize_height, int maxLevel, int criteria_type, int criteria_maxCount, double criteria_epsilon, int flags);
    private static native void calcOpticalFlowPyrLK_2(long prevImg_nativeObj, long nextImg_nativeObj, long prevPts_mat_nativeObj, long nextPts_mat_nativeObj, long status_mat_nativeObj, long err_mat_nativeObj, double winSize_width, double winSize_height, int maxLevel, int criteria_type, int criteria_maxCount, double criteria_epsilon);
    private static native void calcOpticalFlowPyrLK_3(long prevImg_nativeObj, long nextImg_nativeObj, long prevPts_mat_nativeObj, long nextPts_mat_nativeObj, long status_mat_nativeObj, long err_mat_nativeObj, double winSize_width, double winSize_height, int maxLevel);
    private static native void calcOpticalFlowPyrLK_4(long prevImg_nativeObj, long nextImg_nativeObj, long prevPts_mat_nativeObj, long nextPts_mat_nativeObj, long status_mat_nativeObj, long err_mat_nativeObj, double winSize_width, double winSize_height);
    private static native void calcOpticalFlowPyrLK_5(long prevImg_nativeObj, long nextImg_nativeObj, long prevPts_mat_nativeObj, long nextPts_mat_nativeObj, long status_mat_nativeObj, long err_mat_nativeObj);

}
