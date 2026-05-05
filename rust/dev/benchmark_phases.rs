use libfacedetection_rs::blob::Blob;
use libfacedetection_rs::input::{PixelFormat, image_to_initial_blob_to};
use libfacedetection_rs::model::load_static_model;
use libfacedetection_rs::network::{
    BackboneOutputs, HeadOutputs, NetworkWorkspace, PixelInput, forward_network_from_pixels_to,
    forward_network_to,
};
use libfacedetection_rs::postprocess::{
    DEFAULT_DETECTION_OUTPUT_PARAMS, DecodedOutputs, DetectionOutputWorkspace,
    decode_and_concat_to, detection_output_to,
};
use std::env;
use std::hint::black_box;
use std::time::{Duration, Instant};

fn main() -> Result<(), Box<dyn std::error::Error>> {
    let mut args = env::args();
    let program = args.next().unwrap_or_else(|| "benchmark_phases".to_owned());
    let Some(path) = args.next() else {
        eprintln!("usage: {program} <image-path> [iterations]");
        std::process::exit(2);
    };
    let iterations = args
        .next()
        .as_deref()
        .unwrap_or("100")
        .parse::<usize>()?
        .max(1);

    let image = libfacedetection_rs::image::open(path)?.to_rgb8();
    let width = usize::try_from(image.width())?;
    let height = usize::try_from(image.height())?;
    let step = width * 3;
    let pixels = image.as_raw();

    let model = load_static_model();
    let mut input = Blob::new();
    let mut network_workspace = NetworkWorkspace::default();
    let mut backbone_outputs = BackboneOutputs::default();
    let mut head_outputs = HeadOutputs::default();
    let mut fused_network_workspace = NetworkWorkspace::default();
    let mut fused_backbone_outputs = BackboneOutputs::default();
    let mut fused_head_outputs = HeadOutputs::default();
    let mut decoded_outputs = DecodedOutputs::default();
    let mut detection_workspace = DetectionOutputWorkspace::default();

    image_to_initial_blob_to(pixels, width, height, step, PixelFormat::Rgb, &mut input);
    forward_network_to(
        &input,
        &model,
        &mut network_workspace,
        &mut backbone_outputs,
        &mut head_outputs,
    );
    forward_network_from_pixels_to(
        PixelInput {
            pixels,
            width,
            height,
            step,
            pixel_format: PixelFormat::Rgb,
        },
        &model,
        &mut fused_network_workspace,
        &mut fused_backbone_outputs,
        &mut fused_head_outputs,
    );
    decode_and_concat_to(&head_outputs, &mut decoded_outputs);
    let mut faces = detection_output_to(
        &decoded_outputs,
        DEFAULT_DETECTION_OUTPUT_PARAMS,
        &mut detection_workspace,
    )
    .len();
    black_box(faces);

    let mut input_time = Duration::ZERO;
    let mut network_time = Duration::ZERO;
    let mut fused_network_time = Duration::ZERO;
    let mut decode_time = Duration::ZERO;
    let mut output_time = Duration::ZERO;

    for _ in 0..iterations {
        input_time += measure(|| {
            image_to_initial_blob_to(
                black_box(pixels),
                width,
                height,
                step,
                PixelFormat::Rgb,
                &mut input,
            );
        });
        network_time += measure(|| {
            forward_network_to(
                black_box(&input),
                &model,
                &mut network_workspace,
                &mut backbone_outputs,
                &mut head_outputs,
            );
        });
        fused_network_time += measure(|| {
            forward_network_from_pixels_to(
                PixelInput {
                    pixels: black_box(pixels),
                    width,
                    height,
                    step,
                    pixel_format: PixelFormat::Rgb,
                },
                &model,
                &mut fused_network_workspace,
                &mut fused_backbone_outputs,
                &mut fused_head_outputs,
            );
        });
        decode_time += measure(|| {
            decode_and_concat_to(black_box(&head_outputs), &mut decoded_outputs);
        });
        output_time += measure(|| {
            faces = detection_output_to(
                black_box(&decoded_outputs),
                DEFAULT_DETECTION_OUTPUT_PARAMS,
                &mut detection_workspace,
            )
            .len();
            black_box(faces);
        });
    }

    println!("iterations: {iterations}");
    println!("faces: {faces}");
    print_phase("input_ms", input_time, iterations);
    print_phase("network_ms", network_time, iterations);
    print_phase("fused_network_ms", fused_network_time, iterations);
    print_phase("decode_ms", decode_time, iterations);
    print_phase("output_ms", output_time, iterations);
    print_phase(
        "sum_ms",
        input_time + network_time + decode_time + output_time,
        iterations,
    );
    print_phase(
        "fused_sum_ms",
        fused_network_time + decode_time + output_time,
        iterations,
    );

    Ok(())
}

fn measure(run: impl FnOnce()) -> Duration {
    let started = Instant::now();
    run();
    started.elapsed()
}

fn print_phase(name: &str, duration: Duration, iterations: usize) {
    let avg = duration.as_secs_f64() * 1000.0 / iterations as f64;
    println!("{name}: {avg:.3}");
}
