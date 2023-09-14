package com.seiko.avif

actual class AvifImage private constructor(
    private var context: Long,
) {

    companion object {
        fun create(context: Long): AvifImage {
            return AvifImage(context)
        }
    }

    actual fun getWidth(): Int {
        return getWidth(context)
    }

    actual fun getHeight(): Int {
        return getHeight(context)
    }

    actual fun getDepth(): Int {
        return getDepth(context)
    }

    actual fun getFrame(bitmap: PlatformBitmap): Boolean {
        return getFrame(context, getBitmapId(bitmap))
    }

    private external fun getWidth(context: Long): Int
    private external fun getHeight(context: Long): Int
    private external fun getDepth(context: Long): Int
    private external fun getFrame(context: Long, bitmap: BitmapId): Boolean
}

expect class BitmapId

internal expect fun getBitmapId(bitmap: PlatformBitmap): BitmapId