# CMakeLists for libfacedetectcnn

project(facedetection)

cmake_minimum_required(VERSION 2.8)

option(ENABLE_INT8 "use int8" OFF)
option(ENABLE_AVX2 "use avx2" OFF)
option(ENABLE_NEON "whether use neon, if use arm please set it on" OFF)
option(DEMO "build the demo" OFF)

SET(fdt_base_dir   ${PROJECT_SOURCE_DIR})
SET(fdt_src_dir    ${fdt_base_dir}/src)
SET(fdt_inc_dir    ${fdt_base_dir}/src)

SET(fdt_lib_name   facedetection)
SET(fdt_lib_static ${fdt_lib_name})
SET(fdt_lib_shared ${fdt_lib_name}_shared)

FILE(GLOB_RECURSE fdt_source_files ${fdt_src_dir}/*.cpp)
LIST(SORT         fdt_source_files)

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
