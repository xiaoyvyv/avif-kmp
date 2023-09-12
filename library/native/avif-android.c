#include <jni.h>
#include <android/bitmap.h>

#include "avif/avif.h"

#include "util.h"
#include "util-android.h"

jint JNICALL
Java_com_seiko_avif_AvifDecoder_getFrame(JNIEnv *env, jobject type,
                                         jlong context, jobject bitmap) {
    avifDecoder *decoder = jlong_to_ptr(context);

    AndroidBitmapInfo bitmap_info;
    if (AndroidBitmap_getInfo(env, bitmap, &bitmap_info) < 0) {
        ALOGE("AndroidBitmap_getInfo failed.");
        return AVIF_RESULT_UNKNOWN_ERROR;
    }
    // Ensure that the bitmap is large enough to store the decoded image.
//    if (bitmap_info.width < decoder->crop.width ||
//        bitmap_info.height < decoder->crop.height) {
//        ALOGE(
//                "Bitmap is not large enough to fit the image. Bitmap %dx%d Image "
//                "%dx%d.",
//                bitmap_info.width, bitmap_info.height, decoder->decoder->image->width,
//                decoder->decoder->image->height);
//        return AVIF_RESULT_UNKNOWN_ERROR;
//    }
    // Ensure that the bitmap format is RGBA_8888, RGB_565 or RGBA_F16.
    if (bitmap_info.format != ANDROID_BITMAP_FORMAT_RGBA_8888 &&
        bitmap_info.format != ANDROID_BITMAP_FORMAT_RGB_565 &&
        bitmap_info.format != ANDROID_BITMAP_FORMAT_RGBA_F16) {
        ALOGE("Bitmap format (%d) is not supported.", bitmap_info.format);
        return AVIF_RESULT_NOT_IMPLEMENTED;
    }
    void* bitmap_pixels;
    if (AndroidBitmap_lockPixels(env, bitmap, &bitmap_pixels) !=
        ANDROID_BITMAP_RESULT_SUCCESS) {
        ALOGE("Failed to lock Bitmap.");
        return AVIF_RESULT_UNKNOWN_ERROR;
    }
    avifImage* image = decoder->image;
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
//    rgb_image.pixels = static_cast<uint8_t*>(bitmap_pixels);
    rgb_image.rowBytes = bitmap_info.stride;
    // Android always sees the Bitmaps as premultiplied with alpha when it renders
    // them:
    // https://developer.android.com/reference/android/graphics/Bitmap#setPremultiplied(boolean)
    rgb_image.alphaPremultiplied = AVIF_TRUE;
    avifResult res = avifImageYUVToRGB(image, &rgb_image);
    AndroidBitmap_unlockPixels(env, bitmap);
    if (res != AVIF_RESULT_OK) {
        ALOGE("Failed to convert YUV Pixels to RGB. Status: %d", res);
        return res;
    }
    return AVIF_RESULT_OK;
}