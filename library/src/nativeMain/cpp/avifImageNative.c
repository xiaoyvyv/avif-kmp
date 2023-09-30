//
// Created by Seiko on 9/26/23.
//

#include "avifImageNative.h"

avifRGBImage getImageFrame(avifImage *image) {
    avifRGBImage rgb;
    avifRGBImageSetDefaults(&rgb, image);

    rgb.format = AVIF_RGB_FORMAT_RGBA;
    rgb.depth = 8;

    avifResult result = avifRGBImageAllocatePixels(&rgb);
    if (result != AVIF_RESULT_OK) {
//        return 0;
    }

    result = avifImageYUVToRGB(image, &rgb);
    if (result != AVIF_RESULT_OK) {
//        return 0;
    }

    return rgb;
}
