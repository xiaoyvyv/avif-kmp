package com.seiko.avif.compose

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import com.seiko.avif.PlatformBitmap

actual fun PlatformBitmap.asImageBitmap(): ImageBitmap {
    return this.asComposeImageBitmap()
}
