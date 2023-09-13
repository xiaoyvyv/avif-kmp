package com.seiko.avif

import org.jetbrains.skia.Bitmap

actual typealias PlatformBitmap = Bitmap

actual fun createPlatformBitmap(width: Int, height: Int): PlatformBitmap {
    return Bitmap()
}
