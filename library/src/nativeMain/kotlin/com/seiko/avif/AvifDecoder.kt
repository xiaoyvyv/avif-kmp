package com.seiko.avif

import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.convert
import kotlinx.cinterop.get
import kotlinx.cinterop.ptr
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
    private val decoder: avifDecoder,
) {
    actual companion object {

        @Suppress("UNCHECKED_CAST")
        actual fun create(bytes: ByteArray): AvifDecoder {
            val decoderRef = avifDecoderCreate()
            val decoder = requireNotNull(decoderRef)[0]

            decoder.maxThreads = 1
            decoder.ignoreExif = AVIF_TRUE
            decoder.ignoreXMP = AVIF_TRUE

            var result = avifDecoderSetIOMemory(
                decoder.ptr,
                bytes.refTo(0) as CValuesRef<uint8_tVar>,
                bytes.size.convert(),
            )
            if (result != AVIF_RESULT_OK) {
                avifDecoderDestroy(decoder.ptr)
                throw RuntimeException("create AvifDecoder error")
            }
            result = avifDecoderParse(decoder.ptr)
            if (result != AVIF_RESULT_OK) {
                avifDecoderDestroy(decoder.ptr)
                throw RuntimeException("create AvifDecoder error")
            }
            return AvifDecoder(decoder)
        }
    }

    actual fun nextImage(): Boolean {
        return avifDecoderNextImage(decoder.ptr) == AVIF_RESULT_OK
    }

    actual fun getImage(): AvifImage {
        return AvifImage.create(requireNotNull(decoder.image)[0])
    }

    actual fun getImageCount(): Int {
        return decoder.imageCount
    }

    actual fun getImageDurationMs(): Int {
        return decoder.imageTiming.duration.toInt() * 1000 // ms
    }

    actual fun close() {
        avifDecoderDestroy(decoder.ptr)
    }
}
