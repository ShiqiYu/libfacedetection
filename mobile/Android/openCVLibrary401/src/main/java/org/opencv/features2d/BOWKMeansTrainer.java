//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;

import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.features2d.BOWTrainer;

// C++: class BOWKMeansTrainer
//javadoc: BOWKMeansTrainer

public class BOWKMeansTrainer extends BOWTrainer {

    protected BOWKMeansTrainer(long addr) { super(addr); }

    // internal usage only
    public static BOWKMeansTrainer __fromPtr__(long addr) { return new BOWKMeansTrainer(addr); }

    //
    // C++:   cv::BOWKMeansTrainer::BOWKMeansTrainer(int clusterCount, TermCriteria termcrit = TermCriteria(), int attempts = 3, int flags = KMEANS_PP_CENTERS)
    //

    //javadoc: BOWKMeansTrainer::BOWKMeansTrainer(clusterCount, termcrit, attempts, flags)
    public   BOWKMeansTrainer(int clusterCount, TermCriteria termcrit, int attempts, int flags)
    {
        
        super( BOWKMeansTrainer_0(clusterCount, termcrit.type, termcrit.maxCount, termcrit.epsilon, attempts, flags) );
        
        return;
    }

    //javadoc: BOWKMeansTrainer::BOWKMeansTrainer(clusterCount, termcrit, attempts)
    public   BOWKMeansTrainer(int clusterCount, TermCriteria termcrit, int attempts)
    {
        
        super( BOWKMeansTrainer_1(clusterCount, termcrit.type, termcrit.maxCount, termcrit.epsilon, attempts) );
        
        return;
    }

    //javadoc: BOWKMeansTrainer::BOWKMeansTrainer(clusterCount, termcrit)
    public   BOWKMeansTrainer(int clusterCount, TermCriteria termcrit)
    {
        
        super( BOWKMeansTrainer_2(clusterCount, termcrit.type, termcrit.maxCount, termcrit.epsilon) );
        
        return;
    }

    //javadoc: BOWKMeansTrainer::BOWKMeansTrainer(clusterCount)
    public   BOWKMeansTrainer(int clusterCount)
    {
        
        super( BOWKMeansTrainer_3(clusterCount) );
        
        return;
    }


    //
    // C++:  Mat cv::BOWKMeansTrainer::cluster(Mat descriptors)
    //

    //javadoc: BOWKMeansTrainer::cluster(descriptors)
    public  Mat cluster(Mat descriptors)
    {
        
        Mat retVal = new Mat(cluster_0(nativeObj, descriptors.nativeObj));
        
        return retVal;
    }


    //
    // C++:  Mat cv::BOWKMeansTrainer::cluster()
    //

    //javadoc: BOWKMeansTrainer::cluster()
    public  Mat cluster()
    {
        
        Mat retVal = new Mat(cluster_1(nativeObj));
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::BOWKMeansTrainer::BOWKMeansTrainer(int clusterCount, TermCriteria termcrit = TermCriteria(), int attempts = 3, int flags = KMEANS_PP_CENTERS)
    private static native long BOWKMeansTrainer_0(int clusterCount, int termcrit_type, int termcrit_maxCount, double termcrit_epsilon, int attempts, int flags);
    private static native long BOWKMeansTrainer_1(int clusterCount, int termcrit_type, int termcrit_maxCount, double termcrit_epsilon, int attempts);
    private static native long BOWKMeansTrainer_2(int clusterCount, int termcrit_type, int termcrit_maxCount, double termcrit_epsilon);
    private static native long BOWKMeansTrainer_3(int clusterCount);

    // C++:  Mat cv::BOWKMeansTrainer::cluster(Mat descriptors)
    private static native long cluster_0(long nativeObj, long descriptors_nativeObj);

    // C++:  Mat cv::BOWKMeansTrainer::cluster()
    private static native long cluster_1(long nativeObj);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
