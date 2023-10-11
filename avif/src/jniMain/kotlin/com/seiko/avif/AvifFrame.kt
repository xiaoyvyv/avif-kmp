package com.seiko.avif

actual class AvifFrame internal constructor(
    private var context: Long,
) {
    actual fun getWidth(): Int {
        return getWidth(context)
    }

    actual fun getHeight(): Int {
        return getHeight(context)
    }

    actual fun getDepth(): Int {
        return getDepth(context)
    }

    actual fun decodeFrame(bitmap: PlatformBitmap) {
        decodeFrame(context, bitmap.ptr)
    }

    private external fun getWidth(context: Long): Int
    private external fun getHeight(context: Long): Int
    private external fun getDepth(context: Long): Int
    private external fun decodeFrame(context: Long, bitmap: PlatformBitmapPtr): Int
}
