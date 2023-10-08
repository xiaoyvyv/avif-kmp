package com.seiko.avif

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.refTo
import platform.avif.AVIF_RESULT_OK
import platform.avif.AVIF_TRUE
import platform.avif.avifDecoder
import platform.avif.avifDecoderCreate
import platform.avif.avifDecoderDestroy
import platform.avif.avifDecoderNextImage
import platform.avif.avifDecoderParse
import platform.avif.avifDecoderSetIOMemory
import platform.posix.uint8_tVar

@OptIn(ExperimentalForeignApi::class)
actual class AvifDecoder private constructor(
    private val decoderPtr: CPointer<avifDecoder>,
) : Closeable {
    actual companion object {

        @Suppress("UNCHECKED_CAST")
        actual fun create(bytes: ByteArray): AvifDecoder {
            val decoderPtr = requireNotNull(avifDecoderCreate())
            with(decoderPtr.pointed) {
                maxThreads = 1
                ignoreExif = AVIF_TRUE
                ignoreXMP = AVIF_TRUE
            }
            var result = memScoped {
                avifDecoderSetIOMemory(
                    decoderPtr,
                    bytes.refTo(0).getPointer(this) as CValuesRef<uint8_tVar>,
                    bytes.size.convert(),
                )
            }
            if (result != AVIF_RESULT_OK) {
                avifDecoderDestroy(decoderPtr)
                throw RuntimeException("create AvifDecoder error")
            }
            result = avifDecoderParse(decoderPtr)
            if (result != AVIF_RESULT_OK) {
                avifDecoderDestroy(decoderPtr)
                throw RuntimeException("create AvifDecoder error")
            }
            return AvifDecoder(decoderPtr)
        }
    }

    private val decoder: avifDecoder
        get() = decoderPtr.pointed

    actual fun nextImage(): Boolean {
        return avifDecoderNextImage(decoderPtr) == AVIF_RESULT_OK
    }

    actual fun getImage(): AvifImage {
        val avifImagePtr = requireNotNull(decoder.image)
        return AvifImage.create(avifImagePtr)
    }

    actual fun getImageCount(): Int {
        return decoder.imageCount
    }

    actual fun getImageDurationMs(): Int {
        return decoder.imageTiming.duration.toInt() * 1000 // ms
    }

    override fun close() {
        avifDecoderDestroy(decoderPtr)
    }
}
