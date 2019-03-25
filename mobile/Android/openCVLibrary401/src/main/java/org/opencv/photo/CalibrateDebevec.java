//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.photo;

import org.opencv.photo.CalibrateCRF;

// C++: class CalibrateDebevec
//javadoc: CalibrateDebevec

public class CalibrateDebevec extends CalibrateCRF {

    protected CalibrateDebevec(long addr) { super(addr); }

    // internal usage only
    public static CalibrateDebevec __fromPtr__(long addr) { return new CalibrateDebevec(addr); }

    //
    // C++:  bool cv::CalibrateDebevec::getRandom()
    //

    //javadoc: CalibrateDebevec::getRandom()
    public  boolean getRandom()
    {
        
        boolean retVal = getRandom_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::CalibrateDebevec::getLambda()
    //

    //javadoc: CalibrateDebevec::getLambda()
    public  float getLambda()
    {
        
        float retVal = getLambda_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::CalibrateDebevec::getSamples()
    //

    //javadoc: CalibrateDebevec::getSamples()
    public  int getSamples()
    {
        
        int retVal = getSamples_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::CalibrateDebevec::setLambda(float lambda)
    //

    //javadoc: CalibrateDebevec::setLambda(lambda)
    public  void setLambda(float lambda)
    {
        
        setLambda_0(nativeObj, lambda);
        
        return;
    }


    //
    // C++:  void cv::CalibrateDebevec::setRandom(bool random)
    //

    //javadoc: CalibrateDebevec::setRandom(random)
    public  void setRandom(boolean random)
    {
        
        setRandom_0(nativeObj, random);
        
        return;
    }


    //
    // C++:  void cv::CalibrateDebevec::setSamples(int samples)
    //

    //javadoc: CalibrateDebevec::setSamples(samples)
    public  void setSamples(int samples)
    {
        
        setSamples_0(nativeObj, samples);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  bool cv::CalibrateDebevec::getRandom()
    private static native boolean getRandom_0(long nativeObj);

    // C++:  float cv::CalibrateDebevec::getLambda()
    private static native float getLambda_0(long nativeObj);

    // C++:  int cv::CalibrateDebevec::getSamples()
    private static native int getSamples_0(long nativeObj);

    // C++:  void cv::CalibrateDebevec::setLambda(float lambda)
    private static native void setLambda_0(long nativeObj, float lambda);

    // C++:  void cv::CalibrateDebevec::setRandom(bool random)
    private static native void setRandom_0(long nativeObj, boolean random);

    // C++:  void cv::CalibrateDebevec::setSamples(int samples)
    private static native void setSamples_0(long nativeObj, int samples);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
