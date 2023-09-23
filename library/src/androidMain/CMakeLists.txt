cmake_minimum_required(VERSION 3.11.0)
project(avif_android_jni)

#add_subdirectory(${CMAKE_SOURCE_DIR}/../../darwin/libavif libavif)

set(PROJECT_WRAPPER_DIR ${CMAKE_SOURCE_DIR}/../../wrapper)
set(PROJECT_LIB_DIR ${CMAKE_SOURCE_DIR}/../../darwin)

add_library(dav1d STATIC IMPORTED)
add_library(avif STATIC IMPORTED)
set_target_properties(dav1d PROPERTIES IMPORTED_LOCATION ${PROJECT_LIB_DIR}/build/android/${ANDROID_ABI}/libdav1d.a)
set_target_properties(avif PROPERTIES IMPORTED_LOCATION ${PROJECT_LIB_DIR}/build/android/${ANDROID_ABI}/libavif.a)

include_directories(${PROJECT_LIB_DIR}/libavif/include)

file(GLOB_RECURSE sources
        ${PROJECT_WRAPPER_DIR}/avif-decoder-jni.cpp
        ${PROJECT_WRAPPER_DIR}/avif-image-jni.cpp
        ${PROJECT_WRAPPER_DIR}/avif-image-android.cpp)
add_library(avif-android SHARED ${sources})

target_link_libraries(avif-android jnigraphics dav1d avif log)
