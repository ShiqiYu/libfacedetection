use crate::filter::{ConvInfo, Filter};

pub const CONV_LAYER_COUNT: usize = 53;

#[allow(clippy::approx_constant)]
mod generated {
    include!("generated/model_data.rs");
}

#[cfg(test)]
pub use generated::STATIC_MODEL_LAYER_COUNT;
pub(crate) use generated::load_static_model;

#[derive(Debug, Clone, Default)]
pub struct Model {
    filters: Vec<Filter>,
}

impl Model {
    pub fn new() -> Self {
        Self::default()
    }

    pub fn with_capacity(capacity: usize) -> Self {
        Self {
            filters: Vec::with_capacity(capacity),
        }
    }

    pub fn add_filter(&mut self, info: ConvInfo<'_>) {
        self.filters.push(Filter::load(info));
    }

    pub fn filter(&self, index: usize) -> &Filter {
        &self.filters[index]
    }

    pub fn len(&self) -> usize {
        self.filters.len()
    }

    pub fn is_empty(&self) -> bool {
        self.filters.is_empty()
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::filter::{ConvInfo, FilterKind, PointwiseStrategy};

    #[test]
    fn model_owns_loaded_filter_plans() {
        let weights = vec![1.0; 16 * 16];
        let biases = vec![0.0; 16];
        let mut model = Model::with_capacity(CONV_LAYER_COUNT);

        model.add_filter(ConvInfo {
            channels: 16,
            num_filters: 16,
            kind: FilterKind::Pointwise,
            with_relu: true,
            weights: &weights,
            biases: &biases,
        });

        assert_eq!(model.len(), 1);
        assert_eq!(
            model
                .filter(0)
                .pointwise_plan()
                .expect("pointwise plan")
                .strategy(),
            PointwiseStrategy::Packed
        );
    }

    #[test]
    fn generated_static_model_loads_all_filters() {
        let model = load_static_model();

        assert_eq!(STATIC_MODEL_LAYER_COUNT, CONV_LAYER_COUNT);
        assert_eq!(model.len(), CONV_LAYER_COUNT);

        let first = model.filter(0);
        assert_eq!(first.channels(), 32);
        assert_eq!(first.num_filters(), 16);
        assert!(first.is_pointwise());
        assert_eq!(
            first
                .pointwise_plan()
                .expect("first pointwise plan")
                .strategy(),
            PointwiseStrategy::Packed
        );

        let first_depthwise = model.filter(2);
        assert_eq!(first_depthwise.channels(), 16);
        assert!(first_depthwise.is_depthwise());
        assert!(first_depthwise.pointwise_plan().is_none());

        let tiny_head = model.filter(47);
        assert_eq!(tiny_head.channels(), 64);
        assert_eq!(tiny_head.num_filters(), 10);
        assert_eq!(
            tiny_head
                .pointwise_plan()
                .expect("tiny pointwise plan")
                .strategy(),
            PointwiseStrategy::Packed
        );
    }
}
