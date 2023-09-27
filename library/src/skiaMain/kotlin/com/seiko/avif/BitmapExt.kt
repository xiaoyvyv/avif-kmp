package com.seiko.avif

import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.impl.NativePointer

internal val Bitmap.realPtr: NativePointer
    @Suppress("INVISIBLE_MEMBER")
    get() = this._ptr