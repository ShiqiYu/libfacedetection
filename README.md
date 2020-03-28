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


## CNN-based Face Detection on Windows


| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |  X64         |X64          |  X64         |X64          |
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|cnn (CPU, 640x480)  |  58.03ms     |  17.23      | 13.85ms      |   72.20     |
|cnn (CPU, 320x240)  |  14.18ms     |  70.51      |  3.38ms      |  296.21     |
|cnn (CPU, 160x120)  |   3.25ms     | 308.15      |  0.82ms      | 1226.56     |
|cnn (CPU, 128x96)   |   2.11ms     | 474.38      |  0.52ms      | 1929.60     |

* Minimal face size ~10x10
* Intel(R) Core(TM) i7-1065G7 CPU @ 1.3GHz

<!--
## CNN-based Face Detection on ARM Linux (Raspberry Pi 3 B+)

(To be updated)

| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|cnn (CPU, 640x480)  |  512.04ms    |  1.95       |  174.89ms    |   5.72      |
|cnn (CPU, 320x240)  |  123.47ms    |  8.10       |   42.13ms    |  23.74      |
|cnn (CPU, 160x120)  |   27.42ms    | 36.47       |    9.75ms    | 102.58      |
|cnn (CPU, 128x96)   |   17.78ms    | 56.24       |    6.12ms    | 163.50      |

* Face detection only, and no landmark detection included.
* Minimal face size ~10x10
* Raspberry Pi 3 B+, Broadcom BCM2837B0, Cortex-A53 (ARMv8) 64-bit SoC @ 1.4GHz
-->

## Performance on WIDER Face
Run on default settings: scales=[1.], confidence_threshold=0.3, floating point:
```
AP_easy=0.849, AP_medium=0.816, AP_hard=0.601
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
The work is partly supported by the Science Foundation of Shenzhen (Grant No. 20170504160426188).
