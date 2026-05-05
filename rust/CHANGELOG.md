# Changelog

## 0.1.0

- Initial crates.io release candidate.
- Pure Rust CNN face detector based on libfacedetection model data.
- Safe Rust API with `Detector::detect`, `Detector::detect_path`, `detect_rgb`, and `detect_bgr`.
- Structured `Detection` and `Face` outputs.
- Legacy result-buffer layout available through `Detection::result_buffer` and `Detector::detect_into`.
- Generated static Rust model data included in the crate; no external model download or build-time C++ parsing required.
- Scalar fallback and AVX2 acceleration on supported x86/x86_64 CPUs.
- PNG and JPEG image loading through the re-exported `image` crate.

