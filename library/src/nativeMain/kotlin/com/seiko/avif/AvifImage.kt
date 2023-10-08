package com.seiko.avif

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.get
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.useContents
import platform.avif.avifImage
import platform.avif.avifRGBImageFreePixels
import platform.avif.getImageFrame

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

    actual fun getFrame(bitmap: PlatformBitmap): Boolean {
        val rgbPtr = getImageFrame(avifImage.ptr)
        rgbPtr.useContents {
            pixels?.let { pixels ->
                val pixelsByteArray = ByteArray((rowBytes * height).toInt()) {
                    pixels[it].toByte()
                }
                bitmap.installPixels(pixelsByteArray)
            }
        }
        avifRGBImageFreePixels(rgbPtr)
        return false
    }
}
