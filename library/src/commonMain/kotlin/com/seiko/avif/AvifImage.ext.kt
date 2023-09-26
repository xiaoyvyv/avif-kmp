package com.seiko.avif

fun AvifImage.getPlatformBitmap(
    width: Int = getWidth(),
    height: Int = getHeight(),
): PlatformBitmap {
    val bitmap = createPlatformBitmap(width, height)
    getFrame(bitmap)
    return bitmap
}
