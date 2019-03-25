//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.utils.Converters;

// C++: class BOWTrainer
//javadoc: BOWTrainer

public class BOWTrainer {

    protected final long nativeObj;
    protected BOWTrainer(long addr) { nativeObj = addr; }

    public long getNativeObjAddr() { return nativeObj; }

    // internal usage only
    public static BOWTrainer __fromPtr__(long addr) { return new BOWTrainer(addr); }

    //
    // C++:  Mat cv::BOWTrainer::cluster(Mat descriptors)
    //

    //javadoc: BOWTrainer::cluster(descriptors)
    public  Mat cluster(Mat descriptors)
    {
        
        Mat retVal = new Mat(cluster_0(nativeObj, descriptors.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::BOWTrainer::cluster()
    //

    //javadoc: BOWTrainer::cluster()
    public  Mat cluster()
    {
        
        Mat retVal = new Mat(cluster_1(nativeObj));
        
        return retVal;
    }


    //
    // C++:  int cv::BOWTrainer::descriptorsCount()
    //

    //javadoc: BOWTrainer::descriptorsCount()
    public  int descriptorsCount()
    {
        
        int retVal = descriptorsCount_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  vector_Mat cv::BOWTrainer::getDescriptors()
    //

    //javadoc: BOWTrainer::getDescriptors()
    public  List<Mat> getDescriptors()
    {
        List<Mat> retVal = new ArrayList<Mat>();
        Mat retValMat = new Mat(getDescriptors_0(nativeObj));
        Converters.Mat_to_vector_Mat(retValMat, retVal);
        return retVal;
    }


    //
    // C++:  void cv::BOWTrainer::add(Mat descriptors)
    //

    //javadoc: BOWTrainer::add(descriptors)
    public  void add(Mat descriptors)
    {
        
        add_0(nativeObj, descriptors.nativeObj);
        
        return;
    }


    //
    // C++:  void cv::BOWTrainer::clear()
    //

    //javadoc: BOWTrainer::clear()
    public  void clear()
    {
        
        clear_0(nativeObj);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  Mat cv::BOWTrainer::cluster(Mat descriptors)
    private static native long cluster_0(long nativeObj, long descriptors_nativeObj);

    // C++:  Mat cv::BOWTrainer::cluster()
    private static native long cluster_1(long nativeObj);

    // C++:  int cv::BOWTrainer::descriptorsCount()
    private static native int descriptorsCount_0(long nativeObj);

    // C++:  vector_Mat cv::BOWTrainer::getDescriptors()
    private static native long getDescriptors_0(long nativeObj);

    // C++:  void cv::BOWTrainer::add(Mat descriptors)
    private static native void add_0(long nativeObj, long descriptors_nativeObj);

    // C++:  void cv::BOWTrainer::clear()
    private static native void clear_0(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
