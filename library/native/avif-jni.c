#include <jni.h>

#include "avif/avif.h"

#include "util.h"

jlong JNICALL
Java_com_seiko_avif_AvifDecoder_createContext(JNIEnv *env, jclass type,
                                              jbyteArray byteArray,
                                              jint length) {
    avifDecoder *decoder = avifDecoderCreate();
    decoder->maxThreads = 1;
    decoder->ignoreExif = AVIF_TRUE;
    decoder->ignoreXMP = AVIF_TRUE;

    jbyte *buf = (*env)->GetByteArrayElements(env, byteArray, NULL);
    avifResult result = avifDecoderSetIOMemory(decoder, (const uint8_t *) buf, length);
    if (result != AVIF_RESULT_OK) {
        goto cleanup;
    }
    result = avifDecoderParse(decoder);
    if (result != AVIF_RESULT_OK) {
        goto cleanup;
    }
    return ptr_to_jlong(decoder);

    cleanup:
    avifDecoderDestroy(decoder);
    return 0;
}

void JNICALL
Java_com_seiko_avif_AvifDecoder_destroyContext(JNIEnv *env, jobject type, jlong context) {
    avifDecoder *decoder = jlong_to_ptr(context);
    avifDecoderDestroy(decoder);
}

jboolean JNICALL
Java_com_seiko_avif_AvifDecoder_nextImage(JNIEnv *env, jobject type, jlong context) {
    avifDecoder *decoder = jlong_to_ptr(context);
    return avifDecoderNextImage(decoder) == AVIF_RESULT_OK;
}

jint JNICALL
Java_com_seiko_avif_AvifDecoder_getImageCount(JNIEnv *env, jobject type, jlong context) {
    avifDecoder *decoder = jlong_to_ptr(context);
    return decoder->imageCount;
}

jint JNICALL
Java_com_seiko_avif_AvifDecoder_getImageWidth(JNIEnv *env, jobject type, jlong context) {
    avifDecoder *decoder = jlong_to_ptr(context);
    return decoder->image->width;
}

jint JNICALL
Java_com_seiko_avif_AvifDecoder_getImageHeight(JNIEnv *env, jobject type, jlong context) {
    avifDecoder *decoder = jlong_to_ptr(context);
    return decoder->image->height;
}
