@file:OptIn(ExperimentalTime::class, ExperimentalAnimationApi::class)

package ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import kotlin.random.Random
import kotlin.time.ExperimentalTime
import kotlin.time.TimeSource

@Composable
fun MainPlayground(
    modifier: Modifier = Modifier
) {
    PerformanceBox(modifier = modifier.fillMaxSize()) {
        var targetScreen by remember { mutableStateOf<@Composable () -> Unit>({
            BoxWithConstraints {
                with (Random.Default) {
                    with (LocalDensity.current) {
                        for (i in 0..5)
                            for (j in 0..5) {
                                BouncingHello(
                                    startOffset = Offset(maxWidth.toPx() * nextFloat(), maxHeight.toPx() * nextFloat()),
                                    startVelocity = Offset(nextFloat() * 4F, nextFloat() * 4F)
                                )
                            }
                    }
                }
            }
        }) }
        Crossfade(
            modifier = Modifier.fillMaxSize(),
            targetState = targetScreen
        ) { content -> content() }
    }
}

@Composable
private fun BouncingHello(
    modifier: Modifier = Modifier,
    startOffset: Offset = Offset.Zero,
    startVelocity: Offset = Offset(3F, 3F),
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        var textSize by remember { mutableStateOf<LayoutCoordinates?>(null) }
        var offset by remember { mutableStateOf(startOffset) }
        var velocity by remember { mutableStateOf(startVelocity) }
        if (offset.x + (textSize?.size?.width ?: 0) > with(LocalDensity.current) { maxWidth.toPx() } || offset.x < 0) {
            velocity = velocity.copy(x = -velocity.x)
        }
        if (offset.y + (textSize?.size?.height ?: 0) > with(LocalDensity.current) { maxHeight.toPx() } || offset.y < 0) {
            velocity = velocity.copy(y = -velocity.y)
        }

        Text(
            modifier = Modifier.offset(
                with(LocalDensity.current) { offset.x.toDp() },
                with(LocalDensity.current) { offset.y.toDp() }
            ).onPlaced { textSize = it },
            text = "Hello Playground!"
        )

        offset = Offset(offset.x + velocity.x, offset.y + velocity.y)
    }
}

@OptIn(ExperimentalTime::class)
@Composable
private fun PerformanceBox(
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