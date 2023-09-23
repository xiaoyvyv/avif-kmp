#include <jni.h>

#include "avif/avif.h"

extern "C" JNIEXPORT jlong JNICALL
Java_com_seiko_avif_AvifDecoder_createContext(JNIEnv *env, jclass type,
                                              jbyteArray byteArray,
                                              jint length) {
    avifDecoder *decoder = avifDecoderCreate();
    decoder->maxThreads = 1;
    decoder->ignoreExif = AVIF_TRUE;
    decoder->ignoreXMP = AVIF_TRUE;

    jbyte *buf = env->GetByteArrayElements(byteArray, nullptr);
    avifResult result = avifDecoderSetIOMemory(decoder, (const uint8_t *) buf, length);
    if (result != AVIF_RESULT_OK) {
        goto cleanup;
    }
    result = avifDecoderParse(decoder);
    if (result != AVIF_RESULT_OK) {
        goto cleanup;
    }
    return reinterpret_cast<jlong>(decoder);

    cleanup:
    avifDecoderDestroy(decoder);
    return 0;
}

extern "C" JNIEXPORT void JNICALL
Java_com_seiko_avif_AvifDecoder_destroyContext(JNIEnv *env, jobject type, jlong context) {
    avifDecoder *decoder = reinterpret_cast<avifDecoder*>(context);
    avifDecoderDestroy(decoder);
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_seiko_avif_AvifDecoder_nextImage(JNIEnv *env, jobject type, jlong context) {
    avifDecoder *decoder = reinterpret_cast<avifDecoder*>(context);
    return avifDecoderNextImage(decoder) == AVIF_RESULT_OK;
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_seiko_avif_AvifDecoder_getImage(JNIEnv *env, jobject type, jlong context) {
    avifDecoder *decoder = reinterpret_cast<avifDecoder*>(context);
    return reinterpret_cast<jlong>(decoder->image);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_seiko_avif_AvifDecoder_getImageCount(JNIEnv *env, jobject type, jlong context) {
    avifDecoder *decoder = reinterpret_cast<avifDecoder*>(context);
    return decoder->imageCount;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_seiko_avif_AvifDecoder_getImageDurationMs(JNIEnv *env, jobject type, jlong context) {
    avifDecoder *decoder = reinterpret_cast<avifDecoder*>(context);
    return decoder->imageTiming.duration * 1000; //ms
}
