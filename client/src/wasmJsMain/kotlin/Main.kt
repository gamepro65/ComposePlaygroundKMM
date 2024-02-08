import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import kotlinx.browser.window
import org.w3c.dom.HTMLCanvasElement
import ui.Main
import kotlin.math.max

fun main() {
    var windowSize by mutableStateOf(
        Size(
            window.innerWidth.toFloat(),
            window.innerHeight.toFloat()
        )
    )

    window.onresize = {
        windowSize = Size(
            window.innerWidth.toFloat(),
            window.innerHeight.toFloat()
        )
    }

    val canvas = window.document.getElementById("ComposeTarget") as HTMLCanvasElement
    canvas.width = max(window.screen.width, window.screen.height)
    canvas.height = max(window.screen.width, window.screen.height)

    try {
        println("### Registering Service Worker")
        window.navigator.serviceWorker.register("/ComposePlaygroundKMM/serviceWorker.js")
            .then { println("Service worker registered!"); it }
            .catch { println("Service Worker registration failed: $it"); it }
    } catch (e: Throwable) {
        println("###: ${e.message}")
    }

    Window("Compose Playground") {
        Main(
            modifier = Modifier.size(
                DpSize(
                    windowSize.width.toInt().dp,
                    windowSize.height.toInt().dp
                )
            )
        )
    }
}

