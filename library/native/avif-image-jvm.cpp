#include <jni.h>
#include <core/SkBitmap.h>

#include "avif/avif.h"

extern "C" JNIEXPORT jboolean JNICALL
Java_com_seiko_avif_AvifImage_getFrame(JNIEnv *env, jobject type, jlong context, jobject bitmap) {
    avifImage *image = reinterpret_cast<avifImage*>(context);

    SkBitmap *bm = reinterpret_cast<SkBitmap*>(bitmap);
    void *pixels = bm->getPixels();

    avifRGBImage rgb;
    rgb.format = AVIF_RGB_FORMAT_RGBA;
    rgb.depth = 8;
    rgb.rowBytes = rgb.width * avifRGBImagePixelSize(&rgb);
    rgb.pixels = static_cast<uint8_t *>(pixels);

    avifResult result = avifImageYUVToRGB(image, &rgb);
    if (result != AVIF_RESULT_OK) {
        return 0;
    }

    return 1;
}