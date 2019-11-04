# libfacedetection

This is an open source library for CNN-based face detection in images. The CNN model has been converted to static variables in C source files. The source code does not depend on any other libraries. What you need is just a C++ compiler. You can compile the source code under Windows, Linux, ARM and any platform with a C++ compiler.

SIMD instructions are used to speed up the detection. You can enable AVX2 if you use Intel CPU or NEON for ARM.

The model file has also been provided in directory ./models/.

examples/libfacedetectcnn-example.cpp shows how to use the library.

![Examples](/images/cnnresult.png "Detection example")

## How to use the code

You can copy the files in directory src/ into your project, and compile them as the other files in your project. The source code is written in standard C/C++. It should be compiled at any platform which support C/C++.

Some tips:
* Please add -O3 to turn on optimizations when you compile the source code using g++.
* Please choose 'Maximize Speed/-O2' when you compile the source code using Microsoft Visual Studio.
* ENABLE_INT8=ON is recommended for ARM, but it is not recommended for Intel CPU since it cannot gain better speed sometime even worse.
* The source code can only run in single thread. If you want to run parally, you can call the face detection function in multiple threads. Yes, multiple-thread is complex in programming.
* If you want to achieve best performance, you can run the model (not the source code) using OpenVINO on Intel CPU or Tengine on ARM CPU.

If you want to compile and run the example, you can create a build folder first, then run the command:

```
mkdir build; cd build; rm -rf *
```

### Use Tengine to Speedup the detection on ARM
The model has been added to [Tengine](https://github.com/OAID/Tengine). Tengine, developed by OPEN AI LAB, is a lite, high-performance, and modular inference engine for embedded device. 

The model in Tengine can run faster than the C++ source code here because Tengine has been optimized according to ARM CPU. There are detailed manual and example at Tengine web site: https://github.com/OAID/Tengine/tree/master/examples/YuFaceDetectNet

### Cross build for aarch64
1. Set cross compiler for aarch64 (please refer to aarch64-toolchain.cmake)
2. Set opencv path since the example code depends on opencv

```
cmake \
    -DENABLE_INT8=ON \
    -DENABLE_NEON=ON \
    -DCMAKE_BUILD_TYPE=RELEASE \
    -DCMAKE_TOOLCHAIN_FILE=../aarch64-toolchain.cmake \
     ..

make
```

### Native build for avx2
```
cmake \
    -DENABLE_AVX2=ON \
    -DCMAKE_BUILD_TYPE=RELEASE \
    -DDEMO=ON \
     ..

make
```

## CNN-based Face Detection on Windows


| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |  X64         |X64          |  X64         |X64          |
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|OpenCV Haar+AdaBoost (640x480)|   --         | --          | 12.33ms      |   81.1      |
|cnn (CPU, 640x480)  |  64.55ms     | 15.49       | 15.78ms      |  63.36      |
|cnn (CPU, 320x240)  |  15.48ms     | 64.60       |  3.92ms      |  255.01     |
|cnn (CPU, 160x120)  |   3.86ms     | 259.01      |  1.07ms      |  938.71     |
|cnn (CPU, 128x96)   |   2.46ms     | 406.33      |  0.68ms      | 1479.79     |

* OpenCV Haar+AdaBoost runs with minimal face size 48x48
* Face detection only, and no landmark detection included
* Minimal face size ~10x10
* Intel(R) Core(TM) i7-7700 CPU @ 3.6GHz

## CNN-based Face Detection on ARM Linux (Raspberry Pi 3 B+)

(* to be updated)
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


## Author
* Shiqi Yu, <shiqi.yu@gmail.com>

## Contributors
Some contributors are listed [here](https://github.com/ShiqiYu/libfacedetection/graphs/contributors). 

The contributors who are not listed at GitHub.com:
* Jia Wu (吴佳)
* Dong Xu (徐栋)
* Shengyin Wu (伍圣寅)

## Acknowledgment
The work is partly supported by the Science Foundation of Shenzhen (Grant No. JCYJ20150324141711699 and 20170504160426188).
