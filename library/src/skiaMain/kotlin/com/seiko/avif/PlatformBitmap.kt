package com.seiko.avif

import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo

actual typealias PlatformBitmap = Bitmap

actual fun createPlatformBitmap(width: Int, height: Int): PlatformBitmap {
    return Bitmap().apply {
        allocPixels(
            ImageInfo(width, height, ColorType.RGBA_8888, ColorAlphaType.PREMUL)
        )
    }
}
