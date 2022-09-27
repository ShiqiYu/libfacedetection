# Deploy libfacedetection with OpenCV

Example to deploy libfacedetection with the OpenCV's FaceDetectorYN in both Python and C++.

Please note that OpenCV DNN does not support the latest version of YuNet with dynamic input shape. Please ensure you have the exact same input shape as the one in the ONNX model to run latest YuNet with OpenCV DNN.

***Important Notes***:
- Install OpenCV >= 4.5.4 to have the API `FaceDetectorYN`.
- Download the ONNX model from [OpenCV Zoo](https://github.com/opencv/opencv_zoo/tree/master/models/face_detection_yunet).

Envrionment tested:
- System: Ubuntu 18.04 LTS / 20.04 LTS
- OpenCV >= 4.5.4
- Python >= 3.6

## Python
1. Install `numpy` and `opencv-python`.
    ```shell
    pip install numpy
    pip install "opencv-python>=4.5.4.58"
    ```
2. Run demo. For more options, run `python python/detect.py --help`.
    ```shell
    # detect on an image
    python python/detect.py --model=/path/to/yunet.onnx --input=/path/to/example/image
    # detect on default camera
    python python/detect.py --model=/path/to/yunet.onnx
    ```

## C++
1. Build the example with `cmake`:
    ```shell
    cd cpp
    mkdir build && cd build
    cmake .. # NOTE: if cmake failed finding OpenCV, add the option `-DCMAKE_PREFIX_PATH=/path/to/opencv/install`
    make
    ```
2. Run the example:
    ```shell
    # detect on an image
    ./detect -m/path/to/yunet.onnx -i=/path/to/image
    # detect on default camera
    ./detect -m/path/to/yunet.onnx
    ```
