#include <jni.h>
#include <android/bitmap.h>

#include "avif/avif.h"

#include "helperJni.h"

extern "C" JNIEXPORT jint JNICALL
Java_com_seiko_avif_AvifFrame_decodeFrame(JNIEnv *env, jobject type,
                                          jlong context, jobject bitmap) {
    avifImage *image = reinterpret_cast<avifImage *>(context);

    AndroidBitmapInfo bitmap_info;
    if (AndroidBitmap_getInfo(env, bitmap, &bitmap_info) < 0) {
        return throwIllegalStateException(env, "AndroidBitmap_getInfo failed.");
    }

    if (bitmap_info.format != ANDROID_BITMAP_FORMAT_RGBA_8888 &&
        bitmap_info.format != ANDROID_BITMAP_FORMAT_RGB_565 &&
        bitmap_info.format != ANDROID_BITMAP_FORMAT_RGBA_F16) {
        return throwIllegalStateException(env, "Bitmap format (%d) is not supported.",
                                          bitmap_info.format);
    }

    void *bitmap_pixels = nullptr;
    if (AndroidBitmap_lockPixels(env, bitmap, &bitmap_pixels) != ANDROID_BITMAP_RESULT_SUCCESS) {
        return throwIllegalStateException(env, "Failed to lock Bitmap.");
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
        rgb_image.format = AVIF_RGB_FORMAT_RGBA;
        rgb_image.depth = 8;
    }
    rgb_image.rowBytes = bitmap_info.stride;
    rgb_image.pixels = static_cast<uint8_t *>(bitmap_pixels);

    // Android always sees the Bitmaps as premultiplied with alpha when it renders
    // them:
    // https://developer.android.com/reference/android/graphics/Bitmap#setPremultiplied(boolean)
    rgb_image.alphaPremultiplied = AVIF_TRUE;

    avifResult result = avifImageYUVToRGB(image, &rgb_image);
    AndroidBitmap_unlockPixels(env, bitmap);
    if (result != AVIF_RESULT_OK) {
        return throwIllegalStateException(env, "Failed to convert Image to RGBImage. Status: %s",
                                          avifResultToString(result));
    }
    return result;
}
