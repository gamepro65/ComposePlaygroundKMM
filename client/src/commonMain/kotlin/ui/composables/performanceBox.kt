package ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import kotlin.time.TimeSource

@Composable
fun PerformanceBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val frameTimes = remember { mutableStateListOf<Long>() }
    var lastFrameTime by remember { mutableStateOf(TimeSource.Monotonic.markNow()) }
    Box(modifier = modifier) {
        content()

        Text(
            modifier = Modifier.align(Alignment.TopEnd).drawWithContent {
                drawContent()
                frameTimes.add(lastFrameTime.elapsedNow().inWholeMilliseconds)
                if (frameTimes.size > 100) {
                    frameTimes.removeFirst()
                }
                lastFrameTime = TimeSource.Monotonic.markNow()
            },
            text = "FPS: ${(1000.0 / frameTimes.average()).toInt()}"
        )
    }
}
