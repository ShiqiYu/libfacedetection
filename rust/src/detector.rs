use crate::error::DetectError;
use crate::input::PixelFormat;
use crate::model::{Model, load_static_model};
use crate::network::{
    BackboneOutputs, HeadOutputs, NetworkWorkspace, PixelInput, forward_network_from_pixels_to,
};
use crate::postprocess::{
    DEFAULT_DETECTION_OUTPUT_PARAMS, DecodedOutputs, DetectionOutputWorkspace,
    decode_and_concat_to, detection_output_to,
};
use crate::result::{Detection, DetectionSummary, ResultBuffer, write_faces_to_result_buffer};

use image::DynamicImage;
use std::path::Path;

/// Reusable detector instance.
///
/// Create one detector and reuse it for repeated calls. Future model and
/// workspace storage will live here so callers do not pay per-frame allocation
/// costs.
#[derive(Debug)]
pub struct Detector {
    model: Model,
    workspace: Workspace,
}

#[derive(Debug, Default)]
struct Workspace {
    network: NetworkWorkspace,
    backbone_outputs: BackboneOutputs,
    head_outputs: HeadOutputs,
    decoded_outputs: DecodedOutputs,
    detection_output: DetectionOutputWorkspace,
}

impl Detector {
    /// Creates a detector with an owned model/workspace.
    pub fn new() -> Self {
        Self::default()
    }

    /// Simplest Rust API: pass an image loaded by the `image` crate and receive
    /// an owned result.
    pub fn detect(&mut self, image: &DynamicImage) -> Result<Detection, DetectError> {
        if let Some(rgb) = image.as_rgb8() {
            let width = usize::try_from(rgb.width()).map_err(|_| DetectError::DimensionOverflow)?;
            let height =
                usize::try_from(rgb.height()).map_err(|_| DetectError::DimensionOverflow)?;
            let step = width.checked_mul(3).ok_or(DetectError::DimensionOverflow)?;
            self.detect_rgb(rgb.as_raw(), width, height, step)
        } else {
            let rgb = image.to_rgb8();
            let width = usize::try_from(rgb.width()).map_err(|_| DetectError::DimensionOverflow)?;
            let height =
                usize::try_from(rgb.height()).map_err(|_| DetectError::DimensionOverflow)?;
            let step = width.checked_mul(3).ok_or(DetectError::DimensionOverflow)?;
            self.detect_rgb(rgb.as_raw(), width, height, step)
        }
    }

    /// Convenience API for callers who want the detector to read the image file.
    pub fn detect_path<P: AsRef<Path>>(&mut self, path: P) -> Result<Detection, DetectError> {
        let image = image::open(path)?;
        self.detect(&image)
    }

    /// Low-level API: pass a tightly packed or strided RGB image slice.
    ///
    /// Most Rust callers should use [`Detector::detect`] instead. This entry is
    /// useful for callers that already own decoded RGB bytes.
    pub fn detect_rgb(
        &mut self,
        rgb: &[u8],
        width: usize,
        height: usize,
        step: usize,
    ) -> Result<Detection, DetectError> {
        let mut result = ResultBuffer::new();
        let summary = self.detect_pixels(
            rgb,
            width,
            height,
            step,
            PixelFormat::Rgb,
            result.as_mut_bytes(),
        )?;
        let faces = self.workspace.detection_output.faces().to_vec();
        debug_assert_eq!(faces.len(), summary.face_count());
        Ok(Detection::new(result, faces))
    }

    /// Low-level API: pass a tightly packed or strided BGR image slice.
    ///
    /// Most Rust callers should use [`Detector::detect`] instead. This entry is
    /// kept for C API parity, benchmarks, and future internal image-transform
    /// tests.
    pub fn detect_bgr(
        &mut self,
        bgr: &[u8],
        width: usize,
        height: usize,
        step: usize,
    ) -> Result<Detection, DetectError> {
        let mut result = ResultBuffer::new();
        let summary = self.detect_pixels(
            bgr,
            width,
            height,
            step,
            PixelFormat::Bgr,
            result.as_mut_bytes(),
        )?;
        let faces = self.workspace.detection_output.faces().to_vec();
        debug_assert_eq!(faces.len(), summary.face_count());
        Ok(Detection::new(result, faces))
    }

    /// Compatibility API: writes the C-compatible result buffer supplied by the
    /// caller.
    pub fn detect_into(
        &mut self,
        bgr: &[u8],
        width: usize,
        height: usize,
        step: usize,
        result_buffer: &mut [u8],
    ) -> Result<DetectionSummary, DetectError> {
        self.detect_pixels(bgr, width, height, step, PixelFormat::Bgr, result_buffer)
    }

    fn detect_pixels(
        &mut self,
        pixels: &[u8],
        width: usize,
        height: usize,
        step: usize,
        pixel_format: PixelFormat,
        result_buffer: &mut [u8],
    ) -> Result<DetectionSummary, DetectError> {
        validate_image(pixels, width, height, step)?;
        ResultBuffer::validate_len(result_buffer.len())?;

        self.run_pipeline_stub(pixels, width, height, step, pixel_format, result_buffer)
    }

    fn run_pipeline_stub(
        &mut self,
        pixels: &[u8],
        width: usize,
        height: usize,
        step: usize,
        pixel_format: PixelFormat,
        result_buffer: &mut [u8],
    ) -> Result<DetectionSummary, DetectError> {
        let Workspace {
            network,
            backbone_outputs,
            head_outputs,
            decoded_outputs,
            detection_output,
        } = &mut self.workspace;

        forward_network_from_pixels_to(
            PixelInput {
                pixels,
                width,
                height,
                step,
                pixel_format,
            },
            &self.model,
            network,
            backbone_outputs,
            head_outputs,
        );
        decode_and_concat_to(head_outputs, decoded_outputs);
        let faces = detection_output_to(
            decoded_outputs,
            DEFAULT_DETECTION_OUTPUT_PARAMS,
            detection_output,
        );
        let face_count = write_faces_to_result_buffer(faces, result_buffer);
        Ok(DetectionSummary::new(face_count))
    }
}

impl Default for Detector {
    fn default() -> Self {
        Self {
            model: load_static_model(),
            workspace: Workspace::default(),
        }
    }
}

fn validate_image(
    pixels: &[u8],
    width: usize,
    height: usize,
    step: usize,
) -> Result<(), DetectError> {
    if width == 0 {
        return Err(DetectError::InvalidDimension {
            name: "width",
            value: 0,
        });
    }
    if height == 0 {
        return Err(DetectError::InvalidDimension {
            name: "height",
            value: 0,
        });
    }

    let min_step = width.checked_mul(3).ok_or(DetectError::DimensionOverflow)?;
    if step < min_step {
        return Err(DetectError::StepTooSmall { step, min_step });
    }

    let required_len = step
        .checked_mul(height)
        .ok_or(DetectError::DimensionOverflow)?;
    if pixels.len() < required_len {
        return Err(DetectError::ImageTooSmall {
            actual: pixels.len(),
            required: required_len,
        });
    }

    Ok(())
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::result::FACEDETECTION_RESULT_BUFFER_SIZE;

    #[test]
    fn rejects_short_step() {
        let mut detector = Detector::new();
        let mut result = vec![0_u8; FACEDETECTION_RESULT_BUFFER_SIZE];
        let err = detector
            .detect_into(&[0; 12], 4, 1, 11, &mut result)
            .expect_err("step must hold width * 3 bytes");

        assert_eq!(
            err,
            DetectError::StepTooSmall {
                step: 11,
                min_step: 12
            }
        );
    }

    #[test]
    fn rejects_short_image_slice() {
        let mut detector = Detector::new();
        let mut result = vec![0_u8; FACEDETECTION_RESULT_BUFFER_SIZE];
        let err = detector
            .detect_into(&[0; 5], 2, 2, 6, &mut result)
            .expect_err("image slice must contain all rows");

        assert_eq!(
            err,
            DetectError::ImageTooSmall {
                actual: 5,
                required: 12
            }
        );
    }

    #[test]
    fn detect_accepts_dynamic_image() {
        let image = DynamicImage::new_rgb8(3, 2);
        let mut detector = Detector::new();
        let detection = detector.detect(&image).expect("valid image should run");

        assert_eq!(detection.face_count(), detection.faces().len());
        let mut count_bytes = [0_u8; 4];
        count_bytes.copy_from_slice(&detection.result_buffer()[..4]);
        assert_eq!(
            detection.face_count(),
            i32::from_ne_bytes(count_bytes) as usize
        );
    }

    #[test]
    fn detector_owns_static_model() {
        let detector = Detector::new();

        assert_eq!(detector.model.len(), crate::model::CONV_LAYER_COUNT);
    }
}
