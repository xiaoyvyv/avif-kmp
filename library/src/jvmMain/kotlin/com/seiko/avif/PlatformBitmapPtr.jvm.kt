package com.seiko.avif

actual typealias PlatformBitmapPtr = Long

@Suppress("INVISIBLE_MEMBER")
internal actual val PlatformBitmap.ptr: PlatformBitmapPtr
    get() = this._ptr
