package com.seiko.avif

import kotlinx.cinterop.get
import kotlinx.cinterop.ptr
import kotlinx.cinterop.useContents
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
        // var rgbImageRef = cValue<avifRGBImage>()
        //
        // avifRGBImageSetDefaults(rgbImageRef, avifImage.ptr)
        //
        // val isImageRequires64Bit = avifImageUsesU16(avifImage.ptr) > 0
        // println("isImageRequires64Bit=$isImageRequires64Bit")
        //
        // rgbImageRef = rgbImageRef.copy {
        //     format = AVIF_RGB_FORMAT_RGBA
        //     if (isImageRequires64Bit) {
        //         alphaPremultiplied = 1
        //         isFloat = 1
        //         depth = 16u
        //     } else {
        //         alphaPremultiplied = 1
        //         depth = 8u
        //     }
        // }
        //
        // var result = avifRGBImageAllocatePixels(rgbImageRef)
        // if (result != AVIF_RESULT_OK) {
        //     avifRGBImageFreePixels(rgbImageRef)
        //     throw RuntimeException("Allocation of RGB failed: ${avifResultToString(result)?.toKString()}")
        // }
        //
        // result = avifImageYUVToRGB(avifImage.ptr, rgbImageRef)
        // if (result != AVIF_RESULT_OK) {
        //     avifRGBImageFreePixels(rgbImageRef)
        //     println("Conversion from YUV failed: ${avifResultToString(result)?.toKString()}")
        // }
        //
        // rgbImageRef.useContents {
        //     println("rgbImage: depth=$depth, rowBytes=${rowBytes}")
        //     println("rgbImage: $width x $height")
        // }
        //
        // avifRGBImageFreePixels(rgbImageRef)

        val rgb = getImageFrame(avifImage.ptr)
        rgb.useContents {
            val pixelsByteArray = ByteArray((rowBytes * height).toInt()) {
                pixels!![it].toByte()
            }
            bitmap.installPixels(pixelsByteArray)
        }
        return false
    }
}