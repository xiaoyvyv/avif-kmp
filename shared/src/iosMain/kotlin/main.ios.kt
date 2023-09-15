import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.window.ComposeUIViewController
import com.seiko.avif.PlatformBitmap

fun MainViewController() = ComposeUIViewController { App() }

internal actual fun PlatformBitmap.asImageBitmap(): ImageBitmap {
    return this@asImageBitmap.asComposeImageBitmap()
}
