package com.seiko.avif.compose

import androidx.compose.ui.graphics.ImageBitmap
import com.seiko.avif.PlatformBitmap

expect fun PlatformBitmap.asImageBitmap(): ImageBitmap
