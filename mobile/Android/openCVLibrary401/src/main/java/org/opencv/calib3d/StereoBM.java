//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.calib3d;

import org.opencv.calib3d.StereoBM;
import org.opencv.calib3d.StereoMatcher;
import org.opencv.core.Rect;

// C++: class StereoBM
//javadoc: StereoBM

public class StereoBM extends StereoMatcher {

    protected StereoBM(long addr) { super(addr); }

    // internal usage only
    public static StereoBM __fromPtr__(long addr) { return new StereoBM(addr); }

    // C++: enum <unnamed>
    public static final int
            PREFILTER_NORMALIZED_RESPONSE = 0,
            PREFILTER_XSOBEL = 1;


    //
    // C++: static Ptr_StereoBM cv::StereoBM::create(int numDisparities = 0, int blockSize = 21)
    //

    //javadoc: StereoBM::create(numDisparities, blockSize)
    public static StereoBM create(int numDisparities, int blockSize)
    {
        
        StereoBM retVal = StereoBM.__fromPtr__(create_0(numDisparities, blockSize));
        
        return retVal;
    }

    //javadoc: StereoBM::create(numDisparities)
    public static StereoBM create(int numDisparities)
    {
        
        StereoBM retVal = StereoBM.__fromPtr__(create_1(numDisparities));
        
        return retVal;
    }

    //javadoc: StereoBM::create()
    public static StereoBM create()
    {
        
        StereoBM retVal = StereoBM.__fromPtr__(create_2());
        
        return retVal;
    }


    //
    // C++:  Rect cv::StereoBM::getROI1()
    //

    //javadoc: StereoBM::getROI1()
    public  Rect getROI1()
    {
        
        Rect retVal = new Rect(getROI1_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  Rect cv::StereoBM::getROI2()
    //

    //javadoc: StereoBM::getROI2()
    public  Rect getROI2()
    {
        
        Rect retVal = new Rect(getROI2_0(nativeObj));
        
        return retVal;
    }


    //
    // C++:  int cv::StereoBM::getPreFilterCap()
    //

    //javadoc: StereoBM::getPreFilterCap()
    public  int getPreFilterCap()
    {
        
        int retVal = getPreFilterCap_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoBM::getPreFilterSize()
    //

    //javadoc: StereoBM::getPreFilterSize()
    public  int getPreFilterSize()
    {
        
        int retVal = getPreFilterSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoBM::getPreFilterType()
    //

    //javadoc: StereoBM::getPreFilterType()
    public  int getPreFilterType()
    {
        
        int retVal = getPreFilterType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoBM::getSmallerBlockSize()
    //

    //javadoc: StereoBM::getSmallerBlockSize()
    public  int getSmallerBlockSize()
    {
        
        int retVal = getSmallerBlockSize_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoBM::getTextureThreshold()
    //

    //javadoc: StereoBM::getTextureThreshold()
    public  int getTextureThreshold()
    {
        
        int retVal = getTextureThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::StereoBM::getUniquenessRatio()
    //

    //javadoc: StereoBM::getUniquenessRatio()
    public  int getUniquenessRatio()
    {
        
        int retVal = getUniquenessRatio_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::StereoBM::setPreFilterCap(int preFilterCap)
    //

    //javadoc: StereoBM::setPreFilterCap(preFilterCap)
    public  void setPreFilterCap(int preFilterCap)
    {
        
        setPreFilterCap_0(nativeObj, preFilterCap);
        
        return;
    }


    //
    // C++:  void cv::StereoBM::setPreFilterSize(int preFilterSize)
    //

    //javadoc: StereoBM::setPreFilterSize(preFilterSize)
    public  void setPreFilterSize(int preFilterSize)
    {
        
        setPreFilterSize_0(nativeObj, preFilterSize);
        
        return;
    }


    //
    // C++:  void cv::StereoBM::setPreFilterType(int preFilterType)
    //

    //javadoc: StereoBM::setPreFilterType(preFilterType)
    public  void setPreFilterType(int preFilterType)
    {
        
        setPreFilterType_0(nativeObj, preFilterType);
        
        return;
    }


    //
    // C++:  void cv::StereoBM::setROI1(Rect roi1)
    //

    //javadoc: StereoBM::setROI1(roi1)
    public  void setROI1(Rect roi1)
    {
        
        setROI1_0(nativeObj, roi1.x, roi1.y, roi1.width, roi1.height);
        
        return;
    }


    //
    // C++:  void cv::StereoBM::setROI2(Rect roi2)
    //

    //javadoc: StereoBM::setROI2(roi2)
    public  void setROI2(Rect roi2)
    {
        
        setROI2_0(nativeObj, roi2.x, roi2.y, roi2.width, roi2.height);
        
        return;
    }


    //
    // C++:  void cv::StereoBM::setSmallerBlockSize(int blockSize)
    //

    //javadoc: StereoBM::setSmallerBlockSize(blockSize)
    public  void setSmallerBlockSize(int blockSize)
    {
        
        setSmallerBlockSize_0(nativeObj, blockSize);
        
        return;
    }


    //
    // C++:  void cv::StereoBM::setTextureThreshold(int textureThreshold)
    //

    //javadoc: StereoBM::setTextureThreshold(textureThreshold)
    public  void setTextureThreshold(int textureThreshold)
    {
        
        setTextureThreshold_0(nativeObj, textureThreshold);
        
        return;
    }


    //
    // C++:  void cv::StereoBM::setUniquenessRatio(int uniquenessRatio)
    //

    //javadoc: StereoBM::setUniquenessRatio(uniquenessRatio)
    public  void setUniquenessRatio(int uniquenessRatio)
    {
        
        setUniquenessRatio_0(nativeObj, uniquenessRatio);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_StereoBM cv::StereoBM::create(int numDisparities = 0, int blockSize = 21)
    private static native long create_0(int numDisparities, int blockSize);
    private static native long create_1(int numDisparities);
    private static native long create_2();

    // C++:  Rect cv::StereoBM::getROI1()
    private static native double[] getROI1_0(long nativeObj);

    // C++:  Rect cv::StereoBM::getROI2()
    private static native double[] getROI2_0(long nativeObj);

    // C++:  int cv::StereoBM::getPreFilterCap()
    private static native int getPreFilterCap_0(long nativeObj);

    // C++:  int cv::StereoBM::getPreFilterSize()
    private static native int getPreFilterSize_0(long nativeObj);

    // C++:  int cv::StereoBM::getPreFilterType()
    private static native int getPreFilterType_0(long nativeObj);

    // C++:  int cv::StereoBM::getSmallerBlockSize()
    private static native int getSmallerBlockSize_0(long nativeObj);

    // C++:  int cv::StereoBM::getTextureThreshold()
    private static native int getTextureThreshold_0(long nativeObj);

    // C++:  int cv::StereoBM::getUniquenessRatio()
    private static native int getUniquenessRatio_0(long nativeObj);

    // C++:  void cv::StereoBM::setPreFilterCap(int preFilterCap)
    private static native void setPreFilterCap_0(long nativeObj, int preFilterCap);

    // C++:  void cv::StereoBM::setPreFilterSize(int preFilterSize)
    private static native void setPreFilterSize_0(long nativeObj, int preFilterSize);

    // C++:  void cv::StereoBM::setPreFilterType(int preFilterType)
    private static native void setPreFilterType_0(long nativeObj, int preFilterType);

    // C++:  void cv::StereoBM::setROI1(Rect roi1)
    private static native void setROI1_0(long nativeObj, int roi1_x, int roi1_y, int roi1_width, int roi1_height);

    // C++:  void cv::StereoBM::setROI2(Rect roi2)
    private static native void setROI2_0(long nativeObj, int roi2_x, int roi2_y, int roi2_width, int roi2_height);

    // C++:  void cv::StereoBM::setSmallerBlockSize(int blockSize)
    private static native void setSmallerBlockSize_0(long nativeObj, int blockSize);

    // C++:  void cv::StereoBM::setTextureThreshold(int textureThreshold)
    private static native void setTextureThreshold_0(long nativeObj, int textureThreshold);

    // C++:  void cv::StereoBM::setUniquenessRatio(int uniquenessRatio)
    private static native void setUniquenessRatio_0(long nativeObj, int uniquenessRatio);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
