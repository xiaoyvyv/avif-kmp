package com.seiko.avif.compose

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.seiko.avif.PlatformBitmap

actual fun PlatformBitmap.asImageBitmap(): ImageBitmap {
    return this.asImageBitmap()
}
