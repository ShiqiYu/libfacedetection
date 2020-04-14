#!/bin/bash

if [ -n "$1" ]; then
    ANDROID_NDK=$1
fi
if [ -z "${ANDROID_NDK}" ]; then
    echo "$0 ANDROID_NDK"
    exit -1
fi

if [ -z "${ANDROID_STL}" ]; then
   ANDROID_STL=c++_static
fi

if [ ! -d build_android ]; then
    mkdir -p build_android
fi
cd build_android

cmake .. -G"Unix Makefiles" -DCMAKE_INSTALL_PREFIX=install \
    -DCMAKE_BUILD_TYPE=Release \
    -DCMAKE_VERBOSE_MAKEFILE=TRUE \
    -DANDROID_ABI="arm64-v8a" \
    -DANDROID_ARM_NEON=ON \
    -DANDROID_PLATFORM=android-24 \
    -DCMAKE_TOOLCHAIN_FILE=${ANDROID_NDK}/build/cmake/android.toolchain.cmake \
    -DANDROID_STL=${ANDROID_STL} \
    -DENABLE_NEON=ON \
    -DENABLE_AVX2=OFF

cmake --build . --config Release -- -j`cat /proc/cpuinfo |grep 'cpu cores' |wc -l`

cmake --build . --config Release --target install/strip -- -j`cat /proc/cpuinfo |grep 'cpu cores' |wc -l`

cd ..
