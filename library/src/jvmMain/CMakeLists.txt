cmake_minimum_required(VERSION 3.4.1)
project(avif_jvm_jni)

set(CMAKE_C_STANDARD 99)
set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")

set(JAVA_AWT_LIBRARY NotNeeded)
set(JAVA_AWT_INCLUDE_PATH NotNeeded)
find_package(JNI REQUIRED)
include_directories(${JNI_INCLUDE_DIRS})

set(BUILD_SHARED_LIBS OFF CACHE BOOL "" FORCE)
set(AVIF_LOCAL_LIBGAV1 OFF CACHE BOOL "" FORCE)
set(AVIF_CODEC_LIBGAV1 OFF CACHE BOOL "" FORCE)
set(AVIF_LOCAL_DAV1D ON CACHE BOOL "" FORCE)
set(AVIF_CODEC_DAV1D ON CACHE BOOL "" FORCE)

set(PROJECT_NATIVE_DIR ${CMAKE_SOURCE_DIR}/../../native)

include_directories(
        ${PROJECT_NATIVE_DIR}/skia/include
        ${PROJECT_NATIVE_DIR}/skia
)

add_subdirectory(${CMAKE_SOURCE_DIR}/../../darwin/libavif libavif)

file(GLOB_RECURSE sources
        ${PROJECT_NATIVE_DIR}/avif-decoder-jni.cpp
        ${PROJECT_NATIVE_DIR}/avif-image-jni.cpp
        ${PROJECT_NATIVE_DIR}/avif-image-jvm.cpp
)
add_library(avif-jvm SHARED ${sources})

#target_compile_definitions(avif-jvm PUBLIC CONFIG_VERSION="${QUICKJS_VERSION}")
target_link_libraries(avif-jvm avif)
