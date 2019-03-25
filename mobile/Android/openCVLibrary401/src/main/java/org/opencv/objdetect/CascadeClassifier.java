//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.objdetect;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.utils.Converters;

// C++: class CascadeClassifier
//javadoc: CascadeClassifier

public class CascadeClassifier {

    protected final long nativeObj;
    protected CascadeClassifier(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static CascadeClassifier __fromPtr__(long addr) { return new CascadeClassifier(addr); }

    //
    // C++:   cv::CascadeClassifier::CascadeClassifier(String filename)
    //

    //javadoc: CascadeClassifier::CascadeClassifier(filename)
    public   CascadeClassifier(String filename)
    {
        
        nativeObj = CascadeClassifier_0(filename);
        
        return;
    }


    //
    // C++:   cv::CascadeClassifier::CascadeClassifier()
    //

    //javadoc: CascadeClassifier::CascadeClassifier()
    public   CascadeClassifier()
    {
        
        nativeObj = CascadeClassifier_1();
        
        return;
    }


    //
    // C++:  Size cv::CascadeClassifier::getOriginalWindowSize()
    //

    //javadoc: CascadeClassifier::getOriginalWindowSize()
    public  Size getOriginalWindowSize()
    {
        
        Size retVal = new Size(getOriginalWindowSize_0(nativeObj));
        
        return retVal;
    }


    //
    // C++: static bool cv::CascadeClassifier::convert(String oldcascade, String newcascade)
    //

    //javadoc: CascadeClassifier::convert(oldcascade, newcascade)
    public static boolean convert(String oldcascade, String newcascade)
    {
        
        boolean retVal = convert_0(oldcascade, newcascade);
        
        return retVal;
    }


    //
    // C++:  bool cv::CascadeClassifier::empty()
    //

    //javadoc: CascadeClassifier::empty()
    public  boolean empty()
    {
        
        boolean retVal = empty_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::CascadeClassifier::isOldFormatCascade()
    //

    //javadoc: CascadeClassifier::isOldFormatCascade()
    public  boolean isOldFormatCascade()
    {
        
        boolean retVal = isOldFormatCascade_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  bool cv::CascadeClassifier::load(String filename)
    //

    //javadoc: CascadeClassifier::load(filename)
    public  boolean load(String filename)
    {
        
        boolean retVal = load_0(nativeObj, filename);
        
        return retVal;
    }


    //
    // C++:  bool cv::CascadeClassifier::read(FileNode node)
    //

    // Unknown type 'FileNode' (I), skipping the function


    //
    // C++:  int cv::CascadeClassifier::getFeatureType()
    //

    //javadoc: CascadeClassifier::getFeatureType()
    public  int getFeatureType()
    {
        
        int retVal = getFeatureType_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::CascadeClassifier::detectMultiScale(Mat image, vector_Rect& objects, double scaleFactor = 1.1, int minNeighbors = 3, int flags = 0, Size minSize = Size(), Size maxSize = Size())
    //

    //javadoc: CascadeClassifier::detectMultiScale(image, objects, scaleFactor, minNeighbors, flags, minSize, maxSize)
    public  void detectMultiScale(Mat image, MatOfRect objects, double scaleFactor, int minNeighbors, int flags, Size minSize, Size maxSize)
    {
        Mat objects_mat = objects;
        detectMultiScale_0(nativeObj, image.nativeObj, objects_mat.nativeObj, scaleFactor, minNeighbors, flags, minSize.width, minSize.height, maxSize.width, maxSize.height);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale(image, objects, scaleFactor, minNeighbors, flags, minSize)
    public  void detectMultiScale(Mat image, MatOfRect objects, double scaleFactor, int minNeighbors, int flags, Size minSize)
    {
        Mat objects_mat = objects;
        detectMultiScale_1(nativeObj, image.nativeObj, objects_mat.nativeObj, scaleFactor, minNeighbors, flags, minSize.width, minSize.height);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale(image, objects, scaleFactor, minNeighbors, flags)
    public  void detectMultiScale(Mat image, MatOfRect objects, double scaleFactor, int minNeighbors, int flags)
    {
        Mat objects_mat = objects;
        detectMultiScale_2(nativeObj, image.nativeObj, objects_mat.nativeObj, scaleFactor, minNeighbors, flags);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale(image, objects, scaleFactor, minNeighbors)
    public  void detectMultiScale(Mat image, MatOfRect objects, double scaleFactor, int minNeighbors)
    {
        Mat objects_mat = objects;
        detectMultiScale_3(nativeObj, image.nativeObj, objects_mat.nativeObj, scaleFactor, minNeighbors);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale(image, objects, scaleFactor)
    public  void detectMultiScale(Mat image, MatOfRect objects, double scaleFactor)
    {
        Mat objects_mat = objects;
        detectMultiScale_4(nativeObj, image.nativeObj, objects_mat.nativeObj, scaleFactor);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale(image, objects)
    public  void detectMultiScale(Mat image, MatOfRect objects)
    {
        Mat objects_mat = objects;
        detectMultiScale_5(nativeObj, image.nativeObj, objects_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::CascadeClassifier::detectMultiScale(Mat image, vector_Rect& objects, vector_int& numDetections, double scaleFactor = 1.1, int minNeighbors = 3, int flags = 0, Size minSize = Size(), Size maxSize = Size())
    //

    //javadoc: CascadeClassifier::detectMultiScale2(image, objects, numDetections, scaleFactor, minNeighbors, flags, minSize, maxSize)
    public  void detectMultiScale2(Mat image, MatOfRect objects, MatOfInt numDetections, double scaleFactor, int minNeighbors, int flags, Size minSize, Size maxSize)
    {
        Mat objects_mat = objects;
        Mat numDetections_mat = numDetections;
        detectMultiScale2_0(nativeObj, image.nativeObj, objects_mat.nativeObj, numDetections_mat.nativeObj, scaleFactor, minNeighbors, flags, minSize.width, minSize.height, maxSize.width, maxSize.height);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale2(image, objects, numDetections, scaleFactor, minNeighbors, flags, minSize)
    public  void detectMultiScale2(Mat image, MatOfRect objects, MatOfInt numDetections, double scaleFactor, int minNeighbors, int flags, Size minSize)
    {
        Mat objects_mat = objects;
        Mat numDetections_mat = numDetections;
        detectMultiScale2_1(nativeObj, image.nativeObj, objects_mat.nativeObj, numDetections_mat.nativeObj, scaleFactor, minNeighbors, flags, minSize.width, minSize.height);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale2(image, objects, numDetections, scaleFactor, minNeighbors, flags)
    public  void detectMultiScale2(Mat image, MatOfRect objects, MatOfInt numDetections, double scaleFactor, int minNeighbors, int flags)
    {
        Mat objects_mat = objects;
        Mat numDetections_mat = numDetections;
        detectMultiScale2_2(nativeObj, image.nativeObj, objects_mat.nativeObj, numDetections_mat.nativeObj, scaleFactor, minNeighbors, flags);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale2(image, objects, numDetections, scaleFactor, minNeighbors)
    public  void detectMultiScale2(Mat image, MatOfRect objects, MatOfInt numDetections, double scaleFactor, int minNeighbors)
    {
        Mat objects_mat = objects;
        Mat numDetections_mat = numDetections;
        detectMultiScale2_3(nativeObj, image.nativeObj, objects_mat.nativeObj, numDetections_mat.nativeObj, scaleFactor, minNeighbors);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale2(image, objects, numDetections, scaleFactor)
    public  void detectMultiScale2(Mat image, MatOfRect objects, MatOfInt numDetections, double scaleFactor)
    {
        Mat objects_mat = objects;
        Mat numDetections_mat = numDetections;
        detectMultiScale2_4(nativeObj, image.nativeObj, objects_mat.nativeObj, numDetections_mat.nativeObj, scaleFactor);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale2(image, objects, numDetections)
    public  void detectMultiScale2(Mat image, MatOfRect objects, MatOfInt numDetections)
    {
        Mat objects_mat = objects;
        Mat numDetections_mat = numDetections;
        detectMultiScale2_5(nativeObj, image.nativeObj, objects_mat.nativeObj, numDetections_mat.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::CascadeClassifier::detectMultiScale(Mat image, vector_Rect& objects, vector_int& rejectLevels, vector_double& levelWeights, double scaleFactor = 1.1, int minNeighbors = 3, int flags = 0, Size minSize = Size(), Size maxSize = Size(), bool outputRejectLevels = false)
    //

    //javadoc: CascadeClassifier::detectMultiScale3(image, objects, rejectLevels, levelWeights, scaleFactor, minNeighbors, flags, minSize, maxSize, outputRejectLevels)
    public  void detectMultiScale3(Mat image, MatOfRect objects, MatOfInt rejectLevels, MatOfDouble levelWeights, double scaleFactor, int minNeighbors, int flags, Size minSize, Size maxSize, boolean outputRejectLevels)
    {
        Mat objects_mat = objects;
        Mat rejectLevels_mat = rejectLevels;
        Mat levelWeights_mat = levelWeights;
        detectMultiScale3_0(nativeObj, image.nativeObj, objects_mat.nativeObj, rejectLevels_mat.nativeObj, levelWeights_mat.nativeObj, scaleFactor, minNeighbors, flags, minSize.width, minSize.height, maxSize.width, maxSize.height, outputRejectLevels);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale3(image, objects, rejectLevels, levelWeights, scaleFactor, minNeighbors, flags, minSize, maxSize)
    public  void detectMultiScale3(Mat image, MatOfRect objects, MatOfInt rejectLevels, MatOfDouble levelWeights, double scaleFactor, int minNeighbors, int flags, Size minSize, Size maxSize)
    {
        Mat objects_mat = objects;
        Mat rejectLevels_mat = rejectLevels;
        Mat levelWeights_mat = levelWeights;
        detectMultiScale3_1(nativeObj, image.nativeObj, objects_mat.nativeObj, rejectLevels_mat.nativeObj, levelWeights_mat.nativeObj, scaleFactor, minNeighbors, flags, minSize.width, minSize.height, maxSize.width, maxSize.height);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale3(image, objects, rejectLevels, levelWeights, scaleFactor, minNeighbors, flags, minSize)
    public  void detectMultiScale3(Mat image, MatOfRect objects, MatOfInt rejectLevels, MatOfDouble levelWeights, double scaleFactor, int minNeighbors, int flags, Size minSize)
    {
        Mat objects_mat = objects;
        Mat rejectLevels_mat = rejectLevels;
        Mat levelWeights_mat = levelWeights;
        detectMultiScale3_2(nativeObj, image.nativeObj, objects_mat.nativeObj, rejectLevels_mat.nativeObj, levelWeights_mat.nativeObj, scaleFactor, minNeighbors, flags, minSize.width, minSize.height);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale3(image, objects, rejectLevels, levelWeights, scaleFactor, minNeighbors, flags)
    public  void detectMultiScale3(Mat image, MatOfRect objects, MatOfInt rejectLevels, MatOfDouble levelWeights, double scaleFactor, int minNeighbors, int flags)
    {
        Mat objects_mat = objects;
        Mat rejectLevels_mat = rejectLevels;
        Mat levelWeights_mat = levelWeights;
        detectMultiScale3_3(nativeObj, image.nativeObj, objects_mat.nativeObj, rejectLevels_mat.nativeObj, levelWeights_mat.nativeObj, scaleFactor, minNeighbors, flags);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale3(image, objects, rejectLevels, levelWeights, scaleFactor, minNeighbors)
    public  void detectMultiScale3(Mat image, MatOfRect objects, MatOfInt rejectLevels, MatOfDouble levelWeights, double scaleFactor, int minNeighbors)
    {
        Mat objects_mat = objects;
        Mat rejectLevels_mat = rejectLevels;
        Mat levelWeights_mat = levelWeights;
        detectMultiScale3_4(nativeObj, image.nativeObj, objects_mat.nativeObj, rejectLevels_mat.nativeObj, levelWeights_mat.nativeObj, scaleFactor, minNeighbors);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale3(image, objects, rejectLevels, levelWeights, scaleFactor)
    public  void detectMultiScale3(Mat image, MatOfRect objects, MatOfInt rejectLevels, MatOfDouble levelWeights, double scaleFactor)
    {
        Mat objects_mat = objects;
        Mat rejectLevels_mat = rejectLevels;
        Mat levelWeights_mat = levelWeights;
        detectMultiScale3_5(nativeObj, image.nativeObj, objects_mat.nativeObj, rejectLevels_mat.nativeObj, levelWeights_mat.nativeObj, scaleFactor);
        
        return;
    }

    //javadoc: CascadeClassifier::detectMultiScale3(image, objects, rejectLevels, levelWeights)
    public  void detectMultiScale3(Mat image, MatOfRect objects, MatOfInt rejectLevels, MatOfDouble levelWeights)
    {
        Mat objects_mat = objects;
        Mat rejectLevels_mat = rejectLevels;
        Mat levelWeights_mat = levelWeights;
        detectMultiScale3_6(nativeObj, image.nativeObj, objects_mat.nativeObj, rejectLevels_mat.nativeObj, levelWeights_mat.nativeObj);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::CascadeClassifier::CascadeClassifier(String filename)
    private static native long CascadeClassifier_0(String filename);

    // C++:   cv::CascadeClassifier::CascadeClassifier()
    private static native long CascadeClassifier_1();

    // C++:  Size cv::CascadeClassifier::getOriginalWindowSize()
    private static native double[] getOriginalWindowSize_0(long nativeObj);

    // C++: static bool cv::CascadeClassifier::convert(String oldcascade, String newcascade)
    private static native boolean convert_0(String oldcascade, String newcascade);

    // C++:  bool cv::CascadeClassifier::empty()
    private static native boolean empty_0(long nativeObj);

    // C++:  bool cv::CascadeClassifier::isOldFormatCascade()
    private static native boolean isOldFormatCascade_0(long nativeObj);

    // C++:  bool cv::CascadeClassifier::load(String filename)
    private static native boolean load_0(long nativeObj, String filename);

    // C++:  int cv::CascadeClassifier::getFeatureType()
    private static native int getFeatureType_0(long nativeObj);

    // C++:  void cv::CascadeClassifier::detectMultiScale(Mat image, vector_Rect& objects, double scaleFactor = 1.1, int minNeighbors = 3, int flags = 0, Size minSize = Size(), Size maxSize = Size())
    private static native void detectMultiScale_0(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, double scaleFactor, int minNeighbors, int flags, double minSize_width, double minSize_height, double maxSize_width, double maxSize_height);
    private static native void detectMultiScale_1(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, double scaleFactor, int minNeighbors, int flags, double minSize_width, double minSize_height);
    private static native void detectMultiScale_2(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, double scaleFactor, int minNeighbors, int flags);
    private static native void detectMultiScale_3(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, double scaleFactor, int minNeighbors);
    private static native void detectMultiScale_4(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, double scaleFactor);
    private static native void detectMultiScale_5(long nativeObj, long image_nativeObj, long objects_mat_nativeObj);

    // C++:  void cv::CascadeClassifier::detectMultiScale(Mat image, vector_Rect& objects, vector_int& numDetections, double scaleFactor = 1.1, int minNeighbors = 3, int flags = 0, Size minSize = Size(), Size maxSize = Size())
    private static native void detectMultiScale2_0(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long numDetections_mat_nativeObj, double scaleFactor, int minNeighbors, int flags, double minSize_width, double minSize_height, double maxSize_width, double maxSize_height);
    private static native void detectMultiScale2_1(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long numDetections_mat_nativeObj, double scaleFactor, int minNeighbors, int flags, double minSize_width, double minSize_height);
    private static native void detectMultiScale2_2(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long numDetections_mat_nativeObj, double scaleFactor, int minNeighbors, int flags);
    private static native void detectMultiScale2_3(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long numDetections_mat_nativeObj, double scaleFactor, int minNeighbors);
    private static native void detectMultiScale2_4(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long numDetections_mat_nativeObj, double scaleFactor);
    private static native void detectMultiScale2_5(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long numDetections_mat_nativeObj);

    // C++:  void cv::CascadeClassifier::detectMultiScale(Mat image, vector_Rect& objects, vector_int& rejectLevels, vector_double& levelWeights, double scaleFactor = 1.1, int minNeighbors = 3, int flags = 0, Size minSize = Size(), Size maxSize = Size(), bool outputRejectLevels = false)
    private static native void detectMultiScale3_0(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long rejectLevels_mat_nativeObj, long levelWeights_mat_nativeObj, double scaleFactor, int minNeighbors, int flags, double minSize_width, double minSize_height, double maxSize_width, double maxSize_height, boolean outputRejectLevels);
    private static native void detectMultiScale3_1(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long rejectLevels_mat_nativeObj, long levelWeights_mat_nativeObj, double scaleFactor, int minNeighbors, int flags, double minSize_width, double minSize_height, double maxSize_width, double maxSize_height);
    private static native void detectMultiScale3_2(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long rejectLevels_mat_nativeObj, long levelWeights_mat_nativeObj, double scaleFactor, int minNeighbors, int flags, double minSize_width, double minSize_height);
    private static native void detectMultiScale3_3(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long rejectLevels_mat_nativeObj, long levelWeights_mat_nativeObj, double scaleFactor, int minNeighbors, int flags);
    private static native void detectMultiScale3_4(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long rejectLevels_mat_nativeObj, long levelWeights_mat_nativeObj, double scaleFactor, int minNeighbors);
    private static native void detectMultiScale3_5(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long rejectLevels_mat_nativeObj, long levelWeights_mat_nativeObj, double scaleFactor);
    private static native void detectMultiScale3_6(long nativeObj, long image_nativeObj, long objects_mat_nativeObj, long rejectLevels_mat_nativeObj, long levelWeights_mat_nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
