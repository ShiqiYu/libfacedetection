# Rust migration execution plan

## 1. Goal

Build an independent pure Rust implementation under `./rust` that preserves the
current public face detection capability while creating a new path for exploring
performance limits.

The migration should answer four questions:

1. Can the fixed YuNet-style detector be implemented in Rust without third-party
   runtime dependencies?
2. Can Rust match the original `facedetect_cnn` result-buffer behavior exactly?
3. Can stable Rust plus carefully isolated `unsafe` SIMD approach the current
   Highway performance ceiling?
4. Which performance ideas become easier or harder after moving ownership,
   workspace reuse, and kernel dispatch into Rust?

The Rust implementation is not intended to replace the existing C++ path in the
first slice. It should start as an independent experimental line, like the
current `highway/hw` implementation.

## 2. Current Baseline

The repository currently has three useful reference paths:

| Path | Role | Current signal |
| --- | --- | --- |
| `src/` original C++ | Compatibility and public API baseline | about 269 ms on `images/cnnresult.png` |
| `highway/` pure Highway | Portable SIMD baseline | about 40.7 ms on x86 AVX2 build |
| `highway/` x86 hybrid | Current x86 latency ceiling | about 38.1-38.9 ms |

The important established facts are:

- the result buffer can be matched exactly on `images/cnnresult.png`;
- the model has 53 static filters in `src/facedetectcnn-data.cpp`;
- the optimized implementation shape is already known:
  - persistent model-owned packed pointwise plans;
  - caller-owned or thread-local workspaces;
  - output-parameter layer APIs;
  - fused ReLU where possible;
  - direct decode flatten;
  - stage and hotspot benchmark loops.

The Rust line should reuse these lessons instead of rediscovering them.

## 3. Constraints

### 3.1 Dependency policy

The first Rust implementation should avoid third-party runtime dependencies.

Allowed in the first slice:

- Rust standard library;
- `image` crate for user-facing image loading and decode;
- `std::arch` / `core::arch` intrinsics behind target-specific modules;
- build scripts if needed for generated model data;
- local test fixtures generated from existing C++ outputs.

Avoid in the first slice:

- `ndarray`, `rayon`, `opencv`, or benchmark crates in the library hot path;
- nightly-only portable SIMD as the main implementation strategy;
- C++ FFI calls from the Rust detector, except optional comparison harnesses.

Reason: stable `core::simd` portable SIMD is still not the right production
baseline. For a stable high-performance Rust path, use scalar kernels first and
then target-specific `std::arch` SIMD kernels with small, reviewed `unsafe`
blocks.

### 3.2 Correctness policy

Correctness must be proven at multiple levels:

- scalar kernel reference tests;
- Rust kernel vs C++/Highway tensor tests for selected shapes;
- network intermediate tensor comparisons through backbone, heads, and decode;
- public API result buffer parity.

The public result buffer layout should match the existing API:

```text
first int: face count
each face: 16 shorts
max faces: 1024
buffer size: 0x9000 bytes
```

Floating-point differences are allowed only inside tensor-level tolerances. Any
public result-buffer mismatch must be explained before the next optimization
slice.

### 3.3 Performance policy

The Rust implementation should not rely on "Rust is fast" as an optimization
strategy. The performance route is:

1. scalar correctness;
2. allocation and workspace control;
3. packed pointwise data layout;
4. target-specific SIMD;
5. shape-specific kernels;
6. optional threading and deeper fusion.

The current Highway line shows that data layout and memory lifetime matter as
much as SIMD instructions.

## 4. Proposed Directory

```text
rust/
  Cargo.toml
  README.md

  docs/
    README.md
    rs-migration-execution-plan.md
    rs-status.md
    rs-performance-report.md

  src/
    lib.rs
    ffi.rs
    blob.rs
    filter.rs
    model.rs
    model_data.rs
    workspace.rs
    image.rs
    layers.rs
    network.rs
    postprocess.rs

    kernels/
      mod.rs
      scalar.rs
      dispatch.rs
      x86_avx2.rs
      x86_avx512.rs
      aarch64_neon.rs

  tests/
    kernels.rs
    network_parity.rs
    api_parity.rs

  benches/
    rs_benchmark.rs
    rs_image_benchmark.rs
```

Use `rs` for project-local names:

- directory prefix: `rust/`;
- module prefix where helpful: `rs_` only for C ABI/exported names;
- public experimental C API: `facedetect_rs_cnn`;
- docs prefix: `rs-`;
- optional build features: `scalar`, `x86-avx2`, `x86-avx512`, `aarch64-neon`.

## 5. Architecture

### 5.1 Core Rust API

Prefer a caller-owned detector for Rust-native use:

```rust
pub struct Detector {
    model: Model,
    workspace: Workspace,
}

impl Detector {
    pub fn new() -> Self;

    pub fn detect(
        &mut self,
        image: &image::DynamicImage,
    ) -> Result<Detection, DetectError>;

    pub fn detect_path<P: AsRef<std::path::Path>>(
        &mut self,
        path: P,
    ) -> Result<Detection, DetectError>;
}
```

This API makes workspace ownership explicit, uses the `image` crate for image
loading, and avoids making callers prepare or convert channel order. Low-level
RGB/BGR entry points may still exist for tests, benchmarks, and C ABI
compatibility.

### 5.2 C-compatible API

Keep a C ABI wrapper for compatibility:

```rust
#[no_mangle]
pub extern "C" fn facedetect_rs_cnn(
    result_buffer: *mut u8,
    bgr_image_data: *const u8,
    width: i32,
    height: i32,
    step: i32,
) -> *mut i32;
```

The wrapper may use `thread_local!` detector instances to mirror the current
Highway public API behavior.

Rules:

- validate null pointers and invalid dimensions;
- keep unsafe pointer conversion at the API boundary;
- route real work through the safe `Detector::detect_into` API;
- preserve result-buffer packing exactly.

### 5.3 Data model

Map the current Highway abstractions almost directly:

```text
HwBlob              -> Blob
BlobView            -> BlobView / BlobViewMut
HwFilter            -> Filter
HwModel             -> Model
PointwisePlan       -> PointwisePlan
HwNetworkWorkspace  -> Workspace
HwHeadOutputs       -> HeadOutputs
HwDecodedOutputs    -> DecodedOutputs
```

`Blob` should store padded channel data:

```rust
pub struct Blob {
    rows: usize,
    cols: usize,
    channels: usize,
    channel_step: usize,
    data: Vec<f32>,
}
```

Required methods:

- `resize`;
- `resize_for_overwrite`;
- `clear`;
- `ptr` / `ptr_mut` helpers for safe outer code;
- unchecked pointer helpers for SIMD kernels.

### 5.4 Kernel dispatch

Use a small explicit dispatch layer:

```text
layers/network
  -> kernels::dispatch
    -> scalar
    -> x86_avx2 when detected and built
    -> x86_avx512 later
    -> aarch64_neon later
```

On x86, use `is_x86_feature_detected!("avx2")` and
`is_x86_feature_detected!("fma")` before selecting AVX2/FMA functions.

The dispatch decision should be made once per `Detector` or `Model`, not inside
every inner loop.

## 6. Execution Phases

### Phase 0: Project skeleton

Tasks:

- add `rust/Cargo.toml`;
- add `rust/src/lib.rs`;
- add result buffer constants;
- add `Detector::detect` / `Detector::detect_path` shell and C ABI shell;
- add empty docs/status structure.

Acceptance:

- `cargo test` runs;
- no detector logic is wired yet;
- public constants match C++/Highway constants.
- the simplest documented Rust call is `detector.detect(&image)`.

### Phase 1: Scalar tensor and kernel reference

Tasks:

- implement `Blob`;
- implement scalar primitives:
  - ReLU;
  - add;
  - pointwise 1x1;
  - depthwise 3x3;
  - maxpool 2x2/s2;
- add synthetic kernel tests using deterministic data.

Acceptance:

- scalar kernels match simple reference loops;
- padding lanes remain deterministic;
- bounds checks are outside hot loops where practical.

### Phase 2: Static model import

Tasks:

- convert the 53 C++ `ConvInfoStruct` entries into Rust model data;
- decide model-data format:
  - generated Rust arrays for fastest first slice;
  - or generated binary blob plus `include_bytes!` for better compile times;
- implement `Filter::load`;
- create persistent `PointwisePlan` during model load.

Acceptance:

- Rust model reports 53 filters;
- first backbone pointwise/depthwise filters match C++ dimensions;
- tiny detection-head filters match C++ dimensions;
- weight/bias sample checks pass.

### Phase 3: Scalar network parity

Tasks:

- port image transform;
- port backbone;
- port FPN and raw heads;
- port decode/concat/sigmoid;
- port confidence filtering, sort, NMS, and result packing;
- add workspace-backed output-parameter APIs from the beginning.

Acceptance:

- tensor comparisons pass for selected intermediate stages;
- public result buffer matches original/Highway on the fixture image;
- scalar Rust is fully functional, even if slow.

### Phase 4: Benchmark harness

Tasks:

- create a raw BGR fixture for `images/cnnresult.png` so Rust benchmarks do not
  need OpenCV;
- benchmark:
  - image transform;
  - backbone;
  - FPN + raw heads;
  - network workspace;
  - decode + concat;
  - NMS;
  - public API;
- report min/avg/p50/p95.

Acceptance:

- scalar Rust has repeatable numbers;
- benchmark output can be compared with `fdt_hw_image_benchmark`;
- docs record machine, build flags, and image dimensions.

### Phase 5: Packed pointwise

Tasks:

- implement `PackedPointwiseFilter`;
- pack weights at model load;
- implement packed pointwise scalar reference;
- add strategy selector:

```text
packed    when channels >= 16 and out_channels >= 16
primitive otherwise
```

- wire packed pointwise into scalar network.

Acceptance:

- packed pointwise matches primitive pointwise within tolerance;
- public result buffer still matches;
- stage profile shows pointwise-heavy layers improve.

### Phase 6: x86 AVX2/FMA kernels

Tasks:

- implement AVX2 pointwise packed kernel;
- implement AVX2/FMA depthwise interior fast path;
- implement AVX2 maxpool complete-window fast path;
- keep scalar fallback always available;
- isolate `unsafe` code in `kernels/x86_avx2.rs`.

Acceptance:

- tests pass with AVX2 enabled;
- runtime dispatch refuses AVX2 path when features are unavailable;
- public result buffer matches baseline;
- real-image benchmark approaches the current Highway pure/hybrid range.

### Phase 7: Performance-ceiling experiments

Candidate experiments:

- 16-output/32-input `conv_head` pointwise specialization;
- 64-output packed pointwise block;
- 64-channel depthwise specialization A/B;
- maxpool boundary separation;
- FPN `upsample_x2 + add` fusion;
- direct decode/filter fusion;
- line-buffer fusion for depthwise + pointwise;
- external multi-request throughput;
- internal row-block parallelism only after single-thread plateau.

Acceptance:

- every experiment has an A/B switch or isolated branch;
- result buffer parity remains intact;
- wins are recorded with stage and hotspot data.

### Phase 8: ARM/NEON path

Tasks:

- add `aarch64_neon` kernel module;
- start with pointwise and depthwise kernels;
- validate scalar fallback on ARM first;
- compare against existing mobile behavior if available.

Acceptance:

- Rust builds on ARM64;
- public result parity holds on a small image corpus;
- benchmark separates scalar, NEON, and x86 data.

## 7. Benchmark Matrix

Every significant Rust backend change should report:

| Mode | Purpose | Required output |
| --- | --- | --- |
| scalar unit tests | correctness reference | pass count and tolerance |
| scalar real image | complete baseline | public API latency and result parity |
| packed scalar | layout effect | stage delta |
| x86 AVX2 | x86 performance path | real-image latency and stage breakdown |
| hotspot kernels | explain bottlenecks | per-kernel avg and speedup |
| external threading | throughput | images/sec and parity |
| ARM NEON | mobile portability | latency and parity |

Use the current Highway result as the comparison target:

```text
pure Highway AVX2       ~= 40.7 ms
x86 hybrid ceiling      ~= 38.1-38.9 ms
original C++ baseline   ~= 269 ms
```

## 8. Risk Register

| Risk | Impact | Mitigation |
| --- | --- | --- |
| Rust scalar path is much slower than expected | early benchmark disappointment | treat scalar as correctness baseline only |
| bounds checks stay in hot loops | AVX2 path underperforms | isolate hot kernels with pointer-based loops |
| model data generation is clumsy | slow iteration | start with generated Rust arrays, revisit binary format later |
| floating-point drift changes NMS output | false regressions | compare intermediate tensors before public API |
| too much `unsafe` spreads across codebase | maintainability loss | keep `unsafe` inside kernel modules and FFI boundary |
| stable portable SIMD remains unavailable | weak cross-platform abstraction | use `std::arch` per target plus scalar fallback |
| hidden allocation returns | performance regression | make workspace ownership explicit in APIs |
| threading hurts latency | slower single-request path | disable internal threading by default |

## 9. Immediate Next Steps

1. Add the Rust crate skeleton and constants.
2. Implement `Blob` and scalar pointwise/depthwise/maxpool tests.
3. Add a small model-data conversion script or generated Rust model data.
4. Port image transform and backbone first.
5. Compare Rust backbone tensors against the existing C++/Highway test harness.
6. Complete public API parity before writing AVX2 kernels.

This order keeps the project honest: first prove that Rust owns the same model
and produces the same tensors, then spend complexity budget on SIMD and fusion.
