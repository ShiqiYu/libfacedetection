#![allow(dead_code)]

//! Pure Rust face detection library based on the libfacedetection CNN model.
//!
//! The easiest Rust API is [`Detector::detect`]:
//!
//! ```rust
//! use libfacedetection_rs::{image, Detector};
//!
//! # fn main() -> Result<(), libfacedetection_rs::DetectError> {
//! let image = image::DynamicImage::new_rgb8(2, 2);
//!
//! let mut detector = Detector::new();
//! let detection = detector.detect(&image)?;
//!
//! assert_eq!(detection.face_count(), 0);
//! # Ok(())
//! # }
//! ```
//!
//! The crate runs the imported CNN model and returns both structured faces and
//! the C-compatible result buffer behind the same public API.

mod blob;
mod detector;
mod error;
mod filter;
mod input;
mod kernels;
mod layers;
mod model;
mod network;
mod postprocess;
mod result;

pub use detector::Detector;
pub use error::DetectError;
pub use image;
pub use result::{
    Detection, FACEDETECTION_RESULT_BUFFER_SIZE, FACEDETECTION_RESULT_MAX_FACES,
    FACEDETECTION_RESULT_STRIDE_SHORTS, Face, ResultBuffer,
};

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn simple_api_returns_empty_detection_for_the_scaffold() {
        let image = image::DynamicImage::new_rgb8(4, 3);

        let mut detector = Detector::new();
        let detection = detector.detect(&image).expect("valid input should run");

        assert_eq!(detection.face_count(), detection.faces().len());
        assert_eq!(
            detection.result_buffer().len(),
            FACEDETECTION_RESULT_BUFFER_SIZE
        );
    }

    #[test]
    fn detect_into_writes_a_valid_empty_result_buffer() {
        let width = 2;
        let height = 2;
        let step = width * 3;
        let bgr = vec![0_u8; step * height];
        let mut result_buffer = ResultBuffer::new();

        let mut detector = Detector::new();
        let summary = detector
            .detect_into(&bgr, width, height, step, result_buffer.as_mut_bytes())
            .expect("valid input should run");

        assert_eq!(summary.face_count(), result_buffer.face_count());
    }
}
