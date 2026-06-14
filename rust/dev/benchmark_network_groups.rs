use libfacedetection_rs::blob::Blob;
use libfacedetection_rs::input::{PixelFormat, image_to_initial_blob_to};
use libfacedetection_rs::layers::{
    convolution_to, depthwise_pointwise_to, max_pool_2x2_s2_to, upsample_x2_add_to,
};
use libfacedetection_rs::model::{Model, load_static_model};
use std::env;
use std::hint::black_box;
use std::mem;
use std::time::{Duration, Instant};

const GROUPS: [&str; 15] = [
    "conv0",
    "dp_1_2",
    "pool1",
    "unit_3_6",
    "unit_7_10",
    "pool2",
    "fb1_11_14",
    "fb2_15_18",
    "fb3_19_22",
    "head_l5",
    "head_l4",
    "head_l3",
    "head_out_l5",
    "head_out_l4",
    "head_out_l3",
];

fn main() -> Result<(), Box<dyn std::error::Error>> {
    let mut args = env::args();
    let program = args
        .next()
        .unwrap_or_else(|| "benchmark_network_groups".to_owned());
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
    let mut input = Blob::new();
    image_to_initial_blob_to(pixels, width, height, step, PixelFormat::Rgb, &mut input);

    let model = load_static_model();
    let mut graph = GraphWorkspace::default();
    let mut totals = [Duration::ZERO; GROUPS.len()];

    forward_network_grouped(
        &input,
        &model,
        &mut graph,
        &mut [Duration::ZERO; GROUPS.len()],
    );
    black_box(graph.heads.cls[0].channels());

    for _ in 0..iterations {
        forward_network_grouped(&input, &model, &mut graph, &mut totals);
        black_box(graph.heads.cls[0].data());
    }

    println!("iterations: {iterations}");
    for (name, total) in GROUPS.iter().zip(totals) {
        println!(
            "{name}: {:.3}",
            total.as_secs_f64() * 1000.0 / iterations as f64
        );
    }

    Ok(())
}

#[derive(Default)]
struct GraphWorkspace {
    fx: Blob,
    pointwise: Blob,
    block: Blob,
    output: Blob,
    fb1: Blob,
    fb2: Blob,
    fb3: Blob,
    head_pointwise: Blob,
    head_branch: Blob,
    heads: HeadOutputs,
}

#[derive(Default)]
struct HeadOutputs {
    cls: [Blob; 3],
    reg: [Blob; 3],
    kps: [Blob; 3],
    obj: [Blob; 3],
}

fn forward_network_grouped(
    input: &Blob,
    model: &Model,
    graph: &mut GraphWorkspace,
    totals: &mut [Duration; GROUPS.len()],
) {
    totals[0] += measure(|| {
        convolution_to(input.view(), model.filter(0), true, &mut graph.fx);
    });
    totals[1] += measure(|| {
        depthwise_pointwise_to(
            graph.fx.view(),
            model.filter(1),
            model.filter(2),
            true,
            &mut graph.pointwise,
            &mut graph.block,
        );
        mem::swap(&mut graph.fx, &mut graph.block);
    });
    totals[2] += measure(|| {
        max_pool_2x2_s2_to(graph.fx.view(), &mut graph.block);
        mem::swap(&mut graph.fx, &mut graph.block);
    });

    totals[3] += measure(|| {
        convolution_4layer_unit_to(graph, model, [3, 4, 5, 6], true);
        mem::swap(&mut graph.fx, &mut graph.output);
    });
    totals[4] += measure(|| {
        convolution_4layer_unit_to(graph, model, [7, 8, 9, 10], true);
        mem::swap(&mut graph.fx, &mut graph.output);
    });
    totals[5] += measure(|| {
        max_pool_2x2_s2_to(graph.fx.view(), &mut graph.block);
        mem::swap(&mut graph.fx, &mut graph.block);
    });
    totals[6] += measure(|| {
        convolution_4layer_unit_to_to_output(
            &graph.fx,
            model,
            [11, 12, 13, 14],
            true,
            &mut graph.pointwise,
            &mut graph.block,
            &mut graph.fb1,
        );
    });
    totals[7] += measure(|| {
        max_pool_2x2_s2_to(graph.fb1.view(), &mut graph.fx);
        convolution_4layer_unit_to_to_output(
            &graph.fx,
            model,
            [15, 16, 17, 18],
            true,
            &mut graph.pointwise,
            &mut graph.block,
            &mut graph.fb2,
        );
    });
    totals[8] += measure(|| {
        max_pool_2x2_s2_to(graph.fb2.view(), &mut graph.fx);
        convolution_4layer_unit_to_to_output(
            &graph.fx,
            model,
            [19, 20, 21, 22],
            true,
            &mut graph.pointwise,
            &mut graph.block,
            &mut graph.fb3,
        );
    });

    totals[9] += measure(|| {
        depthwise_pointwise_to(
            graph.fb3.view(),
            model.filter(27),
            model.filter(28),
            true,
            &mut graph.head_pointwise,
            &mut graph.head_branch,
        );
        mem::swap(&mut graph.fb3, &mut graph.head_branch);
    });
    totals[12] += measure(|| {
        head_outputs_for_level(
            graph.fb3.view_blob(),
            model,
            2,
            &mut graph.head_pointwise,
            &mut graph.heads,
        );
    });
    totals[10] += measure(|| {
        upsample_x2_add_to(graph.fb3.view(), graph.fb2.view(), &mut graph.head_branch);
        mem::swap(&mut graph.fb2, &mut graph.head_branch);
        depthwise_pointwise_to(
            graph.fb2.view(),
            model.filter(25),
            model.filter(26),
            true,
            &mut graph.head_pointwise,
            &mut graph.head_branch,
        );
        mem::swap(&mut graph.fb2, &mut graph.head_branch);
    });
    totals[13] += measure(|| {
        head_outputs_for_level(
            graph.fb2.view_blob(),
            model,
            1,
            &mut graph.head_pointwise,
            &mut graph.heads,
        );
    });
    totals[11] += measure(|| {
        upsample_x2_add_to(graph.fb2.view(), graph.fb1.view(), &mut graph.head_branch);
        mem::swap(&mut graph.fb1, &mut graph.head_branch);
        depthwise_pointwise_to(
            graph.fb1.view(),
            model.filter(23),
            model.filter(24),
            true,
            &mut graph.head_pointwise,
            &mut graph.head_branch,
        );
        mem::swap(&mut graph.fb1, &mut graph.head_branch);
    });
    totals[14] += measure(|| {
        head_outputs_for_level(
            graph.fb1.view_blob(),
            model,
            0,
            &mut graph.head_pointwise,
            &mut graph.heads,
        );
    });
}

fn convolution_4layer_unit_to(
    graph: &mut GraphWorkspace,
    model: &Model,
    filters: [usize; 4],
    do_relu: bool,
) {
    convolution_4layer_unit_to_to_output(
        &graph.fx,
        model,
        filters,
        do_relu,
        &mut graph.pointwise,
        &mut graph.block,
        &mut graph.output,
    );
}

fn convolution_4layer_unit_to_to_output(
    input: &Blob,
    model: &Model,
    filters: [usize; 4],
    do_relu: bool,
    pointwise_workspace: &mut Blob,
    block_workspace: &mut Blob,
    output: &mut Blob,
) {
    depthwise_pointwise_to(
        input.view(),
        model.filter(filters[0]),
        model.filter(filters[1]),
        true,
        pointwise_workspace,
        block_workspace,
    );
    depthwise_pointwise_to(
        block_workspace.view(),
        model.filter(filters[2]),
        model.filter(filters[3]),
        do_relu,
        pointwise_workspace,
        output,
    );
}

fn head_outputs_for_level(
    input: BlobRef<'_>,
    model: &Model,
    level: usize,
    pointwise_workspace: &mut Blob,
    heads: &mut HeadOutputs,
) {
    let (cls, reg, kps, obj) = match level {
        0 => ([29, 30], [35, 36], [47, 48], [41, 42]),
        1 => ([31, 32], [37, 38], [49, 50], [43, 44]),
        2 => ([33, 34], [39, 40], [51, 52], [45, 46]),
        _ => unreachable!("only three feature levels"),
    };
    depthwise_pointwise_to(
        input.0.view(),
        model.filter(cls[0]),
        model.filter(cls[1]),
        false,
        pointwise_workspace,
        &mut heads.cls[level],
    );
    depthwise_pointwise_to(
        input.0.view(),
        model.filter(reg[0]),
        model.filter(reg[1]),
        false,
        pointwise_workspace,
        &mut heads.reg[level],
    );
    depthwise_pointwise_to(
        input.0.view(),
        model.filter(kps[0]),
        model.filter(kps[1]),
        false,
        pointwise_workspace,
        &mut heads.kps[level],
    );
    depthwise_pointwise_to(
        input.0.view(),
        model.filter(obj[0]),
        model.filter(obj[1]),
        false,
        pointwise_workspace,
        &mut heads.obj[level],
    );
}

struct BlobRef<'a>(&'a Blob);

trait ViewBlob {
    fn view_blob(&self) -> BlobRef<'_>;
}

impl ViewBlob for Blob {
    fn view_blob(&self) -> BlobRef<'_> {
        BlobRef(self)
    }
}

fn measure(run: impl FnOnce()) -> Duration {
    let started = Instant::now();
    run();
    started.elapsed()
}
