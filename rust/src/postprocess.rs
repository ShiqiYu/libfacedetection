use crate::blob::Blob;
use crate::network::HeadOutputs;
use crate::result::Face;

const SIGMOID_CLAMP: f32 = 88.376_26;
pub const DEFAULT_DETECTION_OUTPUT_PARAMS: DetectionOutputParams = DetectionOutputParams {
    overlap_threshold: 0.45,
    confidence_threshold: 0.2,
    top_k: 1000,
    keep_top_k: 512,
};

#[derive(Debug, Clone, Copy)]
pub struct DetectionOutputParams {
    pub overlap_threshold: f32,
    pub confidence_threshold: f32,
    pub top_k: isize,
    pub keep_top_k: isize,
}

#[derive(Debug, Default)]
pub struct DetectionOutputWorkspace {
    candidates: Vec<Face>,
    selected: Vec<Face>,
}

#[derive(Debug, Default)]
pub struct DecodedOutputs {
    pub cls: Blob,
    pub reg: Blob,
    pub kps: Blob,
    pub obj: Blob,
    prior_scratch: [Blob; 3],
    reg_scratch: [Blob; 3],
    kps_scratch: [Blob; 3],
}

impl DetectionOutputWorkspace {
    pub(crate) fn faces(&self) -> &[Face] {
        &self.selected
    }
}

pub fn meshgrid(feature_width: usize, feature_height: usize, stride: usize) -> Blob {
    let mut output = Blob::new();
    meshgrid_to(feature_width, feature_height, stride, &mut output);
    output
}

pub fn meshgrid_to(feature_width: usize, feature_height: usize, stride: usize, output: &mut Blob) {
    output.resize_for_overwrite(feature_height, feature_width, 2);
    let output_channel_step = output.channel_step();
    let stride = stride as f32;
    for row in 0..feature_height {
        let y = row as f32 * stride;
        for col in 0..feature_width {
            let out = output.pixel_mut(row, col);
            out[0] = col as f32 * stride;
            out[1] = y;
            out[2..output_channel_step].fill(0.0);
        }
    }
}

pub fn bbox_decode_to(bbox_pred: &Blob, priors: &Blob, stride: usize, output: &mut Blob) {
    assert_eq!(bbox_pred.rows(), priors.rows());
    assert_eq!(bbox_pred.cols(), priors.cols());
    assert_eq!(bbox_pred.channels(), 4);
    assert_eq!(priors.channels(), 2);

    output.resize_for_overwrite(bbox_pred.rows(), bbox_pred.cols(), 4);
    let output_channel_step = output.channel_step();
    let stride = stride as f32;
    for row in 0..bbox_pred.rows() {
        for col in 0..bbox_pred.cols() {
            let bbox = bbox_pred.pixel(row, col);
            let prior = priors.pixel(row, col);
            let out = output.pixel_mut(row, col);
            let cx = bbox[0] * stride + prior[0];
            let cy = bbox[1] * stride + prior[1];
            let width = bbox[2].exp() * stride;
            let height = bbox[3].exp() * stride;
            out[0] = cx - width * 0.5;
            out[1] = cy - height * 0.5;
            out[2] = cx + width * 0.5;
            out[3] = cy + height * 0.5;
            out[4..output_channel_step].fill(0.0);
        }
    }
}

pub fn kps_decode_to(kps_pred: &Blob, priors: &Blob, stride: usize, output: &mut Blob) {
    assert_eq!(kps_pred.rows(), priors.rows());
    assert_eq!(kps_pred.cols(), priors.cols());
    assert_eq!(kps_pred.channels() % 2, 0);
    assert_eq!(priors.channels(), 2);

    output.resize_for_overwrite(kps_pred.rows(), kps_pred.cols(), kps_pred.channels());
    let output_channels = output.channels();
    let output_channel_step = output.channel_step();
    let stride = stride as f32;
    let points = kps_pred.channels() / 2;
    for row in 0..kps_pred.rows() {
        for col in 0..kps_pred.cols() {
            let kps = kps_pred.pixel(row, col);
            let prior = priors.pixel(row, col);
            let out = output.pixel_mut(row, col);
            for point in 0..points {
                out[2 * point] = kps[2 * point] * stride + prior[0];
                out[2 * point + 1] = kps[2 * point + 1] * stride + prior[1];
            }
            out[output_channels..output_channel_step].fill(0.0);
        }
    }
}

pub fn sigmoid_in_place(blob: &mut Blob) {
    for row in 0..blob.rows() {
        for col in 0..blob.cols() {
            let channels = blob.channels();
            let pixel = blob.pixel_mut(row, col);
            for value in pixel.iter_mut().take(channels) {
                let clamped = value.clamp(-SIGMOID_CLAMP, SIGMOID_CLAMP);
                *value = 1.0 / (1.0 + (-clamped).exp());
            }
        }
    }
}

pub fn flatten3_to(input0: &Blob, input1: &Blob, input2: &Blob, output: &mut Blob) {
    let channels = flattened_len(input0) + flattened_len(input1) + flattened_len(input2);
    output.resize_for_overwrite(1, 1, channels);

    let mut offset = 0;
    append_flattened(input0, output, &mut offset);
    append_flattened(input1, output, &mut offset);
    append_flattened(input2, output, &mut offset);
    output.pixel_mut(0, 0)[offset..].fill(0.0);
}

pub fn decode_and_concat_to(heads: &HeadOutputs, decoded: &mut DecodedOutputs) {
    meshgrid_to(
        heads.cls[0].cols(),
        heads.cls[0].rows(),
        8,
        &mut decoded.prior_scratch[0],
    );
    meshgrid_to(
        heads.cls[1].cols(),
        heads.cls[1].rows(),
        16,
        &mut decoded.prior_scratch[1],
    );
    meshgrid_to(
        heads.cls[2].cols(),
        heads.cls[2].rows(),
        32,
        &mut decoded.prior_scratch[2],
    );

    bbox_decode_to(
        &heads.reg[0],
        &decoded.prior_scratch[0],
        8,
        &mut decoded.reg_scratch[0],
    );
    bbox_decode_to(
        &heads.reg[1],
        &decoded.prior_scratch[1],
        16,
        &mut decoded.reg_scratch[1],
    );
    bbox_decode_to(
        &heads.reg[2],
        &decoded.prior_scratch[2],
        32,
        &mut decoded.reg_scratch[2],
    );
    kps_decode_to(
        &heads.kps[0],
        &decoded.prior_scratch[0],
        8,
        &mut decoded.kps_scratch[0],
    );
    kps_decode_to(
        &heads.kps[1],
        &decoded.prior_scratch[1],
        16,
        &mut decoded.kps_scratch[1],
    );
    kps_decode_to(
        &heads.kps[2],
        &decoded.prior_scratch[2],
        32,
        &mut decoded.kps_scratch[2],
    );

    flatten3_to(
        &heads.cls[0],
        &heads.cls[1],
        &heads.cls[2],
        &mut decoded.cls,
    );
    flatten3_to(
        &decoded.reg_scratch[0],
        &decoded.reg_scratch[1],
        &decoded.reg_scratch[2],
        &mut decoded.reg,
    );
    flatten3_to(
        &decoded.kps_scratch[0],
        &decoded.kps_scratch[1],
        &decoded.kps_scratch[2],
        &mut decoded.kps,
    );
    flatten3_to(
        &heads.obj[0],
        &heads.obj[1],
        &heads.obj[2],
        &mut decoded.obj,
    );
    sigmoid_in_place(&mut decoded.cls);
    sigmoid_in_place(&mut decoded.obj);
}

pub fn detection_output_to<'a>(
    decoded: &DecodedOutputs,
    params: DetectionOutputParams,
    workspace: &'a mut DetectionOutputWorkspace,
) -> &'a [Face] {
    let anchors = decoded.cls.channels();
    assert_eq!(decoded.obj.channels(), anchors);
    assert_eq!(decoded.reg.channels(), anchors * 4);
    assert_eq!(decoded.kps.channels(), anchors * 10);

    let cls = decoded.cls.pixel(0, 0);
    let obj = decoded.obj.pixel(0, 0);
    let reg = decoded.reg.pixel(0, 0);
    let kps = decoded.kps.pixel(0, 0);

    workspace.candidates.clear();
    workspace.selected.clear();
    for index in 0..anchors {
        let score = (cls[index] * obj[index]).sqrt();
        if score >= params.confidence_threshold {
            let reg_index = 4 * index;
            let kps_index = 10 * index;
            let mut landmarks = [0.0; 10];
            landmarks.copy_from_slice(&kps[kps_index..kps_index + 10]);
            workspace.candidates.push(Face {
                score,
                x: reg[reg_index],
                y: reg[reg_index + 1],
                width: reg[reg_index + 2] - reg[reg_index],
                height: reg[reg_index + 3] - reg[reg_index + 1],
                landmarks,
            });
        }
    }

    workspace
        .candidates
        .sort_by(|left, right| right.score.total_cmp(&left.score));
    truncate_if_limited(&mut workspace.candidates, params.top_k);

    for face in &workspace.candidates {
        let keep = workspace
            .selected
            .iter()
            .all(|selected| jaccard_overlap(face, selected) <= params.overlap_threshold);
        if keep {
            workspace.selected.push(*face);
        }
    }
    truncate_if_limited(&mut workspace.selected, params.keep_top_k);

    &workspace.selected
}

fn flattened_len(blob: &Blob) -> usize {
    blob.rows() * blob.cols() * blob.channels()
}

fn append_flattened(input: &Blob, output: &mut Blob, offset: &mut usize) {
    for row in 0..input.rows() {
        for col in 0..input.cols() {
            let src = &input.pixel(row, col)[..input.channels()];
            let end = *offset + input.channels();
            output.pixel_mut(0, 0)[*offset..end].copy_from_slice(src);
            *offset = end;
        }
    }
}

fn truncate_if_limited(faces: &mut Vec<Face>, limit: isize) {
    if limit >= 0 {
        faces.truncate(limit as usize);
    }
}

fn jaccard_overlap(left: &Face, right: &Face) -> f32 {
    let intersect_xmin = left.x.max(right.x);
    let intersect_ymin = left.y.max(right.y);
    let intersect_xmax = (left.x + left.width).min(right.x + right.width);
    let intersect_ymax = (left.y + left.height).min(right.y + right.height);
    let intersect_width = intersect_xmax - intersect_xmin;
    let intersect_height = intersect_ymax - intersect_ymin;

    if intersect_width <= 0.0 || intersect_height <= 0.0 {
        return 0.0;
    }

    let intersect_size = intersect_width * intersect_height;
    let left_size = left.width * left.height;
    let right_size = right.width * right.height;
    intersect_size / (left_size + right_size - intersect_size)
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::input::{PixelFormat, image_to_initial_blob};
    use crate::model::load_static_model;
    use crate::network::{BackboneOutputs, HeadOutputs, NetworkWorkspace, forward_network_to};

    #[test]
    fn meshgrid_matches_stride_layout() {
        let grid = meshgrid(2, 2, 8);

        assert_eq!(&grid.pixel(0, 0)[0..2], &[0.0, 0.0]);
        assert_eq!(&grid.pixel(0, 1)[0..2], &[8.0, 0.0]);
        assert_eq!(&grid.pixel(1, 0)[0..2], &[0.0, 8.0]);
    }

    #[test]
    fn decode_bbox_and_keypoints_use_prior_and_stride() {
        let mut bbox = Blob::with_shape(1, 1, 4);
        bbox.pixel_mut(0, 0)[0..4].copy_from_slice(&[1.0, 2.0, 0.0, 0.0]);
        let mut kps = Blob::with_shape(1, 1, 10);
        kps.pixel_mut(0, 0)[0..2].copy_from_slice(&[1.0, 2.0]);
        let prior = meshgrid(1, 1, 8);
        let mut decoded_bbox = Blob::new();
        let mut decoded_kps = Blob::new();

        bbox_decode_to(&bbox, &prior, 8, &mut decoded_bbox);
        kps_decode_to(&kps, &prior, 8, &mut decoded_kps);

        assert_eq!(&decoded_bbox.pixel(0, 0)[0..4], &[4.0, 12.0, 12.0, 20.0]);
        assert_eq!(&decoded_kps.pixel(0, 0)[0..2], &[8.0, 16.0]);
    }

    #[test]
    fn decoded_outputs_run_on_real_raw_network() {
        let width = 96;
        let height = 96;
        let step = width * 3;
        let pixels: Vec<u8> = (0..step * height).map(|idx| (idx % 251) as u8).collect();
        let input = image_to_initial_blob(&pixels, width, height, step, PixelFormat::Rgb);
        let model = load_static_model();
        let mut workspace = NetworkWorkspace::default();
        let mut backbone = BackboneOutputs::default();
        let mut heads = HeadOutputs::default();
        let mut decoded = DecodedOutputs::default();

        forward_network_to(&input, &model, &mut workspace, &mut backbone, &mut heads);
        decode_and_concat_to(&heads, &mut decoded);

        let anchors = 12 * 12 + 6 * 6 + 3 * 3;
        assert_eq!(decoded.cls.channels(), anchors);
        assert_eq!(decoded.reg.channels(), anchors * 4);
        assert_eq!(decoded.kps.channels(), anchors * 10);
        assert_eq!(decoded.obj.channels(), anchors);
        assert!(
            decoded
                .cls
                .pixel(0, 0)
                .iter()
                .take(decoded.cls.channels())
                .all(|value| (0.0..=1.0).contains(value))
        );
        assert!(
            decoded
                .obj
                .pixel(0, 0)
                .iter()
                .take(decoded.obj.channels())
                .all(|value| (0.0..=1.0).contains(value))
        );
    }

    #[test]
    fn detection_output_filters_sorts_and_suppresses() {
        let mut decoded = DecodedOutputs::default();
        decoded.cls.resize_for_overwrite(1, 1, 3);
        decoded.obj.resize_for_overwrite(1, 1, 3);
        decoded.reg.resize_for_overwrite(1, 1, 12);
        decoded.kps.resize_for_overwrite(1, 1, 30);
        decoded.cls.pixel_mut(0, 0)[0..3].copy_from_slice(&[0.81, 0.64, 0.49]);
        decoded.obj.pixel_mut(0, 0)[0..3].copy_from_slice(&[1.0, 1.0, 1.0]);
        decoded.reg.pixel_mut(0, 0)[0..12].copy_from_slice(&[
            0.0, 0.0, 10.0, 10.0, 1.0, 1.0, 11.0, 11.0, 100.0, 100.0, 110.0, 110.0,
        ]);
        for index in 0..30 {
            decoded.kps.pixel_mut(0, 0)[index] = index as f32;
        }
        let mut workspace = DetectionOutputWorkspace::default();

        let faces = detection_output_to(
            &decoded,
            DetectionOutputParams {
                overlap_threshold: 0.45,
                confidence_threshold: 0.2,
                top_k: -1,
                keep_top_k: -1,
            },
            &mut workspace,
        );

        assert_eq!(faces.len(), 2);
        assert_eq!(faces[0].score, 0.9);
        assert_eq!(faces[0].x, 0.0);
        assert_eq!(faces[1].x, 100.0);
        assert_eq!(faces[1].landmarks[0], 20.0);
    }
}
