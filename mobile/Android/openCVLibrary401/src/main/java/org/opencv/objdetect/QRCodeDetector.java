//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import org.opencv.core.Mat;

// C++: class QRCodeDetector
//javadoc: QRCodeDetector

public class QRCodeDetector {

    protected final long nativeObj;
    protected QRCodeDetector(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static QRCodeDetector __fromPtr__(long addr) { return new QRCodeDetector(addr); }

    //
    // C++:   cv::QRCodeDetector::QRCodeDetector()
    //

    //javadoc: QRCodeDetector::QRCodeDetector()
    public   QRCodeDetector()
    {
        
        nativeObj = QRCodeDetector_0();
        
        return;
    }


    //
    // C++:  bool cv::QRCodeDetector::detect(Mat img, Mat& points)
    //

    //javadoc: QRCodeDetector::detect(img, points)
    public  boolean detect(Mat img, Mat points)
    {
        
        boolean retVal = detect_0(nativeObj, img.nativeObj, points.nativeObj);
        
        return retVal;
    }


    //
    // C++:  string cv::QRCodeDetector::decode(Mat img, Mat points, Mat& straight_qrcode = Mat())
    //

    // Return type 'string' is not supported, skipping the function


    //
    // C++:  string cv::QRCodeDetector::detectAndDecode(Mat img, Mat& points = Mat(), Mat& straight_qrcode = Mat())
    //

    // Return type 'string' is not supported, skipping the function


    //
    // C++:  void cv::QRCodeDetector::setEpsX(double epsX)
    //

    //javadoc: QRCodeDetector::setEpsX(epsX)
    public  void setEpsX(double epsX)
    {
        
        setEpsX_0(nativeObj, epsX);
        
        return;
    }


    //
    // C++:  void cv::QRCodeDetector::setEpsY(double epsY)
    //

    //javadoc: QRCodeDetector::setEpsY(epsY)
    public  void setEpsY(double epsY)
    {
        
        setEpsY_0(nativeObj, epsY);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::QRCodeDetector::QRCodeDetector()
    private static native long QRCodeDetector_0();

    // C++:  bool cv::QRCodeDetector::detect(Mat img, Mat& points)
    private static native boolean detect_0(long nativeObj, long img_nativeObj, long points_nativeObj);

    // C++:  void cv::QRCodeDetector::setEpsX(double epsX)
    private static native void setEpsX_0(long nativeObj, double epsX);

    // C++:  void cv::QRCodeDetector::setEpsY(double epsY)
    private static native void setEpsY_0(long nativeObj, double epsY);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
