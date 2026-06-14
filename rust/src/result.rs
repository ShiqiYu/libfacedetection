use crate::error::DetectError;

pub const FACEDETECTION_RESULT_BUFFER_SIZE: usize = 0x9000;
pub const FACEDETECTION_RESULT_MAX_FACES: usize = 1024;
pub const FACEDETECTION_RESULT_STRIDE_SHORTS: usize = 16;

#[derive(Debug, Clone, Copy, PartialEq)]
pub struct Face {
    pub score: f32,
    pub x: f32,
    pub y: f32,
    pub width: f32,
    pub height: f32,
    pub landmarks: [f32; 10],
}

/// Owned C-compatible result buffer.
#[derive(Debug, Clone)]
pub struct ResultBuffer {
    bytes: Vec<u8>,
}

impl Default for ResultBuffer {
    fn default() -> Self {
        Self::new()
    }
}

impl ResultBuffer {
    pub fn new() -> Self {
        Self {
            bytes: vec![0; FACEDETECTION_RESULT_BUFFER_SIZE],
        }
    }

    pub fn validate_len(actual: usize) -> Result<(), DetectError> {
        if actual < FACEDETECTION_RESULT_BUFFER_SIZE {
            return Err(DetectError::ResultBufferTooSmall {
                actual,
                required: FACEDETECTION_RESULT_BUFFER_SIZE,
            });
        }
        Ok(())
    }

    pub fn as_bytes(&self) -> &[u8] {
        &self.bytes
    }

    pub fn as_mut_bytes(&mut self) -> &mut [u8] {
        &mut self.bytes
    }

    pub fn face_count(&self) -> usize {
        let mut bytes = [0_u8; 4];
        bytes.copy_from_slice(&self.bytes[..4]);
        i32::from_ne_bytes(bytes).max(0) as usize
    }
}

/// Owned detection output for the simple Rust API.
#[derive(Debug, Clone)]
pub struct Detection {
    result_buffer: ResultBuffer,
    faces: Vec<Face>,
}

impl Detection {
    pub(crate) fn new(result_buffer: ResultBuffer, faces: Vec<Face>) -> Self {
        Self {
            result_buffer,
            faces,
        }
    }

    pub fn face_count(&self) -> usize {
        self.faces.len()
    }

    pub fn faces(&self) -> &[Face] {
        &self.faces
    }

    pub fn result_buffer(&self) -> &[u8] {
        self.result_buffer.as_bytes()
    }
}

/// Lightweight summary for `detect_into`.
#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub struct DetectionSummary {
    face_count: usize,
}

impl DetectionSummary {
    pub(crate) fn new(face_count: usize) -> Self {
        Self { face_count }
    }

    pub fn face_count(self) -> usize {
        self.face_count
    }
}

pub(crate) fn write_faces_to_result_buffer(faces: &[Face], result_buffer: &mut [u8]) -> usize {
    let face_count = faces.len().min(FACEDETECTION_RESULT_MAX_FACES);
    result_buffer[..4].copy_from_slice(&(face_count as i32).to_ne_bytes());

    for (index, face) in faces.iter().take(face_count).enumerate() {
        let record_start = 4 + index * FACEDETECTION_RESULT_STRIDE_SHORTS * size_of::<i16>();
        write_i16(result_buffer, record_start, 0, (face.score * 100.0) as i16);
        write_i16(result_buffer, record_start, 1, face.x as i16);
        write_i16(result_buffer, record_start, 2, face.y as i16);
        write_i16(result_buffer, record_start, 3, face.width as i16);
        write_i16(result_buffer, record_start, 4, face.height as i16);
        for (lm_index, landmark) in face.landmarks.iter().enumerate() {
            write_i16(result_buffer, record_start, 5 + lm_index, *landmark as i16);
        }
    }

    face_count
}

fn write_i16(buffer: &mut [u8], record_start: usize, field_index: usize, value: i16) {
    let start = record_start + field_index * size_of::<i16>();
    buffer[start..start + size_of::<i16>()].copy_from_slice(&value.to_ne_bytes());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn default_buffer_has_expected_size_and_zero_faces() {
        let buffer = ResultBuffer::new();
        assert_eq!(buffer.as_bytes().len(), FACEDETECTION_RESULT_BUFFER_SIZE);
        assert_eq!(buffer.face_count(), 0);
    }

    #[test]
    fn writes_c_compatible_face_records() {
        let mut buffer = ResultBuffer::new();
        let face = Face {
            score: 0.91,
            x: 10.7,
            y: 20.2,
            width: 30.9,
            height: 40.1,
            landmarks: [1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0],
        };

        let count = write_faces_to_result_buffer(&[face], buffer.as_mut_bytes());

        assert_eq!(count, 1);
        assert_eq!(buffer.face_count(), 1);
        assert_eq!(read_i16(buffer.as_bytes(), 4, 0), 91);
        assert_eq!(read_i16(buffer.as_bytes(), 4, 1), 10);
        assert_eq!(read_i16(buffer.as_bytes(), 4, 4), 40);
        assert_eq!(read_i16(buffer.as_bytes(), 4, 14), 10);
        assert_eq!(read_i16(buffer.as_bytes(), 4, 15), 0);
    }

    fn read_i16(buffer: &[u8], record_start: usize, field_index: usize) -> i16 {
        let start = record_start + field_index * size_of::<i16>();
        i16::from_ne_bytes([buffer[start], buffer[start + 1]])
    }
}
