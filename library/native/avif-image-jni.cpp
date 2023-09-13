#include <jni.h>

#include "avif/avif.h"

extern "C" JNIEXPORT jint JNICALL
Java_com_seiko_avif_AvifImage_getWidth(JNIEnv *env, jobject type, jlong context) {
    avifImage *image = reinterpret_cast<avifImage*>(context);
    return image->width;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_seiko_avif_AvifImage_getHeight(JNIEnv *env, jobject type, jlong context) {
    avifImage *image = reinterpret_cast<avifImage*>(context);
    return image->height;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_seiko_avif_AvifImage_getDepth(JNIEnv *env, jobject type, jlong context) {
    avifImage *image = reinterpret_cast<avifImage*>(context);
    return image->depth;
}
