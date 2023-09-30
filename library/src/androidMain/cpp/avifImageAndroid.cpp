#include <jni.h>
#include <android/bitmap.h>

#include "avif/avif.h"

#include "logAndroid.h"

extern "C" JNIEXPORT jboolean JNICALL
Java_com_seiko_avif_AvifImage_getFrame(JNIEnv *env, jobject type,
                                       jlong context, jobject bitmap) {
    avifImage *image = reinterpret_cast<avifImage*>(context);

    AndroidBitmapInfo bitmap_info;
    if (AndroidBitmap_getInfo(env, bitmap, &bitmap_info) < 0) {
        ALOGE("AndroidBitmap_getInfo failed.");
        return AVIF_RESULT_UNKNOWN_ERROR;
    }

    if (bitmap_info.format != ANDROID_BITMAP_FORMAT_RGBA_8888 &&
        bitmap_info.format != ANDROID_BITMAP_FORMAT_RGB_565 &&
        bitmap_info.format != ANDROID_BITMAP_FORMAT_RGBA_F16) {
        ALOGE("Bitmap format (%d) is not supported.", bitmap_info.format);
        return AVIF_RESULT_NOT_IMPLEMENTED;
    }

    void* bitmap_pixels = nullptr;
    if (AndroidBitmap_lockPixels(env, bitmap, &bitmap_pixels) != ANDROID_BITMAP_RESULT_SUCCESS) {
        ALOGE("Failed to lock Bitmap.");
        return AVIF_RESULT_UNKNOWN_ERROR;
    }

    avifRGBImage rgb_image;
    avifRGBImageSetDefaults(&rgb_image, image);
    if (bitmap_info.format == ANDROID_BITMAP_FORMAT_RGBA_F16) {
        rgb_image.depth = 16;
        rgb_image.isFloat = AVIF_TRUE;
    } else if (bitmap_info.format == ANDROID_BITMAP_FORMAT_RGB_565) {
        rgb_image.format = AVIF_RGB_FORMAT_RGB_565;
        rgb_image.depth = 8;
    } else {
        rgb_image.depth = 8;
    }
    rgb_image.rowBytes = bitmap_info.stride;
    rgb_image.pixels = static_cast<uint8_t *>(bitmap_pixels);

    avifResult result = avifImageYUVToRGB(image, &rgb_image);
    AndroidBitmap_unlockPixels(env, bitmap);

    if (result != AVIF_RESULT_OK) {
        ALOGE("Failed to convert Image to RGBImage. Status: %s", avifResultToString(result));
        return false;
    }
    return true;
}
