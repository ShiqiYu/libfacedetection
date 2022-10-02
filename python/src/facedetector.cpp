#include <pybind11/pybind11.h>
#include <pybind11/numpy.h>
#include "facedetectcnn.h"
#include "facedetector.h"

namespace py = pybind11;

py::array_t<int> FaceDetector::detect(const py::array_t<unsigned char> &image)
{
    auto *buffer = (unsigned char *) malloc(DETECT_BUFFER_SIZE);
    const auto &info = image.request();
    const auto &shape = info.shape;

    int *results = facedetect_cnn(buffer, (unsigned char *) (info.ptr), shape[1], shape[0], shape[1] * 3);
    auto detections = py::array_t<int>({*results, 5});

    for (int i = 0; i < *results; i++) {
        short *p = ((short *) (results + 1)) + 142 * i;
        *detections.mutable_data(i, 0) = p[0];
        *detections.mutable_data(i, 1) = p[1];
        *detections.mutable_data(i, 2) = p[2];
        *detections.mutable_data(i, 3) = p[3];
        *detections.mutable_data(i, 4) = p[4];
    }

    std::free(buffer);

    return detections;
}
