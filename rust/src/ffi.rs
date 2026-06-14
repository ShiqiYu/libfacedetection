use crate::Detector;
use crate::result::FACEDETECTION_RESULT_BUFFER_SIZE;

use std::cell::RefCell;
use std::slice;

thread_local! {
    static DETECTOR: RefCell<Detector> = RefCell::new(Detector::new());
}

/// C-compatible entry point matching the existing result-buffer style.
///
/// Returns `result_buffer` cast to `*mut i32` on success and null on invalid
/// input. The current scaffold writes a valid empty detection result.
///
/// # Safety
///
/// `result_buffer` must point to at least `FACEDETECTION_RESULT_BUFFER_SIZE`
/// writable bytes. `bgr_image_data` must point to at least `step * height`
/// readable bytes. Both pointers must remain valid for the duration of the
/// call, and the regions must not overlap in a way that violates Rust aliasing
/// rules.
#[unsafe(no_mangle)]
pub unsafe extern "C" fn facedetect_rs_cnn(
    result_buffer: *mut u8,
    bgr_image_data: *const u8,
    width: i32,
    height: i32,
    step: i32,
) -> *mut i32 {
    if result_buffer.is_null() || bgr_image_data.is_null() || width <= 0 || height <= 0 || step <= 0
    {
        return std::ptr::null_mut();
    }

    let width = width as usize;
    let height = height as usize;
    let step = step as usize;
    let image_len = match step.checked_mul(height) {
        Some(len) => len,
        None => return std::ptr::null_mut(),
    };

    let image = unsafe { slice::from_raw_parts(bgr_image_data, image_len) };
    let result =
        unsafe { slice::from_raw_parts_mut(result_buffer, FACEDETECTION_RESULT_BUFFER_SIZE) };

    let ok = DETECTOR.with(|detector| {
        detector
            .borrow_mut()
            .detect_into(image, width, height, step, result)
            .is_ok()
    });

    if ok {
        result_buffer.cast::<i32>()
    } else {
        std::ptr::null_mut()
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn ffi_writes_empty_result_for_valid_input() {
        let width = 2;
        let height = 2;
        let step = width * 3;
        let image = vec![0_u8; step * height];
        let mut result = vec![1_u8; FACEDETECTION_RESULT_BUFFER_SIZE];

        let ptr = unsafe {
            facedetect_rs_cnn(
                result.as_mut_ptr(),
                image.as_ptr(),
                width as i32,
                height as i32,
                step as i32,
            )
        };

        assert!(!ptr.is_null());
        assert_eq!(&result[..4], &[0, 0, 0, 0]);
    }
}
