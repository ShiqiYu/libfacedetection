//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.dnn;

import java.lang.String;

// C++: class DictValue
//javadoc: DictValue

public class DictValue {

    protected final long nativeObj;
    protected DictValue(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static DictValue __fromPtr__(long addr) { return new DictValue(addr); }

    //
    // C++:   cv::dnn::DictValue::DictValue(String s)
    //

    //javadoc: DictValue::DictValue(s)
    public   DictValue(String s)
    {
        
        nativeObj = DictValue_0(s);
        
        return;
    }


    //
    // C++:   cv::dnn::DictValue::DictValue(double p)
    //

    //javadoc: DictValue::DictValue(p)
    public   DictValue(double p)
    {
        
        nativeObj = DictValue_1(p);
        
        return;
    }


    //
    // C++:   cv::dnn::DictValue::DictValue(int i)
    //

    //javadoc: DictValue::DictValue(i)
    public   DictValue(int i)
    {
        
        nativeObj = DictValue_2(i);
        
        return;
    }


    //
    // C++:  String cv::dnn::DictValue::getStringValue(int idx = -1)
    //

    //javadoc: DictValue::getStringValue(idx)
    public  String getStringValue(int idx)
    {
        
        String retVal = getStringValue_0(nativeObj, idx);
        
        return retVal;
    }

    //javadoc: DictValue::getStringValue()
    public  String getStringValue()
    {
        
        String retVal = getStringValue_1(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::dnn::DictValue::isInt()
    //

    //javadoc: DictValue::isInt()
    public  boolean isInt()
    {
        
        boolean retVal = isInt_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::dnn::DictValue::isReal()
    //

    //javadoc: DictValue::isReal()
    public  boolean isReal()
    {
        
        boolean retVal = isReal_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::dnn::DictValue::isString()
    //

    //javadoc: DictValue::isString()
    public  boolean isString()
    {
        
        boolean retVal = isString_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::dnn::DictValue::getRealValue(int idx = -1)
    //

    //javadoc: DictValue::getRealValue(idx)
    public  double getRealValue(int idx)
    {
        
        double retVal = getRealValue_0(nativeObj, idx);
        
        return retVal;
    }

    //javadoc: DictValue::getRealValue()
    public  double getRealValue()
    {
        
        double retVal = getRealValue_1(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::dnn::DictValue::getIntValue(int idx = -1)
    //

    //javadoc: DictValue::getIntValue(idx)
    public  int getIntValue(int idx)
    {
        
        int retVal = getIntValue_0(nativeObj, idx);
        
        return retVal;
    }

    //javadoc: DictValue::getIntValue()
    public  int getIntValue()
    {
        
        int retVal = getIntValue_1(nativeObj);
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::dnn::DictValue::DictValue(String s)
    private static native long DictValue_0(String s);

    // C++:   cv::dnn::DictValue::DictValue(double p)
    private static native long DictValue_1(double p);

    // C++:   cv::dnn::DictValue::DictValue(int i)
    private static native long DictValue_2(int i);

    // C++:  String cv::dnn::DictValue::getStringValue(int idx = -1)
    private static native String getStringValue_0(long nativeObj, int idx);
    private static native String getStringValue_1(long nativeObj);

    // C++:  bool cv::dnn::DictValue::isInt()
    private static native boolean isInt_0(long nativeObj);

    // C++:  bool cv::dnn::DictValue::isReal()
    private static native boolean isReal_0(long nativeObj);

    // C++:  bool cv::dnn::DictValue::isString()
    private static native boolean isString_0(long nativeObj);

    // C++:  double cv::dnn::DictValue::getRealValue(int idx = -1)
    private static native double getRealValue_0(long nativeObj, int idx);
    private static native double getRealValue_1(long nativeObj);

    // C++:  int cv::dnn::DictValue::getIntValue(int idx = -1)
    private static native int getIntValue_0(long nativeObj, int idx);
    private static native int getIntValue_1(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
