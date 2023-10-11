package com.seiko.avif

actual typealias PlatformBitmapPtr = Any

internal actual val PlatformBitmap.ptr: PlatformBitmapPtr
    get() = this
