//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.features2d.BRISK;
import org.opencv.features2d.Feature2D;
import org.opencv.utils.Converters;

// C++: class BRISK
//javadoc: BRISK

public class BRISK extends Feature2D {

    protected BRISK(long addr) { super(addr); }

    // internal usage only
    public static BRISK __fromPtr__(long addr) { return new BRISK(addr); }

    //
    // C++: static Ptr_BRISK cv::BRISK::create(int thresh, int octaves, vector_float radiusList, vector_int numberList, float dMax = 5.85f, float dMin = 8.2f, vector_int indexChange = std::vector<int>())
    //

    //javadoc: BRISK::create(thresh, octaves, radiusList, numberList, dMax, dMin, indexChange)
    public static BRISK create(int thresh, int octaves, MatOfFloat radiusList, MatOfInt numberList, float dMax, float dMin, MatOfInt indexChange)
    {
        Mat radiusList_mat = radiusList;
        Mat numberList_mat = numberList;
        Mat indexChange_mat = indexChange;
        BRISK retVal = BRISK.__fromPtr__(create_0(thresh, octaves, radiusList_mat.nativeObj, numberList_mat.nativeObj, dMax, dMin, indexChange_mat.nativeObj));
        
        return retVal;
    }

    //javadoc: BRISK::create(thresh, octaves, radiusList, numberList, dMax, dMin)
    public static BRISK create(int thresh, int octaves, MatOfFloat radiusList, MatOfInt numberList, float dMax, float dMin)
    {
        Mat radiusList_mat = radiusList;
        Mat numberList_mat = numberList;
        BRISK retVal = BRISK.__fromPtr__(create_1(thresh, octaves, radiusList_mat.nativeObj, numberList_mat.nativeObj, dMax, dMin));
        
        return retVal;
    }

    //javadoc: BRISK::create(thresh, octaves, radiusList, numberList, dMax)
    public static BRISK create(int thresh, int octaves, MatOfFloat radiusList, MatOfInt numberList, float dMax)
    {
        Mat radiusList_mat = radiusList;
        Mat numberList_mat = numberList;
        BRISK retVal = BRISK.__fromPtr__(create_2(thresh, octaves, radiusList_mat.nativeObj, numberList_mat.nativeObj, dMax));
        
        return retVal;
    }

    //javadoc: BRISK::create(thresh, octaves, radiusList, numberList)
    public static BRISK create(int thresh, int octaves, MatOfFloat radiusList, MatOfInt numberList)
    {
        Mat radiusList_mat = radiusList;
        Mat numberList_mat = numberList;
        BRISK retVal = BRISK.__fromPtr__(create_3(thresh, octaves, radiusList_mat.nativeObj, numberList_mat.nativeObj));
        
        return retVal;
    }


    //
    // C++: static Ptr_BRISK cv::BRISK::create(int thresh = 30, int octaves = 3, float patternScale = 1.0f)
    //

    //javadoc: BRISK::create(thresh, octaves, patternScale)
    public static BRISK create(int thresh, int octaves, float patternScale)
    {
        
        BRISK retVal = BRISK.__fromPtr__(create_4(thresh, octaves, patternScale));
        
        return retVal;
    }

    //javadoc: BRISK::create(thresh, octaves)
    public static BRISK create(int thresh, int octaves)
    {
        
        BRISK retVal = BRISK.__fromPtr__(create_5(thresh, octaves));
        
        return retVal;
    }

    //javadoc: BRISK::create(thresh)
    public static BRISK create(int thresh)
    {
        
        BRISK retVal = BRISK.__fromPtr__(create_6(thresh));
        
        return retVal;
    }

    //javadoc: BRISK::create()
    public static BRISK create()
    {
        
        BRISK retVal = BRISK.__fromPtr__(create_7());
        
        return retVal;
    }


    //
    // C++: static Ptr_BRISK cv::BRISK::create(vector_float radiusList, vector_int numberList, float dMax = 5.85f, float dMin = 8.2f, vector_int indexChange = std::vector<int>())
    //

    //javadoc: BRISK::create(radiusList, numberList, dMax, dMin, indexChange)
    public static BRISK create(MatOfFloat radiusList, MatOfInt numberList, float dMax, float dMin, MatOfInt indexChange)
    {
        Mat radiusList_mat = radiusList;
        Mat numberList_mat = numberList;
        Mat indexChange_mat = indexChange;
        BRISK retVal = BRISK.__fromPtr__(create_8(radiusList_mat.nativeObj, numberList_mat.nativeObj, dMax, dMin, indexChange_mat.nativeObj));
        
        return retVal;
    }

    //javadoc: BRISK::create(radiusList, numberList, dMax, dMin)
    public static BRISK create(MatOfFloat radiusList, MatOfInt numberList, float dMax, float dMin)
    {
        Mat radiusList_mat = radiusList;
        Mat numberList_mat = numberList;
        BRISK retVal = BRISK.__fromPtr__(create_9(radiusList_mat.nativeObj, numberList_mat.nativeObj, dMax, dMin));
        
        return retVal;
    }

    //javadoc: BRISK::create(radiusList, numberList, dMax)
    public static BRISK create(MatOfFloat radiusList, MatOfInt numberList, float dMax)
    {
        Mat radiusList_mat = radiusList;
        Mat numberList_mat = numberList;
        BRISK retVal = BRISK.__fromPtr__(create_10(radiusList_mat.nativeObj, numberList_mat.nativeObj, dMax));
        
        return retVal;
    }

    //javadoc: BRISK::create(radiusList, numberList)
    public static BRISK create(MatOfFloat radiusList, MatOfInt numberList)
    {
        Mat radiusList_mat = radiusList;
        Mat numberList_mat = numberList;
        BRISK retVal = BRISK.__fromPtr__(create_11(radiusList_mat.nativeObj, numberList_mat.nativeObj));
        
        return retVal;
    }


    //
    // C++:  String cv::BRISK::getDefaultName()
    //

    //javadoc: BRISK::getDefaultName()
    public  String getDefaultName()
    {
        
        String retVal = getDefaultName_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::BRISK::getOctaves()
    //

    //javadoc: BRISK::getOctaves()
    public  int getOctaves()
    {
        
        int retVal = getOctaves_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::BRISK::getThreshold()
    //

    //javadoc: BRISK::getThreshold()
    public  int getThreshold()
    {
        
        int retVal = getThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::BRISK::setOctaves(int octaves)
    //

    //javadoc: BRISK::setOctaves(octaves)
    public  void setOctaves(int octaves)
    {
        
        setOctaves_0(nativeObj, octaves);
        
        return;
    }


    //
    // C++:  void cv::BRISK::setThreshold(int threshold)
    //

    //javadoc: BRISK::setThreshold(threshold)
    public  void setThreshold(int threshold)
    {
        
        setThreshold_0(nativeObj, threshold);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++: static Ptr_BRISK cv::BRISK::create(int thresh, int octaves, vector_float radiusList, vector_int numberList, float dMax = 5.85f, float dMin = 8.2f, vector_int indexChange = std::vector<int>())
    private static native long create_0(int thresh, int octaves, long radiusList_mat_nativeObj, long numberList_mat_nativeObj, float dMax, float dMin, long indexChange_mat_nativeObj);
    private static native long create_1(int thresh, int octaves, long radiusList_mat_nativeObj, long numberList_mat_nativeObj, float dMax, float dMin);
    private static native long create_2(int thresh, int octaves, long radiusList_mat_nativeObj, long numberList_mat_nativeObj, float dMax);
    private static native long create_3(int thresh, int octaves, long radiusList_mat_nativeObj, long numberList_mat_nativeObj);

    // C++: static Ptr_BRISK cv::BRISK::create(int thresh = 30, int octaves = 3, float patternScale = 1.0f)
    private static native long create_4(int thresh, int octaves, float patternScale);
    private static native long create_5(int thresh, int octaves);
    private static native long create_6(int thresh);
    private static native long create_7();

    // C++: static Ptr_BRISK cv::BRISK::create(vector_float radiusList, vector_int numberList, float dMax = 5.85f, float dMin = 8.2f, vector_int indexChange = std::vector<int>())
    private static native long create_8(long radiusList_mat_nativeObj, long numberList_mat_nativeObj, float dMax, float dMin, long indexChange_mat_nativeObj);
    private static native long create_9(long radiusList_mat_nativeObj, long numberList_mat_nativeObj, float dMax, float dMin);
    private static native long create_10(long radiusList_mat_nativeObj, long numberList_mat_nativeObj, float dMax);
    private static native long create_11(long radiusList_mat_nativeObj, long numberList_mat_nativeObj);

    // C++:  String cv::BRISK::getDefaultName()
    private static native String getDefaultName_0(long nativeObj);

    // C++:  int cv::BRISK::getOctaves()
    private static native int getOctaves_0(long nativeObj);

    // C++:  int cv::BRISK::getThreshold()
    private static native int getThreshold_0(long nativeObj);

    // C++:  void cv::BRISK::setOctaves(int octaves)
    private static native void setOctaves_0(long nativeObj, int octaves);

    // C++:  void cv::BRISK::setThreshold(int threshold)
    private static native void setThreshold_0(long nativeObj, int threshold);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
