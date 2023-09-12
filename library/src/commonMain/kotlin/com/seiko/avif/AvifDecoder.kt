package com.seiko.avif

expect class AvifDecoder {
    companion object {
        fun create(bytes: ByteArray): AvifDecoder
    }

    fun nextImage(): Boolean

    fun getImageCount(): Int

    fun getImageWidth(): Int
    fun getImageHeight(): Int

    fun getFrame(bitmap: Any): Int

    fun close()
}
