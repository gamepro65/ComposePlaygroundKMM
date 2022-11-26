package ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalDensity

@Composable
fun MainPlayground(
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        var textSize by remember { mutableStateOf<LayoutCoordinates?>(null) }
        var offset by remember { mutableStateOf(Offset(0F, 0F)) }
        var velocity by remember { mutableStateOf(Offset(3F, 3F)) }
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