# libfacedetection

This is a binary library for face detection and face landmark detection in images. 

examples/libfacedetectcnn-example.cpp shows how to use the library.

![Examples](/images/cnnresult.png "Detection example")


CNN-based Face Detection on Windows
-------------

| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |  X64         |X64          |  X64         |X64          |
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|OpenCV Haar+AdaBoost (640x480)|   --         | --          | 12.33ms      |   81.1      |
|cnn (CPU, 640x480)  |  69.03ms     | 14.49       | 16.47ms      |   60.72     |
|cnn (CPU, 320x240)  |  16.54ms     | 60.46       |  4.15ms      |  241.00     |
|cnn (CPU, 160x120)  |   3.79ms     | 263.65      |  1.01ms      |  989.98     |
|cnn (CPU, 128x96)   |   2.53ms     | 395.29      |  0.71ms      | 1399.28     |

* OpenCV Haar+AdaBoost runs with minimal face size 48x48
* Face detection only, and no landmark detection included.
* Minimal face size ~10x10
* Intel(R) Core(TM) i7-7700 CPU @ 3.6GHz.

CNN-based Face Detection on ARM Linux (Raspberry Pi 3 B+)
-------------

| Method             |Time          | FPS         |Time          | FPS         |
|--------------------|--------------|-------------|--------------|-------------|
|                    |Single-thread |Single-thread|Multi-thread  |Multi-thread |
|cnn (CPU, 640x480)  |  593.86ms    |  1.68       |  183.85ms    |   5.44      |
|cnn (CPU, 320x240)  |  140.50ms    |  7.12       |   45.48ms    |  21.99      |
|cnn (CPU, 160x120)  |   30.15ms    | 33.17       |   10.75ms    |  92.99      |
|cnn (CPU, 128x96)   |   20.20ms    | 49.49       |    6.73ms    | 148.53      |

* Face detection only, and no landmark detection included.
* Minimal face size ~10x10
* Raspberry Pi 3 B+, Broadcom BCM2837B0, Cortex-A53 (ARMv8) 64-bit SoC @ 1.4GHz


The dll cannot run on ARM. The library should be recompiled from source code for ARM compatibility. If you need the source code, a commercial license is needed.


Author
-------------
* Shiqi Yu, <shiqi.yu@gmail.com>

Contributors
-------------
* Jia Wu
* Shengyin Wu
* Dong Xu
