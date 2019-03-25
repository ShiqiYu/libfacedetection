//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.imgproc;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.LineSegmentDetector;
import org.opencv.utils.Converters;

// C++: class Imgproc
//javadoc: Imgproc

public class Imgproc {

    private static final int
            IPL_BORDER_CONSTANT = 0,
            IPL_BORDER_REPLICATE = 1,
            IPL_BORDER_REFLECT = 2,
            IPL_BORDER_WRAP = 3,
            IPL_BORDER_REFLECT_101 = 4,
            IPL_BORDER_TRANSPARENT = 5,
            CV_INTER_NN = 0,
            CV_INTER_LINEAR = 1,
            CV_INTER_CUBIC = 2,
            CV_INTER_AREA = 3,
            CV_INTER_LANCZOS4 = 4,
            CV_MOP_ERODE = 0,
            CV_MOP_DILATE = 1,
            CV_MOP_OPEN = 2,
            CV_MOP_CLOSE = 3,
            CV_MOP_GRADIENT = 4,
            CV_MOP_TOPHAT = 5,
            CV_MOP_BLACKHAT = 6,
            CV_RETR_EXTERNAL = 0,
            CV_RETR_LIST = 1,
            CV_RETR_CCOMP = 2,
            CV_RETR_TREE = 3,
            CV_RETR_FLOODFILL = 4,
            CV_CHAIN_APPROX_NONE = 1,
            CV_CHAIN_APPROX_SIMPLE = 2,
            CV_CHAIN_APPROX_TC89_L1 = 3,
            CV_CHAIN_APPROX_TC89_KCOS = 4,
            CV_THRESH_BINARY = 0,
            CV_THRESH_BINARY_INV = 1,
            CV_THRESH_TRUNC = 2,
            CV_THRESH_TOZERO = 3,
            CV_THRESH_TOZERO_INV = 4,
            CV_THRESH_MASK = 7,
            CV_THRESH_OTSU = 8,
            CV_THRESH_TRIANGLE = 16;


    // C++: enum HersheyFonts
    public static final int
            FONT_HERSHEY_SIMPLEX = 0,
            FONT_HERSHEY_PLAIN = 1,
            FONT_HERSHEY_DUPLEX = 2,
            FONT_HERSHEY_COMPLEX = 3,
            FONT_HERSHEY_TRIPLEX = 4,
            FONT_HERSHEY_COMPLEX_SMALL = 5,
            FONT_HERSHEY_SCRIPT_SIMPLEX = 6,
            FONT_HERSHEY_SCRIPT_COMPLEX = 7,
            FONT_ITALIC = 16;


    // C++: enum InterpolationMasks
    public static final int
            INTER_BITS = 5,
            INTER_BITS2 = INTER_BITS * 2,
            INTER_TAB_SIZE = 1 << INTER_BITS,
            INTER_TAB_SIZE2 = INTER_TAB_SIZE * INTER_TAB_SIZE;


    // C++: enum MorphTypes
    public static final int
            MORPH_ERODE = 0,
            MORPH_DILATE = 1,
            MORPH_OPEN = 2,
            MORPH_CLOSE = 3,
            MORPH_GRADIENT = 4,
            MORPH_TOPHAT = 5,
            MORPH_BLACKHAT = 6,
            MORPH_HITMISS = 7;


    // C++: enum FloodFillFlags
    public static final int
            FLOODFILL_FIXED_RANGE = 1 << 16,
            FLOODFILL_MASK_ONLY = 1 << 17;


    // C++: enum HoughModes
    public static final int
            HOUGH_STANDARD = 0,
            HOUGH_PROBABILISTIC = 1,
            HOUGH_MULTI_SCALE = 2,
            HOUGH_GRADIENT = 3;


    // C++: enum ConnectedComponentsAlgorithmsTypes
    public static final int
            CCL_WU = 0,
            CCL_DEFAULT = -1,
            CCL_GRANA = 1;


    // C++: enum RetrievalModes
    public static final int
            RETR_EXTERNAL = 0,
            RETR_LIST = 1,
            RETR_CCOMP = 2,
            RETR_TREE = 3,
            RETR_FLOODFILL = 4;


    // C++: enum GrabCutClasses
    public static final int
            GC_BGD = 0,
            GC_FGD = 1,
            GC_PR_BGD = 2,
            GC_PR_FGD = 3;


    // C++: enum ColormapTypes
    public static final int
            COLORMAP_AUTUMN = 0,
            COLORMAP_BONE = 1,
            COLORMAP_JET = 2,
            COLORMAP_WINTER = 3,
            COLORMAP_RAINBOW = 4,
            COLORMAP_OCEAN = 5,
            COLORMAP_SUMMER = 6,
            COLORMAP_SPRING = 7,
            COLORMAP_COOL = 8,
            COLORMAP_HSV = 9,
            COLORMAP_PINK = 10,
            COLORMAP_HOT = 11,
            COLORMAP_PARULA = 12;


    // C++: enum HistCompMethods
    public static final int
            HISTCMP_CORREL = 0,
            HISTCMP_CHISQR = 1,
            HISTCMP_INTERSECT = 2,
            HISTCMP_BHATTACHARYYA = 3,
            HISTCMP_HELLINGER = HISTCMP_BHATTACHARYYA,
            HISTCMP_CHISQR_ALT = 4,
            HISTCMP_KL_DIV = 5;


    // C++: enum LineTypes
    public static final int
            FILLED = -1,
            LINE_4 = 4,
            LINE_8 = 8,
            LINE_AA = 16;


    // C++: enum InterpolationFlags
    public static final int
            INTER_NEAREST = 0,
            INTER_LINEAR = 1,
            INTER_CUBIC = 2,
            INTER_AREA = 3,
            INTER_LANCZOS4 = 4,
            INTER_LINEAR_EXACT = 5,
            INTER_MAX = 7,
            WARP_FILL_OUTLIERS = 8,
            WARP_INVERSE_MAP = 16;


    // C++: enum SpecialFilter
    public static final int
            FILTER_SCHARR = -1;


    // C++: enum ContourApproximationModes
    public static final int
            CHAIN_APPROX_NONE = 1,
            CHAIN_APPROX_SIMPLE = 2,
            CHAIN_APPROX_TC89_L1 = 3,
            CHAIN_APPROX_TC89_KCOS = 4;


    // C++: enum RectanglesIntersectTypes
    public static final int
            INTERSECT_NONE = 0,
            INTERSECT_PARTIAL = 1,
            INTERSECT_FULL = 2;


    // C++: enum <unnamed>
    public static final int
            CV_GAUSSIAN_5x5 = 7,
            CV_SCHARR = -1,
            CV_MAX_SOBEL_KSIZE = 7,
            CV_RGBA2mRGBA = 125,
            CV_mRGBA2RGBA = 126,
            CV_WARP_FILL_OUTLIERS = 8,
            CV_WARP_INVERSE_MAP = 16,
            CV_CHAIN_CODE = 0,
            CV_LINK_RUNS = 5,
            CV_POLY_APPROX_DP = 0,
            CV_CONTOURS_MATCH_I1 = 1,
            CV_CONTOURS_MATCH_I2 = 2,
            CV_CONTOURS_MATCH_I3 = 3,
            CV_CLOCKWISE = 1,
            CV_COUNTER_CLOCKWISE = 2,
            CV_COMP_CORREL = 0,
            CV_COMP_CHISQR = 1,
            CV_COMP_INTERSECT = 2,
            CV_COMP_BHATTACHARYYA = 3,
            CV_COMP_HELLINGER = CV_COMP_BHATTACHARYYA,
            CV_COMP_CHISQR_ALT = 4,
            CV_COMP_KL_DIV = 5,
            CV_DIST_MASK_3 = 3,
            CV_DIST_MASK_5 = 5,
            CV_DIST_MASK_PRECISE = 0,
            CV_DIST_LABEL_CCOMP = 0,
            CV_DIST_LABEL_PIXEL = 1,
            CV_DIST_USER = -1,
            CV_DIST_L1 = 1,
            CV_DIST_L2 = 2,
            CV_DIST_C = 3,
            CV_DIST_L12 = 4,
            CV_DIST_FAIR = 5,
            CV_DIST_WELSCH = 6,
            CV_DIST_HUBER = 7,
            CV_CANNY_L2_GRADIENT = (1 << 31),
            CV_HOUGH_STANDARD = 0,
            CV_HOUGH_PROBABILISTIC = 1,
            CV_HOUGH_MULTI_SCALE = 2,
            CV_HOUGH_GRADIENT = 3;


    // C++: enum ShapeMatchModes
    public static final int
            CONTOURS_MATCH_I1 = 1,
            CONTOURS_MATCH_I2 = 2,
            CONTOURS_MATCH_I3 = 3;


    // C++: enum WarpPolarMode
    public static final int
            WARP_POLAR_LINEAR = 0,
            WARP_POLAR_LOG = 256;


    // C++: enum ColorConversionCodes
    public static final int
            COLOR_BGR2BGRA = 0,
            COLOR_RGB2RGBA = COLOR_BGR2BGRA,
            COLOR_BGRA2BGR = 1,
            COLOR_RGBA2RGB = COLOR_BGRA2BGR,
            COLOR_BGR2RGBA = 2,
            COLOR_RGB2BGRA = COLOR_BGR2RGBA,
            COLOR_RGBA2BGR = 3,
            COLOR_BGRA2RGB = COLOR_RGBA2BGR,
            COLOR_BGR2RGB = 4,
            COLOR_RGB2BGR = COLOR_BGR2RGB,
            COLOR_BGRA2RGBA = 5,
            COLOR_RGBA2BGRA = COLOR_BGRA2RGBA,
            COLOR_BGR2GRAY = 6,
            COLOR_RGB2GRAY = 7,
            COLOR_GRAY2BGR = 8,
            COLOR_GRAY2RGB = COLOR_GRAY2BGR,
            COLOR_GRAY2BGRA = 9,
            COLOR_GRAY2RGBA = COLOR_GRAY2BGRA,
            COLOR_BGRA2GRAY = 10,
            COLOR_RGBA2GRAY = 11,
            COLOR_BGR2BGR565 = 12,
            COLOR_RGB2BGR565 = 13,
            COLOR_BGR5652BGR = 14,
            COLOR_BGR5652RGB = 15,
            COLOR_BGRA2BGR565 = 16,
            COLOR_RGBA2BGR565 = 17,
            COLOR_BGR5652BGRA = 18,
            COLOR_BGR5652RGBA = 19,
            COLOR_GRAY2BGR565 = 20,
            COLOR_BGR5652GRAY = 21,
            COLOR_BGR2BGR555 = 22,
            COLOR_RGB2BGR555 = 23,
            COLOR_BGR5552BGR = 24,
            COLOR_BGR5552RGB = 25,
            COLOR_BGRA2BGR555 = 26,
            COLOR_RGBA2BGR555 = 27,
            COLOR_BGR5552BGRA = 28,
            COLOR_BGR5552RGBA = 29,
            COLOR_GRAY2BGR555 = 30,
            COLOR_BGR5552GRAY = 31,
            COLOR_BGR2XYZ = 32,
            COLOR_RGB2XYZ = 33,
            COLOR_XYZ2BGR = 34,
            COLOR_XYZ2RGB = 35,
            COLOR_BGR2YCrCb = 36,
            COLOR_RGB2YCrCb = 37,
            COLOR_YCrCb2BGR = 38,
            COLOR_YCrCb2RGB = 39,
            COLOR_BGR2HSV = 40,
            COLOR_RGB2HSV = 41,
            COLOR_BGR2Lab = 44,
            COLOR_RGB2Lab = 45,
            COLOR_BGR2Luv = 50,
            COLOR_RGB2Luv = 51,
            COLOR_BGR2HLS = 52,
            COLOR_RGB2HLS = 53,
            COLOR_HSV2BGR = 54,
            COLOR_HSV2RGB = 55,
            COLOR_Lab2BGR = 56,
            COLOR_Lab2RGB = 57,
            COLOR_Luv2BGR = 58,
            COLOR_Luv2RGB = 59,
            COLOR_HLS2BGR = 60,
            COLOR_HLS2RGB = 61,
            COLOR_BGR2HSV_FULL = 66,
            COLOR_RGB2HSV_FULL = 67,
            COLOR_BGR2HLS_FULL = 68,
            COLOR_RGB2HLS_FULL = 69,
            COLOR_HSV2BGR_FULL = 70,
            COLOR_HSV2RGB_FULL = 71,
            COLOR_HLS2BGR_FULL = 72,
            COLOR_HLS2RGB_FULL = 73,
            COLOR_LBGR2Lab = 74,
            COLOR_LRGB2Lab = 75,
            COLOR_LBGR2Luv = 76,
            COLOR_LRGB2Luv = 77,
            COLOR_Lab2LBGR = 78,
            COLOR_Lab2LRGB = 79,
            COLOR_Luv2LBGR = 80,
            COLOR_Luv2LRGB = 81,
            COLOR_BGR2YUV = 82,
            COLOR_RGB2YUV = 83,
            COLOR_YUV2BGR = 84,
            COLOR_YUV2RGB = 85,
            COLOR_YUV2RGB_NV12 = 90,
            COLOR_YUV2BGR_NV12 = 91,
            COLOR_YUV2RGB_NV21 = 92,
            COLOR_YUV2BGR_NV21 = 93,
            COLOR_YUV420sp2RGB = COLOR_YUV2RGB_NV21,
            COLOR_YUV420sp2BGR = COLOR_YUV2BGR_NV21,
            COLOR_YUV2RGBA_NV12 = 94,
            COLOR_YUV2BGRA_NV12 = 95,
            COLOR_YUV2RGBA_NV21 = 96,
            COLOR_YUV2BGRA_NV21 = 97,
            COLOR_YUV420sp2RGBA = COLOR_YUV2RGBA_NV21,
            COLOR_YUV420sp2BGRA = COLOR_YUV2BGRA_NV21,
            COLOR_YUV2RGB_YV12 = 98,
            COLOR_YUV2BGR_YV12 = 99,
            COLOR_YUV2RGB_IYUV = 100,
            COLOR_YUV2BGR_IYUV = 101,
            COLOR_YUV2RGB_I420 = COLOR_YUV2RGB_IYUV,
            COLOR_YUV2BGR_I420 = COLOR_YUV2BGR_IYUV,
            COLOR_YUV420p2RGB = COLOR_YUV2RGB_YV12,
            COLOR_YUV420p2BGR = COLOR_YUV2BGR_YV12,
            COLOR_YUV2RGBA_YV12 = 102,
            COLOR_YUV2BGRA_YV12 = 103,
            COLOR_YUV2RGBA_IYUV = 104,
            COLOR_YUV2BGRA_IYUV = 105,
            COLOR_YUV2RGBA_I420 = COLOR_YUV2RGBA_IYUV,
            COLOR_YUV2BGRA_I420 = COLOR_YUV2BGRA_IYUV,
            COLOR_YUV420p2RGBA = COLOR_YUV2RGBA_YV12,
            COLOR_YUV420p2BGRA = COLOR_YUV2BGRA_YV12,
            COLOR_YUV2GRAY_420 = 106,
            COLOR_YUV2GRAY_NV21 = COLOR_YUV2GRAY_420,
            COLOR_YUV2GRAY_NV12 = COLOR_YUV2GRAY_420,
            COLOR_YUV2GRAY_YV12 = COLOR_YUV2GRAY_420,
            COLOR_YUV2GRAY_IYUV = COLOR_YUV2GRAY_420,
            COLOR_YUV2GRAY_I420 = COLOR_YUV2GRAY_420,
            COLOR_YUV420sp2GRAY = COLOR_YUV2GRAY_420,
            COLOR_YUV420p2GRAY = COLOR_YUV2GRAY_420,
            COLOR_YUV2RGB_UYVY = 107,
            COLOR_YUV2BGR_UYVY = 108,
            COLOR_YUV2RGB_Y422 = COLOR_YUV2RGB_UYVY,
            COLOR_YUV2BGR_Y422 = COLOR_YUV2BGR_UYVY,
            COLOR_YUV2RGB_UYNV = COLOR_YUV2RGB_UYVY,
            COLOR_YUV2BGR_UYNV = COLOR_YUV2BGR_UYVY,
            COLOR_YUV2RGBA_UYVY = 111,
            COLOR_YUV2BGRA_UYVY = 112,
            COLOR_YUV2RGBA_Y422 = COLOR_YUV2RGBA_UYVY,
            COLOR_YUV2BGRA_Y422 = COLOR_YUV2BGRA_UYVY,
            COLOR_YUV2RGBA_UYNV = COLOR_YUV2RGBA_UYVY,
            COLOR_YUV2BGRA_UYNV = COLOR_YUV2BGRA_UYVY,
            COLOR_YUV2RGB_YUY2 = 115,
            COLOR_YUV2BGR_YUY2 = 116,
            COLOR_YUV2RGB_YVYU = 117,
            COLOR_YUV2BGR_YVYU = 118,
            COLOR_YUV2RGB_YUYV = COLOR_YUV2RGB_YUY2,
            COLOR_YUV2BGR_YUYV = COLOR_YUV2BGR_YUY2,
            COLOR_YUV2RGB_YUNV = COLOR_YUV2RGB_YUY2,
            COLOR_YUV2BGR_YUNV = COLOR_YUV2BGR_YUY2,
            COLOR_YUV2RGBA_YUY2 = 119,
            COLOR_YUV2BGRA_YUY2 = 120,
            COLOR_YUV2RGBA_YVYU = 121,
            COLOR_YUV2BGRA_YVYU = 122,
            COLOR_YUV2RGBA_YUYV = COLOR_YUV2RGBA_YUY2,
            COLOR_YUV2BGRA_YUYV = COLOR_YUV2BGRA_YUY2,
            COLOR_YUV2RGBA_YUNV = COLOR_YUV2RGBA_YUY2,
            COLOR_YUV2BGRA_YUNV = COLOR_YUV2BGRA_YUY2,
            COLOR_YUV2GRAY_UYVY = 123,
            COLOR_YUV2GRAY_YUY2 = 124,
            COLOR_YUV2GRAY_Y422 = COLOR_YUV2GRAY_UYVY,
            COLOR_YUV2GRAY_UYNV = COLOR_YUV2GRAY_UYVY,
            COLOR_YUV2GRAY_YVYU = COLOR_YUV2GRAY_YUY2,
            COLOR_YUV2GRAY_YUYV = COLOR_YUV2GRAY_YUY2,
            COLOR_YUV2GRAY_YUNV = COLOR_YUV2GRAY_YUY2,
            COLOR_RGBA2mRGBA = 125,
            COLOR_mRGBA2RGBA = 126,
            COLOR_RGB2YUV_I420 = 127,
            COLOR_BGR2YUV_I420 = 128,
            COLOR_RGB2YUV_IYUV = COLOR_RGB2YUV_I420,
            COLOR_BGR2YUV_IYUV = COLOR_BGR2YUV_I420,
            COLOR_RGBA2YUV_I420 = 129,
            COLOR_BGRA2YUV_I420 = 130,
            COLOR_RGBA2YUV_IYUV = COLOR_RGBA2YUV_I420,
            COLOR_BGRA2YUV_IYUV = COLOR_BGRA2YUV_I420,
            COLOR_RGB2YUV_YV12 = 131,
            COLOR_BGR2YUV_YV12 = 132,
            COLOR_RGBA2YUV_YV12 = 133,
            COLOR_BGRA2YUV_YV12 = 134,
            COLOR_BayerBG2BGR = 46,
            COLOR_BayerGB2BGR = 47,
            COLOR_BayerRG2BGR = 48,
            COLOR_BayerGR2BGR = 49,
            COLOR_BayerBG2RGB = COLOR_BayerRG2BGR,
            COLOR_BayerGB2RGB = COLOR_BayerGR2BGR,
            COLOR_BayerRG2RGB = COLOR_BayerBG2BGR,
            COLOR_BayerGR2RGB = COLOR_BayerGB2BGR,
            COLOR_BayerBG2GRAY = 86,
            COLOR_BayerGB2GRAY = 87,
            COLOR_BayerRG2GRAY = 88,
            COLOR_BayerGR2GRAY = 89,
            COLOR_BayerBG2BGR_VNG = 62,
            COLOR_BayerGB2BGR_VNG = 63,
            COLOR_BayerRG2BGR_VNG = 64,
            COLOR_BayerGR2BGR_VNG = 65,
            COLOR_BayerBG2RGB_VNG = COLOR_BayerRG2BGR_VNG,
            COLOR_BayerGB2RGB_VNG = COLOR_BayerGR2BGR_VNG,
            COLOR_BayerRG2RGB_VNG = COLOR_BayerBG2BGR_VNG,
            COLOR_BayerGR2RGB_VNG = COLOR_BayerGB2BGR_VNG,
            COLOR_BayerBG2BGR_EA = 135,
            COLOR_BayerGB2BGR_EA = 136,
            COLOR_BayerRG2BGR_EA = 137,
            COLOR_BayerGR2BGR_EA = 138,
            COLOR_BayerBG2RGB_EA = COLOR_BayerRG2BGR_EA,
            COLOR_BayerGB2RGB_EA = COLOR_BayerGR2BGR_EA,
            COLOR_BayerRG2RGB_EA = COLOR_BayerBG2BGR_EA,
            COLOR_BayerGR2RGB_EA = COLOR_BayerGB2BGR_EA,
            COLOR_BayerBG2BGRA = 139,
            COLOR_BayerGB2BGRA = 140,
            COLOR_BayerRG2BGRA = 141,
            COLOR_BayerGR2BGRA = 142,
            COLOR_BayerBG2RGBA = COLOR_BayerRG2BGRA,
            COLOR_BayerGB2RGBA = COLOR_BayerGR2BGRA,
            COLOR_BayerRG2RGBA = COLOR_BayerBG2BGRA,
            COLOR_BayerGR2RGBA = COLOR_BayerGB2BGRA,
            COLOR_COLORCVT_MAX = 143;


    // C++: enum LineSegmentDetectorModes
    public static final int
            LSD_REFINE_NONE = 0,
            LSD_REFINE_STD = 1,
            LSD_REFINE_ADV = 2;


    // C++: enum ThresholdTypes
    public static final int
            THRESH_BINARY = 0,
            THRESH_BINARY_INV = 1,
            THRESH_TRUNC = 2,
            THRESH_TOZERO = 3,
            THRESH_TOZERO_INV = 4,
            THRESH_MASK = 7,
            THRESH_OTSU = 8,
            THRESH_TRIANGLE = 16;


    // C++: enum AdaptiveThresholdTypes
    public static final int
            ADAPTIVE_THRESH_MEAN_C = 0,
            ADAPTIVE_THRESH_GAUSSIAN_C = 1;


    // C++: enum MorphShapes_c
    public static final int
            CV_SHAPE_RECT = 0,
            CV_SHAPE_CROSS = 1,
            CV_SHAPE_ELLIPSE = 2,
            CV_SHAPE_CUSTOM = 100;


    // C++: enum GrabCutModes
    public static final int
            GC_INIT_WITH_RECT = 0,
            GC_INIT_WITH_MASK = 1,
            GC_EVAL = 2,
            GC_EVAL_FREEZE_MODEL = 3;


    // C++: enum MorphShapes
    public static final int
            MORPH_RECT = 0,
            MORPH_CROSS = 1,
            MORPH_ELLIPSE = 2;


    // C++: enum DistanceTransformLabelTypes
    public static final int
            DIST_LABEL_CCOMP = 0,
            DIST_LABEL_PIXEL = 1;


    // C++: enum DistanceTypes
    public static final int
            DIST_USER = -1,
            DIST_L1 = 1,
            DIST_L2 = 2,
            DIST_C = 3,
            DIST_L12 = 4,
            DIST_FAIR = 5,
            DIST_WELSCH = 6,
            DIST_HUBER = 7;


    // C++: enum TemplateMatchModes
    public static final int
            TM_SQDIFF = 0,
            TM_SQDIFF_NORMED = 1,
            TM_CCORR = 2,
            TM_CCORR_NORMED = 3,
            TM_CCOEFF = 4,
            TM_CCOEFF_NORMED = 5;


    // C++: enum DistanceTransformMasks
    public static final int
            DIST_MASK_3 = 3,
            DIST_MASK_5 = 5,
            DIST_MASK_PRECISE = 0;


    // C++: enum ConnectedComponentsTypes
    public static final int
            CC_STAT_LEFT = 0,
            CC_STAT_TOP = 1,
            CC_STAT_WIDTH = 2,
            CC_STAT_HEIGHT = 3,
            CC_STAT_AREA = 4,
            CC_STAT_MAX = 5;


    // C++: enum SmoothMethod_c
    public static final int
            CV_BLUR_NO_SCALE = 0,
            CV_BLUR = 1,
            CV_GAUSSIAN = 2,
            CV_MEDIAN = 3,
            CV_BILATERAL = 4;


    // C++: enum MarkerTypes
    public static final int
            MARKER_CROSS = 0,
            MARKER_TILTED_CROSS = 1,
            MARKER_STAR = 2,
            MARKER_DIAMOND = 3,
            MARKER_SQUARE = 4,
            MARKER_TRIANGLE_UP = 5,
            MARKER_TRIANGLE_DOWN = 6;


    //
    // C++:  Mat cv::getAffineTransform(vector_Point2f src, vector_Point2f dst)
    //

    //javadoc: getAffineTransform(src, dst)
    public static Mat getAffineTransform(MatOfPoint2f src, MatOfPoint2f dst)
    {
        Mat src_mat = src;
        Mat dst_mat = dst;
        Mat retVal = new Mat(getAffineTransform_0(src_mat.nativeObj, dst_mat.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::getGaborKernel(Size ksize, double sigma, double theta, double lambd, double gamma, double psi = CV_PI*0.5, int ktype = CV_64F)
    //

    //javadoc: getGaborKernel(ksize, sigma, theta, lambd, gamma, psi, ktype)
    public static Mat getGaborKernel(Size ksize, double sigma, double theta, double lambd, double gamma, double psi, int ktype)
    {
        
        Mat retVal = new Mat(getGaborKernel_0(ksize.width, ksize.height, sigma, theta, lambd, gamma, psi, ktype));
        
        return retVal;
    }

    //javadoc: getGaborKernel(ksize, sigma, theta, lambd, gamma, psi)
    public static Mat getGaborKernel(Size ksize, double sigma, double theta, double lambd, double gamma, double psi)
    {
        
        Mat retVal = new Mat(getGaborKernel_1(ksize.width, ksize.height, sigma, theta, lambd, gamma, psi));
        
        return retVal;
    }

    //javadoc: getGaborKernel(ksize, sigma, theta, lambd, gamma)
    public static Mat getGaborKernel(Size ksize, double sigma, double theta, double lambd, double gamma)
    {
        
        Mat retVal = new Mat(getGaborKernel_2(ksize.width, ksize.height, sigma, theta, lambd, gamma));
        
        return retVal;
    }


    //
    // C++:  Mat cv::getGaussianKernel(int ksize, double sigma, int ktype = CV_64F)
    //

    //javadoc: getGaussianKernel(ksize, sigma, ktype)
    public static Mat getGaussianKernel(int ksize, double sigma, int ktype)
    {
        
        Mat retVal = new Mat(getGaussianKernel_0(ksize, sigma, ktype));
        
        return retVal;
    }

    //javadoc: getGaussianKernel(ksize, sigma)
    public static Mat getGaussianKernel(int ksize, double sigma)
    {
        
        Mat retVal = new Mat(getGaussianKernel_1(ksize, sigma));
        
        return retVal;
    }


    //
    // C++:  Mat cv::getPerspectiveTransform(Mat src, Mat dst, int solveMethod = DECOMP_LU)
    //

    //javadoc: getPerspectiveTransform(src, dst, solveMethod)
    public static Mat getPerspectiveTransform(Mat src, Mat dst, int solveMethod)
    {
        
        Mat retVal = new Mat(getPerspectiveTransform_0(src.nativeObj, dst.nativeObj, solveMethod));
        
        return retVal;
    }

    //javadoc: getPerspectiveTransform(src, dst)
    public static Mat getPerspectiveTransform(Mat src, Mat dst)
    {
        
        Mat retVal = new Mat(getPerspectiveTransform_1(src.nativeObj, dst.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::getRotationMatrix2D(Point2f center, double angle, double scale)
    //

    //javadoc: getRotationMatrix2D(center, angle, scale)
    public static Mat getRotationMatrix2D(Point center, double angle, double scale)
    {
        
        Mat retVal = new Mat(getRotationMatrix2D_0(center.x, center.y, angle, scale));
        
        return retVal;
    }


    //
    // C++:  Mat cv::getStructuringElement(int shape, Size ksize, Point anchor = Point(-1,-1))
    //

    //javadoc: getStructuringElement(shape, ksize, anchor)
    public static Mat getStructuringElement(int shape, Size ksize, Point anchor)
    {
        
        Mat retVal = new Mat(getStructuringElement_0(shape, ksize.width, ksize.height, anchor.x, anchor.y));
        
        return retVal;
    }

    //javadoc: getStructuringElement(shape, ksize)
    public static Mat getStructuringElement(int shape, Size ksize)
    {
        
        Mat retVal = new Mat(getStructuringElement_1(shape, ksize.width, ksize.height));
        
        return retVal;
    }


    //
    // C++:  Moments cv::moments(Mat array, bool binaryImage = false)
    //

    //javadoc: moments(array, binaryImage)
    public static Moments moments(Mat array, boolean binaryImage)
    {
        
        Moments retVal = new Moments(moments_0(array.nativeObj, binaryImage));
        
        return retVal;
    }

    //javadoc: moments(array)
    public static Moments moments(Mat array)
    {
        
        Moments retVal = new Moments(moments_1(array.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Point2d cv::phaseCorrelate(Mat src1, Mat src2, Mat window = Mat(), double* response = 0)
    //

    //javadoc: phaseCorrelate(src1, src2, window, response)
    public static Point phaseCorrelate(Mat src1, Mat src2, Mat window, double[] response)
    {
        double[] response_out = new double[1];
        Point retVal = new Point(phaseCorrelate_0(src1.nativeObj, src2.nativeObj, window.nativeObj, response_out));
        if(response!=null) response[0] = (double)response_out[0];
        return retVal;
    }

    //javadoc: phaseCorrelate(src1, src2, window)
    public static Point phaseCorrelate(Mat src1, Mat src2, Mat window)
    {
        
        Point retVal = new Point(phaseCorrelate_1(src1.nativeObj, src2.nativeObj, window.nativeObj));
        
        return retVal;
    }

    //javadoc: phaseCorrelate(src1, src2)
    public static Point phaseCorrelate(Mat src1, Mat src2)
    {
        
        Point retVal = new Point(phaseCorrelate_2(src1.nativeObj, src2.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Ptr_CLAHE cv::createCLAHE(double clipLimit = 40.0, Size tileGridSize = Size(8, 8))
    //

    //javadoc: createCLAHE(clipLimit, tileGridSize)
    public static CLAHE createCLAHE(double clipLimit, Size tileGridSize)
    {
        
        CLAHE retVal = CLAHE.__fromPtr__(createCLAHE_0(clipLimit, tileGridSize.width, tileGridSize.height));
        
        return retVal;
    }

    //javadoc: createCLAHE(clipLimit)
    public static CLAHE createCLAHE(double clipLimit)
    {
        
        CLAHE retVal = CLAHE.__fromPtr__(createCLAHE_1(clipLimit));
        
        return retVal;
    }

    //javadoc: createCLAHE()
    public static CLAHE createCLAHE()
    {
        
        CLAHE retVal = CLAHE.__fromPtr__(createCLAHE_2());
        
        return retVal;
    }


    //
    // C++:  Ptr_LineSegmentDetector cv::createLineSegmentDetector(int _refine = LSD_REFINE_STD, double _scale = 0.8, double _sigma_scale = 0.6, double _quant = 2.0, double _ang_th = 22.5, double _log_eps = 0, double _density_th = 0.7, int _n_bins = 1024)
    //

    //javadoc: createLineSegmentDetector(_refine, _scale, _sigma_scale, _quant, _ang_th, _log_eps, _density_th, _n_bins)
    public static LineSegmentDetector createLineSegmentDetector(int _refine, double _scale, double _sigma_scale, double _quant, double _ang_th, double _log_eps, double _density_th, int _n_bins)
    {
        
        LineSegmentDetector retVal = LineSegmentDetector.__fromPtr__(createLineSegmentDetector_0(_refine, _scale, _sigma_scale, _quant, _ang_th, _log_eps, _density_th, _n_bins));
        
        return retVal;
    }

    //javadoc: createLineSegmentDetector(_refine, _scale, _sigma_scale, _quant, _ang_th, _log_eps, _density_th)
    public static LineSegmentDetector createLineSegmentDetector(int _refine, double _scale, double _sigma_scale, double _quant, double _ang_th, double _log_eps, double _density_th)
    {
        
        LineSegmentDetector retVal = LineSegmentDetector.__fromPtr__(createLineSegmentDetector_1(_refine, _scale, _sigma_scale, _quant, _ang_th, _log_eps, _density_th));
        
        return retVal;
    }

    //javadoc: createLineSegmentDetector(_refine, _scale, _sigma_scale, _quant, _ang_th, _log_eps)
    public static LineSegmentDetector createLineSegmentDetector(int _refine, double _scale, double _sigma_scale, double _quant, double _ang_th, double _log_eps)
    {
        
        LineSegmentDetector retVal = LineSegmentDetector.__fromPtr__(createLineSegmentDetector_2(_refine, _scale, _sigma_scale, _quant, _ang_th, _log_eps));
        
        return retVal;
    }

    //javadoc: createLineSegmentDetector(_refine, _scale, _sigma_scale, _quant, _ang_th)
    public static LineSegmentDetector createLineSegmentDetector(int _refine, double _scale, double _sigma_scale, double _quant, double _ang_th)
    {
        
        LineSegmentDetector retVal = LineSegmentDetector.__fromPtr__(createLineSegmentDetector_3(_refine, _scale, _sigma_scale, _quant, _ang_th));
        
        return retVal;
    }

    //javadoc: createLineSegmentDetector(_refine, _scale, _sigma_scale, _quant)
    public static LineSegmentDetector createLineSegmentDetector(int _refine, double _scale, double _sigma_scale, double _quant)
    {
        
        LineSegmentDetector retVal = LineSegmentDetector.__fromPtr__(createLineSegmentDetector_4(_refine, _scale, _sigma_scale, _quant));
        
        return retVal;
    }

    //javadoc: createLineSegmentDetector(_refine, _scale, _sigma_scale)
    public static LineSegmentDetector createLineSegmentDetector(int _refine, double _scale, double _sigma_scale)
    {
        
        LineSegmentDetector retVal = LineSegmentDetector.__fromPtr__(createLineSegmentDetector_5(_refine, _scale, _sigma_scale));
        
        return retVal;
    }

    //javadoc: createLineSegmentDetector(_refine, _scale)
    public static LineSegmentDetector createLineSegmentDetector(int _refine, double _scale)
    {
        
        LineSegmentDetector retVal = LineSegmentDetector.__fromPtr__(createLineSegmentDetector_6(_refine, _scale));
        
        return retVal;
    }

    //javadoc: createLineSegmentDetector(_refine)
    public static LineSegmentDetector createLineSegmentDetector(int _refine)
    {
        
        LineSegmentDetector retVal = LineSegmentDetector.__fromPtr__(createLineSegmentDetector_7(_refine));
        
        return retVal;
    }

    //javadoc: createLineSegmentDetector()
    public static LineSegmentDetector createLineSegmentDetector()
    {
        
        LineSegmentDetector retVal = LineSegmentDetector.__fromPtr__(createLineSegmentDetector_8());
        
        return retVal;
    }


    //
    // C++:  Rect cv::boundingRect(Mat array)
    //

    //javadoc: boundingRect(array)
    public static Rect boundingRect(Mat array)
    {
        
        Rect retVal = new Rect(boundingRect_0(array.nativeObj));
        
        return retVal;
    }


    //
    // C++:  RotatedRect cv::fitEllipse(vector_Point2f points)
    //

    //javadoc: fitEllipse(points)
    public static RotatedRect fitEllipse(MatOfPoint2f points)
    {
        Mat points_mat = points;
        RotatedRect retVal = new RotatedRect(fitEllipse_0(points_mat.nativeObj));
        
        return retVal;
    }


    //
    // C++:  RotatedRect cv::fitEllipseAMS(Mat points)
    //

    //javadoc: fitEllipseAMS(points)
    public static RotatedRect fitEllipseAMS(Mat points)
    {
        
        RotatedRect retVal = new RotatedRect(fitEllipseAMS_0(points.nativeObj));
        
        return retVal;
    }


    //
    // C++:  RotatedRect cv::fitEllipseDirect(Mat points)
    //

    //javadoc: fitEllipseDirect(points)
    public static RotatedRect fitEllipseDirect(Mat points)
    {
        
        RotatedRect retVal = new RotatedRect(fitEllipseDirect_0(points.nativeObj));
        
        return retVal;
    }


    //
    // C++:  RotatedRect cv::minAreaRect(vector_Point2f points)
    //

    //javadoc: minAreaRect(points)
    public static RotatedRect minAreaRect(MatOfPoint2f points)
    {
        Mat points_mat = points;
        RotatedRect retVal = new RotatedRect(minAreaRect_0(points_mat.nativeObj));
        
        return retVal;
    }


    //
    // C++:  bool cv::clipLine(Rect imgRect, Point& pt1, Point& pt2)
    //

    //javadoc: clipLine(imgRect, pt1, pt2)
    public static boolean clipLine(Rect imgRect, Point pt1, Point pt2)
    {
        double[] pt1_out = new double[2];
        double[] pt2_out = new double[2];
        boolean retVal = clipLine_0(imgRect.x, imgRect.y, imgRect.width, imgRect.height, pt1.x, pt1.y, pt1_out, pt2.x, pt2.y, pt2_out);
        if(pt1!=null){ pt1.x = pt1_out[0]; pt1.y = pt1_out[1]; } 
        if(pt2!=null){ pt2.x = pt2_out[0]; pt2.y = pt2_out[1]; } 
        return retVal;
    }


    //
    // C++:  bool cv::isContourConvex(vector_Point contour)
    //

    //javadoc: isContourConvex(contour)
    public static boolean isContourConvex(MatOfPoint contour)
    {
        Mat contour_mat = contour;
        boolean retVal = isContourConvex_0(contour_mat.nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::arcLength(vector_Point2f curve, bool closed)
    //

    //javadoc: arcLength(curve, closed)
    public static double arcLength(MatOfPoint2f curve, boolean closed)
    {
        Mat curve_mat = curve;
        double retVal = arcLength_0(curve_mat.nativeObj, closed);
        
        return retVal;
    }


    //
    // C++:  double cv::compareHist(Mat H1, Mat H2, int method)
    //

    //javadoc: compareHist(H1, H2, method)
    public static double compareHist(Mat H1, Mat H2, int method)
    {
        
        double retVal = compareHist_0(H1.nativeObj, H2.nativeObj, method);
        
        return retVal;
    }


    //
    // C++:  double cv::contourArea(Mat contour, bool oriented = false)
    //

    //javadoc: contourArea(contour, oriented)
    public static double contourArea(Mat contour, boolean oriented)
    {
        
        double retVal = contourArea_0(contour.nativeObj, oriented);
        
        return retVal;
    }

    //javadoc: contourArea(contour)
    public static double contourArea(Mat contour)
    {
        
        double retVal = contourArea_1(contour.nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::getFontScaleFromHeight(int fontFace, int pixelHeight, int thickness = 1)
    //

    //javadoc: getFontScaleFromHeight(fontFace, pixelHeight, thickness)
    public static double getFontScaleFromHeight(int fontFace, int pixelHeight, int thickness)
    {
        
        double retVal = getFontScaleFromHeight_0(fontFace, pixelHeight, thickness);
        
        return retVal;
    }

    //javadoc: getFontScaleFromHeight(fontFace, pixelHeight)
    public static double getFontScaleFromHeight(int fontFace, int pixelHeight)
    {
        
        double retVal = getFontScaleFromHeight_1(fontFace, pixelHeight);
        
        return retVal;
    }


    //
    // C++:  double cv::matchShapes(Mat contour1, Mat contour2, int method, double parameter)
    //

    //javadoc: matchShapes(contour1, contour2, method, parameter)
    public static double matchShapes(Mat contour1, Mat contour2, int method, double parameter)
    {
        
        double retVal = matchShapes_0(contour1.nativeObj, contour2.nativeObj, method, parameter);
        
        return retVal;
    }


    //
    // C++:  double cv::minEnclosingTriangle(Mat points, Mat& triangle)
    //

    //javadoc: minEnclosingTriangle(points, triangle)
    public static double minEnclosingTriangle(Mat points, Mat triangle)
    {
        
        double retVal = minEnclosingTriangle_0(points.nativeObj, triangle.nativeObj);
        
        return retVal;
    }


    //
    // C++:  double cv::pointPolygonTest(vector_Point2f contour, Point2f pt, bool measureDist)
    //

    //javadoc: pointPolygonTest(contour, pt, measureDist)
    public static double pointPolygonTest(MatOfPoint2f contour, Point pt, boolean measureDist)
    {
        Mat contour_mat = contour;
        double retVal = pointPolygonTest_0(contour_mat.nativeObj, pt.x, pt.y, measureDist);
        
        return retVal;
    }


    //
    // C++:  double cv::threshold(Mat src, Mat& dst, double thresh, double maxval, int type)
    //

    //javadoc: threshold(src, dst, thresh, maxval, type)
    public static double threshold(Mat src, Mat dst, double thresh, double maxval, int type)
    {
        
        double retVal = threshold_0(src.nativeObj, dst.nativeObj, thresh, maxval, type);
        
        return retVal;
    }


    //
    // C++:  float cv::intersectConvexConvex(Mat _p1, Mat _p2, Mat& _p12, bool handleNested = true)
    //

    //javadoc: intersectConvexConvex(_p1, _p2, _p12, handleNested)
    public static float intersectConvexConvex(Mat _p1, Mat _p2, Mat _p12, boolean handleNested)
    {
        
        float retVal = intersectConvexConvex_0(_p1.nativeObj, _p2.nativeObj, _p12.nativeObj, handleNested);
        
        return retVal;
    }

    //javadoc: intersectConvexConvex(_p1, _p2, _p12)
    public static float intersectConvexConvex(Mat _p1, Mat _p2, Mat _p12)
    {
        
        float retVal = intersectConvexConvex_1(_p1.nativeObj, _p2.nativeObj, _p12.nativeObj);
        
        return retVal;
    }


    //
    // C++:  float cv::wrapperEMD(Mat signature1, Mat signature2, int distType, Mat cost = Mat(), Ptr_float& lowerBound = Ptr<float>(), Mat& flow = Mat())
    //

    //javadoc: EMD(signature1, signature2, distType, cost, flow)
    public static float EMD(Mat signature1, Mat signature2, int distType, Mat cost, Mat flow)
    {
        
        float retVal = EMD_0(signature1.nativeObj, signature2.nativeObj, distType, cost.nativeObj, flow.nativeObj);
        
        return retVal;
    }

    //javadoc: EMD(signature1, signature2, distType, cost)
    public static float EMD(Mat signature1, Mat signature2, int distType, Mat cost)
    {
        
        float retVal = EMD_1(signature1.nativeObj, signature2.nativeObj, distType, cost.nativeObj);
        
        return retVal;
    }

    //javadoc: EMD(signature1, signature2, distType)
    public static float EMD(Mat signature1, Mat signature2, int distType)
    {
        
        float retVal = EMD_3(signature1.nativeObj, signature2.nativeObj, distType);
        
        return retVal;
    }


    //
    // C++:  int cv::connectedComponents(Mat image, Mat& labels, int connectivity, int ltype, int ccltype)
    //

    //javadoc: connectedComponentsWithAlgorithm(image, labels, connectivity, ltype, ccltype)
    public static int connectedComponentsWithAlgorithm(Mat image, Mat labels, int connectivity, int ltype, int ccltype)
    {
        
        int retVal = connectedComponentsWithAlgorithm_0(image.nativeObj, labels.nativeObj, connectivity, ltype, ccltype);
        
        return retVal;
    }


    //
    // C++:  int cv::connectedComponents(Mat image, Mat& labels, int connectivity = 8, int ltype = CV_32S)
    //

    //javadoc: connectedComponents(image, labels, connectivity, ltype)
    public static int connectedComponents(Mat image, Mat labels, int connectivity, int ltype)
    {
        
        int retVal = connectedComponents_0(image.nativeObj, labels.nativeObj, connectivity, ltype);
        
        return retVal;
    }

    //javadoc: connectedComponents(image, labels, connectivity)
    public static int connectedComponents(Mat image, Mat labels, int connectivity)
    {
        
        int retVal = connectedComponents_1(image.nativeObj, labels.nativeObj, connectivity);
        
        return retVal;
    }

    //javadoc: connectedComponents(image, labels)
    public static int connectedComponents(Mat image, Mat labels)
    {
        
        int retVal = connectedComponents_2(image.nativeObj, labels.nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::connectedComponentsWithStats(Mat image, Mat& labels, Mat& stats, Mat& centroids, int connectivity, int ltype, int ccltype)
    //

    //javadoc: connectedComponentsWithStatsWithAlgorithm(image, labels, stats, centroids, connectivity, ltype, ccltype)
    public static int connectedComponentsWithStatsWithAlgorithm(Mat image, Mat labels, Mat stats, Mat centroids, int connectivity, int ltype, int ccltype)
    {
        
        int retVal = connectedComponentsWithStatsWithAlgorithm_0(image.nativeObj, labels.nativeObj, stats.nativeObj, centroids.nativeObj, connectivity, ltype, ccltype);
        
        return retVal;
    }


    //
    // C++:  int cv::connectedComponentsWithStats(Mat image, Mat& labels, Mat& stats, Mat& centroids, int connectivity = 8, int ltype = CV_32S)
    //

    //javadoc: connectedComponentsWithStats(image, labels, stats, centroids, connectivity, ltype)
    public static int connectedComponentsWithStats(Mat image, Mat labels, Mat stats, Mat centroids, int connectivity, int ltype)
    {
        
        int retVal = connectedComponentsWithStats_0(image.nativeObj, labels.nativeObj, stats.nativeObj, centroids.nativeObj, connectivity, ltype);
        
        return retVal;
    }

    //javadoc: connectedComponentsWithStats(image, labels, stats, centroids, connectivity)
    public static int connectedComponentsWithStats(Mat image, Mat labels, Mat stats, Mat centroids, int connectivity)
    {
        
        int retVal = connectedComponentsWithStats_1(image.nativeObj, labels.nativeObj, stats.nativeObj, centroids.nativeObj, connectivity);
        
        return retVal;
    }

    //javadoc: connectedComponentsWithStats(image, labels, stats, centroids)
    public static int connectedComponentsWithStats(Mat image, Mat labels, Mat stats, Mat centroids)
    {
        
        int retVal = connectedComponentsWithStats_2(image.nativeObj, labels.nativeObj, stats.nativeObj, centroids.nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::floodFill(Mat& image, Mat& mask, Point seedPoint, Scalar newVal, Rect* rect = 0, Scalar loDiff = Scalar(), Scalar upDiff = Scalar(), int flags = 4)
    //

    //javadoc: floodFill(image, mask, seedPoint, newVal, rect, loDiff, upDiff, flags)
    public static int floodFill(Mat image, Mat mask, Point seedPoint, Scalar newVal, Rect rect, Scalar loDiff, Scalar upDiff, int flags)
    {
        double[] rect_out = new double[4];
        int retVal = floodFill_0(image.nativeObj, mask.nativeObj, seedPoint.x, seedPoint.y, newVal.val[0], newVal.val[1], newVal.val[2], newVal.val[3], rect_out, loDiff.val[0], loDiff.val[1], loDiff.val[2], loDiff.val[3], upDiff.val[0], upDiff.val[1], upDiff.val[2], upDiff.val[3], flags);
        if(rect!=null){ rect.x = (int)rect_out[0]; rect.y = (int)rect_out[1]; rect.width = (int)rect_out[2]; rect.height = (int)rect_out[3]; } 
        return retVal;
    }

    //javadoc: floodFill(image, mask, seedPoint, newVal, rect, loDiff, upDiff)
    public static int floodFill(Mat image, Mat mask, Point seedPoint, Scalar newVal, Rect rect, Scalar loDiff, Scalar upDiff)
    {
        double[] rect_out = new double[4];
        int retVal = floodFill_1(image.nativeObj, mask.nativeObj, seedPoint.x, seedPoint.y, newVal.val[0], newVal.val[1], newVal.val[2], newVal.val[3], rect_out, loDiff.val[0], loDiff.val[1], loDiff.val[2], loDiff.val[3], upDiff.val[0], upDiff.val[1], upDiff.val[2], upDiff.val[3]);
        if(rect!=null){ rect.x = (int)rect_out[0]; rect.y = (int)rect_out[1]; rect.width = (int)rect_out[2]; rect.height = (int)rect_out[3]; } 
        return retVal;
    }

    //javadoc: floodFill(image, mask, seedPoint, newVal, rect, loDiff)
    public static int floodFill(Mat image, Mat mask, Point seedPoint, Scalar newVal, Rect rect, Scalar loDiff)
    {
        double[] rect_out = new double[4];
        int retVal = floodFill_2(image.nativeObj, mask.nativeObj, seedPoint.x, seedPoint.y, newVal.val[0], newVal.val[1], newVal.val[2], newVal.val[3], rect_out, loDiff.val[0], loDiff.val[1], loDiff.val[2], loDiff.val[3]);
        if(rect!=null){ rect.x = (int)rect_out[0]; rect.y = (int)rect_out[1]; rect.width = (int)rect_out[2]; rect.height = (int)rect_out[3]; } 
        return retVal;
    }

    //javadoc: floodFill(image, mask, seedPoint, newVal, rect)
    public static int floodFill(Mat image, Mat mask, Point seedPoint, Scalar newVal, Rect rect)
    {
        double[] rect_out = new double[4];
        int retVal = floodFill_3(image.nativeObj, mask.nativeObj, seedPoint.x, seedPoint.y, newVal.val[0], newVal.val[1], newVal.val[2], newVal.val[3], rect_out);
        if(rect!=null){ rect.x = (int)rect_out[0]; rect.y = (int)rect_out[1]; rect.width = (int)rect_out[2]; rect.height = (int)rect_out[3]; } 
        return retVal;
    }

    //javadoc: floodFill(image, mask, seedPoint, newVal)
    public static int floodFill(Mat image, Mat mask, Point seedPoint, Scalar newVal)
    {
        
        int retVal = floodFill_4(image.nativeObj, mask.nativeObj, seedPoint.x, seedPoint.y, newVal.val[0], newVal.val[1], newVal.val[2], newVal.val[3]);
        
        return retVal;
    }


    //
    // C++:  int cv::rotatedRectangleIntersection(RotatedRect rect1, RotatedRect rect2, Mat& intersectingRegion)
    //

    //javadoc: rotatedRectangleIntersection(rect1, rect2, intersectingRegion)
    public static int rotatedRectangleIntersection(RotatedRect rect1, RotatedRect rect2, Mat intersectingRegion)
    {
        
        int retVal = rotatedRectangleIntersection_0(rect1.center.x, rect1.center.y, rect1.size.width, rect1.size.height, rect1.angle, rect2.center.x, rect2.center.y, rect2.size.width, rect2.size.height, rect2.angle, intersectingRegion.nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::Canny(Mat dx, Mat dy, Mat& edges, double threshold1, double threshold2, bool L2gradient = false)
    //

    //javadoc: Canny(dx, dy, edges, threshold1, threshold2, L2gradient)
    public static void Canny(Mat dx, Mat dy, Mat edges, double threshold1, double threshold2, boolean L2gradient)
    {
        
        Canny_0(dx.nativeObj, dy.nativeObj, edges.nativeObj, threshold1, threshold2, L2gradient);
        
        return;
    }

    //javadoc: Canny(dx, dy, edges, threshold1, threshold2)
    public static void Canny(Mat dx, Mat dy, Mat edges, double threshold1, double threshold2)
    {
        
        Canny_1(dx.nativeObj, dy.nativeObj, edges.nativeObj, threshold1, threshold2);
        
        return;
    }


    //
    // C++:  void cv::Canny(Mat image, Mat& edges, double threshold1, double threshold2, int apertureSize = 3, bool L2gradient = false)
    //

    //javadoc: Canny(image, edges, threshold1, threshold2, apertureSize, L2gradient)
    public static void Canny(Mat image, Mat edges, double threshold1, double threshold2, int apertureSize, boolean L2gradient)
    {
        
        Canny_2(image.nativeObj, edges.nativeObj, threshold1, threshold2, apertureSize, L2gradient);
        
        return;
    }

    //javadoc: Canny(image, edges, threshold1, threshold2, apertureSize)
    public static void Canny(Mat image, Mat edges, double threshold1, double threshold2, int apertureSize)
    {
        
        Canny_3(image.nativeObj, edges.nativeObj, threshold1, threshold2, apertureSize);
        
        return;
    }

    //javadoc: Canny(image, edges, threshold1, threshold2)
    public static void Canny(Mat image, Mat edges, double threshold1, double threshold2)
    {
        
        Canny_4(image.nativeObj, edges.nativeObj, threshold1, threshold2);
        
        return;
    }


    //
    // C++:  void cv::GaussianBlur(Mat src, Mat& dst, Size ksize, double sigmaX, double sigmaY = 0, int borderType = BORDER_DEFAULT)
    //

    //javadoc: GaussianBlur(src, dst, ksize, sigmaX, sigmaY, borderType)
    public static void GaussianBlur(Mat src, Mat dst, Size ksize, double sigmaX, double sigmaY, int borderType)
    {
        
        GaussianBlur_0(src.nativeObj, dst.nativeObj, ksize.width, ksize.height, sigmaX, sigmaY, borderType);
        
        return;
    }

    //javadoc: GaussianBlur(src, dst, ksize, sigmaX, sigmaY)
    public static void GaussianBlur(Mat src, Mat dst, Size ksize, double sigmaX, double sigmaY)
    {
        
        GaussianBlur_1(src.nativeObj, dst.nativeObj, ksize.width, ksize.height, sigmaX, sigmaY);
        
        return;
    }

    //javadoc: GaussianBlur(src, dst, ksize, sigmaX)
    public static void GaussianBlur(Mat src, Mat dst, Size ksize, double sigmaX)
    {
        
        GaussianBlur_2(src.nativeObj, dst.nativeObj, ksize.width, ksize.height, sigmaX);
        
        return;
    }


    //
    // C++:  void cv::HoughCircles(Mat image, Mat& circles, int method, double dp, double minDist, double param1 = 100, double param2 = 100, int minRadius = 0, int maxRadius = 0)
    //

    //javadoc: HoughCircles(image, circles, method, dp, minDist, param1, param2, minRadius, maxRadius)
    public static void HoughCircles(Mat image, Mat circles, int method, double dp, double minDist, double param1, double param2, int minRadius, int maxRadius)
    {
        
        HoughCircles_0(image.nativeObj, circles.nativeObj, method, dp, minDist, param1, param2, minRadius, maxRadius);
        
        return;
    }

    //javadoc: HoughCircles(image, circles, method, dp, minDist, param1, param2, minRadius)
    public static void HoughCircles(Mat image, Mat circles, int method, double dp, double minDist, double param1, double param2, int minRadius)
    {
        
        HoughCircles_1(image.nativeObj, circles.nativeObj, method, dp, minDist, param1, param2, minRadius);
        
        return;
    }

    //javadoc: HoughCircles(image, circles, method, dp, minDist, param1, param2)
    public static void HoughCircles(Mat image, Mat circles, int method, double dp, double minDist, double param1, double param2)
    {
        
        HoughCircles_2(image.nativeObj, circles.nativeObj, method, dp, minDist, param1, param2);
        
        return;
    }

    //javadoc: HoughCircles(image, circles, method, dp, minDist, param1)
    public static void HoughCircles(Mat image, Mat circles, int method, double dp, double minDist, double param1)
    {
        
        HoughCircles_3(image.nativeObj, circles.nativeObj, method, dp, minDist, param1);
        
        return;
    }

    //javadoc: HoughCircles(image, circles, method, dp, minDist)
    public static void HoughCircles(Mat image, Mat circles, int method, double dp, double minDist)
    {
        
        HoughCircles_4(image.nativeObj, circles.nativeObj, method, dp, minDist);
        
        return;
    }


    //
    // C++:  void cv::HoughLines(Mat image, Mat& lines, double rho, double theta, int threshold, double srn = 0, double stn = 0, double min_theta = 0, double max_theta = CV_PI)
    //

    //javadoc: HoughLines(image, lines, rho, theta, threshold, srn, stn, min_theta, max_theta)
    public static void HoughLines(Mat image, Mat lines, double rho, double theta, int threshold, double srn, double stn, double min_theta, double max_theta)
    {
        
        HoughLines_0(image.nativeObj, lines.nativeObj, rho, theta, threshold, srn, stn, min_theta, max_theta);
        
        return;
    }

    //javadoc: HoughLines(image, lines, rho, theta, threshold, srn, stn, min_theta)
    public static void HoughLines(Mat image, Mat lines, double rho, double theta, int threshold, double srn, double stn, double min_theta)
    {
        
        HoughLines_1(image.nativeObj, lines.nativeObj, rho, theta, threshold, srn, stn, min_theta);
        
        return;
    }

    //javadoc: HoughLines(image, lines, rho, theta, threshold, srn, stn)
    public static void HoughLines(Mat image, Mat lines, double rho, double theta, int threshold, double srn, double stn)
    {
        
        HoughLines_2(image.nativeObj, lines.nativeObj, rho, theta, threshold, srn, stn);
        
        return;
    }

    //javadoc: HoughLines(image, lines, rho, theta, threshold, srn)
    public static void HoughLines(Mat image, Mat lines, double rho, double theta, int threshold, double srn)
    {
        
        HoughLines_3(image.nativeObj, lines.nativeObj, rho, theta, threshold, srn);
        
        return;
    }

    //javadoc: HoughLines(image, lines, rho, theta, threshold)
    public static void HoughLines(Mat image, Mat lines, double rho, double theta, int threshold)
    {
        
        HoughLines_4(image.nativeObj, lines.nativeObj, rho, theta, threshold);
        
        return;
    }


    //
    // C++:  void cv::HoughLinesP(Mat image, Mat& lines, double rho, double theta, int threshold, double minLineLength = 0, double maxLineGap = 0)
    //

    //javadoc: HoughLinesP(image, lines, rho, theta, threshold, minLineLength, maxLineGap)
    public static void HoughLinesP(Mat image, Mat lines, double rho, double theta, int threshold, double minLineLength, double maxLineGap)
    {
        
        HoughLinesP_0(image.nativeObj, lines.nativeObj, rho, theta, threshold, minLineLength, maxLineGap);
        
        return;
    }

    //javadoc: HoughLinesP(image, lines, rho, theta, threshold, minLineLength)
    public static void HoughLinesP(Mat image, Mat lines, double rho, double theta, int threshold, double minLineLength)
    {
        
        HoughLinesP_1(image.nativeObj, lines.nativeObj, rho, theta, threshold, minLineLength);
        
        return;
    }

    //javadoc: HoughLinesP(image, lines, rho, theta, threshold)
    public static void HoughLinesP(Mat image, Mat lines, double rho, double theta, int threshold)
    {
        
        HoughLinesP_2(image.nativeObj, lines.nativeObj, rho, theta, threshold);
        
        return;
    }


    //
    // C++:  void cv::HoughLinesPointSet(Mat _point, Mat& _lines, int lines_max, int threshold, double min_rho, double max_rho, double rho_step, double min_theta, double max_theta, double theta_step)
    //

    //javadoc: HoughLinesPointSet(_point, _lines, lines_max, threshold, min_rho, max_rho, rho_step, min_theta, max_theta, theta_step)
    public static void HoughLinesPointSet(Mat _point, Mat _lines, int lines_max, int threshold, double min_rho, double max_rho, double rho_step, double min_theta, double max_theta, double theta_step)
    {
        
        HoughLinesPointSet_0(_point.nativeObj, _lines.nativeObj, lines_max, threshold, min_rho, max_rho, rho_step, min_theta, max_theta, theta_step);
        
        return;
    }


    //
    // C++:  void cv::HuMoments(Moments m, Mat& hu)
    //

    //javadoc: HuMoments(m, hu)
    public static void HuMoments(Moments m, Mat hu)
    {
        
        HuMoments_0(m.m00, m.m10, m.m01, m.m20, m.m11, m.m02, m.m30, m.m21, m.m12, m.m03, hu.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::Laplacian(Mat src, Mat& dst, int ddepth, int ksize = 1, double scale = 1, double delta = 0, int borderType = BORDER_DEFAULT)
    //

    //javadoc: Laplacian(src, dst, ddepth, ksize, scale, delta, borderType)
    public static void Laplacian(Mat src, Mat dst, int ddepth, int ksize, double scale, double delta, int borderType)
    {
        
        Laplacian_0(src.nativeObj, dst.nativeObj, ddepth, ksize, scale, delta, borderType);
        
        return;
    }

    //javadoc: Laplacian(src, dst, ddepth, ksize, scale, delta)
    public static void Laplacian(Mat src, Mat dst, int ddepth, int ksize, double scale, double delta)
    {
        
        Laplacian_1(src.nativeObj, dst.nativeObj, ddepth, ksize, scale, delta);
        
        return;
    }

    //javadoc: Laplacian(src, dst, ddepth, ksize, scale)
    public static void Laplacian(Mat src, Mat dst, int ddepth, int ksize, double scale)
    {
        
        Laplacian_2(src.nativeObj, dst.nativeObj, ddepth, ksize, scale);
        
        return;
    }

    //javadoc: Laplacian(src, dst, ddepth, ksize)
    public static void Laplacian(Mat src, Mat dst, int ddepth, int ksize)
    {
        
        Laplacian_3(src.nativeObj, dst.nativeObj, ddepth, ksize);
        
        return;
    }

    //javadoc: Laplacian(src, dst, ddepth)
    public static void Laplacian(Mat src, Mat dst, int ddepth)
    {
        
        Laplacian_4(src.nativeObj, dst.nativeObj, ddepth);
        
        return;
    }


    //
    // C++:  void cv::Scharr(Mat src, Mat& dst, int ddepth, int dx, int dy, double scale = 1, double delta = 0, int borderType = BORDER_DEFAULT)
    //

    //javadoc: Scharr(src, dst, ddepth, dx, dy, scale, delta, borderType)
    public static void Scharr(Mat src, Mat dst, int ddepth, int dx, int dy, double scale, double delta, int borderType)
    {
        
        Scharr_0(src.nativeObj, dst.nativeObj, ddepth, dx, dy, scale, delta, borderType);
        
        return;
    }

    //javadoc: Scharr(src, dst, ddepth, dx, dy, scale, delta)
    public static void Scharr(Mat src, Mat dst, int ddepth, int dx, int dy, double scale, double delta)
    {
        
        Scharr_1(src.nativeObj, dst.nativeObj, ddepth, dx, dy, scale, delta);
        
        return;
    }

    //javadoc: Scharr(src, dst, ddepth, dx, dy, scale)
    public static void Scharr(Mat src, Mat dst, int ddepth, int dx, int dy, double scale)
    {
        
        Scharr_2(src.nativeObj, dst.nativeObj, ddepth, dx, dy, scale);
        
        return;
    }

    //javadoc: Scharr(src, dst, ddepth, dx, dy)
    public static void Scharr(Mat src, Mat dst, int ddepth, int dx, int dy)
    {
        
        Scharr_3(src.nativeObj, dst.nativeObj, ddepth, dx, dy);
        
        return;
    }


    //
    // C++:  void cv::Sobel(Mat src, Mat& dst, int ddepth, int dx, int dy, int ksize = 3, double scale = 1, double delta = 0, int borderType = BORDER_DEFAULT)
    //

    //javadoc: Sobel(src, dst, ddepth, dx, dy, ksize, scale, delta, borderType)
    public static void Sobel(Mat src, Mat dst, int ddepth, int dx, int dy, int ksize, double scale, double delta, int borderType)
    {
        
        Sobel_0(src.nativeObj, dst.nativeObj, ddepth, dx, dy, ksize, scale, delta, borderType);
        
        return;
    }

    //javadoc: Sobel(src, dst, ddepth, dx, dy, ksize, scale, delta)
    public static void Sobel(Mat src, Mat dst, int ddepth, int dx, int dy, int ksize, double scale, double delta)
    {
        
        Sobel_1(src.nativeObj, dst.nativeObj, ddepth, dx, dy, ksize, scale, delta);
        
        return;
    }

    //javadoc: Sobel(src, dst, ddepth, dx, dy, ksize, scale)
    public static void Sobel(Mat src, Mat dst, int ddepth, int dx, int dy, int ksize, double scale)
    {
        
        Sobel_2(src.nativeObj, dst.nativeObj, ddepth, dx, dy, ksize, scale);
        
        return;
    }

    //javadoc: Sobel(src, dst, ddepth, dx, dy, ksize)
    public static void Sobel(Mat src, Mat dst, int ddepth, int dx, int dy, int ksize)
    {
        
        Sobel_3(src.nativeObj, dst.nativeObj, ddepth, dx, dy, ksize);
        
        return;
    }

    //javadoc: Sobel(src, dst, ddepth, dx, dy)
    public static void Sobel(Mat src, Mat dst, int ddepth, int dx, int dy)
    {
        
        Sobel_4(src.nativeObj, dst.nativeObj, ddepth, dx, dy);
        
        return;
    }


    //
    // C++:  void cv::accumulate(Mat src, Mat& dst, Mat mask = Mat())
    //

    //javadoc: accumulate(src, dst, mask)
    public static void accumulate(Mat src, Mat dst, Mat mask)
    {
        
        accumulate_0(src.nativeObj, dst.nativeObj, mask.nativeObj);
        
        return;
    }

    //javadoc: accumulate(src, dst)
    public static void accumulate(Mat src, Mat dst)
    {
        
        accumulate_1(src.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::accumulateProduct(Mat src1, Mat src2, Mat& dst, Mat mask = Mat())
    //

    //javadoc: accumulateProduct(src1, src2, dst, mask)
    public static void accumulateProduct(Mat src1, Mat src2, Mat dst, Mat mask)
    {
        
        accumulateProduct_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, mask.nativeObj);
        
        return;
    }

    //javadoc: accumulateProduct(src1, src2, dst)
    public static void accumulateProduct(Mat src1, Mat src2, Mat dst)
    {
        
        accumulateProduct_1(src1.nativeObj, src2.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::accumulateSquare(Mat src, Mat& dst, Mat mask = Mat())
    //

    //javadoc: accumulateSquare(src, dst, mask)
    public static void accumulateSquare(Mat src, Mat dst, Mat mask)
    {
        
        accumulateSquare_0(src.nativeObj, dst.nativeObj, mask.nativeObj);
        
        return;
    }

    //javadoc: accumulateSquare(src, dst)
    public static void accumulateSquare(Mat src, Mat dst)
    {
        
        accumulateSquare_1(src.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::accumulateWeighted(Mat src, Mat& dst, double alpha, Mat mask = Mat())
    //

    //javadoc: accumulateWeighted(src, dst, alpha, mask)
    public static void accumulateWeighted(Mat src, Mat dst, double alpha, Mat mask)
    {
        
        accumulateWeighted_0(src.nativeObj, dst.nativeObj, alpha, mask.nativeObj);
        
        return;
    }

    //javadoc: accumulateWeighted(src, dst, alpha)
    public static void accumulateWeighted(Mat src, Mat dst, double alpha)
    {
        
        accumulateWeighted_1(src.nativeObj, dst.nativeObj, alpha);
        
        return;
    }


    //
    // C++:  void cv::adaptiveThreshold(Mat src, Mat& dst, double maxValue, int adaptiveMethod, int thresholdType, int blockSize, double C)
    //

    //javadoc: adaptiveThreshold(src, dst, maxValue, adaptiveMethod, thresholdType, blockSize, C)
    public static void adaptiveThreshold(Mat src, Mat dst, double maxValue, int adaptiveMethod, int thresholdType, int blockSize, double C)
    {
        
        adaptiveThreshold_0(src.nativeObj, dst.nativeObj, maxValue, adaptiveMethod, thresholdType, blockSize, C);
        
        return;
    }


    //
    // C++:  void cv::applyColorMap(Mat src, Mat& dst, Mat userColor)
    //

    //javadoc: applyColorMap(src, dst, userColor)
    public static void applyColorMap(Mat src, Mat dst, Mat userColor)
    {
        
        applyColorMap_0(src.nativeObj, dst.nativeObj, userColor.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::applyColorMap(Mat src, Mat& dst, int colormap)
    //

    //javadoc: applyColorMap(src, dst, colormap)
    public static void applyColorMap(Mat src, Mat dst, int colormap)
    {
        
        applyColorMap_1(src.nativeObj, dst.nativeObj, colormap);
        
        return;
    }


    //
    // C++:  void cv::approxPolyDP(vector_Point2f curve, vector_Point2f& approxCurve, double epsilon, bool closed)
    //

    //javadoc: approxPolyDP(curve, approxCurve, epsilon, closed)
    public static void approxPolyDP(MatOfPoint2f curve, MatOfPoint2f approxCurve, double epsilon, boolean closed)
    {
        Mat curve_mat = curve;
        Mat approxCurve_mat = approxCurve;
        approxPolyDP_0(curve_mat.nativeObj, approxCurve_mat.nativeObj, epsilon, closed);
        
        return;
    }


    //
    // C++:  void cv::arrowedLine(Mat& img, Point pt1, Point pt2, Scalar color, int thickness = 1, int line_type = 8, int shift = 0, double tipLength = 0.1)
    //

    //javadoc: arrowedLine(img, pt1, pt2, color, thickness, line_type, shift, tipLength)
    public static void arrowedLine(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int line_type, int shift, double tipLength)
    {
        
        arrowedLine_0(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3], thickness, line_type, shift, tipLength);
        
        return;
    }

    //javadoc: arrowedLine(img, pt1, pt2, color, thickness, line_type, shift)
    public static void arrowedLine(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int line_type, int shift)
    {
        
        arrowedLine_1(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3], thickness, line_type, shift);
        
        return;
    }

    //javadoc: arrowedLine(img, pt1, pt2, color, thickness, line_type)
    public static void arrowedLine(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int line_type)
    {
        
        arrowedLine_2(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3], thickness, line_type);
        
        return;
    }

    //javadoc: arrowedLine(img, pt1, pt2, color, thickness)
    public static void arrowedLine(Mat img, Point pt1, Point pt2, Scalar color, int thickness)
    {
        
        arrowedLine_3(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
        
        return;
    }

    //javadoc: arrowedLine(img, pt1, pt2, color)
    public static void arrowedLine(Mat img, Point pt1, Point pt2, Scalar color)
    {
        
        arrowedLine_4(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::bilateralFilter(Mat src, Mat& dst, int d, double sigmaColor, double sigmaSpace, int borderType = BORDER_DEFAULT)
    //

    //javadoc: bilateralFilter(src, dst, d, sigmaColor, sigmaSpace, borderType)
    public static void bilateralFilter(Mat src, Mat dst, int d, double sigmaColor, double sigmaSpace, int borderType)
    {
        
        bilateralFilter_0(src.nativeObj, dst.nativeObj, d, sigmaColor, sigmaSpace, borderType);
        
        return;
    }

    //javadoc: bilateralFilter(src, dst, d, sigmaColor, sigmaSpace)
    public static void bilateralFilter(Mat src, Mat dst, int d, double sigmaColor, double sigmaSpace)
    {
        
        bilateralFilter_1(src.nativeObj, dst.nativeObj, d, sigmaColor, sigmaSpace);
        
        return;
    }


    //
    // C++:  void cv::blur(Mat src, Mat& dst, Size ksize, Point anchor = Point(-1,-1), int borderType = BORDER_DEFAULT)
    //

    //javadoc: blur(src, dst, ksize, anchor, borderType)
    public static void blur(Mat src, Mat dst, Size ksize, Point anchor, int borderType)
    {
        
        blur_0(src.nativeObj, dst.nativeObj, ksize.width, ksize.height, anchor.x, anchor.y, borderType);
        
        return;
    }

    //javadoc: blur(src, dst, ksize, anchor)
    public static void blur(Mat src, Mat dst, Size ksize, Point anchor)
    {
        
        blur_1(src.nativeObj, dst.nativeObj, ksize.width, ksize.height, anchor.x, anchor.y);
        
        return;
    }

    //javadoc: blur(src, dst, ksize)
    public static void blur(Mat src, Mat dst, Size ksize)
    {
        
        blur_2(src.nativeObj, dst.nativeObj, ksize.width, ksize.height);
        
        return;
    }


    //
    // C++:  void cv::boxFilter(Mat src, Mat& dst, int ddepth, Size ksize, Point anchor = Point(-1,-1), bool normalize = true, int borderType = BORDER_DEFAULT)
    //

    //javadoc: boxFilter(src, dst, ddepth, ksize, anchor, normalize, borderType)
    public static void boxFilter(Mat src, Mat dst, int ddepth, Size ksize, Point anchor, boolean normalize, int borderType)
    {
        
        boxFilter_0(src.nativeObj, dst.nativeObj, ddepth, ksize.width, ksize.height, anchor.x, anchor.y, normalize, borderType);
        
        return;
    }

    //javadoc: boxFilter(src, dst, ddepth, ksize, anchor, normalize)
    public static void boxFilter(Mat src, Mat dst, int ddepth, Size ksize, Point anchor, boolean normalize)
    {
        
        boxFilter_1(src.nativeObj, dst.nativeObj, ddepth, ksize.width, ksize.height, anchor.x, anchor.y, normalize);
        
        return;
    }

    //javadoc: boxFilter(src, dst, ddepth, ksize, anchor)
    public static void boxFilter(Mat src, Mat dst, int ddepth, Size ksize, Point anchor)
    {
        
        boxFilter_2(src.nativeObj, dst.nativeObj, ddepth, ksize.width, ksize.height, anchor.x, anchor.y);
        
        return;
    }

    //javadoc: boxFilter(src, dst, ddepth, ksize)
    public static void boxFilter(Mat src, Mat dst, int ddepth, Size ksize)
    {
        
        boxFilter_3(src.nativeObj, dst.nativeObj, ddepth, ksize.width, ksize.height);
        
        return;
    }


    //
    // C++:  void cv::boxPoints(RotatedRect box, Mat& points)
    //

    //javadoc: boxPoints(box, points)
    public static void boxPoints(RotatedRect box, Mat points)
    {
        
        boxPoints_0(box.center.x, box.center.y, box.size.width, box.size.height, box.angle, points.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::calcBackProject(vector_Mat images, vector_int channels, Mat hist, Mat& dst, vector_float ranges, double scale)
    //

    //javadoc: calcBackProject(images, channels, hist, dst, ranges, scale)
    public static void calcBackProject(List<Mat> images, MatOfInt channels, Mat hist, Mat dst, MatOfFloat ranges, double scale)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat channels_mat = channels;
        Mat ranges_mat = ranges;
        calcBackProject_0(images_mat.nativeObj, channels_mat.nativeObj, hist.nativeObj, dst.nativeObj, ranges_mat.nativeObj, scale);
        
        return;
    }


    //
    // C++:  void cv::calcHist(vector_Mat images, vector_int channels, Mat mask, Mat& hist, vector_int histSize, vector_float ranges, bool accumulate = false)
    //

    //javadoc: calcHist(images, channels, mask, hist, histSize, ranges, accumulate)
    public static void calcHist(List<Mat> images, MatOfInt channels, Mat mask, Mat hist, MatOfInt histSize, MatOfFloat ranges, boolean accumulate)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat channels_mat = channels;
        Mat histSize_mat = histSize;
        Mat ranges_mat = ranges;
        calcHist_0(images_mat.nativeObj, channels_mat.nativeObj, mask.nativeObj, hist.nativeObj, histSize_mat.nativeObj, ranges_mat.nativeObj, accumulate);
        
        return;
    }

    //javadoc: calcHist(images, channels, mask, hist, histSize, ranges)
    public static void calcHist(List<Mat> images, MatOfInt channels, Mat mask, Mat hist, MatOfInt histSize, MatOfFloat ranges)
    {
        Mat images_mat = Converters.vector_Mat_to_Mat(images);
        Mat channels_mat = channels;
        Mat histSize_mat = histSize;
        Mat ranges_mat = ranges;
        calcHist_1(images_mat.nativeObj, channels_mat.nativeObj, mask.nativeObj, hist.nativeObj, histSize_mat.nativeObj, ranges_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::circle(Mat& img, Point center, int radius, Scalar color, int thickness = 1, int lineType = LINE_8, int shift = 0)
    //

    //javadoc: circle(img, center, radius, color, thickness, lineType, shift)
    public static void circle(Mat img, Point center, int radius, Scalar color, int thickness, int lineType, int shift)
    {
        
        circle_0(img.nativeObj, center.x, center.y, radius, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
        
        return;
    }

    //javadoc: circle(img, center, radius, color, thickness, lineType)
    public static void circle(Mat img, Point center, int radius, Scalar color, int thickness, int lineType)
    {
        
        circle_1(img.nativeObj, center.x, center.y, radius, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType);
        
        return;
    }

    //javadoc: circle(img, center, radius, color, thickness)
    public static void circle(Mat img, Point center, int radius, Scalar color, int thickness)
    {
        
        circle_2(img.nativeObj, center.x, center.y, radius, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
        
        return;
    }

    //javadoc: circle(img, center, radius, color)
    public static void circle(Mat img, Point center, int radius, Scalar color)
    {
        
        circle_3(img.nativeObj, center.x, center.y, radius, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::convertMaps(Mat map1, Mat map2, Mat& dstmap1, Mat& dstmap2, int dstmap1type, bool nninterpolation = false)
    //

    //javadoc: convertMaps(map1, map2, dstmap1, dstmap2, dstmap1type, nninterpolation)
    public static void convertMaps(Mat map1, Mat map2, Mat dstmap1, Mat dstmap2, int dstmap1type, boolean nninterpolation)
    {
        
        convertMaps_0(map1.nativeObj, map2.nativeObj, dstmap1.nativeObj, dstmap2.nativeObj, dstmap1type, nninterpolation);
        
        return;
    }

    //javadoc: convertMaps(map1, map2, dstmap1, dstmap2, dstmap1type)
    public static void convertMaps(Mat map1, Mat map2, Mat dstmap1, Mat dstmap2, int dstmap1type)
    {
        
        convertMaps_1(map1.nativeObj, map2.nativeObj, dstmap1.nativeObj, dstmap2.nativeObj, dstmap1type);
        
        return;
    }


    //
    // C++:  void cv::convexHull(vector_Point points, vector_int& hull, bool clockwise = false,  _hidden_  returnPoints = true)
    //

    //javadoc: convexHull(points, hull, clockwise)
    public static void convexHull(MatOfPoint points, MatOfInt hull, boolean clockwise)
    {
        Mat points_mat = points;
        Mat hull_mat = hull;
        convexHull_0(points_mat.nativeObj, hull_mat.nativeObj, clockwise);
        
        return;
    }

    //javadoc: convexHull(points, hull)
    public static void convexHull(MatOfPoint points, MatOfInt hull)
    {
        Mat points_mat = points;
        Mat hull_mat = hull;
        convexHull_2(points_mat.nativeObj, hull_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::convexityDefects(vector_Point contour, vector_int convexhull, vector_Vec4i& convexityDefects)
    //

    //javadoc: convexityDefects(contour, convexhull, convexityDefects)
    public static void convexityDefects(MatOfPoint contour, MatOfInt convexhull, MatOfInt4 convexityDefects)
    {
        Mat contour_mat = contour;
        Mat convexhull_mat = convexhull;
        Mat convexityDefects_mat = convexityDefects;
        convexityDefects_0(contour_mat.nativeObj, convexhull_mat.nativeObj, convexityDefects_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::cornerEigenValsAndVecs(Mat src, Mat& dst, int blockSize, int ksize, int borderType = BORDER_DEFAULT)
    //

    //javadoc: cornerEigenValsAndVecs(src, dst, blockSize, ksize, borderType)
    public static void cornerEigenValsAndVecs(Mat src, Mat dst, int blockSize, int ksize, int borderType)
    {
        
        cornerEigenValsAndVecs_0(src.nativeObj, dst.nativeObj, blockSize, ksize, borderType);
        
        return;
    }

    //javadoc: cornerEigenValsAndVecs(src, dst, blockSize, ksize)
    public static void cornerEigenValsAndVecs(Mat src, Mat dst, int blockSize, int ksize)
    {
        
        cornerEigenValsAndVecs_1(src.nativeObj, dst.nativeObj, blockSize, ksize);
        
        return;
    }


    //
    // C++:  void cv::cornerHarris(Mat src, Mat& dst, int blockSize, int ksize, double k, int borderType = BORDER_DEFAULT)
    //

    //javadoc: cornerHarris(src, dst, blockSize, ksize, k, borderType)
    public static void cornerHarris(Mat src, Mat dst, int blockSize, int ksize, double k, int borderType)
    {
        
        cornerHarris_0(src.nativeObj, dst.nativeObj, blockSize, ksize, k, borderType);
        
        return;
    }

    //javadoc: cornerHarris(src, dst, blockSize, ksize, k)
    public static void cornerHarris(Mat src, Mat dst, int blockSize, int ksize, double k)
    {
        
        cornerHarris_1(src.nativeObj, dst.nativeObj, blockSize, ksize, k);
        
        return;
    }


    //
    // C++:  void cv::cornerMinEigenVal(Mat src, Mat& dst, int blockSize, int ksize = 3, int borderType = BORDER_DEFAULT)
    //

    //javadoc: cornerMinEigenVal(src, dst, blockSize, ksize, borderType)
    public static void cornerMinEigenVal(Mat src, Mat dst, int blockSize, int ksize, int borderType)
    {
        
        cornerMinEigenVal_0(src.nativeObj, dst.nativeObj, blockSize, ksize, borderType);
        
        return;
    }

    //javadoc: cornerMinEigenVal(src, dst, blockSize, ksize)
    public static void cornerMinEigenVal(Mat src, Mat dst, int blockSize, int ksize)
    {
        
        cornerMinEigenVal_1(src.nativeObj, dst.nativeObj, blockSize, ksize);
        
        return;
    }

    //javadoc: cornerMinEigenVal(src, dst, blockSize)
    public static void cornerMinEigenVal(Mat src, Mat dst, int blockSize)
    {
        
        cornerMinEigenVal_2(src.nativeObj, dst.nativeObj, blockSize);
        
        return;
    }


    //
    // C++:  void cv::cornerSubPix(Mat image, Mat& corners, Size winSize, Size zeroZone, TermCriteria criteria)
    //

    //javadoc: cornerSubPix(image, corners, winSize, zeroZone, criteria)
    public static void cornerSubPix(Mat image, Mat corners, Size winSize, Size zeroZone, TermCriteria criteria)
    {
        
        cornerSubPix_0(image.nativeObj, corners.nativeObj, winSize.width, winSize.height, zeroZone.width, zeroZone.height, criteria.type, criteria.maxCount, criteria.epsilon);
        
        return;
    }


    //
    // C++:  void cv::createHanningWindow(Mat& dst, Size winSize, int type)
    //

    //javadoc: createHanningWindow(dst, winSize, type)
    public static void createHanningWindow(Mat dst, Size winSize, int type)
    {
        
        createHanningWindow_0(dst.nativeObj, winSize.width, winSize.height, type);
        
        return;
    }


    //
    // C++:  void cv::cvtColor(Mat src, Mat& dst, int code, int dstCn = 0)
    //

    //javadoc: cvtColor(src, dst, code, dstCn)
    public static void cvtColor(Mat src, Mat dst, int code, int dstCn)
    {
        
        cvtColor_0(src.nativeObj, dst.nativeObj, code, dstCn);
        
        return;
    }

    //javadoc: cvtColor(src, dst, code)
    public static void cvtColor(Mat src, Mat dst, int code)
    {
        
        cvtColor_1(src.nativeObj, dst.nativeObj, code);
        
        return;
    }


    //
    // C++:  void cv::cvtColorTwoPlane(Mat src1, Mat src2, Mat& dst, int code)
    //

    //javadoc: cvtColorTwoPlane(src1, src2, dst, code)
    public static void cvtColorTwoPlane(Mat src1, Mat src2, Mat dst, int code)
    {
        
        cvtColorTwoPlane_0(src1.nativeObj, src2.nativeObj, dst.nativeObj, code);
        
        return;
    }


    //
    // C++:  void cv::demosaicing(Mat src, Mat& dst, int code, int dstCn = 0)
    //

    //javadoc: demosaicing(src, dst, code, dstCn)
    public static void demosaicing(Mat src, Mat dst, int code, int dstCn)
    {
        
        demosaicing_0(src.nativeObj, dst.nativeObj, code, dstCn);
        
        return;
    }

    //javadoc: demosaicing(src, dst, code)
    public static void demosaicing(Mat src, Mat dst, int code)
    {
        
        demosaicing_1(src.nativeObj, dst.nativeObj, code);
        
        return;
    }


    //
    // C++:  void cv::dilate(Mat src, Mat& dst, Mat kernel, Point anchor = Point(-1,-1), int iterations = 1, int borderType = BORDER_CONSTANT, Scalar borderValue = morphologyDefaultBorderValue())
    //

    //javadoc: dilate(src, dst, kernel, anchor, iterations, borderType, borderValue)
    public static void dilate(Mat src, Mat dst, Mat kernel, Point anchor, int iterations, int borderType, Scalar borderValue)
    {
        
        dilate_0(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.x, anchor.y, iterations, borderType, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
        
        return;
    }

    //javadoc: dilate(src, dst, kernel, anchor, iterations, borderType)
    public static void dilate(Mat src, Mat dst, Mat kernel, Point anchor, int iterations, int borderType)
    {
        
        dilate_1(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.x, anchor.y, iterations, borderType);
        
        return;
    }

    //javadoc: dilate(src, dst, kernel, anchor, iterations)
    public static void dilate(Mat src, Mat dst, Mat kernel, Point anchor, int iterations)
    {
        
        dilate_2(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.x, anchor.y, iterations);
        
        return;
    }

    //javadoc: dilate(src, dst, kernel, anchor)
    public static void dilate(Mat src, Mat dst, Mat kernel, Point anchor)
    {
        
        dilate_3(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.x, anchor.y);
        
        return;
    }

    //javadoc: dilate(src, dst, kernel)
    public static void dilate(Mat src, Mat dst, Mat kernel)
    {
        
        dilate_4(src.nativeObj, dst.nativeObj, kernel.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::distanceTransform(Mat src, Mat& dst, Mat& labels, int distanceType, int maskSize, int labelType = DIST_LABEL_CCOMP)
    //

    //javadoc: distanceTransformWithLabels(src, dst, labels, distanceType, maskSize, labelType)
    public static void distanceTransformWithLabels(Mat src, Mat dst, Mat labels, int distanceType, int maskSize, int labelType)
    {
        
        distanceTransformWithLabels_0(src.nativeObj, dst.nativeObj, labels.nativeObj, distanceType, maskSize, labelType);
        
        return;
    }

    //javadoc: distanceTransformWithLabels(src, dst, labels, distanceType, maskSize)
    public static void distanceTransformWithLabels(Mat src, Mat dst, Mat labels, int distanceType, int maskSize)
    {
        
        distanceTransformWithLabels_1(src.nativeObj, dst.nativeObj, labels.nativeObj, distanceType, maskSize);
        
        return;
    }


    //
    // C++:  void cv::distanceTransform(Mat src, Mat& dst, int distanceType, int maskSize, int dstType = CV_32F)
    //

    //javadoc: distanceTransform(src, dst, distanceType, maskSize, dstType)
    public static void distanceTransform(Mat src, Mat dst, int distanceType, int maskSize, int dstType)
    {
        
        distanceTransform_0(src.nativeObj, dst.nativeObj, distanceType, maskSize, dstType);
        
        return;
    }

    //javadoc: distanceTransform(src, dst, distanceType, maskSize)
    public static void distanceTransform(Mat src, Mat dst, int distanceType, int maskSize)
    {
        
        distanceTransform_1(src.nativeObj, dst.nativeObj, distanceType, maskSize);
        
        return;
    }


    //
    // C++:  void cv::drawContours(Mat& image, vector_vector_Point contours, int contourIdx, Scalar color, int thickness = 1, int lineType = LINE_8, Mat hierarchy = Mat(), int maxLevel = INT_MAX, Point offset = Point())
    //

    //javadoc: drawContours(image, contours, contourIdx, color, thickness, lineType, hierarchy, maxLevel, offset)
    public static void drawContours(Mat image, List<MatOfPoint> contours, int contourIdx, Scalar color, int thickness, int lineType, Mat hierarchy, int maxLevel, Point offset)
    {
        List<Mat> contours_tmplm = new ArrayList<Mat>((contours != null) ? contours.size() : 0);
        Mat contours_mat = Converters.vector_vector_Point_to_Mat(contours, contours_tmplm);
        drawContours_0(image.nativeObj, contours_mat.nativeObj, contourIdx, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, hierarchy.nativeObj, maxLevel, offset.x, offset.y);
        
        return;
    }

    //javadoc: drawContours(image, contours, contourIdx, color, thickness, lineType, hierarchy, maxLevel)
    public static void drawContours(Mat image, List<MatOfPoint> contours, int contourIdx, Scalar color, int thickness, int lineType, Mat hierarchy, int maxLevel)
    {
        List<Mat> contours_tmplm = new ArrayList<Mat>((contours != null) ? contours.size() : 0);
        Mat contours_mat = Converters.vector_vector_Point_to_Mat(contours, contours_tmplm);
        drawContours_1(image.nativeObj, contours_mat.nativeObj, contourIdx, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, hierarchy.nativeObj, maxLevel);
        
        return;
    }

    //javadoc: drawContours(image, contours, contourIdx, color, thickness, lineType, hierarchy)
    public static void drawContours(Mat image, List<MatOfPoint> contours, int contourIdx, Scalar color, int thickness, int lineType, Mat hierarchy)
    {
        List<Mat> contours_tmplm = new ArrayList<Mat>((contours != null) ? contours.size() : 0);
        Mat contours_mat = Converters.vector_vector_Point_to_Mat(contours, contours_tmplm);
        drawContours_2(image.nativeObj, contours_mat.nativeObj, contourIdx, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, hierarchy.nativeObj);
        
        return;
    }

    //javadoc: drawContours(image, contours, contourIdx, color, thickness, lineType)
    public static void drawContours(Mat image, List<MatOfPoint> contours, int contourIdx, Scalar color, int thickness, int lineType)
    {
        List<Mat> contours_tmplm = new ArrayList<Mat>((contours != null) ? contours.size() : 0);
        Mat contours_mat = Converters.vector_vector_Point_to_Mat(contours, contours_tmplm);
        drawContours_3(image.nativeObj, contours_mat.nativeObj, contourIdx, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType);
        
        return;
    }

    //javadoc: drawContours(image, contours, contourIdx, color, thickness)
    public static void drawContours(Mat image, List<MatOfPoint> contours, int contourIdx, Scalar color, int thickness)
    {
        List<Mat> contours_tmplm = new ArrayList<Mat>((contours != null) ? contours.size() : 0);
        Mat contours_mat = Converters.vector_vector_Point_to_Mat(contours, contours_tmplm);
        drawContours_4(image.nativeObj, contours_mat.nativeObj, contourIdx, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
        
        return;
    }

    //javadoc: drawContours(image, contours, contourIdx, color)
    public static void drawContours(Mat image, List<MatOfPoint> contours, int contourIdx, Scalar color)
    {
        List<Mat> contours_tmplm = new ArrayList<Mat>((contours != null) ? contours.size() : 0);
        Mat contours_mat = Converters.vector_vector_Point_to_Mat(contours, contours_tmplm);
        drawContours_5(image.nativeObj, contours_mat.nativeObj, contourIdx, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::drawMarker(Mat& img, Point position, Scalar color, int markerType = MARKER_CROSS, int markerSize = 20, int thickness = 1, int line_type = 8)
    //

    //javadoc: drawMarker(img, position, color, markerType, markerSize, thickness, line_type)
    public static void drawMarker(Mat img, Point position, Scalar color, int markerType, int markerSize, int thickness, int line_type)
    {
        
        drawMarker_0(img.nativeObj, position.x, position.y, color.val[0], color.val[1], color.val[2], color.val[3], markerType, markerSize, thickness, line_type);
        
        return;
    }

    //javadoc: drawMarker(img, position, color, markerType, markerSize, thickness)
    public static void drawMarker(Mat img, Point position, Scalar color, int markerType, int markerSize, int thickness)
    {
        
        drawMarker_1(img.nativeObj, position.x, position.y, color.val[0], color.val[1], color.val[2], color.val[3], markerType, markerSize, thickness);
        
        return;
    }

    //javadoc: drawMarker(img, position, color, markerType, markerSize)
    public static void drawMarker(Mat img, Point position, Scalar color, int markerType, int markerSize)
    {
        
        drawMarker_2(img.nativeObj, position.x, position.y, color.val[0], color.val[1], color.val[2], color.val[3], markerType, markerSize);
        
        return;
    }

    //javadoc: drawMarker(img, position, color, markerType)
    public static void drawMarker(Mat img, Point position, Scalar color, int markerType)
    {
        
        drawMarker_3(img.nativeObj, position.x, position.y, color.val[0], color.val[1], color.val[2], color.val[3], markerType);
        
        return;
    }

    //javadoc: drawMarker(img, position, color)
    public static void drawMarker(Mat img, Point position, Scalar color)
    {
        
        drawMarker_4(img.nativeObj, position.x, position.y, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::ellipse(Mat& img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color, int thickness = 1, int lineType = LINE_8, int shift = 0)
    //

    //javadoc: ellipse(img, center, axes, angle, startAngle, endAngle, color, thickness, lineType, shift)
    public static void ellipse(Mat img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color, int thickness, int lineType, int shift)
    {
        
        ellipse_0(img.nativeObj, center.x, center.y, axes.width, axes.height, angle, startAngle, endAngle, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
        
        return;
    }

    //javadoc: ellipse(img, center, axes, angle, startAngle, endAngle, color, thickness, lineType)
    public static void ellipse(Mat img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color, int thickness, int lineType)
    {
        
        ellipse_1(img.nativeObj, center.x, center.y, axes.width, axes.height, angle, startAngle, endAngle, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType);
        
        return;
    }

    //javadoc: ellipse(img, center, axes, angle, startAngle, endAngle, color, thickness)
    public static void ellipse(Mat img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color, int thickness)
    {
        
        ellipse_2(img.nativeObj, center.x, center.y, axes.width, axes.height, angle, startAngle, endAngle, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
        
        return;
    }

    //javadoc: ellipse(img, center, axes, angle, startAngle, endAngle, color)
    public static void ellipse(Mat img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color)
    {
        
        ellipse_3(img.nativeObj, center.x, center.y, axes.width, axes.height, angle, startAngle, endAngle, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::ellipse(Mat& img, RotatedRect box, Scalar color, int thickness = 1, int lineType = LINE_8)
    //

    //javadoc: ellipse(img, box, color, thickness, lineType)
    public static void ellipse(Mat img, RotatedRect box, Scalar color, int thickness, int lineType)
    {
        
        ellipse_4(img.nativeObj, box.center.x, box.center.y, box.size.width, box.size.height, box.angle, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType);
        
        return;
    }

    //javadoc: ellipse(img, box, color, thickness)
    public static void ellipse(Mat img, RotatedRect box, Scalar color, int thickness)
    {
        
        ellipse_5(img.nativeObj, box.center.x, box.center.y, box.size.width, box.size.height, box.angle, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
        
        return;
    }

    //javadoc: ellipse(img, box, color)
    public static void ellipse(Mat img, RotatedRect box, Scalar color)
    {
        
        ellipse_6(img.nativeObj, box.center.x, box.center.y, box.size.width, box.size.height, box.angle, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::ellipse2Poly(Point center, Size axes, int angle, int arcStart, int arcEnd, int delta, vector_Point& pts)
    //

    //javadoc: ellipse2Poly(center, axes, angle, arcStart, arcEnd, delta, pts)
    public static void ellipse2Poly(Point center, Size axes, int angle, int arcStart, int arcEnd, int delta, MatOfPoint pts)
    {
        Mat pts_mat = pts;
        ellipse2Poly_0(center.x, center.y, axes.width, axes.height, angle, arcStart, arcEnd, delta, pts_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::equalizeHist(Mat src, Mat& dst)
    //

    //javadoc: equalizeHist(src, dst)
    public static void equalizeHist(Mat src, Mat dst)
    {
        
        equalizeHist_0(src.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::erode(Mat src, Mat& dst, Mat kernel, Point anchor = Point(-1,-1), int iterations = 1, int borderType = BORDER_CONSTANT, Scalar borderValue = morphologyDefaultBorderValue())
    //

    //javadoc: erode(src, dst, kernel, anchor, iterations, borderType, borderValue)
    public static void erode(Mat src, Mat dst, Mat kernel, Point anchor, int iterations, int borderType, Scalar borderValue)
    {
        
        erode_0(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.x, anchor.y, iterations, borderType, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
        
        return;
    }

    //javadoc: erode(src, dst, kernel, anchor, iterations, borderType)
    public static void erode(Mat src, Mat dst, Mat kernel, Point anchor, int iterations, int borderType)
    {
        
        erode_1(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.x, anchor.y, iterations, borderType);
        
        return;
    }

    //javadoc: erode(src, dst, kernel, anchor, iterations)
    public static void erode(Mat src, Mat dst, Mat kernel, Point anchor, int iterations)
    {
        
        erode_2(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.x, anchor.y, iterations);
        
        return;
    }

    //javadoc: erode(src, dst, kernel, anchor)
    public static void erode(Mat src, Mat dst, Mat kernel, Point anchor)
    {
        
        erode_3(src.nativeObj, dst.nativeObj, kernel.nativeObj, anchor.x, anchor.y);
        
        return;
    }

    //javadoc: erode(src, dst, kernel)
    public static void erode(Mat src, Mat dst, Mat kernel)
    {
        
        erode_4(src.nativeObj, dst.nativeObj, kernel.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::fillConvexPoly(Mat& img, vector_Point points, Scalar color, int lineType = LINE_8, int shift = 0)
    //

    //javadoc: fillConvexPoly(img, points, color, lineType, shift)
    public static void fillConvexPoly(Mat img, MatOfPoint points, Scalar color, int lineType, int shift)
    {
        Mat points_mat = points;
        fillConvexPoly_0(img.nativeObj, points_mat.nativeObj, color.val[0], color.val[1], color.val[2], color.val[3], lineType, shift);
        
        return;
    }

    //javadoc: fillConvexPoly(img, points, color, lineType)
    public static void fillConvexPoly(Mat img, MatOfPoint points, Scalar color, int lineType)
    {
        Mat points_mat = points;
        fillConvexPoly_1(img.nativeObj, points_mat.nativeObj, color.val[0], color.val[1], color.val[2], color.val[3], lineType);
        
        return;
    }

    //javadoc: fillConvexPoly(img, points, color)
    public static void fillConvexPoly(Mat img, MatOfPoint points, Scalar color)
    {
        Mat points_mat = points;
        fillConvexPoly_2(img.nativeObj, points_mat.nativeObj, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::fillPoly(Mat& img, vector_vector_Point pts, Scalar color, int lineType = LINE_8, int shift = 0, Point offset = Point())
    //

    //javadoc: fillPoly(img, pts, color, lineType, shift, offset)
    public static void fillPoly(Mat img, List<MatOfPoint> pts, Scalar color, int lineType, int shift, Point offset)
    {
        List<Mat> pts_tmplm = new ArrayList<Mat>((pts != null) ? pts.size() : 0);
        Mat pts_mat = Converters.vector_vector_Point_to_Mat(pts, pts_tmplm);
        fillPoly_0(img.nativeObj, pts_mat.nativeObj, color.val[0], color.val[1], color.val[2], color.val[3], lineType, shift, offset.x, offset.y);
        
        return;
    }

    //javadoc: fillPoly(img, pts, color, lineType, shift)
    public static void fillPoly(Mat img, List<MatOfPoint> pts, Scalar color, int lineType, int shift)
    {
        List<Mat> pts_tmplm = new ArrayList<Mat>((pts != null) ? pts.size() : 0);
        Mat pts_mat = Converters.vector_vector_Point_to_Mat(pts, pts_tmplm);
        fillPoly_1(img.nativeObj, pts_mat.nativeObj, color.val[0], color.val[1], color.val[2], color.val[3], lineType, shift);
        
        return;
    }

    //javadoc: fillPoly(img, pts, color, lineType)
    public static void fillPoly(Mat img, List<MatOfPoint> pts, Scalar color, int lineType)
    {
        List<Mat> pts_tmplm = new ArrayList<Mat>((pts != null) ? pts.size() : 0);
        Mat pts_mat = Converters.vector_vector_Point_to_Mat(pts, pts_tmplm);
        fillPoly_2(img.nativeObj, pts_mat.nativeObj, color.val[0], color.val[1], color.val[2], color.val[3], lineType);
        
        return;
    }

    //javadoc: fillPoly(img, pts, color)
    public static void fillPoly(Mat img, List<MatOfPoint> pts, Scalar color)
    {
        List<Mat> pts_tmplm = new ArrayList<Mat>((pts != null) ? pts.size() : 0);
        Mat pts_mat = Converters.vector_vector_Point_to_Mat(pts, pts_tmplm);
        fillPoly_3(img.nativeObj, pts_mat.nativeObj, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::filter2D(Mat src, Mat& dst, int ddepth, Mat kernel, Point anchor = Point(-1,-1), double delta = 0, int borderType = BORDER_DEFAULT)
    //

    //javadoc: filter2D(src, dst, ddepth, kernel, anchor, delta, borderType)
    public static void filter2D(Mat src, Mat dst, int ddepth, Mat kernel, Point anchor, double delta, int borderType)
    {
        
        filter2D_0(src.nativeObj, dst.nativeObj, ddepth, kernel.nativeObj, anchor.x, anchor.y, delta, borderType);
        
        return;
    }

    //javadoc: filter2D(src, dst, ddepth, kernel, anchor, delta)
    public static void filter2D(Mat src, Mat dst, int ddepth, Mat kernel, Point anchor, double delta)
    {
        
        filter2D_1(src.nativeObj, dst.nativeObj, ddepth, kernel.nativeObj, anchor.x, anchor.y, delta);
        
        return;
    }

    //javadoc: filter2D(src, dst, ddepth, kernel, anchor)
    public static void filter2D(Mat src, Mat dst, int ddepth, Mat kernel, Point anchor)
    {
        
        filter2D_2(src.nativeObj, dst.nativeObj, ddepth, kernel.nativeObj, anchor.x, anchor.y);
        
        return;
    }

    //javadoc: filter2D(src, dst, ddepth, kernel)
    public static void filter2D(Mat src, Mat dst, int ddepth, Mat kernel)
    {
        
        filter2D_3(src.nativeObj, dst.nativeObj, ddepth, kernel.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::findContours(Mat image, vector_vector_Point& contours, Mat& hierarchy, int mode, int method, Point offset = Point())
    //

    //javadoc: findContours(image, contours, hierarchy, mode, method, offset)
    public static void findContours(Mat image, List<MatOfPoint> contours, Mat hierarchy, int mode, int method, Point offset)
    {
        Mat contours_mat = new Mat();
        findContours_0(image.nativeObj, contours_mat.nativeObj, hierarchy.nativeObj, mode, method, offset.x, offset.y);
        Converters.Mat_to_vector_vector_Point(contours_mat, contours);
        contours_mat.release();
        return;
    }

    //javadoc: findContours(image, contours, hierarchy, mode, method)
    public static void findContours(Mat image, List<MatOfPoint> contours, Mat hierarchy, int mode, int method)
    {
        Mat contours_mat = new Mat();
        findContours_1(image.nativeObj, contours_mat.nativeObj, hierarchy.nativeObj, mode, method);
        Converters.Mat_to_vector_vector_Point(contours_mat, contours);
        contours_mat.release();
        return;
    }


    //
    // C++:  void cv::fitLine(Mat points, Mat& line, int distType, double param, double reps, double aeps)
    //

    //javadoc: fitLine(points, line, distType, param, reps, aeps)
    public static void fitLine(Mat points, Mat line, int distType, double param, double reps, double aeps)
    {
        
        fitLine_0(points.nativeObj, line.nativeObj, distType, param, reps, aeps);
        
        return;
    }


    //
    // C++:  void cv::getDerivKernels(Mat& kx, Mat& ky, int dx, int dy, int ksize, bool normalize = false, int ktype = CV_32F)
    //

    //javadoc: getDerivKernels(kx, ky, dx, dy, ksize, normalize, ktype)
    public static void getDerivKernels(Mat kx, Mat ky, int dx, int dy, int ksize, boolean normalize, int ktype)
    {
        
        getDerivKernels_0(kx.nativeObj, ky.nativeObj, dx, dy, ksize, normalize, ktype);
        
        return;
    }

    //javadoc: getDerivKernels(kx, ky, dx, dy, ksize, normalize)
    public static void getDerivKernels(Mat kx, Mat ky, int dx, int dy, int ksize, boolean normalize)
    {
        
        getDerivKernels_1(kx.nativeObj, ky.nativeObj, dx, dy, ksize, normalize);
        
        return;
    }

    //javadoc: getDerivKernels(kx, ky, dx, dy, ksize)
    public static void getDerivKernels(Mat kx, Mat ky, int dx, int dy, int ksize)
    {
        
        getDerivKernels_2(kx.nativeObj, ky.nativeObj, dx, dy, ksize);
        
        return;
    }


    //
    // C++:  void cv::getRectSubPix(Mat image, Size patchSize, Point2f center, Mat& patch, int patchType = -1)
    //

    //javadoc: getRectSubPix(image, patchSize, center, patch, patchType)
    public static void getRectSubPix(Mat image, Size patchSize, Point center, Mat patch, int patchType)
    {
        
        getRectSubPix_0(image.nativeObj, patchSize.width, patchSize.height, center.x, center.y, patch.nativeObj, patchType);
        
        return;
    }

    //javadoc: getRectSubPix(image, patchSize, center, patch)
    public static void getRectSubPix(Mat image, Size patchSize, Point center, Mat patch)
    {
        
        getRectSubPix_1(image.nativeObj, patchSize.width, patchSize.height, center.x, center.y, patch.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::goodFeaturesToTrack(Mat image, vector_Point& corners, int maxCorners, double qualityLevel, double minDistance, Mat mask, int blockSize, int gradientSize, bool useHarrisDetector = false, double k = 0.04)
    //

    //javadoc: goodFeaturesToTrack(image, corners, maxCorners, qualityLevel, minDistance, mask, blockSize, gradientSize, useHarrisDetector, k)
    public static void goodFeaturesToTrack(Mat image, MatOfPoint corners, int maxCorners, double qualityLevel, double minDistance, Mat mask, int blockSize, int gradientSize, boolean useHarrisDetector, double k)
    {
        Mat corners_mat = corners;
        goodFeaturesToTrack_0(image.nativeObj, corners_mat.nativeObj, maxCorners, qualityLevel, minDistance, mask.nativeObj, blockSize, gradientSize, useHarrisDetector, k);
        
        return;
    }

    //javadoc: goodFeaturesToTrack(image, corners, maxCorners, qualityLevel, minDistance, mask, blockSize, gradientSize, useHarrisDetector)
    public static void goodFeaturesToTrack(Mat image, MatOfPoint corners, int maxCorners, double qualityLevel, double minDistance, Mat mask, int blockSize, int gradientSize, boolean useHarrisDetector)
    {
        Mat corners_mat = corners;
        goodFeaturesToTrack_1(image.nativeObj, corners_mat.nativeObj, maxCorners, qualityLevel, minDistance, mask.nativeObj, blockSize, gradientSize, useHarrisDetector);
        
        return;
    }

    //javadoc: goodFeaturesToTrack(image, corners, maxCorners, qualityLevel, minDistance, mask, blockSize, gradientSize)
    public static void goodFeaturesToTrack(Mat image, MatOfPoint corners, int maxCorners, double qualityLevel, double minDistance, Mat mask, int blockSize, int gradientSize)
    {
        Mat corners_mat = corners;
        goodFeaturesToTrack_2(image.nativeObj, corners_mat.nativeObj, maxCorners, qualityLevel, minDistance, mask.nativeObj, blockSize, gradientSize);
        
        return;
    }


    //
    // C++:  void cv::goodFeaturesToTrack(Mat image, vector_Point& corners, int maxCorners, double qualityLevel, double minDistance, Mat mask = Mat(), int blockSize = 3, bool useHarrisDetector = false, double k = 0.04)
    //

    //javadoc: goodFeaturesToTrack(image, corners, maxCorners, qualityLevel, minDistance, mask, blockSize, useHarrisDetector, k)
    public static void goodFeaturesToTrack(Mat image, MatOfPoint corners, int maxCorners, double qualityLevel, double minDistance, Mat mask, int blockSize, boolean useHarrisDetector, double k)
    {
        Mat corners_mat = corners;
        goodFeaturesToTrack_3(image.nativeObj, corners_mat.nativeObj, maxCorners, qualityLevel, minDistance, mask.nativeObj, blockSize, useHarrisDetector, k);
        
        return;
    }

    //javadoc: goodFeaturesToTrack(image, corners, maxCorners, qualityLevel, minDistance, mask, blockSize, useHarrisDetector)
    public static void goodFeaturesToTrack(Mat image, MatOfPoint corners, int maxCorners, double qualityLevel, double minDistance, Mat mask, int blockSize, boolean useHarrisDetector)
    {
        Mat corners_mat = corners;
        goodFeaturesToTrack_4(image.nativeObj, corners_mat.nativeObj, maxCorners, qualityLevel, minDistance, mask.nativeObj, blockSize, useHarrisDetector);
        
        return;
    }

    //javadoc: goodFeaturesToTrack(image, corners, maxCorners, qualityLevel, minDistance, mask, blockSize)
    public static void goodFeaturesToTrack(Mat image, MatOfPoint corners, int maxCorners, double qualityLevel, double minDistance, Mat mask, int blockSize)
    {
        Mat corners_mat = corners;
        goodFeaturesToTrack_5(image.nativeObj, corners_mat.nativeObj, maxCorners, qualityLevel, minDistance, mask.nativeObj, blockSize);
        
        return;
    }

    //javadoc: goodFeaturesToTrack(image, corners, maxCorners, qualityLevel, minDistance, mask)
    public static void goodFeaturesToTrack(Mat image, MatOfPoint corners, int maxCorners, double qualityLevel, double minDistance, Mat mask)
    {
        Mat corners_mat = corners;
        goodFeaturesToTrack_6(image.nativeObj, corners_mat.nativeObj, maxCorners, qualityLevel, minDistance, mask.nativeObj);
        
        return;
    }

    //javadoc: goodFeaturesToTrack(image, corners, maxCorners, qualityLevel, minDistance)
    public static void goodFeaturesToTrack(Mat image, MatOfPoint corners, int maxCorners, double qualityLevel, double minDistance)
    {
        Mat corners_mat = corners;
        goodFeaturesToTrack_7(image.nativeObj, corners_mat.nativeObj, maxCorners, qualityLevel, minDistance);
        
        return;
    }


    //
    // C++:  void cv::grabCut(Mat img, Mat& mask, Rect rect, Mat& bgdModel, Mat& fgdModel, int iterCount, int mode = GC_EVAL)
    //

    //javadoc: grabCut(img, mask, rect, bgdModel, fgdModel, iterCount, mode)
    public static void grabCut(Mat img, Mat mask, Rect rect, Mat bgdModel, Mat fgdModel, int iterCount, int mode)
    {
        
        grabCut_0(img.nativeObj, mask.nativeObj, rect.x, rect.y, rect.width, rect.height, bgdModel.nativeObj, fgdModel.nativeObj, iterCount, mode);
        
        return;
    }

    //javadoc: grabCut(img, mask, rect, bgdModel, fgdModel, iterCount)
    public static void grabCut(Mat img, Mat mask, Rect rect, Mat bgdModel, Mat fgdModel, int iterCount)
    {
        
        grabCut_1(img.nativeObj, mask.nativeObj, rect.x, rect.y, rect.width, rect.height, bgdModel.nativeObj, fgdModel.nativeObj, iterCount);
        
        return;
    }


    //
    // C++:  void cv::integral(Mat src, Mat& sum, Mat& sqsum, Mat& tilted, int sdepth = -1, int sqdepth = -1)
    //

    //javadoc: integral3(src, sum, sqsum, tilted, sdepth, sqdepth)
    public static void integral3(Mat src, Mat sum, Mat sqsum, Mat tilted, int sdepth, int sqdepth)
    {
        
        integral3_0(src.nativeObj, sum.nativeObj, sqsum.nativeObj, tilted.nativeObj, sdepth, sqdepth);
        
        return;
    }

    //javadoc: integral3(src, sum, sqsum, tilted, sdepth)
    public static void integral3(Mat src, Mat sum, Mat sqsum, Mat tilted, int sdepth)
    {
        
        integral3_1(src.nativeObj, sum.nativeObj, sqsum.nativeObj, tilted.nativeObj, sdepth);
        
        return;
    }

    //javadoc: integral3(src, sum, sqsum, tilted)
    public static void integral3(Mat src, Mat sum, Mat sqsum, Mat tilted)
    {
        
        integral3_2(src.nativeObj, sum.nativeObj, sqsum.nativeObj, tilted.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::integral(Mat src, Mat& sum, Mat& sqsum, int sdepth = -1, int sqdepth = -1)
    //

    //javadoc: integral2(src, sum, sqsum, sdepth, sqdepth)
    public static void integral2(Mat src, Mat sum, Mat sqsum, int sdepth, int sqdepth)
    {
        
        integral2_0(src.nativeObj, sum.nativeObj, sqsum.nativeObj, sdepth, sqdepth);
        
        return;
    }

    //javadoc: integral2(src, sum, sqsum, sdepth)
    public static void integral2(Mat src, Mat sum, Mat sqsum, int sdepth)
    {
        
        integral2_1(src.nativeObj, sum.nativeObj, sqsum.nativeObj, sdepth);
        
        return;
    }

    //javadoc: integral2(src, sum, sqsum)
    public static void integral2(Mat src, Mat sum, Mat sqsum)
    {
        
        integral2_2(src.nativeObj, sum.nativeObj, sqsum.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::integral(Mat src, Mat& sum, int sdepth = -1)
    //

    //javadoc: integral(src, sum, sdepth)
    public static void integral(Mat src, Mat sum, int sdepth)
    {
        
        integral_0(src.nativeObj, sum.nativeObj, sdepth);
        
        return;
    }

    //javadoc: integral(src, sum)
    public static void integral(Mat src, Mat sum)
    {
        
        integral_1(src.nativeObj, sum.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::invertAffineTransform(Mat M, Mat& iM)
    //

    //javadoc: invertAffineTransform(M, iM)
    public static void invertAffineTransform(Mat M, Mat iM)
    {
        
        invertAffineTransform_0(M.nativeObj, iM.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::line(Mat& img, Point pt1, Point pt2, Scalar color, int thickness = 1, int lineType = LINE_8, int shift = 0)
    //

    //javadoc: line(img, pt1, pt2, color, thickness, lineType, shift)
    public static void line(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int lineType, int shift)
    {
        
        line_0(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
        
        return;
    }

    //javadoc: line(img, pt1, pt2, color, thickness, lineType)
    public static void line(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int lineType)
    {
        
        line_1(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType);
        
        return;
    }

    //javadoc: line(img, pt1, pt2, color, thickness)
    public static void line(Mat img, Point pt1, Point pt2, Scalar color, int thickness)
    {
        
        line_2(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
        
        return;
    }

    //javadoc: line(img, pt1, pt2, color)
    public static void line(Mat img, Point pt1, Point pt2, Scalar color)
    {
        
        line_3(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::linearPolar(Mat src, Mat& dst, Point2f center, double maxRadius, int flags)
    //

    //javadoc: linearPolar(src, dst, center, maxRadius, flags)
    @Deprecated
    public static void linearPolar(Mat src, Mat dst, Point center, double maxRadius, int flags)
    {
        
        linearPolar_0(src.nativeObj, dst.nativeObj, center.x, center.y, maxRadius, flags);
        
        return;
    }


    //
    // C++:  void cv::logPolar(Mat src, Mat& dst, Point2f center, double M, int flags)
    //

    //javadoc: logPolar(src, dst, center, M, flags)
    @Deprecated
    public static void logPolar(Mat src, Mat dst, Point center, double M, int flags)
    {
        
        logPolar_0(src.nativeObj, dst.nativeObj, center.x, center.y, M, flags);
        
        return;
    }


    //
    // C++:  void cv::matchTemplate(Mat image, Mat templ, Mat& result, int method, Mat mask = Mat())
    //

    //javadoc: matchTemplate(image, templ, result, method, mask)
    public static void matchTemplate(Mat image, Mat templ, Mat result, int method, Mat mask)
    {
        
        matchTemplate_0(image.nativeObj, templ.nativeObj, result.nativeObj, method, mask.nativeObj);
        
        return;
    }

    //javadoc: matchTemplate(image, templ, result, method)
    public static void matchTemplate(Mat image, Mat templ, Mat result, int method)
    {
        
        matchTemplate_1(image.nativeObj, templ.nativeObj, result.nativeObj, method);
        
        return;
    }


    //
    // C++:  void cv::medianBlur(Mat src, Mat& dst, int ksize)
    //

    //javadoc: medianBlur(src, dst, ksize)
    public static void medianBlur(Mat src, Mat dst, int ksize)
    {
        
        medianBlur_0(src.nativeObj, dst.nativeObj, ksize);
        
        return;
    }


    //
    // C++:  void cv::minEnclosingCircle(vector_Point2f points, Point2f& center, float& radius)
    //

    //javadoc: minEnclosingCircle(points, center, radius)
    public static void minEnclosingCircle(MatOfPoint2f points, Point center, float[] radius)
    {
        Mat points_mat = points;
        double[] center_out = new double[2];
        double[] radius_out = new double[1];
        minEnclosingCircle_0(points_mat.nativeObj, center_out, radius_out);
        if(center!=null){ center.x = center_out[0]; center.y = center_out[1]; } 
        if(radius!=null) radius[0] = (float)radius_out[0];
        return;
    }


    //
    // C++:  void cv::morphologyEx(Mat src, Mat& dst, int op, Mat kernel, Point anchor = Point(-1,-1), int iterations = 1, int borderType = BORDER_CONSTANT, Scalar borderValue = morphologyDefaultBorderValue())
    //

    //javadoc: morphologyEx(src, dst, op, kernel, anchor, iterations, borderType, borderValue)
    public static void morphologyEx(Mat src, Mat dst, int op, Mat kernel, Point anchor, int iterations, int borderType, Scalar borderValue)
    {
        
        morphologyEx_0(src.nativeObj, dst.nativeObj, op, kernel.nativeObj, anchor.x, anchor.y, iterations, borderType, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
        
        return;
    }

    //javadoc: morphologyEx(src, dst, op, kernel, anchor, iterations, borderType)
    public static void morphologyEx(Mat src, Mat dst, int op, Mat kernel, Point anchor, int iterations, int borderType)
    {
        
        morphologyEx_1(src.nativeObj, dst.nativeObj, op, kernel.nativeObj, anchor.x, anchor.y, iterations, borderType);
        
        return;
    }

    //javadoc: morphologyEx(src, dst, op, kernel, anchor, iterations)
    public static void morphologyEx(Mat src, Mat dst, int op, Mat kernel, Point anchor, int iterations)
    {
        
        morphologyEx_2(src.nativeObj, dst.nativeObj, op, kernel.nativeObj, anchor.x, anchor.y, iterations);
        
        return;
    }

    //javadoc: morphologyEx(src, dst, op, kernel, anchor)
    public static void morphologyEx(Mat src, Mat dst, int op, Mat kernel, Point anchor)
    {
        
        morphologyEx_3(src.nativeObj, dst.nativeObj, op, kernel.nativeObj, anchor.x, anchor.y);
        
        return;
    }

    //javadoc: morphologyEx(src, dst, op, kernel)
    public static void morphologyEx(Mat src, Mat dst, int op, Mat kernel)
    {
        
        morphologyEx_4(src.nativeObj, dst.nativeObj, op, kernel.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::polylines(Mat& img, vector_vector_Point pts, bool isClosed, Scalar color, int thickness = 1, int lineType = LINE_8, int shift = 0)
    //

    //javadoc: polylines(img, pts, isClosed, color, thickness, lineType, shift)
    public static void polylines(Mat img, List<MatOfPoint> pts, boolean isClosed, Scalar color, int thickness, int lineType, int shift)
    {
        List<Mat> pts_tmplm = new ArrayList<Mat>((pts != null) ? pts.size() : 0);
        Mat pts_mat = Converters.vector_vector_Point_to_Mat(pts, pts_tmplm);
        polylines_0(img.nativeObj, pts_mat.nativeObj, isClosed, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
        
        return;
    }

    //javadoc: polylines(img, pts, isClosed, color, thickness, lineType)
    public static void polylines(Mat img, List<MatOfPoint> pts, boolean isClosed, Scalar color, int thickness, int lineType)
    {
        List<Mat> pts_tmplm = new ArrayList<Mat>((pts != null) ? pts.size() : 0);
        Mat pts_mat = Converters.vector_vector_Point_to_Mat(pts, pts_tmplm);
        polylines_1(img.nativeObj, pts_mat.nativeObj, isClosed, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType);
        
        return;
    }

    //javadoc: polylines(img, pts, isClosed, color, thickness)
    public static void polylines(Mat img, List<MatOfPoint> pts, boolean isClosed, Scalar color, int thickness)
    {
        List<Mat> pts_tmplm = new ArrayList<Mat>((pts != null) ? pts.size() : 0);
        Mat pts_mat = Converters.vector_vector_Point_to_Mat(pts, pts_tmplm);
        polylines_2(img.nativeObj, pts_mat.nativeObj, isClosed, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
        
        return;
    }

    //javadoc: polylines(img, pts, isClosed, color)
    public static void polylines(Mat img, List<MatOfPoint> pts, boolean isClosed, Scalar color)
    {
        List<Mat> pts_tmplm = new ArrayList<Mat>((pts != null) ? pts.size() : 0);
        Mat pts_mat = Converters.vector_vector_Point_to_Mat(pts, pts_tmplm);
        polylines_3(img.nativeObj, pts_mat.nativeObj, isClosed, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::preCornerDetect(Mat src, Mat& dst, int ksize, int borderType = BORDER_DEFAULT)
    //

    //javadoc: preCornerDetect(src, dst, ksize, borderType)
    public static void preCornerDetect(Mat src, Mat dst, int ksize, int borderType)
    {
        
        preCornerDetect_0(src.nativeObj, dst.nativeObj, ksize, borderType);
        
        return;
    }

    //javadoc: preCornerDetect(src, dst, ksize)
    public static void preCornerDetect(Mat src, Mat dst, int ksize)
    {
        
        preCornerDetect_1(src.nativeObj, dst.nativeObj, ksize);
        
        return;
    }


    //
    // C++:  void cv::putText(Mat& img, String text, Point org, int fontFace, double fontScale, Scalar color, int thickness = 1, int lineType = LINE_8, bool bottomLeftOrigin = false)
    //

    //javadoc: putText(img, text, org, fontFace, fontScale, color, thickness, lineType, bottomLeftOrigin)
    public static void putText(Mat img, String text, Point org, int fontFace, double fontScale, Scalar color, int thickness, int lineType, boolean bottomLeftOrigin)
    {
        
        putText_0(img.nativeObj, text, org.x, org.y, fontFace, fontScale, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, bottomLeftOrigin);
        
        return;
    }

    //javadoc: putText(img, text, org, fontFace, fontScale, color, thickness, lineType)
    public static void putText(Mat img, String text, Point org, int fontFace, double fontScale, Scalar color, int thickness, int lineType)
    {
        
        putText_1(img.nativeObj, text, org.x, org.y, fontFace, fontScale, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType);
        
        return;
    }

    //javadoc: putText(img, text, org, fontFace, fontScale, color, thickness)
    public static void putText(Mat img, String text, Point org, int fontFace, double fontScale, Scalar color, int thickness)
    {
        
        putText_2(img.nativeObj, text, org.x, org.y, fontFace, fontScale, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
        
        return;
    }

    //javadoc: putText(img, text, org, fontFace, fontScale, color)
    public static void putText(Mat img, String text, Point org, int fontFace, double fontScale, Scalar color)
    {
        
        putText_3(img.nativeObj, text, org.x, org.y, fontFace, fontScale, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::pyrDown(Mat src, Mat& dst, Size dstsize = Size(), int borderType = BORDER_DEFAULT)
    //

    //javadoc: pyrDown(src, dst, dstsize, borderType)
    public static void pyrDown(Mat src, Mat dst, Size dstsize, int borderType)
    {
        
        pyrDown_0(src.nativeObj, dst.nativeObj, dstsize.width, dstsize.height, borderType);
        
        return;
    }

    //javadoc: pyrDown(src, dst, dstsize)
    public static void pyrDown(Mat src, Mat dst, Size dstsize)
    {
        
        pyrDown_1(src.nativeObj, dst.nativeObj, dstsize.width, dstsize.height);
        
        return;
    }

    //javadoc: pyrDown(src, dst)
    public static void pyrDown(Mat src, Mat dst)
    {
        
        pyrDown_2(src.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::pyrMeanShiftFiltering(Mat src, Mat& dst, double sp, double sr, int maxLevel = 1, TermCriteria termcrit = TermCriteria(TermCriteria::MAX_ITER+TermCriteria::EPS,5,1))
    //

    //javadoc: pyrMeanShiftFiltering(src, dst, sp, sr, maxLevel, termcrit)
    public static void pyrMeanShiftFiltering(Mat src, Mat dst, double sp, double sr, int maxLevel, TermCriteria termcrit)
    {
        
        pyrMeanShiftFiltering_0(src.nativeObj, dst.nativeObj, sp, sr, maxLevel, termcrit.type, termcrit.maxCount, termcrit.epsilon);
        
        return;
    }

    //javadoc: pyrMeanShiftFiltering(src, dst, sp, sr, maxLevel)
    public static void pyrMeanShiftFiltering(Mat src, Mat dst, double sp, double sr, int maxLevel)
    {
        
        pyrMeanShiftFiltering_1(src.nativeObj, dst.nativeObj, sp, sr, maxLevel);
        
        return;
    }

    //javadoc: pyrMeanShiftFiltering(src, dst, sp, sr)
    public static void pyrMeanShiftFiltering(Mat src, Mat dst, double sp, double sr)
    {
        
        pyrMeanShiftFiltering_2(src.nativeObj, dst.nativeObj, sp, sr);
        
        return;
    }


    //
    // C++:  void cv::pyrUp(Mat src, Mat& dst, Size dstsize = Size(), int borderType = BORDER_DEFAULT)
    //

    //javadoc: pyrUp(src, dst, dstsize, borderType)
    public static void pyrUp(Mat src, Mat dst, Size dstsize, int borderType)
    {
        
        pyrUp_0(src.nativeObj, dst.nativeObj, dstsize.width, dstsize.height, borderType);
        
        return;
    }

    //javadoc: pyrUp(src, dst, dstsize)
    public static void pyrUp(Mat src, Mat dst, Size dstsize)
    {
        
        pyrUp_1(src.nativeObj, dst.nativeObj, dstsize.width, dstsize.height);
        
        return;
    }

    //javadoc: pyrUp(src, dst)
    public static void pyrUp(Mat src, Mat dst)
    {
        
        pyrUp_2(src.nativeObj, dst.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::rectangle(Mat& img, Point pt1, Point pt2, Scalar color, int thickness = 1, int lineType = LINE_8, int shift = 0)
    //

    //javadoc: rectangle(img, pt1, pt2, color, thickness, lineType, shift)
    public static void rectangle(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int lineType, int shift)
    {
        
        rectangle_0(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
        
        return;
    }

    //javadoc: rectangle(img, pt1, pt2, color, thickness, lineType)
    public static void rectangle(Mat img, Point pt1, Point pt2, Scalar color, int thickness, int lineType)
    {
        
        rectangle_1(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType);
        
        return;
    }

    //javadoc: rectangle(img, pt1, pt2, color, thickness)
    public static void rectangle(Mat img, Point pt1, Point pt2, Scalar color, int thickness)
    {
        
        rectangle_2(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
        
        return;
    }

    //javadoc: rectangle(img, pt1, pt2, color)
    public static void rectangle(Mat img, Point pt1, Point pt2, Scalar color)
    {
        
        rectangle_3(img.nativeObj, pt1.x, pt1.y, pt2.x, pt2.y, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::rectangle(Mat& img, Rect rec, Scalar color, int thickness = 1, int lineType = LINE_8, int shift = 0)
    //

    //javadoc: rectangle(img, rec, color, thickness, lineType, shift)
    public static void rectangle(Mat img, Rect rec, Scalar color, int thickness, int lineType, int shift)
    {
        
        rectangle_4(img.nativeObj, rec.x, rec.y, rec.width, rec.height, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType, shift);
        
        return;
    }

    //javadoc: rectangle(img, rec, color, thickness, lineType)
    public static void rectangle(Mat img, Rect rec, Scalar color, int thickness, int lineType)
    {
        
        rectangle_5(img.nativeObj, rec.x, rec.y, rec.width, rec.height, color.val[0], color.val[1], color.val[2], color.val[3], thickness, lineType);
        
        return;
    }

    //javadoc: rectangle(img, rec, color, thickness)
    public static void rectangle(Mat img, Rect rec, Scalar color, int thickness)
    {
        
        rectangle_6(img.nativeObj, rec.x, rec.y, rec.width, rec.height, color.val[0], color.val[1], color.val[2], color.val[3], thickness);
        
        return;
    }

    //javadoc: rectangle(img, rec, color)
    public static void rectangle(Mat img, Rect rec, Scalar color)
    {
        
        rectangle_7(img.nativeObj, rec.x, rec.y, rec.width, rec.height, color.val[0], color.val[1], color.val[2], color.val[3]);
        
        return;
    }


    //
    // C++:  void cv::remap(Mat src, Mat& dst, Mat map1, Mat map2, int interpolation, int borderMode = BORDER_CONSTANT, Scalar borderValue = Scalar())
    //

    //javadoc: remap(src, dst, map1, map2, interpolation, borderMode, borderValue)
    public static void remap(Mat src, Mat dst, Mat map1, Mat map2, int interpolation, int borderMode, Scalar borderValue)
    {
        
        remap_0(src.nativeObj, dst.nativeObj, map1.nativeObj, map2.nativeObj, interpolation, borderMode, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
        
        return;
    }

    //javadoc: remap(src, dst, map1, map2, interpolation, borderMode)
    public static void remap(Mat src, Mat dst, Mat map1, Mat map2, int interpolation, int borderMode)
    {
        
        remap_1(src.nativeObj, dst.nativeObj, map1.nativeObj, map2.nativeObj, interpolation, borderMode);
        
        return;
    }

    //javadoc: remap(src, dst, map1, map2, interpolation)
    public static void remap(Mat src, Mat dst, Mat map1, Mat map2, int interpolation)
    {
        
        remap_2(src.nativeObj, dst.nativeObj, map1.nativeObj, map2.nativeObj, interpolation);
        
        return;
    }


    //
    // C++:  void cv::resize(Mat src, Mat& dst, Size dsize, double fx = 0, double fy = 0, int interpolation = INTER_LINEAR)
    //

    //javadoc: resize(src, dst, dsize, fx, fy, interpolation)
    public static void resize(Mat src, Mat dst, Size dsize, double fx, double fy, int interpolation)
    {
        
        resize_0(src.nativeObj, dst.nativeObj, dsize.width, dsize.height, fx, fy, interpolation);
        
        return;
    }

    //javadoc: resize(src, dst, dsize, fx, fy)
    public static void resize(Mat src, Mat dst, Size dsize, double fx, double fy)
    {
        
        resize_1(src.nativeObj, dst.nativeObj, dsize.width, dsize.height, fx, fy);
        
        return;
    }

    //javadoc: resize(src, dst, dsize, fx)
    public static void resize(Mat src, Mat dst, Size dsize, double fx)
    {
        
        resize_2(src.nativeObj, dst.nativeObj, dsize.width, dsize.height, fx);
        
        return;
    }

    //javadoc: resize(src, dst, dsize)
    public static void resize(Mat src, Mat dst, Size dsize)
    {
        
        resize_3(src.nativeObj, dst.nativeObj, dsize.width, dsize.height);
        
        return;
    }


    //
    // C++:  void cv::sepFilter2D(Mat src, Mat& dst, int ddepth, Mat kernelX, Mat kernelY, Point anchor = Point(-1,-1), double delta = 0, int borderType = BORDER_DEFAULT)
    //

    //javadoc: sepFilter2D(src, dst, ddepth, kernelX, kernelY, anchor, delta, borderType)
    public static void sepFilter2D(Mat src, Mat dst, int ddepth, Mat kernelX, Mat kernelY, Point anchor, double delta, int borderType)
    {
        
        sepFilter2D_0(src.nativeObj, dst.nativeObj, ddepth, kernelX.nativeObj, kernelY.nativeObj, anchor.x, anchor.y, delta, borderType);
        
        return;
    }

    //javadoc: sepFilter2D(src, dst, ddepth, kernelX, kernelY, anchor, delta)
    public static void sepFilter2D(Mat src, Mat dst, int ddepth, Mat kernelX, Mat kernelY, Point anchor, double delta)
    {
        
        sepFilter2D_1(src.nativeObj, dst.nativeObj, ddepth, kernelX.nativeObj, kernelY.nativeObj, anchor.x, anchor.y, delta);
        
        return;
    }

    //javadoc: sepFilter2D(src, dst, ddepth, kernelX, kernelY, anchor)
    public static void sepFilter2D(Mat src, Mat dst, int ddepth, Mat kernelX, Mat kernelY, Point anchor)
    {
        
        sepFilter2D_2(src.nativeObj, dst.nativeObj, ddepth, kernelX.nativeObj, kernelY.nativeObj, anchor.x, anchor.y);
        
        return;
    }

    //javadoc: sepFilter2D(src, dst, ddepth, kernelX, kernelY)
    public static void sepFilter2D(Mat src, Mat dst, int ddepth, Mat kernelX, Mat kernelY)
    {
        
        sepFilter2D_3(src.nativeObj, dst.nativeObj, ddepth, kernelX.nativeObj, kernelY.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::spatialGradient(Mat src, Mat& dx, Mat& dy, int ksize = 3, int borderType = BORDER_DEFAULT)
    //

    //javadoc: spatialGradient(src, dx, dy, ksize, borderType)
    public static void spatialGradient(Mat src, Mat dx, Mat dy, int ksize, int borderType)
    {
        
        spatialGradient_0(src.nativeObj, dx.nativeObj, dy.nativeObj, ksize, borderType);
        
        return;
    }

    //javadoc: spatialGradient(src, dx, dy, ksize)
    public static void spatialGradient(Mat src, Mat dx, Mat dy, int ksize)
    {
        
        spatialGradient_1(src.nativeObj, dx.nativeObj, dy.nativeObj, ksize);
        
        return;
    }

    //javadoc: spatialGradient(src, dx, dy)
    public static void spatialGradient(Mat src, Mat dx, Mat dy)
    {
        
        spatialGradient_2(src.nativeObj, dx.nativeObj, dy.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::sqrBoxFilter(Mat src, Mat& dst, int ddepth, Size ksize, Point anchor = Point(-1, -1), bool normalize = true, int borderType = BORDER_DEFAULT)
    //

    //javadoc: sqrBoxFilter(src, dst, ddepth, ksize, anchor, normalize, borderType)
    public static void sqrBoxFilter(Mat src, Mat dst, int ddepth, Size ksize, Point anchor, boolean normalize, int borderType)
    {
        
        sqrBoxFilter_0(src.nativeObj, dst.nativeObj, ddepth, ksize.width, ksize.height, anchor.x, anchor.y, normalize, borderType);
        
        return;
    }

    //javadoc: sqrBoxFilter(src, dst, ddepth, ksize, anchor, normalize)
    public static void sqrBoxFilter(Mat src, Mat dst, int ddepth, Size ksize, Point anchor, boolean normalize)
    {
        
        sqrBoxFilter_1(src.nativeObj, dst.nativeObj, ddepth, ksize.width, ksize.height, anchor.x, anchor.y, normalize);
        
        return;
    }

    //javadoc: sqrBoxFilter(src, dst, ddepth, ksize, anchor)
    public static void sqrBoxFilter(Mat src, Mat dst, int ddepth, Size ksize, Point anchor)
    {
        
        sqrBoxFilter_2(src.nativeObj, dst.nativeObj, ddepth, ksize.width, ksize.height, anchor.x, anchor.y);
        
        return;
    }

    //javadoc: sqrBoxFilter(src, dst, ddepth, ksize)
    public static void sqrBoxFilter(Mat src, Mat dst, int ddepth, Size ksize)
    {
        
        sqrBoxFilter_3(src.nativeObj, dst.nativeObj, ddepth, ksize.width, ksize.height);
        
        return;
    }


    //
    // C++:  void cv::warpAffine(Mat src, Mat& dst, Mat M, Size dsize, int flags = INTER_LINEAR, int borderMode = BORDER_CONSTANT, Scalar borderValue = Scalar())
    //

    //javadoc: warpAffine(src, dst, M, dsize, flags, borderMode, borderValue)
    public static void warpAffine(Mat src, Mat dst, Mat M, Size dsize, int flags, int borderMode, Scalar borderValue)
    {
        
        warpAffine_0(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags, borderMode, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
        
        return;
    }

    //javadoc: warpAffine(src, dst, M, dsize, flags, borderMode)
    public static void warpAffine(Mat src, Mat dst, Mat M, Size dsize, int flags, int borderMode)
    {
        
        warpAffine_1(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags, borderMode);
        
        return;
    }

    //javadoc: warpAffine(src, dst, M, dsize, flags)
    public static void warpAffine(Mat src, Mat dst, Mat M, Size dsize, int flags)
    {
        
        warpAffine_2(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags);
        
        return;
    }

    //javadoc: warpAffine(src, dst, M, dsize)
    public static void warpAffine(Mat src, Mat dst, Mat M, Size dsize)
    {
        
        warpAffine_3(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height);
        
        return;
    }


    //
    // C++:  void cv::warpPerspective(Mat src, Mat& dst, Mat M, Size dsize, int flags = INTER_LINEAR, int borderMode = BORDER_CONSTANT, Scalar borderValue = Scalar())
    //

    //javadoc: warpPerspective(src, dst, M, dsize, flags, borderMode, borderValue)
    public static void warpPerspective(Mat src, Mat dst, Mat M, Size dsize, int flags, int borderMode, Scalar borderValue)
    {
        
        warpPerspective_0(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags, borderMode, borderValue.val[0], borderValue.val[1], borderValue.val[2], borderValue.val[3]);
        
        return;
    }

    //javadoc: warpPerspective(src, dst, M, dsize, flags, borderMode)
    public static void warpPerspective(Mat src, Mat dst, Mat M, Size dsize, int flags, int borderMode)
    {
        
        warpPerspective_1(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags, borderMode);
        
        return;
    }

    //javadoc: warpPerspective(src, dst, M, dsize, flags)
    public static void warpPerspective(Mat src, Mat dst, Mat M, Size dsize, int flags)
    {
        
        warpPerspective_2(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height, flags);
        
        return;
    }

    //javadoc: warpPerspective(src, dst, M, dsize)
    public static void warpPerspective(Mat src, Mat dst, Mat M, Size dsize)
    {
        
        warpPerspective_3(src.nativeObj, dst.nativeObj, M.nativeObj, dsize.width, dsize.height);
        
        return;
    }


    //
    // C++:  void cv::warpPolar(Mat src, Mat& dst, Size dsize, Point2f center, double maxRadius, int flags)
    //

    //javadoc: warpPolar(src, dst, dsize, center, maxRadius, flags)
    public static void warpPolar(Mat src, Mat dst, Size dsize, Point center, double maxRadius, int flags)
    {
        
        warpPolar_0(src.nativeObj, dst.nativeObj, dsize.width, dsize.height, center.x, center.y, maxRadius, flags);
        
        return;
    }


    //
    // C++:  void cv::watershed(Mat image, Mat& markers)
    //

    //javadoc: watershed(image, markers)
    public static void watershed(Mat image, Mat markers)
    {
        
        watershed_0(image.nativeObj, markers.nativeObj);
        
        return;
    }



// C++: Size getTextSize(const String& text, int fontFace, double fontScale, int thickness, int* baseLine);
//javadoc:getTextSize(text, fontFace, fontScale, thickness, baseLine)
public static Size getTextSize(String text, int fontFace, double fontScale, int thickness, int[] baseLine) {
    if(baseLine != null && baseLine.length != 1)
        throw new java.lang.IllegalArgumentException("'baseLine' must be 'int[1]' or 'null'.");
    Size retVal = new Size(n_getTextSize(text, fontFace, fontScale, thickness, baseLine));
    return retVal;
}




    // C++:  Mat cv::getAffineTransform(vector_Point2f src, vector_Point2f dst)
    private static native long getAffineTransform_0(long src_mat_nativeObj, long dst_mat_nativeObj);

    // C++:  Mat cv::getGaborKernel(Size ksize, double sigma, double theta, double lambd, double gamma, double psi = CV_PI*0.5, int ktype = CV_64F)
    private static native long getGaborKernel_0(double ksize_width, double ksize_height, double sigma, double theta, double lambd, double gamma, double psi, int ktype);
    private static native long getGaborKernel_1(double ksize_width, double ksize_height, double sigma, double theta, double lambd, double gamma, double psi);
    private static native long getGaborKernel_2(double ksize_width, double ksize_height, double sigma, double theta, double lambd, double gamma);

    // C++:  Mat cv::getGaussianKernel(int ksize, double sigma, int ktype = CV_64F)
    private static native long getGaussianKernel_0(int ksize, double sigma, int ktype);
    private static native long getGaussianKernel_1(int ksize, double sigma);

    // C++:  Mat cv::getPerspectiveTransform(Mat src, Mat dst, int solveMethod = DECOMP_LU)
    private static native long getPerspectiveTransform_0(long src_nativeObj, long dst_nativeObj, int solveMethod);
    private static native long getPerspectiveTransform_1(long src_nativeObj, long dst_nativeObj);

    // C++:  Mat cv::getRotationMatrix2D(Point2f center, double angle, double scale)
    private static native long getRotationMatrix2D_0(double center_x, double center_y, double angle, double scale);

    // C++:  Mat cv::getStructuringElement(int shape, Size ksize, Point anchor = Point(-1,-1))
    private static native long getStructuringElement_0(int shape, double ksize_width, double ksize_height, double anchor_x, double anchor_y);
    private static native long getStructuringElement_1(int shape, double ksize_width, double ksize_height);

    // C++:  Moments cv::moments(Mat array, bool binaryImage = false)
    private static native double[] moments_0(long array_nativeObj, boolean binaryImage);
    private static native double[] moments_1(long array_nativeObj);

    // C++:  Point2d cv::phaseCorrelate(Mat src1, Mat src2, Mat window = Mat(), double* response = 0)
    private static native double[] phaseCorrelate_0(long src1_nativeObj, long src2_nativeObj, long window_nativeObj, double[] response_out);
    private static native double[] phaseCorrelate_1(long src1_nativeObj, long src2_nativeObj, long window_nativeObj);
    private static native double[] phaseCorrelate_2(long src1_nativeObj, long src2_nativeObj);

    // C++:  Ptr_CLAHE cv::createCLAHE(double clipLimit = 40.0, Size tileGridSize = Size(8, 8))
    private static native long createCLAHE_0(double clipLimit, double tileGridSize_width, double tileGridSize_height);
    private static native long createCLAHE_1(double clipLimit);
    private static native long createCLAHE_2();

    // C++:  Ptr_LineSegmentDetector cv::createLineSegmentDetector(int _refine = LSD_REFINE_STD, double _scale = 0.8, double _sigma_scale = 0.6, double _quant = 2.0, double _ang_th = 22.5, double _log_eps = 0, double _density_th = 0.7, int _n_bins = 1024)
    private static native long createLineSegmentDetector_0(int _refine, double _scale, double _sigma_scale, double _quant, double _ang_th, double _log_eps, double _density_th, int _n_bins);
    private static native long createLineSegmentDetector_1(int _refine, double _scale, double _sigma_scale, double _quant, double _ang_th, double _log_eps, double _density_th);
    private static native long createLineSegmentDetector_2(int _refine, double _scale, double _sigma_scale, double _quant, double _ang_th, double _log_eps);
    private static native long createLineSegmentDetector_3(int _refine, double _scale, double _sigma_scale, double _quant, double _ang_th);
    private static native long createLineSegmentDetector_4(int _refine, double _scale, double _sigma_scale, double _quant);
    private static native long createLineSegmentDetector_5(int _refine, double _scale, double _sigma_scale);
    private static native long createLineSegmentDetector_6(int _refine, double _scale);
    private static native long createLineSegmentDetector_7(int _refine);
    private static native long createLineSegmentDetector_8();

    // C++:  Rect cv::boundingRect(Mat array)
    private static native double[] boundingRect_0(long array_nativeObj);

    // C++:  RotatedRect cv::fitEllipse(vector_Point2f points)
    private static native double[] fitEllipse_0(long points_mat_nativeObj);

    // C++:  RotatedRect cv::fitEllipseAMS(Mat points)
    private static native double[] fitEllipseAMS_0(long points_nativeObj);

    // C++:  RotatedRect cv::fitEllipseDirect(Mat points)
    private static native double[] fitEllipseDirect_0(long points_nativeObj);

    // C++:  RotatedRect cv::minAreaRect(vector_Point2f points)
    private static native double[] minAreaRect_0(long points_mat_nativeObj);

    // C++:  bool cv::clipLine(Rect imgRect, Point& pt1, Point& pt2)
    private static native boolean clipLine_0(int imgRect_x, int imgRect_y, int imgRect_width, int imgRect_height, double pt1_x, double pt1_y, double[] pt1_out, double pt2_x, double pt2_y, double[] pt2_out);

    // C++:  bool cv::isContourConvex(vector_Point contour)
    private static native boolean isContourConvex_0(long contour_mat_nativeObj);

    // C++:  double cv::arcLength(vector_Point2f curve, bool closed)
    private static native double arcLength_0(long curve_mat_nativeObj, boolean closed);

    // C++:  double cv::compareHist(Mat H1, Mat H2, int method)
    private static native double compareHist_0(long H1_nativeObj, long H2_nativeObj, int method);

    // C++:  double cv::contourArea(Mat contour, bool oriented = false)
    private static native double contourArea_0(long contour_nativeObj, boolean oriented);
    private static native double contourArea_1(long contour_nativeObj);

    // C++:  double cv::getFontScaleFromHeight(int fontFace, int pixelHeight, int thickness = 1)
    private static native double getFontScaleFromHeight_0(int fontFace, int pixelHeight, int thickness);
    private static native double getFontScaleFromHeight_1(int fontFace, int pixelHeight);

    // C++:  double cv::matchShapes(Mat contour1, Mat contour2, int method, double parameter)
    private static native double matchShapes_0(long contour1_nativeObj, long contour2_nativeObj, int method, double parameter);

    // C++:  double cv::minEnclosingTriangle(Mat points, Mat& triangle)
    private static native double minEnclosingTriangle_0(long points_nativeObj, long triangle_nativeObj);

    // C++:  double cv::pointPolygonTest(vector_Point2f contour, Point2f pt, bool measureDist)
    private static native double pointPolygonTest_0(long contour_mat_nativeObj, double pt_x, double pt_y, boolean measureDist);

    // C++:  double cv::threshold(Mat src, Mat& dst, double thresh, double maxval, int type)
    private static native double threshold_0(long src_nativeObj, long dst_nativeObj, double thresh, double maxval, int type);

    // C++:  float cv::intersectConvexConvex(Mat _p1, Mat _p2, Mat& _p12, bool handleNested = true)
    private static native float intersectConvexConvex_0(long _p1_nativeObj, long _p2_nativeObj, long _p12_nativeObj, boolean handleNested);
    private static native float intersectConvexConvex_1(long _p1_nativeObj, long _p2_nativeObj, long _p12_nativeObj);

    // C++:  float cv::wrapperEMD(Mat signature1, Mat signature2, int distType, Mat cost = Mat(), Ptr_float& lowerBound = Ptr<float>(), Mat& flow = Mat())
    private static native float EMD_0(long signature1_nativeObj, long signature2_nativeObj, int distType, long cost_nativeObj, long flow_nativeObj);
    private static native float EMD_1(long signature1_nativeObj, long signature2_nativeObj, int distType, long cost_nativeObj);
    private static native float EMD_3(long signature1_nativeObj, long signature2_nativeObj, int distType);

    // C++:  int cv::connectedComponents(Mat image, Mat& labels, int connectivity, int ltype, int ccltype)
    private static native int connectedComponentsWithAlgorithm_0(long image_nativeObj, long labels_nativeObj, int connectivity, int ltype, int ccltype);

    // C++:  int cv::connectedComponents(Mat image, Mat& labels, int connectivity = 8, int ltype = CV_32S)
    private static native int connectedComponents_0(long image_nativeObj, long labels_nativeObj, int connectivity, int ltype);
    private static native int connectedComponents_1(long image_nativeObj, long labels_nativeObj, int connectivity);
    private static native int connectedComponents_2(long image_nativeObj, long labels_nativeObj);

    // C++:  int cv::connectedComponentsWithStats(Mat image, Mat& labels, Mat& stats, Mat& centroids, int connectivity, int ltype, int ccltype)
    private static native int connectedComponentsWithStatsWithAlgorithm_0(long image_nativeObj, long labels_nativeObj, long stats_nativeObj, long centroids_nativeObj, int connectivity, int ltype, int ccltype);

    // C++:  int cv::connectedComponentsWithStats(Mat image, Mat& labels, Mat& stats, Mat& centroids, int connectivity = 8, int ltype = CV_32S)
    private static native int connectedComponentsWithStats_0(long image_nativeObj, long labels_nativeObj, long stats_nativeObj, long centroids_nativeObj, int connectivity, int ltype);
    private static native int connectedComponentsWithStats_1(long image_nativeObj, long labels_nativeObj, long stats_nativeObj, long centroids_nativeObj, int connectivity);
    private static native int connectedComponentsWithStats_2(long image_nativeObj, long labels_nativeObj, long stats_nativeObj, long centroids_nativeObj);

    // C++:  int cv::floodFill(Mat& image, Mat& mask, Point seedPoint, Scalar newVal, Rect* rect = 0, Scalar loDiff = Scalar(), Scalar upDiff = Scalar(), int flags = 4)
    private static native int floodFill_0(long image_nativeObj, long mask_nativeObj, double seedPoint_x, double seedPoint_y, double newVal_val0, double newVal_val1, double newVal_val2, double newVal_val3, double[] rect_out, double loDiff_val0, double loDiff_val1, double loDiff_val2, double loDiff_val3, double upDiff_val0, double upDiff_val1, double upDiff_val2, double upDiff_val3, int flags);
    private static native int floodFill_1(long image_nativeObj, long mask_nativeObj, double seedPoint_x, double seedPoint_y, double newVal_val0, double newVal_val1, double newVal_val2, double newVal_val3, double[] rect_out, double loDiff_val0, double loDiff_val1, double loDiff_val2, double loDiff_val3, double upDiff_val0, double upDiff_val1, double upDiff_val2, double upDiff_val3);
    private static native int floodFill_2(long image_nativeObj, long mask_nativeObj, double seedPoint_x, double seedPoint_y, double newVal_val0, double newVal_val1, double newVal_val2, double newVal_val3, double[] rect_out, double loDiff_val0, double loDiff_val1, double loDiff_val2, double loDiff_val3);
    private static native int floodFill_3(long image_nativeObj, long mask_nativeObj, double seedPoint_x, double seedPoint_y, double newVal_val0, double newVal_val1, double newVal_val2, double newVal_val3, double[] rect_out);
    private static native int floodFill_4(long image_nativeObj, long mask_nativeObj, double seedPoint_x, double seedPoint_y, double newVal_val0, double newVal_val1, double newVal_val2, double newVal_val3);

    // C++:  int cv::rotatedRectangleIntersection(RotatedRect rect1, RotatedRect rect2, Mat& intersectingRegion)
    private static native int rotatedRectangleIntersection_0(double rect1_center_x, double rect1_center_y, double rect1_size_width, double rect1_size_height, double rect1_angle, double rect2_center_x, double rect2_center_y, double rect2_size_width, double rect2_size_height, double rect2_angle, long intersectingRegion_nativeObj);

    // C++:  void cv::Canny(Mat dx, Mat dy, Mat& edges, double threshold1, double threshold2, bool L2gradient = false)
    private static native void Canny_0(long dx_nativeObj, long dy_nativeObj, long edges_nativeObj, double threshold1, double threshold2, boolean L2gradient);
    private static native void Canny_1(long dx_nativeObj, long dy_nativeObj, long edges_nativeObj, double threshold1, double threshold2);

    // C++:  void cv::Canny(Mat image, Mat& edges, double threshold1, double threshold2, int apertureSize = 3, bool L2gradient = false)
    private static native void Canny_2(long image_nativeObj, long edges_nativeObj, double threshold1, double threshold2, int apertureSize, boolean L2gradient);
    private static native void Canny_3(long image_nativeObj, long edges_nativeObj, double threshold1, double threshold2, int apertureSize);
    private static native void Canny_4(long image_nativeObj, long edges_nativeObj, double threshold1, double threshold2);

    // C++:  void cv::GaussianBlur(Mat src, Mat& dst, Size ksize, double sigmaX, double sigmaY = 0, int borderType = BORDER_DEFAULT)
    private static native void GaussianBlur_0(long src_nativeObj, long dst_nativeObj, double ksize_width, double ksize_height, double sigmaX, double sigmaY, int borderType);
    private static native void GaussianBlur_1(long src_nativeObj, long dst_nativeObj, double ksize_width, double ksize_height, double sigmaX, double sigmaY);
    private static native void GaussianBlur_2(long src_nativeObj, long dst_nativeObj, double ksize_width, double ksize_height, double sigmaX);

    // C++:  void cv::HoughCircles(Mat image, Mat& circles, int method, double dp, double minDist, double param1 = 100, double param2 = 100, int minRadius = 0, int maxRadius = 0)
    private static native void HoughCircles_0(long image_nativeObj, long circles_nativeObj, int method, double dp, double minDist, double param1, double param2, int minRadius, int maxRadius);
    private static native void HoughCircles_1(long image_nativeObj, long circles_nativeObj, int method, double dp, double minDist, double param1, double param2, int minRadius);
    private static native void HoughCircles_2(long image_nativeObj, long circles_nativeObj, int method, double dp, double minDist, double param1, double param2);
    private static native void HoughCircles_3(long image_nativeObj, long circles_nativeObj, int method, double dp, double minDist, double param1);
    private static native void HoughCircles_4(long image_nativeObj, long circles_nativeObj, int method, double dp, double minDist);

    // C++:  void cv::HoughLines(Mat image, Mat& lines, double rho, double theta, int threshold, double srn = 0, double stn = 0, double min_theta = 0, double max_theta = CV_PI)
    private static native void HoughLines_0(long image_nativeObj, long lines_nativeObj, double rho, double theta, int threshold, double srn, double stn, double min_theta, double max_theta);
    private static native void HoughLines_1(long image_nativeObj, long lines_nativeObj, double rho, double theta, int threshold, double srn, double stn, double min_theta);
    private static native void HoughLines_2(long image_nativeObj, long lines_nativeObj, double rho, double theta, int threshold, double srn, double stn);
    private static native void HoughLines_3(long image_nativeObj, long lines_nativeObj, double rho, double theta, int threshold, double srn);
    private static native void HoughLines_4(long image_nativeObj, long lines_nativeObj, double rho, double theta, int threshold);

    // C++:  void cv::HoughLinesP(Mat image, Mat& lines, double rho, double theta, int threshold, double minLineLength = 0, double maxLineGap = 0)
    private static native void HoughLinesP_0(long image_nativeObj, long lines_nativeObj, double rho, double theta, int threshold, double minLineLength, double maxLineGap);
    private static native void HoughLinesP_1(long image_nativeObj, long lines_nativeObj, double rho, double theta, int threshold, double minLineLength);
    private static native void HoughLinesP_2(long image_nativeObj, long lines_nativeObj, double rho, double theta, int threshold);

    // C++:  void cv::HoughLinesPointSet(Mat _point, Mat& _lines, int lines_max, int threshold, double min_rho, double max_rho, double rho_step, double min_theta, double max_theta, double theta_step)
    private static native void HoughLinesPointSet_0(long _point_nativeObj, long _lines_nativeObj, int lines_max, int threshold, double min_rho, double max_rho, double rho_step, double min_theta, double max_theta, double theta_step);

    // C++:  void cv::HuMoments(Moments m, Mat& hu)
    private static native void HuMoments_0(double m_m00, double m_m10, double m_m01, double m_m20, double m_m11, double m_m02, double m_m30, double m_m21, double m_m12, double m_m03, long hu_nativeObj);

    // C++:  void cv::Laplacian(Mat src, Mat& dst, int ddepth, int ksize = 1, double scale = 1, double delta = 0, int borderType = BORDER_DEFAULT)
    private static native void Laplacian_0(long src_nativeObj, long dst_nativeObj, int ddepth, int ksize, double scale, double delta, int borderType);
    private static native void Laplacian_1(long src_nativeObj, long dst_nativeObj, int ddepth, int ksize, double scale, double delta);
    private static native void Laplacian_2(long src_nativeObj, long dst_nativeObj, int ddepth, int ksize, double scale);
    private static native void Laplacian_3(long src_nativeObj, long dst_nativeObj, int ddepth, int ksize);
    private static native void Laplacian_4(long src_nativeObj, long dst_nativeObj, int ddepth);

    // C++:  void cv::Scharr(Mat src, Mat& dst, int ddepth, int dx, int dy, double scale = 1, double delta = 0, int borderType = BORDER_DEFAULT)
    private static native void Scharr_0(long src_nativeObj, long dst_nativeObj, int ddepth, int dx, int dy, double scale, double delta, int borderType);
    private static native void Scharr_1(long src_nativeObj, long dst_nativeObj, int ddepth, int dx, int dy, double scale, double delta);
    private static native void Scharr_2(long src_nativeObj, long dst_nativeObj, int ddepth, int dx, int dy, double scale);
    private static native void Scharr_3(long src_nativeObj, long dst_nativeObj, int ddepth, int dx, int dy);

    // C++:  void cv::Sobel(Mat src, Mat& dst, int ddepth, int dx, int dy, int ksize = 3, double scale = 1, double delta = 0, int borderType = BORDER_DEFAULT)
    private static native void Sobel_0(long src_nativeObj, long dst_nativeObj, int ddepth, int dx, int dy, int ksize, double scale, double delta, int borderType);
    private static native void Sobel_1(long src_nativeObj, long dst_nativeObj, int ddepth, int dx, int dy, int ksize, double scale, double delta);
    private static native void Sobel_2(long src_nativeObj, long dst_nativeObj, int ddepth, int dx, int dy, int ksize, double scale);
    private static native void Sobel_3(long src_nativeObj, long dst_nativeObj, int ddepth, int dx, int dy, int ksize);
    private static native void Sobel_4(long src_nativeObj, long dst_nativeObj, int ddepth, int dx, int dy);

    // C++:  void cv::accumulate(Mat src, Mat& dst, Mat mask = Mat())
    private static native void accumulate_0(long src_nativeObj, long dst_nativeObj, long mask_nativeObj);
    private static native void accumulate_1(long src_nativeObj, long dst_nativeObj);

    // C++:  void cv::accumulateProduct(Mat src1, Mat src2, Mat& dst, Mat mask = Mat())
    private static native void accumulateProduct_0(long src1_nativeObj, long src2_nativeObj, long dst_nativeObj, long mask_nativeObj);
    private static native void accumulateProduct_1(long src1_nativeObj, long src2_nativeObj, long dst_nativeObj);

    // C++:  void cv::accumulateSquare(Mat src, Mat& dst, Mat mask = Mat())
    private static native void accumulateSquare_0(long src_nativeObj, long dst_nativeObj, long mask_nativeObj);
    private static native void accumulateSquare_1(long src_nativeObj, long dst_nativeObj);

    // C++:  void cv::accumulateWeighted(Mat src, Mat& dst, double alpha, Mat mask = Mat())
    private static native void accumulateWeighted_0(long src_nativeObj, long dst_nativeObj, double alpha, long mask_nativeObj);
    private static native void accumulateWeighted_1(long src_nativeObj, long dst_nativeObj, double alpha);

    // C++:  void cv::adaptiveThreshold(Mat src, Mat& dst, double maxValue, int adaptiveMethod, int thresholdType, int blockSize, double C)
    private static native void adaptiveThreshold_0(long src_nativeObj, long dst_nativeObj, double maxValue, int adaptiveMethod, int thresholdType, int blockSize, double C);

    // C++:  void cv::applyColorMap(Mat src, Mat& dst, Mat userColor)
    private static native void applyColorMap_0(long src_nativeObj, long dst_nativeObj, long userColor_nativeObj);

    // C++:  void cv::applyColorMap(Mat src, Mat& dst, int colormap)
    private static native void applyColorMap_1(long src_nativeObj, long dst_nativeObj, int colormap);

    // C++:  void cv::approxPolyDP(vector_Point2f curve, vector_Point2f& approxCurve, double epsilon, bool closed)
    private static native void approxPolyDP_0(long curve_mat_nativeObj, long approxCurve_mat_nativeObj, double epsilon, boolean closed);

    // C++:  void cv::arrowedLine(Mat& img, Point pt1, Point pt2, Scalar color, int thickness = 1, int line_type = 8, int shift = 0, double tipLength = 0.1)
    private static native void arrowedLine_0(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int line_type, int shift, double tipLength);
    private static native void arrowedLine_1(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int line_type, int shift);
    private static native void arrowedLine_2(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int line_type);
    private static native void arrowedLine_3(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3, int thickness);
    private static native void arrowedLine_4(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::bilateralFilter(Mat src, Mat& dst, int d, double sigmaColor, double sigmaSpace, int borderType = BORDER_DEFAULT)
    private static native void bilateralFilter_0(long src_nativeObj, long dst_nativeObj, int d, double sigmaColor, double sigmaSpace, int borderType);
    private static native void bilateralFilter_1(long src_nativeObj, long dst_nativeObj, int d, double sigmaColor, double sigmaSpace);

    // C++:  void cv::blur(Mat src, Mat& dst, Size ksize, Point anchor = Point(-1,-1), int borderType = BORDER_DEFAULT)
    private static native void blur_0(long src_nativeObj, long dst_nativeObj, double ksize_width, double ksize_height, double anchor_x, double anchor_y, int borderType);
    private static native void blur_1(long src_nativeObj, long dst_nativeObj, double ksize_width, double ksize_height, double anchor_x, double anchor_y);
    private static native void blur_2(long src_nativeObj, long dst_nativeObj, double ksize_width, double ksize_height);

    // C++:  void cv::boxFilter(Mat src, Mat& dst, int ddepth, Size ksize, Point anchor = Point(-1,-1), bool normalize = true, int borderType = BORDER_DEFAULT)
    private static native void boxFilter_0(long src_nativeObj, long dst_nativeObj, int ddepth, double ksize_width, double ksize_height, double anchor_x, double anchor_y, boolean normalize, int borderType);
    private static native void boxFilter_1(long src_nativeObj, long dst_nativeObj, int ddepth, double ksize_width, double ksize_height, double anchor_x, double anchor_y, boolean normalize);
    private static native void boxFilter_2(long src_nativeObj, long dst_nativeObj, int ddepth, double ksize_width, double ksize_height, double anchor_x, double anchor_y);
    private static native void boxFilter_3(long src_nativeObj, long dst_nativeObj, int ddepth, double ksize_width, double ksize_height);

    // C++:  void cv::boxPoints(RotatedRect box, Mat& points)
    private static native void boxPoints_0(double box_center_x, double box_center_y, double box_size_width, double box_size_height, double box_angle, long points_nativeObj);

    // C++:  void cv::calcBackProject(vector_Mat images, vector_int channels, Mat hist, Mat& dst, vector_float ranges, double scale)
    private static native void calcBackProject_0(long images_mat_nativeObj, long channels_mat_nativeObj, long hist_nativeObj, long dst_nativeObj, long ranges_mat_nativeObj, double scale);

    // C++:  void cv::calcHist(vector_Mat images, vector_int channels, Mat mask, Mat& hist, vector_int histSize, vector_float ranges, bool accumulate = false)
    private static native void calcHist_0(long images_mat_nativeObj, long channels_mat_nativeObj, long mask_nativeObj, long hist_nativeObj, long histSize_mat_nativeObj, long ranges_mat_nativeObj, boolean accumulate);
    private static native void calcHist_1(long images_mat_nativeObj, long channels_mat_nativeObj, long mask_nativeObj, long hist_nativeObj, long histSize_mat_nativeObj, long ranges_mat_nativeObj);

    // C++:  void cv::circle(Mat& img, Point center, int radius, Scalar color, int thickness = 1, int lineType = LINE_8, int shift = 0)
    private static native void circle_0(long img_nativeObj, double center_x, double center_y, int radius, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType, int shift);
    private static native void circle_1(long img_nativeObj, double center_x, double center_y, int radius, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType);
    private static native void circle_2(long img_nativeObj, double center_x, double center_y, int radius, double color_val0, double color_val1, double color_val2, double color_val3, int thickness);
    private static native void circle_3(long img_nativeObj, double center_x, double center_y, int radius, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::convertMaps(Mat map1, Mat map2, Mat& dstmap1, Mat& dstmap2, int dstmap1type, bool nninterpolation = false)
    private static native void convertMaps_0(long map1_nativeObj, long map2_nativeObj, long dstmap1_nativeObj, long dstmap2_nativeObj, int dstmap1type, boolean nninterpolation);
    private static native void convertMaps_1(long map1_nativeObj, long map2_nativeObj, long dstmap1_nativeObj, long dstmap2_nativeObj, int dstmap1type);

    // C++:  void cv::convexHull(vector_Point points, vector_int& hull, bool clockwise = false,  _hidden_  returnPoints = true)
    private static native void convexHull_0(long points_mat_nativeObj, long hull_mat_nativeObj, boolean clockwise);
    private static native void convexHull_2(long points_mat_nativeObj, long hull_mat_nativeObj);

    // C++:  void cv::convexityDefects(vector_Point contour, vector_int convexhull, vector_Vec4i& convexityDefects)
    private static native void convexityDefects_0(long contour_mat_nativeObj, long convexhull_mat_nativeObj, long convexityDefects_mat_nativeObj);

    // C++:  void cv::cornerEigenValsAndVecs(Mat src, Mat& dst, int blockSize, int ksize, int borderType = BORDER_DEFAULT)
    private static native void cornerEigenValsAndVecs_0(long src_nativeObj, long dst_nativeObj, int blockSize, int ksize, int borderType);
    private static native void cornerEigenValsAndVecs_1(long src_nativeObj, long dst_nativeObj, int blockSize, int ksize);

    // C++:  void cv::cornerHarris(Mat src, Mat& dst, int blockSize, int ksize, double k, int borderType = BORDER_DEFAULT)
    private static native void cornerHarris_0(long src_nativeObj, long dst_nativeObj, int blockSize, int ksize, double k, int borderType);
    private static native void cornerHarris_1(long src_nativeObj, long dst_nativeObj, int blockSize, int ksize, double k);

    // C++:  void cv::cornerMinEigenVal(Mat src, Mat& dst, int blockSize, int ksize = 3, int borderType = BORDER_DEFAULT)
    private static native void cornerMinEigenVal_0(long src_nativeObj, long dst_nativeObj, int blockSize, int ksize, int borderType);
    private static native void cornerMinEigenVal_1(long src_nativeObj, long dst_nativeObj, int blockSize, int ksize);
    private static native void cornerMinEigenVal_2(long src_nativeObj, long dst_nativeObj, int blockSize);

    // C++:  void cv::cornerSubPix(Mat image, Mat& corners, Size winSize, Size zeroZone, TermCriteria criteria)
    private static native void cornerSubPix_0(long image_nativeObj, long corners_nativeObj, double winSize_width, double winSize_height, double zeroZone_width, double zeroZone_height, int criteria_type, int criteria_maxCount, double criteria_epsilon);

    // C++:  void cv::createHanningWindow(Mat& dst, Size winSize, int type)
    private static native void createHanningWindow_0(long dst_nativeObj, double winSize_width, double winSize_height, int type);

    // C++:  void cv::cvtColor(Mat src, Mat& dst, int code, int dstCn = 0)
    private static native void cvtColor_0(long src_nativeObj, long dst_nativeObj, int code, int dstCn);
    private static native void cvtColor_1(long src_nativeObj, long dst_nativeObj, int code);

    // C++:  void cv::cvtColorTwoPlane(Mat src1, Mat src2, Mat& dst, int code)
    private static native void cvtColorTwoPlane_0(long src1_nativeObj, long src2_nativeObj, long dst_nativeObj, int code);

    // C++:  void cv::demosaicing(Mat src, Mat& dst, int code, int dstCn = 0)
    private static native void demosaicing_0(long src_nativeObj, long dst_nativeObj, int code, int dstCn);
    private static native void demosaicing_1(long src_nativeObj, long dst_nativeObj, int code);

    // C++:  void cv::dilate(Mat src, Mat& dst, Mat kernel, Point anchor = Point(-1,-1), int iterations = 1, int borderType = BORDER_CONSTANT, Scalar borderValue = morphologyDefaultBorderValue())
    private static native void dilate_0(long src_nativeObj, long dst_nativeObj, long kernel_nativeObj, double anchor_x, double anchor_y, int iterations, int borderType, double borderValue_val0, double borderValue_val1, double borderValue_val2, double borderValue_val3);
    private static native void dilate_1(long src_nativeObj, long dst_nativeObj, long kernel_nativeObj, double anchor_x, double anchor_y, int iterations, int borderType);
    private static native void dilate_2(long src_nativeObj, long dst_nativeObj, long kernel_nativeObj, double anchor_x, double anchor_y, int iterations);
    private static native void dilate_3(long src_nativeObj, long dst_nativeObj, long kernel_nativeObj, double anchor_x, double anchor_y);
    private static native void dilate_4(long src_nativeObj, long dst_nativeObj, long kernel_nativeObj);

    // C++:  void cv::distanceTransform(Mat src, Mat& dst, Mat& labels, int distanceType, int maskSize, int labelType = DIST_LABEL_CCOMP)
    private static native void distanceTransformWithLabels_0(long src_nativeObj, long dst_nativeObj, long labels_nativeObj, int distanceType, int maskSize, int labelType);
    private static native void distanceTransformWithLabels_1(long src_nativeObj, long dst_nativeObj, long labels_nativeObj, int distanceType, int maskSize);

    // C++:  void cv::distanceTransform(Mat src, Mat& dst, int distanceType, int maskSize, int dstType = CV_32F)
    private static native void distanceTransform_0(long src_nativeObj, long dst_nativeObj, int distanceType, int maskSize, int dstType);
    private static native void distanceTransform_1(long src_nativeObj, long dst_nativeObj, int distanceType, int maskSize);

    // C++:  void cv::drawContours(Mat& image, vector_vector_Point contours, int contourIdx, Scalar color, int thickness = 1, int lineType = LINE_8, Mat hierarchy = Mat(), int maxLevel = INT_MAX, Point offset = Point())
    private static native void drawContours_0(long image_nativeObj, long contours_mat_nativeObj, int contourIdx, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType, long hierarchy_nativeObj, int maxLevel, double offset_x, double offset_y);
    private static native void drawContours_1(long image_nativeObj, long contours_mat_nativeObj, int contourIdx, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType, long hierarchy_nativeObj, int maxLevel);
    private static native void drawContours_2(long image_nativeObj, long contours_mat_nativeObj, int contourIdx, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType, long hierarchy_nativeObj);
    private static native void drawContours_3(long image_nativeObj, long contours_mat_nativeObj, int contourIdx, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType);
    private static native void drawContours_4(long image_nativeObj, long contours_mat_nativeObj, int contourIdx, double color_val0, double color_val1, double color_val2, double color_val3, int thickness);
    private static native void drawContours_5(long image_nativeObj, long contours_mat_nativeObj, int contourIdx, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::drawMarker(Mat& img, Point position, Scalar color, int markerType = MARKER_CROSS, int markerSize = 20, int thickness = 1, int line_type = 8)
    private static native void drawMarker_0(long img_nativeObj, double position_x, double position_y, double color_val0, double color_val1, double color_val2, double color_val3, int markerType, int markerSize, int thickness, int line_type);
    private static native void drawMarker_1(long img_nativeObj, double position_x, double position_y, double color_val0, double color_val1, double color_val2, double color_val3, int markerType, int markerSize, int thickness);
    private static native void drawMarker_2(long img_nativeObj, double position_x, double position_y, double color_val0, double color_val1, double color_val2, double color_val3, int markerType, int markerSize);
    private static native void drawMarker_3(long img_nativeObj, double position_x, double position_y, double color_val0, double color_val1, double color_val2, double color_val3, int markerType);
    private static native void drawMarker_4(long img_nativeObj, double position_x, double position_y, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::ellipse(Mat& img, Point center, Size axes, double angle, double startAngle, double endAngle, Scalar color, int thickness = 1, int lineType = LINE_8, int shift = 0)
    private static native void ellipse_0(long img_nativeObj, double center_x, double center_y, double axes_width, double axes_height, double angle, double startAngle, double endAngle, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType, int shift);
    private static native void ellipse_1(long img_nativeObj, double center_x, double center_y, double axes_width, double axes_height, double angle, double startAngle, double endAngle, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType);
    private static native void ellipse_2(long img_nativeObj, double center_x, double center_y, double axes_width, double axes_height, double angle, double startAngle, double endAngle, double color_val0, double color_val1, double color_val2, double color_val3, int thickness);
    private static native void ellipse_3(long img_nativeObj, double center_x, double center_y, double axes_width, double axes_height, double angle, double startAngle, double endAngle, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::ellipse(Mat& img, RotatedRect box, Scalar color, int thickness = 1, int lineType = LINE_8)
    private static native void ellipse_4(long img_nativeObj, double box_center_x, double box_center_y, double box_size_width, double box_size_height, double box_angle, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType);
    private static native void ellipse_5(long img_nativeObj, double box_center_x, double box_center_y, double box_size_width, double box_size_height, double box_angle, double color_val0, double color_val1, double color_val2, double color_val3, int thickness);
    private static native void ellipse_6(long img_nativeObj, double box_center_x, double box_center_y, double box_size_width, double box_size_height, double box_angle, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::ellipse2Poly(Point center, Size axes, int angle, int arcStart, int arcEnd, int delta, vector_Point& pts)
    private static native void ellipse2Poly_0(double center_x, double center_y, double axes_width, double axes_height, int angle, int arcStart, int arcEnd, int delta, long pts_mat_nativeObj);

    // C++:  void cv::equalizeHist(Mat src, Mat& dst)
    private static native void equalizeHist_0(long src_nativeObj, long dst_nativeObj);

    // C++:  void cv::erode(Mat src, Mat& dst, Mat kernel, Point anchor = Point(-1,-1), int iterations = 1, int borderType = BORDER_CONSTANT, Scalar borderValue = morphologyDefaultBorderValue())
    private static native void erode_0(long src_nativeObj, long dst_nativeObj, long kernel_nativeObj, double anchor_x, double anchor_y, int iterations, int borderType, double borderValue_val0, double borderValue_val1, double borderValue_val2, double borderValue_val3);
    private static native void erode_1(long src_nativeObj, long dst_nativeObj, long kernel_nativeObj, double anchor_x, double anchor_y, int iterations, int borderType);
    private static native void erode_2(long src_nativeObj, long dst_nativeObj, long kernel_nativeObj, double anchor_x, double anchor_y, int iterations);
    private static native void erode_3(long src_nativeObj, long dst_nativeObj, long kernel_nativeObj, double anchor_x, double anchor_y);
    private static native void erode_4(long src_nativeObj, long dst_nativeObj, long kernel_nativeObj);

    // C++:  void cv::fillConvexPoly(Mat& img, vector_Point points, Scalar color, int lineType = LINE_8, int shift = 0)
    private static native void fillConvexPoly_0(long img_nativeObj, long points_mat_nativeObj, double color_val0, double color_val1, double color_val2, double color_val3, int lineType, int shift);
    private static native void fillConvexPoly_1(long img_nativeObj, long points_mat_nativeObj, double color_val0, double color_val1, double color_val2, double color_val3, int lineType);
    private static native void fillConvexPoly_2(long img_nativeObj, long points_mat_nativeObj, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::fillPoly(Mat& img, vector_vector_Point pts, Scalar color, int lineType = LINE_8, int shift = 0, Point offset = Point())
    private static native void fillPoly_0(long img_nativeObj, long pts_mat_nativeObj, double color_val0, double color_val1, double color_val2, double color_val3, int lineType, int shift, double offset_x, double offset_y);
    private static native void fillPoly_1(long img_nativeObj, long pts_mat_nativeObj, double color_val0, double color_val1, double color_val2, double color_val3, int lineType, int shift);
    private static native void fillPoly_2(long img_nativeObj, long pts_mat_nativeObj, double color_val0, double color_val1, double color_val2, double color_val3, int lineType);
    private static native void fillPoly_3(long img_nativeObj, long pts_mat_nativeObj, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::filter2D(Mat src, Mat& dst, int ddepth, Mat kernel, Point anchor = Point(-1,-1), double delta = 0, int borderType = BORDER_DEFAULT)
    private static native void filter2D_0(long src_nativeObj, long dst_nativeObj, int ddepth, long kernel_nativeObj, double anchor_x, double anchor_y, double delta, int borderType);
    private static native void filter2D_1(long src_nativeObj, long dst_nativeObj, int ddepth, long kernel_nativeObj, double anchor_x, double anchor_y, double delta);
    private static native void filter2D_2(long src_nativeObj, long dst_nativeObj, int ddepth, long kernel_nativeObj, double anchor_x, double anchor_y);
    private static native void filter2D_3(long src_nativeObj, long dst_nativeObj, int ddepth, long kernel_nativeObj);

    // C++:  void cv::findContours(Mat image, vector_vector_Point& contours, Mat& hierarchy, int mode, int method, Point offset = Point())
    private static native void findContours_0(long image_nativeObj, long contours_mat_nativeObj, long hierarchy_nativeObj, int mode, int method, double offset_x, double offset_y);
    private static native void findContours_1(long image_nativeObj, long contours_mat_nativeObj, long hierarchy_nativeObj, int mode, int method);

    // C++:  void cv::fitLine(Mat points, Mat& line, int distType, double param, double reps, double aeps)
    private static native void fitLine_0(long points_nativeObj, long line_nativeObj, int distType, double param, double reps, double aeps);

    // C++:  void cv::getDerivKernels(Mat& kx, Mat& ky, int dx, int dy, int ksize, bool normalize = false, int ktype = CV_32F)
    private static native void getDerivKernels_0(long kx_nativeObj, long ky_nativeObj, int dx, int dy, int ksize, boolean normalize, int ktype);
    private static native void getDerivKernels_1(long kx_nativeObj, long ky_nativeObj, int dx, int dy, int ksize, boolean normalize);
    private static native void getDerivKernels_2(long kx_nativeObj, long ky_nativeObj, int dx, int dy, int ksize);

    // C++:  void cv::getRectSubPix(Mat image, Size patchSize, Point2f center, Mat& patch, int patchType = -1)
    private static native void getRectSubPix_0(long image_nativeObj, double patchSize_width, double patchSize_height, double center_x, double center_y, long patch_nativeObj, int patchType);
    private static native void getRectSubPix_1(long image_nativeObj, double patchSize_width, double patchSize_height, double center_x, double center_y, long patch_nativeObj);

    // C++:  void cv::goodFeaturesToTrack(Mat image, vector_Point& corners, int maxCorners, double qualityLevel, double minDistance, Mat mask, int blockSize, int gradientSize, bool useHarrisDetector = false, double k = 0.04)
    private static native void goodFeaturesToTrack_0(long image_nativeObj, long corners_mat_nativeObj, int maxCorners, double qualityLevel, double minDistance, long mask_nativeObj, int blockSize, int gradientSize, boolean useHarrisDetector, double k);
    private static native void goodFeaturesToTrack_1(long image_nativeObj, long corners_mat_nativeObj, int maxCorners, double qualityLevel, double minDistance, long mask_nativeObj, int blockSize, int gradientSize, boolean useHarrisDetector);
    private static native void goodFeaturesToTrack_2(long image_nativeObj, long corners_mat_nativeObj, int maxCorners, double qualityLevel, double minDistance, long mask_nativeObj, int blockSize, int gradientSize);

    // C++:  void cv::goodFeaturesToTrack(Mat image, vector_Point& corners, int maxCorners, double qualityLevel, double minDistance, Mat mask = Mat(), int blockSize = 3, bool useHarrisDetector = false, double k = 0.04)
    private static native void goodFeaturesToTrack_3(long image_nativeObj, long corners_mat_nativeObj, int maxCorners, double qualityLevel, double minDistance, long mask_nativeObj, int blockSize, boolean useHarrisDetector, double k);
    private static native void goodFeaturesToTrack_4(long image_nativeObj, long corners_mat_nativeObj, int maxCorners, double qualityLevel, double minDistance, long mask_nativeObj, int blockSize, boolean useHarrisDetector);
    private static native void goodFeaturesToTrack_5(long image_nativeObj, long corners_mat_nativeObj, int maxCorners, double qualityLevel, double minDistance, long mask_nativeObj, int blockSize);
    private static native void goodFeaturesToTrack_6(long image_nativeObj, long corners_mat_nativeObj, int maxCorners, double qualityLevel, double minDistance, long mask_nativeObj);
    private static native void goodFeaturesToTrack_7(long image_nativeObj, long corners_mat_nativeObj, int maxCorners, double qualityLevel, double minDistance);

    // C++:  void cv::grabCut(Mat img, Mat& mask, Rect rect, Mat& bgdModel, Mat& fgdModel, int iterCount, int mode = GC_EVAL)
    private static native void grabCut_0(long img_nativeObj, long mask_nativeObj, int rect_x, int rect_y, int rect_width, int rect_height, long bgdModel_nativeObj, long fgdModel_nativeObj, int iterCount, int mode);
    private static native void grabCut_1(long img_nativeObj, long mask_nativeObj, int rect_x, int rect_y, int rect_width, int rect_height, long bgdModel_nativeObj, long fgdModel_nativeObj, int iterCount);

    // C++:  void cv::integral(Mat src, Mat& sum, Mat& sqsum, Mat& tilted, int sdepth = -1, int sqdepth = -1)
    private static native void integral3_0(long src_nativeObj, long sum_nativeObj, long sqsum_nativeObj, long tilted_nativeObj, int sdepth, int sqdepth);
    private static native void integral3_1(long src_nativeObj, long sum_nativeObj, long sqsum_nativeObj, long tilted_nativeObj, int sdepth);
    private static native void integral3_2(long src_nativeObj, long sum_nativeObj, long sqsum_nativeObj, long tilted_nativeObj);

    // C++:  void cv::integral(Mat src, Mat& sum, Mat& sqsum, int sdepth = -1, int sqdepth = -1)
    private static native void integral2_0(long src_nativeObj, long sum_nativeObj, long sqsum_nativeObj, int sdepth, int sqdepth);
    private static native void integral2_1(long src_nativeObj, long sum_nativeObj, long sqsum_nativeObj, int sdepth);
    private static native void integral2_2(long src_nativeObj, long sum_nativeObj, long sqsum_nativeObj);

    // C++:  void cv::integral(Mat src, Mat& sum, int sdepth = -1)
    private static native void integral_0(long src_nativeObj, long sum_nativeObj, int sdepth);
    private static native void integral_1(long src_nativeObj, long sum_nativeObj);

    // C++:  void cv::invertAffineTransform(Mat M, Mat& iM)
    private static native void invertAffineTransform_0(long M_nativeObj, long iM_nativeObj);

    // C++:  void cv::line(Mat& img, Point pt1, Point pt2, Scalar color, int thickness = 1, int lineType = LINE_8, int shift = 0)
    private static native void line_0(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType, int shift);
    private static native void line_1(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType);
    private static native void line_2(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3, int thickness);
    private static native void line_3(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::linearPolar(Mat src, Mat& dst, Point2f center, double maxRadius, int flags)
    private static native void linearPolar_0(long src_nativeObj, long dst_nativeObj, double center_x, double center_y, double maxRadius, int flags);

    // C++:  void cv::logPolar(Mat src, Mat& dst, Point2f center, double M, int flags)
    private static native void logPolar_0(long src_nativeObj, long dst_nativeObj, double center_x, double center_y, double M, int flags);

    // C++:  void cv::matchTemplate(Mat image, Mat templ, Mat& result, int method, Mat mask = Mat())
    private static native void matchTemplate_0(long image_nativeObj, long templ_nativeObj, long result_nativeObj, int method, long mask_nativeObj);
    private static native void matchTemplate_1(long image_nativeObj, long templ_nativeObj, long result_nativeObj, int method);

    // C++:  void cv::medianBlur(Mat src, Mat& dst, int ksize)
    private static native void medianBlur_0(long src_nativeObj, long dst_nativeObj, int ksize);

    // C++:  void cv::minEnclosingCircle(vector_Point2f points, Point2f& center, float& radius)
    private static native void minEnclosingCircle_0(long points_mat_nativeObj, double[] center_out, double[] radius_out);

    // C++:  void cv::morphologyEx(Mat src, Mat& dst, int op, Mat kernel, Point anchor = Point(-1,-1), int iterations = 1, int borderType = BORDER_CONSTANT, Scalar borderValue = morphologyDefaultBorderValue())
    private static native void morphologyEx_0(long src_nativeObj, long dst_nativeObj, int op, long kernel_nativeObj, double anchor_x, double anchor_y, int iterations, int borderType, double borderValue_val0, double borderValue_val1, double borderValue_val2, double borderValue_val3);
    private static native void morphologyEx_1(long src_nativeObj, long dst_nativeObj, int op, long kernel_nativeObj, double anchor_x, double anchor_y, int iterations, int borderType);
    private static native void morphologyEx_2(long src_nativeObj, long dst_nativeObj, int op, long kernel_nativeObj, double anchor_x, double anchor_y, int iterations);
    private static native void morphologyEx_3(long src_nativeObj, long dst_nativeObj, int op, long kernel_nativeObj, double anchor_x, double anchor_y);
    private static native void morphologyEx_4(long src_nativeObj, long dst_nativeObj, int op, long kernel_nativeObj);

    // C++:  void cv::polylines(Mat& img, vector_vector_Point pts, bool isClosed, Scalar color, int thickness = 1, int lineType = LINE_8, int shift = 0)
    private static native void polylines_0(long img_nativeObj, long pts_mat_nativeObj, boolean isClosed, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType, int shift);
    private static native void polylines_1(long img_nativeObj, long pts_mat_nativeObj, boolean isClosed, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType);
    private static native void polylines_2(long img_nativeObj, long pts_mat_nativeObj, boolean isClosed, double color_val0, double color_val1, double color_val2, double color_val3, int thickness);
    private static native void polylines_3(long img_nativeObj, long pts_mat_nativeObj, boolean isClosed, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::preCornerDetect(Mat src, Mat& dst, int ksize, int borderType = BORDER_DEFAULT)
    private static native void preCornerDetect_0(long src_nativeObj, long dst_nativeObj, int ksize, int borderType);
    private static native void preCornerDetect_1(long src_nativeObj, long dst_nativeObj, int ksize);

    // C++:  void cv::putText(Mat& img, String text, Point org, int fontFace, double fontScale, Scalar color, int thickness = 1, int lineType = LINE_8, bool bottomLeftOrigin = false)
    private static native void putText_0(long img_nativeObj, String text, double org_x, double org_y, int fontFace, double fontScale, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType, boolean bottomLeftOrigin);
    private static native void putText_1(long img_nativeObj, String text, double org_x, double org_y, int fontFace, double fontScale, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType);
    private static native void putText_2(long img_nativeObj, String text, double org_x, double org_y, int fontFace, double fontScale, double color_val0, double color_val1, double color_val2, double color_val3, int thickness);
    private static native void putText_3(long img_nativeObj, String text, double org_x, double org_y, int fontFace, double fontScale, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::pyrDown(Mat src, Mat& dst, Size dstsize = Size(), int borderType = BORDER_DEFAULT)
    private static native void pyrDown_0(long src_nativeObj, long dst_nativeObj, double dstsize_width, double dstsize_height, int borderType);
    private static native void pyrDown_1(long src_nativeObj, long dst_nativeObj, double dstsize_width, double dstsize_height);
    private static native void pyrDown_2(long src_nativeObj, long dst_nativeObj);

    // C++:  void cv::pyrMeanShiftFiltering(Mat src, Mat& dst, double sp, double sr, int maxLevel = 1, TermCriteria termcrit = TermCriteria(TermCriteria::MAX_ITER+TermCriteria::EPS,5,1))
    private static native void pyrMeanShiftFiltering_0(long src_nativeObj, long dst_nativeObj, double sp, double sr, int maxLevel, int termcrit_type, int termcrit_maxCount, double termcrit_epsilon);
    private static native void pyrMeanShiftFiltering_1(long src_nativeObj, long dst_nativeObj, double sp, double sr, int maxLevel);
    private static native void pyrMeanShiftFiltering_2(long src_nativeObj, long dst_nativeObj, double sp, double sr);

    // C++:  void cv::pyrUp(Mat src, Mat& dst, Size dstsize = Size(), int borderType = BORDER_DEFAULT)
    private static native void pyrUp_0(long src_nativeObj, long dst_nativeObj, double dstsize_width, double dstsize_height, int borderType);
    private static native void pyrUp_1(long src_nativeObj, long dst_nativeObj, double dstsize_width, double dstsize_height);
    private static native void pyrUp_2(long src_nativeObj, long dst_nativeObj);

    // C++:  void cv::rectangle(Mat& img, Point pt1, Point pt2, Scalar color, int thickness = 1, int lineType = LINE_8, int shift = 0)
    private static native void rectangle_0(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType, int shift);
    private static native void rectangle_1(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType);
    private static native void rectangle_2(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3, int thickness);
    private static native void rectangle_3(long img_nativeObj, double pt1_x, double pt1_y, double pt2_x, double pt2_y, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::rectangle(Mat& img, Rect rec, Scalar color, int thickness = 1, int lineType = LINE_8, int shift = 0)
    private static native void rectangle_4(long img_nativeObj, int rec_x, int rec_y, int rec_width, int rec_height, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType, int shift);
    private static native void rectangle_5(long img_nativeObj, int rec_x, int rec_y, int rec_width, int rec_height, double color_val0, double color_val1, double color_val2, double color_val3, int thickness, int lineType);
    private static native void rectangle_6(long img_nativeObj, int rec_x, int rec_y, int rec_width, int rec_height, double color_val0, double color_val1, double color_val2, double color_val3, int thickness);
    private static native void rectangle_7(long img_nativeObj, int rec_x, int rec_y, int rec_width, int rec_height, double color_val0, double color_val1, double color_val2, double color_val3);

    // C++:  void cv::remap(Mat src, Mat& dst, Mat map1, Mat map2, int interpolation, int borderMode = BORDER_CONSTANT, Scalar borderValue = Scalar())
    private static native void remap_0(long src_nativeObj, long dst_nativeObj, long map1_nativeObj, long map2_nativeObj, int interpolation, int borderMode, double borderValue_val0, double borderValue_val1, double borderValue_val2, double borderValue_val3);
    private static native void remap_1(long src_nativeObj, long dst_nativeObj, long map1_nativeObj, long map2_nativeObj, int interpolation, int borderMode);
    private static native void remap_2(long src_nativeObj, long dst_nativeObj, long map1_nativeObj, long map2_nativeObj, int interpolation);

    // C++:  void cv::resize(Mat src, Mat& dst, Size dsize, double fx = 0, double fy = 0, int interpolation = INTER_LINEAR)
    private static native void resize_0(long src_nativeObj, long dst_nativeObj, double dsize_width, double dsize_height, double fx, double fy, int interpolation);
    private static native void resize_1(long src_nativeObj, long dst_nativeObj, double dsize_width, double dsize_height, double fx, double fy);
    private static native void resize_2(long src_nativeObj, long dst_nativeObj, double dsize_width, double dsize_height, double fx);
    private static native void resize_3(long src_nativeObj, long dst_nativeObj, double dsize_width, double dsize_height);

    // C++:  void cv::sepFilter2D(Mat src, Mat& dst, int ddepth, Mat kernelX, Mat kernelY, Point anchor = Point(-1,-1), double delta = 0, int borderType = BORDER_DEFAULT)
    private static native void sepFilter2D_0(long src_nativeObj, long dst_nativeObj, int ddepth, long kernelX_nativeObj, long kernelY_nativeObj, double anchor_x, double anchor_y, double delta, int borderType);
    private static native void sepFilter2D_1(long src_nativeObj, long dst_nativeObj, int ddepth, long kernelX_nativeObj, long kernelY_nativeObj, double anchor_x, double anchor_y, double delta);
    private static native void sepFilter2D_2(long src_nativeObj, long dst_nativeObj, int ddepth, long kernelX_nativeObj, long kernelY_nativeObj, double anchor_x, double anchor_y);
    private static native void sepFilter2D_3(long src_nativeObj, long dst_nativeObj, int ddepth, long kernelX_nativeObj, long kernelY_nativeObj);

    // C++:  void cv::spatialGradient(Mat src, Mat& dx, Mat& dy, int ksize = 3, int borderType = BORDER_DEFAULT)
    private static native void spatialGradient_0(long src_nativeObj, long dx_nativeObj, long dy_nativeObj, int ksize, int borderType);
    private static native void spatialGradient_1(long src_nativeObj, long dx_nativeObj, long dy_nativeObj, int ksize);
    private static native void spatialGradient_2(long src_nativeObj, long dx_nativeObj, long dy_nativeObj);

    // C++:  void cv::sqrBoxFilter(Mat src, Mat& dst, int ddepth, Size ksize, Point anchor = Point(-1, -1), bool normalize = true, int borderType = BORDER_DEFAULT)
    private static native void sqrBoxFilter_0(long src_nativeObj, long dst_nativeObj, int ddepth, double ksize_width, double ksize_height, double anchor_x, double anchor_y, boolean normalize, int borderType);
    private static native void sqrBoxFilter_1(long src_nativeObj, long dst_nativeObj, int ddepth, double ksize_width, double ksize_height, double anchor_x, double anchor_y, boolean normalize);
    private static native void sqrBoxFilter_2(long src_nativeObj, long dst_nativeObj, int ddepth, double ksize_width, double ksize_height, double anchor_x, double anchor_y);
    private static native void sqrBoxFilter_3(long src_nativeObj, long dst_nativeObj, int ddepth, double ksize_width, double ksize_height);

    // C++:  void cv::warpAffine(Mat src, Mat& dst, Mat M, Size dsize, int flags = INTER_LINEAR, int borderMode = BORDER_CONSTANT, Scalar borderValue = Scalar())
    private static native void warpAffine_0(long src_nativeObj, long dst_nativeObj, long M_nativeObj, double dsize_width, double dsize_height, int flags, int borderMode, double borderValue_val0, double borderValue_val1, double borderValue_val2, double borderValue_val3);
    private static native void warpAffine_1(long src_nativeObj, long dst_nativeObj, long M_nativeObj, double dsize_width, double dsize_height, int flags, int borderMode);
    private static native void warpAffine_2(long src_nativeObj, long dst_nativeObj, long M_nativeObj, double dsize_width, double dsize_height, int flags);
    private static native void warpAffine_3(long src_nativeObj, long dst_nativeObj, long M_nativeObj, double dsize_width, double dsize_height);

    // C++:  void cv::warpPerspective(Mat src, Mat& dst, Mat M, Size dsize, int flags = INTER_LINEAR, int borderMode = BORDER_CONSTANT, Scalar borderValue = Scalar())
    private static native void warpPerspective_0(long src_nativeObj, long dst_nativeObj, long M_nativeObj, double dsize_width, double dsize_height, int flags, int borderMode, double borderValue_val0, double borderValue_val1, double borderValue_val2, double borderValue_val3);
    private static native void warpPerspective_1(long src_nativeObj, long dst_nativeObj, long M_nativeObj, double dsize_width, double dsize_height, int flags, int borderMode);
    private static native void warpPerspective_2(long src_nativeObj, long dst_nativeObj, long M_nativeObj, double dsize_width, double dsize_height, int flags);
    private static native void warpPerspective_3(long src_nativeObj, long dst_nativeObj, long M_nativeObj, double dsize_width, double dsize_height);

    // C++:  void cv::warpPolar(Mat src, Mat& dst, Size dsize, Point2f center, double maxRadius, int flags)
    private static native void warpPolar_0(long src_nativeObj, long dst_nativeObj, double dsize_width, double dsize_height, double center_x, double center_y, double maxRadius, int flags);

    // C++:  void cv::watershed(Mat image, Mat& markers)
    private static native void watershed_0(long image_nativeObj, long markers_nativeObj);
private static native double[] n_getTextSize(String text, int fontFace, double fontScale, int thickness, int[] baseLine);

}
