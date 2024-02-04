
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.Window
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import org.w3c.dom.HTMLCanvasElement
import org.w3c.workers.ServiceWorkerGlobalScope
import ui.MainPlayground

external val self: ServiceWorkerGlobalScope
val scope = MainScope()

fun main() {
    var innerSize by mutableStateOf(Size.Zero)
    window.onresize = {
        innerSize = Size(
            window.innerWidth.toFloat(),
            window.innerHeight.toFloat()
        )
        Unit
    }

    window.onload = {
        innerSize = Size(
            window.innerWidth.toFloat(),
            window.innerHeight.toFloat()
        )
        Unit
    }

//    onWasmReady {
        val canvas = window.document.getElementById("ComposeTarget") as HTMLCanvasElement
        canvas.width = window.screen.width
        canvas.height = window.screen.height

        if (window.navigator.serviceWorker != null) {
            println("### Registering Service Worker")
            window.navigator.serviceWorker.register("/ComposePlaygroundKMM/serviceWorker.js")
                .then { println("Service worker registered!"); it }
                .catch { println("Service Worker registration failed: $it"); it }
        }

        Window("Compose Playground") {
            val clip = with(LocalDensity.current) { innerSize.toDpSize() }
            MainPlayground(
                modifier = Modifier.size(clip)
            )
        }
//    }
}

