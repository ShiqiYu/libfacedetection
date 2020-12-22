# How to compile the library

You can use the library.  
Step:

- Complie it

```bash
mkdir build
cd build
cmake .. -DCMAKE_INSTALL_PREFIX=install -DBUILD_SHARED_LIBS=ON -DCMAKE_BUILD_TYPE=Release -DDEMO=OFF
cmake --build . --config Release
cmake --build . --config Release --target install
```

Dynamic libraries are generated.
If you want to generate static libraries,
you need to set the parameter: BUILD_SHARED_LIBS = OFF

- Use find_package(facedetection) in CMakeLists of your project

```
find_package(facedetection)
if(facedetection_FOUND)
    # your code
endif()
```

## Example

- Examples of this project  
If you want to run the examples. you must be isntalled opencv library and set OpenCV_DIR first, then run the command:

```bash
mkdir build
cd build
cmake .. -DDEMO=ON -DOpenCV_DIR=
cmak --build . 
```

- Third-party examples

  - FaceRecognizer: https://github.com/KangLin/FaceRecognizer  
    It's a cross-platforms programe. It's supported windows, linux, android etc.

## Use Tengine to Speedup the detection on ARM

The model has been added to [Tengine](https://github.com/OAID/Tengine). Tengine, developed by OPEN AI LAB, is a lite, high-performance, and modular inference engine for embedded device. 

The model in Tengine can run faster than the C++ source code here because Tengine has been optimized according to ARM CPU. There are detailed manual and example at Tengine web site: https://github.com/OAID/Tengine/tree/master/examples/YuFaceDetectNet

## Cross build for aarch64

1. Set cross compiler for aarch64 (please refer to aarch64-toolchain.cmake)
2. Set opencv path since the example code depends on opencv

```bash
cmake \
    -DENABLE_NEON=ON \
    -DCMAKE_BUILD_TYPE=RELEASE \
    -DCMAKE_TOOLCHAIN_FILE=../aarch64-toolchain.cmake \
     ..

make
```

## Native build for avx2

```bash
cmake \
    -DENABLE_AVX2=ON \
    -DCMAKE_BUILD_TYPE=RELEASE \
    -DDEMO=ON \
     ..

make
```

## Android platform compilation instructions

+ Install ndk
  - Download and install to /home/android-ndk from https://developer.android.com/ndk/downloads 
  - Setting environment variables

        export ANDROID_NDK=/home/android-ndk

+ Complie
  - The host is linux
    - Compile

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

    - Installation

            cmake --build . --config MinSizeRel --target install/strip

  - The host is windows
    - cmd
      - Compile

            mkdir build
            cd build
            cmake .. -DCMAKE_INSTALL_PREFIX=%cd%\install ^
                  -G"Unix Makefiles" ^
                  -DCMAKE_BUILD_TYPE=MinSizeRel ^
                  -DCMAKE_TOOLCHAIN_FILE=%ANDROID_NDK%/build/cmake/android.toolchain.cmake ^
                  -DCMAKE_MAKE_PROGRAM=%ANDROID_NDK%/prebuilt/windows-x86_64/bin/make.exe ^
                  -DANDROID_ABI=arm64-v8a ^
                  -DANDROID_ARM_NEON=ON ^
                  -DANDROID_PLATFORM=android-24 ^
                  -DUSE_OPENMP=OFF ^
                  -DENABLE_NEON=ON ^
                  -DENABLE_AVX2=OFF ^
                  -DDEMO=OFF
            cmake --build . --config MinSizeRel

      - Installation

            cmake --build . --config MinSizeRel --target install/strip
    
    - msys2 or cygwin
      - Compile

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

      - Installation

            cmake --build . --config MinSizeRel --target install/strip

  - Parameter Description: https://developer.android.google.cn/ndk/guides/cmake
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
      
