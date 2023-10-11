package com.seiko.avif

import android.graphics.Bitmap

actual typealias PlatformBitmap = Bitmap

actual fun createPlatformBitmap(width: Int, height: Int): PlatformBitmap {
    return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
}
