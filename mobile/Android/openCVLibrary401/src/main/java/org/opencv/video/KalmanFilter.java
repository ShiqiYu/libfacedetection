//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;

import org.opencv.core.Mat;

// C++: class KalmanFilter
//javadoc: KalmanFilter

public class KalmanFilter {

    protected final long nativeObj;
    protected KalmanFilter(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static KalmanFilter __fromPtr__(long addr) { return new KalmanFilter(addr); }

    //
    // C++:   cv::KalmanFilter::KalmanFilter(int dynamParams, int measureParams, int controlParams = 0, int type = CV_32F)
    //

    //javadoc: KalmanFilter::KalmanFilter(dynamParams, measureParams, controlParams, type)
    public   KalmanFilter(int dynamParams, int measureParams, int controlParams, int type)
    {
        
        nativeObj = KalmanFilter_0(dynamParams, measureParams, controlParams, type);
        
        return;
    }

    //javadoc: KalmanFilter::KalmanFilter(dynamParams, measureParams, controlParams)
    public   KalmanFilter(int dynamParams, int measureParams, int controlParams)
    {
        
        nativeObj = KalmanFilter_1(dynamParams, measureParams, controlParams);
        
        return;
    }

    //javadoc: KalmanFilter::KalmanFilter(dynamParams, measureParams)
    public   KalmanFilter(int dynamParams, int measureParams)
    {
        
        nativeObj = KalmanFilter_2(dynamParams, measureParams);
        
        return;
    }


    //
    // C++:   cv::KalmanFilter::KalmanFilter()
    //

    //javadoc: KalmanFilter::KalmanFilter()
    public   KalmanFilter()
    {
        
        nativeObj = KalmanFilter_3();
        
        return;
    }


    //
    // C++:  Mat cv::KalmanFilter::correct(Mat measurement)
    //

    //javadoc: KalmanFilter::correct(measurement)
    public  Mat correct(Mat measurement)
    {
        
        Mat retVal = new Mat(correct_0(nativeObj, measurement.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::KalmanFilter::predict(Mat control = Mat())
    //

    //javadoc: KalmanFilter::predict(control)
    public  Mat predict(Mat control)
    {
        
        Mat retVal = new Mat(predict_0(nativeObj, control.nativeObj));
        
        return retVal;
    }

    //javadoc: KalmanFilter::predict()
    public  Mat predict()
    {
        
        Mat retVal = new Mat(predict_1(nativeObj));
        
        return retVal;
    }


    //
    // C++: Mat KalmanFilter::statePre
    //

    //javadoc: KalmanFilter::get_statePre()
    public  Mat get_statePre()
    {
        
        Mat retVal = new Mat(get_statePre_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: void KalmanFilter::statePre
    //

    //javadoc: KalmanFilter::set_statePre(statePre)
    public  void set_statePre(Mat statePre)
    {
        
        set_statePre_0(nativeObj, statePre.nativeObj);
        
        return;
    }


    //
    // C++: Mat KalmanFilter::statePost
    //

    //javadoc: KalmanFilter::get_statePost()
    public  Mat get_statePost()
    {
        
        Mat retVal = new Mat(get_statePost_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: void KalmanFilter::statePost
    //

    //javadoc: KalmanFilter::set_statePost(statePost)
    public  void set_statePost(Mat statePost)
    {
        
        set_statePost_0(nativeObj, statePost.nativeObj);
        
        return;
    }


    //
    // C++: Mat KalmanFilter::transitionMatrix
    //

    //javadoc: KalmanFilter::get_transitionMatrix()
    public  Mat get_transitionMatrix()
    {
        
        Mat retVal = new Mat(get_transitionMatrix_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: void KalmanFilter::transitionMatrix
    //

    //javadoc: KalmanFilter::set_transitionMatrix(transitionMatrix)
    public  void set_transitionMatrix(Mat transitionMatrix)
    {
        
        set_transitionMatrix_0(nativeObj, transitionMatrix.nativeObj);
        
        return;
    }


    //
    // C++: Mat KalmanFilter::controlMatrix
    //

    //javadoc: KalmanFilter::get_controlMatrix()
    public  Mat get_controlMatrix()
    {
        
        Mat retVal = new Mat(get_controlMatrix_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: void KalmanFilter::controlMatrix
    //

    //javadoc: KalmanFilter::set_controlMatrix(controlMatrix)
    public  void set_controlMatrix(Mat controlMatrix)
    {
        
        set_controlMatrix_0(nativeObj, controlMatrix.nativeObj);
        
        return;
    }


    //
    // C++: Mat KalmanFilter::measurementMatrix
    //

    //javadoc: KalmanFilter::get_measurementMatrix()
    public  Mat get_measurementMatrix()
    {
        
        Mat retVal = new Mat(get_measurementMatrix_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: void KalmanFilter::measurementMatrix
    //

    //javadoc: KalmanFilter::set_measurementMatrix(measurementMatrix)
    public  void set_measurementMatrix(Mat measurementMatrix)
    {
        
        set_measurementMatrix_0(nativeObj, measurementMatrix.nativeObj);
        
        return;
    }


    //
    // C++: Mat KalmanFilter::processNoiseCov
    //

    //javadoc: KalmanFilter::get_processNoiseCov()
    public  Mat get_processNoiseCov()
    {
        
        Mat retVal = new Mat(get_processNoiseCov_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: void KalmanFilter::processNoiseCov
    //

    //javadoc: KalmanFilter::set_processNoiseCov(processNoiseCov)
    public  void set_processNoiseCov(Mat processNoiseCov)
    {
        
        set_processNoiseCov_0(nativeObj, processNoiseCov.nativeObj);
        
        return;
    }


    //
    // C++: Mat KalmanFilter::measurementNoiseCov
    //

    //javadoc: KalmanFilter::get_measurementNoiseCov()
    public  Mat get_measurementNoiseCov()
    {
        
        Mat retVal = new Mat(get_measurementNoiseCov_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: void KalmanFilter::measurementNoiseCov
    //

    //javadoc: KalmanFilter::set_measurementNoiseCov(measurementNoiseCov)
    public  void set_measurementNoiseCov(Mat measurementNoiseCov)
    {
        
        set_measurementNoiseCov_0(nativeObj, measurementNoiseCov.nativeObj);
        
        return;
    }


    //
    // C++: Mat KalmanFilter::errorCovPre
    //

    //javadoc: KalmanFilter::get_errorCovPre()
    public  Mat get_errorCovPre()
    {
        
        Mat retVal = new Mat(get_errorCovPre_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: void KalmanFilter::errorCovPre
    //

    //javadoc: KalmanFilter::set_errorCovPre(errorCovPre)
    public  void set_errorCovPre(Mat errorCovPre)
    {
        
        set_errorCovPre_0(nativeObj, errorCovPre.nativeObj);
        
        return;
    }


    //
    // C++: Mat KalmanFilter::gain
    //

    //javadoc: KalmanFilter::get_gain()
    public  Mat get_gain()
    {
        
        Mat retVal = new Mat(get_gain_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: void KalmanFilter::gain
    //

    //javadoc: KalmanFilter::set_gain(gain)
    public  void set_gain(Mat gain)
    {
        
        set_gain_0(nativeObj, gain.nativeObj);
        
        return;
    }


    //
    // C++: Mat KalmanFilter::errorCovPost
    //

    //javadoc: KalmanFilter::get_errorCovPost()
    public  Mat get_errorCovPost()
    {
        
        Mat retVal = new Mat(get_errorCovPost_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: void KalmanFilter::errorCovPost
    //

    //javadoc: KalmanFilter::set_errorCovPost(errorCovPost)
    public  void set_errorCovPost(Mat errorCovPost)
    {
        
        set_errorCovPost_0(nativeObj, errorCovPost.nativeObj);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::KalmanFilter::KalmanFilter(int dynamParams, int measureParams, int controlParams = 0, int type = CV_32F)
    private static native long KalmanFilter_0(int dynamParams, int measureParams, int controlParams, int type);
    private static native long KalmanFilter_1(int dynamParams, int measureParams, int controlParams);
    private static native long KalmanFilter_2(int dynamParams, int measureParams);

    // C++:   cv::KalmanFilter::KalmanFilter()
    private static native long KalmanFilter_3();

    // C++:  Mat cv::KalmanFilter::correct(Mat measurement)
    private static native long correct_0(long nativeObj, long measurement_nativeObj);

    // C++:  Mat cv::KalmanFilter::predict(Mat control = Mat())
    private static native long predict_0(long nativeObj, long control_nativeObj);
    private static native long predict_1(long nativeObj);

    // C++: Mat KalmanFilter::statePre
    private static native long get_statePre_0(long nativeObj);

    // C++: void KalmanFilter::statePre
    private static native void set_statePre_0(long nativeObj, long statePre_nativeObj);

    // C++: Mat KalmanFilter::statePost
    private static native long get_statePost_0(long nativeObj);

    // C++: void KalmanFilter::statePost
    private static native void set_statePost_0(long nativeObj, long statePost_nativeObj);

    // C++: Mat KalmanFilter::transitionMatrix
    private static native long get_transitionMatrix_0(long nativeObj);

    // C++: void KalmanFilter::transitionMatrix
    private static native void set_transitionMatrix_0(long nativeObj, long transitionMatrix_nativeObj);

    // C++: Mat KalmanFilter::controlMatrix
    private static native long get_controlMatrix_0(long nativeObj);

    // C++: void KalmanFilter::controlMatrix
    private static native void set_controlMatrix_0(long nativeObj, long controlMatrix_nativeObj);

    // C++: Mat KalmanFilter::measurementMatrix
    private static native long get_measurementMatrix_0(long nativeObj);

    // C++: void KalmanFilter::measurementMatrix
    private static native void set_measurementMatrix_0(long nativeObj, long measurementMatrix_nativeObj);

    // C++: Mat KalmanFilter::processNoiseCov
    private static native long get_processNoiseCov_0(long nativeObj);

    // C++: void KalmanFilter::processNoiseCov
    private static native void set_processNoiseCov_0(long nativeObj, long processNoiseCov_nativeObj);

    // C++: Mat KalmanFilter::measurementNoiseCov
    private static native long get_measurementNoiseCov_0(long nativeObj);

    // C++: void KalmanFilter::measurementNoiseCov
    private static native void set_measurementNoiseCov_0(long nativeObj, long measurementNoiseCov_nativeObj);

    // C++: Mat KalmanFilter::errorCovPre
    private static native long get_errorCovPre_0(long nativeObj);

    // C++: void KalmanFilter::errorCovPre
    private static native void set_errorCovPre_0(long nativeObj, long errorCovPre_nativeObj);

    // C++: Mat KalmanFilter::gain
    private static native long get_gain_0(long nativeObj);

    // C++: void KalmanFilter::gain
    private static native void set_gain_0(long nativeObj, long gain_nativeObj);

    // C++: Mat KalmanFilter::errorCovPost
    private static native long get_errorCovPost_0(long nativeObj);

    // C++: void KalmanFilter::errorCovPost
    private static native void set_errorCovPost_0(long nativeObj, long errorCovPost_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
