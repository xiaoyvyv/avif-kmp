package com.seiko.avif

actual typealias BitmapId = Any

internal actual fun getBitmapId(bitmap: PlatformBitmap): BitmapId = bitmap
