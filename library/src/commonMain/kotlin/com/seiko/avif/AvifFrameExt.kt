package com.seiko.avif

fun AvifFrame.getBitmapResult(): Result<PlatformBitmap> {
    return runCatching {
        createPlatformBitmap().also {
            decodeFrame(it)
        }
    }
}

fun AvifFrame.createPlatformBitmap(): PlatformBitmap {
    return createPlatformBitmap(getWidth(), getHeight())
}
