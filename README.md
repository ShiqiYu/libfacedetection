# libfacedetection

This is an open source library for CNN-based face detection in images. The CNN model has been converted to static variables in C source files. The source code does not depend on any other libraries. What you need is just a C++ compiler. You can compile the source code under Windows, Linux, ARM and any platform with a C++ compiler.

SIMD instructions are used to speed up the detection. You can enable AVX2 if you use Intel CPU or NEON for ARM.

The model files are provided in `src/facedetectcnn-data.cpp` (C++ arrays) & [the model (ONNX) from OpenCV Zoo](https://github.com/opencv/opencv_zoo/tree/master/models/face_detection_yunet). You can try our scripts (C++ & Python) in `opencv_dnn/` with the ONNX model. View the network architecture [here](https://netron.app/?url=https://raw.githubusercontent.com/ShiqiYu/libfacedetection.train/master/onnx/yunet*.onnx).

Please note that OpenCV DNN does not support the latest version of YuNet with dynamic input shape. Please ensure you have the exact same input shape as the one in the ONNX model to run latest YuNet with OpenCV DNN.

examples/detect-image.cpp and examples/detect-camera.cpp show how to use the library.

The library was trained by [libfacedetection.train](https://github.com/ShiqiYu/libfacedetection.train).

![Examples](/images/cnnresult.png "Detection example")

## How to use the code

You can copy the files in directory src/ into your project,
and compile them as the other files in your project.
The source code is written in standard C/C++.
It should be compiled at any platform which supports C/C++.

Some tips:

  * Please add facedetection_export.h file in the position where you copy your facedetectcnn.h files, add #define FACEDETECTION_EXPORT to  facedetection_export.h file. See: [issues #222](https://github.com/ShiqiYu/libfacedetection/issues/222)
  * Please add -O3 to turn on optimizations when you compile the source code using g++.
  * Please choose 'Maximize Speed/-O2' when you compile the source code using Microsoft Visual Studio.
  * You can enable OpenMP to speedup. But the best solution is to call the detection function in different threads.

You can also compile the source code to a static or dynamic library, and then use it in your project.

[How to compile](COMPILE.md)


## CNN-based Face Detection on Intel CPU

Using **AVX2** instructions
| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |  X64         |X64          |  X64         |X64          |
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|cnn (CPU, 640x480)  |  50.02ms     |  19.99      |   6.55ms     |  152.65     |
|cnn (CPU, 320x240)  |  13.09ms     |  76.39      |   1.82ms     |  550.54     |
|cnn (CPU, 160x120)  |   3.61ms     | 277.37      |   0.57ms     | 1745.13     |
|cnn (CPU, 128x96)   |   2.11ms     | 474.60      |   0.33ms     | 2994.23     | 

Using **AVX512** instructions
| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |  X64         |X64          |  X64         |X64          |
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|cnn (CPU, 640x480)  |  46.47ms     |  21.52      |   6.39ms     |  156.47     |
|cnn (CPU, 320x240)  |  12.10ms     |  82.67      |   1.67ms     |  599.31     |
|cnn (CPU, 160x120)  |   3.37ms     | 296.47      |   0.46ms     | 2155.80     |
|cnn (CPU, 128x96)   |   1.98ms     | 504.72      |   0.31ms     | 3198.63     | 

* Minimal face size ~10x10
* Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz
* Multi-thread in 16 threads and 16 processors.


## CNN-based Face Detection on ARM Linux (Raspberry Pi 4 B)

| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|cnn (CPU, 640x480)  |  404.63ms    |  2.47       |  125.47ms    |   7.97      |
|cnn (CPU, 320x240)  |  105.73ms    |  9.46       |   32.98ms    |  30.32      |
|cnn (CPU, 160x120)  |   26.05ms    | 38.38       |    7.91ms    | 126.49      |
|cnn (CPU, 128x96)   |   15.06ms    | 66.38       |    4.50ms    | 222.28      |

* Minimal face size ~10x10
* Raspberry Pi 4 B, Broadcom BCM2835, Cortex-A72 (ARMv8) 64-bit SoC @ 1.5GHz
* Multi-thread in 4 threads and 4 processors.

## Performance on WIDER Face 
Run on default settings: scales=[1.], confidence_threshold=0.02, floating point:
```
AP_easy=0.887, AP_medium=0.871, AP_hard=0.768
```

## Highway Optimized Version

An independent Highway-based implementation has been added under `highway/`.
It keeps the original implementation untouched and exposes a separate C API:

```C++
#include "facedetect_hw.h"

int* results = facedetect_hw_cnn(result_buffer, bgr_image_data,
                                 width, height, step);
```

The Highway version follows the same deployment model as the original project:
build separately for each instruction set/platform. It does not currently use
Highway runtime dynamic dispatch. The current x86 performance path is:

| Backend | Description |
|---------|-------------|
| pure Highway | Portable Highway kernels for pointwise, depthwise, maxpool, image/network/postprocess flow |
| x86 hybrid AVX2 | Highway packed pointwise plus guarded AVX2/FMA intrinsics for selected depthwise/maxpool ceiling kernels |

The public API uses thread-local internal workspaces, so the recommended
multi-threading model is external parallelism: call `facedetect_hw_cnn` from
multiple threads, with one result buffer per calling thread.

Measured resolution benchmark:

| ISA / Backend | 640x480 single | 640x480 external MT | 320x240 single | 320x240 external MT | 160x120 single | 160x120 external MT | 128x96 single | 128x96 external MT |
|---------------|----------------|---------------------|----------------|---------------------|----------------|---------------------|---------------|--------------------|
| origin default/scalar | 65.66 ms / 15.23 FPS | 5.58 ms / 179.19 FPS | 16.41 ms / 60.93 FPS | 1.48 ms / 674.91 FPS | 3.69 ms / 270.69 FPS | 0.47 ms / 2110.46 FPS | 2.20 ms / 453.71 FPS | 0.30 ms / 3291.92 FPS |
| origin AVX2 | 33.42 ms / 29.92 FPS | 4.56 ms / 219.10 FPS | 7.70 ms / 129.91 FPS | 1.08 ms / 926.34 FPS | 1.49 ms / 673.02 FPS | 0.23 ms / 4390.31 FPS | 0.88 ms / 1140.45 FPS | 0.15 ms / 6750.38 FPS |
| origin AVX512 | N/A on this CPU | N/A on this CPU | N/A on this CPU | N/A on this CPU | N/A on this CPU | N/A on this CPU | N/A on this CPU | N/A on this CPU |
| Highway scalar | 53.50 ms / 18.69 FPS | 4.64 ms / 215.70 FPS | 13.93 ms / 71.81 FPS | 1.39 ms / 718.85 FPS | 3.46 ms / 288.90 FPS | 0.44 ms / 2298.29 FPS | 2.06 ms / 484.78 FPS | 0.32 ms / 3112.95 FPS |
| Highway SSE/default | 14.84 ms / 67.39 FPS | 2.62 ms / 381.29 FPS | 3.84 ms / 260.39 FPS | 0.48 ms / 2062.60 FPS | 0.97 ms / 1036.19 FPS | 0.18 ms / 5678.19 FPS | 0.58 ms / 1710.98 FPS | 0.10 ms / 10125.38 FPS |
| Highway AVX2 pure | 9.20 ms / 108.72 FPS | 2.45 ms / 408.33 FPS | 2.34 ms / 427.32 FPS | 0.43 ms / 2323.32 FPS | 0.58 ms / 1727.04 FPS | 0.13 ms / 7747.01 FPS | 0.35 ms / 2894.80 FPS | 0.06 ms / 17268.13 FPS |
| Highway AVX2 hybrid | 8.48 ms / 117.89 FPS | 2.44 ms / 410.30 FPS | 2.14 ms / 468.15 FPS | 0.39 ms / 2538.84 FPS | 0.53 ms / 1903.34 FPS | 0.09 ms / 10740.69 FPS | 0.32 ms / 3158.93 FPS | 0.05 ms / 19047.05 FPS |
| Highway AVX512 pure | N/A on this CPU | N/A on this CPU | N/A on this CPU | N/A on this CPU | N/A on this CPU | N/A on this CPU | N/A on this CPU | N/A on this CPU |

Notes:

* Benchmark command: `fdt_hw_resolution_benchmark.exe images\cnnresult.png 128 28`.
* Test CPU: Intel(R) Core(TM) i7-14700KF, 20 cores, 28 logical processors,
  max clock reported by Windows: 3400 MHz.
* Compiler/toolchain: Visual Studio 17 2022, MSVC 19.40.33821.0, Release build.
* `origin AVX2` was built with `_ENABLE_AVX2`; `origin default/scalar` was
  built without original SIMD macros.
* `Highway scalar` was built with `FDT_HW_FORCE_SCALAR=ON`.
* External MT means external multi-threading with 28 caller threads and one
  result buffer per thread. The reported time is throughput-normalized average
  time per image.
* AVX512 builds are buildable with `/arch:AVX512`, but cannot be run on the
  test CPU, so no AVX512 timing is reported here.
* ARM Linux has not been tested for the Highway version yet.
* Existing ARM Linux numbers below are from the original implementation, not
  the new Highway implementation.
* See `COMPILE.md` and `example/detect-image-highway.cpp` /
  `example/benchmark-highway.cpp` for build and usage examples.

## Rust Crate

A pure Rust implementation is available under `rust/` as the crate
`libfacedetection_rs`. It uses the same CNN model data, generated into Rust
source so the crate is self-contained and does not need to parse the C++ model
file at build time.

The Rust crate is intended for normal Rust dependency usage:

```toml
[dependencies]
libfacedetection_rs = "0.1"
```

Minimal usage:

```rust
use libfacedetection_rs::{image, Detector};

fn main() -> Result<(), libfacedetection_rs::DetectError> {
    let image = image::open("face.jpg")?;
    let mut detector = Detector::new();
    let detection = detector.detect(&image)?;

    println!("faces: {}", detection.face_count());
    Ok(())
}
```

Current crate shape:

| Item | Status |
|------|--------|
| Public API | Safe Rust API: `Detector`, `Detection`, `Face`, `DetectError` |
| Image loading | Via the `image` crate, with PNG/JPEG enabled |
| Model data | Generated static Rust source in `rust/src/generated/model_data.rs` |
| Acceleration | Scalar fallback plus runtime AVX2 dispatch on x86/x86_64 |
| C ABI | Not published in the Rust crate `0.1.0`; C wrapper may be split later |

Measured resolution benchmark against the original C++ implementation:

| Backend | 640x480 single | 640x480 external MT | 320x240 single | 320x240 external MT | 160x120 single | 160x120 external MT | 128x96 single | 128x96 external MT |
|---------|----------------|---------------------|----------------|---------------------|----------------|---------------------|---------------|--------------------|
| origin C++ AVX2 | 35.93 ms / 27.83 FPS | 5.07 ms / 197.39 FPS | 7.57 ms / 132.07 FPS | 0.93 ms / 1070.13 FPS | 1.49 ms / 669.31 FPS | 0.23 ms / 4339.53 FPS | 0.89 ms / 1122.29 FPS | 0.15 ms / 6879.46 FPS |
| Rust AVX2 | 8.73 ms / 114.60 FPS | 2.18 ms / 458.86 FPS | 2.25 ms / 444.14 FPS | 0.36 ms / 2809.03 FPS | 0.53 ms / 1880.12 FPS | 0.08 ms / 11948.88 FPS | 0.32 ms / 3125.11 FPS | 0.07 ms / 14812.76 FPS |

Notes:

* Benchmark image: `images\cnnresult.png`, resized to the listed input
  resolutions before detection.
* C++ command:
  `.\build-hw-bench-avx2-hybrid\Release\fdt_hw_resolution_benchmark.exe images\cnnresult.png 128 28`.
* Rust command:
  `cargo run --release --manifest-path rust/dev/resolution_benchmark/Cargo.toml -- images\cnnresult.png 128 28`.
* Test CPU: Intel(R) Core(TM) i7-14700KF, 20 cores, 28 logical processors,
  max clock reported by Windows: 3400 MHz.
* C++ compiler/toolchain: Visual Studio 17 2022, MSVC 19.40.33821.0, Release
  build. The origin C++ row above is built with `_ENABLE_AVX2`.
* Rust toolchain: `rustc 1.95.0`, `cargo 1.95.0`, release profile. The Rust
  row uses runtime AVX2 dispatch when available.
* External MT means external multi-thread throughput with 28 caller threads.
  The C++ benchmark uses one result buffer per caller thread. The Rust
  benchmark uses one reusable `Detector` per worker thread, warms each worker,
  then synchronizes the timed loop.
* The reported MT time is throughput-normalized average time per image, not
  single-request latency.
* The C++ benchmark uses OpenCV resize; the Rust benchmark uses the `image`
  crate resize path. The table is intended for performance comparison, not
  result-buffer parity or accuracy evaluation.
* See `rust/README.md` for crate usage and `rust/docs/rs-performance-optimization-report.md`
  for the detailed optimization report.

## Author
* Shiqi Yu, <shiqi.yu@gmail.com>

## Contributors
All contributors who contribute at GitHub.com are listed [here](https://github.com/ShiqiYu/libfacedetection/graphs/contributors). 

The contributors who were not listed at GitHub.com:
* Jia Wu (吴佳)
* Dong Xu (徐栋)
* Shengyin Wu (伍圣寅)

## Acknowledgment
The work was partly supported by the Science Foundation of Shenzhen (Grant No. 20170504160426188).

## Citation
We published a paper for the main idea of this repository:

```
@article{yunet,
	title={YuNet: A Tiny Millisecond-level Face Detector},
	author={Wu, Wei and Peng, Hanyang and Yu, Shiqi},
	journal={Machine Intelligence Research},
	pages={1--10},
	year={2023},
	doi={10.1007/s11633-023-1423-y},
	publisher={Springer}
}
```
The paper can be open accessed at https://link.springer.com/article/10.1007/s11633-023-1423-y.


We published a paper on face detection to evaluate different methods. This project has also been evaluated in the paper.
```
@article{facedetect-yu,
	author={Feng, Yuantao and Yu, Shiqi and Peng, Hanyang and Li, Yan-Ran and Zhang, Jianguo},
	journal={IEEE Transactions on Biometrics, Behavior, and Identity Science}, 
	title={Detect Faces Efficiently: A Survey and Evaluations}, 
	year={2022},
	volume={4},
	number={1},
	pages={1-18},
	doi={10.1109/TBIOM.2021.3120412}
}
```
The paper can be open accessed at https://ieeexplore.ieee.org/document/9580485

The loss used in training is EIoU, a novel extended IoU. More details can be found in:
```
@article{eiou,
	author={Peng, Hanyang and Yu, Shiqi},
	journal={IEEE Transactions on Image Processing}, 
	title={A Systematic IoU-Related Method: Beyond Simplified Regression for Better Localization}, 
	year={2021},
	volume={30},
	pages={5032-5044},
	doi={10.1109/TIP.2021.3077144}
}
```
The paper can be open accessed at https://ieeexplore.ieee.org/document/9429909.

