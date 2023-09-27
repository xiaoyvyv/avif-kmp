//
// Created by Seiko on 9/26/23.
//

#include "avifImageNative.h"

static int getImageFrame(avifImage *image, long bitmapPtr) {
//    avifImage *image = reinterpret_cast<avifImage*>(imagePtr);
//    SkBitmap *bm = reinterpret_cast<SkBitmap*>(bitmapPtr);
//
//    avifRGBImage rgb_image;
//    avifRGBImageSetDefaults(&rgb_image, image);
//
//    rgb_image.format = AVIF_RGB_FORMAT_RGBA;
//    rgb_image.depth = 8;
//
//    rgb_image.rowBytes = rgb_image.width * avifRGBImagePixelSize(&rgb_image);
//    rgb_image.pixels = static_cast<uint8_t *>(bm->getPixels());
//
//    avifResult result = avifImageYUVToRGB(image, &rgb_image);
//    if (result != AVIF_RESULT_OK) {
//        return 0;
//    }
    printf("hello world\n");
    return 1;
}