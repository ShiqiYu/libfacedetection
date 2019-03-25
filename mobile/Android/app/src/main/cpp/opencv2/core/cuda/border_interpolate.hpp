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

#ifndef OPENCV_CUDA_BORDER_INTERPOLATE_HPP
#define OPENCV_CUDA_BORDER_INTERPOLATE_HPP

#include "saturate_cast.hpp"
#include "vec_traits.hpp"
#include "vec_math.hpp"

/** @file
 * @deprecated Use @ref cudev instead.
 */

//! @cond IGNORED

namespace cv { namespace cuda { namespace device
{
    //////////////////////////////////////////////////////////////
    // BrdConstant

    template <typename D> struct BrdRowConstant
    {
        typedef D result_type;

        explicit __host__ __device__ __forceinline__ BrdRowConstant(int width_, const D& val_ = VecTraits<D>::all(0)) : width(width_), val(val_) {}

        template <typename T> __device__ __forceinline__ D at_low(int x, const T* data) const
        {
            return x >= 0 ? saturate_cast<D>(data[x]) : val;
        }

        template <typename T> __device__ __forceinline__ D at_high(int x, const T* data) const
        {
            return x < width ? saturate_cast<D>(data[x]) : val;
        }

        template <typename T> __device__ __forceinline__ D at(int x, const T* data) const
        {
            return (x >= 0 && x < width) ? saturate_cast<D>(data[x]) : val;
        }

        int width;
        D val;
    };

    template <typename D> struct BrdColConstant
    {
        typedef D result_type;

        explicit __host__ __device__ __forceinline__ BrdColConstant(int height_, const D& val_ = VecTraits<D>::all(0)) : height(height_), val(val_) {}

        template <typename T> __device__ __forceinline__ D at_low(int y, const T* data, size_t step) const
        {
            return y >= 0 ? saturate_cast<D>(*(const T*)((const char*)data + y * step)) : val;
        }

        template <typename T> __device__ __forceinline__ D at_high(int y, const T* data, size_t step) const
        {
            return y < height ? saturate_cast<D>(*(const T*)((const char*)data + y * step)) : val;
        }

        template <typename T> __device__ __forceinline__ D at(int y, const T* data, size_t step) const
        {
            return (y >= 0 && y < height) ? saturate_cast<D>(*(const T*)((const char*)data + y * step)) : val;
        }

        int height;
        D val;
    };

    template <typename D> struct BrdConstant
    {
        typedef D result_type;

        __host__ __device__ __forceinline__ BrdConstant(int height_, int width_, const D& val_ = VecTraits<D>::all(0)) : height(height_), width(width_), val(val_)
        {
        }

        template <typename T> __device__ __forceinline__ D at(int y, int x, const T* data, size_t step) const
        {
            return (x >= 0 && x < width && y >= 0 && y < height) ? saturate_cast<D>(((const T*)((const uchar*)data + y * step))[x]) : val;
        }

        template <typename Ptr2D> __device__ __forceinline__ D at(typename Ptr2D::index_type y, typename Ptr2D::index_type x, const Ptr2D& src) const
        {
            return (x >= 0 && x < width && y >= 0 && y < height) ? saturate_cast<D>(src(y, x)) : val;
        }

        int height;
        int width;
        D val;
    };

    //////////////////////////////////////////////////////////////
    // BrdReplicate

    template <typename D> struct BrdRowReplicate
    {
        typedef D result_type;

        explicit __host__ __device__ __forceinline__ BrdRowReplicate(int width) : last_col(width - 1) {}
        template <typename U> __host__ __device__ __forceinline__ BrdRowReplicate(int width, U) : last_col(width - 1) {}

        __device__ __forceinline__ int idx_col_low(int x) const
        {
            return ::max(x, 0);
        }

        __device__ __forceinline__ int idx_col_high(int x) const
        {
            return ::min(x, last_col);
        }

        __device__ __forceinline__ int idx_col(int x) const
        {
            return idx_col_low(idx_col_high(x));
        }

        template <typename T> __device__ __forceinline__ D at_low(int x, const T* data) const
        {
            return saturate_cast<D>(data[idx_col_low(x)]);
        }

        template <typename T> __device__ __forceinline__ D at_high(int x, const T* data) const
        {
            return saturate_cast<D>(data[idx_col_high(x)]);
        }

        template <typename T> __device__ __forceinline__ D at(int x, const T* data) const
        {
            return saturate_cast<D>(data[idx_col(x)]);
        }

        int last_col;
    };

    template <typename D> struct BrdColReplicate
    {
        typedef D result_type;

        explicit __host__ __device__ __forceinline__ BrdColReplicate(int height) : last_row(height - 1) {}
        template <typename U> __host__ __device__ __forceinline__ BrdColReplicate(int height, U) : last_row(height - 1) {}

        __device__ __forceinline__ int idx_row_low(int y) const
        {
            return ::max(y, 0);
        }

        __device__ __forceinline__ int idx_row_high(int y) const
        {
            return ::min(y, last_row);
        }

        __device__ __forceinline__ int idx_row(int y) const
        {
            return idx_row_low(idx_row_high(y));
        }

        template <typename T> __device__ __forceinline__ D at_low(int y, const T* data, size_t step) const
        {
            return saturate_cast<D>(*(const T*)((const char*)data + idx_row_low(y) * step));
        }

        template <typename T> __device__ __forceinline__ D at_high(int y, const T* data, size_t step) const
        {
            return saturate_cast<D>(*(const T*)((const char*)data + idx_row_high(y) * step));
        }

        template <typename T> __device__ __forceinline__ D at(int y, const T* data, size_t step) const
        {
            return saturate_cast<D>(*(const T*)((const char*)data + idx_row(y) * step));
        }

        int last_row;
    };

    template <typename D> struct BrdReplicate
    {
        typedef D result_type;

        __host__ __device__ __forceinline__ BrdReplicate(int height, int width) : last_row(height - 1), last_col(width - 1) {}
        template <typename U> __host__ __device__ __forceinline__ BrdReplicate(int height, int width, U) : last_row(height - 1), last_col(width - 1) {}

        __device__ __forceinline__ int idx_row_low(int y) const
        {
            return ::max(y, 0);
        }

        __device__ __forceinline__ int idx_row_high(int y) const
        {
            return ::min(y, last_row);
        }

        __device__ __forceinline__ int idx_row(int y) const
        {
            return idx_row_low(idx_row_high(y));
        }

        __device__ __forceinline__ int idx_col_low(int x) const
        {
            return ::max(x, 0);
        }

        __device__ __forceinline__ int idx_col_high(int x) const
        {
            return ::min(x, last_col);
        }

        __device__ __forceinline__ int idx_col(int x) const
        {
            return idx_col_low(idx_col_high(x));
        }

        template <typename T> __device__ __forceinline__ D at(int y, int x, const T* data, size_t step) const
        {
            return saturate_cast<D>(((const T*)((const char*)data + idx_row(y) * step))[idx_col(x)]);
        }

        template <typename Ptr2D> __device__ __forceinline__ D at(typename Ptr2D::index_type y, typename Ptr2D::index_type x, const Ptr2D& src) const
        {
            return saturate_cast<D>(src(idx_row(y), idx_col(x)));
        }

        int last_row;
        int last_col;
    };

    //////////////////////////////////////////////////////////////
    // BrdReflect101

    template <typename D> struct BrdRowReflect101
    {
        typedef D result_type;

        explicit __host__ __device__ __forceinline__ BrdRowReflect101(int width) : last_col(width - 1) {}
        template <typename U> __host__ __device__ __forceinline__ BrdRowReflect101(int width, U) : last_col(width - 1) {}

        __device__ __forceinline__ int idx_col_low(int x) const
        {
            return ::abs(x) % (last_col + 1);
        }

        __device__ __forceinline__ int idx_col_high(int x) const
        {
            return ::abs(last_col - ::abs(last_col - x)) % (last_col + 1);
        }

        __device__ __forceinline__ int idx_col(int x) const
        {
            return idx_col_low(idx_col_high(x));
        }

        template <typename T> __device__ __forceinline__ D at_low(int x, const T* data) const
        {
            return saturate_cast<D>(data[idx_col_low(x)]);
        }

        template <typename T> __device__ __forceinline__ D at_high(int x, const T* data) const
        {
            return saturate_cast<D>(data[idx_col_high(x)]);
        }

        template <typename T> __device__ __forceinline__ D at(int x, const T* data) const
        {
            return saturate_cast<D>(data[idx_col(x)]);
        }

        int last_col;
    };

    template <typename D> struct BrdColReflect101
    {
        typedef D result_type;

        explicit __host__ __device__ __forceinline__ BrdColReflect101(int height) : last_row(height - 1) {}
        template <typename U> __host__ __device__ __forceinline__ BrdColReflect101(int height, U) : last_row(height - 1) {}

        __device__ __forceinline__ int idx_row_low(int y) const
        {
            return ::abs(y) % (last_row + 1);
        }

        __device__ __forceinline__ int idx_row_high(int y) const
        {
            return ::abs(last_row - ::abs(last_row - y)) % (last_row + 1);
        }

        __device__ __forceinline__ int idx_row(int y) const
        {
            return idx_row_low(idx_row_high(y));
        }

        template <typename T> __device__ __forceinline__ D at_low(int y, const T* data, size_t step) const
        {
            return saturate_cast<D>(*(const D*)((const char*)data + idx_row_low(y) * step));
        }

        template <typename T> __device__ __forceinline__ D at_high(int y, const T* data, size_t step) const
        {
            return saturate_cast<D>(*(const D*)((const char*)data + idx_row_high(y) * step));
        }

        template <typename T> __device__ __forceinline__ D at(int y, const T* data, size_t step) const
        {
            return saturate_cast<D>(*(const D*)((const char*)data + idx_row(y) * step));
        }

        int last_row;
    };

    template <typename D> struct BrdReflect101
    {
        typedef D result_type;

        __host__ __device__ __forceinline__ BrdReflect101(int height, int width) : last_row(height - 1), last_col(width - 1) {}
        template <typename U> __host__ __device__ __forceinline__ BrdReflect101(int height, int width, U) : last_row(height - 1), last_col(width - 1) {}

        __device__ __forceinline__ int idx_row_low(int y) const
        {
            return ::abs(y) % (last_row + 1);
        }

        __device__ __forceinline__ int idx_row_high(int y) const
        {
            return ::abs(last_row - ::abs(last_row - y)) % (last_row + 1);
        }

        __device__ __forceinline__ int idx_row(int y) const
        {
            return idx_row_low(idx_row_high(y));
        }

        __device__ __forceinline__ int idx_col_low(int x) const
        {
            return ::abs(x) % (last_col + 1);
        }

        __device__ __forceinline__ int idx_col_high(int x) const
        {
            return ::abs(last_col - ::abs(last_col - x)) % (last_col + 1);
        }

        __device__ __forceinline__ int idx_col(int x) const
        {
            return idx_col_low(idx_col_high(x));
        }

        template <typename T> __device__ __forceinline__ D at(int y, int x, const T* data, size_t step) const
        {
            return saturate_cast<D>(((const T*)((const char*)data + idx_row(y) * step))[idx_col(x)]);
        }

        template <typename Ptr2D> __device__ __forceinline__ D at(typename Ptr2D::index_type y, typename Ptr2D::index_type x, const Ptr2D& src) const
        {
            return saturate_cast<D>(src(idx_row(y), idx_col(x)));
        }

        int last_row;
        int last_col;
    };

    //////////////////////////////////////////////////////////////
    // BrdReflect

    template <typename D> struct BrdRowReflect
    {
        typedef D result_type;

        explicit __host__ __device__ __forceinline__ BrdRowReflect(int width) : last_col(width - 1) {}
        template <typename U> __host__ __device__ __forceinline__ BrdRowReflect(int width, U) : last_col(width - 1) {}

        __device__ __forceinline__ int idx_col_low(int x) const
        {
            return (::abs(x) - (x < 0)) % (last_col + 1);
        }

        __device__ __forceinline__ int idx_col_high(int x) const
        {
            return ::abs(last_col - ::abs(last_col - x) + (x > last_col)) % (last_col + 1);
        }

        __device__ __forceinline__ int idx_col(int x) const
        {
            return idx_col_high(::abs(x) - (x < 0));
        }

        template <typename T> __device__ __forceinline__ D at_low(int x, const T* data) const
        {
            return saturate_cast<D>(data[idx_col_low(x)]);
        }

        template <typename T> __device__ __forceinline__ D at_high(int x, const T* data) const
        {
            return saturate_cast<D>(data[idx_col_high(x)]);
        }

        template <typename T> __device__ __forceinline__ D at(int x, const T* data) const
        {
            return saturate_cast<D>(data[idx_col(x)]);
        }

        int last_col;
    };

    template <typename D> struct BrdColReflect
    {
        typedef D result_type;

        explicit __host__ __device__ __forceinline__ BrdColReflect(int height) : last_row(height - 1) {}
        template <typename U> __host__ __device__ __forceinline__ BrdColReflect(int height, U) : last_row(height - 1) {}

        __device__ __forceinline__ int idx_row_low(int y) const
        {
            return (::abs(y) - (y < 0)) % (last_row + 1);
        }

        __device__ __forceinline__ int idx_row_high(int y) const
        {
            return ::abs(last_row - ::abs(last_row - y) + (y > last_row)) % (last_row + 1);
        }

        __device__ __forceinline__ int idx_row(int y) const
        {
            return idx_row_high(::abs(y) - (y < 0));
        }

        template <typename T> __device__ __forceinline__ D at_low(int y, const T* data, size_t step) const
        {
            return saturate_cast<D>(*(const D*)((const char*)data + idx_row_low(y) * step));
        }

        template <typename T> __device__ __forceinline__ D at_high(int y, const T* data, size_t step) const
        {
            return saturate_cast<D>(*(const D*)((const char*)data + idx_row_high(y) * step));
        }

        template <typename T> __device__ __forceinline__ D at(int y, const T* data, size_t step) const
        {
            return saturate_cast<D>(*(const D*)((const char*)data + idx_row(y) * step));
        }

        int last_row;
    };

    template <typename D> struct BrdReflect
    {
        typedef D result_type;

        __host__ __device__ __forceinline__ BrdReflect(int height, int width) : last_row(height - 1), last_col(width - 1) {}
        template <typename U> __host__ __device__ __forceinline__ BrdReflect(int height, int width, U) : last_row(height - 1), last_col(width - 1) {}

        __device__ __forceinline__ int idx_row_low(int y) const
        {
            return (::abs(y) - (y < 0)) % (last_row + 1);
        }

        __device__ __forceinline__ int idx_row_high(int y) const
        {
            return /*::abs*/(last_row - ::abs(last_row - y) + (y > last_row)) /*% (last_row + 1)*/;
        }

        __device__ __forceinline__ int idx_row(int y) const
        {
            return idx_row_low(idx_row_high(y));
        }

        __device__ __forceinline__ int idx_col_low(int x) const
        {
            return (::abs(x) - (x < 0)) % (last_col + 1);
        }

        __device__ __forceinline__ int idx_col_high(int x) const
        {
            return (last_col - ::abs(last_col - x) + (x > last_col));
        }

        __device__ __forceinline__ int idx_col(int x) const
        {
            return idx_col_low(idx_col_high(x));
        }

        template <typename T> __device__ __forceinline__ D at(int y, int x, const T* data, size_t step) const
        {
            return saturate_cast<D>(((const T*)((const char*)data + idx_row(y) * step))[idx_col(x)]);
        }

        template <typename Ptr2D> __device__ __forceinline__ D at(typename Ptr2D::index_type y, typename Ptr2D::index_type x, const Ptr2D& src) const
        {
            return saturate_cast<D>(src(idx_row(y), idx_col(x)));
        }

        int last_row;
        int last_col;
    };

    //////////////////////////////////////////////////////////////
    // BrdWrap

    template <typename D> struct BrdRowWrap
    {
        typedef D result_type;

        explicit __host__ __device__ __forceinline__ BrdRowWrap(int width_) : width(width_) {}
        template <typename U> __host__ __device__ __forceinline__ BrdRowWrap(int width_, U) : width(width_) {}

        __device__ __forceinline__ int idx_col_low(int x) const
        {
            return (x >= 0) * x + (x < 0) * (x - ((x - width + 1) / width) * width);
        }

        __device__ __forceinline__ int idx_col_high(int x) const
        {
            return (x < width) * x + (x >= width) * (x % width);
        }

        __device__ __forceinline__ int idx_col(int x) const
        {
            return idx_col_high(idx_col_low(x));
        }

        template <typename T> __device__ __forceinline__ D at_low(int x, const T* data) const
        {
            return saturate_cast<D>(data[idx_col_low(x)]);
        }

        template <typename T> __device__ __forceinline__ D at_high(int x, const T* data) const
        {
            return saturate_cast<D>(data[idx_col_high(x)]);
        }

        template <typename T> __device__ __forceinline__ D at(int x, const T* data) const
        {
            return saturate_cast<D>(data[idx_col(x)]);
        }

        int width;
    };

    template <typename D> struct BrdColWrap
    {
        typedef D result_type;

        explicit __host__ __device__ __forceinline__ BrdColWrap(int height_) : height(height_) {}
        template <typename U> __host__ __device__ __forceinline__ BrdColWrap(int height_, U) : height(height_) {}

        __device__ __forceinline__ int idx_row_low(int y) const
        {
            return (y >= 0) * y + (y < 0) * (y - ((y - height + 1) / height) * height);
        }

        __device__ __forceinline__ int idx_row_high(int y) const
        {
            return (y < height) * y + (y >= height) * (y % height);
        }

        __device__ __forceinline__ int idx_row(int y) const
        {
            return idx_row_high(idx_row_low(y));
        }

        template <typename T> __device__ __forceinline__ D at_low(int y, const T* data, size_t step) const
        {
            return saturate_cast<D>(*(const D*)((const char*)data + idx_row_low(y) * step));
        }

        template <typename T> __device__ __forceinline__ D at_high(int y, const T* data, size_t step) const
        {
            return saturate_cast<D>(*(const D*)((const char*)data + idx_row_high(y) * step));
        }

        template <typename T> __device__ __forceinline__ D at(int y, const T* data, size_t step) const
        {
            return saturate_cast<D>(*(const D*)((const char*)data + idx_row(y) * step));
        }

        int height;
    };

    template <typename D> struct BrdWrap
    {
        typedef D result_type;

        __host__ __device__ __forceinline__ BrdWrap(int height_, int width_) :
            height(height_), width(width_)
        {
        }
        template <typename U>
        __host__ __device__ __forceinline__ BrdWrap(int height_, int width_, U) :
            height(height_), width(width_)
        {
        }

        __device__ __forceinline__ int idx_row_low(int y) const
        {
            return (y >= 0) ? y : (y - ((y - height + 1) / height) * height);
        }

        __device__ __forceinline__ int idx_row_high(int y) const
        {
            return (y < height) ? y : (y % height);
        }

        __device__ __forceinline__ int idx_row(int y) const
        {
            return idx_row_high(idx_row_low(y));
        }

        __device__ __forceinline__ int idx_col_low(int x) const
        {
            return (x >= 0) ? x : (x - ((x - width + 1) / width) * width);
        }

        __device__ __forceinline__ int idx_col_high(int x) const
        {
            return (x < width) ? x : (x % width);
        }

        __device__ __forceinline__ int idx_col(int x) const
        {
            return idx_col_high(idx_col_low(x));
        }

        template <typename T> __device__ __forceinline__ D at(int y, int x, const T* data, size_t step) const
        {
            return saturate_cast<D>(((const T*)((const char*)data + idx_row(y) * step))[idx_col(x)]);
        }

        template <typename Ptr2D> __device__ __forceinline__ D at(typename Ptr2D::index_type y, typename Ptr2D::index_type x, const Ptr2D& src) const
        {
            return saturate_cast<D>(src(idx_row(y), idx_col(x)));
        }

        int height;
        int width;
    };

    //////////////////////////////////////////////////////////////
    // BorderReader

    template <typename Ptr2D, typename B> struct BorderReader
    {
        typedef typename B::result_type elem_type;
        typedef typename Ptr2D::index_type index_type;

        __host__ __device__ __forceinline__ BorderReader(const Ptr2D& ptr_, const B& b_) : ptr(ptr_), b(b_) {}

        __device__ __forceinline__ elem_type operator ()(index_type y, index_type x) const
        {
            return b.at(y, x, ptr);
        }

        Ptr2D ptr;
        B b;
    };

    // under win32 there is some bug with templated types that passed as kernel parameters
    // with this specialization all works fine
    template <typename Ptr2D, typename D> struct BorderReader< Ptr2D, BrdConstant<D> >
    {
        typedef typename BrdConstant<D>::result_type elem_type;
        typedef typename Ptr2D::index_type index_type;

        __host__ __device__ __forceinline__ BorderReader(const Ptr2D& src_, const BrdConstant<D>& b) :
            src(src_), height(b.height), width(b.width), val(b.val)
        {
        }

        __device__ __forceinline__ D operator ()(index_type y, index_type x) const
        {
            return (x >= 0 && x < width && y >= 0 && y < height) ? saturate_cast<D>(src(y, x)) : val;
        }

        Ptr2D src;
        int height;
        int width;
        D val;
    };
}}} // namespace cv { namespace cuda { namespace cudev

//! @endcond

#endif // OPENCV_CUDA_BORDER_INTERPOLATE_HPP
