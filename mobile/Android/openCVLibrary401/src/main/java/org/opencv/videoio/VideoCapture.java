//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.videoio;

import java.lang.String;
import org.opencv.core.Mat;

// C++: class VideoCapture
//javadoc: VideoCapture

public class VideoCapture {

    protected final long nativeObj;
    protected VideoCapture(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static VideoCapture __fromPtr__(long addr) { return new VideoCapture(addr); }

    //
    // C++:   cv::VideoCapture::VideoCapture(String filename, int apiPreference = CAP_ANY)
    //

    //javadoc: VideoCapture::VideoCapture(filename, apiPreference)
    public   VideoCapture(String filename, int apiPreference)
    {
        
        nativeObj = VideoCapture_0(filename, apiPreference);
        
        return;
    }

    //javadoc: VideoCapture::VideoCapture(filename)
    public   VideoCapture(String filename)
    {
        
        nativeObj = VideoCapture_1(filename);
        
        return;
    }


    //
    // C++:   cv::VideoCapture::VideoCapture(int index, int apiPreference = CAP_ANY)
    //

    //javadoc: VideoCapture::VideoCapture(index, apiPreference)
    public   VideoCapture(int index, int apiPreference)
    {
        
        nativeObj = VideoCapture_2(index, apiPreference);
        
        return;
    }

    //javadoc: VideoCapture::VideoCapture(index)
    public   VideoCapture(int index)
    {
        
        nativeObj = VideoCapture_3(index);
        
        return;
    }


    //
    // C++:   cv::VideoCapture::VideoCapture()
    //

    //javadoc: VideoCapture::VideoCapture()
    public   VideoCapture()
    {
        
        nativeObj = VideoCapture_4();
        
        return;
    }


    //
    // C++:  String cv::VideoCapture::getBackendName()
    //

    //javadoc: VideoCapture::getBackendName()
    public  String getBackendName()
    {
        
        String retVal = getBackendName_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::VideoCapture::grab()
    //

    //javadoc: VideoCapture::grab()
    public  boolean grab()
    {
        
        boolean retVal = grab_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::VideoCapture::isOpened()
    //

    //javadoc: VideoCapture::isOpened()
    public  boolean isOpened()
    {
        
        boolean retVal = isOpened_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::VideoCapture::open(String filename, int apiPreference = CAP_ANY)
    //

    //javadoc: VideoCapture::open(filename, apiPreference)
    public  boolean open(String filename, int apiPreference)
    {
        
        boolean retVal = open_0(nativeObj, filename, apiPreference);
        
        return retVal;
    }

    //javadoc: VideoCapture::open(filename)
    public  boolean open(String filename)
    {
        
        boolean retVal = open_1(nativeObj, filename);
        
        return retVal;
    }


    //
    // C++:  bool cv::VideoCapture::open(int index, int apiPreference = CAP_ANY)
    //

    //javadoc: VideoCapture::open(index, apiPreference)
    public  boolean open(int index, int apiPreference)
    {
        
        boolean retVal = open_2(nativeObj, index, apiPreference);
        
        return retVal;
    }

    //javadoc: VideoCapture::open(index)
    public  boolean open(int index)
    {
        
        boolean retVal = open_3(nativeObj, index);
        
        return retVal;
    }


    //
    // C++:  bool cv::VideoCapture::read(Mat& image)
    //

    //javadoc: VideoCapture::read(image)
    public  boolean read(Mat image)
    {
        
        boolean retVal = read_0(nativeObj, image.nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::VideoCapture::retrieve(Mat& image, int flag = 0)
    //

    //javadoc: VideoCapture::retrieve(image, flag)
    public  boolean retrieve(Mat image, int flag)
    {
        
        boolean retVal = retrieve_0(nativeObj, image.nativeObj, flag);
        
        return retVal;
    }

    //javadoc: VideoCapture::retrieve(image)
    public  boolean retrieve(Mat image)
    {
        
        boolean retVal = retrieve_1(nativeObj, image.nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::VideoCapture::set(int propId, double value)
    //

    //javadoc: VideoCapture::set(propId, value)
    public  boolean set(int propId, double value)
    {
        
        boolean retVal = set_0(nativeObj, propId, value);
        
        return retVal;
    }


    //
    // C++:  double cv::VideoCapture::get(int propId)
    //

    //javadoc: VideoCapture::get(propId)
    public  double get(int propId)
    {
        
        double retVal = get_0(nativeObj, propId);
        
        return retVal;
    }


    //
    // C++:  void cv::VideoCapture::release()
    //

    //javadoc: VideoCapture::release()
    public  void release()
    {
        
        release_0(nativeObj);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::VideoCapture::VideoCapture(String filename, int apiPreference = CAP_ANY)
    private static native long VideoCapture_0(String filename, int apiPreference);
    private static native long VideoCapture_1(String filename);

    // C++:   cv::VideoCapture::VideoCapture(int index, int apiPreference = CAP_ANY)
    private static native long VideoCapture_2(int index, int apiPreference);
    private static native long VideoCapture_3(int index);

    // C++:   cv::VideoCapture::VideoCapture()
    private static native long VideoCapture_4();

    // C++:  String cv::VideoCapture::getBackendName()
    private static native String getBackendName_0(long nativeObj);

    // C++:  bool cv::VideoCapture::grab()
    private static native boolean grab_0(long nativeObj);

    // C++:  bool cv::VideoCapture::isOpened()
    private static native boolean isOpened_0(long nativeObj);

    // C++:  bool cv::VideoCapture::open(String filename, int apiPreference = CAP_ANY)
    private static native boolean open_0(long nativeObj, String filename, int apiPreference);
    private static native boolean open_1(long nativeObj, String filename);

    // C++:  bool cv::VideoCapture::open(int index, int apiPreference = CAP_ANY)
    private static native boolean open_2(long nativeObj, int index, int apiPreference);
    private static native boolean open_3(long nativeObj, int index);

    // C++:  bool cv::VideoCapture::read(Mat& image)
    private static native boolean read_0(long nativeObj, long image_nativeObj);

    // C++:  bool cv::VideoCapture::retrieve(Mat& image, int flag = 0)
    private static native boolean retrieve_0(long nativeObj, long image_nativeObj, int flag);
    private static native boolean retrieve_1(long nativeObj, long image_nativeObj);

    // C++:  bool cv::VideoCapture::set(int propId, double value)
    private static native boolean set_0(long nativeObj, int propId, double value);

    // C++:  double cv::VideoCapture::get(int propId)
    private static native double get_0(long nativeObj, int propId);

    // C++:  void cv::VideoCapture::release()
    private static native void release_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
