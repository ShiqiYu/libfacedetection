libfacedetection
====================

This is a binary library for face detection in images. 
The 32-bits dll file is provided for Visual Studio 2010.

examples/libfacedetect-example.cpp shows how to use the library.

Comparison:
-------------

| Method | Time   | FPS  | Misc   |
|--------|--------|------|--------|
|OpenCV  | 21.2ms | 47.2 | Yaw angle: -40 to 40 degrees. Classifier: haarcascade_frontalface_alt.xml |
|frontal |  5.2ms | 193.3 | Yaw angle: -60 to 60 degrees|
|multiview|  17.4ms | 57.5 | Yaw angle: -90 to 90 degrees |
|multiview_reinforce|  35.2ms | 28.4 | Yaw angle: -90 to 90 degrees |

* 640x480 image size (VGA), scale=1.2, minimal window size = 48
* Intel(R) Core(TM) i7-4770 CPU @ 3.4GHz
* Multi-core parallelization is enabled for the four methods.
 
Author:
-------------
* Shiqi Yu <shiqi.yu@gmail.com> Computer Vision Institute, Shenzhen University, China

Contributors:
-------------
* Shengyin Wu, Shenzhen University, China
* Dong Xu, Shenzhen University, China
