package com.seiko.avif

fun AvifImage.getBitmapResult(
    width: Int = getWidth(),
    height: Int = getHeight(),
): Result<PlatformBitmap> {
    val bitmap = createPlatformBitmap(width, height)
    return runCatching {
        getFrame(bitmap)
        bitmap
    }
}
