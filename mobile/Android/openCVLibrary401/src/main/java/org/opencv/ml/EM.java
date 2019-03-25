//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.ml;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.EM;
import org.opencv.ml.StatModel;
import org.opencv.utils.Converters;

// C++: class EM
//javadoc: EM

public class EM extends StatModel {

    protected EM(long addr) { super(addr); }

    // internal usage only
    public static EM __fromPtr__(long addr) { return new EM(addr); }

    // C++: enum <unnamed>
    public static final int
            DEFAULT_NCLUSTERS = 5,
            DEFAULT_MAX_ITERS = 100,
            START_E_STEP = 1,
            START_M_STEP = 2,
            START_AUTO_STEP = 0;


    // C++: enum Types
    public static final int
            COV_MAT_SPHERICAL = 0,
            COV_MAT_DIAGONAL = 1,
            COV_MAT_GENERIC = 2,
            COV_MAT_DEFAULT = COV_MAT_DIAGONAL;


    //
    // C++:  Mat cv::ml::EM::getMeans()
    //

    //javadoc: EM::getMeans()
    public  Mat getMeans()
    {
        
        Mat retVal = new Mat(getMeans_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::EM::getWeights()
    //

    //javadoc: EM::getWeights()
    public  Mat getWeights()
    {
        
        Mat retVal = new Mat(getWeights_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: static Ptr_EM cv::ml::EM::create()
    //

    //javadoc: EM::create()
    public static EM create()
    {
        
        EM retVal = EM.__fromPtr__(create_0());
        
        return retVal;
    }


    //
    // C++: static Ptr_EM cv::ml::EM::load(String filepath, String nodeName = String())
    //

    //javadoc: EM::load(filepath, nodeName)
    public static EM load(String filepath, String nodeName)
    {
        
        EM retVal = EM.__fromPtr__(load_0(filepath, nodeName));
        
        return retVal;
    }

    //javadoc: EM::load(filepath)
    public static EM load(String filepath)
    {
        
        EM retVal = EM.__fromPtr__(load_1(filepath));
        
        return retVal;
    }


    //
    // C++:  TermCriteria cv::ml::EM::getTermCriteria()
    //

    //javadoc: EM::getTermCriteria()
    public  TermCriteria getTermCriteria()
    {
        
        TermCriteria retVal = new TermCriteria(getTermCriteria_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Vec2d cv::ml::EM::predict2(Mat sample, Mat& probs)
    //

    //javadoc: EM::predict2(sample, probs)
    public  double[] predict2(Mat sample, Mat probs)
    {
        
        double[] retVal = predict2_0(nativeObj, sample.nativeObj, probs.nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::ml::EM::trainE(Mat samples, Mat means0, Mat covs0 = Mat(), Mat weights0 = Mat(), Mat& logLikelihoods = Mat(), Mat& labels = Mat(), Mat& probs = Mat())
    //

    //javadoc: EM::trainE(samples, means0, covs0, weights0, logLikelihoods, labels, probs)
    public  boolean trainE(Mat samples, Mat means0, Mat covs0, Mat weights0, Mat logLikelihoods, Mat labels, Mat probs)
    {
        
        boolean retVal = trainE_0(nativeObj, samples.nativeObj, means0.nativeObj, covs0.nativeObj, weights0.nativeObj, logLikelihoods.nativeObj, labels.nativeObj, probs.nativeObj);
        
        return retVal;
    }

    //javadoc: EM::trainE(samples, means0, covs0, weights0, logLikelihoods, labels)
    public  boolean trainE(Mat samples, Mat means0, Mat covs0, Mat weights0, Mat logLikelihoods, Mat labels)
    {
        
        boolean retVal = trainE_1(nativeObj, samples.nativeObj, means0.nativeObj, covs0.nativeObj, weights0.nativeObj, logLikelihoods.nativeObj, labels.nativeObj);
        
        return retVal;
    }

    //javadoc: EM::trainE(samples, means0, covs0, weights0, logLikelihoods)
    public  boolean trainE(Mat samples, Mat means0, Mat covs0, Mat weights0, Mat logLikelihoods)
    {
        
        boolean retVal = trainE_2(nativeObj, samples.nativeObj, means0.nativeObj, covs0.nativeObj, weights0.nativeObj, logLikelihoods.nativeObj);
        
        return retVal;
    }

    //javadoc: EM::trainE(samples, means0, covs0, weights0)
    public  boolean trainE(Mat samples, Mat means0, Mat covs0, Mat weights0)
    {
        
        boolean retVal = trainE_3(nativeObj, samples.nativeObj, means0.nativeObj, covs0.nativeObj, weights0.nativeObj);
        
        return retVal;
    }

    //javadoc: EM::trainE(samples, means0, covs0)
    public  boolean trainE(Mat samples, Mat means0, Mat covs0)
    {
        
        boolean retVal = trainE_4(nativeObj, samples.nativeObj, means0.nativeObj, covs0.nativeObj);
        
        return retVal;
    }

    //javadoc: EM::trainE(samples, means0)
    public  boolean trainE(Mat samples, Mat means0)
    {
        
        boolean retVal = trainE_5(nativeObj, samples.nativeObj, means0.nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::ml::EM::trainEM(Mat samples, Mat& logLikelihoods = Mat(), Mat& labels = Mat(), Mat& probs = Mat())
    //

    //javadoc: EM::trainEM(samples, logLikelihoods, labels, probs)
    public  boolean trainEM(Mat samples, Mat logLikelihoods, Mat labels, Mat probs)
    {
        
        boolean retVal = trainEM_0(nativeObj, samples.nativeObj, logLikelihoods.nativeObj, labels.nativeObj, probs.nativeObj);
        
        return retVal;
    }

    //javadoc: EM::trainEM(samples, logLikelihoods, labels)
    public  boolean trainEM(Mat samples, Mat logLikelihoods, Mat labels)
    {
        
        boolean retVal = trainEM_1(nativeObj, samples.nativeObj, logLikelihoods.nativeObj, labels.nativeObj);
        
        return retVal;
    }

    //javadoc: EM::trainEM(samples, logLikelihoods)
    public  boolean trainEM(Mat samples, Mat logLikelihoods)
    {
        
        boolean retVal = trainEM_2(nativeObj, samples.nativeObj, logLikelihoods.nativeObj);
        
        return retVal;
    }

    //javadoc: EM::trainEM(samples)
    public  boolean trainEM(Mat samples)
    {
        
        boolean retVal = trainEM_3(nativeObj, samples.nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::ml::EM::trainM(Mat samples, Mat probs0, Mat& logLikelihoods = Mat(), Mat& labels = Mat(), Mat& probs = Mat())
    //

    //javadoc: EM::trainM(samples, probs0, logLikelihoods, labels, probs)
    public  boolean trainM(Mat samples, Mat probs0, Mat logLikelihoods, Mat labels, Mat probs)
    {
        
        boolean retVal = trainM_0(nativeObj, samples.nativeObj, probs0.nativeObj, logLikelihoods.nativeObj, labels.nativeObj, probs.nativeObj);
        
        return retVal;
    }

    //javadoc: EM::trainM(samples, probs0, logLikelihoods, labels)
    public  boolean trainM(Mat samples, Mat probs0, Mat logLikelihoods, Mat labels)
    {
        
        boolean retVal = trainM_1(nativeObj, samples.nativeObj, probs0.nativeObj, logLikelihoods.nativeObj, labels.nativeObj);
        
        return retVal;
    }

    //javadoc: EM::trainM(samples, probs0, logLikelihoods)
    public  boolean trainM(Mat samples, Mat probs0, Mat logLikelihoods)
    {
        
        boolean retVal = trainM_2(nativeObj, samples.nativeObj, probs0.nativeObj, logLikelihoods.nativeObj);
        
        return retVal;
    }

    //javadoc: EM::trainM(samples, probs0)
    public  boolean trainM(Mat samples, Mat probs0)
    {
        
        boolean retVal = trainM_3(nativeObj, samples.nativeObj, probs0.nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::ml::EM::predict(Mat samples, Mat& results = Mat(), int flags = 0)
    //

    //javadoc: EM::predict(samples, results, flags)
    public  float predict(Mat samples, Mat results, int flags)
    {
        
        float retVal = predict_0(nativeObj, samples.nativeObj, results.nativeObj, flags);
        
        return retVal;
    }

    //javadoc: EM::predict(samples, results)
    public  float predict(Mat samples, Mat results)
    {
        
        float retVal = predict_1(nativeObj, samples.nativeObj, results.nativeObj);
        
        return retVal;
    }

    //javadoc: EM::predict(samples)
    public  float predict(Mat samples)
    {
        
        float retVal = predict_2(nativeObj, samples.nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::EM::getClustersNumber()
    //

    //javadoc: EM::getClustersNumber()
    public  int getClustersNumber()
    {
        
        int retVal = getClustersNumber_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::EM::getCovarianceMatrixType()
    //

    //javadoc: EM::getCovarianceMatrixType()
    public  int getCovarianceMatrixType()
    {
        
        int retVal = getCovarianceMatrixType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::ml::EM::getCovs(vector_Mat& covs)
    //

    //javadoc: EM::getCovs(covs)
    public  void getCovs(List<Mat> covs)
    {
        Mat covs_mat = new Mat();
        getCovs_0(nativeObj, covs_mat.nativeObj);
        Converters.Mat_to_vector_Mat(covs_mat, covs);
        covs_mat.release();
        return;
    }


    //
    // C++:  void cv::ml::EM::setClustersNumber(int val)
    //

    //javadoc: EM::setClustersNumber(val)
    public  void setClustersNumber(int val)
    {
        
        setClustersNumber_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::EM::setCovarianceMatrixType(int val)
    //

    //javadoc: EM::setCovarianceMatrixType(val)
    public  void setCovarianceMatrixType(int val)
    {
        
        setCovarianceMatrixType_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::EM::setTermCriteria(TermCriteria val)
    //

    //javadoc: EM::setTermCriteria(val)
    public  void setTermCriteria(TermCriteria val)
    {
        
        setTermCriteria_0(nativeObj, val.type, val.maxCount, val.epsilon);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  Mat cv::ml::EM::getMeans()
    private static native long getMeans_0(long nativeObj);

    // C++:  Mat cv::ml::EM::getWeights()
    private static native long getWeights_0(long nativeObj);

    // C++: static Ptr_EM cv::ml::EM::create()
    private static native long create_0();

    // C++: static Ptr_EM cv::ml::EM::load(String filepath, String nodeName = String())
    private static native long load_0(String filepath, String nodeName);
    private static native long load_1(String filepath);

    // C++:  TermCriteria cv::ml::EM::getTermCriteria()
    private static native double[] getTermCriteria_0(long nativeObj);

    // C++:  Vec2d cv::ml::EM::predict2(Mat sample, Mat& probs)
    private static native double[] predict2_0(long nativeObj, long sample_nativeObj, long probs_nativeObj);

    // C++:  bool cv::ml::EM::trainE(Mat samples, Mat means0, Mat covs0 = Mat(), Mat weights0 = Mat(), Mat& logLikelihoods = Mat(), Mat& labels = Mat(), Mat& probs = Mat())
    private static native boolean trainE_0(long nativeObj, long samples_nativeObj, long means0_nativeObj, long covs0_nativeObj, long weights0_nativeObj, long logLikelihoods_nativeObj, long labels_nativeObj, long probs_nativeObj);
    private static native boolean trainE_1(long nativeObj, long samples_nativeObj, long means0_nativeObj, long covs0_nativeObj, long weights0_nativeObj, long logLikelihoods_nativeObj, long labels_nativeObj);
    private static native boolean trainE_2(long nativeObj, long samples_nativeObj, long means0_nativeObj, long covs0_nativeObj, long weights0_nativeObj, long logLikelihoods_nativeObj);
    private static native boolean trainE_3(long nativeObj, long samples_nativeObj, long means0_nativeObj, long covs0_nativeObj, long weights0_nativeObj);
    private static native boolean trainE_4(long nativeObj, long samples_nativeObj, long means0_nativeObj, long covs0_nativeObj);
    private static native boolean trainE_5(long nativeObj, long samples_nativeObj, long means0_nativeObj);

    // C++:  bool cv::ml::EM::trainEM(Mat samples, Mat& logLikelihoods = Mat(), Mat& labels = Mat(), Mat& probs = Mat())
    private static native boolean trainEM_0(long nativeObj, long samples_nativeObj, long logLikelihoods_nativeObj, long labels_nativeObj, long probs_nativeObj);
    private static native boolean trainEM_1(long nativeObj, long samples_nativeObj, long logLikelihoods_nativeObj, long labels_nativeObj);
    private static native boolean trainEM_2(long nativeObj, long samples_nativeObj, long logLikelihoods_nativeObj);
    private static native boolean trainEM_3(long nativeObj, long samples_nativeObj);

    // C++:  bool cv::ml::EM::trainM(Mat samples, Mat probs0, Mat& logLikelihoods = Mat(), Mat& labels = Mat(), Mat& probs = Mat())
    private static native boolean trainM_0(long nativeObj, long samples_nativeObj, long probs0_nativeObj, long logLikelihoods_nativeObj, long labels_nativeObj, long probs_nativeObj);
    private static native boolean trainM_1(long nativeObj, long samples_nativeObj, long probs0_nativeObj, long logLikelihoods_nativeObj, long labels_nativeObj);
    private static native boolean trainM_2(long nativeObj, long samples_nativeObj, long probs0_nativeObj, long logLikelihoods_nativeObj);
    private static native boolean trainM_3(long nativeObj, long samples_nativeObj, long probs0_nativeObj);

    // C++:  float cv::ml::EM::predict(Mat samples, Mat& results = Mat(), int flags = 0)
    private static native float predict_0(long nativeObj, long samples_nativeObj, long results_nativeObj, int flags);
    private static native float predict_1(long nativeObj, long samples_nativeObj, long results_nativeObj);
    private static native float predict_2(long nativeObj, long samples_nativeObj);

    // C++:  int cv::ml::EM::getClustersNumber()
    private static native int getClustersNumber_0(long nativeObj);

    // C++:  int cv::ml::EM::getCovarianceMatrixType()
    private static native int getCovarianceMatrixType_0(long nativeObj);

    // C++:  void cv::ml::EM::getCovs(vector_Mat& covs)
    private static native void getCovs_0(long nativeObj, long covs_mat_nativeObj);

    // C++:  void cv::ml::EM::setClustersNumber(int val)
    private static native void setClustersNumber_0(long nativeObj, int val);

    // C++:  void cv::ml::EM::setCovarianceMatrixType(int val)
    private static native void setCovarianceMatrixType_0(long nativeObj, int val);

    // C++:  void cv::ml::EM::setTermCriteria(TermCriteria val)
    private static native void setTermCriteria_0(long nativeObj, int val_type, int val_maxCount, double val_epsilon);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
