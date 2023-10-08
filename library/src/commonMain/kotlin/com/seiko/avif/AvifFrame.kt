package com.seiko.avif

expect class AvifFrame {

    /**
     * Get the width of the image.
     */
    fun getWidth(): Int

    /**
     * Get the height of the image.
     */
    fun getHeight(): Int

    /**
     * Get the depth (bit depth) of the image.
     */
    fun getDepth(): Int

    /**
     * Decode frame into platform Bitmap
     * PS: make sure AvifDecoder not close
     */
    fun decodeFrame(bitmap: PlatformBitmap)
}
