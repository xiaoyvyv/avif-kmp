import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.seiko.avif.AvifDecoder
import com.seiko.avif.AvifImage
import com.seiko.avif.PlatformBitmap
import com.seiko.avif.getBitmapResult
import com.seiko.avif.use
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.resource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            var title by remember { mutableStateOf("") }

            val painter by produceState<Painter>(EmptyPainter) {
                val bytes = resource("test.avif").readBytes()
                title += "isAvif=${AvifDecoder.isAvifImage(bytes)}\n"

                AvifDecoder.create(bytes).use { decoder ->
                    val image = getFirstFrameImageBitmap(decoder)
                    title += "width=${image.getWidth()}, height=${image.getHeight()}\n"

                    image.getBitmapResult().onSuccess {
                        value = BitmapPainter(it.asImageBitmap())
                    }.onFailure {
                        println(it.message.orEmpty())
                    }
                }
            }
            Text(title, textAlign = TextAlign.Center)
            Spacer(Modifier.height(8.dp))
            Image(
                painter,
                null,
                Modifier.size(200.dp),
            )
        }
    }
}

private object EmptyPainter : Painter() {
    override val intrinsicSize get() = Size.Unspecified
    override fun DrawScope.onDraw() = Unit
}

private fun getFirstFrameImageBitmap(decoder: AvifDecoder): AvifImage {
    decoder.nextImage()
    return decoder.getImage()
}

internal expect fun PlatformBitmap.asImageBitmap(): ImageBitmap
