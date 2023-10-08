package com.seiko.avif

expect class AvifDecoder : Closeable {
    companion object {

        /**
         * Returns true if the bytes seem like an AVIF image.
         */
        fun isAvifImage(bytes: ByteArray): Boolean

        fun create(bytes: ByteArray): AvifDecoder
    }

    fun nextImage(): Boolean

    fun getImage(): AvifImage

    fun getImageCount(): Int

    fun getImageDurationMs(): Int
}
