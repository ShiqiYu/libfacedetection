#include "hw_image.h"

namespace fdt {
namespace hw {

HwBlob ImageToInitialBlobHw(const unsigned char* input_data,
                            int image_width,
                            int image_height,
                            int image_channels,
                            int image_width_step,
                            int pad_divisor) {
    HwBlob output;
    ImageToInitialBlobHwTo(input_data, image_width, image_height,
                           image_channels, image_width_step, &output,
                           pad_divisor);
    return output;
}

void ImageToInitialBlobHwTo(const unsigned char* input_data,
                            int image_width,
                            int image_height,
                            int image_channels,
                            int image_width_step,
                            HwBlob* output,
                            int pad_divisor) {
    const int rows = ((image_height - 1) / pad_divisor + 1) * pad_divisor / 2;
    const int cols = ((image_width - 1) / pad_divisor + 1) * pad_divisor / 2;
    output->ResizeForOverwrite(rows, cols, 32);

    for (int row = 0; row < rows; ++row) {
        for (int col = 0; col < cols; ++col) {
            float* out = output->Ptr(row, col);
            const int center_y = row * 2;
            const int center_x = col * 2;
            if (center_y > 0 && center_y + 1 < image_height && center_x > 0 &&
                center_x + 1 < image_width) {
                if (image_channels == 3) {
                    const unsigned char* row0 =
                        input_data + static_cast<size_t>(image_width_step) *
                                         (center_y - 1) +
                        3 * (center_x - 1);
                    const unsigned char* row1 =
                        input_data + static_cast<size_t>(image_width_step) *
                                         center_y +
                        3 * (center_x - 1);
                    const unsigned char* row2 =
                        input_data + static_cast<size_t>(image_width_step) *
                                         (center_y + 1) +
                        3 * (center_x - 1);
                    out[0] = static_cast<float>(row0[0]);
                    out[1] = static_cast<float>(row0[1]);
                    out[2] = static_cast<float>(row0[2]);
                    out[3] = static_cast<float>(row0[3]);
                    out[4] = static_cast<float>(row0[4]);
                    out[5] = static_cast<float>(row0[5]);
                    out[6] = static_cast<float>(row0[6]);
                    out[7] = static_cast<float>(row0[7]);
                    out[8] = static_cast<float>(row0[8]);
                    out[9] = static_cast<float>(row1[0]);
                    out[10] = static_cast<float>(row1[1]);
                    out[11] = static_cast<float>(row1[2]);
                    out[12] = static_cast<float>(row1[3]);
                    out[13] = static_cast<float>(row1[4]);
                    out[14] = static_cast<float>(row1[5]);
                    out[15] = static_cast<float>(row1[6]);
                    out[16] = static_cast<float>(row1[7]);
                    out[17] = static_cast<float>(row1[8]);
                    out[18] = static_cast<float>(row2[0]);
                    out[19] = static_cast<float>(row2[1]);
                    out[20] = static_cast<float>(row2[2]);
                    out[21] = static_cast<float>(row2[3]);
                    out[22] = static_cast<float>(row2[4]);
                    out[23] = static_cast<float>(row2[5]);
                    out[24] = static_cast<float>(row2[6]);
                    out[25] = static_cast<float>(row2[7]);
                    out[26] = static_cast<float>(row2[8]);
                } else {
                    for (int fy = -1; fy <= 1; ++fy) {
                        const unsigned char* in =
                            input_data + static_cast<size_t>(image_width_step) *
                                             (center_y + fy) +
                            image_channels * (center_x - 1);
                        int out_channel = (fy + 1) * 3 * image_channels;
                        for (int fx = -1; fx <= 1; ++fx) {
                            out[out_channel] = static_cast<float>(in[0]);
                            out[out_channel + 1] = static_cast<float>(in[1]);
                            out[out_channel + 2] = static_cast<float>(in[2]);
                            in += image_channels;
                            out_channel += image_channels;
                        }
                    }
                }
                out[27] = 0.0f;
                out[28] = 0.0f;
                out[29] = 0.0f;
                out[30] = 0.0f;
                out[31] = 0.0f;
                continue;
            }

            for (int ch = 0; ch < 32; ++ch) {
                out[ch] = 0.0f;
            }
            for (int fy = -1; fy <= 1; ++fy) {
                const int src_y = center_y + fy;
                if (src_y < 0 || src_y >= image_height) {
                    continue;
                }

                for (int fx = -1; fx <= 1; ++fx) {
                    const int src_x = center_x + fx;
                    if (src_x < 0 || src_x >= image_width) {
                        continue;
                    }

                    const unsigned char* in =
                        input_data + static_cast<size_t>(image_width_step) *
                                         src_y +
                        image_channels * src_x;
                    const int output_channel_offset =
                        (fy + 1) * 3 + fx + 1;
                    out[output_channel_offset * image_channels] =
                        static_cast<float>(in[0]);
                    out[output_channel_offset * image_channels + 1] =
                        static_cast<float>(in[1]);
                    out[output_channel_offset * image_channels + 2] =
                        static_cast<float>(in[2]);
                }
            }
        }
    }
}

}  // namespace hw
}  // namespace fdt
