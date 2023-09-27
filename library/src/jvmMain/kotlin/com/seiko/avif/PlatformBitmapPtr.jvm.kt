package com.seiko.avif

actual typealias PlatformBitmapPtr = Long

internal actual val PlatformBitmap.ptr: PlatformBitmapPtr
    get() = realPtr
