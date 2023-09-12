#include <jni.h>
#include <android/bitmap.h>

#include "avif/avif.h"

#include "util.h"
#include "util-android.h"

jint JNICALL
Java_com_seiko_avif_AvifDecoder_getFrame(JNIEnv *env, jobject type,
                                         jlong context, jobject bitmap) {
    avifDecoder *decoder = jlong_to_ptr(context);

    int ret;

    avifResult result;
    AndroidBitmapInfo info;
    void *pixels;
    avifRGBImage rgb;

    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0
        || info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        ALOGE("Couldn't get info from Bitmap %s", avifResultToString(ret));
        return 0;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
        ALOGE("Bitmap pixels couldn't be locked %s", avifResultToString(ret));
        return 0;
    }

    avifRGBImageSetDefaults(&rgb, decoder->image);
    rgb.format = AVIF_RGB_FORMAT_RGBA;
    rgb.depth = 8;
    rgb.rowBytes = rgb.width * avifRGBImagePixelSize(&rgb);
    rgb.pixels = pixels;

    result = avifImageYUVToRGB(decoder->image, &rgb);

    AndroidBitmap_unlockPixels(env, bitmap);

    if (result != AVIF_RESULT_OK) {
        jniThrowException(env, "java/lang/IllegalStateException", avifResultToString(result));
    }

    //ms
    return decoder->imageTiming.duration * 1000;
}