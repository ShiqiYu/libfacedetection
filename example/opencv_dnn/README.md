# Deploy libfacedetection with the OpenCV DNN module

Example to deploy libfacedetection with the OpenCV DNN module and [ONNX](https://github.com/onnx/onnx) in both Python and C++.

Envrionment tested:
- System: Ubuntu 18.04 LTS / 20.04 LTS
- OpenCV 4.3.0 / 4.4.0
- Python >= 3.6


Download the converted `onnx` model from https://github.com/ShiqiYu/libfacedetection.train/tree/master/tasks/task1/onnx and place it in this directory.
You can view the network architecture here[[netron]](https://lutzroeder.github.io/netron/?url=https://raw.githubusercontent.com/ShiqiYu/libfacedetection.train/master/tasks/task1/onnx/YuFaceDetectNet_320.onnx).

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
0. (Optional) Compile OpenCV with `pkg-confg` information for simplicity.
    ```shell
    sudo apt install pkg-config cmake libgtk2.0-dev
    # Download and extract OpenCV source code.
    # Assume the root directory of OpenCV source code is $opencv.
    cd $opencv
    mkdir build && cd build
    cmake -D CMAKE_BUILD_TYPE=RELEASE \
        -D CMAKE_INSTALL_PREFIX=/usr/local \ # or change to your preferred dir
        -D BUILD_opencv_python2=OFF \
        -D BUILD_opencv_python3=OFF \
        -D INSTALL_PYTHON_EXAMPLES=OFF \
        -D INSTALL_C_EXAMPLES=OFF \
        -D BUILD_EXAMPLES=OFF \
        -D OPENCV_GENERATE_PKGCONFIG=YES ..
    make
    make install # `sudo` is needed if you do not have access to write into the prefix dir
    ```
1. Change the location of example image and model path in `detect.cpp` accordingly.
    ```shell
    cd cpp
    # modify line 24 and 25 in `detect.cpp`
    ```
2. Compile the C++ demo. NOTE: if your OpenCV installation directory is not `/usr/local`, change the value of `OPENCV` in `Makefile` to `/your/install/prefix/lib/pkgconfig/opencv4.pc`.
    ```shell
    cp Makefile.config Makefile
    make
    ```
3. Run the detection demo. For more options, check `python/detect.py`. NOTE: if your OpenCV installation directory is not `/usr/local`, you need to add `/your/install/prefix/lib` to `$LD_LIBRARY_APTH`.
    ```shell
    # export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/your/install/prefix/lib
    ./detect
    ```