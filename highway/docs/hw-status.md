# hw status

## 2026-05-04 initial kernel loop

Implemented:

- independent `highway/CMakeLists.txt`
- `find_package(hwy 1.3.0 CONFIG ...)` integration
- automatic local variant discovery via `FDT_HW_HIGHWAY_ROOT`
- scalar reference kernels
- AVX2/FMA intrinsics comparison kernels
- Highway primitive kernels
- doctest kernel equivalence tests
- primitive benchmark harness

Validated command:

```powershell
cmake -S highway -B build-hw-rootprefix -DFDT_HW_HIGHWAY_ROOT=E:/projects/thirdParty/lib/highway-1.3.0
cmake --build build-hw-rootprefix --config Release
.\build-hw-rootprefix\Release\fdt_hw_tests.exe
```

Result:

```text
test cases:    3 |    3 passed
assertions: 5038 | 5038 passed
```

Initial benchmark on VS2022 x64 Release:

```text
shape rows=48 cols=64 channels=32 out_channels=16
scalar pointwise         avg=0.3106 ms
hw pointwise             avg=0.1664 ms
intrinsics pointwise     avg=0.1197 ms
scalar depthwise         avg=0.1028 ms
hw depthwise             avg=0.1239 ms
intrinsics depthwise     avg=0.1090 ms

shape rows=24 cols=32 channels=64 out_channels=64
scalar pointwise         avg=0.7055 ms
hw pointwise             avg=0.1939 ms
intrinsics pointwise     avg=0.1482 ms
scalar depthwise         avg=0.0295 ms
hw depthwise             avg=0.0417 ms
intrinsics depthwise     avg=0.0320 ms

shape rows=12 cols=16 channels=64 out_channels=64
scalar pointwise         avg=0.1782 ms
hw pointwise             avg=0.0491 ms
intrinsics pointwise     avg=0.0377 ms
scalar depthwise         avg=0.0071 ms
hw depthwise             avg=0.0099 ms
intrinsics depthwise     avg=0.0077 ms

shape rows=6 cols=8 channels=64 out_channels=10
scalar pointwise         avg=0.0069 ms
hw pointwise             avg=0.0023 ms
intrinsics pointwise     avg=0.0017 ms
scalar depthwise         avg=0.0016 ms
hw depthwise             avg=0.0022 ms
intrinsics depthwise     avg=0.0018 ms
```

Early read:

- Highway primitive pointwise is much faster than scalar.
- AVX2/FMA intrinsics still beat the first Highway primitive pointwise path.
- Highway primitive depthwise is currently slower than scalar and intrinsics.
- The performance-ceiling path should move beyond primitive replacement and target packed/kernel-level pointwise first.

Next slice:

1. Add packed pointwise weight layout.
2. Implement `hw_pointwise_1x1_packed`.
3. Benchmark packed pointwise against intrinsics pointwise.
4. Only then migrate the full `facedetect_hw_cnn` network flow.

## 2026-05-04 packed pointwise slice

Implemented:

- `PackedPointwiseFilter`
- `PackPointwiseFilter`
- scalar packed pointwise reference
- Highway packed pointwise kernel
- AVX2/FMA packed pointwise comparison kernel
- 4-output-block grouped accumulator path for packed kernels
- doctest coverage for packed pointwise, including tail output channels

Validated:

```text
test cases:    4 |    4 passed
assertions: 9454 | 9454 passed
```

Updated benchmark on VS2022 x64 Release:

```text
shape rows=48 cols=64 channels=32 out_channels=16
hw pointwise             avg=0.1661 ms
hw packed pointwise      avg=0.0559 ms
intrinsics pointwise     avg=0.1165 ms
intrinsics packed pw     avg=0.0612 ms

shape rows=24 cols=32 channels=64 out_channels=64
hw pointwise             avg=0.2233 ms
hw packed pointwise      avg=0.0612 ms
intrinsics pointwise     avg=0.1496 ms
intrinsics packed pw     avg=0.0619 ms

shape rows=12 cols=16 channels=64 out_channels=64
hw pointwise             avg=0.0531 ms
hw packed pointwise      avg=0.0157 ms
intrinsics pointwise     avg=0.0377 ms
intrinsics packed pw     avg=0.0155 ms

shape rows=6 cols=8 channels=64 out_channels=10
hw pointwise             avg=0.0023 ms
hw packed pointwise      avg=0.0021 ms
intrinsics pointwise     avg=0.0017 ms
intrinsics packed pw     avg=0.0021 ms
```

Read:

- Packed pointwise is the first clear performance-ceiling win.
- Highway packed pointwise is now effectively tied with AVX2/FMA packed pointwise on the large 64-output-channel shapes.
- For common 16/64-output pointwise layers, packed kernels are much faster than dot-product primitive kernels.
- For tiny head layers such as 10 output channels, primitive intrinsics can still win because padding and tail handling dominate.

Next slice:

1. Add a pointwise strategy selector:
   - packed for 16/32/64 output channels
   - primitive for tiny 1/4/10 output channels unless full-network profiling says otherwise
2. Start moving the model flow into `facedetect_hw_cnn`.
3. Add stage profiling so the full-network benchmark can show pointwise vs depthwise vs decode time.

## 2026-05-04 pointwise strategy selector

Implemented:

- `PointwiseStrategy`
- `PointwisePlan`
- `ShouldUsePackedPointwise`
- `CreatePointwisePlanHw`
- `CreatePointwisePlanIntrinsics`
- `Pointwise1x1PlannedHw`
- `Pointwise1x1PlannedIntrinsics`

Strategy:

```text
packed    when channels >= 16 and out_channels >= 16
primitive otherwise
```

This currently maps YuNet-style pointwise layers as:

```text
16/32/64-output backbone and neck layers -> packed
1/4/10-output detection heads             -> primitive
```

Validated:

```text
test cases:     5 |     5 passed
assertions: 12403 | 12403 passed
```

Representative planned benchmark:

```text
shape rows=24 cols=32 channels=64 out_channels=64
hw planned pointwise          avg=0.0613 ms
intrinsics planned pointwise  avg=0.0635 ms

shape rows=12 cols=16 channels=64 out_channels=64
hw planned pointwise          avg=0.0154 ms
intrinsics planned pointwise  avg=0.0156 ms

shape rows=6 cols=8 channels=64 out_channels=10
hw planned pointwise          avg=0.0022 ms
intrinsics planned pointwise  avg=0.0017 ms
```

Read:

- The plan seam is ready for full-network migration.
- Large pointwise layers now get the packed speedup without per-call packing.
- Tiny head layers avoid the padded packed path.
- The full-network migration should create one `PointwisePlan` per pointwise layer during model initialization.

Next slice:

1. Add `hw_blob` / `hw_filter` model data structures or adapt the existing `CDataBlob`/`ConvInfoStruct` safely.
2. Compile or import the existing static weights into the `fdt_hw` target.
3. Port the model flow up through the first few conv blocks with `PointwisePlan`.
4. Add stage profiling before completing the full network.

## 2026-05-04 model data and layer wrapper slice

Implemented:

- `HwBlob`
  - owns padded float blob storage
  - exposes `BlobView` / `ConstBlobView`
  - pads channel step to the active Highway/Intrinsics lane width
- `HwConvInfo`
  - lightweight ConvInfo-style import contract for independent hw code
- `HwFilter`
  - copies pointwise and depthwise weights into hw-owned storage
  - preserves the original filter layout:
    - pointwise: `weights[oc][ic]`
    - depthwise: `weights[kernel_index][channel]`
  - creates persistent `PointwisePlan` objects during filter load
- `hw_layers`
  - scalar, Highway, and intrinsics convolution wrappers
  - Highway depthwise-pointwise wrapper
  - Highway maxpool 2x2/s2
  - Highway element add
  - Highway upsample x2
- doctest coverage for:
  - ConvInfo-style filter import
  - persistent packed pointwise plan creation
  - depthwise layout preservation
  - layer wrappers matching scalar reference
  - maxpool/add/upsample shape and value behavior

Validated:

```text
test cases:     8 |     8 passed
assertions: 19887 | 19887 passed
```

Representative benchmark after the wrapper slice:

```text
shape rows=48 cols=64 channels=32 out_channels=16
hw packed pointwise      avg=0.0596 ms
hw planned pointwise     avg=0.0571 ms
intrinsics packed pw     avg=0.0629 ms
intrinsics planned pw    avg=0.0597 ms

shape rows=24 cols=32 channels=64 out_channels=64
hw packed pointwise      avg=0.0627 ms
hw planned pointwise     avg=0.0617 ms
intrinsics packed pw     avg=0.0627 ms
intrinsics planned pw    avg=0.0646 ms

shape rows=12 cols=16 channels=64 out_channels=64
hw packed pointwise      avg=0.0154 ms
hw planned pointwise     avg=0.0155 ms
intrinsics packed pw     avg=0.0154 ms
intrinsics planned pw    avg=0.0157 ms
```

Read:

- The packed/planned pointwise path still holds after adding model-level data structures.
- `HwFilter` is now the intended place to pay the one-time packing cost.
- `hw_layers` gives the full-network migration a small, testable API instead of wiring kernels directly.
- Depthwise is still not the Highway win path; current full-network speedup should come mostly from packed pointwise and reducing extra blob traffic.

Next slice:

1. Add a `HwModel`/`HwNetwork` container that owns the 53 filters as `HwFilter`.
2. Import `param_pConvInfo` from the existing static model data into that container.
3. Port the first backbone stages through `hw_layers` and compare stage outputs against the original implementation.
4. Add scoped stage profiling around pointwise, depthwise, maxpool, upsample/add, and decode.

## 2026-05-04 static model import slice

Implemented:

- `facedetection_export.h` shim under `highway/include` so the independent hw target can compile the existing generated model data.
- `src/facedetectcnn-data.cpp` is now compiled into `fdt_hw_kernels`.
- `HwModel`
  - owns the imported static filter table as `HwFilter`
  - loads all 53 entries from the original global `param_pConvInfo`
  - pays pointwise packed-plan creation once during model load
- doctest coverage for:
  - total imported filter count
  - first backbone pointwise filter shape and packed strategy
  - first depthwise filter shape
  - tiny detection-head filter import

Validated:

```text
test cases:     9 |     9 passed
assertions: 19898 | 19898 passed
```

Read:

- The hw project now uses the real generated model weights instead of only synthetic benchmark weights.
- The next migration step can build the real forward graph directly from `HwModel::Filter(index)`.
- Because original `param_pConvInfo` stays in its generated source file, this avoids copying the large weight table into the highway tree.

Next slice:

1. Add `HwNetworkForwardBackbone` for conv0 through conv5 using `HwModel` and `hw_layers`.
2. Add a comparison harness against the original `CDataBlob` flow for the same intermediate stages.
3. Introduce stage timers before migrating detection heads and decode.

## 2026-05-04 backbone forward slice

Implemented:

- explicit `do_relu` overloads for layer wrappers
  - matches the original `convolution(..., do_relu)` and `convolutionDP(..., do_relu)` call semantics
  - keeps detection-head migration from accidentally using `filter.with_relu`
- `HwBackboneOutputs`
  - `fb1`
  - `fb2`
  - `fb3`
- `Convolution4LayerUnitHw`
- `ForwardBackboneHw`
  - conv0
  - conv1
  - conv2
  - conv3
  - conv4
  - conv5
- original-flow comparison test
  - compiles `src/facedetectcnn.cpp` into the hw test target
  - initializes original `Filters<float>` from `param_pConvInfo`
  - runs the original `CDataBlob` backbone path
  - compares `fb1`, `fb2`, and `fb3` against the hw backbone outputs
- backbone benchmark entry
  - measures `ForwardBackboneHw` with real static weights

Validated:

```text
test cases:    10 |    10 passed
assertions: 23875 | 23875 passed
```

Backbone benchmark:

```text
backbone conv0-conv5 rows=48 cols=64 channels=32
hw backbone              avg=0.9781 ms
fb1=12x16x64 fb2=6x8x64 fb3=3x4x64
```

Read:

- The hw backbone is now numerically aligned with the original CDataBlob flow through conv5.
- The packed pointwise path survives real model wiring, not just synthetic kernels.
- The next likely bottleneck is no longer correctness of early layers, but memory traffic and allocation churn between layer wrappers.

Next slice:

1. Add FPN/branch head forward after `fb1/fb2/fb3`.
2. Add reusable scratch/workspace blobs to remove repeated allocation in `ForwardBackboneHw`.
3. Add stage-level timers to split backbone, FPN add/upsample, heads, decode, and output.

## 2026-05-04 raw heads forward slice

Implemented:

- `HwHeadOutputs`
  - `cls[3]`
  - `reg[3]`
  - `kps[3]`
  - `obj[3]`
- `ForwardHeadsHw`
  - branch5 from `fb3`
  - add5 / branch4 from `fb2`
  - add4 / branch3 from `fb1`
  - preserves original level order:
    - level 0: stride 8
    - level 1: stride 16
    - level 2: stride 32
- `ForwardNetworkHw`
  - backbone + FPN + raw heads
- original-flow comparison test for raw heads
  - runs original `CDataBlob` branch flow
  - compares all `cls/reg/kps/obj` outputs for all three levels
- raw network benchmark entry

Validated:

```text
test cases:    11 |    11 passed
assertions: 40103 | 40103 passed
```

Raw network benchmark:

```text
backbone conv0-conv5 rows=48 cols=64 channels=32
hw backbone              avg=0.9562 ms
fb1=12x16x64 fb2=6x8x64 fb3=3x4x64

raw network rows=48 cols=64 channels=32
hw raw network           avg=1.0976 ms
level0 cls=12x16x1 reg=12x16x4 kps=12x16x10 obj=12x16x1
level1 cls=6x8x1 reg=6x8x4 kps=6x8x10 obj=6x8x1
level2 cls=3x4x1 reg=3x4x4 kps=3x4x10 obj=3x4x1
```

Read:

- The hw path now matches the original convolutional network through all raw detection heads.
- FPN + heads add only about 0.14 ms over the measured backbone case, so the current high-value optimization target remains early/mid backbone allocation and layer traffic.
- Decode, sigmoid, flatten/concat, and NMS are still on the migration path.

Next slice:

1. Add `HwBlob` helpers for `meshgrid`, `bbox_decode`, `kps_decode`, `sigmoid`, `blob2vector`, and `concat3`.
2. Compare decoded/concatenated tensors against original flow before touching NMS.
3. Add a workspace API so `ForwardNetworkHw` can reuse buffers instead of allocating every layer.

## 2026-05-04 decoded tensor slice

Implemented:

- `HwDecodedOutputs`
  - concatenated `cls`
  - concatenated `reg`
  - concatenated `kps`
  - concatenated `obj`
- postprocess tensor helpers:
  - `MeshgridHw`
  - `BboxDecodeHw`
  - `KpsDecodeHw`
  - `SigmoidHw`
  - `BlobToVectorHw`
  - `Concat3Hw`
  - `DecodeAndConcatHw`
- original-flow comparison test for decoded and concatenated tensors
  - decodes original `pred_reg`
  - decodes original `pred_kps`
  - concatenates original `cls/reg/kps/obj`
  - applies sigmoid to original `cls` and `obj`
  - compares against `DecodeAndConcatHw(ForwardNetworkHw(...))`
- decoded tensor benchmark entry

Validated:

```text
test cases:    12 |    12 passed
assertions: 44147 | 44147 passed
```

Decoded tensor benchmark:

```text
decoded network rows=48 cols=64 channels=32
hw decoded tensors       avg=1.1289 ms
cls=1x1x252 reg=1x1x1008 kps=1x1x2520 obj=1x1x252
```

Read:

- The hw path now matches the original flow from converted-image-like feature input through decoded/concatenated detection tensors.
- Decode/concat/sigmoid adds very little over raw heads at this small benchmark size; convolution and allocation traffic still dominate.
- Remaining functional gap is NMS/result packing plus the image-to-initial-feature transform.

Next slice:

1. Port `detection_output` into hw types, or adapt the decoded hw tensors back into the existing function for a first end-to-end result comparison.
2. Add the initial image transform `setDataFrom3x3S2P1to1x1S1P0FromImage` equivalent for `HwBlob`.
3. Introduce workspace reuse before deep performance tuning, because current layer wrappers allocate many temporary blobs.

## 2026-05-04 image input and public API slice

Implemented:

- `ImageToInitialBlobHw`
  - mirrors `setDataFrom3x3S2P1to1x1S1P0FromImage`
  - pads image size by `padDivisor=32`
  - produces 32-channel initial feature blobs
  - preserves the original 3x3 neighborhood channel placement
- `HwFaceRect`
- `DetectionOutputHw`
  - confidence filtering
  - stable score sort
  - top-k clipping
  - NMS
  - keep-top-k clipping
- public `facedetect_hw_cnn`
  - image transform
  - static model load
  - raw network
  - decode/concat/sigmoid
  - detection output
  - result buffer packing
- doctest coverage for:
  - image transform vs original initial blob
  - image-to-decoded tensor flow vs original flow
  - deterministic confidence/sort/NMS behavior
  - public API result buffer smoke test
- benchmark entries for:
  - image-to-decoded
  - public API result buffer output

Validated:

```text
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

Representative benchmark:

```text
image-to-decoded width=96 height=96 channels=3
hw image decoded         avg=0.7628 ms
cls=1x1x189 reg=1x1x756 kps=1x1x1890 obj=1x1x189

public api width=96 height=96 channels=3
facedetect_hw_cnn        avg=0.6990 ms
faces=0
```

Read:

- The independent hw path now has a callable C API that covers the full detection pipeline.
- Functional comparison is strong through decoded tensors; detection output has deterministic unit coverage and API smoke coverage.
- The next meaningful performance work should avoid changing math and focus on allocation/workspace reuse plus a more stable benchmark harness.

Next slice:

1. Add direct result comparison against original `facedetect_cnn` on a small image set.
2. Introduce a `HwWorkspace` / in-place layer API to remove repeated temporary blob allocation.
3. Add stage profiling to quantify image transform, backbone, heads, decode, NMS, and packing separately.

## 2026-05-04 real image benchmark and FPN fusion slice

Implemented:

- optional OpenCV image benchmark target:
  - `fdt_hw_image_benchmark`
  - reads `images/cnnresult.png` by default
  - runs original `facedetect_cnn`
  - runs `facedetect_hw_cnn`
  - compares result buffer face count and packed shorts
  - prints the first few detections
  - reports original/HW timing
- OpenCV CMake discovery fallback:
  - `FDT_HW_OPENCV_ROOT`
  - variant search under `vs2022-x64-release`, `vs2022-x64-debug`, static variants
  - checks root, `lib`, and `staticlib` config locations
- real-image stage breakdown:
  - image transform
  - backbone
  - FPN + raw heads
  - decode + concat
  - NMS
- `UpsampleX2AddHw`
  - fuses `ElementAddHw(UpsampleX2Hw(x), lateral)`
  - removes a temporary upsample blob in FPN add5/add4
  - preserves exact result buffer output on `cnnresult.png`
- rvalue overload for `ForwardHeadsHw`
  - avoids copying backbone outputs in `ForwardNetworkHw`

Validated:

```text
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

Real image result:

```text
image=images\cnnresult.png size=1280x960 step=3840
original faces=39
hw faces=39
result short mismatches=0
```

Real image benchmark:

```text
original facedetect_cnn  avg=270.8535 ms
hw facedetect_hw_cnn     avg=111.5492 ms
```

Stage breakdown after FPN fusion:

```text
image transform          avg=10.7554 ms
backbone                 avg=84.0578 ms
fpn + raw heads          avg=15.5387 ms
decode + concat          avg= 2.6202 ms
nms                      avg= 0.0759 ms
stage faces=39 input=480x640x32 fb1=120x160x64 fb2=60x80x64 fb3=30x40x64
```

Read:

- The HW path is now a verified drop-in result-buffer match for `images/cnnresult.png`.
- Current speedup on that image is about 2.4x over the original implementation.
- The performance ceiling work should focus on backbone first; it dominates the runtime.
- FPN fusion is worthwhile but secondary: it reduced `fpn + raw heads` from about 17 ms to about 15.5 ms on the real image.

Next slice:

1. Add backbone stage breakdown by block (`conv0`, `conv1`, ..., `conv5`) to locate the largest remaining hotspots.
2. Add a workspace/in-place convolution path to remove repeated large `HwBlob` allocations in backbone.
3. Explore hybrid depthwise strategy or a stronger Highway depthwise kernel, because current depthwise is still slower than intrinsics in microbenchmarks.

## 2026-05-04 hybrid ceiling and block profiling slice

Implemented:

- backbone block breakdown in `fdt_hw_image_benchmark`
  - `conv_head`
  - `conv0`
  - `pool0`
  - `conv1`
  - `conv2`
  - `pool3`
  - `conv3`
  - `pool4`
  - `conv4`
  - `pool5`
  - `conv5`
- `FDT_HW_ENABLE_HYBRID_CEILING`
  - enabled by default
  - keeps Highway packed pointwise in the hw pipeline
  - routes depthwise and maxpool wrappers to the faster intrinsics kernels on x64
- intrinsics depthwise interior fast path
  - avoids per-pixel bounds checks and filter-index recomputation for non-border pixels
  - keeps original boundary behavior
- image transform interior fast path
  - avoids per-neighbor bounds checks for non-border output pixels
  - keeps zero-padded boundary behavior

Validated:

```text
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

Real image result:

```text
image=images\cnnresult.png size=1280x960 step=3840
original faces=39
hw faces=39
result short mismatches=0
```

Real image benchmark after this slice:

```text
original facedetect_cnn  avg=262.7880 ms
hw facedetect_hw_cnn     avg= 98.1276 ms
```

Stage breakdown after this slice:

```text
image transform          avg= 9.1051 ms
backbone                 avg=73.8006 ms
fpn + raw heads          avg=13.7535 ms
decode + concat          avg= 2.6074 ms
nms                      avg= 0.0763 ms
```

Backbone block profile:

```text
conv_head                avg=10.6942 ms
conv0                    avg=16.6978 ms
pool0                    avg= 1.5539 ms
conv1                    avg=10.6264 ms
conv2                    avg=21.0796 ms
pool3                    avg= 1.5595 ms
conv3                    avg= 8.3173 ms
pool4                    avg= 0.3129 ms
conv4                    avg= 2.0760 ms
pool5                    avg= 0.0399 ms
conv5                    avg= 0.5747 ms
```

Read:

- Current real-image speedup is about 2.7x over the original implementation.
- The largest remaining block is `conv2`, followed by `conv0`, then `conv_head` / `conv1`.
- The high-value next step is not postprocess; NMS is already negligible.
- The remaining ceiling likely depends on reducing pointwise/depthwise temporary traffic and improving early large-map blocks.

Next slice:

1. Split `conv0` and `conv2` into pointwise vs depthwise timings.
2. Add an output-parameter/in-place convolution API so workspace buffers can be reused without changing math.
3. Consider a specialized early-block path for 16/32-channel depthwise and pointwise shapes.

## 2026-05-04 fused depthwise and pointwise shape slice

Implemented:

- pointwise/depthwise part profiling in `fdt_hw_image_benchmark`
  - splits `conv0`, `conv1`, `conv2`, and `conv3`
  - reports pointwise and depthwise timings separately
- fused intrinsics depthwise accumulation
  - starts each vector accumulator from bias
  - keeps the 3x3 sum in registers
  - avoids clearing output and repeatedly loading/storing partial sums
- `Depthwise3x3IntrinsicsRelu`
  - fuses ReLU for depthwise layers that request it
  - leaves non-ReLU head outputs on the non-fused path
- packed pointwise 2-vector block
  - improves the 16-output-channel shape used by `conv_head`
  - keeps the existing 4-vector block for 32/64-output-channel shapes
- intrinsics maxpool complete-window fast path
  - handles full 2x2 windows without the generic boundary loop

Validated:

```text
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

Real image result:

```text
image=images\cnnresult.png size=1280x960 step=3840
original faces=39
hw faces=39
result short mismatches=0
```

Real image benchmark after this slice:

```text
original facedetect_cnn  avg=265.5792 ms
hw facedetect_hw_cnn     avg= 84.1715 ms
```

Stage breakdown after this slice:

```text
image transform          avg= 9.1552 ms
backbone                 avg=59.5073 ms
fpn + raw heads          avg=12.2297 ms
decode + concat          avg= 2.5951 ms
nms                      avg= 0.0766 ms
```

Backbone block profile:

```text
conv_head                avg= 8.6120 ms
conv0                    avg=12.3978 ms
pool0                    avg= 1.4624 ms
conv1                    avg= 8.3102 ms
conv2                    avg=18.1691 ms
pool3                    avg= 1.4776 ms
conv3                    avg= 7.5278 ms
conv4                    avg= 1.9449 ms
conv5                    avg= 0.5440 ms
```

Pointwise/depthwise read:

```text
conv0 pointwise          avg= 6.4262 ms
conv0 depthwise          avg= 5.8791 ms
conv2 pointwise0         avg= 3.6640 ms
conv2 depthwise0         avg= 2.7372 ms
conv2 pointwise1         avg= 6.1359 ms
conv2 depthwise1         avg= 5.5440 ms
```

Microbenchmark read:

```text
shape rows=48 cols=64 channels=32 out_channels=16
hw planned pointwise     avg=0.0394 ms
intrinsics planned pw    avg=0.0546 ms
intrinsics depthwise     avg=0.0455 ms
```

Read:

- Real-image speedup is now about 3.15x over the original implementation on `cnnresult.png`.
- Fused depthwise was the biggest single gain in this slice; it moved the public API from about 98-102 ms down to about 84 ms.
- Highway packed pointwise remains competitive and is faster than the intrinsics planned path for the 16-output-channel micro shape after the 2-vector block.
- Remaining runtime is mostly early large-map convolution traffic. Postprocess and NMS are no longer important optimization targets.

Next slice:

1. Add output-parameter convolution wrappers and a `HwWorkspace` so large temporary blobs can be reused.
2. Consider fusing pointwise ReLU for `conv_head`.
3. Explore a fused depthwise-pointwise block only if workspace reuse still leaves `conv0`/`conv2` as the dominant cost.

## 2026-05-04 workspace reuse and fused pointwise slice

Implemented:

- output-parameter layer APIs:
  - `ConvolutionHwTo`
  - `ConvolutionDepthwisePointwiseHwTo`
  - `MaxPooling2x2S2HwTo`
  - `UpsampleX2AddHwTo`
- `HwBlob::ResizeForOverwrite`
  - preserves vector capacity when shapes are reused
  - avoids clearing large outputs that will be fully overwritten
- backbone scratch reuse
  - reuses pointwise, block, and output scratch blobs through conv0-conv5
  - keeps `fb1/fb2/fb3` as owned outputs
- FPN/head scratch reuse
  - reuses pointwise and branch scratch blobs
  - writes detection head tensors directly into `HwHeadOutputs`
- fused Highway pointwise ReLU
  - avoids a separate ReLU pass for `conv_head`
  - keeps padding cleared after pointwise writes so small-channel depthwise heads remain safe

Validated:

```text
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

Real image result:

```text
image=images\cnnresult.png size=1280x960 step=3840
original faces=39
hw faces=39
result short mismatches=0
```

Real image benchmark after this slice:

```text
original facedetect_cnn  avg=268.7651 ms
hw facedetect_hw_cnn     avg= 64.0079 ms
```

Stage breakdown after this slice:

```text
image transform          avg=10.2807 ms
backbone                 avg=42.8646 ms
fpn + raw heads          avg=10.4243 ms
decode + concat          avg= 2.5678 ms
nms                      avg= 0.0754 ms
```

Backbone block profile:

```text
conv_head                avg= 7.9335 ms
conv0                    avg=12.7521 ms
conv1                    avg= 8.4443 ms
conv2                    avg=19.1043 ms
conv3                    avg= 7.6963 ms
conv4                    avg= 1.9647 ms
conv5                    avg= 0.5303 ms
```

Read:

- Real-image speedup is now about 4.2x over the original implementation on `cnnresult.png`.
- Workspace reuse was the largest gain in this slice, moving the public API from about 84 ms to about 64 ms.
- Fused pointwise ReLU reduced `conv_head` and avoided one large output pass.
- `decode + concat` and `nms` are now too small to drive the next optimization step.

Next slice:

1. Add a workspace-backed image transform path so the public API can avoid another large allocation.
2. Profile `conv2` with lower-noise iteration counts; it is still the largest convolution block.
3. Consider specialized 32-channel/64-channel depthwise kernels only after confirming workspace reuse has plateaued.

## 2026-05-04 input and decode allocation cleanup slice

Implemented:

- `ImageToInitialBlobHwTo`
  - writes into a caller-owned blob
  - public API now reuses a `thread_local` input blob
  - avoids the repeated 480x640x32 allocation in steady-state calls
- image transform zeroing cleanup
  - removed full-blob clear from the transform hot path
  - interior pixels write 27 BGR-neighborhood channels plus 5 zero padding channels
  - border/padded pixels still explicitly zero all 32 channels before partial fill
- 3-channel image transform fast path
  - unrolls the common BGR 3x3 gather
  - keeps the generic path for non-3-channel callers
- depthwise interior address hoisting
  - computes the 9 input neighbor pointers once per output pixel
  - avoids repeated `Ptr(...)` arithmetic for every vector lane block
- direct decode flatten
  - replaces `BlobToVectorHw(...)` temporaries plus `Concat3Hw(...)`
  - writes each three-level output directly into the final decoded blob

Validated:

```text
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

Real image result:

```text
image=images\cnnresult.png size=1280x960 step=3840
original faces=39
hw faces=39
result short mismatches=0
```

Real image benchmark after this slice:

```text
original facedetect_cnn  avg=271.6980 ms
hw facedetect_hw_cnn     avg= 56.1630 ms
```

Stage breakdown after this slice:

```text
image transform          avg= 2.0926 ms
backbone                 avg=41.7232 ms
fpn + raw heads          avg=10.3161 ms
decode + concat          avg= 2.2695 ms
nms                      avg= 0.0750 ms
```

Read:

- Real-image speedup is now about 4.8x over the original implementation on `cnnresult.png`.
- The benchmark now measures image transform with the same output-reuse behavior used by the public API.
- Input transform and decode are no longer dominant; together they are around 4.4 ms.
- The remaining ceiling is mainly convolution traffic: backbone plus FPN/heads are about 52 ms.

Next slice:

1. Add lower-noise profiling for `conv2` and `conv0` with more iterations and fewer unrelated stage measurements.
2. Explore a specialized 64-channel depthwise path for the second half of `conv2`.
3. Consider line-buffer fusion for depthwise-pointwise only if the profiling shows memory traffic, not arithmetic, is the limiting factor.

## 2026-05-04 full network workspace slice

Implemented:

- `HwNetworkWorkspace`
  - persists backbone scratch blobs
  - persists FPN/head scratch blobs
  - now also owns reusable backbone outputs for full-network calls
- output-parameter network APIs:
  - `ForwardBackboneHwTo`
  - `ForwardHeadsHwTo`
  - `ForwardNetworkHwTo`
- public API steady-state reuse:
  - `thread_local` input blob
  - `thread_local` network workspace
  - `thread_local` raw head outputs
  - `thread_local` decoded outputs
- decode pointer overload:
  - `DecodeAndConcatHwTo(HwHeadOutputs*, HwDecodedOutputs*)`
  - keeps raw head output capacity in place instead of moving it into a temporary
- hotspot benchmark:
  - adds a kernel-only section using output-parameter convolution calls
  - separates allocator/lifetime costs from kernel costs
- stage benchmark:
  - adds `network workspace` timing to match the public API network path

Validated:

```text
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

Real image result:

```text
image=images\cnnresult.png size=1280x960 step=3840
original faces=39
hw faces=39
result short mismatches=0
```

Real image benchmark after this slice:

```text
original facedetect_cnn  avg=267.4085 ms
hw facedetect_hw_cnn     avg= 41.5604 ms
```

Stage breakdown after this slice:

```text
image transform          avg= 2.3617 ms
backbone                 avg=32.5010 ms
fpn + raw heads          avg= 9.0785 ms
network workspace        avg=41.0842 ms
decode + concat          avg= 2.2358 ms
nms                      avg= 0.0737 ms
```

Hotspot kernel-only profile:

```text
conv_head pw+relu        avg= 4.5579 ms
conv0 pointwise          avg= 2.3840 ms
conv0 depthwise          avg= 2.4579 ms
conv2 pointwise0         avg= 1.5185 ms
conv2 depthwise0         avg= 1.0773 ms
conv2 pointwise1         avg= 2.9894 ms
conv2 depthwise1         avg= 2.1917 ms
```

Read:

- Real-image speedup is now about 6.4x over the original implementation on `cnnresult.png`.
- The largest remaining measured component is the workspace-backed network at about 41 ms.
- The public API is now mostly steady-state compute plus unavoidable output production; allocator churn is no longer the dominant factor.
- Kernel-only timings show that `conv_head`, `conv0`, and the second half of `conv2` remain the main targets.

Next slice:

1. Explore a specialized 64-output-channel packed pointwise path for `conv2 pointwise1`.
2. Explore a specialized 64-channel depthwise path for `conv2 depthwise1`.
3. Consider fusing post-decode confidence filtering into decode only after convolution work plateaus.

## 2026-05-04 64-channel kernel specialization slice

Implemented:

- 64-output Highway packed pointwise block
  - processes 8 SIMD vectors per output-channel block on AVX2-width targets
  - avoids running the same input-channel loop twice for 64-output layers
  - targets `conv2 pointwise1`, `conv3`, `conv4`, and 64-output FPN blocks
- 64-channel intrinsics depthwise interior path
  - unrolls the channel loop by two AVX2 vectors
  - keeps the generic boundary path
  - targets 64-channel depthwise layers in the middle/back half of the network
- hotspot benchmark refinement
  - measures kernel-only output-parameter calls for `conv_head`, `conv0`, and `conv2`
  - confirms which gains are kernel work vs. workspace lifetime

Validated:

```text
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

Microbenchmark signal:

```text
shape rows=24 cols=32 channels=64 out_channels=64
hw planned pointwise     avg=0.0469 ms
intrinsics planned pw    avg=0.0631 ms

shape rows=12 cols=16 channels=64 out_channels=64
hw planned pointwise     avg=0.0107 ms
intrinsics planned pw    avg=0.0157 ms
```

Real image result:

```text
image=images\cnnresult.png size=1280x960 step=3840
original faces=39
hw faces=39
result short mismatches=0
```

Real image benchmark after this slice:

```text
original facedetect_cnn  avg=269.7163 ms
hw facedetect_hw_cnn     avg= 38.8514 ms
```

Stage breakdown after this slice:

```text
image transform          avg= 2.4536 ms
backbone                 avg=29.9302 ms
fpn + raw heads          avg= 8.2240 ms
network workspace        avg=37.6668 ms
decode + concat          avg= 2.1938 ms
nms                      avg= 0.0955 ms
```

Hotspot kernel-only profile:

```text
conv_head pw+relu        avg= 5.0693 ms
conv0 pointwise          avg= 3.3526 ms
conv0 depthwise          avg= 2.8557 ms
conv2 pointwise0         avg= 1.5234 ms
conv2 depthwise0         avg= 1.0947 ms
conv2 pointwise1         avg= 2.4610 ms
conv2 depthwise1         avg= 2.2397 ms
```

Read:

- Real-image speedup is now about 6.9x over the original implementation on `cnnresult.png`.
- The 64-output Highway packed pointwise path is a clear win in microbenchmarks and improves the real-image network path.
- The 64-channel depthwise specialization is smaller and noisier; keep it for now, but treat it as a candidate for future A/B cleanup.
- Remaining high-value work is likely shape-specialized early pointwise/depthwise or deeper fusion, not allocator cleanup.

Next slice:

1. A/B the 64-channel depthwise specialization with a focused benchmark before making it permanent.
2. Explore a 16-output/32-input `conv_head`-specific pointwise path.
3. Consider fusing confidence filtering with decode/output packing once convolution improvements plateau.

## 2026-05-04 cross-platform backend separation slice

Goal:

- Preserve the current x86 AVX2 single-thread performance ceiling path.
- Start separating the portable Highway baseline from x86-specific intrinsics.
- Prepare the `hw` project for future ARM/NEON and threading experiments.

Documented:

- Added `highway/docs/hw-cross-platform-execution-plan.md`.
- The plan separates:
  - pure Highway backend
  - x86 hybrid AVX2 backend
  - future Highway runtime dispatch
  - external parallelism vs. internal parallelism

Implemented:

- `highway/CMakeLists.txt`
  - detects whether the current target processor is x86/x64.
  - derives `FDT_HW_ENABLE_X86_AVX2` from `FDT_HW_ENABLE_INTRINSICS_COMPARE`
    and the detected x86 target.
  - applies `/arch:AVX2`, `-mavx2`, and `-mfma` only when
    `FDT_HW_ENABLE_X86_AVX2` is enabled.
  - defines `FDT_HW_ENABLE_HYBRID_CEILING` only when the requested hybrid path
    can actually use x86 AVX2.
  - prints `FDT_HW_EFFECTIVE_BACKEND` during configure.
- `highway/src/hw_kernels_intrinsics.cpp`
  - now enables AVX2 intrinsics through the build-controlled
    `FDT_HW_ENABLE_X86_AVX2` macro instead of treating every x64 build as AVX2.

Configured existing x86 ceiling build:

```text
FDT_HW_X86_TARGET = ON
FDT_HW_EFFECTIVE_BACKEND = x86-hybrid-avx2
FDT_HW_ENABLE_INTRINSICS_COMPARE = ON
FDT_HW_ENABLE_X86_AVX2 = ON
FDT_HW_ENABLE_HYBRID_CEILING = ON
```

Validated existing x86 ceiling tests:

```text
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

Configured pure Highway build:

```powershell
cmake -S highway -B build-hw-pure -DFDT_HW_HIGHWAY_ROOT=E:/projects/thirdParty/lib/highway-1.3.0 -DFDT_HW_ENABLE_HYBRID_CEILING=OFF -DFDT_HW_ENABLE_INTRINSICS_COMPARE=OFF
```

Pure Highway configure result:

```text
FDT_HW_X86_TARGET = ON
FDT_HW_EFFECTIVE_BACKEND = pure-highway
FDT_HW_ENABLE_INTRINSICS_COMPARE = OFF
FDT_HW_ENABLE_X86_AVX2 = OFF
FDT_HW_ENABLE_HYBRID_CEILING = OFF
```

Validated pure Highway tests:

```text
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

OpenCV confirmation:

- The working OpenCV package for image benchmarks is:

```text
E:/projects/thirdParty/lib/opencv-4.5.1/vs2022-x64-release-static/staticlib
```

- The previous failed pure benchmark used the stale cached non-static
  `OpenCV_DIR=E:/projects/thirdParty/lib/opencv-4.5.1/vs2022-x64-release/lib`.
- `highway/CMakeLists.txt` now searches static OpenCV variants before dynamic
  variants.
- A clean configure using only `FDT_HW_OPENCV_ROOT` now reports:

```text
FDT_HW_OPENCV_VARIANT = vs2022-x64-release-static
```

Pure Highway image benchmark with static OpenCV:

```powershell
cmake -S highway -B build-hw-pure-static -DFDT_HW_HIGHWAY_ROOT=E:/projects/thirdParty/lib/highway-1.3.0 -DOpenCV_DIR=E:/projects/thirdParty/lib/opencv-4.5.1/vs2022-x64-release-static/staticlib -DFDT_HW_ENABLE_HYBRID_CEILING=OFF -DFDT_HW_ENABLE_INTRINSICS_COMPARE=OFF
cmake --build build-hw-pure-static --config Release --target fdt_hw_image_benchmark
.\build-hw-pure-static\Release\fdt_hw_image_benchmark.exe images\cnnresult.png
```

Result:

```text
image=images\cnnresult.png size=1280x960 step=3840
original faces=39
hw faces=39
result short mismatches=0

original facedetect_cnn  avg=273.0649 ms
hw facedetect_hw_cnn     avg= 90.3654 ms
```

Pure Highway stage breakdown:

```text
image transform          avg= 1.9724 ms
backbone                 avg=73.9600 ms
fpn + raw heads          avg=14.3793 ms
network workspace        avg=86.7055 ms
decode + concat          avg= 1.6608 ms
nms                      avg= 0.0782 ms
```

Read:

- The pure Highway backend is correct on the real image and is about 3.0x
  faster than the original implementation in this benchmark configuration.
- The x86 hybrid ceiling remains much faster than pure Highway because
  depthwise and maxpool use AVX2/FMA intrinsics in the hybrid path.

## 2026-05-04 Highway depthwise specialization slice

Goal:

- Close the gap between pure Highway and the x86 hybrid ceiling by optimizing
  the portable Highway depthwise kernel.
- Keep the x86 hybrid path unchanged.
- Preserve a no-AVX2 pure Highway correctness baseline.

Implemented:

- `Depthwise3x3Hw` no longer uses the old
  `memset -> MulAddHw -> AddInplaceHw` structure.
- Added a shared Highway implementation that:
  - initializes accumulators directly from bias
  - expands the 3x3 interior path into 9 explicit multiply-add steps
  - keeps boundary pixels on a smaller generic path
  - supports fused ReLU through `Depthwise3x3HwRelu`
  - adds a 64-channel two-vector interior path for the common model shape
  - clears padding lanes only when `channel_step > channels`
- `ConvolutionHwTo` now calls `Depthwise3x3HwRelu` in pure Highway mode when
  the filter has ReLU, avoiding a separate full-output ReLU pass.

Validated:

```text
pure-avx2 tests:
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed

pure no-AVX2 tests:
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed

x86 hybrid tests:
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

Pure Highway AVX2 real image result after this slice:

```text
image=images\cnnresult.png size=1280x960 step=3840
original faces=39
hw faces=39
result short mismatches=0

original facedetect_cnn  avg=271.8372 ms
hw facedetect_hw_cnn     avg= 43.0686 ms
```

Stage breakdown:

```text
image transform          avg= 2.1758 ms
backbone                 avg=33.2759 ms
fpn + raw heads          avg= 9.3841 ms
network workspace        avg=41.5387 ms
decode + concat          avg= 2.2233 ms
nms                      avg= 0.0881 ms
```

Hotspot kernel-only profile:

```text
conv_head pw+relu        avg= 4.5028 ms
conv0 pointwise          avg= 3.0245 ms
conv0 depthwise          avg= 2.9718 ms
conv2 pointwise0         avg= 1.5217 ms
conv2 depthwise0         avg= 1.1783 ms
conv2 pointwise1         avg= 2.4489 ms
conv2 depthwise1         avg= 2.2706 ms
```

Comparison:

```text
pure Highway, no AVX2 compile width      avg=90.3654 ms
pure Highway, AVX2 before depthwise opt  avg=66.2632 ms
pure Highway, AVX2 after depthwise opt   avg=43.0686 ms
x86 hybrid ceiling reference             avg=38.8514 ms
```

Read:

- The dedicated Highway depthwise path removes most of the pure Highway gap.
- Depthwise hotspot timings are now close to the x86 intrinsics ceiling:
  - `conv0 depthwise`: about 2.97 ms Highway vs. about 2.86 ms hybrid
  - `conv2 depthwise0`: about 1.18 ms Highway vs. about 1.09 ms hybrid
  - `conv2 depthwise1`: about 2.27 ms Highway vs. about 2.24 ms hybrid
- Remaining pure Highway gap is now mostly outside depthwise. High-value next
  candidates are Highway maxpool complete-window fast path and FPN/head
  pointwise specialization.

## 2026-05-04 Highway maxpool and two-pixel pointwise slice

Goal:

- Continue reducing the remaining pure Highway gap after depthwise optimization.
- Target low-risk kernels that still appear in the backbone profile:
  - maxpool complete-window path
  - 16-output packed pointwise on large feature maps

Implemented:

- `MaxPool2x2S2Hw`
  - adds a complete 2x2 window fast path.
  - computes `max(max(top-left, top-right), max(bottom-left, bottom-right))`
    directly.
  - processes `channel_step` for complete windows so padded lanes remain
    well-defined.
  - keeps the old bounded loop only for right/bottom edge windows.
- `Pointwise1x1PackedHwImpl`
  - adds a two-pixel path when `out_channels == 2 * lanes`.
  - loads each packed weight vector once and applies it to two adjacent input
    pixels.
  - keeps the generic packed path for all other output-channel shapes.

Validated:

```text
pure-avx2 tests:
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed

x86 hybrid tests:
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

Pure Highway AVX2 real image result after this slice:

```text
image=images\cnnresult.png size=1280x960 step=3840
original faces=39
hw faces=39
result short mismatches=0

original facedetect_cnn  avg=269.6401 ms
hw facedetect_hw_cnn     avg= 40.6871 ms
```

Pure Highway AVX2 stage breakdown:

```text
image transform          avg= 2.8098 ms
backbone                 avg=31.1384 ms
fpn + raw heads          avg= 9.1567 ms
network workspace        avg=39.4237 ms
decode + concat          avg= 2.2626 ms
nms                      avg= 0.0884 ms
```

Pure Highway AVX2 selected block timings:

```text
pool0                    avg= 1.4559 ms
pool3                    avg= 1.3347 ms
pool4                    avg= 0.4265 ms
pool5                    avg= 0.0138 ms

conv_head pw+relu        avg= 4.6369 ms
conv0 pointwise          avg= 2.2697 ms
conv2 pointwise0         avg= 1.5510 ms
conv2 pointwise1         avg= 2.3846 ms
```

Current x86 hybrid ceiling after this slice:

```text
image=images\cnnresult.png size=1280x960 step=3840
original faces=39
hw faces=39
result short mismatches=0

original facedetect_cnn  avg=265.0142 ms
hw facedetect_hw_cnn     avg= 38.1391 ms
```

Hybrid stage breakdown:

```text
image transform          avg= 2.6734 ms
backbone                 avg=30.3341 ms
fpn + raw heads          avg= 8.4051 ms
network workspace        avg=37.8457 ms
decode + concat          avg= 2.2226 ms
nms                      avg= 0.0762 ms
```

Read:

- Pure Highway AVX2 improved from about 43.07 ms after the depthwise slice to
  about 40.69 ms after maxpool and two-pixel pointwise.
- The current fastest x86 hybrid path improved from the earlier 38.85 ms
  reference to about 38.14 ms in this run.
- Most remaining time is now in the network workspace:
  - large-map pointwise/depthwise blocks
  - FPN/head pointwise
  - image transform and decode are smaller but no longer negligible.
- The two-pixel pointwise path is useful but noisier than the depthwise win.
  Any further pointwise specialization should be A/B measured carefully.
