cmake_minimum_required(VERSION 3.11.0)
project(avif_android_jni)

#include(ExternalProject)
#include(../../native/common/custom_steps.cmake)
#include(../../native/dav1d/dav1d.cmake)

#set(AVIF_LOCAL_LIBGAV1 OFF CACHE BOOL "" FORCE)
#set(AVIF_CODEC_LIBGAV1 OFF CACHE BOOL "" FORCE)
#set(AVIF_LOCAL_DAV1D ON CACHE BOOL "" FORCE)
#set(AVIF_CODEC_DAV1D ON CACHE BOOL "" FORCE)

add_subdirectory(../../native/libavif ${CMAKE_CURRENT_BINARY_DIR}/libavif)

set(SRC_FILES
        ../../native/avif-jni.c
        ../../native/avif-android.c
        ../../native/util-android.c
        ../../native/util.h)

add_library(avif-android SHARED ${SRC_FILES})

target_link_libraries(avif-android jnigraphics avif log)
