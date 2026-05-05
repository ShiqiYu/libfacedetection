# libfacedetection_rs

Pure Rust face detection library based on the `libfacedetection` CNN model.

The crate provides a small safe Rust API, generated static model data,
postprocess/NMS, structured face results, the legacy result-buffer layout, scalar
fallbacks, and AVX2 acceleration on supported x86/x86_64 CPUs.

## Installation

```toml
[dependencies]
libfacedetection_rs = "0.1"
```

## Quick Start

Use the re-exported `image` crate to read an image, then pass it directly to one
reusable `Detector`:

```rust
use libfacedetection_rs::{image, Detector};

fn main() -> Result<(), libfacedetection_rs::DetectError> {
    let image = image::open("face.jpg")?;

    let mut detector = Detector::new();
    let detection = detector.detect(&image)?;

    println!("faces: {}", detection.face_count());
    for face in detection.faces() {
        println!(
            "score={:.2} box=({}, {}, {}, {})",
            face.score, face.x, face.y, face.width, face.height
        );
    }

    Ok(())
}
```

For a shorter file-based call:

```rust
use libfacedetection_rs::Detector;

fn main() -> Result<(), libfacedetection_rs::DetectError> {
    let mut detector = Detector::new();
    let detection = detector.detect_path("face.jpg")?;

    println!("faces: {}", detection.face_count());

    Ok(())
}
```

`Detector` owns the model and reusable workspace, so create one detector and
reuse it for repeated calls.

## Image Input

The high-level API accepts `image::DynamicImage` directly. Callers do not need
to prepare BGR/RGB buffers or manually convert channel order.

Low-level `detect_rgb` and `detect_bgr` methods remain available for callers
that already own decoded image bytes.

PNG and JPEG decoding are enabled through the `image` crate with default
features disabled.

## Result Output

The safe API returns `Detection`, which contains:

- structured `Face` values;
- `face_count()`;
- `faces()`;
- `result_buffer()` for compatibility with the legacy libfacedetection result
  layout.

The crate does not publish a C ABI in `0.1.0`. A dedicated C wrapper can be
added later without expanding the Rust API surface.

## Platform Support

- Rust MSRV: `1.85`
- Works with scalar fallback on supported Rust targets.
- Uses runtime AVX2 detection on x86/x86_64 and falls back to scalar code when
  AVX2 is unavailable.
- ARM/NEON and multithreading are future optimization areas.

## Benchmark

Run the published single-image benchmark helper with a real image:

```powershell
cargo run --release --example benchmark -- path\to\face.jpg 100
```

For resolution and external multi-thread benchmarking, the repository also
contains a dev-only harness that is not included in the crates.io package:

```powershell
cargo run --release --manifest-path dev/resolution_benchmark/Cargo.toml -- ..\images\cnnresult.png 128 28
```

Measured resolution benchmark against the original C++ implementation:

| Backend | 640x480 single | 640x480 external MT | 320x240 single | 320x240 external MT | 160x120 single | 160x120 external MT | 128x96 single | 128x96 external MT |
|---------|----------------|---------------------|----------------|---------------------|----------------|---------------------|---------------|--------------------|
| origin C++ AVX2 | 35.93 ms / 27.83 FPS | 5.07 ms / 197.39 FPS | 7.57 ms / 132.07 FPS | 0.93 ms / 1070.13 FPS | 1.49 ms / 669.31 FPS | 0.23 ms / 4339.53 FPS | 0.89 ms / 1122.29 FPS | 0.15 ms / 6879.46 FPS |
| Rust AVX2 | 8.73 ms / 114.60 FPS | 2.18 ms / 458.86 FPS | 2.25 ms / 444.14 FPS | 0.36 ms / 2809.03 FPS | 0.53 ms / 1880.12 FPS | 0.08 ms / 11948.88 FPS | 0.32 ms / 3125.11 FPS | 0.07 ms / 14812.76 FPS |

Notes:

- Benchmark image: `images\cnnresult.png`, resized to the listed input
  resolutions before detection.
- C++ command:
  `.\build-hw-bench-avx2-hybrid\Release\fdt_hw_resolution_benchmark.exe images\cnnresult.png 128 28`.
- Rust command:
  `cargo run --release --manifest-path rust/dev/resolution_benchmark/Cargo.toml -- images\cnnresult.png 128 28`.
- Test CPU: Intel(R) Core(TM) i7-14700KF, 20 cores, 28 logical processors,
  max clock reported by Windows: 3400 MHz.
- C++ compiler/toolchain: Visual Studio 17 2022, MSVC 19.40.33821.0, Release
  build. The origin C++ row above is built with `_ENABLE_AVX2`.
- Rust toolchain: `rustc 1.95.0`, `cargo 1.95.0`, release profile. The Rust
  row uses runtime AVX2 dispatch when available.
- External MT means external multi-thread throughput with 28 caller threads.
  The C++ benchmark uses one result buffer per caller thread. The Rust
  benchmark uses one reusable `Detector` per worker thread, warms each worker,
  then synchronizes the timed loop.
- The reported MT time is throughput-normalized average time per image, not
  single-request latency.
- The C++ benchmark uses OpenCV resize; the Rust benchmark uses the `image`
  crate resize path. The table is intended for performance comparison, not
  result-buffer parity or accuracy evaluation.
- Performance depends on CPU, image size, compiler version, resize path, and
  enabled CPU features.

## Stability

This crate is in early `0.x` development. The public API is intentionally small
while packaging, platform coverage, and optional C ABI support continue to
evolve.

## License

BSD-3-Clause. See `LICENSE`.
