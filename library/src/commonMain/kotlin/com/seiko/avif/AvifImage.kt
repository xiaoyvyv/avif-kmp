package com.seiko.avif

expect class AvifImage {
    fun getWidth(): Int

    fun getHeight(): Int

    fun getDepth(): Int

    // make sure AvifDecoder not close
    fun getFrame(bitmap: PlatformBitmap): Boolean
}
