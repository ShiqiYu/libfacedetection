# facedetect_rs Cargo 发布准备计划

日期：2026-05-05  
目标：将 `libfacedetection_rs` 整理为可发布到 crates.io、可被普通 Rust 用户稳定使用、并便于后续维护和演进的正式 crate。

## 当前结论

当前 `rust/` 目录已经具备一个 Rust crate 的基本结构，但还不适合直接发布到 crates.io。最核心的阻塞项是模型数据不自包含：`build.rs` 目前从仓库根目录的 `../src/facedetectcnn-data.cpp` 读取权重数据，而 crates.io package 只会包含 `rust/` crate 目录内被打包的文件。用户通过 crates.io 安装后不会拥有仓库上一级目录，因此 package verify 会失败。

## 当前执行状态

2026-05-05 已完成第一轮发布改造：

- crate/package name 已改为 `libfacedetection_rs`；
- 已提交生成后的 Rust 模型数据：`src/generated/model_data.rs`；
- `model.rs` 已改为直接 `include!("generated/model_data.rs")`；
- `Cargo.toml` 已设置 `build = false`，当前构建不再运行 `build.rs`；
- `cargo test`、`cargo clippy --all-targets -- -D warnings`、`cargo package --allow-dirty` 已通过。

下一轮发布准备应继续处理 `Cargo.toml` 发布元信息、`LICENSE`、package `include`、public API 收窄和 README 发布化。

2026-05-05 第二轮发布改造：

- 已将根目录 `LICENSE` 复制到 `rust/LICENSE`；
- 已补齐 `Cargo.toml` 的 `rust-version`、`readme`、`repository`、`homepage`、`documentation`、`keywords`、`categories`；
- 已配置 package `include`，发布包只包含 crate 根部发布文件、`src/**` 和 examples；
- README 已从实验性描述调整为发布候选描述。
- `cargo package --allow-dirty` 已通过，当前 package 为 25 个文件，约 149.6 KiB 压缩包；
- `cargo publish --dry-run --allow-dirty --registry crates-io` 已通过。

下一轮发布准备应继续处理 public API 收窄，以及 C ABI crate-type/feature 策略。

2026-05-05 第三轮发布改造：

- `lib.rs` 已收窄默认 public API，只公开 `Detector`、`DetectError`、`image` re-export、`Detection`、`Face`、`ResultBuffer` 和 result buffer 常量；
- 内部模块 `blob`、`filter`、`input`、`kernels`、`layers`、`model`、`network`、`postprocess`、`ffi` 已改为 crate-private；
- `Cargo.toml` 已关闭自动 example 发现，只注册用户向 `benchmark` example；
- package `include` 已收窄为只发布 `examples/benchmark.rs`，内部 profiling examples 不进入 crates.io package；
- `cargo test`、`cargo clippy --all-targets -- -D warnings`、`cargo doc --no-deps`、`cargo publish --dry-run --allow-dirty --registry crates-io` 已通过。

下一轮发布准备应继续处理 C ABI crate-type/feature 策略，并决定是否将内部 profiling harness 迁移到非发布目录。

2026-05-05 第四轮发布改造：

- 首发 crate 形态已收敛为 Rust-only；
- `Cargo.toml` 的 `crate-type` 已改为只生成 `rlib`；
- `ffi` 模块已从默认 crate 编译入口移除，C ABI 不进入 0.1.0 发布承诺；
- package `include` 已显式排除未发布的 `src/ffi.rs`；
- README 已移除 C-compatible usage 小节，并把 C ABI 调整为后续可选包装工作。

下一轮发布准备可以处理剩余 polish：README 安装段、CHANGELOG、最终 package clean run，以及是否把未发布的 profiling examples 移到 `tools/`。

2026-05-05 第五轮发布改造：

- 已新增 `CHANGELOG.md` 并纳入 package `include`；
- README 已补齐安装段、Quick Start、输入/输出、平台支持、benchmark、稳定性和 license 说明；
- README 已明确 `0.1.0` 不发布 C ABI，只保留 Rust API 和 legacy result-buffer layout。

下一轮发布准备可以做最终 clean run，并决定是否在仓库层面移动或归档未发布的 profiling examples。

2026-05-05 第六轮发布改造：

- 未发布的 profiling harness 已从 `examples/` 移到 `dev/`；
- `examples/` 只保留发布给用户的 `benchmark.rs`；
- `dev/` 不在 package `include` 中，不进入 crates.io package。

下一轮发布准备可以做最终 clean run，并准备正式发布命令。

实际验证命令：

```powershell
cd rust
cargo package --allow-dirty
```

当前失败点：

```text
failed to run custom build command
read facedetectcnn-data.cpp: 系统找不到指定的路径。
```

因此发布准备工作的第一优先级不是补文档，而是让 crate 自包含、可 package verify。

## 发布目标

### 用户体验目标

发布后的用户应能通过最短路径使用：

```toml
[dependencies]
facedetect-rs = "0.1"
```

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

用户不需要：

- 手动准备 BGR/RGB buffer；
- 下载额外模型文件；
- 运行代码生成脚本；
- 知道内部 SIMD、Blob、padding、workspace 细节。

### 工程目标

- `cargo publish --dry-run` 通过；
- `cargo test --all-targets` 通过；
- `cargo clippy --all-targets -- -D warnings` 通过；
- docs.rs 能成功构建；
- README 能让新用户在一分钟内跑通；
- public API 尽量小，避免把内部优化结构变成 semver 承诺；
- 模型数据来源清晰，license 合规。

## 阶段 1：修复 package 自包含

这是发布阻塞项，必须最先处理。

### 问题

当前 `build.rs` 依赖 package 外部文件：

```text
../src/facedetectcnn-data.cpp
```

`cargo package --list` 不包含该文件，因此 crates.io 用户无法构建。

### 推荐方案 A：提交生成后的 Rust 模型数据

将 build-time C++ 解析步骤从发布 crate 中移除，改为提交生成后的 Rust 源文件：

```text
rust/src/generated/model_data.rs
```

调整：

- 新增一次性生成脚本或保留开发脚本；
- `model.rs` 改为 `include!("generated/model_data.rs")` 或普通 module；
- 发布 crate 不再需要 `build.rs` 解析 C++ 权重；
- `Cargo.toml` 可移除 `build = "build.rs"`，或者只在开发 feature 下启用。

优点：

- crates.io 和 docs.rs 最稳定；
- 构建速度更快；
- 用户构建时不依赖 C++ 源文件格式；
- package 内容直观。

缺点：

- 仓库中会出现一份较大的生成文件；
- 如果 C++ 权重更新，需要重新生成 Rust 文件。

适用判断：这是最推荐的 crates.io 发布方案。

### 备选方案 B：把 C++ 权重文件放入 crate 内

保留 `build.rs`，但将权重源文件复制到 package 内：

```text
rust/model/facedetectcnn-data.cpp
```

调整：

- `build.rs` 改读 `model/facedetectcnn-data.cpp`；
- `Cargo.toml` 的 `include` 明确包含 `model/**`；
- `cargo:rerun-if-changed=model/facedetectcnn-data.cpp`。

优点：

- 继续使用现有解析逻辑；
- C++ 权重仍是生成源。

缺点：

- 用户构建时仍要运行较重的 build script；
- docs.rs 构建也依赖 build script；
- package 内包含 C++ 数据文件但 crate 又是 pure Rust，叙事略不干净；
- 解析器和 C++ 源格式仍强耦合。

适用判断：可以快速解除发布阻塞，但长期维护不如方案 A。

### 阶段 1 验收

```powershell
cd rust
cargo package --allow-dirty
cargo publish --dry-run --allow-dirty
```

预期：

- package verify 不再报 `facedetectcnn-data.cpp` missing；
- docs build 不依赖仓库根目录；
- package list 中包含所有构建必需文件。

## 阶段 2：整理 Cargo.toml 元信息

当前 manifest 缺少发布推荐元信息。建议补齐：

```toml
[package]
name = "libfacedetection_rs"
version = "0.1.0"
edition = "2024"
rust-version = "1.85"
description = "Pure Rust face detection library based on libfacedetection's CNN model"
license = "BSD-3-Clause"
readme = "README.md"
repository = "https://github.com/<owner>/<repo>"
homepage = "https://github.com/<owner>/<repo>/tree/main/rust"
documentation = "https://docs.rs/libfacedetection_rs"
keywords = ["face", "detection", "cnn", "vision"]
categories = ["computer-vision", "multimedia::images"]
```

### crate 名称建议

当前已选择 package name：

```toml
name = "libfacedetection_rs"
```

用户代码中 crate path 是：

```rust
use libfacedetection_rs::Detector;
```

注意：crates.io 名称一旦发布就不能真正删除复用，应在首次发布前最终确认。

### include/exclude 建议

发布 crate 应避免把教学报告、开发状态文档、benchmark 输出、target 等非必要内容打包给终端用户。建议加入显式 `include`：

```toml
include = [
  "Cargo.toml",
  "README.md",
  "LICENSE",
  "src/**",
  "examples/**"
]
```

如果采用方案 B，则还要加入：

```toml
"model/**",
"build.rs"
```

如果采用方案 A 且移除 build script，则不需要 `build.rs`。

### 阶段 2 验收

```powershell
cd rust
cargo package --allow-dirty --list
```

检查：

- package 中没有 `target/`；
- package 中没有仅用于迁移过程的长文档，除非明确希望随 crate 发布；
- package 中包含 README、LICENSE、src、必要模型数据；
- Cargo 不再提示缺少 repository/homepage/documentation。

## 阶段 3：收窄 public API

当前 `lib.rs` 公开了较多内部模块，例如：

```rust
pub mod blob;
pub mod ffi;
pub mod filter;
pub mod input;
pub mod kernels;
pub mod layers;
pub mod model;
pub mod network;
pub mod postprocess;
```

这些模块一旦发布，就会成为 semver 兼容承诺。后续如果要重构 Blob 布局、kernel dispatch、Filter 打包策略、NetworkWorkspace，都可能被 public API 约束。

### 推荐 public API

首发版本建议只公开：

```rust
pub use detector::Detector;
pub use error::DetectError;
pub use image;
pub use result::{
    Detection,
    Face,
    ResultBuffer,
    FACEDETECTION_RESULT_BUFFER_SIZE,
    FACEDETECTION_RESULT_MAX_FACES,
    FACEDETECTION_RESULT_STRIDE_SHORTS,
};
```

可以考虑保留：

- `Detector::detect`
- `Detector::detect_path`
- `Detector::detect_rgb`
- `Detector::detect_bgr`
- `Detector::detect_into`

但不建议公开：

- `Blob`
- `Filter`
- `Model`
- `NetworkWorkspace`
- `HeadOutputs`
- `DecodedOutputs`
- `kernels::*`

### FFI API 策略

当前 crate 同时生成：

```toml
crate-type = ["rlib", "staticlib", "cdylib"]
```

对 crates.io 普通 Rust 用户来说，`rlib` 已足够。建议发布前二选一：

方案 A：首发只面向 Rust：

```toml
[lib]
crate-type = ["rlib"]
```

方案 B：保留 C ABI，但用 feature 控制：

```toml
[features]
default = []
capi = []

[lib]
crate-type = ["rlib", "staticlib", "cdylib"]
```

实际 Cargo 对 `crate-type` 不能直接按 feature 条件切换得很优雅，所以如果 C API 是长期目标，更干净的方式是拆分：

```text
facedetect-rs       # Rust API
facedetect-rs-capi  # C ABI wrapper
```

首发建议：先发布 Rust API crate，C ABI 等 API 稳定后再拆包或再开放。

### 阶段 3 验收

```powershell
cd rust
cargo doc --no-deps
```

检查生成文档：

- 首页只显示用户应该使用的类型；
- 内部优化模块不会出现在公开文档中；
- 示例代码能直接复制运行。

## 阶段 4：README 与文档发布化

当前 README 已经能说明基本使用，但发布前建议调整语气和内容：

### README 必备内容

- crate 简介；
- 安装方式；
- 最小使用示例；
- `Detector` 复用建议；
- 支持图片格式；
- RGB/BGR 低层接口说明；
- C result buffer 兼容性说明；
- 性能参考；
- 平台支持；
- license；
- 与原 `libfacedetection` 的关系。

### README 语气调整

当前描述包含：

```text
Pure Rust experimental implementation
SIMD kernels are still an optimization target
```

如果准备正式发布，建议改成更稳定的表述，例如：

```text
Pure Rust face detection library based on libfacedetection's CNN model.
Includes scalar fallback and AVX2 acceleration on supported x86/x86_64 CPUs.
```

如果仍想表达早期版本状态，可以放在 Stability 小节：

```text
This crate is in early 0.x development. Public API is intentionally small while performance and platform coverage continue to evolve.
```

### docs 目录处理

当前 `rust/docs` 下有迁移计划、状态记录、性能报告和 Excalidraw 图。这些对仓库教学非常有价值，但未必适合打包进 crates.io。

建议：

- 仓库保留 `rust/docs/**`；
- crates.io package 通过 `include` 排除 `docs/**`；
- README 中链接到 GitHub 上的性能报告；
- docs.rs 只展示 crate API 文档。

## 阶段 5：license 与模型来源合规

发布前必须确认：

- 根目录 `LICENSE` 是否适用于 Rust crate；
- 模型权重是否允许作为 crate 内容分发；
- README 中是否需要注明权重来源；
- 如果采用生成后的 `model_data.rs`，文件头部应写清楚来源和 license。

建议：

```text
// @generated from libfacedetection's facedetectcnn-data.cpp
// Source project license: BSD-3-Clause
```

并将 `LICENSE` 放入：

```text
rust/LICENSE
```

或者在 package include 中包含根 license 的复制版本。crates.io package 不能引用 package 外部文件，所以最终 tarball 内应有 LICENSE。

## 阶段 6：平台与 feature 策略

当前实现路线：

- x86/x86_64 上 runtime 检测 AVX2；
- 不满足 AVX2 时使用 scalar fallback；
- image 依赖默认只启用 `jpeg` 和 `png`。

建议首发 feature：

```toml
[features]
default = ["image-io"]
image-io = ["dep:image"]
```

但因为当前 public API 直接 re-export `image`，并且 `Detector::detect(&DynamicImage)` 是核心接口，所以是否拆 `image-io` 要谨慎。为了首发简单，可以暂时不拆 feature，保持：

```toml
image = { version = "0.25", default-features = false, features = ["jpeg", "png"] }
```

后续再考虑：

- `default-formats = ["jpeg", "png"]`
- `avif`
- `webp`
- `rayon`
- `capi`

首发不要引入太多 feature，否则会增加 docs.rs 和用户组合测试成本。

## 阶段 7：发布验证清单

发布前固定执行：

```powershell
cd rust
cargo fmt --check
cargo test --all-targets
cargo clippy --all-targets -- -D warnings
cargo doc --no-deps
cargo package --list
cargo publish --dry-run
```

如果保留 examples：

```powershell
cargo run --release --example benchmark -- ..\images\cnnresult.png 10
cargo run --release --example benchmark_phases -- ..\images\cnnresult.png 10
```

注意：examples 如果依赖仓库外的图片路径，不应在 examples 内硬编码该路径。当前 benchmark 接收命令行路径，这一点是合适的。

## 阶段 8：版本策略

建议首发：

```text
0.1.0
```

0.x 阶段建议遵守：

- patch version：bug fix、性能提升、不破坏 API；
- minor version：新增 API、调整 feature；
- breaking change：即使 0.x technically 允许，也尽量在 changelog 明确说明。

建议新增：

```text
rust/CHANGELOG.md
```

首发 changelog：

```markdown
# Changelog

## 0.1.0

- Initial crates.io release.
- Pure Rust CNN face detector.
- `Detector::detect` and `Detector::detect_path` safe APIs.
- RGB/BGR low-level APIs.
- C-compatible result buffer.
- Scalar fallback and AVX2 acceleration on supported x86/x86_64 CPUs.
```

## 推荐执行顺序

1. crate name 已选择：`libfacedetection_rs`。
2. 选择模型数据方案：优先生成并提交 `src/generated/model_data.rs`。
3. 移除或改造 `build.rs`，确保 package 自包含。
4. 添加 `rust/LICENSE`。
5. 补齐 `Cargo.toml` metadata 和 `include`。
6. 收窄 `lib.rs` public API。
7. 调整 README 为发布版。
8. 新增 `CHANGELOG.md`。
9. 运行完整发布验证清单。
10. 通过 `cargo publish --dry-run` 后再正式 `cargo publish`。

## 发布前 Go/No-Go 标准

可以发布的条件：

- `cargo publish --dry-run` 成功；
- package 不依赖仓库根目录；
- README 最小示例可运行；
- public API 已收窄；
- LICENSE 在 package 内；
- docs.rs 构建风险已排除；
- 当前性能数据已记录但不夸大跨机器稳定性；
- crate name 已确认不会后悔。

不建议发布的情况：

- `build.rs` 仍读取 `../src/**`；
- 内部模块仍全部 public；
- package 内缺 LICENSE；
- README 仍称主要能力为 scaffold 或 experimental optimization target；
- `cargo package --list` 中包含大量教学/迁移临时文档；
- 尚未确认模型权重分发许可。

## 后续可选增强

发布 0.1.0 后可以继续推进：

- docs.rs 上补 API examples；
- 添加 cross-platform CI：Windows、Linux、macOS；
- 添加 `aarch64` NEON 优化计划；
- 添加更标准的 benchmark corpus；
- 分离 `facedetect-rs-capi`；
- 提供 no-image low-level buffer-only feature；
- 增加 WASM 可行性研究；
- 与 C++/Highway 做 layer-by-layer parity benchmark。

这份计划的核心判断是：先把 crate 做成“用户安装后必定能构建、API 小而稳定、模型数据自包含”的发布形态，再继续扩展平台和接口。性能实现已经足够亮眼，发布前真正要补的是 packaging discipline。
