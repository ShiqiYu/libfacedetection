# libfacedetection

This is an open source library for CNN-based face detection in images. The CNN model has been converted to static variables in C source files. The source code does not depend on any other libraries. What you need is just a C++ compiler. You can compile the source code under Windows, Linux, ARM and any platform with a C++ compiler.

SIMD instructions are used to speed up the detection. You can enable AVX2 if you use Intel CPU or NEON for ARM.

The model file has also been provided in directory ./models/.

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

<!--
| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |  X64         |X64          |  X64         |X64          |
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|cnn (CPU, 640x480)  |  58.03ms     |  17.23      | 13.85ms      |   72.20     |
|cnn (CPU, 320x240)  |  14.18ms     |  70.51      |  3.38ms      |  296.21     |
|cnn (CPU, 160x120)  |   3.25ms     | 308.15      |  0.82ms      | 1226.56     |
|cnn (CPU, 128x96)   |   2.11ms     | 474.38      |  0.52ms      | 1929.60     |
-->
| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |  X64         |X64          |  X64         |X64          |
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|cnn (CPU, 640x480)  |  58.06ms.    |  17.22      |  12.93ms     |   77.34     |
|cnn (CPU, 320x240)  |  13.77ms     |  72.60      |   3.19ms     |  313.14     |
|cnn (CPU, 160x120)  |   3.26ms     | 306.81      |   0.77ms     | 1293.99     |
|cnn (CPU, 128x96)   |   1.41ms     | 711.69      |   0.49ms     | 2027.74     |

* Minimal face size ~10x10
* Intel(R) Core(TM) i7-1065G7 CPU @ 1.3GHz


## CNN-based Face Detection on ARM Linux (Raspberry Pi 4 B)

| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|cnn (CPU, 640x480)  |  492.99ms    |  2.03       |  149.66ms    |   6.68      |
|cnn (CPU, 320x240)  |  116.43ms    |  8.59       |   34.19ms    |  29.25      |
|cnn (CPU, 160x120)  |   27.91ms    | 35.83       |    8.43ms    | 118.64      |
|cnn (CPU, 128x96)   |   17.94ms    | 55.74       |    5.24ms    | 190.82      |

<!-- * Face detection only, and no landmark detection included. -->
* Minimal face size ~10x10
* Raspberry Pi 4 B, Broadcom BCM2835, Cortex-A72 (ARMv8) 64-bit SoC @ 1.5GHz


## Performance on WIDER Face 
Run on default settings: scales=[1.], confidence_threshold=0.3, floating point:
```
AP_easy=0.834, AP_medium=0.824, AP_hard=0.708
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
The loss used in [model training](https://github.com/ShiqiYu/libfacedetection.train) is EIoU, a novel extended IoU. More details can be found in:

	@article{eiou,
	 title={A Systematic IoU-Related Method: Beyond Simplified Regression for Better Localization},
	 author={Hanyang Peng and Shiqi Yu},
	 journal={IEEE Transactions on Image Processing},
	 year={2021}
	 }

The paper can be downloaded at https://ieeexplore.ieee.org/document/9429909
