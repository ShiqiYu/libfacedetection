//
// This file is auto-generated. Please don't modify it!
//
package org.opencv.imgproc;

import org.opencv.imgproc.GeneralizedHough;

// C++: class GeneralizedHoughGuil
//javadoc: GeneralizedHoughGuil

public class GeneralizedHoughGuil extends GeneralizedHough {

    protected GeneralizedHoughGuil(long addr) { super(addr); }

    // internal usage only
    public static GeneralizedHoughGuil __fromPtr__(long addr) { return new GeneralizedHoughGuil(addr); }

    @Override
    protected void finalize() throws Throwable {
        delete(nativeObj);
    }



    // native support for java finalize()
    private static native void delete(long nativeObj);

}
