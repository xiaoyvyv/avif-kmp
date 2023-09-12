import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.seiko.avif.AvifDecoder

@Composable
fun MainView() = App()

actual fun generateImageBitmap(decoder: AvifDecoder): ImageBitmap {
    return decoder.use {
        it.nextImage() // first image is empty
        Bitmap.createBitmap(
            it.getImageWidth(),
            it.getImageHeight(),
            Bitmap.Config.ARGB_8888,
        ).also { bitmap ->
            it.getFrame(bitmap)
        }.asImageBitmap()
    }
}

actual fun getPlatformName(): String = "Android"
