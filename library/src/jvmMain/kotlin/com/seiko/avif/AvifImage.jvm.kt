package com.seiko.avif

actual typealias BitmapId = Long

@Suppress("INVISIBLE_MEMBER")
internal actual fun getBitmapId(bitmap: PlatformBitmap): Long = bitmap._ptr
