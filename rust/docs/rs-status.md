# rs status

## 2026-05-04 crate and public API scaffold

Implemented:

- `cargo init --lib --name facedetect_rs`
- safe Rust API:
  - `Detector::detect(&image::DynamicImage)`
  - `Detector::detect_path(...)`
  - low-level `detect_rgb(...)`
  - low-level `detect_bgr(...)`
  - compatibility `detect_into(...)`
- C ABI scaffold:
  - `facedetect_rs_cnn(...)`
- result-buffer constants matching the C++/Highway API:
  - `FACEDETECTION_RESULT_BUFFER_SIZE = 0x9000`
  - `FACEDETECTION_RESULT_MAX_FACES = 1024`
  - `FACEDETECTION_RESULT_STRIDE_SHORTS = 16`
- `image` crate integration for PNG/JPEG loading.

Current behavior:

- validates inputs;
- writes a valid empty result buffer;
- does not run the CNN model yet.

Validated:

```text
cargo test
7 unit tests passed
1 doctest passed
```

## 2026-05-04 Blob and scalar kernel foundation

Goal:

- Establish the tensor and kernel API shape before model migration.
- Keep the first implementation scalar but performance-oriented.
- Avoid future rewrites by using padded storage and output-parameter kernels now.

Implemented:

- `Blob`
  - HWC `f32` storage;
  - padded `channel_step`;
  - default channel padding to 8 floats for an AVX2-friendly baseline;
  - `resize` for cleared allocation;
  - `resize_for_overwrite` for workspace reuse;
  - immutable and mutable view types.
- scalar kernels under `kernels::scalar`
  - `relu_in_place`;
  - `add_to`;
  - `pointwise_1x1_to`;
  - `depthwise_3x3_to`;
  - `max_pool_2x2_s2_to`;
  - `max_pool_output_size`.

Performance-shape decisions:

- Kernels write into caller-owned `BlobViewMut` outputs.
- Pointwise/depthwise/maxpool kernels do not allocate.
- Padding lanes are cleared by writers so future SIMD reads remain deterministic.
- `resize_for_overwrite` intentionally keeps existing values when shape/capacity
  allow reuse.
- The `image` crate stays at the API/decode edge; hot kernels operate on views.

Validated:

```text
cargo test
14 unit tests passed
1 doctest passed
```

Read:

- The Rust line now has the basic tensor/kernel foundation needed for Phase 2
  model import.
- The next implementation slice should import static filters and create a
  `Filter`/`Model` layer that can feed these kernels without changing their hot
  path signatures.

## 2026-05-04 Filter, Model, and packed pointwise foundation

Goal:

- Move pointwise packing to model/filter load time.
- Keep runtime convolution dispatch small and shape-aware.
- Prepare for the 53-filter static model import without changing kernel
  signatures.

Implemented:

- `FilterKind`
  - `Pointwise`
  - `Depthwise`
- `ConvInfo`
  - Rust-side equivalent of the C++ `ConvInfoStruct` import contract.
- `Filter`
  - owns copied padded weights;
  - owns compact biases;
  - records `with_relu`;
  - creates a persistent `PointwisePlan` for pointwise filters.
- `PointwisePlan`
  - `Primitive` for tiny heads and small-output layers;
  - `Packed` when `channels >= 16 && out_channels >= 16`.
- `PackedPointwiseFilter`
  - stores weights as `[input_channel][padded_output_channel]`;
  - stores padded biases;
  - defaults to 8-lane packing, matching the current AVX2-oriented baseline.
- `Model`
  - owns loaded filters;
  - exposes `filter(index)`;
  - reserves around the known 53-filter model shape.
- `layers`
  - `convolution_to`;
  - `convolution`;
  - `depthwise_pointwise_to`.

Performance-shape decisions:

- Packing is paid once during `Filter::load`.
- Runtime pointwise convolution dispatches through the model-owned plan.
- Large pointwise layers use packed scalar now and can later route to AVX2 using
  the same `PackedPointwiseFilter`.
- Tiny 1/4/10-output heads keep primitive pointwise to avoid padding overhead.
- Layer wrappers write into caller-owned `Blob` outputs and reuse pointwise
  workspace for depthwise-pointwise blocks.

Validated:

```text
cargo test
21 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Read:

- The Rust implementation now has the same key performance seam that made the
  Highway path fast: persistent packed pointwise plans plus output-parameter
  layer APIs.
- The next high-value slice is static model-data generation/import from
  `src/facedetectcnn-data.cpp`, followed by scalar backbone parity.

## 2026-05-04 static model data import

Goal:

- Use the existing generated C++ weights as the single source of truth.
- Avoid committing a second huge hand-maintained Rust weight file.
- Load all 53 filters into the Rust `Model` with persistent pointwise plans.

Implemented:

- `build.rs`
  - reads `../src/facedetectcnn-data.cpp`;
  - parses all `float name[...] = {...};` arrays;
  - parses `param_pConvInfo[53]`;
  - validates every referenced weight and bias array length;
  - generates `OUT_DIR/model_data.rs`.
- `model::load_static_model`
  - generated at build time;
  - loads all filters through `Filter::load`;
  - preserves pointwise/depthwise kind and `with_relu`.
- `Detector`
  - now owns the generated static `Model` plus workspace from construction.

Performance-shape decisions:

- Weight parsing happens at build time, never at runtime.
- Runtime model construction copies weights once into padded `Blob` storage.
- Pointwise packing is still paid once at `Filter::load`.
- Generated data is lint-isolated for numeric constants while hand-written code
  remains under strict clippy.

Validated:

```text
cargo test
23 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Read:

- Rust now owns the real static model data and can construct the same 53-filter
  model used by C++/Highway.
- The next slice should implement image-to-initial-blob and the scalar backbone
  forward path through `conv0` to `conv5`, then compare intermediate tensors
  against the existing C++/Highway tests.

## 2026-05-04 image transform and scalar backbone slice

Goal:

- Start running the real Rust forward graph instead of only loading weights.
- Preserve the user-facing `Detector::detect` API while moving internals toward
  the final pipeline.
- Keep allocations controlled through detector-owned workspace.

Implemented:

- `input`
  - `PixelFormat`
  - `image_to_initial_blob`
  - `image_to_initial_blob_to`
  - 3x3/S2/P1 image gather into the 32-channel initial tensor.
- `layers`
  - `max_pool_2x2_s2_to`.
- `network`
  - `BackboneOutputs`
  - `NetworkWorkspace`
  - `forward_backbone`
  - `forward_backbone_to`
  - scalar backbone through filters `0..=22`.
- `Detector`
  - now owns `input`, `NetworkWorkspace`, and `BackboneOutputs`;
  - `detect_rgb` / `detect_bgr` transform the caller image into the initial
    blob;
  - the scaffold path now runs the real scalar backbone before writing the
    current empty public result.

Performance-shape decisions:

- Image transform writes into detector-owned `Blob` storage.
- Backbone writes into reusable workspace blobs.
- The scalar backbone uses model-owned packed pointwise plans for large
  pointwise layers.
- RGB/BGR support is represented by `PixelFormat`; no eager channel-swap copy is
  performed by the public `image` path.

Validated:

```text
cargo test
28 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Backbone smoke signal:

```text
input image: 96x96
initial blob: 48x48x32
fb1: 12x12x64
fb2:  6x 6x64
fb3:  3x 3x64
```

Read:

- The Rust detector now exercises real model weights and real convolutional
  backbone code on every safe API call.
- The public output is still a scaffold with zero faces.
- The next slice should add FPN/raw heads (`ForwardHeads`) and then decoded
  tensor helpers before result-buffer parity work.

## 2026-05-04 FPN and raw head forward slice

Goal:

- Continue the real forward path from backbone outputs into FPN and detection
  heads.
- Preserve workspace reuse and fused FPN add behavior from the Highway path.
- Keep decode/NMS/result packing out of this slice.

Implemented:

- `layers::upsample_x2_add_to`
  - fuses nearest-neighbor x2 upsample and lateral add;
  - avoids an intermediate upsample blob.
- `HeadOutputs`
  - `cls[3]`
  - `reg[3]`
  - `kps[3]`
  - `obj[3]`
- `NetworkWorkspace`
  - adds reusable `head_pointwise`;
  - adds reusable `head_branch`.
- `forward_heads_to`
  - branch5 from `fb3`;
  - add5 / branch4 from `fb2`;
  - add4 / branch3 from `fb1`;
  - preserves output level order:
    - level 0: stride 8;
    - level 1: stride 16;
    - level 2: stride 32.
- `forward_network_to`
  - backbone + raw heads.
- `Detector`
  - safe API calls now run image transform, backbone, FPN, and raw heads before
    writing the current scaffold result buffer.

Performance-shape decisions:

- FPN uses fused `upsample_x2_add_to`.
- Raw heads write into detector-owned `HeadOutputs`.
- Reusable `head_pointwise` and `head_branch` avoid per-head temporary
  allocations.
- Large FPN pointwise layers still use persistent packed pointwise plans.

Validated:

```text
cargo test
30 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Raw network smoke signal on 96x96 input:

```text
level 0: cls=12x12x1 reg=12x12x4 kps=12x12x10 obj=12x12x1
level 1: cls= 6x 6x1 reg= 6x 6x4 kps= 6x 6x10 obj= 6x 6x1
level 2: cls= 3x 3x1 reg= 3x 3x4 kps= 3x 3x10 obj= 3x 3x1
```

Read:

- Rust now runs the real static model from image tensor through all raw
  detection heads.
- The public result buffer remains a valid empty scaffold.
- The next slice should implement decoded tensor helpers:
  - meshgrid;
  - bbox decode;
  - keypoint decode;
  - sigmoid;
  - direct flatten/concat into decoded outputs.

## 2026-05-04 decode, NMS, and result packing slice

Goal:

- Replace the empty public result scaffold with the real C++ postprocess shape.
- Keep decode and detection output buffers reusable inside `Detector`.
- Preserve the simple Rust API while still writing the C-compatible result
  buffer.

Implemented:

- `postprocess`
  - `DecodedOutputs`;
  - meshgrid generation for stride 8/16/32 heads;
  - bbox decode;
  - keypoint decode;
  - sigmoid for cls/obj;
  - direct flatten/concat into vector blobs;
  - `detection_output_to` with `sqrt(cls * obj)`, confidence filtering, stable
    descending score sort, top-k, NMS, and keep-top-k.
- `DetectionOutputWorkspace`
  - reusable candidate and selected-face vectors.
- `result`
  - structured `Face`;
  - `Detection::faces()`;
  - C-compatible result-buffer packing:
    - `int32` face count;
    - 16-short records;
    - score scaled by 100;
    - bbox and 5 landmarks.
- `Detector`
  - safe API calls now run image transform, full scalar network, decode,
    detection output, and result packing.

Performance-shape decisions:

- Decode scratch blobs live inside `DecodedOutputs`; no per-call decode scratch
  blobs are allocated.
- Detection output reuses candidate/selected vectors owned by the detector
  workspace.
- NMS iterates the sorted candidate list directly instead of erasing the front
  of a vector.
- The public `image` path still avoids a separate RGB/BGR channel-swap copy.

Validated:

```text
cargo test
35 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Read:

- Rust now has an end-to-end scalar detector path from `image::DynamicImage` to
  structured `Face` values and the legacy result buffer.
- The next slice should add parity/performance harnesses against the C++ and
  Highway implementations, then use those numbers to prioritize SIMD kernels.

## 2026-05-04 hot-path reuse and local benchmark slice

Goal:

- Remove remaining avoidable allocations/copies in the scalar detector path.
- Add a no-third-party way to gather repeatable Rust-only timing numbers.

Implemented:

- `Detector::detect`
  - borrows `DynamicImage::ImageRgb8` storage directly via `as_rgb8`;
  - only falls back to `to_rgb8()` when the source image is not already RGB8.
- `postprocess`
  - adds `meshgrid_to`;
  - stores stride 8/16/32 prior blobs in `DecodedOutputs`;
  - reuses prior storage across detector calls.
- `examples/benchmark.rs`
  - loads one image through the re-exported `image` crate;
  - reuses one `Detector`;
  - performs one warmup call;
  - reports total and average milliseconds over N iterations.

Performance-shape decisions:

- Existing RGB8 callers avoid an extra image-buffer allocation/copy on the safe
  API path.
- Prior meshgrids resize in detector-owned workspace instead of allocating new
  blobs during every decode.
- Benchmarking uses only `std::time::Instant` and `std::hint::black_box`, so it
  does not add Criterion or other dev dependencies.

Validated:

```text
cargo test
35 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Run:

```powershell
cargo run --release --example benchmark -- path\to\face.jpg 100
```

Read:

- The scalar pipeline is now ready for baseline timing with real images.
- The next slice should add either C++/Highway parity fixtures or the first
  targeted SIMD kernel based on benchmark hotspots.

## 2026-05-04 packed pointwise scalar loop reorder

Goal:

- Use `images\cnnresult.png` as the first real release benchmark image.
- Improve the dominant packed pointwise scalar loop without changing model data
  layout or public APIs.

Benchmark command:

```powershell
cargo run --release --example benchmark -- ..\images\cnnresult.png 50
```

Baseline:

```text
faces: 45
avg_ms: 397.116
```

Implemented:

- `kernels::scalar::pointwise_1x1_packed_to`
  - initializes the output pixel by copying packed biases once;
  - iterates input channels outside output channels;
  - streams one contiguous packed output-channel weight row per input channel;
  - writes padded output lanes deterministically from padded zero biases and
    weights.

Performance result:

```text
faces: 45
avg_ms: 159.915
speedup: 2.48x vs baseline
```

Negative result recorded:

- Manual 8-lane scalar unrolling with `chunks_exact_mut(8)` regressed to about
  `347.541 ms` on the same image and was reverted.
- The compiler currently produces better release code from the compact indexed
  inner loop.

Validated:

```text
cargo test
35 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Read:

- Packed pointwise order is now the strongest scalar baseline so far.
- The next high-value step is a timing breakdown or direct AVX2 implementation
  for this same packed pointwise data layout.

## 2026-05-04 AVX2 pointwise/depthwise kernel slice

Goal:

- Push the current pure Rust implementation past scalar limits while keeping
  runtime portability.
- Keep x86 SIMD behind runtime CPU feature detection.
- Add enough phase timing to choose the next optimization target with data.

Implemented:

- `kernels::pointwise_1x1_packed_to`
  - dispatches to AVX2 on x86/x86_64 when available;
  - falls back to the scalar packed pointwise loop elsewhere.
- `kernels::depthwise_3x3_to`
  - dispatches to AVX2 for 8-channel-multiple depthwise tensors;
  - falls back to scalar for other shapes or non-AVX2 CPUs.
- `kernels::x86`
  - contains AVX2 intrinsics using `std::arch`;
  - keeps unsafe code local and guarded by shape assertions plus runtime feature
    checks.
- `examples/benchmark_phases.rs`
  - reports average input transform, network, decode, output, and sum timings.

Performance results on `images\cnnresult.png`:

```text
cargo run --release --example benchmark -- ..\images\cnnresult.png 50
faces: 45
avg_ms: 72.482
```

Phase timing:

```text
cargo run --release --example benchmark_phases -- ..\images\cnnresult.png 50
faces: 45
input_ms:   2.676
network_ms: 69.376
decode_ms:  0.744
output_ms:  0.039
sum_ms:    72.835
```

Comparison:

```text
initial scalar packed pointwise baseline: 397.116 ms
scalar reordered packed pointwise:        159.915 ms
AVX2 packed pointwise:                    115.892 ms
AVX2 pointwise + AVX2 depthwise:           72.482 ms
overall speedup from initial baseline:      5.48x
```

Negative result recorded:

- AVX2+FMA pointwise dispatch was tested and regressed to about `120.326 ms`
  on the same image, compared with about `115.892 ms` for AVX2 mul+add, so the
  FMA path was reverted.

Validated:

```text
cargo test
35 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Read:

- The remaining time is overwhelmingly in `network_ms`.
- `input_ms` is now a small but visible fixed cost; decode and output are not
  current priorities.
- The next serious performance work should either:
  - add a finer network-layer timing breakdown;
  - specialize the first image transform/conv path;
  - or compare against C++/Highway on the same image to set a concrete target.

## 2026-05-04 network group timing and small-kernel slice

Goal:

- Break down the remaining `network_ms` cost into actionable groups.
- Optimize the large feature-map head outputs and pooling hotspots without
  changing API behavior.

Implemented:

- `examples/benchmark_network_groups.rs`
  - reports average timings for backbone pools/units and FPN/head groups.
- `kernels::scalar::pointwise_1x1_to`
  - adds a stack-buffer small-output path for `<= 10` output channels;
  - improves cls/reg/kps/obj head pointwise layers that are intentionally not
    packed.
- `kernels::max_pool_2x2_s2_to`
  - dispatches to an AVX2 maxpool kernel for 8-channel-multiple tensors;
  - falls back to scalar elsewhere.
- `kernels::x86`
  - adds `max_pool_2x2_s2_avx2_to`.

Network group signal before the small-output and maxpool work:

```text
unit_7_10:   12.468 ms
head_out_l3: 11.045 ms
pool1:        4.219 ms
pool2:        3.680 ms
```

After:

```text
head_out_l3: about 8.8-9.0 ms
pool1:       about 0.8 ms
pool2:       about 0.8 ms
```

Current clean hot-start benchmark on `images\cnnresult.png`:

```text
cargo run --release --example benchmark -- ..\images\cnnresult.png 50
faces: 45
avg_ms: 60.605
```

Phase timing:

```text
cargo run --release --example benchmark_phases -- ..\images\cnnresult.png 50
faces: 45
input_ms:   2.607
network_ms: 57.460
decode_ms:  0.694
output_ms:  0.038
sum_ms:    60.799
```

Comparison:

```text
initial scalar packed pointwise baseline: 397.116 ms
AVX2 pointwise + AVX2 depthwise:           72.482 ms
small heads + AVX2 maxpool:                60.605 ms
overall speedup from initial baseline:      6.55x
```

Negative result recorded:

- AVX2 ReLU was tested. It did not produce a stable improvement on this image
  and was reverted.

Validated:

```text
cargo test
35 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Read:

- The remaining largest groups are still `unit_7_10`, early `dp_1_2/conv0`,
  and `head_out_l3`.
- The next slice should focus on either:
  - first-layer/image-transform fusion;
  - head output fusion for cls/reg/kps/obj;
  - or C++/Highway parity benchmarking to calibrate the target ceiling.

## 2026-05-04 packed pointwise threshold and multi-accumulator AVX2 slice

Goal:

- Push the pointwise-heavy network groups further by reducing repeated input
  broadcasts inside the AVX2 kernel.
- Move small head pointwise layers to the packed path when the measured result
  supports it.

Implemented:

- `filter::should_use_packed_pointwise`
  - now packs all pointwise layers with `channels >= 16`;
  - 1/4/10-output heads now use model-load packing and AVX2 dispatch.
- `kernels::x86::pointwise_1x1_packed_avx2_to`
  - adds 16/32/64-output multi-accumulator paths;
  - broadcasts each input channel once per pixel and updates multiple output
    vectors;
  - keeps a dynamic fallback for other padded output widths.

Performance results on `images\cnnresult.png`:

```text
cargo run --release --example benchmark -- ..\images\cnnresult.png 50
faces: 45
avg_ms: 47.314
```

Phase timing:

```text
cargo run --release --example benchmark_phases -- ..\images\cnnresult.png 50
faces: 45
input_ms:   2.676
network_ms: 43.987
decode_ms:  0.668
output_ms:  0.038
sum_ms:    47.370
```

Group timing signal:

```text
head_out_l3: about 4.8 ms
unit_7_10:   about 13.0 ms
dp_1_2:      about 9.2 ms
conv0:       about 6.5 ms
```

Comparison:

```text
initial scalar packed pointwise baseline: 397.116 ms
small heads + AVX2 maxpool:                60.605 ms
multi-accumulator AVX2 + all packed heads: 47.314 ms
overall speedup from initial baseline:      8.39x
```

Validated:

```text
cargo test
38 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Read:

- The head output path is no longer the dominant concern.
- The remaining high-value areas are first-layer/input fusion and the
  pointwise/depthwise blocks in `unit_7_10` and `dp_1_2`.

## 2026-05-04 first-layer fusion experiment and result packing cleanup

Goal:

- Test whether fusing image transform and `conv0` beats the existing
  `initial blob + AVX2 conv0` path.
- Remove avoidable public result-buffer work.

Implemented:

- `input::image_to_initial_conv_to`
  - computes the first pointwise convolution directly from the 3x3/S2 image
    window;
  - skips materializing the 32-channel initial blob;
  - has a parity test against `image_to_initial_blob_to + convolution_to`.
- `result::write_faces_to_result_buffer`
  - no longer clears the full `0x9000` buffer before writing the count and face
    records;
  - matches the C++ style more closely, where stale records beyond `face_count`
    are ignored.

Negative result recorded:

- Wiring the scalar fused initial conv into `Detector` regressed the public
  benchmark to about `58.476 ms`.
- The current separated path is faster because `conv0` benefits from the AVX2
  packed pointwise kernel.
- The fused path remains useful as a correctness reference for a future AVX2
  fused implementation, but it is not used by the production detector path.

Current clean hot-start benchmark on `images\cnnresult.png`:

```text
cargo run --release --example benchmark -- ..\images\cnnresult.png 50
faces: 45
avg_ms: 48.002
```

Phase timing:

```text
cargo run --release --example benchmark_phases -- ..\images\cnnresult.png 50
faces: 45
input_ms:   2.656
network_ms: 44.193
decode_ms:  0.718
output_ms:  0.039
sum_ms:    47.606
```

Validated:

```text
cargo test
39 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Read:

- Safe `Detector::detect` is now close to the phase sum; the remaining gap is
  mostly owned `Detection` construction and normal benchmark noise.
- The next fusion attempt should be AVX2 from the beginning, not scalar.

## 2026-05-04 AVX2 fused conv0 slice

Goal:

- Turn the failed scalar `image + conv0` fusion into a real AVX2 fused entry.
- Keep the scalar fused implementation as a correctness fallback/reference.

Implemented:

- `kernels::image_to_initial_conv_3x3_s2_to`
  - dispatches the fused initial convolution to AVX2 when conv0 has the fixed
    `32 -> 16` packed shape;
  - falls back to scalar fused behavior otherwise.
- `kernels::x86::image_to_initial_conv_3x3_s2_avx2_to`
  - directly accumulates conv0 from each 3x3/S2 image window;
  - uses two 8-lane accumulators for the 16 conv0 outputs;
  - applies ReLU before writing the conv0 output blob;
  - adds a separate interior fast path so most pixels skip per-sample boundary
    checks.
- `network::forward_network_from_pixels_to`
  - production detector path now enters the network through the fused conv0
    path.
- `examples/benchmark_phases.rs`
  - now reports both separated `input_ms + network_ms` and production
    `fused_network_ms`.

Current clean hot-start benchmark on `images\cnnresult.png`:

```text
cargo run --release --example benchmark -- ..\images\cnnresult.png 50
faces: 45
avg_ms: 46.707
```

Phase timing:

```text
cargo run --release --example benchmark_phases -- ..\images\cnnresult.png 50
faces: 45
input_ms:          2.605
network_ms:       44.886
fused_network_ms: 44.981
decode_ms:         0.707
output_ms:         0.039
sum_ms:           48.237
fused_sum_ms:     45.727
```

Comparison:

```text
initial scalar packed pointwise baseline: 397.116 ms
multi-accumulator AVX2 + all packed heads: 47.314 ms
AVX2 fused conv0 production path:          46.707 ms
overall speedup from initial baseline:      8.50x
```

Validated:

```text
cargo test
39 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Read:

- AVX2 fused conv0 is now worth keeping, but it is a small win rather than a
  breakthrough.
- Remaining cost is dominated by the backbone blocks, especially `unit_7_10`
  and early `dp_1_2`.

## 2026-05-04 depthwise ReLU fusion slice

Goal:

- Remove the separate ReLU pass after depthwise convolutions.
- Keep the optimization local to depthwise layers with `do_relu`.

Implemented:

- `kernels::depthwise_3x3_relu_to`
  - dispatches to AVX2 when available;
  - falls back to scalar fused depthwise+ReLU otherwise.
- `kernels::x86::depthwise_3x3_relu_avx2_to`
  - reuses the AVX2 depthwise implementation shape;
  - applies `max(acc, 0)` before each vector store.
- `kernels::scalar::depthwise_3x3_relu_to`
  - fuses scalar ReLU into the depthwise pixel write path.
- `layers::convolution_to`
  - sends depthwise layers with `do_relu` directly to the fused kernel and
    skips the later full-buffer ReLU pass.

Current clean hot-start benchmark on `images\cnnresult.png`:

```text
cargo run --release --example benchmark -- ..\images\cnnresult.png 50
faces: 45
avg_ms: 42.111
```

Phase timing:

```text
cargo run --release --example benchmark_phases -- ..\images\cnnresult.png 50
faces: 45
input_ms:          2.578
network_ms:       41.181
fused_network_ms: 41.193
decode_ms:         0.711
output_ms:         0.039
sum_ms:           44.508
fused_sum_ms:     41.943
```

Group timing:

```text
conv0:       5.265
dp_1_2:      6.499
unit_7_10:   9.972
fb1_11_14:   4.305
head_out_l3: 4.812
```

Comparison:

```text
initial scalar packed pointwise baseline: 397.116 ms
AVX2 fused conv0 production path:          46.707 ms
depthwise ReLU fusion:                     42.111 ms
overall speedup from initial baseline:      9.43x
```

Validated:

```text
cargo test
39 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Read:

- Fusing depthwise ReLU is now one of the highest-value changes after the
  pointwise AVX2 work.
- Remaining hotspots are mostly unavoidable backbone compute unless we add more
  aggressive block fusion or parallelism.

## 2026-05-04 final depthwise interior fast path slice

Goal:

- Make one final low-risk attempt at the remaining backbone hotspots.
- Avoid changing public APIs or model layout.

Implemented:

- `kernels::x86::depthwise_3x3_avx2_impl`
  - adds an interior fast path for non-border pixels;
  - manually expands the 9 depthwise samples for interior pixels;
  - keeps the old boundary-aware generic path for border pixels.
- `add_depthwise_3x3_sample`
  - small AVX2 helper for the repeated load/mul/add pattern.

Current clean hot-start benchmark on `images\cnnresult.png`:

```text
cargo run --release --example benchmark -- ..\images\cnnresult.png 50
faces: 45
avg_ms: 38.136
```

Phase timing:

```text
cargo run --release --example benchmark_phases -- ..\images\cnnresult.png 50
faces: 45
input_ms:          2.617
network_ms:       37.586
fused_network_ms: 37.843
decode_ms:         0.720
output_ms:         0.040
sum_ms:           40.964
fused_sum_ms:     38.603
```

Comparison:

```text
initial scalar packed pointwise baseline: 397.116 ms
depthwise ReLU fusion:                     42.111 ms
depthwise interior fast path:              38.136 ms
overall speedup from initial baseline:     10.41x
```

Validated:

```text
cargo test
39 unit tests passed
1 doctest passed

cargo clippy --all-targets -- -D warnings
clean
```

Read:

- This is a strong final pure Rust AVX2 baseline.
- Further gains likely require larger architectural moves:
  - fusing full depthwise-pointwise blocks;
  - multithreading large feature maps;
  - or comparing against Highway/C++ layer by layer to choose the next target.
