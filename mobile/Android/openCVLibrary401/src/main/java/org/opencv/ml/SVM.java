//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.ml;

import java.lang.String;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.ParamGrid;
import org.opencv.ml.SVM;
import org.opencv.ml.StatModel;

// C++: class SVM
//javadoc: SVM

public class SVM extends StatModel {

    protected SVM(long addr) { super(addr); }

    // internal usage only
    public static SVM __fromPtr__(long addr) { return new SVM(addr); }

    // C++: enum KernelTypes
    public static final int
            CUSTOM = -1,
            LINEAR = 0,
            POLY = 1,
            RBF = 2,
            SIGMOID = 3,
            CHI2 = 4,
            INTER = 5;


    // C++: enum Types
    public static final int
            C_SVC = 100,
            NU_SVC = 101,
            ONE_CLASS = 102,
            EPS_SVR = 103,
            NU_SVR = 104;


    // C++: enum ParamTypes
    public static final int
            C = 0,
            GAMMA = 1,
            P = 2,
            NU = 3,
            COEF = 4,
            DEGREE = 5;


    //
    // C++:  Mat cv::ml::SVM::getClassWeights()
    //

    //javadoc: SVM::getClassWeights()
    public  Mat getClassWeights()
    {
        
        Mat retVal = new Mat(getClassWeights_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::SVM::getSupportVectors()
    //

    //javadoc: SVM::getSupportVectors()
    public  Mat getSupportVectors()
    {
        
        Mat retVal = new Mat(getSupportVectors_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::SVM::getUncompressedSupportVectors()
    //

    //javadoc: SVM::getUncompressedSupportVectors()
    public  Mat getUncompressedSupportVectors()
    {
        
        Mat retVal = new Mat(getUncompressedSupportVectors_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: static Ptr_ParamGrid cv::ml::SVM::getDefaultGridPtr(int param_id)
    //

    //javadoc: SVM::getDefaultGridPtr(param_id)
    public static ParamGrid getDefaultGridPtr(int param_id)
    {
        
        ParamGrid retVal = ParamGrid.__fromPtr__(getDefaultGridPtr_0(param_id));
        
        return retVal;
    }


    //
    // C++: static Ptr_SVM cv::ml::SVM::create()
    //

    //javadoc: SVM::create()
    public static SVM create()
    {
        
        SVM retVal = SVM.__fromPtr__(create_0());
        
        return retVal;
    }


    //
    // C++: static Ptr_SVM cv::ml::SVM::load(String filepath)
    //

    //javadoc: SVM::load(filepath)
    public static SVM load(String filepath)
    {
        
        SVM retVal = SVM.__fromPtr__(load_0(filepath));
        
        return retVal;
    }


    //
    // C++:  TermCriteria cv::ml::SVM::getTermCriteria()
    //

    //javadoc: SVM::getTermCriteria()
    public  TermCriteria getTermCriteria()
    {
        
        TermCriteria retVal = new TermCriteria(getTermCriteria_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  bool cv::ml::SVM::trainAuto(Mat samples, int layout, Mat responses, int kFold = 10, Ptr_ParamGrid Cgrid = SVM::getDefaultGridPtr(SVM::C), Ptr_ParamGrid gammaGrid = SVM::getDefaultGridPtr(SVM::GAMMA), Ptr_ParamGrid pGrid = SVM::getDefaultGridPtr(SVM::P), Ptr_ParamGrid nuGrid = SVM::getDefaultGridPtr(SVM::NU), Ptr_ParamGrid coeffGrid = SVM::getDefaultGridPtr(SVM::COEF), Ptr_ParamGrid degreeGrid = SVM::getDefaultGridPtr(SVM::DEGREE), bool balanced = false)
    //

    //javadoc: SVM::trainAuto(samples, layout, responses, kFold, Cgrid, gammaGrid, pGrid, nuGrid, coeffGrid, degreeGrid, balanced)
    public  boolean trainAuto(Mat samples, int layout, Mat responses, int kFold, ParamGrid Cgrid, ParamGrid gammaGrid, ParamGrid pGrid, ParamGrid nuGrid, ParamGrid coeffGrid, ParamGrid degreeGrid, boolean balanced)
    {
        
        boolean retVal = trainAuto_0(nativeObj, samples.nativeObj, layout, responses.nativeObj, kFold, Cgrid.getNativeObjAddr(), gammaGrid.getNativeObjAddr(), pGrid.getNativeObjAddr(), nuGrid.getNativeObjAddr(), coeffGrid.getNativeObjAddr(), degreeGrid.getNativeObjAddr(), balanced);
        
        return retVal;
    }

    //javadoc: SVM::trainAuto(samples, layout, responses, kFold, Cgrid, gammaGrid, pGrid, nuGrid, coeffGrid, degreeGrid)
    public  boolean trainAuto(Mat samples, int layout, Mat responses, int kFold, ParamGrid Cgrid, ParamGrid gammaGrid, ParamGrid pGrid, ParamGrid nuGrid, ParamGrid coeffGrid, ParamGrid degreeGrid)
    {
        
        boolean retVal = trainAuto_1(nativeObj, samples.nativeObj, layout, responses.nativeObj, kFold, Cgrid.getNativeObjAddr(), gammaGrid.getNativeObjAddr(), pGrid.getNativeObjAddr(), nuGrid.getNativeObjAddr(), coeffGrid.getNativeObjAddr(), degreeGrid.getNativeObjAddr());
        
        return retVal;
    }

    //javadoc: SVM::trainAuto(samples, layout, responses, kFold, Cgrid, gammaGrid, pGrid, nuGrid, coeffGrid)
    public  boolean trainAuto(Mat samples, int layout, Mat responses, int kFold, ParamGrid Cgrid, ParamGrid gammaGrid, ParamGrid pGrid, ParamGrid nuGrid, ParamGrid coeffGrid)
    {
        
        boolean retVal = trainAuto_2(nativeObj, samples.nativeObj, layout, responses.nativeObj, kFold, Cgrid.getNativeObjAddr(), gammaGrid.getNativeObjAddr(), pGrid.getNativeObjAddr(), nuGrid.getNativeObjAddr(), coeffGrid.getNativeObjAddr());
        
        return retVal;
    }

    //javadoc: SVM::trainAuto(samples, layout, responses, kFold, Cgrid, gammaGrid, pGrid, nuGrid)
    public  boolean trainAuto(Mat samples, int layout, Mat responses, int kFold, ParamGrid Cgrid, ParamGrid gammaGrid, ParamGrid pGrid, ParamGrid nuGrid)
    {
        
        boolean retVal = trainAuto_3(nativeObj, samples.nativeObj, layout, responses.nativeObj, kFold, Cgrid.getNativeObjAddr(), gammaGrid.getNativeObjAddr(), pGrid.getNativeObjAddr(), nuGrid.getNativeObjAddr());
        
        return retVal;
    }

    //javadoc: SVM::trainAuto(samples, layout, responses, kFold, Cgrid, gammaGrid, pGrid)
    public  boolean trainAuto(Mat samples, int layout, Mat responses, int kFold, ParamGrid Cgrid, ParamGrid gammaGrid, ParamGrid pGrid)
    {
        
        boolean retVal = trainAuto_4(nativeObj, samples.nativeObj, layout, responses.nativeObj, kFold, Cgrid.getNativeObjAddr(), gammaGrid.getNativeObjAddr(), pGrid.getNativeObjAddr());
        
        return retVal;
    }

    //javadoc: SVM::trainAuto(samples, layout, responses, kFold, Cgrid, gammaGrid)
    public  boolean trainAuto(Mat samples, int layout, Mat responses, int kFold, ParamGrid Cgrid, ParamGrid gammaGrid)
    {
        
        boolean retVal = trainAuto_5(nativeObj, samples.nativeObj, layout, responses.nativeObj, kFold, Cgrid.getNativeObjAddr(), gammaGrid.getNativeObjAddr());
        
        return retVal;
    }

    //javadoc: SVM::trainAuto(samples, layout, responses, kFold, Cgrid)
    public  boolean trainAuto(Mat samples, int layout, Mat responses, int kFold, ParamGrid Cgrid)
    {
        
        boolean retVal = trainAuto_6(nativeObj, samples.nativeObj, layout, responses.nativeObj, kFold, Cgrid.getNativeObjAddr());
        
        return retVal;
    }

    //javadoc: SVM::trainAuto(samples, layout, responses, kFold)
    public  boolean trainAuto(Mat samples, int layout, Mat responses, int kFold)
    {
        
        boolean retVal = trainAuto_7(nativeObj, samples.nativeObj, layout, responses.nativeObj, kFold);
        
        return retVal;
    }

    //javadoc: SVM::trainAuto(samples, layout, responses)
    public  boolean trainAuto(Mat samples, int layout, Mat responses)
    {
        
        boolean retVal = trainAuto_8(nativeObj, samples.nativeObj, layout, responses.nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::SVM::getC()
    //

    //javadoc: SVM::getC()
    public  double getC()
    {
        
        double retVal = getC_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::SVM::getCoef0()
    //

    //javadoc: SVM::getCoef0()
    public  double getCoef0()
    {
        
        double retVal = getCoef0_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::SVM::getDecisionFunction(int i, Mat& alpha, Mat& svidx)
    //

    //javadoc: SVM::getDecisionFunction(i, alpha, svidx)
    public  double getDecisionFunction(int i, Mat alpha, Mat svidx)
    {
        
        double retVal = getDecisionFunction_0(nativeObj, i, alpha.nativeObj, svidx.nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::SVM::getDegree()
    //

    //javadoc: SVM::getDegree()
    public  double getDegree()
    {
        
        double retVal = getDegree_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::SVM::getGamma()
    //

    //javadoc: SVM::getGamma()
    public  double getGamma()
    {
        
        double retVal = getGamma_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::SVM::getNu()
    //

    //javadoc: SVM::getNu()
    public  double getNu()
    {
        
        double retVal = getNu_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::SVM::getP()
    //

    //javadoc: SVM::getP()
    public  double getP()
    {
        
        double retVal = getP_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::SVM::getKernelType()
    //

    //javadoc: SVM::getKernelType()
    public  int getKernelType()
    {
        
        int retVal = getKernelType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::SVM::getType()
    //

    //javadoc: SVM::getType()
    public  int getType()
    {
        
        int retVal = getType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::ml::SVM::setC(double val)
    //

    //javadoc: SVM::setC(val)
    public  void setC(double val)
    {
        
        setC_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::SVM::setClassWeights(Mat val)
    //

    //javadoc: SVM::setClassWeights(val)
    public  void setClassWeights(Mat val)
    {
        
        setClassWeights_0(nativeObj, val.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::ml::SVM::setCoef0(double val)
    //

    //javadoc: SVM::setCoef0(val)
    public  void setCoef0(double val)
    {
        
        setCoef0_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::SVM::setDegree(double val)
    //

    //javadoc: SVM::setDegree(val)
    public  void setDegree(double val)
    {
        
        setDegree_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::SVM::setGamma(double val)
    //

    //javadoc: SVM::setGamma(val)
    public  void setGamma(double val)
    {
        
        setGamma_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::SVM::setKernel(int kernelType)
    //

    //javadoc: SVM::setKernel(kernelType)
    public  void setKernel(int kernelType)
    {
        
        setKernel_0(nativeObj, kernelType);
        
        return;
    }


    //
    // C++:  void cv::ml::SVM::setNu(double val)
    //

    //javadoc: SVM::setNu(val)
    public  void setNu(double val)
    {
        
        setNu_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::SVM::setP(double val)
    //

    //javadoc: SVM::setP(val)
    public  void setP(double val)
    {
        
        setP_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::SVM::setTermCriteria(TermCriteria val)
    //

    //javadoc: SVM::setTermCriteria(val)
    public  void setTermCriteria(TermCriteria val)
    {
        
        setTermCriteria_0(nativeObj, val.type, val.maxCount, val.epsilon);
        
        return;
    }


    //
    // C++:  void cv::ml::SVM::setType(int val)
    //

    //javadoc: SVM::setType(val)
    public  void setType(int val)
    {
        
        setType_0(nativeObj, val);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  Mat cv::ml::SVM::getClassWeights()
    private static native long getClassWeights_0(long nativeObj);

    // C++:  Mat cv::ml::SVM::getSupportVectors()
    private static native long getSupportVectors_0(long nativeObj);

    // C++:  Mat cv::ml::SVM::getUncompressedSupportVectors()
    private static native long getUncompressedSupportVectors_0(long nativeObj);

    // C++: static Ptr_ParamGrid cv::ml::SVM::getDefaultGridPtr(int param_id)
    private static native long getDefaultGridPtr_0(int param_id);

    // C++: static Ptr_SVM cv::ml::SVM::create()
    private static native long create_0();

    // C++: static Ptr_SVM cv::ml::SVM::load(String filepath)
    private static native long load_0(String filepath);

    // C++:  TermCriteria cv::ml::SVM::getTermCriteria()
    private static native double[] getTermCriteria_0(long nativeObj);

    // C++:  bool cv::ml::SVM::trainAuto(Mat samples, int layout, Mat responses, int kFold = 10, Ptr_ParamGrid Cgrid = SVM::getDefaultGridPtr(SVM::C), Ptr_ParamGrid gammaGrid = SVM::getDefaultGridPtr(SVM::GAMMA), Ptr_ParamGrid pGrid = SVM::getDefaultGridPtr(SVM::P), Ptr_ParamGrid nuGrid = SVM::getDefaultGridPtr(SVM::NU), Ptr_ParamGrid coeffGrid = SVM::getDefaultGridPtr(SVM::COEF), Ptr_ParamGrid degreeGrid = SVM::getDefaultGridPtr(SVM::DEGREE), bool balanced = false)
    private static native boolean trainAuto_0(long nativeObj, long samples_nativeObj, int layout, long responses_nativeObj, int kFold, long Cgrid_nativeObj, long gammaGrid_nativeObj, long pGrid_nativeObj, long nuGrid_nativeObj, long coeffGrid_nativeObj, long degreeGrid_nativeObj, boolean balanced);
    private static native boolean trainAuto_1(long nativeObj, long samples_nativeObj, int layout, long responses_nativeObj, int kFold, long Cgrid_nativeObj, long gammaGrid_nativeObj, long pGrid_nativeObj, long nuGrid_nativeObj, long coeffGrid_nativeObj, long degreeGrid_nativeObj);
    private static native boolean trainAuto_2(long nativeObj, long samples_nativeObj, int layout, long responses_nativeObj, int kFold, long Cgrid_nativeObj, long gammaGrid_nativeObj, long pGrid_nativeObj, long nuGrid_nativeObj, long coeffGrid_nativeObj);
    private static native boolean trainAuto_3(long nativeObj, long samples_nativeObj, int layout, long responses_nativeObj, int kFold, long Cgrid_nativeObj, long gammaGrid_nativeObj, long pGrid_nativeObj, long nuGrid_nativeObj);
    private static native boolean trainAuto_4(long nativeObj, long samples_nativeObj, int layout, long responses_nativeObj, int kFold, long Cgrid_nativeObj, long gammaGrid_nativeObj, long pGrid_nativeObj);
    private static native boolean trainAuto_5(long nativeObj, long samples_nativeObj, int layout, long responses_nativeObj, int kFold, long Cgrid_nativeObj, long gammaGrid_nativeObj);
    private static native boolean trainAuto_6(long nativeObj, long samples_nativeObj, int layout, long responses_nativeObj, int kFold, long Cgrid_nativeObj);
    private static native boolean trainAuto_7(long nativeObj, long samples_nativeObj, int layout, long responses_nativeObj, int kFold);
    private static native boolean trainAuto_8(long nativeObj, long samples_nativeObj, int layout, long responses_nativeObj);

    // C++:  double cv::ml::SVM::getC()
    private static native double getC_0(long nativeObj);

    // C++:  double cv::ml::SVM::getCoef0()
    private static native double getCoef0_0(long nativeObj);

    // C++:  double cv::ml::SVM::getDecisionFunction(int i, Mat& alpha, Mat& svidx)
    private static native double getDecisionFunction_0(long nativeObj, int i, long alpha_nativeObj, long svidx_nativeObj);

    // C++:  double cv::ml::SVM::getDegree()
    private static native double getDegree_0(long nativeObj);

    // C++:  double cv::ml::SVM::getGamma()
    private static native double getGamma_0(long nativeObj);

    // C++:  double cv::ml::SVM::getNu()
    private static native double getNu_0(long nativeObj);

    // C++:  double cv::ml::SVM::getP()
    private static native double getP_0(long nativeObj);

    // C++:  int cv::ml::SVM::getKernelType()
    private static native int getKernelType_0(long nativeObj);

    // C++:  int cv::ml::SVM::getType()
    private static native int getType_0(long nativeObj);

    // C++:  void cv::ml::SVM::setC(double val)
    private static native void setC_0(long nativeObj, double val);

    // C++:  void cv::ml::SVM::setClassWeights(Mat val)
    private static native void setClassWeights_0(long nativeObj, long val_nativeObj);

    // C++:  void cv::ml::SVM::setCoef0(double val)
    private static native void setCoef0_0(long nativeObj, double val);

    // C++:  void cv::ml::SVM::setDegree(double val)
    private static native void setDegree_0(long nativeObj, double val);

    // C++:  void cv::ml::SVM::setGamma(double val)
    private static native void setGamma_0(long nativeObj, double val);

    // C++:  void cv::ml::SVM::setKernel(int kernelType)
    private static native void setKernel_0(long nativeObj, int kernelType);

    // C++:  void cv::ml::SVM::setNu(double val)
    private static native void setNu_0(long nativeObj, double val);

    // C++:  void cv::ml::SVM::setP(double val)
    private static native void setP_0(long nativeObj, double val);

    // C++:  void cv::ml::SVM::setTermCriteria(TermCriteria val)
    private static native void setTermCriteria_0(long nativeObj, int val_type, int val_maxCount, double val_epsilon);

    // C++:  void cv::ml::SVM::setType(int val)
    private static native void setType_0(long nativeObj, int val);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
