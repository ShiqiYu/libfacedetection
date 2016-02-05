libfacedetection
====================

This is a binary library for face detection in images. 
The 32-bits dll file is provided for Visual Studio 2010.

examples/libfacedetect-example.cpp shows how to use the library.

Comparison on Windows
-------------

| Method | Time   | FPS  | Misc   |
|--------|--------|------|--------|
|OpenCV  | 21.2ms | 47.2 | Yaw angle: -40 to 40 degrees. Classifier: haarcascade_frontalface_alt.xml |
|frontal |  4.3ms | 232.6 | Yaw angle: -60 to 60 degrees|
|multiview|  10.5ms | 95.2 | Yaw angle: -90 to 90 degrees |
|multiview_reinforce|  15.6ms | 64.1 | Yaw angle: -90 to 90 degrees |

* 640x480 image size (VGA), scale=1.2, minimal window size = 48
* Intel(R) Core(TM) i7-4770 CPU @ 3.4GHz
* Multi-core parallelization is enabled for the four methods.

Comparison on ARM
-------------

| Method | Time   | FPS  | Misc   |
|--------|--------|------|--------|
|frontal |  4.3ms | 232.6 | Yaw angle: -60 to 60 degrees|
|multiview|  10.5ms | 95.2 | Yaw angle: -90 to 90 degrees |
|multiview_reinforce|  15.6ms | 64.1 | Yaw angle: -90 to 90 degrees |

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
* the heights of the face rectangles are scaled to 1.2.

Author
-------------
* Shiqi Yu <shiqi.yu@gmail.com> Computer Vision Institute, Shenzhen University, China

Contributors
-------------
* Shengyin Wu, Shenzhen University, China
* Dong Xu, Shenzhen University, China
