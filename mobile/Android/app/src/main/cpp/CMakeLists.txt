# CMakeLists for libfacedetectcnn

project(facedetection)

cmake_minimum_required(VERSION 2.8)
option(ENABLE_OPENCV "use opencv" ON)
option(ENABLE_OPENMP "use openmp" ON)
option(ENABLE_INT8 "use int8" ON)
option(ENABLE_AVX2 "use avx2" OFF)
option(ENABLE_NEON "whether use neon, if use arm please set it on" ON)
option(DEMO "build the demo" OFF)
add_definitions("-O3")

SET(fdt_base_dir   ${PROJECT_SOURCE_DIR})
SET(fdt_src_dir    ${fdt_base_dir})
SET(fdt_inc_dir    ${fdt_base_dir})

SET(fdt_lib_name   facedetection)
SET(fdt_lib_static ${fdt_lib_name})
SET(fdt_lib_shared ${fdt_lib_name}_shared)

FILE(GLOB_RECURSE fdt_source_files ${fdt_src_dir}/*.cpp)
LIST(SORT         fdt_source_files)

if(ENABLE_OPENMP)
    message("using openmp")
    add_definitions(-D_OPENMP)
endif()

if(ENABLE_INT8)
	message("using int8")
	add_definitions(-D_ENABLE_INT8)
endif()

if(ENABLE_AVX2)
	message("using avx2")
	add_definitions(-D_ENABLE_AVX2)
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -mavx2 -mfma")
endif()

if(ENABLE_NEON)
	message("using arm")
	add_definitions(-D_ENABLE_NEON)
endif()

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

INCLUDE_DIRECTORIES(${fdt_inc_dir})

# Create a static library (.a)
ADD_LIBRARY(${fdt_lib_static} STATIC ${fdt_source_files})

# Create a shared library (.so)
ADD_LIBRARY(${fdt_lib_shared} SHARED ${fdt_source_files})
SET_TARGET_PROPERTIES(${fdt_lib_shared} PROPERTIES OUTPUT_NAME "${fdt_lib_name}")
SET_TARGET_PROPERTIES(${fdt_lib_shared} PROPERTIES PREFIX "lib")

# Create demo. OpenCV is requred.
if (ENABLE_OPENCV)
    SET(OpenCV_INCLUDE_DIRS   ${PROJECT_SOURCE_DIR}/opencv2)
    SET(OpenCV_LIBS   ${PROJECT_SOURCE_DIR}/../jniLibs/${ANDROID_ABI})
    #    find_package(OpenCV REQUIRED)
    include_directories(${OpenCV_INCLUDE_DIRS})

    add_library(libopencv_java4 SHARED IMPORTED )
    set_target_properties(libopencv_java4 PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_java4.so")

    #add_library(libopencv_java SHARED IMPORTED )
    #set_target_properties(libopencv_java PROPERTIES
    #    IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_java.so")

    add_library(libopencv_calib3d STATIC IMPORTED )
    set_target_properties(libopencv_calib3d PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_calib3d.a")

    add_library(libopencv_core STATIC IMPORTED )
    set_target_properties(libopencv_core PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_core.a")

    add_library(libopencv_dnn STATIC IMPORTED )
    set_target_properties(libopencv_dnn PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_dnn.a")

    add_library(libopencv_features2d STATIC IMPORTED )
    set_target_properties(libopencv_features2d PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_features2d.a")

    add_library(libopencv_flann STATIC IMPORTED )
    set_target_properties(libopencv_flann PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_flann.a")

    add_library(libopencv_highgui STATIC IMPORTED )
    set_target_properties(libopencv_highgui PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_highgui.a")

    add_library(libopencv_imgcodecs STATIC IMPORTED )
    set_target_properties(libopencv_imgcodecs PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_imgcodecs.a")

    add_library(libopencv_imgproc STATIC IMPORTED )
    set_target_properties(libopencv_imgproc PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_imgproc.a")

    add_library(libopencv_ml STATIC IMPORTED )
    set_target_properties(libopencv_ml PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_ml.a")

    add_library(libopencv_objdetect STATIC IMPORTED )
    set_target_properties(libopencv_objdetect PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_objdetect.a")

    add_library(libopencv_photo STATIC IMPORTED )
    set_target_properties(libopencv_photo PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_photo.a")

    add_library(libopencv_stitching STATIC IMPORTED )
    set_target_properties(libopencv_stitching PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_stitching.a")

    add_library(libopencv_video STATIC IMPORTED )
    set_target_properties(libopencv_video PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_video.a")

    add_library(libopencv_videoio STATIC IMPORTED )
    set_target_properties(libopencv_videoio PROPERTIES
            IMPORTED_LOCATION "${OpenCV_LIBS}/libopencv_videoio.a")
    find_library( # Sets the name of the path variable.
            log-lib

            # Specifies the name of the NDK library that
            # you want CMake to locate.
            log)
    target_link_libraries(${fdt_lib_shared}
            ${fdt_lib_static}
            libopencv_java4     #opencv manager
            #    libopencv_java      #opencv manager

            #编译 opencv 需要依赖的静态链接库
            libopencv_calib3d
            libopencv_core
            libopencv_dnn
            libopencv_features2d
            libopencv_flann
            libopencv_highgui
            libopencv_imgcodecs
            libopencv_imgproc
            libopencv_ml
            libopencv_objdetect
            libopencv_photo
            libopencv_stitching
            libopencv_video
            libopencv_videoio
            ${log-lib}
            )
endif()

if (DEMO)
    #if(WIN32)
    #    set(OpenCV_DIR "D:/opencv343/build") # TODO
    #endif()
    find_package(OpenCV REQUIRED)
    include_directories(${OpenCV_INCLUDE_DIRS})

    set(fdt_demo_files ${fdt_base_dir}/example/libfacedetectcnn-example.cpp)
    add_executable(fdt_demo ${fdt_demo_files})
    target_link_libraries(fdt_demo ${fdt_lib_static} ${OpenCV_LIBS})
endif()

if (GSTREAMER)
    find_package(OpenCV REQUIRED)

    include(FindPkgConfig)
    pkg_search_module(GSTREAMER REQUIRED gstreamer-1.0)
    pkg_search_module(GSTREAMER_BASE REQUIRED gstreamer-base-1.0)
    pkg_search_module(GSTREAMER_VIDEO REQUIRED gstreamer-video-1.0)

    add_library(gstfacedetect SHARED
        example/libfacedetect.cpp
    )

    include_directories(gstfacedetect PRIVATE
        ${GSTREAMER_INCLUDE_DIRS}
        ${GSTREAMER_BASE_INCLUDE_DIRS}
        ${GSTREAMER_VIDEO_INCLUDE_DIRS}
        ${OpenCV_INCLUDE_DIRS}
    )

    target_link_libraries(gstfacedetect
        ${GSTREAMER_LIBRARIES}
        ${GSTREAMER_BASE_LIBRARIES}
        ${GSTREAMER_VIDEO_LIBRARIES}
        ${OpenCV_LIBS}
        ${fdt_lib_shared}
    )



endif()
