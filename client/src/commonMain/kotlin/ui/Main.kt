@file:OptIn(ExperimentalAnimationApi::class)

package ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import ui.composables.BouncingPlayground
import ui.composables.PerformanceBox

@Composable
fun Main(
    modifier: Modifier = Modifier
) {
    PerformanceBox(modifier = modifier.fillMaxSize()) {
        var targetScreen by remember { mutableStateOf(Screens.HOME) }
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

            Crossfade(
                modifier = Modifier.fillMaxSize(), targetState = targetScreen
            ) { content ->
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
    modifier: Modifier = Modifier,
    goTo: (Screens) -> Unit
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


enum class Screens {
    HOME, BOUNCING_HELLO
}