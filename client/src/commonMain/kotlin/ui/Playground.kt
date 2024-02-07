@file:OptIn(ExperimentalAnimationApi::class)

package ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.random.Random
import kotlin.time.TimeSource

@Composable
fun MainPlayground(
    modifier: Modifier = Modifier
) {
    PerformanceBox(modifier = modifier.fillMaxSize()) {
        var targetScreen by remember { mutableStateOf(Screens.HOME) }
        Crossfade(
            modifier = Modifier.fillMaxSize(), targetState = targetScreen
        ) { content ->
            Column {
                Row(modifier = Modifier.background(Color.LightGray).fillMaxWidth()) {
                    Button(
                        modifier = Modifier.padding(8.dp),
                        onClick = { targetScreen = Screens.HOME },
                        enabled = targetScreen != Screens.HOME
                    ) {
                        Image(
                            painter = rememberVectorPainter(Icons.Default.ArrowBack),
                            contentDescription = null
                        )
                    }
                }

                when (content) {
                    Screens.HOME -> WelcomeScreen { targetScreen = it }
                    Screens.BOUNCING_HELLO -> BouncingPlayground()
                }
            }
        }
    }
}

@Composable
private fun WelcomeScreen(
    modifier: Modifier = Modifier, goTo: (Screens) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Welcome to the Playground"
        )

        Button(onClick = { goTo(Screens.BOUNCING_HELLO) }) {
            Text("Bouncing Demo")
        }
    }
}

@Composable
private fun BouncingPlayground(
    modifier: Modifier = Modifier
) {
    var bouncingHelloCount by remember { mutableStateOf(0) }
    Box(modifier = modifier.fillMaxSize()) {
        for (i in 0 until bouncingHelloCount) {
            RandomBouncingHello()
        }

        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = { bouncingHelloCount-- },
                enabled = bouncingHelloCount != 0
            ) {
                Text("-")
            }

            Text(text = "Total: $bouncingHelloCount")

            Button(onClick = { bouncingHelloCount++ }) {
                Text("+")
            }
        }
    }
}

@Composable
private fun RandomBouncingHello() {
    BoxWithConstraints {
        with(Random.Default) {
            with(LocalDensity.current) {
                BouncingSlot(
                    startOffset = Offset(
                        maxWidth.toPx() * (.05F + nextFloat() * .5F),
                        maxHeight.toPx() * (.05F + nextFloat() * .5F)
                    ), startVelocity = Offset(nextFloat() * 4F, nextFloat() * 4F)
                ) {
                    Text(text = "Hello Playground!")
                }
            }
        }
    }
}

@Composable
private fun BouncingSlot(
    modifier: Modifier = Modifier,
    startOffset: Offset = Offset.Zero,
    startVelocity: Offset = Offset(3F, 3F),
    content: @Composable BoxScope.() -> Unit,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        var textSize by remember { mutableStateOf<LayoutCoordinates?>(null) }
        var offset by remember { mutableStateOf(startOffset) }
        var velocity by remember { mutableStateOf(startVelocity) }
        textSize?.let { textSize ->
            with (LocalDensity.current) {
                if (offset.x + textSize.size.width > maxWidth.toPx() || offset.x < 0) {
                    velocity = velocity.copy(x = -velocity.x)
                }

                if (offset.y + textSize.size.height > maxHeight.toPx() || offset.y < 0) {
                    velocity = velocity.copy(y = -velocity.y)
                }
            }
        }

        Box(
            modifier =
                Modifier
                    .onPlaced { textSize = it }
                    .graphicsLayer {
                        translationX = offset.x
                        translationY = offset.y
                    },
            content = content
        )

        offset = Offset(offset.x + velocity.x, offset.y + velocity.y)
    }
}

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

enum class Screens {
    HOME, BOUNCING_HELLO
}