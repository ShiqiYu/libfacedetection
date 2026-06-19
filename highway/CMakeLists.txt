cmake_minimum_required(VERSION 3.15)

project(fdt_hw LANGUAGES CXX)

include(GNUInstallDirs)

option(FDT_HW_BUILD_TESTS "Build hw doctest tests" ON)
option(FDT_HW_BUILD_BENCHMARKS "Build hw benchmarks" ON)
option(FDT_HW_BUILD_IMAGE_BENCHMARK "Build OpenCV image benchmark" ON)
option(FDT_HW_FORCE_SCALAR "Force scalar kernels in the hw pipeline" OFF)
option(FDT_HW_ENABLE_INTRINSICS_COMPARE "Build AVX2 intrinsics comparison kernels when available" ON)
option(FDT_HW_ENABLE_HYBRID_CEILING "Use fastest available non-pointwise kernels in the hw pipeline" ON)
set(FDT_HW_X86_ARCH "DEFAULT" CACHE STRING "x86 compile ISA: DEFAULT, AVX2, or AVX512")
set_property(CACHE FDT_HW_X86_ARCH PROPERTY STRINGS DEFAULT AVX2 AVX512)
string(TOUPPER "${FDT_HW_X86_ARCH}" FDT_HW_X86_ARCH_UPPER)

SET(BUILD_VERSION "v0.0.3")

string(TOLOWER "${CMAKE_SYSTEM_PROCESSOR}" FDT_HW_SYSTEM_PROCESSOR_LOWER)
set(FDT_HW_X86_TARGET OFF)
if(FDT_HW_SYSTEM_PROCESSOR_LOWER MATCHES "^(x86_64|amd64|x64|i[3-6]86)$")
    set(FDT_HW_X86_TARGET ON)
endif()
set(FDT_HW_ENABLE_X86_AVX2 OFF)
if(FDT_HW_ENABLE_INTRINSICS_COMPARE AND FDT_HW_X86_TARGET AND NOT FDT_HW_FORCE_SCALAR AND
        NOT FDT_HW_X86_ARCH_UPPER STREQUAL "AVX512")
    set(FDT_HW_ENABLE_X86_AVX2 ON)
endif()
set(FDT_HW_EFFECTIVE_BACKEND "pure-highway")
if(FDT_HW_FORCE_SCALAR)
    set(FDT_HW_EFFECTIVE_BACKEND "scalar")
elseif(FDT_HW_ENABLE_HYBRID_CEILING AND FDT_HW_ENABLE_X86_AVX2)
    set(FDT_HW_EFFECTIVE_BACKEND "x86-hybrid-avx2")
endif()

set(FDT_HW_HIGHWAY_ROOT "" CACHE PATH
    "Optional root directory containing prebuilt Highway variants")
set(FDT_HW_OPENCV_ROOT "" CACHE PATH
    "Optional root directory containing OpenCV variants for image benchmark")
set(FDT_HW_HIGHWAY_VARIANT "")
set(FDT_HW_OPENCV_VARIANT "")

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
        $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/include>
        $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/src>
        $<INSTALL_INTERFACE:${CMAKE_INSTALL_INCLUDEDIR}>
    PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/../src
)
target_link_libraries(fdt_hw_kernels PUBLIC ${FDT_HW_HIGHWAY_TARGET})
target_compile_features(fdt_hw_kernels PUBLIC cxx_std_11)
if(FDT_HW_ENABLE_HYBRID_CEILING)
    if(FDT_HW_FORCE_SCALAR)
        message(STATUS "FDT_HW_FORCE_SCALAR enabled; using scalar kernels")
    elseif(FDT_HW_ENABLE_X86_AVX2)
        target_compile_definitions(fdt_hw_kernels PRIVATE
            FDT_HW_ENABLE_HYBRID_CEILING=1)
    else()
        message(STATUS "FDT_HW_ENABLE_HYBRID_CEILING requested, but x86 AVX2 is unavailable; using pure Highway kernels")
    endif()
endif()
if(FDT_HW_FORCE_SCALAR)
    target_compile_definitions(fdt_hw_kernels PRIVATE FDT_HW_FORCE_SCALAR=1)
endif()
if(FDT_HW_ENABLE_X86_AVX2)
    target_compile_definitions(fdt_hw_kernels PRIVATE FDT_HW_ENABLE_X86_AVX2=1)
endif()

if(MSVC)
    target_compile_options(fdt_hw_kernels PRIVATE /W4 /permissive-)
    if(FDT_HW_X86_TARGET AND NOT FDT_HW_FORCE_SCALAR AND
            FDT_HW_X86_ARCH_UPPER STREQUAL "AVX512")
        target_compile_options(fdt_hw_kernels PRIVATE /arch:AVX512)
    elseif(FDT_HW_X86_TARGET AND NOT FDT_HW_FORCE_SCALAR AND
            (FDT_HW_ENABLE_X86_AVX2 OR FDT_HW_X86_ARCH_UPPER STREQUAL "AVX2"))
        target_compile_options(fdt_hw_kernels PRIVATE /arch:AVX2)
    endif()
else()
    target_compile_options(fdt_hw_kernels PRIVATE -Wall -Wextra -Wpedantic)
    if(FDT_HW_X86_TARGET AND NOT FDT_HW_FORCE_SCALAR AND
            FDT_HW_X86_ARCH_UPPER STREQUAL "AVX512")
        target_compile_options(fdt_hw_kernels PRIVATE -mavx512f)
    elseif(FDT_HW_X86_TARGET AND NOT FDT_HW_FORCE_SCALAR AND
            (FDT_HW_ENABLE_X86_AVX2 OR FDT_HW_X86_ARCH_UPPER STREQUAL "AVX2"))
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
    find_package(OpenCV QUIET COMPONENTS core imgcodecs imgproc)
    if(NOT OpenCV_FOUND AND EXISTS "${FDT_HW_OPENCV_ROOT}")
        foreach(_fdt_hw_opencv_variant IN ITEMS
            vs2022-x64-release-static
            vs2022-x64-debug-static
            vs2022-x64-release
            vs2022-x64-debug
        )
            foreach(_fdt_hw_opencv_suffix IN ITEMS staticlib "" lib)
                set(_fdt_hw_opencv_candidate
                    "${FDT_HW_OPENCV_ROOT}/${_fdt_hw_opencv_variant}/${_fdt_hw_opencv_suffix}")
                if(EXISTS "${_fdt_hw_opencv_candidate}/OpenCVConfig.cmake")
                    set(OpenCV_DIR "${_fdt_hw_opencv_candidate}" CACHE PATH
                        "Path to OpenCVConfig.cmake" FORCE)
                    find_package(OpenCV QUIET COMPONENTS core imgcodecs imgproc)
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

        add_executable(fdt_hw_resolution_benchmark
            ${CMAKE_CURRENT_SOURCE_DIR}/../src/facedetectcnn.cpp
            ${CMAKE_CURRENT_SOURCE_DIR}/../src/facedetectcnn-model.cpp
            benchmark/hw_resolution_benchmark.cpp
        )
        target_include_directories(fdt_hw_resolution_benchmark PRIVATE
            ${CMAKE_CURRENT_SOURCE_DIR}/../src
            ${OpenCV_INCLUDE_DIRS}
        )
        target_link_libraries(fdt_hw_resolution_benchmark PRIVATE
            fdt_hw_kernels
            ${OpenCV_LIBS}
        )
        target_compile_features(fdt_hw_resolution_benchmark PRIVATE cxx_std_11)
        if(FDT_HW_X86_TARGET AND FDT_HW_X86_ARCH_UPPER STREQUAL "AVX512")
            target_compile_definitions(fdt_hw_resolution_benchmark PRIVATE
                _ENABLE_AVX512)
            if(MSVC)
                target_compile_options(fdt_hw_resolution_benchmark PRIVATE
                    /arch:AVX512)
            else()
                target_compile_options(fdt_hw_resolution_benchmark PRIVATE
                    -mavx512f)
            endif()
        elseif(FDT_HW_X86_TARGET AND FDT_HW_X86_ARCH_UPPER STREQUAL "AVX2")
            target_compile_definitions(fdt_hw_resolution_benchmark PRIVATE
                _ENABLE_AVX2)
            if(MSVC)
                target_compile_options(fdt_hw_resolution_benchmark PRIVATE
                    /arch:AVX2)
            else()
                target_compile_options(fdt_hw_resolution_benchmark PRIVATE
                    -mavx2 -mfma)
            endif()
        endif()
    else()
        message(STATUS "OpenCV not found; fdt_hw_image_benchmark disabled")
    endif()
endif()

message(STATUS "FDT_HW_HIGHWAY_TARGET = ${FDT_HW_HIGHWAY_TARGET}")
message(STATUS "FDT_HW_HIGHWAY_VARIANT = ${FDT_HW_HIGHWAY_VARIANT}")
message(STATUS "FDT_HW_OPENCV_VARIANT = ${FDT_HW_OPENCV_VARIANT}")
message(STATUS "FDT_HW_X86_TARGET = ${FDT_HW_X86_TARGET}")
message(STATUS "FDT_HW_X86_ARCH = ${FDT_HW_X86_ARCH_UPPER}")
message(STATUS "FDT_HW_EFFECTIVE_BACKEND = ${FDT_HW_EFFECTIVE_BACKEND}")
message(STATUS "FDT_HW_FORCE_SCALAR = ${FDT_HW_FORCE_SCALAR}")
message(STATUS "FDT_HW_ENABLE_INTRINSICS_COMPARE = ${FDT_HW_ENABLE_INTRINSICS_COMPARE}")
message(STATUS "FDT_HW_ENABLE_X86_AVX2 = ${FDT_HW_ENABLE_X86_AVX2}")
message(STATUS "FDT_HW_ENABLE_HYBRID_CEILING = ${FDT_HW_ENABLE_HYBRID_CEILING}")

# ---------------------------------------------------------------------------
# Install + package config so downstream projects can use find_package(fdt_hw).
#
#   find_package(fdt_hw REQUIRED CONFIG)
#   target_link_libraries(my_app PRIVATE fdt_hw::fdt_hw_kernels)
#
# The generated config re-discovers Highway via find_dependency(hwy), so the
# transitive hwy::hwy link/include usage requirements propagate automatically.
# ---------------------------------------------------------------------------
include(CMakePackageConfigHelpers)

install(TARGETS fdt_hw_kernels
    EXPORT fdt_hwTargets
    RUNTIME DESTINATION ${CMAKE_INSTALL_BINDIR}
    LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
    ARCHIVE DESTINATION ${CMAKE_INSTALL_LIBDIR}
    INCLUDES DESTINATION ${CMAKE_INSTALL_INCLUDEDIR})

install(FILES ${CMAKE_CURRENT_SOURCE_DIR}/include/facedetect_hw.h
    DESTINATION ${CMAKE_INSTALL_INCLUDEDIR}/facedetection)

install(EXPORT fdt_hwTargets
    FILE fdt_hwTargets.cmake
    NAMESPACE fdt_hw::
    DESTINATION ${CMAKE_INSTALL_LIBDIR}/cmake/fdt_hw)

configure_package_config_file(
    ${CMAKE_CURRENT_SOURCE_DIR}/cmake/fdt_hwConfig.cmake.in
    ${CMAKE_CURRENT_BINARY_DIR}/fdt_hwConfig.cmake
    INSTALL_DESTINATION ${CMAKE_INSTALL_LIBDIR}/cmake/fdt_hw)

write_basic_package_version_file(
    ${CMAKE_CURRENT_BINARY_DIR}/fdt_hwConfigVersion.cmake
    VERSION ${BUILD_VERSION}
    COMPATIBILITY AnyNewerVersion)

install(FILES
    ${CMAKE_CURRENT_BINARY_DIR}/fdt_hwConfig.cmake
    ${CMAKE_CURRENT_BINARY_DIR}/fdt_hwConfigVersion.cmake
    DESTINATION ${CMAKE_INSTALL_LIBDIR}/cmake/fdt_hw)
