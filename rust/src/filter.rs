use crate::blob::{Blob, BlobView};

const DEFAULT_PACK_LANES: usize = 8;

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum FilterKind {
    Pointwise,
    Depthwise,
}

#[derive(Debug, Clone, Copy)]
pub struct ConvInfo<'a> {
    pub channels: usize,
    pub num_filters: usize,
    pub kind: FilterKind,
    pub with_relu: bool,
    pub weights: &'a [f32],
    pub biases: &'a [f32],
}

#[derive(Debug, Clone)]
pub struct Filter {
    channels: usize,
    num_filters: usize,
    kind: FilterKind,
    with_relu: bool,
    weights: Blob,
    biases: Vec<f32>,
    pointwise_plan: Option<PointwisePlan>,
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum PointwiseStrategy {
    Primitive,
    Packed,
}

#[derive(Debug, Clone)]
pub struct PointwisePlan {
    strategy: PointwiseStrategy,
    packed: Option<PackedPointwiseFilter>,
}

#[derive(Debug, Clone)]
pub struct PackedPointwiseFilter {
    channels: usize,
    out_channels: usize,
    padded_out_channels: usize,
    weights: Vec<f32>,
    biases: Vec<f32>,
}

impl Filter {
    pub fn load(info: ConvInfo<'_>) -> Self {
        validate_conv_info(info);

        let weight_cols = match info.kind {
            FilterKind::Pointwise => info.num_filters,
            FilterKind::Depthwise => 9,
        };
        let mut weights = Blob::with_shape(1, weight_cols, info.channels);

        for col in 0..weight_cols {
            let src = &info.weights[col * info.channels..(col + 1) * info.channels];
            weights.pixel_mut(0, col)[..info.channels].copy_from_slice(src);
        }

        let biases = info.biases[..info.num_filters].to_vec();
        let pointwise_plan = if info.kind == FilterKind::Pointwise {
            Some(PointwisePlan::new(
                weights.view(),
                &biases,
                DEFAULT_PACK_LANES,
            ))
        } else {
            None
        };

        Self {
            channels: info.channels,
            num_filters: info.num_filters,
            kind: info.kind,
            with_relu: info.with_relu,
            weights,
            biases,
            pointwise_plan,
        }
    }

    pub fn is_pointwise(&self) -> bool {
        self.kind == FilterKind::Pointwise
    }

    pub fn is_depthwise(&self) -> bool {
        self.kind == FilterKind::Depthwise
    }

    pub fn weights(&self) -> BlobView<'_> {
        self.weights.view()
    }

    pub fn biases(&self) -> &[f32] {
        &self.biases
    }

    pub fn pointwise_plan(&self) -> Option<&PointwisePlan> {
        self.pointwise_plan.as_ref()
    }

    pub fn channels(&self) -> usize {
        self.channels
    }

    pub fn num_filters(&self) -> usize {
        self.num_filters
    }

    pub fn kind(&self) -> FilterKind {
        self.kind
    }

    pub fn with_relu(&self) -> bool {
        self.with_relu
    }
}

impl PointwisePlan {
    pub fn new(weights: BlobView<'_>, biases: &[f32], lane_count: usize) -> Self {
        if should_use_packed_pointwise(weights.channels(), weights.cols()) {
            Self {
                strategy: PointwiseStrategy::Packed,
                packed: Some(PackedPointwiseFilter::pack(weights, biases, lane_count)),
            }
        } else {
            Self {
                strategy: PointwiseStrategy::Primitive,
                packed: None,
            }
        }
    }

    pub fn strategy(&self) -> PointwiseStrategy {
        self.strategy
    }

    pub fn packed(&self) -> Option<&PackedPointwiseFilter> {
        self.packed.as_ref()
    }
}

impl PackedPointwiseFilter {
    pub fn pack(weights: BlobView<'_>, biases: &[f32], lane_count: usize) -> Self {
        assert!(lane_count > 0);
        assert_eq!(weights.rows(), 1);
        assert_eq!(biases.len(), weights.cols());

        let padded_out_channels = weights.cols().div_ceil(lane_count) * lane_count;
        let mut packed_weights = vec![0.0; weights.channels() * padded_out_channels];
        let mut packed_biases = vec![0.0; padded_out_channels];

        for oc in 0..weights.cols() {
            packed_biases[oc] = biases[oc];
            let src = weights.pixel(0, oc);
            for ic in 0..weights.channels() {
                packed_weights[ic * padded_out_channels + oc] = src[ic];
            }
        }

        Self {
            channels: weights.channels(),
            out_channels: weights.cols(),
            padded_out_channels,
            weights: packed_weights,
            biases: packed_biases,
        }
    }

    pub fn channels(&self) -> usize {
        self.channels
    }

    pub fn out_channels(&self) -> usize {
        self.out_channels
    }

    pub fn padded_out_channels(&self) -> usize {
        self.padded_out_channels
    }

    pub fn weights(&self) -> &[f32] {
        &self.weights
    }

    pub fn biases(&self) -> &[f32] {
        &self.biases
    }
}

pub fn should_use_packed_pointwise(channels: usize, out_channels: usize) -> bool {
    channels >= 16 && out_channels >= 1
}

fn validate_conv_info(info: ConvInfo<'_>) {
    assert!(info.channels > 0);
    assert!(info.num_filters > 0);
    assert!(info.biases.len() >= info.num_filters);

    let expected_weights = match info.kind {
        FilterKind::Pointwise => info.channels * info.num_filters,
        FilterKind::Depthwise => info.channels * 9,
    };
    assert!(info.weights.len() >= expected_weights);

    if info.kind == FilterKind::Depthwise {
        assert_eq!(info.channels, info.num_filters);
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    fn values(len: usize) -> Vec<f32> {
        (0..len).map(|idx| idx as f32 * 0.25 - 3.0).collect()
    }

    #[test]
    fn pointwise_filter_copies_weights_and_creates_packed_plan() {
        let weights = values(16 * 16);
        let biases = values(16);
        let filter = Filter::load(ConvInfo {
            channels: 16,
            num_filters: 16,
            kind: FilterKind::Pointwise,
            with_relu: true,
            weights: &weights,
            biases: &biases,
        });

        assert!(filter.is_pointwise());
        assert_eq!(filter.weights().pixel(0, 3)[5], weights[3 * 16 + 5]);
        assert_eq!(filter.biases()[4], biases[4]);

        let plan = filter.pointwise_plan().expect("pointwise plan");
        assert_eq!(plan.strategy(), PointwiseStrategy::Packed);
        assert_eq!(plan.packed().expect("packed").padded_out_channels(), 16);
    }

    #[test]
    fn tiny_pointwise_filter_with_small_input_keeps_primitive_strategy() {
        let weights = values(8);
        let biases = values(1);
        let filter = Filter::load(ConvInfo {
            channels: 8,
            num_filters: 1,
            kind: FilterKind::Pointwise,
            with_relu: false,
            weights: &weights,
            biases: &biases,
        });

        assert_eq!(
            filter.pointwise_plan().expect("pointwise plan").strategy(),
            PointwiseStrategy::Primitive
        );
    }

    #[test]
    fn one_channel_pointwise_with_large_input_uses_packed_strategy() {
        let weights = values(64);
        let biases = values(1);
        let filter = Filter::load(ConvInfo {
            channels: 64,
            num_filters: 1,
            kind: FilterKind::Pointwise,
            with_relu: false,
            weights: &weights,
            biases: &biases,
        });

        assert_eq!(
            filter.pointwise_plan().expect("pointwise plan").strategy(),
            PointwiseStrategy::Packed
        );
    }

    #[test]
    fn keypoint_pointwise_uses_packed_strategy() {
        let weights = values(10 * 64);
        let biases = values(10);
        let filter = Filter::load(ConvInfo {
            channels: 64,
            num_filters: 10,
            kind: FilterKind::Pointwise,
            with_relu: false,
            weights: &weights,
            biases: &biases,
        });

        assert_eq!(
            filter.pointwise_plan().expect("pointwise plan").strategy(),
            PointwiseStrategy::Packed
        );
    }

    #[test]
    fn regression_pointwise_uses_packed_strategy() {
        let weights = values(4 * 64);
        let biases = values(4);
        let filter = Filter::load(ConvInfo {
            channels: 64,
            num_filters: 4,
            kind: FilterKind::Pointwise,
            with_relu: false,
            weights: &weights,
            biases: &biases,
        });

        assert_eq!(
            filter.pointwise_plan().expect("pointwise plan").strategy(),
            PointwiseStrategy::Packed
        );
    }

    #[test]
    fn depthwise_filter_has_no_pointwise_plan() {
        let weights = values(32 * 9);
        let biases = values(32);
        let filter = Filter::load(ConvInfo {
            channels: 32,
            num_filters: 32,
            kind: FilterKind::Depthwise,
            with_relu: true,
            weights: &weights,
            biases: &biases,
        });

        assert!(filter.is_depthwise());
        assert!(filter.pointwise_plan().is_none());
        assert_eq!(filter.weights().pixel(0, 8)[31], weights[8 * 32 + 31]);
    }
}
