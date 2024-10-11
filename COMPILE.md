# How to compile the library

## Table of contents:
- [Compilation](#compilation)
  - Windows 10
     - [with Visual Studio 2019 (MSVC)](#windows-10-with-visual-studio-2019)
     - [with MinGW](#windows-10-with-mingw)
  - [Linux/Ubuntu](#linux-or-ubuntu)
  - [Android](#android)
  - [OpenCV DNN](#opencv-dnn)
  - [Cross build for aarch64](#cross-build-for-aarch64)

- [Usage](#usage)

- [Example](#example)

## Compilation
To use the library, you can do either of the following:
- Copy the .cpp files in src folder directly to your project's source folder, and then compile them with the other files in your project.
The source code are written in standard C/C++, so they should compile on any platform that supports C/C++;
- Or follow the steps below to generate dynamic/static libraries under different environments.
### Windows 10 with Visual Studio 2019
  0. Set up OpenCV with 4.51+ version.
  1. Download libfacedetection and then run powershell terminal <ins>as administrator</ins>:

            cd libfacedetection
            mkdir build
            cd build
            cmake .. -DCMAKE_INSTALL_PREFIX=install -DBUILD_SHARED_LIBS=ON -DCMAKE_BUILD_TYPE=Release -DDEMO=OFF
            cmake --build . --config Release
            cmake --build . --config Release --target install

      Dynamic library(facedetection.dll) is generated. Then, to generate static library(lib), you need to set the parameter `BUILD_SHARED_LIBS` to `OFF` with the above commands.
  2. To deploy the facedetection libraries in a Visual Studio C++ console app, in your console application's property pages, <ins>under Release mode</ins> (because the build type is Release), add the path of your `libfacedetection\build\install\include\facedetection` to VC++ Directories -> Include Directories and the path of your `libfacedetection\build\install\lib` to VC++ Directories -> Library Directories, and add `facedetection.lib` to Linker -> Input -> Additional Dependencies.

  3. Add `#include "facedetectcnn.h"` to your source files. See [code example built with Visual Studio](#visual-studio-example).

### Windows 10 with MinGW
  0. Set up OpenCV with 4.51+ version.
  1. Same as the step 1 above with Visual Studio. `libfacedetection.so` is built with `BUILD_SHARED_LIBS=ON` and `libfacedetecion.a` is built with the variable set to `OFF`. 
  2. Set the environment variable `facedetection_DIR` to `path\to\libfacedetection\build`.
  3. Use `find_package(facedetection)` in the CMakeLists of your project, or use `target_link_libraries( your-program /path/to/libfacedetection/build/install/lib/libfacedetection.so)`.

            find_package(facedetection)
            if(facedetection_FOUND)
            //your code
            endif()
  4. Add `#include "facedetectcnn.h"` to your source files to use the libraries.

### Linux or Ubuntu
  0. [Set up OpenCV](https://docs.opencv.org/4.5.2/d7/d9f/tutorial_linux_install.html) with 4.51+ version.
  1. Same as the step 1 above with Visual Studio. `libfacedetection.so` is built with `BUILD_SHARED_LIBS=ON` and `libfacedetecion.a` is built with the variable set to `OFF`. 
  2. Set the environment variable `facedetection_DIR` to `path\to\libfacedetection\build`.
  3. Use `target_link_libraries( your-program /path/to/libfacedetection/build/install/lib/libfacedetection.so)`in the CMakelists of your project to use the shared object.
  4. Add `#include "facedetectcnn.h"` to your source files to use the libraries. See [code example built with GNU on Linux/Ubuntu](#linux-or-ubuntu-example).

### Android
1. Install ndk
   - Download and install to /home/android-ndk from https://developer.android.com/ndk/downloads 
   - Setting environment variables

        export ANDROID_NDK=/home/android-ndk

2. Compile
   - The host is Linux / Ubuntu
     - Build

            mkdir build
            cd build
            cmake .. -DCMAKE_INSTALL_PREFIX=install \
                  -DCMAKE_BUILD_TYPE=MinSizeRel \
                  -DCMAKE_TOOLCHAIN_FILE=${ANDROID_NDK}/build/cmake/android.toolchain.cmake \
                  -DANDROID_ABI="arm64-v8a" \
                  -DANDROID_PLATFORM=android-18 \
                  -DUSE_OPENMP=OFF \
                  -DENABLE_NEON=ON \
                  -DENABLE_AVX2=OFF \
                  -DDEMO=OFF
            cmake --build . --config MinSizeRel

     - Install

            cmake --build . --config MinSizeRel --target install/strip

   - The host is Windows
      - Build

            mkdir build
            cd build
            cmake .. -DCMAKE_INSTALL_PREFIX=%cd%\install -G"Unix Makefiles" -DCMAKE_BUILD_TYPE=MinSizeRel -DCMAKE_TOOLCHAIN_FILE=%ANDROID_NDK%/build/cmake/android.toolchain.cmake -DCMAKE_MAKE_PROGRAM=%ANDROID_NDK%/prebuilt/windows-x86_64/bin/make.exe -DANDROID_ABI=arm64-v8a -DANDROID_ARM_NEON=ON -DANDROID_PLATFORM=android-24 -DUSE_OPENMP=OFF -DENABLE_NEON=ON -DENABLE_AVX2=OFF -DDEMO=OFF
            cmake --build . --config MinSizeRel

      - Install

            cmake --build . --config MinSizeRel --target install/strip
    
    - msys2 or cygwin
      - Build

            mkdir build
            cd build
            cmake .. -DCMAKE_INSTALL_PREFIX=install \
                  -G"Unix Makefiles" \
                  -DCMAKE_BUILD_TYPE=MinSizeRel \
                  -DCMAKE_TOOLCHAIN_FILE=${ANDROID_NDK}/build/cmake/android.toolchain.cmake \
                  -DCMAKE_MAKE_PROGRAM=${ANDROID_NDK}\prebuilt\windows-x86_64\bin\make.exe \
                  -DANDROID_ABI=arm64-v8a \
                  -DANDROID_ARM_NEON=ON \
                  -DUSE_OPENMP=OFF \
                  -DENABLE_NEON=ON \
                  -DENABLE_AVX2=OFF \
                  -DDEMO=OFF
            cmake --build . --config MinSizeRel

      - Install

            cmake --build . --config MinSizeRel --target install/strip

+ Parameter Description: https://developer.android.google.cn/ndk/guides/cmake
    + ANDROID_ABI: The following values can be taken:
       Goal ABI. If the target ABI is not specified, CMake uses armeabi-v7a by default.
       Valid ABI are:
      - armeabi：CPU with software floating point arithmetic based on ARMv5TE
      - armeabi-v7a：ARMv7-based device with hardware FPU instructions (VFP v3 D16)
      - armeabi-v7a with NEON：Same as armeabi-v7a, but with NEON floating point instructions enabled. This is equivalent to setting -DANDROID_ABI=armeabi-v7a and -DANDROID_ARM_NEON=ON.
      - arm64-v8a：ARMv8 AArch64 Instruction Set
      - x86：IA-32 Instruction Set
      - x86_64 - x86-64 Instruction Set
    + ANDROID_NDK <path> The path of installed ndk in host
    + ANDROID_PLATFORM: For a full list of platform names and corresponding Android system images, see the [Android NDK Native API] (https://developer.android.google.com/ndk/guides/stable_apis.html)
    + ANDROID_ARM_MODE
    + ANDROID_ARM_NEON
    + ANDROID_STL:Specifies the STL that CMake should use. 
      - c++_shared: use libc++ shared library
      - c++_static: use libc++ static library
      - none: no stl
      - system: use system STL 

### OpenCV DNN
- To deploy libfacedetection with the OpenCV DNN module and ONNX model, see
[face detection with OpenCV DNN](https://github.com/ShiqiYu/libfacedetection/tree/master/opencv_dnn).

### Cross build for aarch64

1. Set cross compiler for aarch64 (please refer to aarch64-toolchain.cmake).
2. Set opencv path since the example code depends on opencv.

```bash
cmake \
    -DENABLE_NEON=ON \
    -DCMAKE_BUILD_TYPE=RELEASE \
    -DCMAKE_TOOLCHAIN_FILE=../aarch64-toolchain.cmake \
     ..

make
```    


## Usage
Here is an example of how to use the face detection model in C++:
```C++
#include "facedetect.h"
#include <opencv2/opencv.hpp>

#define DETECT_BUFFER_SIZE 0x20000

int main()
{
    int * pResults = NULL;
    unsigned char * pBuffer = (unsigned char *)malloc(DETECT_BUFFER_SIZE);
    Mat image = imread(file_path);

    /**
     The function that loads the face detection model.
     
     @param result_buffer Buffer memory for storing face detection results, whose size must be 0x20000 * bytes.
     @param rgb_image_data Input image, which must be BGR (three channels) instead of RGB image.
     @param width The width of the input image.
     @param height The height.
     @param step The step.
     @return An int pointer reflecting the face detection result, see Example for detailed usage.
    */
    int * pResults = facedetect_cnn(pBuffer, (unsigned char*)(image.ptr(0)), image.cols, image.rows, (int)image.step);
}
```

## Example

+ To build the ./example of libfacedetection:

  - Tips:
    * Please add facedetection_export.h file in the position where you copy your facedetectcnn.h files, add #define FACEDETECTION_EXPORT to  facedetection_export.h file. See: [issues #222](https://github.com/ShiqiYu/libfacedetection/issues/222)
    * Please add -O3 to turn on optimizations when you compile the source code using g++.
    * Please choose 'Maximize Speed/-O2' when you compile the source code using Microsoft Visual Studio.
    * You can enable OpenMP to speedup. But the best solution is to call the detection function in different threads.

  ### Linux or Ubuntu example

  If using Linux/Ubuntu, you can:
    
    0. [Generate libfacedetecion.so](#linux-or-ubuntu);
    1. Add CMakeLists.txt:

        ```
        cmake_minimum_required( VERSION 2.8 )
        project( example )
        find_package( OpenCV REQUIRED )
        message(STATUS "OpenCV_LIBS = ${OpenCV_LIBS}")
        include_directories( ${OpenCV_INCLUDE_DIRS} )
        add_executable( detect-image detect-image.cpp )
        add_executable( detect-camera detect-camera.cpp )
        target_link_libraries( detect-image ${OpenCV_LIBS} )
        target_link_libraries( detect-image /libfacedetection/build/install/lib/libfacedetection.so )
        target_link_libraries( detect-image /opencv/build/lib/libopencv_highgui.so )
        target_link_libraries( detect-image /opencv/build/lib/libopencv_imgproc.so )
        target_link_libraries( detect-image /opencv/build/lib/libopencv_core.so )
        target_link_libraries( detect-image /opencv/build/lib/libopencv_imgcodecs.so )
        target_link_libraries( detect-camera ${OpenCV_LIBS} )
        target_link_libraries( detect-camera /libfacedetection/build/install/lib/libfacedetection.so )
        target_link_libraries( detect-camera /opencv/build/lib/libopencv_highgui.so )
        target_link_libraries( detect-camera /opencv/build/lib/libopencv_video.so )
        target_link_libraries( detect-camera /opencv/build/lib/libopencv_imgproc.so )
        target_link_libraries( detect-camera /opencv/build/lib/libopencv_core.so )
        target_link_libraries( detect-camera /opencv/build/lib/libopencv_videoio.so )
        ```
            
    2. CMake and make:
       
        ```
        cd example
        mkdir build
        cd build
        cmake ..
        make
        // detect using an image
        ./detect-image <path to image>
        // or detect using camera
        ./detect-camera <camera-index>
        ```
  ### Visual Studio example

  If using Visual Studio 2019, you can:
    
    0. [Generate facedetection.lib](#windows-10-with-visual-studio-2019) as well as facedetection.dll (to avoid errors);
    1. You can either: 
       - Add a similar CMakeLists, but instead linking the project to *.lib and compile the folder as a whole into a solution(.sln) that opens in Visual Studio. 
    
       OR

       - Create a new project with C++ Console App template. Go to Project->Properties and select Release in Configuration, x64 in Platform, then do the following: add the path of your `libfacedetection\build\install\include\facedetection` (as well as your OpenCV include path) to VC++ Directories -> Include Directories and the path of your `libfacedetection\build\install\lib` (as well as your OpenCV lib path)to VC++ Directories -> Library Directories, and add `facedetection.lib` and other necessary dependencies to Linker -> Input -> Additional Dependencies.
       - Add one file in the example folder to the project's Source folder. To build another example, you can right-click the current Solution in the Solution Explorer, Add->New Project and follow the above step (or use property manager to copy-paste Property Sheet). 
       
    2. Build the solution and run the powershell terminal in Visual Studio:
        ```
        cd x64/Release
        // detect using an image
        ./detect-image <path/to/your/image/file>
        // or detect using camera
        ./detect-camera <camera-index>
        ```


  
+ Sample output of detect-image
    ![Examples](/images/cnnresult.png "Detection example")

+ Third-party examples

  - FaceRecognizer: https://github.com/KangLin/FaceRecognizer  
    This is a cross-platforms program. It has supported windows, linux, android, etc.
