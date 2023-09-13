import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.seiko.avif.PlatformBitmap

@Composable fun MainView() = App()

@Preview
@Composable
fun AppPreview() {
    App()
}

internal actual fun PlatformBitmap.asImageBitmap(): ImageBitmap {
    return this@asImageBitmap.asComposeImageBitmap()
}

// internal actual fun ByteArray.toImageBitmap(): ImageBitmap {
//     return org.jetbrains.skia.Image.makeFromEncoded(this).toComposeImageBitmap()
// }
