package com.seiko.avif

import com.seiko.avif.internal.log
import java.io.Closeable

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

    actual fun getImageCount(): Int {
        return context.toInt()
    }

    actual fun getImageWidth(): Int {
        return getImageWidth(context)
    }

    actual fun getImageHeight(): Int {
        return getImageHeight(context)
    }

    actual fun getFrame(bitmap: Any): Int {
        return getFrame(context, bitmap)
    }

    actual override fun close() {
        val contextToClose = context
        if (contextToClose != 0L) {
            context = 0
            destroyContext(contextToClose)
        }
    }

    protected fun finalize() {
        if (context != 0L) {
            log("warn") { "AvifDecoder($context) leaked!" }
        }
    }

    // private external fun nextImage(context: Long): Boolean
    // private external fun getImageCount(context: Long): Int
    private external fun getImageWidth(context: Long): Int
    private external fun getImageHeight(context: Long): Int
    private external fun getFrame(context: Long, bitmap: Any): Int
    private external fun destroyContext(context: Long)
}
