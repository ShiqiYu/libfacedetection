#include <facedetection/facedetect_hw.h>

#include "hw_image.h"
#include "hw_model.h"
#include "hw_network.h"
#include "hw_postprocess.h"

#include <algorithm>
#include <vector>

namespace {

void PackFaces(unsigned char* result_buffer,
               const std::vector<fdt::hw::HwFaceRect>& faces) {
    const int num_faces = std::min(
        static_cast<int>(faces.size()), FACEDETECTION_HW_RESULT_MAX_FACES);
    int* count = reinterpret_cast<int*>(result_buffer);
    count[0] = num_faces;

    for (int i = 0; i < num_faces; ++i) {
        short* out =
            reinterpret_cast<short*>(result_buffer + 4) +
            FACEDETECTION_HW_RESULT_STRIDE_SHORTS * static_cast<size_t>(i);
        out[0] = static_cast<short>(faces[static_cast<size_t>(i)].score * 100);
        out[1] = static_cast<short>(faces[static_cast<size_t>(i)].x);
        out[2] = static_cast<short>(faces[static_cast<size_t>(i)].y);
        out[3] = static_cast<short>(faces[static_cast<size_t>(i)].w);
        out[4] = static_cast<short>(faces[static_cast<size_t>(i)].h);
        for (int lm = 0; lm < 10; ++lm) {
            out[5 + lm] =
                static_cast<short>(faces[static_cast<size_t>(i)].lm[lm]);
        }
    }
}

}  // namespace

extern "C" int* facedetect_hw_cnn(unsigned char* result_buffer,
                                  unsigned char* bgr_image_data,
                                  int width,
                                  int height,
                                  int step) {
    if (!result_buffer) {
        return 0;
    }
    result_buffer[0] = 0;
    result_buffer[1] = 0;
    result_buffer[2] = 0;
    result_buffer[3] = 0;

    static const fdt::hw::HwModel model = fdt::hw::LoadStaticModel();
    thread_local fdt::hw::HwBlob input;
    thread_local fdt::hw::HwNetworkWorkspace network_workspace;
    thread_local fdt::hw::HwHeadOutputs head_outputs;
    thread_local fdt::hw::HwDecodedOutputs decoded;
    fdt::hw::ImageToInitialBlobHwTo(bgr_image_data, width, height, 3, step,
                                    &input);
    fdt::hw::ForwardNetworkHwTo(input, model, &network_workspace,
                                &head_outputs);
    fdt::hw::DecodeAndConcatHwTo(&head_outputs, &decoded);
    const std::vector<fdt::hw::HwFaceRect> faces = fdt::hw::DetectionOutputHw(
        decoded, 0.45f, 0.2f, 1000, 512);
    PackFaces(result_buffer, faces);
    return reinterpret_cast<int*>(result_buffer);
}
