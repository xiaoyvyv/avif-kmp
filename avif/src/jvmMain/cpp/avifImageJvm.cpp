#include <jni.h>
#include <core/SkBitmap.h>

#include "avif/avif.h"

#include "helperJni.h"

extern "C" JNIEXPORT jint JNICALL
Java_com_seiko_avif_AvifFrame_decodeFrame(JNIEnv *env, jobject type, jlong context, jlong ptr) {
    avifImage *image = reinterpret_cast<avifImage *>(context);

    SkBitmap *bm = reinterpret_cast<SkBitmap *>(ptr);

    const SkColorType colorType = bm->info().colorType();
    if (colorType != kRGBA_8888_SkColorType &&
        colorType != kRGB_565_SkColorType &&
        colorType != kRGBA_F16_SkColorType) {
        return throwIllegalStateException(env, "Bitmap colorType (%d) is not supported.",
                                          colorType);
    }

    avifRGBImage rgb_image;
    avifRGBImageSetDefaults(&rgb_image, image);

    if (colorType == kRGBA_F16_SkColorType) {
        rgb_image.depth = 16;
        rgb_image.isFloat = AVIF_TRUE;
    } else if (colorType == kRGB_565_SkColorType) {
        rgb_image.format = AVIF_RGB_FORMAT_RGB_565;
        rgb_image.depth = 8;
    } else {
        rgb_image.format = AVIF_RGB_FORMAT_RGBA;
        rgb_image.depth = 8;
    }
    rgb_image.rowBytes = image->width * avifRGBImagePixelSize(&rgb_image);
    rgb_image.pixels = static_cast<uint8_t *>(bm->getPixels());

    avifResult result = avifImageYUVToRGB(image, &rgb_image);
    if (result != AVIF_RESULT_OK) {
        return throwIllegalStateException(env, "Failed to convert Image to RGBImage. Status: %s",
                                          avifResultToString(result));
    }
    return result;
}
