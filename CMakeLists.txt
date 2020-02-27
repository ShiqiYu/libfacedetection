# CMakeLists for libfacedetectcnn

project(libfacedetection)

cmake_minimum_required(VERSION 2.8)

option(ENABLE_AVX512 "use avx512" OFF)
option(ENABLE_AVX2 "use avx2" ON)
option(ENABLE_NEON "whether use neon, if use arm please set it on" OFF)
option(DEMO "build the demo" ON)

SET(fdt_base_dir   ${PROJECT_SOURCE_DIR})
SET(fdt_src_dir    ${fdt_base_dir}/src)
SET(fdt_inc_dir    ${fdt_base_dir}/src)

SET(fdt_lib_name   facedetection)
SET(fdt_lib_static ${fdt_lib_name})
SET(fdt_lib_shared ${fdt_lib_name}_shared)

FILE(GLOB_RECURSE fdt_source_files ${fdt_src_dir}/*.cpp)
LIST(SORT         fdt_source_files)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)

#use -O3 to speedup
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O3")


if(ENABLE_AVX512)
	add_definitions(-D_ENABLE_AVX512)
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -mavx512bw")
endif()

if(ENABLE_AVX2)
	add_definitions(-D_ENABLE_AVX2)
	set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -mavx2 -mfma")
endif()

if(ENABLE_NEON)
	message("Using ENON")
	add_definitions(-D_ENABLE_NEON)
endif()

#FIND_PACKAGE(OpenMP)
#if(OPENMP_FOUND)
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${OpenMP_CXX_FLAGS}")
#set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} ${OpenMP_EXE_LINKER_FLAGS}")
#endif()

INCLUDE_DIRECTORIES(${fdt_inc_dir})

# Create a static library (.a)
ADD_LIBRARY(${fdt_lib_static} STATIC ${fdt_source_files})

# Create a shared library (.so)
ADD_LIBRARY(${fdt_lib_shared} SHARED ${fdt_source_files})
SET_TARGET_PROPERTIES(${fdt_lib_shared} PROPERTIES OUTPUT_NAME "${fdt_lib_name}")
SET_TARGET_PROPERTIES(${fdt_lib_shared} PROPERTIES PREFIX "lib")

# Create demo. OpenCV is requred.
if (DEMO)
    find_package(OpenCV REQUIRED)
    include_directories(${OpenCV_INCLUDE_DIRS})

    add_executable(detect-image-demo ${fdt_base_dir}/example/detect-image.cpp)
    target_link_libraries(detect-image-demo ${fdt_lib_static} ${OpenCV_LIBS})

    add_executable(detect-camera-demo ${fdt_base_dir}/example/detect-camera.cpp)
    target_link_libraries(detect-camera-demo ${fdt_lib_static} ${OpenCV_LIBS})
    
        add_executable(benchmark ${fdt_base_dir}/example/benchmark.cpp)
    target_link_libraries(benchmark ${fdt_lib_static} ${OpenCV_LIBS})
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

message("AVX512 = ${ENABLE_AVX512}")
message("AVX2 = ${ENABLE_AVX2}")
message("NEON = ${ENABLE_NEON}")
message("OpenMP = ${OPENMP_FOUND}")
message("DEMO = ${DEMO}")
