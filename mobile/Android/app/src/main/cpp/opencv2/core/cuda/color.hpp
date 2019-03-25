/*M///////////////////////////////////////////////////////////////////////////////////////
//
//  IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.
//
//  By downloading, copying, installing or using the software you agree to this license.
//  If you do not agree to this license, do not download, install,
//  copy or use the software.
//
//
//                           License Agreement
//                For Open Source Computer Vision Library
//
// Copyright (C) 2000-2008, Intel Corporation, all rights reserved.
// Copyright (C) 2009, Willow Garage Inc., all rights reserved.
// Third party copyrights are property of their respective owners.
//
// Redistribution and use in source and binary forms, with or without modification,
// are permitted provided that the following conditions are met:
//
//   * Redistribution's of source code must retain the above copyright notice,
//     this list of conditions and the following disclaimer.
//
//   * Redistribution's in binary form must reproduce the above copyright notice,
//     this list of conditions and the following disclaimer in the documentation
//     and/or other materials provided with the distribution.
//
//   * The name of the copyright holders may not be used to endorse or promote products
//     derived from this software without specific prior written permission.
//
// This software is provided by the copyright holders and contributors "as is" and
// any express or implied warranties, including, but not limited to, the implied
// warranties of merchantability and fitness for a particular purpose are disclaimed.
// In no event shall the Intel Corporation or contributors be liable for any direct,
// indirect, incidental, special, exemplary, or consequential damages
// (including, but not limited to, procurement of substitute goods or services;
// loss of use, data, or profits; or business interruption) however caused
// and on any theory of liability, whether in contract, strict liability,
// or tort (including negligence or otherwise) arising in any way out of
// the use of this software, even if advised of the possibility of such damage.
//
//M*/

#ifndef OPENCV_CUDA_COLOR_HPP
#define OPENCV_CUDA_COLOR_HPP

#include "detail/color_detail.hpp"

/** @file
 * @deprecated Use @ref cudev instead.
 */

//! @cond IGNORED

namespace cv { namespace cuda { namespace device
{
    // All OPENCV_CUDA_IMPLEMENT_*_TRAITS(ColorSpace1_to_ColorSpace2, ...) macros implements
    // template <typename T> class ColorSpace1_to_ColorSpace2_traits
    // {
    //     typedef ... functor_type;
    //     static __host__ __device__ functor_type create_functor();
    // };

    OPENCV_CUDA_IMPLEMENT_RGB2RGB_TRAITS(bgr_to_rgb, 3, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2RGB_TRAITS(bgr_to_bgra, 3, 4, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2RGB_TRAITS(bgr_to_rgba, 3, 4, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2RGB_TRAITS(bgra_to_bgr, 4, 3, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2RGB_TRAITS(bgra_to_rgb, 4, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2RGB_TRAITS(bgra_to_rgba, 4, 4, 2)

    #undef OPENCV_CUDA_IMPLEMENT_RGB2RGB_TRAITS

    OPENCV_CUDA_IMPLEMENT_RGB2RGB5x5_TRAITS(bgr_to_bgr555, 3, 0, 5)
    OPENCV_CUDA_IMPLEMENT_RGB2RGB5x5_TRAITS(bgr_to_bgr565, 3, 0, 6)
    OPENCV_CUDA_IMPLEMENT_RGB2RGB5x5_TRAITS(rgb_to_bgr555, 3, 2, 5)
    OPENCV_CUDA_IMPLEMENT_RGB2RGB5x5_TRAITS(rgb_to_bgr565, 3, 2, 6)
    OPENCV_CUDA_IMPLEMENT_RGB2RGB5x5_TRAITS(bgra_to_bgr555, 4, 0, 5)
    OPENCV_CUDA_IMPLEMENT_RGB2RGB5x5_TRAITS(bgra_to_bgr565, 4, 0, 6)
    OPENCV_CUDA_IMPLEMENT_RGB2RGB5x5_TRAITS(rgba_to_bgr555, 4, 2, 5)
    OPENCV_CUDA_IMPLEMENT_RGB2RGB5x5_TRAITS(rgba_to_bgr565, 4, 2, 6)

    #undef OPENCV_CUDA_IMPLEMENT_RGB2RGB5x5_TRAITS

    OPENCV_CUDA_IMPLEMENT_RGB5x52RGB_TRAITS(bgr555_to_rgb, 3, 2, 5)
    OPENCV_CUDA_IMPLEMENT_RGB5x52RGB_TRAITS(bgr565_to_rgb, 3, 2, 6)
    OPENCV_CUDA_IMPLEMENT_RGB5x52RGB_TRAITS(bgr555_to_bgr, 3, 0, 5)
    OPENCV_CUDA_IMPLEMENT_RGB5x52RGB_TRAITS(bgr565_to_bgr, 3, 0, 6)
    OPENCV_CUDA_IMPLEMENT_RGB5x52RGB_TRAITS(bgr555_to_rgba, 4, 2, 5)
    OPENCV_CUDA_IMPLEMENT_RGB5x52RGB_TRAITS(bgr565_to_rgba, 4, 2, 6)
    OPENCV_CUDA_IMPLEMENT_RGB5x52RGB_TRAITS(bgr555_to_bgra, 4, 0, 5)
    OPENCV_CUDA_IMPLEMENT_RGB5x52RGB_TRAITS(bgr565_to_bgra, 4, 0, 6)

    #undef OPENCV_CUDA_IMPLEMENT_RGB5x52RGB_TRAITS

    OPENCV_CUDA_IMPLEMENT_GRAY2RGB_TRAITS(gray_to_bgr, 3)
    OPENCV_CUDA_IMPLEMENT_GRAY2RGB_TRAITS(gray_to_bgra, 4)

    #undef OPENCV_CUDA_IMPLEMENT_GRAY2RGB_TRAITS

    OPENCV_CUDA_IMPLEMENT_GRAY2RGB5x5_TRAITS(gray_to_bgr555, 5)
    OPENCV_CUDA_IMPLEMENT_GRAY2RGB5x5_TRAITS(gray_to_bgr565, 6)

    #undef OPENCV_CUDA_IMPLEMENT_GRAY2RGB5x5_TRAITS

    OPENCV_CUDA_IMPLEMENT_RGB5x52GRAY_TRAITS(bgr555_to_gray, 5)
    OPENCV_CUDA_IMPLEMENT_RGB5x52GRAY_TRAITS(bgr565_to_gray, 6)

    #undef OPENCV_CUDA_IMPLEMENT_RGB5x52GRAY_TRAITS

    OPENCV_CUDA_IMPLEMENT_RGB2GRAY_TRAITS(rgb_to_gray, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2GRAY_TRAITS(bgr_to_gray, 3, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2GRAY_TRAITS(rgba_to_gray, 4, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2GRAY_TRAITS(bgra_to_gray, 4, 0)

    #undef OPENCV_CUDA_IMPLEMENT_RGB2GRAY_TRAITS

    OPENCV_CUDA_IMPLEMENT_RGB2YUV_TRAITS(rgb_to_yuv, 3, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2YUV_TRAITS(rgba_to_yuv, 4, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2YUV_TRAITS(rgb_to_yuv4, 3, 4, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2YUV_TRAITS(rgba_to_yuv4, 4, 4, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2YUV_TRAITS(bgr_to_yuv, 3, 3, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2YUV_TRAITS(bgra_to_yuv, 4, 3, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2YUV_TRAITS(bgr_to_yuv4, 3, 4, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2YUV_TRAITS(bgra_to_yuv4, 4, 4, 0)

    #undef OPENCV_CUDA_IMPLEMENT_RGB2YUV_TRAITS

    OPENCV_CUDA_IMPLEMENT_YUV2RGB_TRAITS(yuv_to_rgb, 3, 3, 2)
    OPENCV_CUDA_IMPLEMENT_YUV2RGB_TRAITS(yuv_to_rgba, 3, 4, 2)
    OPENCV_CUDA_IMPLEMENT_YUV2RGB_TRAITS(yuv4_to_rgb, 4, 3, 2)
    OPENCV_CUDA_IMPLEMENT_YUV2RGB_TRAITS(yuv4_to_rgba, 4, 4, 2)
    OPENCV_CUDA_IMPLEMENT_YUV2RGB_TRAITS(yuv_to_bgr, 3, 3, 0)
    OPENCV_CUDA_IMPLEMENT_YUV2RGB_TRAITS(yuv_to_bgra, 3, 4, 0)
    OPENCV_CUDA_IMPLEMENT_YUV2RGB_TRAITS(yuv4_to_bgr, 4, 3, 0)
    OPENCV_CUDA_IMPLEMENT_YUV2RGB_TRAITS(yuv4_to_bgra, 4, 4, 0)

    #undef OPENCV_CUDA_IMPLEMENT_YUV2RGB_TRAITS

    OPENCV_CUDA_IMPLEMENT_RGB2YCrCb_TRAITS(rgb_to_YCrCb, 3, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2YCrCb_TRAITS(rgba_to_YCrCb, 4, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2YCrCb_TRAITS(rgb_to_YCrCb4, 3, 4, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2YCrCb_TRAITS(rgba_to_YCrCb4, 4, 4, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2YCrCb_TRAITS(bgr_to_YCrCb, 3, 3, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2YCrCb_TRAITS(bgra_to_YCrCb, 4, 3, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2YCrCb_TRAITS(bgr_to_YCrCb4, 3, 4, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2YCrCb_TRAITS(bgra_to_YCrCb4, 4, 4, 0)

    #undef OPENCV_CUDA_IMPLEMENT_RGB2YCrCb_TRAITS

    OPENCV_CUDA_IMPLEMENT_YCrCb2RGB_TRAITS(YCrCb_to_rgb, 3, 3, 2)
    OPENCV_CUDA_IMPLEMENT_YCrCb2RGB_TRAITS(YCrCb_to_rgba, 3, 4, 2)
    OPENCV_CUDA_IMPLEMENT_YCrCb2RGB_TRAITS(YCrCb4_to_rgb, 4, 3, 2)
    OPENCV_CUDA_IMPLEMENT_YCrCb2RGB_TRAITS(YCrCb4_to_rgba, 4, 4, 2)
    OPENCV_CUDA_IMPLEMENT_YCrCb2RGB_TRAITS(YCrCb_to_bgr, 3, 3, 0)
    OPENCV_CUDA_IMPLEMENT_YCrCb2RGB_TRAITS(YCrCb_to_bgra, 3, 4, 0)
    OPENCV_CUDA_IMPLEMENT_YCrCb2RGB_TRAITS(YCrCb4_to_bgr, 4, 3, 0)
    OPENCV_CUDA_IMPLEMENT_YCrCb2RGB_TRAITS(YCrCb4_to_bgra, 4, 4, 0)

    #undef OPENCV_CUDA_IMPLEMENT_YCrCb2RGB_TRAITS

    OPENCV_CUDA_IMPLEMENT_RGB2XYZ_TRAITS(rgb_to_xyz, 3, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2XYZ_TRAITS(rgba_to_xyz, 4, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2XYZ_TRAITS(rgb_to_xyz4, 3, 4, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2XYZ_TRAITS(rgba_to_xyz4, 4, 4, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2XYZ_TRAITS(bgr_to_xyz, 3, 3, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2XYZ_TRAITS(bgra_to_xyz, 4, 3, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2XYZ_TRAITS(bgr_to_xyz4, 3, 4, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2XYZ_TRAITS(bgra_to_xyz4, 4, 4, 0)

    #undef OPENCV_CUDA_IMPLEMENT_RGB2XYZ_TRAITS

    OPENCV_CUDA_IMPLEMENT_XYZ2RGB_TRAITS(xyz_to_rgb, 3, 3, 2)
    OPENCV_CUDA_IMPLEMENT_XYZ2RGB_TRAITS(xyz4_to_rgb, 4, 3, 2)
    OPENCV_CUDA_IMPLEMENT_XYZ2RGB_TRAITS(xyz_to_rgba, 3, 4, 2)
    OPENCV_CUDA_IMPLEMENT_XYZ2RGB_TRAITS(xyz4_to_rgba, 4, 4, 2)
    OPENCV_CUDA_IMPLEMENT_XYZ2RGB_TRAITS(xyz_to_bgr, 3, 3, 0)
    OPENCV_CUDA_IMPLEMENT_XYZ2RGB_TRAITS(xyz4_to_bgr, 4, 3, 0)
    OPENCV_CUDA_IMPLEMENT_XYZ2RGB_TRAITS(xyz_to_bgra, 3, 4, 0)
    OPENCV_CUDA_IMPLEMENT_XYZ2RGB_TRAITS(xyz4_to_bgra, 4, 4, 0)

    #undef OPENCV_CUDA_IMPLEMENT_XYZ2RGB_TRAITS

    OPENCV_CUDA_IMPLEMENT_RGB2HSV_TRAITS(rgb_to_hsv, 3, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2HSV_TRAITS(rgba_to_hsv, 4, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2HSV_TRAITS(rgb_to_hsv4, 3, 4, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2HSV_TRAITS(rgba_to_hsv4, 4, 4, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2HSV_TRAITS(bgr_to_hsv, 3, 3, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2HSV_TRAITS(bgra_to_hsv, 4, 3, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2HSV_TRAITS(bgr_to_hsv4, 3, 4, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2HSV_TRAITS(bgra_to_hsv4, 4, 4, 0)

    #undef OPENCV_CUDA_IMPLEMENT_RGB2HSV_TRAITS

    OPENCV_CUDA_IMPLEMENT_HSV2RGB_TRAITS(hsv_to_rgb, 3, 3, 2)
    OPENCV_CUDA_IMPLEMENT_HSV2RGB_TRAITS(hsv_to_rgba, 3, 4, 2)
    OPENCV_CUDA_IMPLEMENT_HSV2RGB_TRAITS(hsv4_to_rgb, 4, 3, 2)
    OPENCV_CUDA_IMPLEMENT_HSV2RGB_TRAITS(hsv4_to_rgba, 4, 4, 2)
    OPENCV_CUDA_IMPLEMENT_HSV2RGB_TRAITS(hsv_to_bgr, 3, 3, 0)
    OPENCV_CUDA_IMPLEMENT_HSV2RGB_TRAITS(hsv_to_bgra, 3, 4, 0)
    OPENCV_CUDA_IMPLEMENT_HSV2RGB_TRAITS(hsv4_to_bgr, 4, 3, 0)
    OPENCV_CUDA_IMPLEMENT_HSV2RGB_TRAITS(hsv4_to_bgra, 4, 4, 0)

    #undef OPENCV_CUDA_IMPLEMENT_HSV2RGB_TRAITS

    OPENCV_CUDA_IMPLEMENT_RGB2HLS_TRAITS(rgb_to_hls, 3, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2HLS_TRAITS(rgba_to_hls, 4, 3, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2HLS_TRAITS(rgb_to_hls4, 3, 4, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2HLS_TRAITS(rgba_to_hls4, 4, 4, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2HLS_TRAITS(bgr_to_hls, 3, 3, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2HLS_TRAITS(bgra_to_hls, 4, 3, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2HLS_TRAITS(bgr_to_hls4, 3, 4, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2HLS_TRAITS(bgra_to_hls4, 4, 4, 0)

    #undef OPENCV_CUDA_IMPLEMENT_RGB2HLS_TRAITS

    OPENCV_CUDA_IMPLEMENT_HLS2RGB_TRAITS(hls_to_rgb, 3, 3, 2)
    OPENCV_CUDA_IMPLEMENT_HLS2RGB_TRAITS(hls_to_rgba, 3, 4, 2)
    OPENCV_CUDA_IMPLEMENT_HLS2RGB_TRAITS(hls4_to_rgb, 4, 3, 2)
    OPENCV_CUDA_IMPLEMENT_HLS2RGB_TRAITS(hls4_to_rgba, 4, 4, 2)
    OPENCV_CUDA_IMPLEMENT_HLS2RGB_TRAITS(hls_to_bgr, 3, 3, 0)
    OPENCV_CUDA_IMPLEMENT_HLS2RGB_TRAITS(hls_to_bgra, 3, 4, 0)
    OPENCV_CUDA_IMPLEMENT_HLS2RGB_TRAITS(hls4_to_bgr, 4, 3, 0)
    OPENCV_CUDA_IMPLEMENT_HLS2RGB_TRAITS(hls4_to_bgra, 4, 4, 0)

    #undef OPENCV_CUDA_IMPLEMENT_HLS2RGB_TRAITS

    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(rgb_to_lab, 3, 3, true, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(rgba_to_lab, 4, 3, true, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(rgb_to_lab4, 3, 4, true, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(rgba_to_lab4, 4, 4, true, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(bgr_to_lab, 3, 3, true, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(bgra_to_lab, 4, 3, true, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(bgr_to_lab4, 3, 4, true, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(bgra_to_lab4, 4, 4, true, 0)

    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(lrgb_to_lab, 3, 3, false, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(lrgba_to_lab, 4, 3, false, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(lrgb_to_lab4, 3, 4, false, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(lrgba_to_lab4, 4, 4, false, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(lbgr_to_lab, 3, 3, false, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(lbgra_to_lab, 4, 3, false, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(lbgr_to_lab4, 3, 4, false, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS(lbgra_to_lab4, 4, 4, false, 0)

    #undef OPENCV_CUDA_IMPLEMENT_RGB2Lab_TRAITS

    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab_to_rgb, 3, 3, true, 2)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab4_to_rgb, 4, 3, true, 2)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab_to_rgba, 3, 4, true, 2)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab4_to_rgba, 4, 4, true, 2)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab_to_bgr, 3, 3, true, 0)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab4_to_bgr, 4, 3, true, 0)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab_to_bgra, 3, 4, true, 0)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab4_to_bgra, 4, 4, true, 0)

    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab_to_lrgb, 3, 3, false, 2)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab4_to_lrgb, 4, 3, false, 2)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab_to_lrgba, 3, 4, false, 2)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab4_to_lrgba, 4, 4, false, 2)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab_to_lbgr, 3, 3, false, 0)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab4_to_lbgr, 4, 3, false, 0)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab_to_lbgra, 3, 4, false, 0)
    OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS(lab4_to_lbgra, 4, 4, false, 0)

    #undef OPENCV_CUDA_IMPLEMENT_Lab2RGB_TRAITS

    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(rgb_to_luv, 3, 3, true, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(rgba_to_luv, 4, 3, true, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(rgb_to_luv4, 3, 4, true, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(rgba_to_luv4, 4, 4, true, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(bgr_to_luv, 3, 3, true, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(bgra_to_luv, 4, 3, true, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(bgr_to_luv4, 3, 4, true, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(bgra_to_luv4, 4, 4, true, 0)

    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(lrgb_to_luv, 3, 3, false, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(lrgba_to_luv, 4, 3, false, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(lrgb_to_luv4, 3, 4, false, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(lrgba_to_luv4, 4, 4, false, 2)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(lbgr_to_luv, 3, 3, false, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(lbgra_to_luv, 4, 3, false, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(lbgr_to_luv4, 3, 4, false, 0)
    OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS(lbgra_to_luv4, 4, 4, false, 0)

    #undef OPENCV_CUDA_IMPLEMENT_RGB2Luv_TRAITS

    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv_to_rgb, 3, 3, true, 2)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv4_to_rgb, 4, 3, true, 2)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv_to_rgba, 3, 4, true, 2)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv4_to_rgba, 4, 4, true, 2)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv_to_bgr, 3, 3, true, 0)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv4_to_bgr, 4, 3, true, 0)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv_to_bgra, 3, 4, true, 0)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv4_to_bgra, 4, 4, true, 0)

    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv_to_lrgb, 3, 3, false, 2)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv4_to_lrgb, 4, 3, false, 2)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv_to_lrgba, 3, 4, false, 2)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv4_to_lrgba, 4, 4, false, 2)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv_to_lbgr, 3, 3, false, 0)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv4_to_lbgr, 4, 3, false, 0)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv_to_lbgra, 3, 4, false, 0)
    OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS(luv4_to_lbgra, 4, 4, false, 0)

    #undef OPENCV_CUDA_IMPLEMENT_Luv2RGB_TRAITS
}}} // namespace cv { namespace cuda { namespace cudev

//! @endcond

#endif // OPENCV_CUDA_COLOR_HPP
