cmake_minimum_required(VERSION 3.11.0)
project(avif_android_jni)

set(AVIF_LOCAL_LIBGAV1 OFF CACHE BOOL "" FORCE)
set(AVIF_CODEC_LIBGAV1 OFF CACHE BOOL "" FORCE)
set(AVIF_LOCAL_DAV1D OFF CACHE BOOL "" FORCE)
set(AVIF_CODEC_DAV1D ON CACHE BOOL "" FORCE)

set(PROJECT_NATIVE_DIR ${CMAKE_SOURCE_DIR}/../../native)
set(EXTERNAL_DIR ${CMAKE_SOURCE_DIR}/../../../external)

# TODO: ======= WAIT TO REMOVE =========
add_library(lib_dav1d STATIC IMPORTED)
set_target_properties(lib_dav1d PROPERTIES IMPORTED_LOCATION
        ${EXTERNAL_DIR}/build/dav1d/${ANDROID_ABI}/src/libdav1d.a)
set(DAV1D_INCLUDE_DIR
        ${EXTERNAL_DIR}/deps/dav1d/include
        ${EXTERNAL_DIR}/build/dav1d/${ANDROID_ABI}/include/dav1d)
set(DAV1D_VERSION 1.2.1)
set(DAV1D_LIBRARY lib_dav1d)
# ================================

add_subdirectory(${EXTERNAL_DIR}/deps/libavif libavif)

file(GLOB_RECURSE sources
        ${PROJECT_NATIVE_DIR}/avif-decoder-jni.cpp
        ${PROJECT_NATIVE_DIR}/avif-image-jni.cpp
        ${PROJECT_NATIVE_DIR}/avif-image-android.cpp)
add_library(avif-android SHARED ${sources})

target_link_libraries(avif-android jnigraphics avif log)
