package com.seiko.avif

actual class AvifDecoder private constructor() {
    actual companion object {
        actual fun create(bytes: ByteArray): AvifDecoder {
            return AvifDecoder()
        }
    }

    actual fun getImageCount(): Int {
        return 0
    }
}
