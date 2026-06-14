#pragma once

#include "hw_blob.h"

namespace fdt {
namespace hw {

HwBlob ImageToInitialBlobHw(const unsigned char* input_data,
                            int image_width,
                            int image_height,
                            int image_channels,
                            int image_width_step,
                            int pad_divisor = 32);
void ImageToInitialBlobHwTo(const unsigned char* input_data,
                            int image_width,
                            int image_height,
                            int image_channels,
                            int image_width_step,
                            HwBlob* output,
                            int pad_divisor = 32);

}  // namespace hw
}  // namespace fdt
