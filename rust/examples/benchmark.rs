use libfacedetection_rs::Detector;
use std::env;
use std::hint::black_box;
use std::time::Instant;

fn main() -> Result<(), Box<dyn std::error::Error>> {
    let mut args = env::args();
    let program = args.next().unwrap_or_else(|| "benchmark".to_owned());
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

    let image = libfacedetection_rs::image::open(path)?;
    let mut detector = Detector::new();

    black_box(detector.detect(&image)?);

    let started = Instant::now();
    let mut faces = 0;
    for _ in 0..iterations {
        let detection = black_box(detector.detect(&image)?);
        faces = black_box(detection.face_count());
    }
    let elapsed = started.elapsed();
    let avg = elapsed.as_secs_f64() * 1000.0 / iterations as f64;

    println!("iterations: {iterations}");
    println!("faces: {faces}");
    println!("total_ms: {:.3}", elapsed.as_secs_f64() * 1000.0);
    println!("avg_ms: {avg:.3}");

    Ok(())
}
