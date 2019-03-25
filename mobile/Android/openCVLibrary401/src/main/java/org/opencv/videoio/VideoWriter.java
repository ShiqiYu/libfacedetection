//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.videoio;

import java.lang.String;
import org.opencv.core.Mat;
import org.opencv.core.Size;

// C++: class VideoWriter
//javadoc: VideoWriter

public class VideoWriter {

    protected final long nativeObj;
    protected VideoWriter(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static VideoWriter __fromPtr__(long addr) { return new VideoWriter(addr); }

    //
    // C++:   cv::VideoWriter::VideoWriter(String filename, int apiPreference, int fourcc, double fps, Size frameSize, bool isColor = true)
    //

    //javadoc: VideoWriter::VideoWriter(filename, apiPreference, fourcc, fps, frameSize, isColor)
    public   VideoWriter(String filename, int apiPreference, int fourcc, double fps, Size frameSize, boolean isColor)
    {
        
        nativeObj = VideoWriter_0(filename, apiPreference, fourcc, fps, frameSize.width, frameSize.height, isColor);
        
        return;
    }

    //javadoc: VideoWriter::VideoWriter(filename, apiPreference, fourcc, fps, frameSize)
    public   VideoWriter(String filename, int apiPreference, int fourcc, double fps, Size frameSize)
    {
        
        nativeObj = VideoWriter_1(filename, apiPreference, fourcc, fps, frameSize.width, frameSize.height);
        
        return;
    }


    //
    // C++:   cv::VideoWriter::VideoWriter(String filename, int fourcc, double fps, Size frameSize, bool isColor = true)
    //

    //javadoc: VideoWriter::VideoWriter(filename, fourcc, fps, frameSize, isColor)
    public   VideoWriter(String filename, int fourcc, double fps, Size frameSize, boolean isColor)
    {
        
        nativeObj = VideoWriter_2(filename, fourcc, fps, frameSize.width, frameSize.height, isColor);
        
        return;
    }

    //javadoc: VideoWriter::VideoWriter(filename, fourcc, fps, frameSize)
    public   VideoWriter(String filename, int fourcc, double fps, Size frameSize)
    {
        
        nativeObj = VideoWriter_3(filename, fourcc, fps, frameSize.width, frameSize.height);
        
        return;
    }


    //
    // C++:   cv::VideoWriter::VideoWriter()
    //

    //javadoc: VideoWriter::VideoWriter()
    public   VideoWriter()
    {
        
        nativeObj = VideoWriter_4();
        
        return;
    }


    //
    // C++:  String cv::VideoWriter::getBackendName()
    //

    //javadoc: VideoWriter::getBackendName()
    public  String getBackendName()
    {
        
        String retVal = getBackendName_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::VideoWriter::isOpened()
    //

    //javadoc: VideoWriter::isOpened()
    public  boolean isOpened()
    {
        
        boolean retVal = isOpened_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::VideoWriter::open(String filename, int apiPreference, int fourcc, double fps, Size frameSize, bool isColor = true)
    //

    //javadoc: VideoWriter::open(filename, apiPreference, fourcc, fps, frameSize, isColor)
    public  boolean open(String filename, int apiPreference, int fourcc, double fps, Size frameSize, boolean isColor)
    {
        
        boolean retVal = open_0(nativeObj, filename, apiPreference, fourcc, fps, frameSize.width, frameSize.height, isColor);
        
        return retVal;
    }

    //javadoc: VideoWriter::open(filename, apiPreference, fourcc, fps, frameSize)
    public  boolean open(String filename, int apiPreference, int fourcc, double fps, Size frameSize)
    {
        
        boolean retVal = open_1(nativeObj, filename, apiPreference, fourcc, fps, frameSize.width, frameSize.height);
        
        return retVal;
    }


    //
    // C++:  bool cv::VideoWriter::open(String filename, int fourcc, double fps, Size frameSize, bool isColor = true)
    //

    //javadoc: VideoWriter::open(filename, fourcc, fps, frameSize, isColor)
    public  boolean open(String filename, int fourcc, double fps, Size frameSize, boolean isColor)
    {
        
        boolean retVal = open_2(nativeObj, filename, fourcc, fps, frameSize.width, frameSize.height, isColor);
        
        return retVal;
    }

    //javadoc: VideoWriter::open(filename, fourcc, fps, frameSize)
    public  boolean open(String filename, int fourcc, double fps, Size frameSize)
    {
        
        boolean retVal = open_3(nativeObj, filename, fourcc, fps, frameSize.width, frameSize.height);
        
        return retVal;
    }


    //
    // C++:  bool cv::VideoWriter::set(int propId, double value)
    //

    //javadoc: VideoWriter::set(propId, value)
    public  boolean set(int propId, double value)
    {
        
        boolean retVal = set_0(nativeObj, propId, value);
        
        return retVal;
    }


    //
    // C++:  double cv::VideoWriter::get(int propId)
    //

    //javadoc: VideoWriter::get(propId)
    public  double get(int propId)
    {
        
        double retVal = get_0(nativeObj, propId);
        
        return retVal;
    }


    //
    // C++: static int cv::VideoWriter::fourcc(char c1, char c2, char c3, char c4)
    //

    //javadoc: VideoWriter::fourcc(c1, c2, c3, c4)
    public static int fourcc(char c1, char c2, char c3, char c4)
    {
        
        int retVal = fourcc_0(c1, c2, c3, c4);
        
        return retVal;
    }


    //
    // C++:  void cv::VideoWriter::release()
    //

    //javadoc: VideoWriter::release()
    public  void release()
    {
        
        release_0(nativeObj);
        
        return;
    }


    //
    // C++:  void cv::VideoWriter::write(Mat image)
    //

    //javadoc: VideoWriter::write(image)
    public  void write(Mat image)
    {
        
        write_0(nativeObj, image.nativeObj);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::VideoWriter::VideoWriter(String filename, int apiPreference, int fourcc, double fps, Size frameSize, bool isColor = true)
    private static native long VideoWriter_0(String filename, int apiPreference, int fourcc, double fps, double frameSize_width, double frameSize_height, boolean isColor);
    private static native long VideoWriter_1(String filename, int apiPreference, int fourcc, double fps, double frameSize_width, double frameSize_height);

    // C++:   cv::VideoWriter::VideoWriter(String filename, int fourcc, double fps, Size frameSize, bool isColor = true)
    private static native long VideoWriter_2(String filename, int fourcc, double fps, double frameSize_width, double frameSize_height, boolean isColor);
    private static native long VideoWriter_3(String filename, int fourcc, double fps, double frameSize_width, double frameSize_height);

    // C++:   cv::VideoWriter::VideoWriter()
    private static native long VideoWriter_4();

    // C++:  String cv::VideoWriter::getBackendName()
    private static native String getBackendName_0(long nativeObj);

    // C++:  bool cv::VideoWriter::isOpened()
    private static native boolean isOpened_0(long nativeObj);

    // C++:  bool cv::VideoWriter::open(String filename, int apiPreference, int fourcc, double fps, Size frameSize, bool isColor = true)
    private static native boolean open_0(long nativeObj, String filename, int apiPreference, int fourcc, double fps, double frameSize_width, double frameSize_height, boolean isColor);
    private static native boolean open_1(long nativeObj, String filename, int apiPreference, int fourcc, double fps, double frameSize_width, double frameSize_height);

    // C++:  bool cv::VideoWriter::open(String filename, int fourcc, double fps, Size frameSize, bool isColor = true)
    private static native boolean open_2(long nativeObj, String filename, int fourcc, double fps, double frameSize_width, double frameSize_height, boolean isColor);
    private static native boolean open_3(long nativeObj, String filename, int fourcc, double fps, double frameSize_width, double frameSize_height);

    // C++:  bool cv::VideoWriter::set(int propId, double value)
    private static native boolean set_0(long nativeObj, int propId, double value);

    // C++:  double cv::VideoWriter::get(int propId)
    private static native double get_0(long nativeObj, int propId);

    // C++: static int cv::VideoWriter::fourcc(char c1, char c2, char c3, char c4)
    private static native int fourcc_0(char c1, char c2, char c3, char c4);

    // C++:  void cv::VideoWriter::release()
    private static native void release_0(long nativeObj);

    // C++:  void cv::VideoWriter::write(Mat image)
    private static native void write_0(long nativeObj, long image_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
