cmake_minimum_required(VERSION 3.11.0)
project(avif_android_jni)

set(AVIF_LOCAL_LIBGAV1 OFF CACHE BOOL "" FORCE)
set(AVIF_CODEC_LIBGAV1 OFF CACHE BOOL "" FORCE)
set(AVIF_LOCAL_DAV1D OFF CACHE BOOL "" FORCE)
set(AVIF_CODEC_DAV1D ON CACHE BOOL "" FORCE)

#include(../../native/cmake/dependency.cmake)

# ======= WAIT TO REMOVE =========
set(DAV1D_DIR ${CMAKE_SOURCE_DIR}/../../native/dav1d)
add_library(lib_dav1d STATIC IMPORTED)
set_target_properties(lib_dav1d PROPERTIES IMPORTED_LOCATION ${DAV1D_DIR}/build/${ANDROID_ABI}/src/libdav1d.a)
set(DAV1D_INCLUDE_DIR ${DAV1D_DIR}/include ${DAV1D_DIR}/build/${ANDROID_ABI}/include/dav1d)
set(DAV1D_VERSION 1.2.1)
set(DAV1D_LIBRARY lib_dav1d)
# ================================

add_subdirectory(../../native/libavif libavif)

set(SRC_FILES
        ../../native/avif-jni.c
        ../../native/avif-android.c
        ../../native/util-android.c)
add_library(avif-android SHARED ${SRC_FILES})

target_link_libraries(avif-android jnigraphics avif log)
