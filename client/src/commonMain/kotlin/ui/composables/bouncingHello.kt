package ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun BouncingPlayground(
    modifier: Modifier = Modifier
) {
    var bouncingHelloCount by remember { mutableStateOf(5) }
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
                onClick = { bouncingHelloCount -= 5 },
                enabled = bouncingHelloCount != 0
            ) {
                Text("-")
            }

            Text(text = "Total: $bouncingHelloCount")

            Button(onClick = { bouncingHelloCount += 5 }) {
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