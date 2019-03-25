//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.MSER;
import org.opencv.utils.Converters;

// C++: class MSER
//javadoc: MSER

public class MSER extends Feature2D {

    protected MSER(long addr) { super(addr); }

    // internal usage only
    public static MSER __fromPtr__(long addr) { return new MSER(addr); }

    //
    // C++: static Ptr_MSER cv::MSER::create(int _delta = 5, int _min_area = 60, int _max_area = 14400, double _max_variation = 0.25, double _min_diversity = .2, int _max_evolution = 200, double _area_threshold = 1.01, double _min_margin = 0.003, int _edge_blur_size = 5)
    //

    //javadoc: MSER::create(_delta, _min_area, _max_area, _max_variation, _min_diversity, _max_evolution, _area_threshold, _min_margin, _edge_blur_size)
    public static MSER create(int _delta, int _min_area, int _max_area, double _max_variation, double _min_diversity, int _max_evolution, double _area_threshold, double _min_margin, int _edge_blur_size)
    {
        
        MSER retVal = MSER.__fromPtr__(create_0(_delta, _min_area, _max_area, _max_variation, _min_diversity, _max_evolution, _area_threshold, _min_margin, _edge_blur_size));
        
        return retVal;
    }

    //javadoc: MSER::create(_delta, _min_area, _max_area, _max_variation, _min_diversity, _max_evolution, _area_threshold, _min_margin)
    public static MSER create(int _delta, int _min_area, int _max_area, double _max_variation, double _min_diversity, int _max_evolution, double _area_threshold, double _min_margin)
    {
        
        MSER retVal = MSER.__fromPtr__(create_1(_delta, _min_area, _max_area, _max_variation, _min_diversity, _max_evolution, _area_threshold, _min_margin));
        
        return retVal;
    }

    //javadoc: MSER::create(_delta, _min_area, _max_area, _max_variation, _min_diversity, _max_evolution, _area_threshold)
    public static MSER create(int _delta, int _min_area, int _max_area, double _max_variation, double _min_diversity, int _max_evolution, double _area_threshold)
    {
        
        MSER retVal = MSER.__fromPtr__(create_2(_delta, _min_area, _max_area, _max_variation, _min_diversity, _max_evolution, _area_threshold));
        
        return retVal;
    }

    //javadoc: MSER::create(_delta, _min_area, _max_area, _max_variation, _min_diversity, _max_evolution)
    public static MSER create(int _delta, int _min_area, int _max_area, double _max_variation, double _min_diversity, int _max_evolution)
    {
        
        MSER retVal = MSER.__fromPtr__(create_3(_delta, _min_area, _max_area, _max_variation, _min_diversity, _max_evolution));
        
        return retVal;
    }

    //javadoc: MSER::create(_delta, _min_area, _max_area, _max_variation, _min_diversity)
    public static MSER create(int _delta, int _min_area, int _max_area, double _max_variation, double _min_diversity)
    {
        
        MSER retVal = MSER.__fromPtr__(create_4(_delta, _min_area, _max_area, _max_variation, _min_diversity));
        
        return retVal;
    }

    //javadoc: MSER::create(_delta, _min_area, _max_area, _max_variation)
    public static MSER create(int _delta, int _min_area, int _max_area, double _max_variation)
    {
        
        MSER retVal = MSER.__fromPtr__(create_5(_delta, _min_area, _max_area, _max_variation));
        
        return retVal;
    }

    //javadoc: MSER::create(_delta, _min_area, _max_area)
    public static MSER create(int _delta, int _min_area, int _max_area)
    {
        
        MSER retVal = MSER.__fromPtr__(create_6(_delta, _min_area, _max_area));
        
        return retVal;
    }

    //javadoc: MSER::create(_delta, _min_area)
    public static MSER create(int _delta, int _min_area)
    {
        
        MSER retVal = MSER.__fromPtr__(create_7(_delta, _min_area));
        
        return retVal;
    }

    //javadoc: MSER::create(_delta)
    public static MSER create(int _delta)
    {
        
        MSER retVal = MSER.__fromPtr__(create_8(_delta));
        
        return retVal;
    }

    //javadoc: MSER::create()
    public static MSER create()
    {
        
        MSER retVal = MSER.__fromPtr__(create_9());
        
        return retVal;
    }


    //
    // C++:  String cv::MSER::getDefaultName()
    //

    //javadoc: MSER::getDefaultName()
    public  String getDefaultName()
    {
        
        String retVal = getDefaultName_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::MSER::getPass2Only()
    //

    //javadoc: MSER::getPass2Only()
    public  boolean getPass2Only()
    {
        
        boolean retVal = getPass2Only_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::MSER::getDelta()
    //

    //javadoc: MSER::getDelta()
    public  int getDelta()
    {
        
        int retVal = getDelta_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::MSER::getMaxArea()
    //

    //javadoc: MSER::getMaxArea()
    public  int getMaxArea()
    {
        
        int retVal = getMaxArea_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::MSER::getMinArea()
    //

    //javadoc: MSER::getMinArea()
    public  int getMinArea()
    {
        
        int retVal = getMinArea_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::MSER::detectRegions(Mat image, vector_vector_Point& msers, vector_Rect& bboxes)
    //

    //javadoc: MSER::detectRegions(image, msers, bboxes)
    public  void detectRegions(Mat image, List<MatOfPoint> msers, MatOfRect bboxes)
    {
        Mat msers_mat = new Mat();
        Mat bboxes_mat = bboxes;
        detectRegions_0(nativeObj, image.nativeObj, msers_mat.nativeObj, bboxes_mat.nativeObj);
        Converters.Mat_to_vector_vector_Point(msers_mat, msers);
        msers_mat.release();
        return;
    }


    //
    // C++:  void cv::MSER::setDelta(int delta)
    //

    //javadoc: MSER::setDelta(delta)
    public  void setDelta(int delta)
    {
        
        setDelta_0(nativeObj, delta);
        
        return;
    }


    //
    // C++:  void cv::MSER::setMaxArea(int maxArea)
    //

    //javadoc: MSER::setMaxArea(maxArea)
    public  void setMaxArea(int maxArea)
    {
        
        setMaxArea_0(nativeObj, maxArea);
        
        return;
    }


    //
    // C++:  void cv::MSER::setMinArea(int minArea)
    //

    //javadoc: MSER::setMinArea(minArea)
    public  void setMinArea(int minArea)
    {
        
        setMinArea_0(nativeObj, minArea);
        
        return;
    }


    //
    // C++:  void cv::MSER::setPass2Only(bool f)
    //

    //javadoc: MSER::setPass2Only(f)
    public  void setPass2Only(boolean f)
    {
        
        setPass2Only_0(nativeObj, f);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_MSER cv::MSER::create(int _delta = 5, int _min_area = 60, int _max_area = 14400, double _max_variation = 0.25, double _min_diversity = .2, int _max_evolution = 200, double _area_threshold = 1.01, double _min_margin = 0.003, int _edge_blur_size = 5)
    private static native long create_0(int _delta, int _min_area, int _max_area, double _max_variation, double _min_diversity, int _max_evolution, double _area_threshold, double _min_margin, int _edge_blur_size);
    private static native long create_1(int _delta, int _min_area, int _max_area, double _max_variation, double _min_diversity, int _max_evolution, double _area_threshold, double _min_margin);
    private static native long create_2(int _delta, int _min_area, int _max_area, double _max_variation, double _min_diversity, int _max_evolution, double _area_threshold);
    private static native long create_3(int _delta, int _min_area, int _max_area, double _max_variation, double _min_diversity, int _max_evolution);
    private static native long create_4(int _delta, int _min_area, int _max_area, double _max_variation, double _min_diversity);
    private static native long create_5(int _delta, int _min_area, int _max_area, double _max_variation);
    private static native long create_6(int _delta, int _min_area, int _max_area);
    private static native long create_7(int _delta, int _min_area);
    private static native long create_8(int _delta);
    private static native long create_9();

    // C++:  String cv::MSER::getDefaultName()
    private static native String getDefaultName_0(long nativeObj);

    // C++:  bool cv::MSER::getPass2Only()
    private static native boolean getPass2Only_0(long nativeObj);

    // C++:  int cv::MSER::getDelta()
    private static native int getDelta_0(long nativeObj);

    // C++:  int cv::MSER::getMaxArea()
    private static native int getMaxArea_0(long nativeObj);

    // C++:  int cv::MSER::getMinArea()
    private static native int getMinArea_0(long nativeObj);

    // C++:  void cv::MSER::detectRegions(Mat image, vector_vector_Point& msers, vector_Rect& bboxes)
    private static native void detectRegions_0(long nativeObj, long image_nativeObj, long msers_mat_nativeObj, long bboxes_mat_nativeObj);

    // C++:  void cv::MSER::setDelta(int delta)
    private static native void setDelta_0(long nativeObj, int delta);

    // C++:  void cv::MSER::setMaxArea(int maxArea)
    private static native void setMaxArea_0(long nativeObj, int maxArea);

    // C++:  void cv::MSER::setMinArea(int minArea)
    private static native void setMinArea_0(long nativeObj, int minArea);

    // C++:  void cv::MSER::setPass2Only(bool f)
    private static native void setPass2Only_0(long nativeObj, boolean f);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
