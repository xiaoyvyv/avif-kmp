package com.seiko.avif

import kotlinx.cinterop.ptr
import platform.avif.avifImage
import platform.avif.getImageFrame

actual class AvifImage private constructor(
    private val avifImage: avifImage,
) {
    companion object {
        fun create(avifImage: avifImage): AvifImage {
            return AvifImage(avifImage)
        }
    }

    actual fun getWidth(): Int {
        return avifImage.width.toInt()
    }

    actual fun getHeight(): Int {
        return avifImage.height.toInt()
    }

    actual fun getDepth(): Int {
        return avifImage.depth.toInt()
    }

    actual fun getFrame(bitmap: PlatformBitmap): Boolean {
        // val rgbImageRef = cValue<avifRGBImage> {
        //     format = AVIF_RGB_FORMAT_RGBA
        //     depth = 8u
        //     rowBytes = width * avifRGBImagePixelSize(this.ptr)
        //     pixels = bitmap.pixelRef.objcPtr() as CPointer<uint8_tVar>
        // }
        // avifRGBImageSetDefaults(rgbImageRef, avifImage.ptr)
        //
        // val result = avifImageYUVToRGB(avifImage.ptr, rgbImageRef)
        // return result == AVIF_RESULT_OK
        return getImageFrame(avifImage.ptr, bitmap.realPtr.toLong()) > 0
    }
}