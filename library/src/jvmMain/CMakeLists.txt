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

set(PROJECT_WRAPPER_DIR ${CMAKE_SOURCE_DIR}/../../wrapper)
set(PROJECT_LIB_DIR ${CMAKE_SOURCE_DIR}/../../darwin)

add_library(yuv STATIC IMPORTED)
add_library(sharpyuv STATIC IMPORTED)
add_library(dav1d STATIC IMPORTED)
add_library(avif STATIC IMPORTED)
set_target_properties(yuv PROPERTIES IMPORTED_LOCATION ${PROJECT_LIB_DIR}/build/native/libyuv.a)
set_target_properties(sharpyuv PROPERTIES IMPORTED_LOCATION ${PROJECT_LIB_DIR}/build/native/libsharpyuv.a)
set_target_properties(dav1d PROPERTIES IMPORTED_LOCATION ${PROJECT_LIB_DIR}/build/native/libdav1d.a)
set_target_properties(avif PROPERTIES IMPORTED_LOCATION ${PROJECT_LIB_DIR}/build/native/libavif.a)

include_directories(${PROJECT_LIB_DIR}/libavif/include)

include_directories(
        ${PROJECT_WRAPPER_DIR}/skia/include
        ${PROJECT_WRAPPER_DIR}/skia
)

file(GLOB_RECURSE sources
        ${PROJECT_WRAPPER_DIR}/avifDecoderJni.cpp
        ${PROJECT_WRAPPER_DIR}/avifImageJni.cpp
        ${PROJECT_WRAPPER_DIR}/avifImageJvm.cpp
)
add_library(avif-jvm SHARED ${sources})

#target_compile_definitions(avif-jvm PUBLIC CONFIG_VERSION="${QUICKJS_VERSION}")
target_link_libraries(avif-jvm yuv sharpyuv dav1d avif)
