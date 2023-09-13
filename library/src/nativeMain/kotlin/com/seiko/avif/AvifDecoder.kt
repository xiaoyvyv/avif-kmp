package com.seiko.avif

actual class AvifDecoder private constructor() {
    actual companion object {
        actual fun create(bytes: ByteArray): AvifDecoder {
            return AvifDecoder()
        }
    }

    actual fun nextImage(): Boolean {
        TODO()
    }

    actual fun getImage(): AvifImage {
        TODO()
    }

    actual fun getImageCount(): Int {
        TODO()
    }

    actual fun getImageDurationMs(): Int {
        TODO()
    }

    actual fun close() {
        TODO()
    }
}
