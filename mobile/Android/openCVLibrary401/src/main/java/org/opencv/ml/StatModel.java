//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.ml;

import org.opencv.core.Algorithm;
import org.opencv.core.Mat;
import org.opencv.ml.TrainData;

// C++: class StatModel
//javadoc: StatModel

public class StatModel extends Algorithm {

    protected StatModel(long addr) { super(addr); }

    // internal usage only
    public static StatModel __fromPtr__(long addr) { return new StatModel(addr); }

    // C++: enum Flags
    public static final int
            UPDATE_MODEL = 1,
            RAW_OUTPUT = 1,
            COMPRESSED_INPUT = 2,
            PREPROCESSED_INPUT = 4;


    //
    // C++:  bool cv::ml::StatModel::empty()
    //

    //javadoc: StatModel::empty()
    public  boolean empty()
    {
        
        boolean retVal = empty_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::ml::StatModel::isClassifier()
    //

    //javadoc: StatModel::isClassifier()
    public  boolean isClassifier()
    {
        
        boolean retVal = isClassifier_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::ml::StatModel::isTrained()
    //

    //javadoc: StatModel::isTrained()
    public  boolean isTrained()
    {
        
        boolean retVal = isTrained_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::ml::StatModel::train(Mat samples, int layout, Mat responses)
    //

    //javadoc: StatModel::train(samples, layout, responses)
    public  boolean train(Mat samples, int layout, Mat responses)
    {
        
        boolean retVal = train_0(nativeObj, samples.nativeObj, layout, responses.nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::ml::StatModel::train(Ptr_TrainData trainData, int flags = 0)
    //

    //javadoc: StatModel::train(trainData, flags)
    public  boolean train(TrainData trainData, int flags)
    {
        
        boolean retVal = train_1(nativeObj, trainData.getNativeObjAddr(), flags);
        
        return retVal;
    }

    //javadoc: StatModel::train(trainData)
    public  boolean train(TrainData trainData)
    {
        
        boolean retVal = train_2(nativeObj, trainData.getNativeObjAddr());
        
        return retVal;
    }


    //
    // C++:  float cv::ml::StatModel::calcError(Ptr_TrainData data, bool test, Mat& resp)
    //

    //javadoc: StatModel::calcError(data, test, resp)
    public  float calcError(TrainData data, boolean test, Mat resp)
    {
        
        float retVal = calcError_0(nativeObj, data.getNativeObjAddr(), test, resp.nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::ml::StatModel::predict(Mat samples, Mat& results = Mat(), int flags = 0)
    //

    //javadoc: StatModel::predict(samples, results, flags)
    public  float predict(Mat samples, Mat results, int flags)
    {
        
        float retVal = predict_0(nativeObj, samples.nativeObj, results.nativeObj, flags);
        
        return retVal;
    }

    //javadoc: StatModel::predict(samples, results)
    public  float predict(Mat samples, Mat results)
    {
        
        float retVal = predict_1(nativeObj, samples.nativeObj, results.nativeObj);
        
        return retVal;
    }

    //javadoc: StatModel::predict(samples)
    public  float predict(Mat samples)
    {
        
        float retVal = predict_2(nativeObj, samples.nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::StatModel::getVarCount()
    //

    //javadoc: StatModel::getVarCount()
    public  int getVarCount()
    {
        
        int retVal = getVarCount_0(nativeObj);
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  bool cv::ml::StatModel::empty()
    private static native boolean empty_0(long nativeObj);

    // C++:  bool cv::ml::StatModel::isClassifier()
    private static native boolean isClassifier_0(long nativeObj);

    // C++:  bool cv::ml::StatModel::isTrained()
    private static native boolean isTrained_0(long nativeObj);

    // C++:  bool cv::ml::StatModel::train(Mat samples, int layout, Mat responses)
    private static native boolean train_0(long nativeObj, long samples_nativeObj, int layout, long responses_nativeObj);

    // C++:  bool cv::ml::StatModel::train(Ptr_TrainData trainData, int flags = 0)
    private static native boolean train_1(long nativeObj, long trainData_nativeObj, int flags);
    private static native boolean train_2(long nativeObj, long trainData_nativeObj);

    // C++:  float cv::ml::StatModel::calcError(Ptr_TrainData data, bool test, Mat& resp)
    private static native float calcError_0(long nativeObj, long data_nativeObj, boolean test, long resp_nativeObj);

    // C++:  float cv::ml::StatModel::predict(Mat samples, Mat& results = Mat(), int flags = 0)
    private static native float predict_0(long nativeObj, long samples_nativeObj, long results_nativeObj, int flags);
    private static native float predict_1(long nativeObj, long samples_nativeObj, long results_nativeObj);
    private static native float predict_2(long nativeObj, long samples_nativeObj);

    // C++:  int cv::ml::StatModel::getVarCount()
    private static native int getVarCount_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
