package com.seiko.avif

fun AvifFrame.getBitmapResult(
    width: Int = getWidth(),
    height: Int = getHeight(),
): Result<PlatformBitmap> {
    return runCatching {
        createPlatformBitmap(width, height).also {
            decodeFrame(it)
        }
    }
}
