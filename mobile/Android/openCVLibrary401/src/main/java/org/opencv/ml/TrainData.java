//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.ml;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.ml.TrainData;
import org.opencv.utils.Converters;

// C++: class TrainData
//javadoc: TrainData

public class TrainData {

    protected final long nativeObj;
    protected TrainData(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static TrainData __fromPtr__(long addr) { return new TrainData(addr); }

    //
    // C++:  Mat cv::ml::TrainData::getCatMap()
    //

    //javadoc: TrainData::getCatMap()
    public  Mat getCatMap()
    {
        
        Mat retVal = new Mat(getCatMap_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getCatOfs()
    //

    //javadoc: TrainData::getCatOfs()
    public  Mat getCatOfs()
    {
        
        Mat retVal = new Mat(getCatOfs_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getClassLabels()
    //

    //javadoc: TrainData::getClassLabels()
    public  Mat getClassLabels()
    {
        
        Mat retVal = new Mat(getClassLabels_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getDefaultSubstValues()
    //

    //javadoc: TrainData::getDefaultSubstValues()
    public  Mat getDefaultSubstValues()
    {
        
        Mat retVal = new Mat(getDefaultSubstValues_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getMissing()
    //

    //javadoc: TrainData::getMissing()
    public  Mat getMissing()
    {
        
        Mat retVal = new Mat(getMissing_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getNormCatResponses()
    //

    //javadoc: TrainData::getNormCatResponses()
    public  Mat getNormCatResponses()
    {
        
        Mat retVal = new Mat(getNormCatResponses_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getResponses()
    //

    //javadoc: TrainData::getResponses()
    public  Mat getResponses()
    {
        
        Mat retVal = new Mat(getResponses_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getSampleWeights()
    //

    //javadoc: TrainData::getSampleWeights()
    public  Mat getSampleWeights()
    {
        
        Mat retVal = new Mat(getSampleWeights_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getSamples()
    //

    //javadoc: TrainData::getSamples()
    public  Mat getSamples()
    {
        
        Mat retVal = new Mat(getSamples_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: static Mat cv::ml::TrainData::getSubMatrix(Mat matrix, Mat idx, int layout)
    //

    //javadoc: TrainData::getSubMatrix(matrix, idx, layout)
    public static Mat getSubMatrix(Mat matrix, Mat idx, int layout)
    {
        
        Mat retVal = new Mat(getSubMatrix_0(matrix.nativeObj, idx.nativeObj, layout));
        
        return retVal;
    }


    //
    // C++: static Mat cv::ml::TrainData::getSubVector(Mat vec, Mat idx)
    //

    //javadoc: TrainData::getSubVector(vec, idx)
    public static Mat getSubVector(Mat vec, Mat idx)
    {
        
        Mat retVal = new Mat(getSubVector_0(vec.nativeObj, idx.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getTestNormCatResponses()
    //

    //javadoc: TrainData::getTestNormCatResponses()
    public  Mat getTestNormCatResponses()
    {
        
        Mat retVal = new Mat(getTestNormCatResponses_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getTestResponses()
    //

    //javadoc: TrainData::getTestResponses()
    public  Mat getTestResponses()
    {
        
        Mat retVal = new Mat(getTestResponses_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getTestSampleIdx()
    //

    //javadoc: TrainData::getTestSampleIdx()
    public  Mat getTestSampleIdx()
    {
        
        Mat retVal = new Mat(getTestSampleIdx_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getTestSampleWeights()
    //

    //javadoc: TrainData::getTestSampleWeights()
    public  Mat getTestSampleWeights()
    {
        
        Mat retVal = new Mat(getTestSampleWeights_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getTestSamples()
    //

    //javadoc: TrainData::getTestSamples()
    public  Mat getTestSamples()
    {
        
        Mat retVal = new Mat(getTestSamples_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getTrainNormCatResponses()
    //

    //javadoc: TrainData::getTrainNormCatResponses()
    public  Mat getTrainNormCatResponses()
    {
        
        Mat retVal = new Mat(getTrainNormCatResponses_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getTrainResponses()
    //

    //javadoc: TrainData::getTrainResponses()
    public  Mat getTrainResponses()
    {
        
        Mat retVal = new Mat(getTrainResponses_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getTrainSampleIdx()
    //

    //javadoc: TrainData::getTrainSampleIdx()
    public  Mat getTrainSampleIdx()
    {
        
        Mat retVal = new Mat(getTrainSampleIdx_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getTrainSampleWeights()
    //

    //javadoc: TrainData::getTrainSampleWeights()
    public  Mat getTrainSampleWeights()
    {
        
        Mat retVal = new Mat(getTrainSampleWeights_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getTrainSamples(int layout = ROW_SAMPLE, bool compressSamples = true, bool compressVars = true)
    //

    //javadoc: TrainData::getTrainSamples(layout, compressSamples, compressVars)
    public  Mat getTrainSamples(int layout, boolean compressSamples, boolean compressVars)
    {
        
        Mat retVal = new Mat(getTrainSamples_0(nativeObj, layout, compressSamples, compressVars));
        
        return retVal;
    }

    //javadoc: TrainData::getTrainSamples(layout, compressSamples)
    public  Mat getTrainSamples(int layout, boolean compressSamples)
    {
        
        Mat retVal = new Mat(getTrainSamples_1(nativeObj, layout, compressSamples));
        
        return retVal;
    }

    //javadoc: TrainData::getTrainSamples(layout)
    public  Mat getTrainSamples(int layout)
    {
        
        Mat retVal = new Mat(getTrainSamples_2(nativeObj, layout));
        
        return retVal;
    }

    //javadoc: TrainData::getTrainSamples()
    public  Mat getTrainSamples()
    {
        
        Mat retVal = new Mat(getTrainSamples_3(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getVarIdx()
    //

    //javadoc: TrainData::getVarIdx()
    public  Mat getVarIdx()
    {
        
        Mat retVal = new Mat(getVarIdx_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getVarSymbolFlags()
    //

    //javadoc: TrainData::getVarSymbolFlags()
    public  Mat getVarSymbolFlags()
    {
        
        Mat retVal = new Mat(getVarSymbolFlags_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::TrainData::getVarType()
    //

    //javadoc: TrainData::getVarType()
    public  Mat getVarType()
    {
        
        Mat retVal = new Mat(getVarType_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: static Ptr_TrainData cv::ml::TrainData::create(Mat samples, int layout, Mat responses, Mat varIdx = Mat(), Mat sampleIdx = Mat(), Mat sampleWeights = Mat(), Mat varType = Mat())
    //

    //javadoc: TrainData::create(samples, layout, responses, varIdx, sampleIdx, sampleWeights, varType)
    public static TrainData create(Mat samples, int layout, Mat responses, Mat varIdx, Mat sampleIdx, Mat sampleWeights, Mat varType)
    {
        
        TrainData retVal = TrainData.__fromPtr__(create_0(samples.nativeObj, layout, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, sampleWeights.nativeObj, varType.nativeObj));
        
        return retVal;
    }

    //javadoc: TrainData::create(samples, layout, responses, varIdx, sampleIdx, sampleWeights)
    public static TrainData create(Mat samples, int layout, Mat responses, Mat varIdx, Mat sampleIdx, Mat sampleWeights)
    {
        
        TrainData retVal = TrainData.__fromPtr__(create_1(samples.nativeObj, layout, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj, sampleWeights.nativeObj));
        
        return retVal;
    }

    //javadoc: TrainData::create(samples, layout, responses, varIdx, sampleIdx)
    public static TrainData create(Mat samples, int layout, Mat responses, Mat varIdx, Mat sampleIdx)
    {
        
        TrainData retVal = TrainData.__fromPtr__(create_2(samples.nativeObj, layout, responses.nativeObj, varIdx.nativeObj, sampleIdx.nativeObj));
        
        return retVal;
    }

    //javadoc: TrainData::create(samples, layout, responses, varIdx)
    public static TrainData create(Mat samples, int layout, Mat responses, Mat varIdx)
    {
        
        TrainData retVal = TrainData.__fromPtr__(create_3(samples.nativeObj, layout, responses.nativeObj, varIdx.nativeObj));
        
        return retVal;
    }

    //javadoc: TrainData::create(samples, layout, responses)
    public static TrainData create(Mat samples, int layout, Mat responses)
    {
        
        TrainData retVal = TrainData.__fromPtr__(create_4(samples.nativeObj, layout, responses.nativeObj));
        
        return retVal;
    }


    //
    // C++:  int cv::ml::TrainData::getCatCount(int vi)
    //

    //javadoc: TrainData::getCatCount(vi)
    public  int getCatCount(int vi)
    {
        
        int retVal = getCatCount_0(nativeObj, vi);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::TrainData::getLayout()
    //

    //javadoc: TrainData::getLayout()
    public  int getLayout()
    {
        
        int retVal = getLayout_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::TrainData::getNAllVars()
    //

    //javadoc: TrainData::getNAllVars()
    public  int getNAllVars()
    {
        
        int retVal = getNAllVars_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::TrainData::getNSamples()
    //

    //javadoc: TrainData::getNSamples()
    public  int getNSamples()
    {
        
        int retVal = getNSamples_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::TrainData::getNTestSamples()
    //

    //javadoc: TrainData::getNTestSamples()
    public  int getNTestSamples()
    {
        
        int retVal = getNTestSamples_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::TrainData::getNTrainSamples()
    //

    //javadoc: TrainData::getNTrainSamples()
    public  int getNTrainSamples()
    {
        
        int retVal = getNTrainSamples_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::TrainData::getNVars()
    //

    //javadoc: TrainData::getNVars()
    public  int getNVars()
    {
        
        int retVal = getNVars_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::TrainData::getResponseType()
    //

    //javadoc: TrainData::getResponseType()
    public  int getResponseType()
    {
        
        int retVal = getResponseType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::ml::TrainData::getNames(vector_String names)
    //

    //javadoc: TrainData::getNames(names)
    public  void getNames(List<String> names)
    {
        
        getNames_0(nativeObj, names);
        
        return;
    }


    //
    // C++:  void cv::ml::TrainData::getSample(Mat varIdx, int sidx, float* buf)
    //

    //javadoc: TrainData::getSample(varIdx, sidx, buf)
    public  void getSample(Mat varIdx, int sidx, float buf)
    {
        
        getSample_0(nativeObj, varIdx.nativeObj, sidx, buf);
        
        return;
    }


    //
    // C++:  void cv::ml::TrainData::getValues(int vi, Mat sidx, float* values)
    //

    //javadoc: TrainData::getValues(vi, sidx, values)
    public  void getValues(int vi, Mat sidx, float values)
    {
        
        getValues_0(nativeObj, vi, sidx.nativeObj, values);
        
        return;
    }


    //
    // C++:  void cv::ml::TrainData::setTrainTestSplit(int count, bool shuffle = true)
    //

    //javadoc: TrainData::setTrainTestSplit(count, shuffle)
    public  void setTrainTestSplit(int count, boolean shuffle)
    {
        
        setTrainTestSplit_0(nativeObj, count, shuffle);
        
        return;
    }

    //javadoc: TrainData::setTrainTestSplit(count)
    public  void setTrainTestSplit(int count)
    {
        
        setTrainTestSplit_1(nativeObj, count);
        
        return;
    }


    //
    // C++:  void cv::ml::TrainData::setTrainTestSplitRatio(double ratio, bool shuffle = true)
    //

    //javadoc: TrainData::setTrainTestSplitRatio(ratio, shuffle)
    public  void setTrainTestSplitRatio(double ratio, boolean shuffle)
    {
        
        setTrainTestSplitRatio_0(nativeObj, ratio, shuffle);
        
        return;
    }

    //javadoc: TrainData::setTrainTestSplitRatio(ratio)
    public  void setTrainTestSplitRatio(double ratio)
    {
        
        setTrainTestSplitRatio_1(nativeObj, ratio);
        
        return;
    }


    //
    // C++:  void cv::ml::TrainData::shuffleTrainTest()
    //

    //javadoc: TrainData::shuffleTrainTest()
    public  void shuffleTrainTest()
    {
        
        shuffleTrainTest_0(nativeObj);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  Mat cv::ml::TrainData::getCatMap()
    private static native long getCatMap_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getCatOfs()
    private static native long getCatOfs_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getClassLabels()
    private static native long getClassLabels_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getDefaultSubstValues()
    private static native long getDefaultSubstValues_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getMissing()
    private static native long getMissing_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getNormCatResponses()
    private static native long getNormCatResponses_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getResponses()
    private static native long getResponses_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getSampleWeights()
    private static native long getSampleWeights_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getSamples()
    private static native long getSamples_0(long nativeObj);

    // C++: static Mat cv::ml::TrainData::getSubMatrix(Mat matrix, Mat idx, int layout)
    private static native long getSubMatrix_0(long matrix_nativeObj, long idx_nativeObj, int layout);

    // C++: static Mat cv::ml::TrainData::getSubVector(Mat vec, Mat idx)
    private static native long getSubVector_0(long vec_nativeObj, long idx_nativeObj);

    // C++:  Mat cv::ml::TrainData::getTestNormCatResponses()
    private static native long getTestNormCatResponses_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getTestResponses()
    private static native long getTestResponses_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getTestSampleIdx()
    private static native long getTestSampleIdx_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getTestSampleWeights()
    private static native long getTestSampleWeights_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getTestSamples()
    private static native long getTestSamples_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getTrainNormCatResponses()
    private static native long getTrainNormCatResponses_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getTrainResponses()
    private static native long getTrainResponses_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getTrainSampleIdx()
    private static native long getTrainSampleIdx_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getTrainSampleWeights()
    private static native long getTrainSampleWeights_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getTrainSamples(int layout = ROW_SAMPLE, bool compressSamples = true, bool compressVars = true)
    private static native long getTrainSamples_0(long nativeObj, int layout, boolean compressSamples, boolean compressVars);
    private static native long getTrainSamples_1(long nativeObj, int layout, boolean compressSamples);
    private static native long getTrainSamples_2(long nativeObj, int layout);
    private static native long getTrainSamples_3(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getVarIdx()
    private static native long getVarIdx_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getVarSymbolFlags()
    private static native long getVarSymbolFlags_0(long nativeObj);

    // C++:  Mat cv::ml::TrainData::getVarType()
    private static native long getVarType_0(long nativeObj);

    // C++: static Ptr_TrainData cv::ml::TrainData::create(Mat samples, int layout, Mat responses, Mat varIdx = Mat(), Mat sampleIdx = Mat(), Mat sampleWeights = Mat(), Mat varType = Mat())
    private static native long create_0(long samples_nativeObj, int layout, long responses_nativeObj, long varIdx_nativeObj, long sampleIdx_nativeObj, long sampleWeights_nativeObj, long varType_nativeObj);
    private static native long create_1(long samples_nativeObj, int layout, long responses_nativeObj, long varIdx_nativeObj, long sampleIdx_nativeObj, long sampleWeights_nativeObj);
    private static native long create_2(long samples_nativeObj, int layout, long responses_nativeObj, long varIdx_nativeObj, long sampleIdx_nativeObj);
    private static native long create_3(long samples_nativeObj, int layout, long responses_nativeObj, long varIdx_nativeObj);
    private static native long create_4(long samples_nativeObj, int layout, long responses_nativeObj);

    // C++:  int cv::ml::TrainData::getCatCount(int vi)
    private static native int getCatCount_0(long nativeObj, int vi);

    // C++:  int cv::ml::TrainData::getLayout()
    private static native int getLayout_0(long nativeObj);

    // C++:  int cv::ml::TrainData::getNAllVars()
    private static native int getNAllVars_0(long nativeObj);

    // C++:  int cv::ml::TrainData::getNSamples()
    private static native int getNSamples_0(long nativeObj);

    // C++:  int cv::ml::TrainData::getNTestSamples()
    private static native int getNTestSamples_0(long nativeObj);

    // C++:  int cv::ml::TrainData::getNTrainSamples()
    private static native int getNTrainSamples_0(long nativeObj);

    // C++:  int cv::ml::TrainData::getNVars()
    private static native int getNVars_0(long nativeObj);

    // C++:  int cv::ml::TrainData::getResponseType()
    private static native int getResponseType_0(long nativeObj);

    // C++:  void cv::ml::TrainData::getNames(vector_String names)
    private static native void getNames_0(long nativeObj, List<String> names);

    // C++:  void cv::ml::TrainData::getSample(Mat varIdx, int sidx, float* buf)
    private static native void getSample_0(long nativeObj, long varIdx_nativeObj, int sidx, float buf);

    // C++:  void cv::ml::TrainData::getValues(int vi, Mat sidx, float* values)
    private static native void getValues_0(long nativeObj, int vi, long sidx_nativeObj, float values);

    // C++:  void cv::ml::TrainData::setTrainTestSplit(int count, bool shuffle = true)
    private static native void setTrainTestSplit_0(long nativeObj, int count, boolean shuffle);
    private static native void setTrainTestSplit_1(long nativeObj, int count);

    // C++:  void cv::ml::TrainData::setTrainTestSplitRatio(double ratio, bool shuffle = true)
    private static native void setTrainTestSplitRatio_0(long nativeObj, double ratio, boolean shuffle);
    private static native void setTrainTestSplitRatio_1(long nativeObj, double ratio);

    // C++:  void cv::ml::TrainData::shuffleTrainTest()
    private static native void shuffleTrainTest_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
