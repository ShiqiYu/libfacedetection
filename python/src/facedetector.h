#include <pybind11/pybind11.h>
#include <pybind11/numpy.h>

#define DETECT_BUFFER_SIZE 0x20000

namespace py = pybind11;

class FaceDetector {
public:
    py::array_t<int> detect(const py::array_t<unsigned char>& image);
};
