import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.seiko.avif.PlatformBitmap

@Composable
fun MainView() = App()

internal actual fun PlatformBitmap.asImageBitmap(): ImageBitmap {
    return this@asImageBitmap.asImageBitmap()
}
