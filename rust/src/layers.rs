use crate::blob::{Blob, BlobView};
use crate::filter::{Filter, PointwiseStrategy};
use crate::kernels::{self, scalar};

pub fn convolution_to(input: BlobView<'_>, filter: &Filter, do_relu: bool, output: &mut Blob) {
    output.resize_for_overwrite(input.rows(), input.cols(), filter.num_filters());
    let output_view = output.view_mut();

    if filter.is_pointwise() {
        match filter
            .pointwise_plan()
            .expect("pointwise filters must have a plan")
            .strategy()
        {
            PointwiseStrategy::Packed => {
                let packed = filter
                    .pointwise_plan()
                    .and_then(|plan| plan.packed())
                    .expect("packed strategy must have packed filter");
                kernels::pointwise_1x1_packed_to(input, packed, output_view);
            }
            PointwiseStrategy::Primitive => {
                scalar::pointwise_1x1_to(input, filter.weights(), filter.biases(), output_view);
            }
        }
    } else if filter.is_depthwise() {
        if do_relu {
            kernels::depthwise_3x3_relu_to(input, filter.weights(), filter.biases(), output_view);
            return;
        }
        kernels::depthwise_3x3_to(input, filter.weights(), filter.biases(), output_view);
    } else {
        unreachable!("unsupported filter kind");
    }

    if do_relu {
        scalar::relu_in_place(output.data_mut());
    }
}

pub fn convolution(input: BlobView<'_>, filter: &Filter, do_relu: bool) -> Blob {
    let mut output = Blob::new();
    convolution_to(input, filter, do_relu, &mut output);
    output
}

pub fn depthwise_pointwise_to(
    input: BlobView<'_>,
    pointwise: &Filter,
    depthwise: &Filter,
    do_relu: bool,
    pointwise_workspace: &mut Blob,
    output: &mut Blob,
) {
    convolution_to(input, pointwise, false, pointwise_workspace);
    convolution_to(pointwise_workspace.view(), depthwise, do_relu, output);
}

pub fn max_pool_2x2_s2_to(input: BlobView<'_>, output: &mut Blob) {
    output.resize_for_overwrite(
        scalar::max_pool_output_size(input.rows()),
        scalar::max_pool_output_size(input.cols()),
        input.channels(),
    );
    kernels::max_pool_2x2_s2_to(input, output.view_mut());
}

pub fn upsample_x2_add_to(input: BlobView<'_>, lateral: BlobView<'_>, output: &mut Blob) {
    assert_eq!(lateral.rows(), input.rows() * 2);
    assert_eq!(lateral.cols(), input.cols() * 2);
    assert_eq!(lateral.channels(), input.channels());

    output.resize_for_overwrite(lateral.rows(), lateral.cols(), lateral.channels());
    let channels = input.channels();
    let mut output_view = output.view_mut();

    for row in 0..input.rows() {
        for col in 0..input.cols() {
            let in_start = input.pixel_offset(row, col);
            let out_row = row * 2;
            let out_col = col * 2;
            add_upsampled_pixel(
                input,
                lateral,
                &mut output_view,
                in_start,
                out_row,
                out_col,
                channels,
            );
            add_upsampled_pixel(
                input,
                lateral,
                &mut output_view,
                in_start,
                out_row,
                out_col + 1,
                channels,
            );
            add_upsampled_pixel(
                input,
                lateral,
                &mut output_view,
                in_start,
                out_row + 1,
                out_col,
                channels,
            );
            add_upsampled_pixel(
                input,
                lateral,
                &mut output_view,
                in_start,
                out_row + 1,
                out_col + 1,
                channels,
            );
        }
    }
}

fn add_upsampled_pixel(
    input: BlobView<'_>,
    lateral: BlobView<'_>,
    output: &mut crate::blob::BlobViewMut<'_>,
    in_start: usize,
    out_row: usize,
    out_col: usize,
    channels: usize,
) {
    let lateral_start = lateral.pixel_offset(out_row, out_col);
    let output_start = output.pixel_offset(out_row, out_col);
    for ch in 0..channels {
        output.data_mut()[output_start + ch] =
            input.data()[in_start + ch] + lateral.data()[lateral_start + ch];
    }
    for ch in channels..output.channel_step() {
        output.data_mut()[output_start + ch] = 0.0;
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::filter::{ConvInfo, FilterKind};

    fn values(len: usize) -> Vec<f32> {
        (0..len).map(|idx| idx as f32 * 0.125 - 2.0).collect()
    }

    fn fill_valid(blob: &mut Blob) {
        for row in 0..blob.rows() {
            for col in 0..blob.cols() {
                for ch in 0..blob.channels() {
                    blob.pixel_mut(row, col)[ch] =
                        ((row * 11 + col * 5 + ch * 3) as f32) * 0.125 - 1.0;
                }
            }
        }
    }

    #[test]
    fn convolution_to_uses_model_owned_pointwise_plan() {
        let mut input = Blob::with_shape(2, 2, 16);
        let weights = values(16 * 16);
        let biases = values(16);
        let filter = Filter::load(ConvInfo {
            channels: 16,
            num_filters: 16,
            kind: FilterKind::Pointwise,
            with_relu: true,
            weights: &weights,
            biases: &biases,
        });
        let mut output = Blob::new();
        fill_valid(&mut input);

        convolution_to(input.view(), &filter, filter.with_relu(), &mut output);

        assert_eq!(output.rows(), input.rows());
        assert_eq!(output.cols(), input.cols());
        assert_eq!(output.channels(), filter.num_filters());
        assert!(output.data().iter().all(|value| *value >= 0.0));
    }

    #[test]
    fn depthwise_pointwise_reuses_workspace() {
        let mut input = Blob::with_shape(2, 2, 16);
        let pw_weights = values(16 * 16);
        let pw_biases = values(16);
        let dw_weights = values(16 * 9);
        let dw_biases = values(16);
        let pointwise = Filter::load(ConvInfo {
            channels: 16,
            num_filters: 16,
            kind: FilterKind::Pointwise,
            with_relu: false,
            weights: &pw_weights,
            biases: &pw_biases,
        });
        let depthwise = Filter::load(ConvInfo {
            channels: 16,
            num_filters: 16,
            kind: FilterKind::Depthwise,
            with_relu: true,
            weights: &dw_weights,
            biases: &dw_biases,
        });
        let mut workspace = Blob::new();
        let mut output = Blob::new();
        fill_valid(&mut input);

        depthwise_pointwise_to(
            input.view(),
            &pointwise,
            &depthwise,
            true,
            &mut workspace,
            &mut output,
        );

        assert_eq!(workspace.channels(), 16);
        assert_eq!(output.channels(), 16);
        assert!(output.data().iter().all(|value| *value >= 0.0));
    }

    #[test]
    fn upsample_x2_add_fuses_upsample_and_lateral_add() {
        let mut input = Blob::with_shape(1, 1, 2);
        let mut lateral = Blob::with_shape(2, 2, 2);
        let mut output = Blob::new();
        input.pixel_mut(0, 0)[0] = 10.0;
        input.pixel_mut(0, 0)[1] = 20.0;
        fill_valid(&mut lateral);

        upsample_x2_add_to(input.view(), lateral.view(), &mut output);

        for row in 0..2 {
            for col in 0..2 {
                assert_eq!(output.pixel(row, col)[0], 10.0 + lateral.pixel(row, col)[0]);
                assert_eq!(output.pixel(row, col)[1], 20.0 + lateral.pixel(row, col)[1]);
            }
        }
    }
}
