# hw cross-platform execution plan

## 1. Goal

The current `hw` implementation has reached a useful x86 AVX2 single-thread
performance ceiling. The next engineering goal is different: keep that ceiling
path, while turning `hw` into a platform-aware implementation that can be
validated across multiple SIMD families and threading modes.

This plan separates three concerns that were intentionally mixed during the
performance-ceiling exploration:

- portable SIMD implementation through Highway;
- x86-specific ceiling kernels through AVX2/FMA intrinsics;
- single-request latency versus multi-request throughput.

The expected end state is:

```text
facedetect_hw_cnn
  |
  +-- pure Highway backend
  |     +-- scalar / SSE / AVX2 / AVX512 / NEON / SVE, selected by build
  |     +-- baseline for cross-platform correctness and maintainability
  |
  +-- x86 hybrid backend
  |     +-- Highway packed pointwise
  |     +-- AVX2/FMA depthwise and maxpool
  |     +-- best known x86 single-thread latency path
  |
  +-- optional internal threading
        +-- disabled by default
        +-- enabled only when image size and layer shape justify the overhead
```

## 2. Current State

### 2.1 SIMD backend

The `hw` codebase currently contains three kernel families:

| Family | Files | Role |
| --- | --- | --- |
| Scalar | `hw_kernels_scalar.cpp` | Correctness fallback and simple reference |
| Highway | `hw_kernels_hw.cpp` | Portable SIMD primitive and packed pointwise path |
| Intrinsics | `hw_kernels_intrinsics.cpp` | AVX2/FMA comparison and current x86 hybrid ceiling |

The fastest measured pipeline is not pure Highway. It is a hybrid:

```text
pointwise / add / relu: Highway
depthwise / maxpool:    AVX2/FMA intrinsics when hybrid ceiling is enabled
postprocess:            scalar C++ with focused memory reuse
```

This is an appropriate performance research result, but it is not yet a fully
portable production shape.

### 2.2 Multi-ISA support

The current Highway code uses `hwy::HWY_NAMESPACE`, `ScalableTag<float>`, and
`Lanes()`, so the source-level style is portable. However, the current build is
not yet a Highway multi-target fat binary:

- no `foreach_target.h`;
- no `HWY_TARGET_INCLUDE`;
- no `HWY_EXPORT`;
- no `HWY_DYNAMIC_DISPATCH`.

Therefore, today the selected SIMD width is primarily a build configuration
property, not a runtime CPU-dispatch property.

### 2.3 Threading

The public API uses `thread_local` workspaces:

```cpp
thread_local HwBlob input;
thread_local HwNetworkWorkspace network_workspace;
thread_local HwHeadOutputs head_outputs;
thread_local HwDecodedOutputs decoded;
```

This makes concurrent external calls much cleaner because each caller thread
gets independent reusable buffers. It does not mean a single inference request
is internally parallel. The current `hw` forward path is single-threaded.

## 3. Backend Policy

### 3.1 Pure Highway backend

Pure Highway is the cross-platform baseline. It should:

- avoid x86-only compiler flags;
- avoid direct calls into AVX2 intrinsics kernels;
- compile on x86, ARM, and other Highway-supported targets;
- remain correct even when vector width is 1;
- become the default backend for non-x86 targets.

This path is used to answer:

- Can the same codebase run correctly everywhere?
- How much performance do we get from portable SIMD alone?
- Which kernels still need platform-specific specialization?

### 3.2 x86 hybrid backend

The hybrid backend is the x86 latency ceiling path. It may:

- require AVX2/FMA compile options;
- use `hw_kernels_intrinsics.cpp` for depthwise and maxpool;
- keep current benchmark numbers comparable with the performance report.

This path is used to answer:

- What is the best currently known x86 single-thread performance?
- Which Highway kernels are already competitive with hand-tuned intrinsics?
- Where is platform-specific specialization still justified?

### 3.3 Future Highway runtime dispatch backend

After pure Highway and x86 hybrid are separated cleanly, introduce a true
Highway runtime-dispatch backend:

```text
public wrapper
  |
  +-- HWY_DYNAMIC_DISPATCH(Pointwise1x1Packed)
  +-- HWY_DYNAMIC_DISPATCH(Depthwise3x3)
  +-- HWY_DYNAMIC_DISPATCH(MaxPool2x2S2)
```

This should be done after the current kernels have stable signatures because
Highway multi-target compilation has strict file layout requirements.

## 4. Threading Policy

### 4.1 External parallelism

External parallelism means multiple caller threads invoke `facedetect_hw_cnn`
at the same time on different images. The current `thread_local` workspaces
already support this model, provided each caller passes a separate result
buffer.

Validation required:

- run multiple concurrent calls with independent result buffers;
- compare face count and packed result shorts against single-thread baseline;
- measure throughput scaling with 1, 2, 4, and 8 caller threads.

### 4.2 Internal parallelism

Internal parallelism means one inference request splits layer work across
threads. This is not implemented yet. It should be introduced carefully because
the model has many small tensors where thread overhead can exceed useful work.

Candidate units:

- pointwise convolution by row block;
- depthwise convolution by row block;
- image transform by row block;
- decode by output level.

Initial rule:

```text
parallelize only when rows * cols * channels exceeds a measured threshold
```

Implementation options:

- OpenMP for fast experimentation and easy parity with the original project;
- a small caller-owned thread pool for lower overhead and production control;
- no internal threading by default until latency and throughput data justify it.

### 4.3 Workspace ownership

Internal threading must not share mutable `HwBlob` write regions without clear
partitioning. The preferred approach is:

- keep model weights immutable and shared;
- partition output tensors by row range;
- keep temporary buffers caller-owned or thread-owned;
- avoid nested parallel regions.

## 5. Execution Phases

### Phase 0: Preserve the current x86 ceiling

Status: complete before this plan.

Known reference numbers on `images/cnnresult.png`:

```text
original facedetect_cnn  avg=269.7163 ms
hw facedetect_hw_cnn     avg= 38.8514 ms
```

Correctness baseline:

```text
test cases:    16 |    16 passed
assertions: 79965 | 79965 passed
```

### Phase 1: Separate backend configuration

Tasks:

- keep hybrid AVX2 kernels enabled on x86 by default;
- avoid AVX2 compiler flags on non-x86 targets;
- make pure Highway fallback explicit when AVX2 is unavailable;
- print backend and ISA decisions during CMake configure.

Acceptance:

- existing x86 benchmark path still builds;
- non-x86 configure does not receive `-mavx2`, `-mfma`, or `/arch:AVX2`;
- source code makes it clear that hybrid means x86 AVX2 ceiling.

### Phase 2: Pure Highway correctness matrix

Tasks:

- configure with hybrid disabled;
- run `fdt_hw_tests`;
- run real-image benchmark and compare output with original;
- record pure Highway performance in `hw-status.md`.

Suggested commands:

```powershell
cmake -S highway -B build-hw-pure -DFDT_HW_ENABLE_HYBRID_CEILING=OFF -DFDT_HW_ENABLE_INTRINSICS_COMPARE=OFF
cmake --build build-hw-pure --config Release
.\build-hw-pure\Release\fdt_hw_tests.exe
.\build-hw-pure\Release\fdt_hw_image_benchmark.exe images\cnnresult.png
```

### Phase 3: Highway multi-target dispatch

Tasks:

- move selected Highway kernels into a target-include implementation file;
- add `HWY_TARGET_INCLUDE`, `foreach_target.h`, and dynamic dispatch wrappers;
- start with `Pointwise1x1PackedHw`, then expand to depthwise/maxpool;
- keep scalar reference tests unchanged.

Acceptance:

- one binary can dispatch to the best available Highway target on x86;
- `HwFloatLanes()` or a new diagnostic can report selected runtime target;
- tests pass with Highway dynamic dispatch enabled.

### Phase 4: ARM/NEON validation

Tasks:

- build pure Highway backend on Android or another ARM64 toolchain;
- validate result parity on a small image corpus;
- profile pointwise, depthwise, image transform, and NMS separately;
- decide whether any NEON-specific non-Highway kernel is justified.

Acceptance:

- compile without x86 intrinsics;
- end-to-end result parity with the original NEON path;
- documented performance gap versus original mobile implementation.

### Phase 5: Threading experiments

Tasks:

- add `FDT_HW_ENABLE_THREADS` build option;
- first measure external parallelism using multiple caller threads;
- then prototype internal row-block parallelism for pointwise only;
- add thresholds to avoid slowing down small layers;
- compare latency and throughput separately.

Acceptance:

- single-thread latency does not regress when threading is disabled;
- multi-thread throughput improves on batch-like workloads;
- internal threading is only enabled where measured benefit is stable.

## 6. Benchmark Matrix

Every backend change should be reported with this matrix:

| Mode | Purpose | Required output |
| --- | --- | --- |
| scalar tests | correctness reference | doctest pass count |
| pure Highway single-thread | cross-platform baseline | real-image latency and mismatch count |
| x86 hybrid single-thread | x86 ceiling | real-image latency and stage breakdown |
| external multi-thread | throughput | images/sec and per-thread parity |
| internal multi-thread | latency experiment | p50/p95 latency and speedup |

## 7. Risks

- AVX2 compile flags can accidentally leak into ARM builds.
- Runtime dispatch can complicate debugging if wrappers are added before kernel
  signatures stabilize.
- Internal threading can regress latency on small tensors.
- `thread_local` buffers improve steady-state allocation but increase memory
  usage when many caller threads are active.
- The original project's OpenMP numbers and `hw` single-thread numbers must be
  kept separate in documentation.

## 8. Immediate Next Steps

1. Land Phase 1 build guards.
2. Run the existing x86 tests to confirm no ceiling-path regression.
3. Create a pure Highway build and record its first benchmark.
4. Decide whether the first runtime-dispatch target should be packed pointwise
   or depthwise.
