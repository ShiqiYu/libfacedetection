# hw implementation plan

## Goal

Build an independent `hw` version under `./highway` to explore the performance ceiling of a Highway-based implementation without disturbing the existing hand-written SIMD code in `src/`.

The experiment should answer three questions:

1. Can a direct Highway rewrite of the current SIMD primitives match or beat the hand-written version?
2. Can a Highway-specific kernel layout beat the hand-written version after changing loop order, packing, or dispatch?
3. Is the extra dependency worthwhile for cross-platform SIMD maintenance even if peak speed is similar?

## Naming

Use `hw` for project-local names:

- Directory prefix: `highway/`
- File prefix: `hw_`
- Public experimental API prefix: `facedetect_hw_`
- CMake target prefix: `fdt_hw_`
- CMake options: `FDT_HW_*`

Avoid long names such as `facedetect_highway_*` in new files unless referring to the external library itself.

## Dependency

Use the prebuilt Highway 1.3.0 package from:

```text
E:/projects/thirdParty/lib/highway-1.3.0
```

The `hw` build must locate it via `find_package`, not vendoring or `add_subdirectory`.

Recommended configure command:

```powershell
cmake -S highway -B build-hw -DFDT_HW_HIGHWAY_ROOT=E:/projects/thirdParty/lib/highway-1.3.0
```

Recommended CMake pattern:

```cmake
find_package(hwy 1.3.0 CONFIG QUIET)

if(TARGET hwy::hwy)
    set(FDT_HW_HIGHWAY_TARGET hwy::hwy)
elseif(TARGET hwy)
    set(FDT_HW_HIGHWAY_TARGET hwy)
else()
    message(FATAL_ERROR "Highway was found, but no known hwy target is available")
endif()
```

Some package managers expose `find_package(hwy CONFIG REQUIRED)` plus target `hwy::hwy`; the local package should be checked during implementation. Keep the CMake small but tolerant so the same source works with both the local build and package-manager installs.

## Proposed Directory

```text
highway/
  CMakeLists.txt
  README.md

  docs/
    README.md
    hw-plan.md

  include/
    facedetect_hw.h

  src/
    hw_blob.h
    hw_filters.h
    hw_api.cpp
    hw_model.cpp
    hw_data.cpp
    hw_kernels.h
    hw_kernels_scalar.cpp
    hw_kernels_hw.cpp
    hw_kernels_hw-inl.h
    hw_profiler.h

  tests/
    hw_test_kernels.cpp
    hw_test_api_equivalence.cpp

  benchmark/
    hw_benchmark.cpp
    hw_compare.cpp
```

## Public API

Keep the API close to the existing one, but avoid symbol conflicts:

```cpp
int* facedetect_hw_cnn(unsigned char* result_buffer,
                       unsigned char* bgr_image_data,
                       int width,
                       int height,
                       int step);
```

The output buffer layout should match `facedetect_cnn`:

- first `int`: face count
- each face: `FACEDETECTION_RESULT_STRIDE_SHORTS` shorts
- max faces: `FACEDETECTION_RESULT_MAX_FACES`
- buffer size: `FACEDETECTION_RESULT_BUFFER_SIZE`

The `hw` implementation may include the existing `src/facedetectcnn.h` for shared constants, or duplicate constants during the first experimental milestone. Prefer including the existing header once symbol/export friction is understood.

## Build Options

Use explicit experimental options:

```cmake
option(FDT_HW_BUILD_TESTS "Build hw doctest tests" ON)
option(FDT_HW_BUILD_BENCHMARKS "Build hw benchmarks" ON)
option(FDT_HW_DYNAMIC_DISPATCH "Use Highway dynamic dispatch" OFF)
option(FDT_HW_ENABLE_PROFILER "Enable per-stage profiling" ON)
set(FDT_HW_KERNEL_MODE "primitive" CACHE STRING "hw kernel mode: primitive or packed")
```

Start with static dispatch for clean A/B measurements. Add dynamic dispatch only after correctness and benchmark harnesses are stable.

## Architecture

Keep the independent `hw` implementation deep at the kernel seam.

Initial seam:

```text
facedetect_hw_cnn
  -> hw_objectdetect_cnn
    -> hw convolution flow
      -> hw kernels
```

Kernel interface:

```cpp
float hw_dot_product(const float* a, const float* b, int n);
void hw_mul_add(const float* a, const float* b, float* acc, int n);
void hw_add(const float* a, const float* b, float* out, int n);
void hw_relu(float* data, int n);
void hw_maxpool_2x2s2(...);

void hw_pointwise_1x1(...);
void hw_depthwise_3x3(...);
void hw_element_add(...);
```

The primitive functions are useful for the first milestone. The kernel functions are where the performance ceiling should be explored.

## Milestones

### Milestone 1: Independent Build

- Add `highway/CMakeLists.txt`.
- Use `find_package(HWY 1.3.0 CONFIG REQUIRED)`.
- Build a static or shared `fdt_hw` target.
- Link against the discovered Highway target.
- Do not modify the root `src/` implementation except for optional shared constants.

### Milestone 2: API Parity

- Implement `facedetect_hw_cnn`.
- Copy or adapt the current model flow from `src/facedetectcnn-model.cpp`.
- Copy or adapt the current weights from `src/facedetectcnn-data.cpp`.
- Keep the first version algorithmically equivalent to the hand-written SIMD version.

### Milestone 3: Primitive Highway Backend

Rewrite these operations using Highway:

- dot product
- multiply-add accumulation
- vector add
- ReLU
- 2x2 stride-2 maxpool

This answers whether Highway alone improves the current implementation shape.

### Milestone 4: Correctness Tests

Use `externals/doctest.h`.

Add focused tests:

- scalar vs hw dot product
- scalar vs hw multiply-add
- scalar vs hw add
- scalar vs hw ReLU
- scalar vs hw maxpool
- scalar vs hw pointwise 1x1
- scalar vs hw depthwise 3x3

Then add API equivalence tests:

- same image through original `facedetect_cnn`
- same image through `facedetect_hw_cnn`
- compare face count and result buffer fields with tight tolerances

Kernel tests are required because NMS and thresholds can magnify small floating-point differences.

### Milestone 5: Benchmark Harness

Add two benchmark programs:

```text
hw_benchmark.cpp
hw_compare.cpp
```

Benchmark matrix:

```text
128x96
160x120
320x240
640x480
```

For each input size:

- warmup: 10 iterations
- measured: 100 or 256 iterations
- report: min, avg, p50, p95
- modes: single-thread first, then OpenMP multi-thread

Compare:

- original hand-written SIMD
- hw primitive mode
- hw packed/kernel mode once available

### Milestone 6: Packed Kernels

This is the performance-ceiling phase.

Current pointwise convolution does:

```text
for each output channel:
  dot(input channels, weights for output channel)
```

The packed `hw` version may:

- pack/transpose pointwise weights during model initialization
- compute multiple output channels per pixel
- reduce horizontal reductions inside the hot loop
- specialize for channel counts 16, 32, and 64
- provide small-channel paths for 1, 4, and 10 channel heads

Suggested data structure:

```cpp
struct HwPackedFilter {
    int channels;
    int num_filters;
    bool is_depthwise;
    bool is_pointwise;
    std::vector<float> weights;
    std::vector<float> biases;
};
```

Do not add packing until the primitive `hw` version is correct and benchmarked.

### Milestone 7: Dynamic Dispatch

After static dispatch is stable:

- add `FDT_HW_DYNAMIC_DISPATCH`
- route hot kernels through Highway dynamic dispatch
- benchmark startup cost and steady-state throughput separately

Dynamic dispatch is useful for distribution; static dispatch is cleaner for early performance experiments.

## Profiling

The `hw` implementation should include a lightweight stage profiler, disabled or compiled out when not needed.

Recommended stage names:

```text
convert_image
conv_head
backbone
neck
head_cls
head_bbox
head_obj
head_kps
decode
nms
api_pack
```

For kernel-level experiments, add counters for:

```text
pointwise_1x1
depthwise_3x3
relu
maxpool
element_add
```

## Success Criteria

Minimum useful result:

- `hw` builds independently.
- `facedetect_hw_cnn` runs on the same inputs as `facedetect_cnn`.
- doctest equivalence tests pass.
- benchmark numbers compare original vs hw primitive mode.

Performance-ceiling result:

- packed/kernel `hw` mode is benchmarked against original.
- per-stage profile shows where speedup or slowdown comes from.
- results explain whether the bottleneck is SIMD primitive choice, memory layout, weight layout, OpenMP scheduling, or post-processing.

## Current Empirical Checkpoint

As of the real-image benchmark slice:

```text
image=images\cnnresult.png size=1280x960 step=3840
original faces=39
hw faces=39
result short mismatches=0
original facedetect_cnn  avg=269.7163 ms
hw facedetect_hw_cnn     avg= 38.8514 ms
```

Current stage profile:

```text
image transform          avg= 2.3617 ms
backbone                 avg=29.9302 ms
fpn + raw heads          avg= 8.2240 ms
network workspace        avg=37.6668 ms
decode + concat          avg= 2.1938 ms
nms                      avg= 0.0955 ms
```

The current x64 performance-ceiling path uses Highway packed pointwise plus a hybrid intrinsics path for depthwise/maxpool, because microbenchmarks and real-image profiling showed intrinsics depthwise is faster than the first Highway depthwise implementation. The latest pipeline reuses caller-owned blobs across input, backbone, FPN/head, raw head output, and decoded output stages, fuses ReLU into pointwise/depthwise stores where applicable, directly flattens decoded outputs, and avoids most repeated temporary allocations. The next performance-ceiling work should prioritize the remaining large-map convolution traffic in `conv2`, `conv0`, and `conv_head`.

## Risks

- Direct Highway primitive replacement may not beat hand-written AVX2/NEON because the current code is already close to hardware intrinsics.
- Horizontal reductions in pointwise convolution may remain the limiting factor unless the packed kernel changes loop order.
- Small-channel heads such as 1, 4, and 10 channels need care; padding may be faster than masked tails.
- Floating-point differences can alter threshold/NMS output. Kernel-level tests are needed to distinguish numerical drift from logic bugs.

## Recommended First Implementation Slice

1. Create `highway/CMakeLists.txt` with `find_package(HWY 1.3.0 CONFIG REQUIRED)`.
2. Add `include/facedetect_hw.h`.
3. Add scalar and Highway primitive kernels.
4. Add doctest kernel equivalence tests.
5. Add `hw_benchmark.cpp` for primitive mode.
6. Only then copy/adapt the full model flow into `facedetect_hw_cnn`.

This sequence gives a fast feedback loop before the full network is moved.
