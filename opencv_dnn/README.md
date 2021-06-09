# Deploy libfacedetection with the OpenCV DNN module

Example to deploy libfacedetection with the OpenCV DNN module and [ONNX](https://github.com/onnx/onnx) in both Python and C++.

***NOTE***: The OpenCV DNN module now supports ONNX models with dynamic input sizes since version 4.5.1. Please install OpenCV >= 4.5.1 to run this example!

Envrionment tested:
- System: Ubuntu 18.04 LTS / 20.04 LTS
- OpenCV >= 4.5.1
- Python >= 3.6

Download the ported `onnx` model from https://github.com/ShiqiYu/libfacedetection.train/tree/master/tasks/task1/onnx and place it in this directory.
You can view the network architecture here[[netron]](https://netron.app/?url=https://raw.githubusercontent.com/ShiqiYu/libfacedetection.train/master/tasks/task1/onnx/YuFaceDetectNet.onnx).

## Python
1. Install `numpy` and `opencv-python`.
    ```shell
    pip install numpy
    pip install opencv-python
    ```
2. Run the detection demo. For more options, run `python python/detect.py --help`.
    ```shell
    python python/detect.py --image=/path/to/example/image --model=/path/to/onnx/model
    ```

## C++
1. Build the example with `cmake`:
    ```shell
    cd cpp
    mkdir build && cd build
    cmake .. # NOTE: if cmake failed find OpenCV, add the option `-DCMAKE_PREFIX_PATH=/path/to/opencv/install`
    make
    ```
2. Run the example:
    ```shell
    ./detect-image /path/to/image /path/to/YuFaceDetectNet.onnx
    ./detect-camera 0 /path/to/YuFaceDetectNet.onnx  # '0' is your camera index.
    ```