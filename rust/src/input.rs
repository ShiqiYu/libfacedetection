use crate::blob::Blob;
use crate::filter::Filter;

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum PixelFormat {
    Rgb,
    Bgr,
}

pub fn image_to_initial_blob(
    pixels: &[u8],
    width: usize,
    height: usize,
    step: usize,
    pixel_format: PixelFormat,
) -> Blob {
    let mut output = Blob::new();
    image_to_initial_blob_to(pixels, width, height, step, pixel_format, &mut output);
    output
}

pub fn image_to_initial_blob_to(
    pixels: &[u8],
    width: usize,
    height: usize,
    step: usize,
    pixel_format: PixelFormat,
    output: &mut Blob,
) {
    let _pixel_format = pixel_format;
    let rows = height.div_ceil(32) * 16;
    let cols = width.div_ceil(32) * 16;
    output.resize_for_overwrite(rows, cols, 32);

    for row in 0..rows {
        for col in 0..cols {
            let center_y = row * 2;
            let center_x = col * 2;
            let out = output.pixel_mut(row, col);

            if center_y > 0 && center_y + 1 < height && center_x > 0 && center_x + 1 < width {
                let row0 = (center_y - 1) * step + (center_x - 1) * 3;
                let row1 = center_y * step + (center_x - 1) * 3;
                let row2 = (center_y + 1) * step + (center_x - 1) * 3;
                copy_3x3_row(&pixels[row0..], &mut out[0..9]);
                copy_3x3_row(&pixels[row1..], &mut out[9..18]);
                copy_3x3_row(&pixels[row2..], &mut out[18..27]);
                out[27..32].fill(0.0);
                continue;
            }

            out[..32].fill(0.0);
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
                    let out_ch = ((fy + 1) * 3 + fx + 1) as usize * 3;
                    out[out_ch] = pixels[src] as f32;
                    out[out_ch + 1] = pixels[src + 1] as f32;
                    out[out_ch + 2] = pixels[src + 2] as f32;
                }
            }
        }
    }
}

pub fn image_to_initial_conv_to(
    pixels: &[u8],
    width: usize,
    height: usize,
    step: usize,
    pixel_format: PixelFormat,
    filter: &Filter,
    output: &mut Blob,
) {
    let _pixel_format = pixel_format;
    assert!(filter.is_pointwise());
    assert_eq!(filter.channels(), 32);

    let packed = filter
        .pointwise_plan()
        .and_then(|plan| plan.packed())
        .expect("initial convolution must use a packed pointwise filter");
    let rows = height.div_ceil(32) * 16;
    let cols = width.div_ceil(32) * 16;
    output.resize_for_overwrite(rows, cols, filter.num_filters());

    let out_channels = filter.num_filters();
    let output_channel_step = output.channel_step();
    let padded_out_channels = packed.padded_out_channels();

    if crate::kernels::image_to_initial_conv_3x3_s2_to(
        pixels,
        width,
        height,
        step,
        packed,
        output.view_mut(),
    ) {
        return;
    }

    for row in 0..rows {
        for col in 0..cols {
            let center_y = row * 2;
            let center_x = col * 2;
            let out = output.pixel_mut(row, col);
            out[..padded_out_channels].copy_from_slice(packed.biases());

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
                        let input_value = pixels[src + channel_offset] as f32;
                        let input_channel = base_input_channel + channel_offset;
                        let weight_start = input_channel * padded_out_channels;
                        let weights =
                            &packed.weights()[weight_start..weight_start + padded_out_channels];
                        for oc in 0..padded_out_channels {
                            out[oc] += input_value * weights[oc];
                        }
                    }
                }
            }

            for value in &mut out[..out_channels] {
                *value = value.max(0.0);
            }
            out[out_channels..output_channel_step].fill(0.0);
        }
    }
}

fn copy_3x3_row(input: &[u8], output: &mut [f32]) {
    debug_assert!(input.len() >= 9);
    debug_assert_eq!(output.len(), 9);
    for idx in 0..9 {
        output[idx] = input[idx] as f32;
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::layers::convolution_to;
    use crate::model::load_static_model;

    #[test]
    fn image_transform_shape_pads_to_divisor_32_then_stride_2() {
        let width = 33;
        let height = 65;
        let step = width * 3;
        let pixels = vec![0_u8; step * height];

        let output = image_to_initial_blob(&pixels, width, height, step, PixelFormat::Rgb);

        assert_eq!(output.rows(), 48);
        assert_eq!(output.cols(), 32);
        assert_eq!(output.channels(), 32);
    }

    #[test]
    fn image_transform_preserves_source_channel_order() {
        let width = 5;
        let height = 5;
        let step = width * 3;
        let pixels: Vec<u8> = (0..step * height).map(|idx| idx as u8).collect();

        let output = image_to_initial_blob(&pixels, width, height, step, PixelFormat::Rgb);
        let mut expected = Vec::new();
        for src_y in 1..=3 {
            let start = src_y * step + 3;
            expected.extend(pixels[start..start + 9].iter().copied().map(f32::from));
        }

        assert_eq!(&output.pixel(1, 1)[0..27], expected.as_slice());
    }

    #[test]
    fn image_transform_zeroes_out_of_bounds_and_padding_channels() {
        let width = 1;
        let height = 1;
        let pixels = [7_u8, 8, 9];

        let output = image_to_initial_blob(&pixels, width, height, 3, PixelFormat::Bgr);

        assert_eq!(output.pixel(0, 0)[12], 7.0);
        assert_eq!(output.pixel(0, 0)[13], 8.0);
        assert_eq!(output.pixel(0, 0)[14], 9.0);
        assert_eq!(output.pixel(0, 0)[0], 0.0);
        assert_eq!(output.pixel(0, 0)[31], 0.0);
    }

    #[test]
    fn fused_initial_conv_matches_separate_transform_and_conv0() {
        let width = 67;
        let height = 49;
        let step = width * 3;
        let pixels: Vec<u8> = (0..step * height).map(|idx| (idx % 251) as u8).collect();
        let model = load_static_model();
        let mut initial = Blob::new();
        let mut separate = Blob::new();
        let mut fused = Blob::new();

        image_to_initial_blob_to(&pixels, width, height, step, PixelFormat::Rgb, &mut initial);
        convolution_to(initial.view(), model.filter(0), true, &mut separate);
        image_to_initial_conv_to(
            &pixels,
            width,
            height,
            step,
            PixelFormat::Rgb,
            model.filter(0),
            &mut fused,
        );

        assert_eq!(fused.rows(), separate.rows());
        assert_eq!(fused.cols(), separate.cols());
        assert_eq!(fused.channels(), separate.channels());
        for (actual, expected) in fused.data().iter().zip(separate.data()) {
            assert!((actual - expected).abs() < 1e-3);
        }
    }
}
