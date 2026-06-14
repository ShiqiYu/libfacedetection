use core::fmt;

/// Errors returned by the safe Rust API.
#[derive(Debug, Clone, PartialEq, Eq)]
pub enum DetectError {
    InvalidDimension { name: &'static str, value: usize },
    DimensionOverflow,
    ImageRead(String),
    StepTooSmall { step: usize, min_step: usize },
    ImageTooSmall { actual: usize, required: usize },
    ResultBufferTooSmall { actual: usize, required: usize },
}

impl fmt::Display for DetectError {
    fn fmt(&self, f: &mut fmt::Formatter<'_>) -> fmt::Result {
        match self {
            Self::InvalidDimension { name, value } => {
                write!(f, "invalid {name}: {value}")
            }
            Self::DimensionOverflow => write!(f, "image dimensions overflow"),
            Self::ImageRead(message) => write!(f, "failed to read image: {message}"),
            Self::StepTooSmall { step, min_step } => {
                write!(f, "image step {step} is smaller than required {min_step}")
            }
            Self::ImageTooSmall { actual, required } => {
                write!(f, "image slice has {actual} bytes, requires {required}")
            }
            Self::ResultBufferTooSmall { actual, required } => {
                write!(f, "result buffer has {actual} bytes, requires {required}")
            }
        }
    }
}

impl std::error::Error for DetectError {}

impl From<image::ImageError> for DetectError {
    fn from(error: image::ImageError) -> Self {
        Self::ImageRead(error.to_string())
    }
}
