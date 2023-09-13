package com.seiko.avif

actual class AvifImage private constructor() {
    actual fun getWidth(): Int {
        TODO()
    }

    actual fun getHeight(): Int {
        TODO()
    }

    actual fun getDepth(): Int {
        TODO()
    }

    actual fun getFrame(bitmap: PlatformBitmap): Boolean {
        TODO()
    }

    actual fun getPlatformBitmap(width: Int, height: Int): PlatformBitmap {
        TODO()
    }
}