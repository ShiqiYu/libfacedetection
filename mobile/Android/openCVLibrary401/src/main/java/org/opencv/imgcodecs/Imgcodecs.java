//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.imgcodecs;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.utils.Converters;

// C++: class Imgcodecs
//javadoc: Imgcodecs

public class Imgcodecs {

    // C++: enum ImwriteFlags
    public static final int
            IMWRITE_JPEG_QUALITY = 1,
            IMWRITE_JPEG_PROGRESSIVE = 2,
            IMWRITE_JPEG_OPTIMIZE = 3,
            IMWRITE_JPEG_RST_INTERVAL = 4,
            IMWRITE_JPEG_LUMA_QUALITY = 5,
            IMWRITE_JPEG_CHROMA_QUALITY = 6,
            IMWRITE_PNG_COMPRESSION = 16,
            IMWRITE_PNG_STRATEGY = 17,
            IMWRITE_PNG_BILEVEL = 18,
            IMWRITE_PXM_BINARY = 32,
            IMWRITE_EXR_TYPE = (3 << 4) + 0,
            IMWRITE_WEBP_QUALITY = 64,
            IMWRITE_PAM_TUPLETYPE = 128,
            IMWRITE_TIFF_RESUNIT = 256,
            IMWRITE_TIFF_XDPI = 257,
            IMWRITE_TIFF_YDPI = 258;


    // C++: enum ImreadModes
    public static final int
            IMREAD_UNCHANGED = -1,
            IMREAD_GRAYSCALE = 0,
            IMREAD_COLOR = 1,
            IMREAD_ANYDEPTH = 2,
            IMREAD_ANYCOLOR = 4,
            IMREAD_LOAD_GDAL = 8,
            IMREAD_REDUCED_GRAYSCALE_2 = 16,
            IMREAD_REDUCED_COLOR_2 = 17,
            IMREAD_REDUCED_GRAYSCALE_4 = 32,
            IMREAD_REDUCED_COLOR_4 = 33,
            IMREAD_REDUCED_GRAYSCALE_8 = 64,
            IMREAD_REDUCED_COLOR_8 = 65,
            IMREAD_IGNORE_ORIENTATION = 128;


    // C++: enum ImwritePAMFlags
    public static final int
            IMWRITE_PAM_FORMAT_NULL = 0,
            IMWRITE_PAM_FORMAT_BLACKANDWHITE = 1,
            IMWRITE_PAM_FORMAT_GRAYSCALE = 2,
            IMWRITE_PAM_FORMAT_GRAYSCALE_ALPHA = 3,
            IMWRITE_PAM_FORMAT_RGB = 4,
            IMWRITE_PAM_FORMAT_RGB_ALPHA = 5;


    // C++: enum ImwriteEXRTypeFlags
    public static final int
            IMWRITE_EXR_TYPE_HALF = 1,
            IMWRITE_EXR_TYPE_FLOAT = 2;


    // C++: enum ImwritePNGFlags
    public static final int
            IMWRITE_PNG_STRATEGY_DEFAULT = 0,
            IMWRITE_PNG_STRATEGY_FILTERED = 1,
            IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY = 2,
            IMWRITE_PNG_STRATEGY_RLE = 3,
            IMWRITE_PNG_STRATEGY_FIXED = 4;


    //
    // C++:  Mat cv::imdecode(Mat buf, int flags)
    //

    //javadoc: imdecode(buf, flags)
    public static Mat imdecode(Mat buf, int flags)
    {
        
        Mat retVal = new Mat(imdecode_0(buf.nativeObj, flags));
        
        return retVal;
    }


    //
    // C++:  Mat cv::imread(String filename, int flags = IMREAD_COLOR)
    //

    //javadoc: imread(filename, flags)
    public static Mat imread(String filename, int flags)
    {
        
        Mat retVal = new Mat(imread_0(filename, flags));
        
        return retVal;
    }

    //javadoc: imread(filename)
    public static Mat imread(String filename)
    {
        
        Mat retVal = new Mat(imread_1(filename));
        
        return retVal;
    }


    //
    // C++:  bool cv::haveImageReader(String filename)
    //

    //javadoc: haveImageReader(filename)
    public static boolean haveImageReader(String filename)
    {
        
        boolean retVal = haveImageReader_0(filename);
        
        return retVal;
    }


    //
    // C++:  bool cv::haveImageWriter(String filename)
    //

    //javadoc: haveImageWriter(filename)
    public static boolean haveImageWriter(String filename)
    {
        
        boolean retVal = haveImageWriter_0(filename);
        
        return retVal;
    }


    //
    // C++:  bool cv::imencode(String ext, Mat img, vector_uchar& buf, vector_int params = std::vector<int>())
    //

    //javadoc: imencode(ext, img, buf, params)
    public static boolean imencode(String ext, Mat img, MatOfByte buf, MatOfInt params)
    {
        Mat buf_mat = buf;
        Mat params_mat = params;
        boolean retVal = imencode_0(ext, img.nativeObj, buf_mat.nativeObj, params_mat.nativeObj);
        
        return retVal;
    }

    //javadoc: imencode(ext, img, buf)
    public static boolean imencode(String ext, Mat img, MatOfByte buf)
    {
        Mat buf_mat = buf;
        boolean retVal = imencode_1(ext, img.nativeObj, buf_mat.nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::imreadmulti(String filename, vector_Mat& mats, int flags = IMREAD_ANYCOLOR)
    //

    //javadoc: imreadmulti(filename, mats, flags)
    public static boolean imreadmulti(String filename, List<Mat> mats, int flags)
    {
        Mat mats_mat = new Mat();
        boolean retVal = imreadmulti_0(filename, mats_mat.nativeObj, flags);
        Converters.Mat_to_vector_Mat(mats_mat, mats);
        mats_mat.release();
        return retVal;
    }

    //javadoc: imreadmulti(filename, mats)
    public static boolean imreadmulti(String filename, List<Mat> mats)
    {
        Mat mats_mat = new Mat();
        boolean retVal = imreadmulti_1(filename, mats_mat.nativeObj);
        Converters.Mat_to_vector_Mat(mats_mat, mats);
        mats_mat.release();
        return retVal;
    }


    //
    // C++:  bool cv::imwrite(String filename, Mat img, vector_int params = std::vector<int>())
    //

    //javadoc: imwrite(filename, img, params)
    public static boolean imwrite(String filename, Mat img, MatOfInt params)
    {
        Mat params_mat = params;
        boolean retVal = imwrite_0(filename, img.nativeObj, params_mat.nativeObj);
        
        return retVal;
    }

    //javadoc: imwrite(filename, img)
    public static boolean imwrite(String filename, Mat img)
    {
        
        boolean retVal = imwrite_1(filename, img.nativeObj);
        
        return retVal;
    }




    // C++:  Mat cv::imdecode(Mat buf, int flags)
    private static native long imdecode_0(long buf_nativeObj, int flags);

    // C++:  Mat cv::imread(String filename, int flags = IMREAD_COLOR)
    private static native long imread_0(String filename, int flags);
    private static native long imread_1(String filename);

    // C++:  bool cv::haveImageReader(String filename)
    private static native boolean haveImageReader_0(String filename);

    // C++:  bool cv::haveImageWriter(String filename)
    private static native boolean haveImageWriter_0(String filename);

    // C++:  bool cv::imencode(String ext, Mat img, vector_uchar& buf, vector_int params = std::vector<int>())
    private static native boolean imencode_0(String ext, long img_nativeObj, long buf_mat_nativeObj, long params_mat_nativeObj);
    private static native boolean imencode_1(String ext, long img_nativeObj, long buf_mat_nativeObj);

    // C++:  bool cv::imreadmulti(String filename, vector_Mat& mats, int flags = IMREAD_ANYCOLOR)
    private static native boolean imreadmulti_0(String filename, long mats_mat_nativeObj, int flags);
    private static native boolean imreadmulti_1(String filename, long mats_mat_nativeObj);

    // C++:  bool cv::imwrite(String filename, Mat img, vector_int params = std::vector<int>())
    private static native boolean imwrite_0(String filename, long img_nativeObj, long params_mat_nativeObj);
    private static native boolean imwrite_1(String filename, long img_nativeObj);

}
