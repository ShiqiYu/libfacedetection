#include <pybind11/pybind11.h>
#include <pybind11/numpy.h>
#include "facedetector.h"

namespace py = pybind11;

PYBIND11_MODULE(pylibfacedetection, m)
{
    m.doc() = "libfacedetection Python bindings";

    py::class_<FaceDetector>(m, "FaceDetector")
        .def(py::init<>())
        .def("detect", (py::array_t<int> (FaceDetector::*)(py::array_t<unsigned char>)) &FaceDetector::detect);
};
