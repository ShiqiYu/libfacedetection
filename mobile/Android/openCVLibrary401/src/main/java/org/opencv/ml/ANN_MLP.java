//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.ml;

import java.lang.String;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.ANN_MLP;
import org.opencv.ml.StatModel;

// C++: class ANN_MLP
//javadoc: ANN_MLP

public class ANN_MLP extends StatModel {

    protected ANN_MLP(long addr) { super(addr); }

    // internal usage only
    public static ANN_MLP __fromPtr__(long addr) { return new ANN_MLP(addr); }

    // C++: enum TrainingMethods
    public static final int
            BACKPROP = 0,
            RPROP = 1,
            ANNEAL = 2;


    // C++: enum TrainFlags
    public static final int
            UPDATE_WEIGHTS = 1,
            NO_INPUT_SCALE = 2,
            NO_OUTPUT_SCALE = 4;


    // C++: enum ActivationFunctions
    public static final int
            IDENTITY = 0,
            SIGMOID_SYM = 1,
            GAUSSIAN = 2,
            RELU = 3,
            LEAKYRELU = 4;


    //
    // C++:  Mat cv::ml::ANN_MLP::getLayerSizes()
    //

    //javadoc: ANN_MLP::getLayerSizes()
    public  Mat getLayerSizes()
    {
        
        Mat retVal = new Mat(getLayerSizes_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::ml::ANN_MLP::getWeights(int layerIdx)
    //

    //javadoc: ANN_MLP::getWeights(layerIdx)
    public  Mat getWeights(int layerIdx)
    {
        
        Mat retVal = new Mat(getWeights_0(nativeObj, layerIdx));
        
        return retVal;
    }


    //
    // C++: static Ptr_ANN_MLP cv::ml::ANN_MLP::create()
    //

    //javadoc: ANN_MLP::create()
    public static ANN_MLP create()
    {
        
        ANN_MLP retVal = ANN_MLP.__fromPtr__(create_0());
        
        return retVal;
    }


    //
    // C++: static Ptr_ANN_MLP cv::ml::ANN_MLP::load(String filepath)
    //

    //javadoc: ANN_MLP::load(filepath)
    public static ANN_MLP load(String filepath)
    {
        
        ANN_MLP retVal = ANN_MLP.__fromPtr__(load_0(filepath));
        
        return retVal;
    }


    //
    // C++:  TermCriteria cv::ml::ANN_MLP::getTermCriteria()
    //

    //javadoc: ANN_MLP::getTermCriteria()
    public  TermCriteria getTermCriteria()
    {
        
        TermCriteria retVal = new TermCriteria(getTermCriteria_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  double cv::ml::ANN_MLP::getAnnealCoolingRatio()
    //

    //javadoc: ANN_MLP::getAnnealCoolingRatio()
    public  double getAnnealCoolingRatio()
    {
        
        double retVal = getAnnealCoolingRatio_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::ANN_MLP::getAnnealFinalT()
    //

    //javadoc: ANN_MLP::getAnnealFinalT()
    public  double getAnnealFinalT()
    {
        
        double retVal = getAnnealFinalT_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::ANN_MLP::getAnnealInitialT()
    //

    //javadoc: ANN_MLP::getAnnealInitialT()
    public  double getAnnealInitialT()
    {
        
        double retVal = getAnnealInitialT_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::ANN_MLP::getBackpropMomentumScale()
    //

    //javadoc: ANN_MLP::getBackpropMomentumScale()
    public  double getBackpropMomentumScale()
    {
        
        double retVal = getBackpropMomentumScale_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::ANN_MLP::getBackpropWeightScale()
    //

    //javadoc: ANN_MLP::getBackpropWeightScale()
    public  double getBackpropWeightScale()
    {
        
        double retVal = getBackpropWeightScale_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::ANN_MLP::getRpropDW0()
    //

    //javadoc: ANN_MLP::getRpropDW0()
    public  double getRpropDW0()
    {
        
        double retVal = getRpropDW0_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::ANN_MLP::getRpropDWMax()
    //

    //javadoc: ANN_MLP::getRpropDWMax()
    public  double getRpropDWMax()
    {
        
        double retVal = getRpropDWMax_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::ANN_MLP::getRpropDWMin()
    //

    //javadoc: ANN_MLP::getRpropDWMin()
    public  double getRpropDWMin()
    {
        
        double retVal = getRpropDWMin_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::ANN_MLP::getRpropDWMinus()
    //

    //javadoc: ANN_MLP::getRpropDWMinus()
    public  double getRpropDWMinus()
    {
        
        double retVal = getRpropDWMinus_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::ml::ANN_MLP::getRpropDWPlus()
    //

    //javadoc: ANN_MLP::getRpropDWPlus()
    public  double getRpropDWPlus()
    {
        
        double retVal = getRpropDWPlus_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::ANN_MLP::getAnnealItePerStep()
    //

    //javadoc: ANN_MLP::getAnnealItePerStep()
    public  int getAnnealItePerStep()
    {
        
        int retVal = getAnnealItePerStep_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::ml::ANN_MLP::getTrainMethod()
    //

    //javadoc: ANN_MLP::getTrainMethod()
    public  int getTrainMethod()
    {
        
        int retVal = getTrainMethod_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setActivationFunction(int type, double param1 = 0, double param2 = 0)
    //

    //javadoc: ANN_MLP::setActivationFunction(type, param1, param2)
    public  void setActivationFunction(int type, double param1, double param2)
    {
        
        setActivationFunction_0(nativeObj, type, param1, param2);
        
        return;
    }

    //javadoc: ANN_MLP::setActivationFunction(type, param1)
    public  void setActivationFunction(int type, double param1)
    {
        
        setActivationFunction_1(nativeObj, type, param1);
        
        return;
    }

    //javadoc: ANN_MLP::setActivationFunction(type)
    public  void setActivationFunction(int type)
    {
        
        setActivationFunction_2(nativeObj, type);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setAnnealCoolingRatio(double val)
    //

    //javadoc: ANN_MLP::setAnnealCoolingRatio(val)
    public  void setAnnealCoolingRatio(double val)
    {
        
        setAnnealCoolingRatio_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setAnnealFinalT(double val)
    //

    //javadoc: ANN_MLP::setAnnealFinalT(val)
    public  void setAnnealFinalT(double val)
    {
        
        setAnnealFinalT_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setAnnealInitialT(double val)
    //

    //javadoc: ANN_MLP::setAnnealInitialT(val)
    public  void setAnnealInitialT(double val)
    {
        
        setAnnealInitialT_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setAnnealItePerStep(int val)
    //

    //javadoc: ANN_MLP::setAnnealItePerStep(val)
    public  void setAnnealItePerStep(int val)
    {
        
        setAnnealItePerStep_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setBackpropMomentumScale(double val)
    //

    //javadoc: ANN_MLP::setBackpropMomentumScale(val)
    public  void setBackpropMomentumScale(double val)
    {
        
        setBackpropMomentumScale_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setBackpropWeightScale(double val)
    //

    //javadoc: ANN_MLP::setBackpropWeightScale(val)
    public  void setBackpropWeightScale(double val)
    {
        
        setBackpropWeightScale_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setLayerSizes(Mat _layer_sizes)
    //

    //javadoc: ANN_MLP::setLayerSizes(_layer_sizes)
    public  void setLayerSizes(Mat _layer_sizes)
    {
        
        setLayerSizes_0(nativeObj, _layer_sizes.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setRpropDW0(double val)
    //

    //javadoc: ANN_MLP::setRpropDW0(val)
    public  void setRpropDW0(double val)
    {
        
        setRpropDW0_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setRpropDWMax(double val)
    //

    //javadoc: ANN_MLP::setRpropDWMax(val)
    public  void setRpropDWMax(double val)
    {
        
        setRpropDWMax_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setRpropDWMin(double val)
    //

    //javadoc: ANN_MLP::setRpropDWMin(val)
    public  void setRpropDWMin(double val)
    {
        
        setRpropDWMin_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setRpropDWMinus(double val)
    //

    //javadoc: ANN_MLP::setRpropDWMinus(val)
    public  void setRpropDWMinus(double val)
    {
        
        setRpropDWMinus_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setRpropDWPlus(double val)
    //

    //javadoc: ANN_MLP::setRpropDWPlus(val)
    public  void setRpropDWPlus(double val)
    {
        
        setRpropDWPlus_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setTermCriteria(TermCriteria val)
    //

    //javadoc: ANN_MLP::setTermCriteria(val)
    public  void setTermCriteria(TermCriteria val)
    {
        
        setTermCriteria_0(nativeObj, val.type, val.maxCount, val.epsilon);
        
        return;
    }


    //
    // C++:  void cv::ml::ANN_MLP::setTrainMethod(int method, double param1 = 0, double param2 = 0)
    //

    //javadoc: ANN_MLP::setTrainMethod(method, param1, param2)
    public  void setTrainMethod(int method, double param1, double param2)
    {
        
        setTrainMethod_0(nativeObj, method, param1, param2);
        
        return;
    }

    //javadoc: ANN_MLP::setTrainMethod(method, param1)
    public  void setTrainMethod(int method, double param1)
    {
        
        setTrainMethod_1(nativeObj, method, param1);
        
        return;
    }

    //javadoc: ANN_MLP::setTrainMethod(method)
    public  void setTrainMethod(int method)
    {
        
        setTrainMethod_2(nativeObj, method);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  Mat cv::ml::ANN_MLP::getLayerSizes()
    private static native long getLayerSizes_0(long nativeObj);

    // C++:  Mat cv::ml::ANN_MLP::getWeights(int layerIdx)
    private static native long getWeights_0(long nativeObj, int layerIdx);

    // C++: static Ptr_ANN_MLP cv::ml::ANN_MLP::create()
    private static native long create_0();

    // C++: static Ptr_ANN_MLP cv::ml::ANN_MLP::load(String filepath)
    private static native long load_0(String filepath);

    // C++:  TermCriteria cv::ml::ANN_MLP::getTermCriteria()
    private static native double[] getTermCriteria_0(long nativeObj);

    // C++:  double cv::ml::ANN_MLP::getAnnealCoolingRatio()
    private static native double getAnnealCoolingRatio_0(long nativeObj);

    // C++:  double cv::ml::ANN_MLP::getAnnealFinalT()
    private static native double getAnnealFinalT_0(long nativeObj);

    // C++:  double cv::ml::ANN_MLP::getAnnealInitialT()
    private static native double getAnnealInitialT_0(long nativeObj);

    // C++:  double cv::ml::ANN_MLP::getBackpropMomentumScale()
    private static native double getBackpropMomentumScale_0(long nativeObj);

    // C++:  double cv::ml::ANN_MLP::getBackpropWeightScale()
    private static native double getBackpropWeightScale_0(long nativeObj);

    // C++:  double cv::ml::ANN_MLP::getRpropDW0()
    private static native double getRpropDW0_0(long nativeObj);

    // C++:  double cv::ml::ANN_MLP::getRpropDWMax()
    private static native double getRpropDWMax_0(long nativeObj);

    // C++:  double cv::ml::ANN_MLP::getRpropDWMin()
    private static native double getRpropDWMin_0(long nativeObj);

    // C++:  double cv::ml::ANN_MLP::getRpropDWMinus()
    private static native double getRpropDWMinus_0(long nativeObj);

    // C++:  double cv::ml::ANN_MLP::getRpropDWPlus()
    private static native double getRpropDWPlus_0(long nativeObj);

    // C++:  int cv::ml::ANN_MLP::getAnnealItePerStep()
    private static native int getAnnealItePerStep_0(long nativeObj);

    // C++:  int cv::ml::ANN_MLP::getTrainMethod()
    private static native int getTrainMethod_0(long nativeObj);

    // C++:  void cv::ml::ANN_MLP::setActivationFunction(int type, double param1 = 0, double param2 = 0)
    private static native void setActivationFunction_0(long nativeObj, int type, double param1, double param2);
    private static native void setActivationFunction_1(long nativeObj, int type, double param1);
    private static native void setActivationFunction_2(long nativeObj, int type);

    // C++:  void cv::ml::ANN_MLP::setAnnealCoolingRatio(double val)
    private static native void setAnnealCoolingRatio_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP::setAnnealFinalT(double val)
    private static native void setAnnealFinalT_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP::setAnnealInitialT(double val)
    private static native void setAnnealInitialT_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP::setAnnealItePerStep(int val)
    private static native void setAnnealItePerStep_0(long nativeObj, int val);

    // C++:  void cv::ml::ANN_MLP::setBackpropMomentumScale(double val)
    private static native void setBackpropMomentumScale_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP::setBackpropWeightScale(double val)
    private static native void setBackpropWeightScale_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP::setLayerSizes(Mat _layer_sizes)
    private static native void setLayerSizes_0(long nativeObj, long _layer_sizes_nativeObj);

    // C++:  void cv::ml::ANN_MLP::setRpropDW0(double val)
    private static native void setRpropDW0_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP::setRpropDWMax(double val)
    private static native void setRpropDWMax_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP::setRpropDWMin(double val)
    private static native void setRpropDWMin_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP::setRpropDWMinus(double val)
    private static native void setRpropDWMinus_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP::setRpropDWPlus(double val)
    private static native void setRpropDWPlus_0(long nativeObj, double val);

    // C++:  void cv::ml::ANN_MLP::setTermCriteria(TermCriteria val)
    private static native void setTermCriteria_0(long nativeObj, int val_type, int val_maxCount, double val_epsilon);

    // C++:  void cv::ml::ANN_MLP::setTrainMethod(int method, double param1 = 0, double param2 = 0)
    private static native void setTrainMethod_0(long nativeObj, int method, double param1, double param2);
    private static native void setTrainMethod_1(long nativeObj, int method, double param1);
    private static native void setTrainMethod_2(long nativeObj, int method);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
