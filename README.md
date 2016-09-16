libfacedetection
====================

This is a binary library for face detection in images. 
The 32-bit and 64-bit dll files are provided.


examples/libfacedetect-example.cpp shows how to use the library.

Comparison on Windows
-------------

| Method | Time   | FPS  | Misc   |
|--------|--------|------|--------|
|OpenCV  | 21.2ms | 47.2 | Yaw angle: -40 to 40 degrees. Classifier: haarcascade_frontalface_alt.xml |
|frontal |  2.7ms | 370.4 | Yaw angle: -60 to 60 degrees|
|frontal-surveillance|  3.8ms | 263.2 | Yaw angle: -70 to 70 degrees |
|multiview|  6.6ms | 151.5 | Yaw angle: -90 to 90 degrees |
|multiview_reinforce|  9.5ms | 105.3 | Yaw angle: -90 to 90 degrees |

* 640x480 image size (VGA), scale=1.2, minimal window size = 48
* Intel(R) Core(TM) i7-4770 CPU @ 3.4GHz
* Multi-core parallelization is enabled for the four methods.

Comparison on iPhone(+)
-------------

| Method | Time (iPhoneSE)   | FPS (iPhoneSE) | Time (iPhone5S) | FPS (iPhone5S) | Misc   |
|--------|--------|------|--------|------|--------|
|frontal |  14.9ms | 67.1 | 23.8 | 42.0 | Yaw angle: -60 to 60 degrees|
|multiview|  47.3ms | 21.1 | 75.1 | 13.3 |Yaw angle: -90 to 90 degrees |
|multiview_reinforce|  83.2ms | 12.0 | 132.4 | 7.6 | Yaw angle: -90 to 90 degrees |

* 640x480 image size (VGA), scale=1.2, minimal window size = 48
* Multi-core parallelization is disabled.
* C programming language, and no SIMD instruction is used.
(+) The algorithm tested on iPhone is an old one. Currrent alorithm can gain ~40% speed improvement.


Comparison on ARM
-------------

| Method | Time   | FPS  | Misc   |
|--------|--------|------|--------|
|frontal |  21.5ms | 46.5 | Yaw angle: -60 to 60 degrees|
|frontal-surveillance|  36.5ms | 27.4 | Yaw angle: -70 to 70 degrees |
|multiview|  67.1ms | 14.9 | Yaw angle: -90 to 90 degrees |
|multiview_reinforce|  112.6ms | 8.9 | Yaw angle: -90 to 90 degrees |

* 640x480 image size (VGA), scale=1.2, minimal window size = 48
* NVIDIA TK1 "4-Plus-1" 2.32GHz ARM quad-core Cortex-A15 CPU
* Multi-core parallelization is disabled.
* C programming language, and no SIMD instruction is used.

The dll cannot run on ARM. The library should be recompiled from source code for ARM compatibility. If you need the source code, a commercial license is needed.

Evaluation
-------------
FDDB: http://vis-www.cs.umass.edu/fddb/index.html

![Evaluation on FDDB](https://github.com/ShiqiYu/libfacedetection/blob/master/FDDB-results-of-3functions.png "Evaluation on FDDB")

* scale=1.08
* minimal window size = 16
* the heights of the face rectangles are scaled to 1.2 to fit the ground truth data in FDDB.

Tip
-------------
* Do NOT use the functions in multiple threads. The memory used in the functions is not protected according to multiple threads.

Author
-------------
* Shiqi Yu <shiqi.yu@gmail.com> Computer Vision Institute, Shenzhen University, China

Contributors
-------------
* Shengyin Wu, Shenzhen University, China
* Dong Xu, Shenzhen University, China
