package com.seiko.avif

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.cValue
import kotlinx.cinterop.cValuesOf
import kotlinx.cinterop.objcPtr
import kotlinx.cinterop.ptr
import platform.avif.avifImage
import platform.posix.uint8_tVar

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

    @OptIn(ExperimentalForeignApi::class)
    @Suppress("CAST_NEVER_SUCCEEDS")
    actual fun getFrame(bitmap: PlatformBitmap): Boolean {
        TODO()
        // val rgbImageRef = cValue<avifRGBImage>()

        // avifRGBImageSetDefaults(rgbImageRef, avifImage.ptr)
        //
        // val rgbImage = avifRGBImage(rgbImageRef.objcPtr())
        // rgbImage.format = AVIF_RGB_FORMAT_RGBA
        // rgbImage.depth = 8u
        //
        // rgbImage.rowBytes = rgbImage.width * avifRGBImagePixelSize(rgbImageRef)
        // rgbImage.pixels = bitmap.pixelRef as CPointer<uint8_tVar>
        //
        // val result = avifImageYUVToRGB(avifImage.ptr, rgbImageRef)
        // return result == AVIF_RESULT_OK
    }
}