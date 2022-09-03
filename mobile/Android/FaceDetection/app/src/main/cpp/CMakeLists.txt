# CMakeLists for libfacedetectcnn

project(facedetection)

cmake_minimum_required(VERSION 2.8)
option(ENABLE_OPENCV "use opencv" ON)
option(ENABLE_OPENMP "use openmp" ON)
option(ENABLE_INT8 "use int8" ON)
option(ENABLE_AVX2 "use avx2" OFF)
option(ENABLE_AVX512 "use avx512" OFF)
option(ENABLE_NEON "whether use neon, if use arm please set it on" ON)
option(DEMO "build the demo" OFF)
add_definitions("-O3")

if (BUILD_SHARED_LIBS)
    add_definitions(-DBUILD_SHARED_LIBS)
    if (CMAKE_COMPILER_IS_GNUCXX AND NOT MINGW)
        # Just setting CMAKE_POSITION_INDEPENDENT_CODE should be enough to set
        # -fPIC for GCC but sometimes it still doesn't get set, so make sure it
        # does.
        add_definitions("-fPIC")
    endif()
    set(CMAKE_POSITION_INDEPENDENT_CODE ON)
endif()

SET(fdt_base_dir   ${PROJECT_SOURCE_DIR})
SET(fdt_src_dir    ${fdt_base_dir}/../../../../../../../src/)
SET(fdt_inc_dir    ${fdt_base_dir}/../../../../../../../src/)

SET(fdt_lib_name   facedetection)
SET(fdt_lib_static ${fdt_lib_name})
SET(fdt_lib_shared ${fdt_lib_name}_shared)

FILE(GLOB_RECURSE fdt_source_files ${fdt_src_dir}/*.cpp)
FILE(GLOB_RECURSE jni_source_files ${fdt_base_dir}/facedetectcnn-jni.cpp)

LIST(SORT         fdt_source_files)

SET(INSTALLHEADER_FILES ${fdt_inc_dir}/facedetectcnn.h)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

if(ENABLE_OPENMP)
    message("using openmp")
    add_definitions(-D_OPENMP)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${OpenMP_CXX_FLAGS}")
    set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ${OpenMP_EXE_LINKER_FLAGS}")
endif()

if(ENABLE_INT8)
	message("using int8")
	add_definitions(-D_ENABLE_INT8)
endif()

IF(CMAKE_CXX_COMPILER_ID STREQUAL "GNU"
        OR CMAKE_CXX_COMPILER_ID STREQUAL "Clang")
    message("use -O3 to speedup")
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3")
ENDIF()

if(ENABLE_AVX512)
    add_definitions(-D_ENABLE_AVX512)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -mavx512bw")
endif()

if(ENABLE_AVX2)
    add_definitions(-D_ENABLE_AVX2)
    set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -mavx2 -mfma")
endif()

if(ENABLE_NEON)
    message("Using NEON")
    add_definitions(-D_ENABLE_NEON)
endif()

INCLUDE_DIRECTORIES(${fdt_inc_dir})


# Create a static library (.a)
ADD_LIBRARY(${fdt_lib_static} STATIC ${fdt_source_files} ${jni_source_files})

# Create a shared library (.so)
ADD_LIBRARY(${fdt_lib_shared} SHARED ${fdt_source_files} ${jni_source_files})
SET_TARGET_PROPERTIES(${fdt_lib_shared} PROPERTIES OUTPUT_NAME "${fdt_lib_name}")
SET_TARGET_PROPERTIES(${fdt_lib_shared} PROPERTIES PREFIX "lib")

# Create demo. OpenCV is requred.
if (ENABLE_OPENCV)
    SET(OpenCV_INCLUDE_DIRS   ${PROJECT_SOURCE_DIR}/../../../../OpenCV-android-sdk/sdk/native/jni/include)
    SET(OpenCV_SHARED_LIBS   ${PROJECT_SOURCE_DIR}/../../../../OpenCV-android-sdk/sdk/native/libs/${ANDROID_ABI})
    SET(OpenCV_STATIC_LIBS   ${PROJECT_SOURCE_DIR}/../../../../OpenCV-android-sdk/sdk/native/staticlibs/${ANDROID_ABI})

    #    find_package(OpenCV REQUIRED)
    include_directories(${OpenCV_INCLUDE_DIRS})

    add_library(libopencv_java4 SHARED IMPORTED )
    set_target_properties(libopencv_java4 PROPERTIES
            IMPORTED_LOCATION "${OpenCV_SHARED_LIBS}/libopencv_java4.so")

#    add_library(libopencv_calib3d STATIC IMPORTED )
#    set_target_properties(libopencv_calib3d PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_calib3d.a")
#
#    add_library(libopencv_core STATIC IMPORTED )
#    set_target_properties(libopencv_core PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_core.a")
#
#    add_library(libopencv_dnn STATIC IMPORTED )
#    set_target_properties(libopencv_dnn PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_dnn.a")
#
#    add_library(libopencv_features2d STATIC IMPORTED )
#    set_target_properties(libopencv_features2d PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_features2d.a")
#
#    add_library(libopencv_flann STATIC IMPORTED )
#    set_target_properties(libopencv_flann PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_flann.a")
#
#    add_library(libopencv_highgui STATIC IMPORTED )
#    set_target_properties(libopencv_highgui PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_highgui.a")
#
#    add_library(libopencv_imgcodecs STATIC IMPORTED )
#    set_target_properties(libopencv_imgcodecs PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_imgcodecs.a")
#
#    add_library(libopencv_imgproc STATIC IMPORTED )
#    set_target_properties(libopencv_imgproc PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_imgproc.a")
#
#    add_library(libopencv_ml STATIC IMPORTED )
#    set_target_properties(libopencv_ml PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_ml.a")
#
#    add_library(libopencv_objdetect STATIC IMPORTED )
#    set_target_properties(libopencv_objdetect PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_objdetect.a")
#
#    add_library(libopencv_photo STATIC IMPORTED )
#    set_target_properties(libopencv_photo PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_photo.a")
#
#    add_library(libopencv_stitching STATIC IMPORTED )
#    set_target_properties(libopencv_stitching PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_stitching.a")
#
#    add_library(libopencv_video STATIC IMPORTED )
#    set_target_properties(libopencv_video PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_video.a")
#
#    add_library(libopencv_videoio STATIC IMPORTED )
#    set_target_properties(libopencv_videoio PROPERTIES
#            IMPORTED_LOCATION "${OpenCV_STATIC_LIBS}/libopencv_videoio.a")
    find_library( # Sets the name of the path variable.
            log-lib

            # Specifies the name of the NDK library that
            # you want CMake to locate.
            log)
    target_link_libraries(
            ${fdt_lib_shared}
#            ${fdt_lib_static}
            libopencv_java4
            #编译静态 opencv 需要依赖的静态链接库
#            libopencv_calib3d
#            libopencv_core
#            libopencv_dnn
#            libopencv_features2d
#            libopencv_flann
#            libopencv_highgui
#            libopencv_imgcodecs
#            libopencv_imgproc
#            libopencv_ml
#            libopencv_objdetect
#            libopencv_photo
#            libopencv_stitching
#            libopencv_video
#            libopencv_videoio
            ${log-lib}
            )
endif()