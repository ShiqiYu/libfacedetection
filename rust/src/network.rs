use crate::blob::Blob;
use crate::input::{PixelFormat, image_to_initial_conv_to};
use crate::layers::{
    convolution_to, depthwise_pointwise_to, max_pool_2x2_s2_to, upsample_x2_add_to,
};
use crate::model::Model;

#[derive(Debug, Default)]
pub struct BackboneOutputs {
    pub fb1: Blob,
    pub fb2: Blob,
    pub fb3: Blob,
}

#[derive(Debug, Default)]
pub struct HeadOutputs {
    pub cls: [Blob; 3],
    pub reg: [Blob; 3],
    pub kps: [Blob; 3],
    pub obj: [Blob; 3],
}

#[derive(Debug, Default)]
pub struct NetworkWorkspace {
    backbone_fx: Blob,
    backbone_pointwise: Blob,
    backbone_block: Blob,
    backbone_output: Blob,
    head_pointwise: Blob,
    head_branch: Blob,
}

#[derive(Debug, Clone, Copy)]
pub struct PixelInput<'a> {
    pub pixels: &'a [u8],
    pub width: usize,
    pub height: usize,
    pub step: usize,
    pub pixel_format: PixelFormat,
}

pub fn forward_backbone(input: &Blob, model: &Model) -> BackboneOutputs {
    let mut workspace = NetworkWorkspace::default();
    let mut outputs = BackboneOutputs::default();
    forward_backbone_to(input, model, &mut workspace, &mut outputs);
    outputs
}

pub fn forward_backbone_to(
    input: &Blob,
    model: &Model,
    workspace: &mut NetworkWorkspace,
    outputs: &mut BackboneOutputs,
) {
    convolution_to(
        input.view(),
        model.filter(0),
        true,
        &mut workspace.backbone_fx,
    );
    forward_backbone_after_conv0_to(model, workspace, outputs);
}

pub fn forward_network_from_pixels_to(
    input: PixelInput<'_>,
    model: &Model,
    workspace: &mut NetworkWorkspace,
    backbone: &mut BackboneOutputs,
    heads: &mut HeadOutputs,
) {
    image_to_initial_conv_to(
        input.pixels,
        input.width,
        input.height,
        input.step,
        input.pixel_format,
        model.filter(0),
        &mut workspace.backbone_fx,
    );
    forward_backbone_after_conv0_to(model, workspace, backbone);
    forward_heads_to(backbone, model, workspace, heads);
}

fn forward_backbone_after_conv0_to(
    model: &Model,
    workspace: &mut NetworkWorkspace,
    outputs: &mut BackboneOutputs,
) {
    depthwise_pointwise_to(
        workspace.backbone_fx.view(),
        model.filter(1),
        model.filter(2),
        true,
        &mut workspace.backbone_pointwise,
        &mut workspace.backbone_block,
    );
    core::mem::swap(&mut workspace.backbone_fx, &mut workspace.backbone_block);
    max_pool_2x2_s2_to(workspace.backbone_fx.view(), &mut workspace.backbone_block);
    core::mem::swap(&mut workspace.backbone_fx, &mut workspace.backbone_block);

    convolution_4layer_unit_to(
        &workspace.backbone_fx,
        model,
        [3, 4, 5, 6],
        true,
        &mut workspace.backbone_pointwise,
        &mut workspace.backbone_block,
        &mut workspace.backbone_output,
    );
    core::mem::swap(&mut workspace.backbone_fx, &mut workspace.backbone_output);

    convolution_4layer_unit_to(
        &workspace.backbone_fx,
        model,
        [7, 8, 9, 10],
        true,
        &mut workspace.backbone_pointwise,
        &mut workspace.backbone_block,
        &mut workspace.backbone_output,
    );
    core::mem::swap(&mut workspace.backbone_fx, &mut workspace.backbone_output);

    max_pool_2x2_s2_to(workspace.backbone_fx.view(), &mut workspace.backbone_block);
    core::mem::swap(&mut workspace.backbone_fx, &mut workspace.backbone_block);

    convolution_4layer_unit_to(
        &workspace.backbone_fx,
        model,
        [11, 12, 13, 14],
        true,
        &mut workspace.backbone_pointwise,
        &mut workspace.backbone_block,
        &mut outputs.fb1,
    );

    max_pool_2x2_s2_to(outputs.fb1.view(), &mut workspace.backbone_fx);
    convolution_4layer_unit_to(
        &workspace.backbone_fx,
        model,
        [15, 16, 17, 18],
        true,
        &mut workspace.backbone_pointwise,
        &mut workspace.backbone_block,
        &mut outputs.fb2,
    );

    max_pool_2x2_s2_to(outputs.fb2.view(), &mut workspace.backbone_fx);
    convolution_4layer_unit_to(
        &workspace.backbone_fx,
        model,
        [19, 20, 21, 22],
        true,
        &mut workspace.backbone_pointwise,
        &mut workspace.backbone_block,
        &mut outputs.fb3,
    );
}

pub fn forward_heads_to(
    backbone: &mut BackboneOutputs,
    model: &Model,
    workspace: &mut NetworkWorkspace,
    outputs: &mut HeadOutputs,
) {
    depthwise_pointwise_to(
        backbone.fb3.view(),
        model.filter(27),
        model.filter(28),
        true,
        &mut workspace.head_pointwise,
        &mut workspace.head_branch,
    );
    core::mem::swap(&mut backbone.fb3, &mut workspace.head_branch);
    depthwise_pointwise_to(
        backbone.fb3.view(),
        model.filter(33),
        model.filter(34),
        false,
        &mut workspace.head_pointwise,
        &mut outputs.cls[2],
    );
    depthwise_pointwise_to(
        backbone.fb3.view(),
        model.filter(39),
        model.filter(40),
        false,
        &mut workspace.head_pointwise,
        &mut outputs.reg[2],
    );
    depthwise_pointwise_to(
        backbone.fb3.view(),
        model.filter(51),
        model.filter(52),
        false,
        &mut workspace.head_pointwise,
        &mut outputs.kps[2],
    );
    depthwise_pointwise_to(
        backbone.fb3.view(),
        model.filter(45),
        model.filter(46),
        false,
        &mut workspace.head_pointwise,
        &mut outputs.obj[2],
    );

    upsample_x2_add_to(
        backbone.fb3.view(),
        backbone.fb2.view(),
        &mut workspace.head_branch,
    );
    core::mem::swap(&mut backbone.fb2, &mut workspace.head_branch);
    depthwise_pointwise_to(
        backbone.fb2.view(),
        model.filter(25),
        model.filter(26),
        true,
        &mut workspace.head_pointwise,
        &mut workspace.head_branch,
    );
    core::mem::swap(&mut backbone.fb2, &mut workspace.head_branch);
    depthwise_pointwise_to(
        backbone.fb2.view(),
        model.filter(31),
        model.filter(32),
        false,
        &mut workspace.head_pointwise,
        &mut outputs.cls[1],
    );
    depthwise_pointwise_to(
        backbone.fb2.view(),
        model.filter(37),
        model.filter(38),
        false,
        &mut workspace.head_pointwise,
        &mut outputs.reg[1],
    );
    depthwise_pointwise_to(
        backbone.fb2.view(),
        model.filter(49),
        model.filter(50),
        false,
        &mut workspace.head_pointwise,
        &mut outputs.kps[1],
    );
    depthwise_pointwise_to(
        backbone.fb2.view(),
        model.filter(43),
        model.filter(44),
        false,
        &mut workspace.head_pointwise,
        &mut outputs.obj[1],
    );

    upsample_x2_add_to(
        backbone.fb2.view(),
        backbone.fb1.view(),
        &mut workspace.head_branch,
    );
    core::mem::swap(&mut backbone.fb1, &mut workspace.head_branch);
    depthwise_pointwise_to(
        backbone.fb1.view(),
        model.filter(23),
        model.filter(24),
        true,
        &mut workspace.head_pointwise,
        &mut workspace.head_branch,
    );
    core::mem::swap(&mut backbone.fb1, &mut workspace.head_branch);
    depthwise_pointwise_to(
        backbone.fb1.view(),
        model.filter(29),
        model.filter(30),
        false,
        &mut workspace.head_pointwise,
        &mut outputs.cls[0],
    );
    depthwise_pointwise_to(
        backbone.fb1.view(),
        model.filter(35),
        model.filter(36),
        false,
        &mut workspace.head_pointwise,
        &mut outputs.reg[0],
    );
    depthwise_pointwise_to(
        backbone.fb1.view(),
        model.filter(47),
        model.filter(48),
        false,
        &mut workspace.head_pointwise,
        &mut outputs.kps[0],
    );
    depthwise_pointwise_to(
        backbone.fb1.view(),
        model.filter(41),
        model.filter(42),
        false,
        &mut workspace.head_pointwise,
        &mut outputs.obj[0],
    );
}

pub fn forward_network_to(
    input: &Blob,
    model: &Model,
    workspace: &mut NetworkWorkspace,
    backbone: &mut BackboneOutputs,
    heads: &mut HeadOutputs,
) {
    forward_backbone_to(input, model, workspace, backbone);
    forward_heads_to(backbone, model, workspace, heads);
}

fn convolution_4layer_unit_to(
    input: &Blob,
    model: &Model,
    filters: [usize; 4],
    do_relu: bool,
    pointwise_workspace: &mut Blob,
    block_workspace: &mut Blob,
    output: &mut Blob,
) {
    depthwise_pointwise_to(
        input.view(),
        model.filter(filters[0]),
        model.filter(filters[1]),
        true,
        pointwise_workspace,
        block_workspace,
    );
    depthwise_pointwise_to(
        block_workspace.view(),
        model.filter(filters[2]),
        model.filter(filters[3]),
        do_relu,
        pointwise_workspace,
        output,
    );
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::input::{PixelFormat, image_to_initial_blob};
    use crate::model::load_static_model;

    #[test]
    fn scalar_backbone_runs_real_static_model() {
        let width = 96;
        let height = 96;
        let step = width * 3;
        let pixels: Vec<u8> = (0..step * height).map(|idx| (idx % 251) as u8).collect();
        let input = image_to_initial_blob(&pixels, width, height, step, PixelFormat::Rgb);
        let model = load_static_model();

        let outputs = forward_backbone(&input, &model);

        assert_eq!(
            (
                outputs.fb1.rows(),
                outputs.fb1.cols(),
                outputs.fb1.channels()
            ),
            (12, 12, 64)
        );
        assert_eq!(
            (
                outputs.fb2.rows(),
                outputs.fb2.cols(),
                outputs.fb2.channels()
            ),
            (6, 6, 64)
        );
        assert_eq!(
            (
                outputs.fb3.rows(),
                outputs.fb3.cols(),
                outputs.fb3.channels()
            ),
            (3, 3, 64)
        );
        assert!(outputs.fb1.data().iter().all(|value| value.is_finite()));
        assert!(outputs.fb2.data().iter().all(|value| value.is_finite()));
        assert!(outputs.fb3.data().iter().all(|value| value.is_finite()));
    }

    #[test]
    fn backbone_workspace_can_be_reused() {
        let width = 64;
        let height = 64;
        let step = width * 3;
        let pixels = vec![3_u8; step * height];
        let input = image_to_initial_blob(&pixels, width, height, step, PixelFormat::Bgr);
        let model = load_static_model();
        let mut workspace = NetworkWorkspace::default();
        let mut outputs = BackboneOutputs::default();

        forward_backbone_to(&input, &model, &mut workspace, &mut outputs);
        let first_capacity = workspace.backbone_fx.data().len();
        forward_backbone_to(&input, &model, &mut workspace, &mut outputs);

        assert_eq!(workspace.backbone_fx.data().len(), first_capacity);
    }

    #[test]
    fn scalar_raw_network_runs_real_static_model() {
        let width = 96;
        let height = 96;
        let step = width * 3;
        let pixels: Vec<u8> = (0..step * height).map(|idx| (idx % 251) as u8).collect();
        let input = image_to_initial_blob(&pixels, width, height, step, PixelFormat::Rgb);
        let model = load_static_model();
        let mut workspace = NetworkWorkspace::default();
        let mut backbone = BackboneOutputs::default();
        let mut heads = HeadOutputs::default();

        forward_network_to(&input, &model, &mut workspace, &mut backbone, &mut heads);

        assert_eq!(
            (
                heads.cls[0].rows(),
                heads.cls[0].cols(),
                heads.cls[0].channels()
            ),
            (12, 12, 1)
        );
        assert_eq!(
            (
                heads.reg[0].rows(),
                heads.reg[0].cols(),
                heads.reg[0].channels()
            ),
            (12, 12, 4)
        );
        assert_eq!(
            (
                heads.kps[0].rows(),
                heads.kps[0].cols(),
                heads.kps[0].channels()
            ),
            (12, 12, 10)
        );
        assert_eq!(
            (
                heads.obj[0].rows(),
                heads.obj[0].cols(),
                heads.obj[0].channels()
            ),
            (12, 12, 1)
        );
        assert_eq!(
            (
                heads.cls[1].rows(),
                heads.cls[1].cols(),
                heads.cls[1].channels()
            ),
            (6, 6, 1)
        );
        assert_eq!(
            (
                heads.cls[2].rows(),
                heads.cls[2].cols(),
                heads.cls[2].channels()
            ),
            (3, 3, 1)
        );
        for level in 0..3 {
            assert!(
                heads.cls[level]
                    .data()
                    .iter()
                    .all(|value| value.is_finite())
            );
            assert!(
                heads.reg[level]
                    .data()
                    .iter()
                    .all(|value| value.is_finite())
            );
            assert!(
                heads.kps[level]
                    .data()
                    .iter()
                    .all(|value| value.is_finite())
            );
            assert!(
                heads.obj[level]
                    .data()
                    .iter()
                    .all(|value| value.is_finite())
            );
        }
    }
}
