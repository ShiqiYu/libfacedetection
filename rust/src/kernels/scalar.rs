use crate::blob::{BlobView, BlobViewMut};
use crate::filter::PackedPointwiseFilter;

pub fn max_pool_output_size(input_size: usize) -> usize {
    let size = ((input_size as f32 - 3.0) / 2.0).ceil() as isize + 1;
    size.max(1) as usize
}

pub fn relu_in_place(data: &mut [f32]) {
    for value in data {
        *value = value.max(0.0);
    }
}

pub fn add_to(lhs: BlobView<'_>, rhs: BlobView<'_>, mut output: BlobViewMut<'_>) {
    assert_same_shape(lhs, rhs);
    assert_same_mut_shape(lhs, &output);

    for row in 0..lhs.rows() {
        for col in 0..lhs.cols() {
            let lhs_start = lhs.pixel_offset(row, col);
            let rhs_start = rhs.pixel_offset(row, col);
            let out_start = output.pixel_offset(row, col);
            for ch in 0..lhs.channels() {
                output.data_mut()[out_start + ch] =
                    lhs.data()[lhs_start + ch] + rhs.data()[rhs_start + ch];
            }
            clear_pixel_padding(&mut output, out_start);
        }
    }
}

pub fn pointwise_1x1_to(
    input: BlobView<'_>,
    weights: BlobView<'_>,
    biases: &[f32],
    mut output: BlobViewMut<'_>,
) {
    assert_eq!(weights.rows(), 1);
    assert_eq!(weights.channels(), input.channels());
    assert_eq!(biases.len(), weights.cols());
    assert_eq!(output.rows(), input.rows());
    assert_eq!(output.cols(), input.cols());
    assert_eq!(output.channels(), weights.cols());

    if weights.cols() <= 10 {
        pointwise_1x1_small_to(input, weights, biases, output);
        return;
    }

    for row in 0..input.rows() {
        for col in 0..input.cols() {
            let in_start = input.pixel_offset(row, col);
            let out_start = output.pixel_offset(row, col);

            for (oc, bias) in biases.iter().copied().enumerate() {
                let weight_start = weights.pixel_offset(0, oc);
                let mut sum = bias;
                for ic in 0..input.channels() {
                    sum += input.data()[in_start + ic] * weights.data()[weight_start + ic];
                }
                output.data_mut()[out_start + oc] = sum;
            }
            clear_pixel_padding(&mut output, out_start);
        }
    }
}

fn pointwise_1x1_small_to(
    input: BlobView<'_>,
    weights: BlobView<'_>,
    biases: &[f32],
    mut output: BlobViewMut<'_>,
) {
    let out_channels = weights.cols();

    for row in 0..input.rows() {
        for col in 0..input.cols() {
            let in_start = input.pixel_offset(row, col);
            let out_start = output.pixel_offset(row, col);
            let output_channel_step = output.channel_step();
            let mut sums = [0.0; 10];
            sums[..out_channels].copy_from_slice(biases);

            for ic in 0..input.channels() {
                let input_value = input.data()[in_start + ic];
                for (oc, sum) in sums.iter_mut().enumerate().take(out_channels) {
                    *sum += input_value * weights.data()[weights.pixel_offset(0, oc) + ic];
                }
            }

            let output_pixel = &mut output.data_mut()[out_start..out_start + output_channel_step];
            output_pixel[..out_channels].copy_from_slice(&sums[..out_channels]);
            output_pixel[out_channels..].fill(0.0);
        }
    }
}

pub fn pointwise_1x1_packed_to(
    input: BlobView<'_>,
    filter: &PackedPointwiseFilter,
    mut output: BlobViewMut<'_>,
) {
    assert_eq!(filter.channels(), input.channels());
    assert_eq!(output.rows(), input.rows());
    assert_eq!(output.cols(), input.cols());
    assert_eq!(output.channels(), filter.out_channels());

    for row in 0..input.rows() {
        for col in 0..input.cols() {
            let in_start = input.pixel_offset(row, col);
            let out_start = output.pixel_offset(row, col);
            let out_end = out_start + filter.padded_out_channels();
            let output_pixel = &mut output.data_mut()[out_start..out_end];
            output_pixel.copy_from_slice(filter.biases());

            for ic in 0..filter.channels() {
                let input_value = input.data()[in_start + ic];
                let weight_start = ic * filter.padded_out_channels();
                let weights =
                    &filter.weights()[weight_start..weight_start + filter.padded_out_channels()];
                for oc in 0..filter.padded_out_channels() {
                    output_pixel[oc] += input_value * weights[oc];
                }
            }
        }
    }
}

pub fn depthwise_3x3_to(
    input: BlobView<'_>,
    weights: BlobView<'_>,
    biases: &[f32],
    output: BlobViewMut<'_>,
) {
    depthwise_3x3_impl(input, weights, biases, output, false);
}

pub fn depthwise_3x3_relu_to(
    input: BlobView<'_>,
    weights: BlobView<'_>,
    biases: &[f32],
    output: BlobViewMut<'_>,
) {
    depthwise_3x3_impl(input, weights, biases, output, true);
}

fn depthwise_3x3_impl(
    input: BlobView<'_>,
    weights: BlobView<'_>,
    biases: &[f32],
    mut output: BlobViewMut<'_>,
    do_relu: bool,
) {
    assert_eq!(weights.rows(), 1);
    assert_eq!(weights.cols(), 9);
    assert_eq!(weights.channels(), input.channels());
    assert_eq!(biases.len(), input.channels());
    assert_eq!(output.rows(), input.rows());
    assert_eq!(output.cols(), input.cols());
    assert_eq!(output.channels(), input.channels());

    for row in 0..output.rows() {
        let src_row_start = row.saturating_sub(1);
        let src_row_end = (row + 2).min(input.rows());
        for col in 0..output.cols() {
            let src_col_start = col.saturating_sub(1);
            let src_col_end = (col + 2).min(input.cols());
            let out_start = output.pixel_offset(row, col);

            for (ch, bias) in biases.iter().copied().enumerate() {
                output.data_mut()[out_start + ch] = bias;
            }

            for src_row in src_row_start..src_row_end {
                for src_col in src_col_start..src_col_end {
                    let filter_row = src_row + 1 - row;
                    let filter_col = src_col + 1 - col;
                    let filter_idx = filter_row * 3 + filter_col;
                    let in_start = input.pixel_offset(src_row, src_col);
                    let weight_start = weights.pixel_offset(0, filter_idx);

                    for ch in 0..input.channels() {
                        output.data_mut()[out_start + ch] +=
                            input.data()[in_start + ch] * weights.data()[weight_start + ch];
                    }
                }
            }
            if do_relu {
                for ch in 0..input.channels() {
                    output.data_mut()[out_start + ch] = output.data()[out_start + ch].max(0.0);
                }
            }
            clear_pixel_padding(&mut output, out_start);
        }
    }
}

pub fn max_pool_2x2_s2_to(input: BlobView<'_>, mut output: BlobViewMut<'_>) {
    assert_eq!(output.rows(), max_pool_output_size(input.rows()));
    assert_eq!(output.cols(), max_pool_output_size(input.cols()));
    assert_eq!(output.channels(), input.channels());

    for row in 0..output.rows() {
        for col in 0..output.cols() {
            let src_row_start = row * 2;
            let src_col_start = col * 2;
            let src_row_end = (src_row_start + 2).min(input.rows());
            let src_col_end = (src_col_start + 2).min(input.cols());
            let out_start = output.pixel_offset(row, col);

            for ch in 0..input.channels() {
                let mut max_value =
                    input.data()[input.pixel_offset(src_row_start, src_col_start) + ch];
                for src_row in src_row_start..src_row_end {
                    for src_col in src_col_start..src_col_end {
                        let value = input.data()[input.pixel_offset(src_row, src_col) + ch];
                        max_value = max_value.max(value);
                    }
                }
                output.data_mut()[out_start + ch] = max_value;
            }
            clear_pixel_padding(&mut output, out_start);
        }
    }
}

fn clear_pixel_padding(output: &mut BlobViewMut<'_>, pixel_start: usize) {
    for ch in output.channels()..output.channel_step() {
        output.data_mut()[pixel_start + ch] = 0.0;
    }
}

fn assert_same_shape(lhs: BlobView<'_>, rhs: BlobView<'_>) {
    assert_eq!(lhs.rows(), rhs.rows());
    assert_eq!(lhs.cols(), rhs.cols());
    assert_eq!(lhs.channels(), rhs.channels());
}

fn assert_same_mut_shape(lhs: BlobView<'_>, rhs: &BlobViewMut<'_>) {
    assert_eq!(lhs.rows(), rhs.rows());
    assert_eq!(lhs.cols(), rhs.cols());
    assert_eq!(lhs.channels(), rhs.channels());
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::blob::Blob;

    fn fill_valid(blob: &mut Blob) {
        for row in 0..blob.rows() {
            for col in 0..blob.cols() {
                for ch in 0..blob.channels() {
                    blob.pixel_mut(row, col)[ch] =
                        ((row * 17 + col * 7 + ch * 3) as f32) * 0.25 - 3.0;
                }
            }
        }
    }

    #[test]
    fn pointwise_matches_reference() {
        let mut input = Blob::with_shape(2, 3, 5);
        let mut weights = Blob::with_shape(1, 4, 5);
        let mut output = Blob::with_shape(2, 3, 4);
        let biases = [0.5, -1.0, 2.0, 0.25];
        fill_valid(&mut input);
        fill_valid(&mut weights);

        pointwise_1x1_to(input.view(), weights.view(), &biases, output.view_mut());

        for row in 0..2 {
            for col in 0..3 {
                for (oc, bias) in biases.iter().copied().enumerate() {
                    let mut expected = bias;
                    for ic in 0..5 {
                        expected += input.pixel(row, col)[ic] * weights.pixel(0, oc)[ic];
                    }
                    assert!((output.pixel(row, col)[oc] - expected).abs() < 1e-5);
                }
                assert_eq!(output.pixel(row, col)[4], 0.0);
            }
        }
    }

    #[test]
    fn packed_pointwise_matches_primitive() {
        let mut input = Blob::with_shape(3, 2, 16);
        let mut weights = Blob::with_shape(1, 16, 16);
        let mut primitive = Blob::with_shape(3, 2, 16);
        let mut packed = Blob::with_shape(3, 2, 16);
        let biases: Vec<f32> = (0..16).map(|idx| idx as f32 * 0.125).collect();
        fill_valid(&mut input);
        fill_valid(&mut weights);

        let packed_filter = PackedPointwiseFilter::pack(weights.view(), &biases, 8);
        pointwise_1x1_to(input.view(), weights.view(), &biases, primitive.view_mut());
        pointwise_1x1_packed_to(input.view(), &packed_filter, packed.view_mut());

        for (expected, actual) in primitive.data().iter().zip(packed.data()) {
            assert!((expected - actual).abs() < 1e-5);
        }
    }

    #[test]
    fn depthwise_handles_borders_like_zero_padding() {
        let mut input = Blob::with_shape(2, 2, 2);
        let mut weights = Blob::with_shape(1, 9, 2);
        let mut output = Blob::with_shape(2, 2, 2);
        let biases = [1.0, -2.0];
        fill_valid(&mut input);
        for idx in 0..9 {
            weights.pixel_mut(0, idx)[0] = 1.0;
            weights.pixel_mut(0, idx)[1] = 2.0;
        }

        depthwise_3x3_to(input.view(), weights.view(), &biases, output.view_mut());

        for row in 0_usize..2 {
            for col in 0_usize..2 {
                let mut expected_ch0 = biases[0];
                let mut expected_ch1 = biases[1];
                for src_row in row.saturating_sub(1)..=(row + 1).min(1) {
                    for src_col in col.saturating_sub(1)..=(col + 1).min(1) {
                        expected_ch0 += input.pixel(src_row, src_col)[0];
                        expected_ch1 += input.pixel(src_row, src_col)[1] * 2.0;
                    }
                }
                assert!((output.pixel(row, col)[0] - expected_ch0).abs() < 1e-5);
                assert!((output.pixel(row, col)[1] - expected_ch1).abs() < 1e-5);
            }
        }
    }

    #[test]
    fn maxpool_matches_reference_for_odd_shape() {
        let mut input = Blob::with_shape(5, 5, 3);
        let mut output = Blob::with_shape(
            max_pool_output_size(input.rows()),
            max_pool_output_size(input.cols()),
            input.channels(),
        );
        fill_valid(&mut input);

        max_pool_2x2_s2_to(input.view(), output.view_mut());

        for row in 0..output.rows() {
            for col in 0..output.cols() {
                for ch in 0..input.channels() {
                    let mut expected = input.pixel(row * 2, col * 2)[ch];
                    for src_row in row * 2..(row * 2 + 2).min(input.rows()) {
                        for src_col in col * 2..(col * 2 + 2).min(input.cols()) {
                            expected = expected.max(input.pixel(src_row, src_col)[ch]);
                        }
                    }
                    assert_eq!(output.pixel(row, col)[ch], expected);
                }
            }
        }
    }

    #[test]
    fn add_and_relu_work_on_flat_hot_path_data() {
        let mut lhs = Blob::with_shape(1, 2, 3);
        let mut rhs = Blob::with_shape(1, 2, 3);
        let mut output = Blob::with_shape(1, 2, 3);
        fill_valid(&mut lhs);
        fill_valid(&mut rhs);

        add_to(lhs.view(), rhs.view(), output.view_mut());
        relu_in_place(output.data_mut());

        for value in output.data() {
            assert!(*value >= 0.0);
        }
    }
}
