package ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity

@Composable
fun BouncingSlot(
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