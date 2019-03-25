//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.imgproc;

import org.opencv.imgproc.GeneralizedHough;

// C++: class GeneralizedHoughBallard
//javadoc: GeneralizedHoughBallard

public class GeneralizedHoughBallard extends GeneralizedHough {

    protected GeneralizedHoughBallard(long addr) { super(addr); }

    // internal usage only
    public static GeneralizedHoughBallard __fromPtr__(long addr) { return new GeneralizedHoughBallard(addr); }

    //
    // C++:  int cv::GeneralizedHoughBallard::getLevels()
    //

    //javadoc: GeneralizedHoughBallard::getLevels()
    public  int getLevels()
    {
        
        int retVal = getLevels_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  int cv::GeneralizedHoughBallard::getVotesThreshold()
    //

    //javadoc: GeneralizedHoughBallard::getVotesThreshold()
    public  int getVotesThreshold()
    {
        
        int retVal = getVotesThreshold_0(nativeObj);
        
        return retVal;
    }


    //
    // C++:  void cv::GeneralizedHoughBallard::setLevels(int levels)
    //

    //javadoc: GeneralizedHoughBallard::setLevels(levels)
    public  void setLevels(int levels)
    {
        
        setLevels_0(nativeObj, levels);
        
        return;
    }


    //
    // C++:  void cv::GeneralizedHoughBallard::setVotesThreshold(int votesThreshold)
    //

    //javadoc: GeneralizedHoughBallard::setVotesThreshold(votesThreshold)
    public  void setVotesThreshold(int votesThreshold)
    {
        
        setVotesThreshold_0(nativeObj, votesThreshold);
        
        return;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:  int cv::GeneralizedHoughBallard::getLevels()
    private static native int getLevels_0(long nativeObj);

    // C++:  int cv::GeneralizedHoughBallard::getVotesThreshold()
    private static native int getVotesThreshold_0(long nativeObj);

    // C++:  void cv::GeneralizedHoughBallard::setLevels(int levels)
    private static native void setLevels_0(long nativeObj, int levels);

    // C++:  void cv::GeneralizedHoughBallard::setVotesThreshold(int votesThreshold)
    private static native void setVotesThreshold_0(long nativeObj, int votesThreshold);

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
