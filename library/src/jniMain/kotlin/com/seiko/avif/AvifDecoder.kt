package com.seiko.avif

import com.seiko.avif.internal.log

actual class AvifDecoder private constructor(
    private var context: Long,
) : Closeable {
    actual companion object {
        init {
            loadNativeLibrary()
        }

        @JvmStatic
        actual fun create(bytes: ByteArray): AvifDecoder {
            return AvifDecoder(createContext(bytes, bytes.size))
        }

        @JvmStatic
        external fun createContext(bytes: ByteArray, length: Int): Long
    }

    actual fun nextImage(): Boolean {
        return nextImage(context)
    }

    actual fun getImage(): AvifImage {
        return AvifImage.create(getImage(context))
    }

    actual fun getImageCount(): Int {
        return getImageCount(context)
    }

    actual fun getImageDurationMs(): Int {
        return getImageDurationMs(context)
    }

    override fun close() {
        val contextToClose = context
        if (contextToClose != 0L) {
            context = 0L
            destroyContext(contextToClose)
        }
    }

    protected fun finalize() {
        if (context != 0L) {
            log("warn") { "AvifDecoder($context) leaked!" }
        }
    }

    private external fun nextImage(context: Long): Boolean
    private external fun getImage(context: Long): Long
    private external fun getImageCount(context: Long): Int
    private external fun getImageDurationMs(context: Long): Int
    private external fun destroyContext(context: Long)
}
