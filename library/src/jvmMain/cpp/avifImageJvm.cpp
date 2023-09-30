#include <jni.h>
#include <core/SkBitmap.h>

#include "../../../darwin/libavif/include/avif/avif.h"

extern "C" JNIEXPORT jboolean JNICALL
Java_com_seiko_avif_AvifImage_getFrame(JNIEnv *env, jobject type, jlong context, jlong ptr) {
    avifImage *image = reinterpret_cast<avifImage*>(context);
    SkBitmap* bm = reinterpret_cast<SkBitmap*>(ptr);

    avifRGBImage rgb_image;
    avifRGBImageSetDefaults(&rgb_image, image);

    rgb_image.format = AVIF_RGB_FORMAT_RGBA;
    rgb_image.depth = 8;

    rgb_image.rowBytes = rgb_image.width * avifRGBImagePixelSize(&rgb_image);
    rgb_image.pixels = static_cast<uint8_t *>(bm->getPixels());

    avifResult result = avifImageYUVToRGB(image, &rgb_image);
    if (result != AVIF_RESULT_OK) {
        return 0;
    }

    return 1;
}