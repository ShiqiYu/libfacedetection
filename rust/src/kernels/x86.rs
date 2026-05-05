use crate::blob::{BlobView, BlobViewMut};
use crate::filter::PackedPointwiseFilter;

#[cfg(target_arch = "x86")]
use core::arch::x86::{
    __m256, _mm256_add_ps, _mm256_loadu_ps, _mm256_max_ps, _mm256_mul_ps, _mm256_set1_ps,
    _mm256_storeu_ps,
};
#[cfg(target_arch = "x86_64")]
use core::arch::x86_64::{
    __m256, _mm256_add_ps, _mm256_loadu_ps, _mm256_max_ps, _mm256_mul_ps, _mm256_set1_ps,
    _mm256_storeu_ps,
};

pub fn has_avx2() -> bool {
    #[cfg(target_arch = "x86")]
    {
        std::arch::is_x86_feature_detected!("avx2")
    }
    #[cfg(target_arch = "x86_64")]
    {
        std::arch::is_x86_feature_detected!("avx2")
    }
}

#[target_feature(enable = "avx2")]
pub unsafe fn pointwise_1x1_packed_avx2_to(
    input: BlobView<'_>,
    filter: &PackedPointwiseFilter,
    mut output: BlobViewMut<'_>,
) {
    assert_eq!(filter.channels(), input.channels());
    assert_eq!(output.rows(), input.rows());
    assert_eq!(output.cols(), input.cols());
    assert_eq!(output.channels(), filter.out_channels());
    assert_eq!(filter.padded_out_channels() % 8, 0);

    let input_data = input.data().as_ptr();
    let weights = filter.weights().as_ptr();
    let biases = filter.biases().as_ptr();
    let output_data = output.data_mut().as_mut_ptr();
    let input_channels = filter.channels();
    let padded_out_channels = filter.padded_out_channels();

    for row in 0..input.rows() {
        for col in 0..input.cols() {
            let args = PointwisePixelArgs {
                input_data,
                weights,
                biases,
                output_data,
                in_start: input.pixel_offset(row, col),
                out_start: output.pixel_offset(row, col),
                input_channels,
                padded_out_channels,
            };

            // SAFETY: shape assertions above guarantee that every computed
            // offset stays within the input, bias, weight, and output slices.
            unsafe {
                match padded_out_channels {
                    16 => pointwise_pixel_avx2_vectors::<2>(args),
                    32 => pointwise_pixel_avx2_vectors::<4>(args),
                    64 => pointwise_pixel_avx2_vectors::<8>(args),
                    _ => pointwise_pixel_avx2_dynamic(args),
                }
            }
        }
    }
}

#[derive(Clone, Copy)]
struct PointwisePixelArgs {
    input_data: *const f32,
    weights: *const f32,
    biases: *const f32,
    output_data: *mut f32,
    in_start: usize,
    out_start: usize,
    input_channels: usize,
    padded_out_channels: usize,
}

#[inline]
unsafe fn pointwise_pixel_avx2_vectors<const VECTORS: usize>(args: PointwisePixelArgs) {
    // SAFETY: callers pass pointers and offsets validated by the enclosing
    // kernel's shape assertions. VECTORS * 8 equals padded_out_channels for
    // all call sites in this file.
    unsafe {
        let mut acc: [__m256; VECTORS] =
            core::array::from_fn(|idx| loadu_ps(args.biases.add(idx * 8)));
        for ic in 0..args.input_channels {
            let input_value = set1_ps(*args.input_data.add(args.in_start + ic));
            let weight_start = ic * args.padded_out_channels;
            for (vector_idx, acc_value) in acc.iter_mut().enumerate() {
                let offset = vector_idx * 8;
                let weight = loadu_ps(args.weights.add(weight_start + offset));
                *acc_value = add_ps(*acc_value, mul_ps(input_value, weight));
            }
        }
        for (vector_idx, acc_value) in acc.iter().copied().enumerate() {
            storeu_ps(
                args.output_data.add(args.out_start + vector_idx * 8),
                acc_value,
            );
        }
    }
}

#[inline]
unsafe fn pointwise_pixel_avx2_dynamic(args: PointwisePixelArgs) {
    let output_vectors = args.padded_out_channels / 8;
    for vector_idx in 0..output_vectors {
        let offset = vector_idx * 8;
        // SAFETY: callers pass pointers and offsets validated by the enclosing
        // kernel's shape assertions.
        unsafe {
            let mut acc = loadu_ps(args.biases.add(offset));
            for ic in 0..args.input_channels {
                let input_value = set1_ps(*args.input_data.add(args.in_start + ic));
                let weight_offset = ic * args.padded_out_channels + offset;
                let weight = loadu_ps(args.weights.add(weight_offset));
                acc = add_ps(acc, mul_ps(input_value, weight));
            }
            storeu_ps(args.output_data.add(args.out_start + offset), acc);
        }
    }
}

#[target_feature(enable = "avx2")]
pub unsafe fn depthwise_3x3_avx2_to(
    input: BlobView<'_>,
    weights: BlobView<'_>,
    biases: &[f32],
    output: BlobViewMut<'_>,
) {
    unsafe {
        depthwise_3x3_avx2_impl::<false>(input, weights, biases, output);
    }
}

#[target_feature(enable = "avx2")]
pub unsafe fn depthwise_3x3_relu_avx2_to(
    input: BlobView<'_>,
    weights: BlobView<'_>,
    biases: &[f32],
    output: BlobViewMut<'_>,
) {
    unsafe {
        depthwise_3x3_avx2_impl::<true>(input, weights, biases, output);
    }
}

#[target_feature(enable = "avx2")]
unsafe fn depthwise_3x3_avx2_impl<const DO_RELU: bool>(
    input: BlobView<'_>,
    weights: BlobView<'_>,
    biases: &[f32],
    mut output: BlobViewMut<'_>,
) {
    assert_eq!(weights.rows(), 1);
    assert_eq!(weights.cols(), 9);
    assert_eq!(weights.channels(), input.channels());
    assert_eq!(biases.len(), input.channels());
    assert_eq!(output.rows(), input.rows());
    assert_eq!(output.cols(), input.cols());
    assert_eq!(output.channels(), input.channels());
    assert_eq!(input.channels() % 8, 0);

    let input_data = input.data().as_ptr();
    let weight_data = weights.data().as_ptr();
    let bias_data = biases.as_ptr();
    let output_data = output.data_mut().as_mut_ptr();
    let channel_vectors = input.channels() / 8;

    for row in 0..output.rows() {
        let is_interior_row = row > 0 && row + 1 < input.rows();
        for col in 0..output.cols() {
            let is_interior = is_interior_row && col > 0 && col + 1 < input.cols();
            let out_start = output.pixel_offset(row, col);

            for vector_idx in 0..channel_vectors {
                let ch = vector_idx * 8;
                // SAFETY: shape assertions above guarantee every channel block
                // and pixel offset is in range. The caller performed AVX2
                // runtime feature detection.
                unsafe {
                    let mut acc = loadu_ps(bias_data.add(ch));

                    if is_interior {
                        acc = add_depthwise_3x3_sample(
                            input_data,
                            weight_data,
                            input.pixel_offset(row - 1, col - 1) + ch,
                            weights.pixel_offset(0, 0) + ch,
                            acc,
                        );
                        acc = add_depthwise_3x3_sample(
                            input_data,
                            weight_data,
                            input.pixel_offset(row - 1, col) + ch,
                            weights.pixel_offset(0, 1) + ch,
                            acc,
                        );
                        acc = add_depthwise_3x3_sample(
                            input_data,
                            weight_data,
                            input.pixel_offset(row - 1, col + 1) + ch,
                            weights.pixel_offset(0, 2) + ch,
                            acc,
                        );
                        acc = add_depthwise_3x3_sample(
                            input_data,
                            weight_data,
                            input.pixel_offset(row, col - 1) + ch,
                            weights.pixel_offset(0, 3) + ch,
                            acc,
                        );
                        acc = add_depthwise_3x3_sample(
                            input_data,
                            weight_data,
                            input.pixel_offset(row, col) + ch,
                            weights.pixel_offset(0, 4) + ch,
                            acc,
                        );
                        acc = add_depthwise_3x3_sample(
                            input_data,
                            weight_data,
                            input.pixel_offset(row, col + 1) + ch,
                            weights.pixel_offset(0, 5) + ch,
                            acc,
                        );
                        acc = add_depthwise_3x3_sample(
                            input_data,
                            weight_data,
                            input.pixel_offset(row + 1, col - 1) + ch,
                            weights.pixel_offset(0, 6) + ch,
                            acc,
                        );
                        acc = add_depthwise_3x3_sample(
                            input_data,
                            weight_data,
                            input.pixel_offset(row + 1, col) + ch,
                            weights.pixel_offset(0, 7) + ch,
                            acc,
                        );
                        acc = add_depthwise_3x3_sample(
                            input_data,
                            weight_data,
                            input.pixel_offset(row + 1, col + 1) + ch,
                            weights.pixel_offset(0, 8) + ch,
                            acc,
                        );
                    } else {
                        let src_row_start = row.saturating_sub(1);
                        let src_row_end = (row + 2).min(input.rows());
                        let src_col_start = col.saturating_sub(1);
                        let src_col_end = (col + 2).min(input.cols());
                        for src_row in src_row_start..src_row_end {
                            for src_col in src_col_start..src_col_end {
                                let filter_row = src_row + 1 - row;
                                let filter_col = src_col + 1 - col;
                                let filter_idx = filter_row * 3 + filter_col;
                                let in_start = input.pixel_offset(src_row, src_col);
                                let weight_start = weights.pixel_offset(0, filter_idx);
                                acc = add_depthwise_3x3_sample(
                                    input_data,
                                    weight_data,
                                    in_start + ch,
                                    weight_start + ch,
                                    acc,
                                );
                            }
                        }
                    }

                    if DO_RELU {
                        acc = max_ps(acc, set1_ps(0.0));
                    }
                    storeu_ps(output_data.add(out_start + ch), acc);
                }
            }
        }
    }
}

#[inline]
unsafe fn add_depthwise_3x3_sample(
    input_data: *const f32,
    weight_data: *const f32,
    input_offset: usize,
    weight_offset: usize,
    acc: __m256,
) -> __m256 {
    unsafe {
        let input_values = loadu_ps(input_data.add(input_offset));
        let weight_values = loadu_ps(weight_data.add(weight_offset));
        add_ps(acc, mul_ps(input_values, weight_values))
    }
}

#[target_feature(enable = "avx2")]
pub unsafe fn max_pool_2x2_s2_avx2_to(input: BlobView<'_>, mut output: BlobViewMut<'_>) {
    assert_eq!(
        output.rows(),
        crate::kernels::scalar::max_pool_output_size(input.rows())
    );
    assert_eq!(
        output.cols(),
        crate::kernels::scalar::max_pool_output_size(input.cols())
    );
    assert_eq!(output.channels(), input.channels());
    assert_eq!(input.channels() % 8, 0);

    let input_data = input.data().as_ptr();
    let output_data = output.data_mut().as_mut_ptr();
    let channel_vectors = input.channels() / 8;

    for row in 0..output.rows() {
        for col in 0..output.cols() {
            let src_row_start = row * 2;
            let src_col_start = col * 2;
            let src_row_end = (src_row_start + 2).min(input.rows());
            let src_col_end = (src_col_start + 2).min(input.cols());
            let out_start = output.pixel_offset(row, col);

            for vector_idx in 0..channel_vectors {
                let ch = vector_idx * 8;
                // SAFETY: shape assertions guarantee the output and all source
                // pixels are in range. The caller performed AVX2 runtime
                // feature detection.
                unsafe {
                    let first = input.pixel_offset(src_row_start, src_col_start) + ch;
                    let mut max_values = loadu_ps(input_data.add(first));
                    if src_col_start + 1 < src_col_end {
                        let offset = input.pixel_offset(src_row_start, src_col_start + 1) + ch;
                        max_values = max_ps(max_values, loadu_ps(input_data.add(offset)));
                    }
                    if src_row_start + 1 < src_row_end {
                        let offset = input.pixel_offset(src_row_start + 1, src_col_start) + ch;
                        max_values = max_ps(max_values, loadu_ps(input_data.add(offset)));
                        if src_col_start + 1 < src_col_end {
                            let offset =
                                input.pixel_offset(src_row_start + 1, src_col_start + 1) + ch;
                            max_values = max_ps(max_values, loadu_ps(input_data.add(offset)));
                        }
                    }
                    storeu_ps(output_data.add(out_start + ch), max_values);
                }
            }
        }
    }
}

#[target_feature(enable = "avx2")]
pub unsafe fn image_to_initial_conv_3x3_s2_avx2_to(
    pixels: &[u8],
    width: usize,
    height: usize,
    step: usize,
    filter: &PackedPointwiseFilter,
    mut output: BlobViewMut<'_>,
) {
    assert_eq!(filter.channels(), 32);
    assert_eq!(filter.padded_out_channels(), 16);
    assert_eq!(output.channels(), filter.out_channels());
    assert_eq!(output.channel_step(), 16);

    let pixel_data = pixels.as_ptr();
    let weights = filter.weights().as_ptr();
    let biases = filter.biases().as_ptr();
    let output_data = output.data_mut().as_mut_ptr();
    let output_cols = output.cols();
    let rows = output.rows();
    let cols = output.cols();

    for row in 0..rows {
        for col in 0..cols {
            let center_y = row * 2;
            let center_x = col * 2;
            let out_start = (row * output_cols + col) * 16;

            // SAFETY: shape assertions guarantee the output writes are in
            // range. Source pixel offsets are checked against image bounds
            // before every load. The caller performed AVX2 runtime detection.
            unsafe {
                let mut acc0 = loadu_ps(biases);
                let mut acc1 = loadu_ps(biases.add(8));

                if center_y > 0 && center_y + 1 < height && center_x > 0 && center_x + 1 < width {
                    let row0 = (center_y - 1) * step + (center_x - 1) * 3;
                    let row1 = center_y * step + (center_x - 1) * 3;
                    let row2 = (center_y + 1) * step + (center_x - 1) * 3;
                    for input_channel in 0..9 {
                        add_initial_conv_channel(
                            pixel_data,
                            weights,
                            row0 + input_channel,
                            input_channel,
                            &mut acc0,
                            &mut acc1,
                        );
                    }
                    for input_channel in 0..9 {
                        add_initial_conv_channel(
                            pixel_data,
                            weights,
                            row1 + input_channel,
                            input_channel + 9,
                            &mut acc0,
                            &mut acc1,
                        );
                    }
                    for input_channel in 0..9 {
                        add_initial_conv_channel(
                            pixel_data,
                            weights,
                            row2 + input_channel,
                            input_channel + 18,
                            &mut acc0,
                            &mut acc1,
                        );
                    }
                } else {
                    for fy in -1..=1 {
                        let src_y = center_y as isize + fy;
                        if src_y < 0 || src_y >= height as isize {
                            continue;
                        }
                        for fx in -1..=1 {
                            let src_x = center_x as isize + fx;
                            if src_x < 0 || src_x >= width as isize {
                                continue;
                            }

                            let src = src_y as usize * step + src_x as usize * 3;
                            let base_input_channel = ((fy + 1) * 3 + fx + 1) as usize * 3;
                            for channel_offset in 0..3 {
                                add_initial_conv_channel(
                                    pixel_data,
                                    weights,
                                    src + channel_offset,
                                    base_input_channel + channel_offset,
                                    &mut acc0,
                                    &mut acc1,
                                );
                            }
                        }
                    }
                }

                let zero = set1_ps(0.0);
                storeu_ps(output_data.add(out_start), max_ps(acc0, zero));
                storeu_ps(output_data.add(out_start + 8), max_ps(acc1, zero));
            }
        }
    }
}

#[inline]
unsafe fn add_initial_conv_channel(
    pixel_data: *const u8,
    weights: *const f32,
    pixel_offset: usize,
    input_channel: usize,
    acc0: &mut __m256,
    acc1: &mut __m256,
) {
    unsafe {
        let input_value = set1_ps(*pixel_data.add(pixel_offset) as f32);
        let weight_start = input_channel * 16;
        let weight0 = loadu_ps(weights.add(weight_start));
        let weight1 = loadu_ps(weights.add(weight_start + 8));
        *acc0 = add_ps(*acc0, mul_ps(input_value, weight0));
        *acc1 = add_ps(*acc1, mul_ps(input_value, weight1));
    }
}

#[inline]
unsafe fn loadu_ps(ptr: *const f32) -> __m256 {
    unsafe { _mm256_loadu_ps(ptr) }
}

#[inline]
unsafe fn storeu_ps(ptr: *mut f32, value: __m256) {
    unsafe { _mm256_storeu_ps(ptr, value) };
}

#[inline]
unsafe fn set1_ps(value: f32) -> __m256 {
    unsafe { _mm256_set1_ps(value) }
}

#[inline]
unsafe fn add_ps(left: __m256, right: __m256) -> __m256 {
    unsafe { _mm256_add_ps(left, right) }
}

#[inline]
unsafe fn mul_ps(left: __m256, right: __m256) -> __m256 {
    unsafe { _mm256_mul_ps(left, right) }
}

#[inline]
unsafe fn max_ps(left: __m256, right: __m256) -> __m256 {
    unsafe { _mm256_max_ps(left, right) }
}
