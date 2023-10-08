package com.seiko.avif

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.alloc
import kotlinx.cinterop.get
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString
import platform.avif.AVIF_RESULT_OK
import platform.avif.AVIF_RGB_FORMAT_RGBA
import platform.avif.avifImage
import platform.avif.avifImageYUVToRGB
import platform.avif.avifRGBImage
import platform.avif.avifRGBImageAllocatePixels
import platform.avif.avifRGBImageFreePixels
import platform.avif.avifRGBImageSetDefaults
import platform.avif.avifResultToString

actual class AvifImage private constructor(
    private val avifImagePtr: CPointer<avifImage>,
) {
    companion object {
        fun create(avifImagePtr: CPointer<avifImage>): AvifImage {
            return AvifImage(avifImagePtr)
        }
    }

    private val avifImage: avifImage
        get() = avifImagePtr.pointed

    actual fun getWidth(): Int {
        return avifImage.width.toInt()
    }

    actual fun getHeight(): Int {
        return avifImage.height.toInt()
    }

    actual fun getDepth(): Int {
        return avifImage.depth.toInt()
    }

    actual fun getFrame(bitmap: PlatformBitmap) = memScoped {
        val rgbImage = alloc<avifRGBImage>()
        avifRGBImageSetDefaults(rgbImage.ptr, avifImagePtr)

        rgbImage.format = AVIF_RGB_FORMAT_RGBA
        rgbImage.depth = 8u

        var result = avifRGBImageAllocatePixels(rgbImage.ptr)
        if (result != AVIF_RESULT_OK) {
            error("Failed to allocate pixels to RGBImage. Status: ${avifResultToString(result)?.toKString()}")
        }

        result = avifImageYUVToRGB(avifImagePtr, rgbImage.ptr)
        if (result != AVIF_RESULT_OK) {
            avifRGBImageFreePixels(rgbImage.ptr)
            error("Failed to convert Image to RGBImage. Status: ${avifResultToString(result)?.toKString()}")
        }

        with(rgbImage) {
            pixels?.let { pixels ->
                val pixelsByteArray = ByteArray((rowBytes * height).toInt()) {
                    pixels[it].toByte()
                }
                bitmap.installPixels(pixelsByteArray)
            }
        }
        avifRGBImageFreePixels(rgbImage.ptr)
    }
}
