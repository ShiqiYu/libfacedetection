pub mod scalar;

use crate::blob::{BlobView, BlobViewMut};
use crate::filter::PackedPointwiseFilter;

#[cfg(any(target_arch = "x86", target_arch = "x86_64"))]
mod x86;

pub fn pointwise_1x1_packed_to(
    input: BlobView<'_>,
    filter: &PackedPointwiseFilter,
    output: BlobViewMut<'_>,
) {
    #[cfg(any(target_arch = "x86", target_arch = "x86_64"))]
    {
        if x86::has_avx2() {
            // SAFETY: guarded by runtime AVX2 detection above. The AVX2 kernel
            // validates shapes before using raw pointers.
            unsafe {
                x86::pointwise_1x1_packed_avx2_to(input, filter, output);
            }
            return;
        }
    }

    scalar::pointwise_1x1_packed_to(input, filter, output);
}

pub fn depthwise_3x3_to(
    input: BlobView<'_>,
    weights: BlobView<'_>,
    biases: &[f32],
    output: BlobViewMut<'_>,
) {
    #[cfg(any(target_arch = "x86", target_arch = "x86_64"))]
    {
        if x86::has_avx2() && input.channels() % 8 == 0 {
            // SAFETY: guarded by runtime AVX2 detection above. The AVX2 kernel
            // validates shapes before using raw pointers.
            unsafe {
                x86::depthwise_3x3_avx2_to(input, weights, biases, output);
            }
            return;
        }
    }

    scalar::depthwise_3x3_to(input, weights, biases, output);
}

pub fn depthwise_3x3_relu_to(
    input: BlobView<'_>,
    weights: BlobView<'_>,
    biases: &[f32],
    output: BlobViewMut<'_>,
) {
    #[cfg(any(target_arch = "x86", target_arch = "x86_64"))]
    {
        if x86::has_avx2() && input.channels() % 8 == 0 {
            // SAFETY: guarded by runtime AVX2 detection above. The AVX2 kernel
            // validates shapes before using raw pointers.
            unsafe {
                x86::depthwise_3x3_relu_avx2_to(input, weights, biases, output);
            }
            return;
        }
    }

    scalar::depthwise_3x3_relu_to(input, weights, biases, output);
}

pub fn max_pool_2x2_s2_to(input: BlobView<'_>, output: BlobViewMut<'_>) {
    #[cfg(any(target_arch = "x86", target_arch = "x86_64"))]
    {
        if x86::has_avx2() && input.channels() % 8 == 0 {
            // SAFETY: guarded by runtime AVX2 detection above. The AVX2 kernel
            // validates shapes before using raw pointers.
            unsafe {
                x86::max_pool_2x2_s2_avx2_to(input, output);
            }
            return;
        }
    }

    scalar::max_pool_2x2_s2_to(input, output);
}

pub fn image_to_initial_conv_3x3_s2_to(
    pixels: &[u8],
    width: usize,
    height: usize,
    step: usize,
    filter: &PackedPointwiseFilter,
    output: BlobViewMut<'_>,
) -> bool {
    #[cfg(any(target_arch = "x86", target_arch = "x86_64"))]
    {
        if x86::has_avx2()
            && filter.channels() == 32
            && filter.padded_out_channels() == 16
            && output.channels() == filter.out_channels()
            && output.channel_step() == 16
        {
            // SAFETY: guarded by runtime AVX2 detection above. The AVX2 kernel
            // validates the fixed conv0 shape before using raw pointers.
            unsafe {
                x86::image_to_initial_conv_3x3_s2_avx2_to(
                    pixels, width, height, step, filter, output,
                );
            }
            return true;
        }
    }

    false
}
