import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.Window
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import org.jetbrains.skiko.wasm.onWasmReady
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

    onWasmReady {
        val canvas = window.document.getElementById("ComposeTarget") as HTMLCanvasElement
        canvas.width = window.screen.width
        canvas.height = window.screen.height

        if (window.navigator.serviceWorker != null) {
            console.log("### Registering Service Worker")
            window.navigator.serviceWorker.register("/ComposePlaygroundKMM/serviceWorker.js")
                .then { console.log("Service worker registered!") }
                .catch { console.error("Service Worker registration failed: $it") }
        }

        Window("Compose Playground") {
            val clip = with(LocalDensity.current) { innerSize.toDpSize() }
            MainPlayground(
                modifier = Modifier.size(clip)
            )
        }
    }
}

