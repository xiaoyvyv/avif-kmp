package com.seiko.avif

expect class AvifImage {
    fun getWidth(): Int

    fun getHeight(): Int

    fun getDepth(): Int

    // make sure AvifDecoder not close
    fun getFrame(bitmap: PlatformBitmap): Boolean
}

fun AvifImage.getPlatformBitmap(
    width: Int = getWidth(),
    height: Int = getHeight(),
): PlatformBitmap {
    val bitmap = createPlatformBitmap(width, height)
    getFrame(bitmap)
    return bitmap
}