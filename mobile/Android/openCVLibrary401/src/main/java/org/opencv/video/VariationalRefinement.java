//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.video;

import org.opencv.core.Mat;
import org.opencv.video.DenseOpticalFlow;
import org.opencv.video.VariationalRefinement;

// C++: class VariationalRefinement
//javadoc: VariationalRefinement

public class VariationalRefinement extends DenseOpticalFlow {

    protected VariationalRefinement(long addr) { super(addr); }

    // internal usage only
    public static VariationalRefinement __fromPtr__(long addr) { return new VariationalRefinement(addr); }

    //
    // C++: static Ptr_VariationalRefinement cv::VariationalRefinement::create()
    //

    //javadoc: VariationalRefinement::create()
    public static VariationalRefinement create()
    {
        
        VariationalRefinement retVal = VariationalRefinement.__fromPtr__(create_0());
        
        return retVal;
    }


    //
    // C++:  float cv::VariationalRefinement::getAlpha()
    //

    //javadoc: VariationalRefinement::getAlpha()
    public  float getAlpha()
    {
        
        float retVal = getAlpha_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::VariationalRefinement::getDelta()
    //

    //javadoc: VariationalRefinement::getDelta()
    public  float getDelta()
    {
        
        float retVal = getDelta_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::VariationalRefinement::getGamma()
    //

    //javadoc: VariationalRefinement::getGamma()
    public  float getGamma()
    {
        
        float retVal = getGamma_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::VariationalRefinement::getOmega()
    //

    //javadoc: VariationalRefinement::getOmega()
    public  float getOmega()
    {
        
        float retVal = getOmega_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::VariationalRefinement::getFixedPointIterations()
    //

    //javadoc: VariationalRefinement::getFixedPointIterations()
    public  int getFixedPointIterations()
    {
        
        int retVal = getFixedPointIterations_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::VariationalRefinement::getSorIterations()
    //

    //javadoc: VariationalRefinement::getSorIterations()
    public  int getSorIterations()
    {
        
        int retVal = getSorIterations_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::VariationalRefinement::calcUV(Mat I0, Mat I1, Mat& flow_u, Mat& flow_v)
    //

    //javadoc: VariationalRefinement::calcUV(I0, I1, flow_u, flow_v)
    public  void calcUV(Mat I0, Mat I1, Mat flow_u, Mat flow_v)
    {
        
        calcUV_0(nativeObj, I0.nativeObj, I1.nativeObj, flow_u.nativeObj, flow_v.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::VariationalRefinement::setAlpha(float val)
    //

    //javadoc: VariationalRefinement::setAlpha(val)
    public  void setAlpha(float val)
    {
        
        setAlpha_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::VariationalRefinement::setDelta(float val)
    //

    //javadoc: VariationalRefinement::setDelta(val)
    public  void setDelta(float val)
    {
        
        setDelta_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::VariationalRefinement::setFixedPointIterations(int val)
    //

    //javadoc: VariationalRefinement::setFixedPointIterations(val)
    public  void setFixedPointIterations(int val)
    {
        
        setFixedPointIterations_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::VariationalRefinement::setGamma(float val)
    //

    //javadoc: VariationalRefinement::setGamma(val)
    public  void setGamma(float val)
    {
        
        setGamma_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::VariationalRefinement::setOmega(float val)
    //

    //javadoc: VariationalRefinement::setOmega(val)
    public  void setOmega(float val)
    {
        
        setOmega_0(nativeObj, val);
        
        return;
    }


    //
    // C++:  void cv::VariationalRefinement::setSorIterations(int val)
    //

    //javadoc: VariationalRefinement::setSorIterations(val)
    public  void setSorIterations(int val)
    {
        
        setSorIterations_0(nativeObj, val);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_VariationalRefinement cv::VariationalRefinement::create()
    private static native long create_0();

    // C++:  float cv::VariationalRefinement::getAlpha()
    private static native float getAlpha_0(long nativeObj);

    // C++:  float cv::VariationalRefinement::getDelta()
    private static native float getDelta_0(long nativeObj);

    // C++:  float cv::VariationalRefinement::getGamma()
    private static native float getGamma_0(long nativeObj);

    // C++:  float cv::VariationalRefinement::getOmega()
    private static native float getOmega_0(long nativeObj);

    // C++:  int cv::VariationalRefinement::getFixedPointIterations()
    private static native int getFixedPointIterations_0(long nativeObj);

    // C++:  int cv::VariationalRefinement::getSorIterations()
    private static native int getSorIterations_0(long nativeObj);

    // C++:  void cv::VariationalRefinement::calcUV(Mat I0, Mat I1, Mat& flow_u, Mat& flow_v)
    private static native void calcUV_0(long nativeObj, long I0_nativeObj, long I1_nativeObj, long flow_u_nativeObj, long flow_v_nativeObj);

    // C++:  void cv::VariationalRefinement::setAlpha(float val)
    private static native void setAlpha_0(long nativeObj, float val);

    // C++:  void cv::VariationalRefinement::setDelta(float val)
    private static native void setDelta_0(long nativeObj, float val);

    // C++:  void cv::VariationalRefinement::setFixedPointIterations(int val)
    private static native void setFixedPointIterations_0(long nativeObj, int val);

    // C++:  void cv::VariationalRefinement::setGamma(float val)
    private static native void setGamma_0(long nativeObj, float val);

    // C++:  void cv::VariationalRefinement::setOmega(float val)
    private static native void setOmega_0(long nativeObj, float val);

    // C++:  void cv::VariationalRefinement::setSorIterations(int val)
    private static native void setSorIterations_0(long nativeObj, int val);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
