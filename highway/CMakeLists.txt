cmake_minimum_required(VERSION 3.15)

project(fdt_hw LANGUAGES CXX)

option(FDT_HW_BUILD_TESTS "Build hw doctest tests" ON)
option(FDT_HW_BUILD_BENCHMARKS "Build hw benchmarks" ON)
option(FDT_HW_BUILD_IMAGE_BENCHMARK "Build OpenCV image benchmark" ON)
option(FDT_HW_ENABLE_PROFILER "Enable per-stage profiling" ON)
option(FDT_HW_ENABLE_INTRINSICS_COMPARE "Build AVX2 intrinsics comparison kernels when available" ON)
option(FDT_HW_ENABLE_HYBRID_CEILING "Use fastest available non-pointwise kernels in the hw pipeline" ON)
set(FDT_HW_KERNEL_MODE "primitive" CACHE STRING "hw kernel mode: primitive or packed")
set_property(CACHE FDT_HW_KERNEL_MODE PROPERTY STRINGS primitive packed)
set(FDT_HW_HIGHWAY_ROOT "E:/projects/thirdParty/lib/highway-1.3.0" CACHE PATH
    "Root directory containing the prebuilt Highway 1.3.0 variants")
set(FDT_HW_OPENCV_ROOT "E:/projects/thirdParty/lib/opencv-4.5.1" CACHE PATH
    "Root directory containing optional OpenCV variants for image benchmark")

find_package(hwy 1.3.0 CONFIG QUIET)
if(NOT hwy_FOUND AND EXISTS "${FDT_HW_HIGHWAY_ROOT}")
    foreach(_fdt_hw_variant IN ITEMS
        vs2022-x64-release-static
        vs2022-x64-release
        vs2022-x64-debug-static
        vs2022-x64-debug
    )
        set(_fdt_hw_candidate
            "${FDT_HW_HIGHWAY_ROOT}/${_fdt_hw_variant}/lib/cmake/hwy")
        if(EXISTS "${_fdt_hw_candidate}/hwy-config.cmake")
            set(hwy_DIR "${_fdt_hw_candidate}" CACHE PATH "Path to hwy-config.cmake" FORCE)
            find_package(hwy 1.3.0 CONFIG QUIET)
            if(hwy_FOUND)
                set(FDT_HW_HIGHWAY_VARIANT "${_fdt_hw_variant}")
                break()
            endif()
        endif()
    endforeach()
endif()
if(NOT hwy_FOUND)
    find_package(HWY 1.3.0 CONFIG REQUIRED)
endif()

if(TARGET hwy::hwy)
    set(FDT_HW_HIGHWAY_TARGET hwy::hwy)
elseif(TARGET hwy)
    set(FDT_HW_HIGHWAY_TARGET hwy)
else()
    message(FATAL_ERROR "Highway was found, but no known hwy target is available")
endif()

add_library(fdt_hw_kernels
    ${CMAKE_CURRENT_SOURCE_DIR}/../src/facedetectcnn-data.cpp
    src/facedetect_hw.cpp
    src/hw_blob.cpp
    src/hw_filter.cpp
    src/hw_image.cpp
    src/hw_layers.cpp
    src/hw_model.cpp
    src/hw_network.cpp
    src/hw_postprocess.cpp
    src/hw_kernels_scalar.cpp
    src/hw_kernels_intrinsics.cpp
    src/hw_kernels_hw.cpp
)
target_include_directories(fdt_hw_kernels
    PUBLIC
        ${CMAKE_CURRENT_SOURCE_DIR}/include
        ${CMAKE_CURRENT_SOURCE_DIR}/src
    PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/../src
)
target_link_libraries(fdt_hw_kernels PUBLIC ${FDT_HW_HIGHWAY_TARGET})
target_compile_features(fdt_hw_kernels PUBLIC cxx_std_11)
if(FDT_HW_ENABLE_HYBRID_CEILING)
    target_compile_definitions(fdt_hw_kernels PRIVATE FDT_HW_ENABLE_HYBRID_CEILING=1)
endif()

if(MSVC)
    target_compile_options(fdt_hw_kernels PRIVATE /W4 /permissive-)
    if(FDT_HW_ENABLE_INTRINSICS_COMPARE)
        target_compile_options(fdt_hw_kernels PRIVATE /arch:AVX2)
    endif()
else()
    target_compile_options(fdt_hw_kernels PRIVATE -Wall -Wextra -Wpedantic)
    if(FDT_HW_ENABLE_INTRINSICS_COMPARE)
        target_compile_options(fdt_hw_kernels PRIVATE -mavx2 -mfma)
    endif()
endif()

if(FDT_HW_BUILD_TESTS)
    add_executable(fdt_hw_tests
        ${CMAKE_CURRENT_SOURCE_DIR}/../src/facedetectcnn.cpp
        tests/hw_test_kernels.cpp
    )
    target_include_directories(fdt_hw_tests PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/externals
        ${CMAKE_CURRENT_SOURCE_DIR}/../externals
        ${CMAKE_CURRENT_SOURCE_DIR}/../src
    )
    target_link_libraries(fdt_hw_tests PRIVATE fdt_hw_kernels)
    target_compile_features(fdt_hw_tests PRIVATE cxx_std_11)
endif()

if(FDT_HW_BUILD_BENCHMARKS)
    add_executable(fdt_hw_benchmark
        benchmark/hw_benchmark.cpp
    )
    target_link_libraries(fdt_hw_benchmark PRIVATE fdt_hw_kernels)
    target_compile_features(fdt_hw_benchmark PRIVATE cxx_std_11)
endif()

if(FDT_HW_BUILD_IMAGE_BENCHMARK)
    find_package(OpenCV QUIET COMPONENTS core imgcodecs)
    if(NOT OpenCV_FOUND AND EXISTS "${FDT_HW_OPENCV_ROOT}")
        foreach(_fdt_hw_opencv_variant IN ITEMS
            vs2022-x64-release
            vs2022-x64-debug
            vs2022-x64-release-static
            vs2022-x64-debug-static
        )
            foreach(_fdt_hw_opencv_suffix IN ITEMS "" lib staticlib)
                set(_fdt_hw_opencv_candidate
                    "${FDT_HW_OPENCV_ROOT}/${_fdt_hw_opencv_variant}/${_fdt_hw_opencv_suffix}")
                if(EXISTS "${_fdt_hw_opencv_candidate}/OpenCVConfig.cmake")
                    set(OpenCV_DIR "${_fdt_hw_opencv_candidate}" CACHE PATH
                        "Path to OpenCVConfig.cmake" FORCE)
                    find_package(OpenCV QUIET COMPONENTS core imgcodecs)
                    if(OpenCV_FOUND)
                        set(FDT_HW_OPENCV_VARIANT "${_fdt_hw_opencv_variant}")
                        break()
                    endif()
                endif()
            endforeach()
            if(OpenCV_FOUND)
                break()
            endif()
        endforeach()
    endif()

    if(OpenCV_FOUND)
        add_executable(fdt_hw_image_benchmark
            ${CMAKE_CURRENT_SOURCE_DIR}/../src/facedetectcnn.cpp
            ${CMAKE_CURRENT_SOURCE_DIR}/../src/facedetectcnn-model.cpp
            benchmark/hw_image_benchmark.cpp
        )
        target_include_directories(fdt_hw_image_benchmark PRIVATE
            ${CMAKE_CURRENT_SOURCE_DIR}/../src
            ${OpenCV_INCLUDE_DIRS}
        )
        target_link_libraries(fdt_hw_image_benchmark PRIVATE
            fdt_hw_kernels
            ${OpenCV_LIBS}
        )
        target_compile_features(fdt_hw_image_benchmark PRIVATE cxx_std_11)
    else()
        message(STATUS "OpenCV not found; fdt_hw_image_benchmark disabled")
    endif()
endif()

message(STATUS "FDT_HW_HIGHWAY_TARGET = ${FDT_HW_HIGHWAY_TARGET}")
message(STATUS "FDT_HW_HIGHWAY_VARIANT = ${FDT_HW_HIGHWAY_VARIANT}")
message(STATUS "FDT_HW_OPENCV_VARIANT = ${FDT_HW_OPENCV_VARIANT}")
message(STATUS "FDT_HW_KERNEL_MODE = ${FDT_HW_KERNEL_MODE}")
message(STATUS "FDT_HW_ENABLE_INTRINSICS_COMPARE = ${FDT_HW_ENABLE_INTRINSICS_COMPARE}")
message(STATUS "FDT_HW_ENABLE_HYBRID_CEILING = ${FDT_HW_ENABLE_HYBRID_CEILING}")
