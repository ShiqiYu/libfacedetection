//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.features2d;

import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FlannBasedMatcher;

// C++: class FlannBasedMatcher
//javadoc: FlannBasedMatcher

public class FlannBasedMatcher extends DescriptorMatcher {

    protected FlannBasedMatcher(long addr) { super(addr); }

    // internal usage only
    public static FlannBasedMatcher __fromPtr__(long addr) { return new FlannBasedMatcher(addr); }

    //
    // C++:   cv::FlannBasedMatcher::FlannBasedMatcher(Ptr_flann_IndexParams indexParams = makePtr<flann::KDTreeIndexParams>(), Ptr_flann_SearchParams searchParams = makePtr<flann::SearchParams>())
    //

    //javadoc: FlannBasedMatcher::FlannBasedMatcher()
    public   FlannBasedMatcher()
    {
        
        super( FlannBasedMatcher_0() );
        
        return;
    }


    //
    // C++: static Ptr_FlannBasedMatcher cv::FlannBasedMatcher::create()
    //

    //javadoc: FlannBasedMatcher::create()
    public static FlannBasedMatcher create()
    {
        
        FlannBasedMatcher retVal = FlannBasedMatcher.__fromPtr__(create_0());
        
        return retVal;
    }


    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // C++:   cv::FlannBasedMatcher::FlannBasedMatcher(Ptr_flann_IndexParams indexParams = makePtr<flann::KDTreeIndexParams>(), Ptr_flann_SearchParams searchParams = makePtr<flann::SearchParams>())
    private static native long FlannBasedMatcher_0();

    // C++: static Ptr_FlannBasedMatcher cv::FlannBasedMatcher::create()
    private static native long create_0();

    // native support for java finalize()
    private static native void delete(long nativeObj);

}
