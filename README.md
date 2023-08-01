# libfacedetection

This is an open source library for CNN-based face detection in images. The CNN model has been converted to static variables in C source files. The source code does not depend on any other libraries. What you need is just a C++ compiler. You can compile the source code under Windows, Linux, ARM and any platform with a C++ compiler.

SIMD instructions are used to speed up the detection. You can enable AVX2 if you use Intel CPU or NEON for ARM.

The model files are provided in `src/facedetectcnn-data.cpp` (C++ arrays) & [the model (ONNX) from OpenCV Zoo](https://github.com/opencv/opencv_zoo/tree/master/models/face_detection_yunet). You can try our scripts (C++ & Python) in `opencv_dnn/` with the ONNX model. View the network architecture [here](https://netron.app/?url=https://raw.githubusercontent.com/ShiqiYu/libfacedetection.train/master/onnx/yunet*.onnx).

Please note that OpenCV DNN does not support the latest version of YuNet with dynamic input shape. Please ensure you have the exact same input shape as the one in the ONNX model to run latest YuNet with OpenCV DNN.

examples/detect-image.cpp and examples/detect-camera.cpp show how to use the library.

The library was trained by [libfacedetection.train](https://github.com/ShiqiYu/libfacedetection.train).

![Examples](/images/cnnresult.png "Detection example")

## How to use the code

You can copy the files in directory src/ into your project,
and compile them as the other files in your project.
The source code is written in standard C/C++.
It should be compiled at any platform which supports C/C++.

Some tips:

  * Please add facedetection_export.h file in the position where you copy your facedetectcnn.h files, add #define FACEDETECTION_EXPORT to  facedetection_export.h file. See: [issues #222](https://github.com/ShiqiYu/libfacedetection/issues/222)
  * Please add -O3 to turn on optimizations when you compile the source code using g++.
  * Please choose 'Maximize Speed/-O2' when you compile the source code using Microsoft Visual Studio.
  * You can enable OpenMP to speedup. But the best solution is to call the detection function in different threads.

You can also compile the source code to a static or dynamic library, and then use it in your project.

[How to compile](COMPILE.md)


## CNN-based Face Detection on Intel CPU

Using **AVX2** instructions
| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |  X64         |X64          |  X64         |X64          |
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|cnn (CPU, 640x480)  |  50.02ms     |  19.99      |   6.55ms     |  152.65     |
|cnn (CPU, 320x240)  |  13.09ms     |  76.39      |   1.82ms     |  550.54     |
|cnn (CPU, 160x120)  |   3.61ms     | 277.37      |   0.57ms     | 1745.13     |
|cnn (CPU, 128x96)   |   2.11ms     | 474.60      |   0.33ms     | 2994.23     | 

Using **AVX512** instructions
| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |  X64         |X64          |  X64         |X64          |
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|cnn (CPU, 640x480)  |  46.47ms     |  21.52      |   6.39ms     |  156.47     |
|cnn (CPU, 320x240)  |  12.10ms     |  82.67      |   1.67ms     |  599.31     |
|cnn (CPU, 160x120)  |   3.37ms     | 296.47      |   0.46ms     | 2155.80     |
|cnn (CPU, 128x96)   |   1.98ms     | 504.72      |   0.31ms     | 3198.63     | 

* Minimal face size ~10x10
* Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz
* Multi-thread in 16 threads and 16 processors.


## CNN-based Face Detection on ARM Linux (Raspberry Pi 4 B)

| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|cnn (CPU, 640x480)  |  404.63ms    |  2.47       |  125.47ms    |   7.97      |
|cnn (CPU, 320x240)  |  105.73ms    |  9.46       |   32.98ms    |  30.32      |
|cnn (CPU, 160x120)  |   26.05ms    | 38.38       |    7.91ms    | 126.49      |
|cnn (CPU, 128x96)   |   15.06ms    | 66.38       |    4.50ms    | 222.28      |

* Minimal face size ~10x10
* Raspberry Pi 4 B, Broadcom BCM2835, Cortex-A72 (ARMv8) 64-bit SoC @ 1.5GHz
* Multi-thread in 4 threads and 4 processors.

## Performance on WIDER Face 
Run on default settings: scales=[1.], confidence_threshold=0.02, floating point:
```
AP_easy=0.887, AP_medium=0.871, AP_hard=0.768
```

## Author
* Shiqi Yu, <shiqi.yu@gmail.com>

## Contributors
All contributors who contribute at GitHub.com are listed [here](https://github.com/ShiqiYu/libfacedetection/graphs/contributors). 

The contributors who were not listed at GitHub.com:
* Jia Wu (吴佳)
* Dong Xu (徐栋)
* Shengyin Wu (伍圣寅)

## Acknowledgment
The work was partly supported by the Science Foundation of Shenzhen (Grant No. 20170504160426188).

## Citation

The master thesis of Mr. Wei Wu. All details of the algorithm are in the thesis. The thesis can be downloaded at [吴伟硕士毕业论文](wu-thesis-facedetect.pdf)
```
@thesis{wu2023thesisyunet,
    author      = {吴伟},
    title       = {面向边缘设备的高精度毫秒级人脸检测技术研究},
    type        = {硕士学位论文},
    institution = {南方科技大学},
    year        = {2023},
}
```

The paper for the main idea of this repository https://link.springer.com/article/10.1007/s11633-023-1423-y.

```
@article{wu2023miryunet,
	title     = {YuNet: A Tiny Millisecond-level Face Detector},
	author    = {Wu, Wei and Peng, Hanyang and Yu, Shiqi},
	journal   = {Machine Intelligence Research},
	pages     = {1--10},
	year      = {2023},
	doi       = {10.1007/s11633-023-1423-y},
	publisher = {Springer}
}
```

The survey paper on face detection to evaluate different methods. It can be open-accessed at https://ieeexplore.ieee.org/document/9580485
```
@article{feng2022face,
	author  = {Feng, Yuantao and Yu, Shiqi and Peng, Hanyang and Li, Yan-Ran and Zhang, Jianguo},
	journal = {IEEE Transactions on Biometrics, Behavior, and Identity Science}, 
	title   = {Detect Faces Efficiently: A Survey and Evaluations}, 
	year    = {2022},
	volume  = {4},
	number  = {1},
	pages   = {1-18},
	doi     = {10.1109/TBIOM.2021.3120412}
}
```

The loss used in training is EIoU, a novel extended IoU. The paper can be open-accessed at https://ieeexplore.ieee.org/document/9429909.
```
@article{peng2021eiou,
	author  = {Peng, Hanyang and Yu, Shiqi},
	journal = {IEEE Transactions on Image Processing}, 
	title   = {A Systematic IoU-Related Method: Beyond Simplified Regression for Better Localization}, 
	year    = {2021},
	volume  = {30},
	pages   = {5032-5044},
	doi     = {10.1109/TIP.2021.3077144}
}
```
