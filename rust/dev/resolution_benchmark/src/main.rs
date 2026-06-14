use libfacedetection_rs::image::imageops::FilterType;
use libfacedetection_rs::{Detector, image};
use std::env;
use std::hint::black_box;
use std::sync::{Arc, Barrier};
use std::thread;
use std::time::Instant;

#[derive(Clone, Copy)]
struct SizeCase {
    width: u32,
    height: u32,
}

struct Stats {
    ms: f64,
    fps: f64,
    faces: usize,
}

fn main() -> Result<(), Box<dyn std::error::Error>> {
    let mut args = env::args();
    let _program = args
        .next()
        .unwrap_or_else(|| "resolution_benchmark".to_owned());
    let image_path = args
        .next()
        .unwrap_or_else(|| "../../images/cnnresult.png".to_owned());
    let iterations = args
        .next()
        .as_deref()
        .unwrap_or("128")
        .parse::<usize>()?
        .max(1);
    let thread_count = args
        .next()
        .map(|value| value.parse::<usize>())
        .transpose()?
        .filter(|value| *value > 0)
        .unwrap_or_else(|| {
            thread::available_parallelism()
                .map(usize::from)
                .unwrap_or(1)
        });

    let source = image::open(&image_path)?;
    let cases = [
        SizeCase {
            width: 640,
            height: 480,
        },
        SizeCase {
            width: 320,
            height: 240,
        },
        SizeCase {
            width: 160,
            height: 120,
        },
        SizeCase {
            width: 128,
            height: 96,
        },
    ];

    println!("image={image_path} iterations={iterations} external_threads={thread_count}");
    println!("rust");
    println!("| Input | Single ms | Single FPS | External MT ms | External MT FPS | Faces |");
    println!("|-------|-----------|------------|----------------|-----------------|-------|");

    for case in cases {
        let resized = source.resize_exact(case.width, case.height, FilterType::Triangle);
        let single = measure_single(&resized, iterations)?;
        let multi = measure_external_threads(resized, iterations, thread_count)?;
        println!(
            "| {}x{} | {:.2} | {:.2} | {:.2} | {:.2} | {} |",
            case.width, case.height, single.ms, single.fps, multi.ms, multi.fps, single.faces
        );
    }

    Ok(())
}

fn measure_single(
    image: &image::DynamicImage,
    iterations: usize,
) -> Result<Stats, Box<dyn std::error::Error>> {
    let mut detector = Detector::new();
    for _ in 0..5 {
        black_box(detector.detect(image)?);
    }

    let started = Instant::now();
    let mut faces = 0;
    for _ in 0..iterations {
        let detection = black_box(detector.detect(image)?);
        faces = black_box(detection.face_count());
    }
    let total_ms = started.elapsed().as_secs_f64() * 1000.0;
    let ms = total_ms / iterations as f64;

    Ok(Stats {
        ms,
        fps: 1000.0 / ms,
        faces,
    })
}

fn measure_external_threads(
    image: image::DynamicImage,
    iterations: usize,
    thread_count: usize,
) -> Result<Stats, Box<dyn std::error::Error>> {
    let total_iterations = iterations.max(thread_count);
    let image = Arc::new(image);
    let barrier = Arc::new(Barrier::new(thread_count + 1));
    let mut handles = Vec::with_capacity(thread_count);

    for thread_index in 0..thread_count {
        let image = Arc::clone(&image);
        let barrier = Arc::clone(&barrier);
        let begin = total_iterations * thread_index / thread_count;
        let end = total_iterations * (thread_index + 1) / thread_count;
        handles.push(thread::spawn(move || -> Result<usize, String> {
            let mut detector = Detector::new();
            let mut faces = 0;
            black_box(detector.detect(&image).map_err(|error| error.to_string())?);
            barrier.wait();
            for _ in begin..end {
                let detection =
                    black_box(detector.detect(&image).map_err(|error| error.to_string())?);
                faces = black_box(detection.face_count());
            }
            Ok(faces)
        }));
    }

    barrier.wait();
    let started = Instant::now();
    let mut faces = 0;
    for handle in handles {
        faces = handle
            .join()
            .map_err(|_| "benchmark thread panicked".to_owned())?
            .map_err(|error| format!("benchmark thread failed: {error}"))?;
    }
    let total_ms = started.elapsed().as_secs_f64() * 1000.0;
    let ms = total_ms / total_iterations as f64;

    Ok(Stats {
        ms,
        fps: 1000.0 / ms,
        faces,
    })
}
