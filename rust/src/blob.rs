const DEFAULT_CHANNEL_ALIGNMENT: usize = 8;

/// Dense HWC float tensor with padded channel stride.
///
/// The detector mostly works with small fixed channel counts. Padding each pixel
/// to an AVX2-width multiple gives scalar code deterministic padding and gives
/// future SIMD kernels naturally aligned channel blocks.
#[derive(Debug, Clone, Default)]
pub struct Blob {
    rows: usize,
    cols: usize,
    channels: usize,
    channel_step: usize,
    data: Vec<f32>,
}

#[derive(Debug, Clone, Copy)]
pub struct BlobView<'a> {
    data: &'a [f32],
    rows: usize,
    cols: usize,
    channels: usize,
    channel_step: usize,
}

#[derive(Debug)]
pub struct BlobViewMut<'a> {
    data: &'a mut [f32],
    rows: usize,
    cols: usize,
    channels: usize,
    channel_step: usize,
}

impl Blob {
    pub fn new() -> Self {
        Self::default()
    }

    pub fn with_shape(rows: usize, cols: usize, channels: usize) -> Self {
        let mut blob = Self::new();
        blob.resize(rows, cols, channels);
        blob
    }

    pub fn resize(&mut self, rows: usize, cols: usize, channels: usize) {
        self.set_shape(rows, cols, channels);
        self.data.fill(0.0);
    }

    pub fn resize_for_overwrite(&mut self, rows: usize, cols: usize, channels: usize) {
        self.set_shape(rows, cols, channels);
    }

    pub fn clear(&mut self) {
        self.data.fill(0.0);
    }

    pub fn is_empty(&self) -> bool {
        self.rows == 0 || self.cols == 0 || self.channels == 0 || self.data.is_empty()
    }

    pub fn view(&self) -> BlobView<'_> {
        BlobView {
            data: &self.data,
            rows: self.rows,
            cols: self.cols,
            channels: self.channels,
            channel_step: self.channel_step,
        }
    }

    pub fn view_mut(&mut self) -> BlobViewMut<'_> {
        BlobViewMut {
            data: &mut self.data,
            rows: self.rows,
            cols: self.cols,
            channels: self.channels,
            channel_step: self.channel_step,
        }
    }

    pub fn pixel(&self, row: usize, col: usize) -> &[f32] {
        let start = (row * self.cols + col) * self.channel_step;
        &self.data[start..start + self.channel_step]
    }

    pub fn pixel_mut(&mut self, row: usize, col: usize) -> &mut [f32] {
        let start = (row * self.cols + col) * self.channel_step;
        &mut self.data[start..start + self.channel_step]
    }

    pub fn data(&self) -> &[f32] {
        &self.data
    }

    pub fn data_mut(&mut self) -> &mut [f32] {
        &mut self.data
    }

    pub fn rows(&self) -> usize {
        self.rows
    }

    pub fn cols(&self) -> usize {
        self.cols
    }

    pub fn channels(&self) -> usize {
        self.channels
    }

    pub fn channel_step(&self) -> usize {
        self.channel_step
    }

    fn set_shape(&mut self, rows: usize, cols: usize, channels: usize) {
        let channel_step = padded_channels(channels);
        let len = rows
            .checked_mul(cols)
            .and_then(|pixels| pixels.checked_mul(channel_step))
            .expect("blob dimensions overflow usize");

        self.rows = rows;
        self.cols = cols;
        self.channels = channels;
        self.channel_step = channel_step;
        self.data.resize(len, 0.0);
    }
}

impl<'a> BlobView<'a> {
    pub fn data(&self) -> &'a [f32] {
        self.data
    }

    pub fn rows(&self) -> usize {
        self.rows
    }

    pub fn cols(&self) -> usize {
        self.cols
    }

    pub fn channels(&self) -> usize {
        self.channels
    }

    pub fn channel_step(&self) -> usize {
        self.channel_step
    }

    pub fn pixel(&self, row: usize, col: usize) -> &'a [f32] {
        let start = self.pixel_offset(row, col);
        &self.data[start..start + self.channel_step]
    }

    pub(crate) fn pixel_offset(&self, row: usize, col: usize) -> usize {
        debug_assert!(row < self.rows);
        debug_assert!(col < self.cols);
        (row * self.cols + col) * self.channel_step
    }
}

impl<'a> BlobViewMut<'a> {
    pub fn data(&self) -> &[f32] {
        self.data
    }

    pub fn data_mut(&mut self) -> &mut [f32] {
        self.data
    }

    pub fn rows(&self) -> usize {
        self.rows
    }

    pub fn cols(&self) -> usize {
        self.cols
    }

    pub fn channels(&self) -> usize {
        self.channels
    }

    pub fn channel_step(&self) -> usize {
        self.channel_step
    }

    pub fn pixel_mut(&mut self, row: usize, col: usize) -> &mut [f32] {
        let start = self.pixel_offset(row, col);
        &mut self.data[start..start + self.channel_step]
    }

    pub(crate) fn pixel_offset(&self, row: usize, col: usize) -> usize {
        debug_assert!(row < self.rows);
        debug_assert!(col < self.cols);
        (row * self.cols + col) * self.channel_step
    }
}

pub(crate) fn padded_channels(channels: usize) -> usize {
    if channels == 0 {
        return 0;
    }
    channels.div_ceil(DEFAULT_CHANNEL_ALIGNMENT) * DEFAULT_CHANNEL_ALIGNMENT
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn blob_pads_channels_to_avx2_width() {
        let blob = Blob::with_shape(2, 3, 10);

        assert_eq!(blob.rows(), 2);
        assert_eq!(blob.cols(), 3);
        assert_eq!(blob.channels(), 10);
        assert_eq!(blob.channel_step(), 16);
        assert_eq!(blob.data().len(), 2 * 3 * 16);
    }

    #[test]
    fn resize_for_overwrite_reuses_existing_values_when_shape_is_same() {
        let mut blob = Blob::with_shape(1, 1, 3);
        blob.pixel_mut(0, 0)[0] = 42.0;

        blob.resize_for_overwrite(1, 1, 3);

        assert_eq!(blob.pixel(0, 0)[0], 42.0);
    }

    #[test]
    fn resize_clears_existing_storage() {
        let mut blob = Blob::with_shape(1, 1, 3);
        blob.pixel_mut(0, 0)[0] = 42.0;

        blob.resize(1, 1, 3);

        assert_eq!(blob.pixel(0, 0)[0], 0.0);
    }
}
