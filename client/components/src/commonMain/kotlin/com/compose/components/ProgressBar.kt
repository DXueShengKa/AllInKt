package com.compose.components

import androidx.annotation.FloatRange
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/*@Preview
@Composable
fun ProgressBarPreview() {
    var v by remember { mutableStateOf(0.5f) }
    ProgressBar(v, { v = it })
}*/

@Stable
private class ProgressBarState(
    onValueChange: (Float) -> Unit,
) {

    var size: Size = Size.Zero

    private var barLength = 0f

    var percentage: Float = 0f
        set(value) {
            fEnd = fEnd.copy(x = paddingX + barLength * value)
            field = value
        }

    val draggableState = DraggableState {
        val width = size.width

        val oldLen = width * percentage
        val newLen = (oldLen + it) / width

        val l = when {
            newLen > 1f -> 1f
            newLen < 0f -> 0f
            else -> newLen
        }

        onValueChange(l)
    }

    var start = Offset.Zero

    //进度条高度
    var barHeight = 0f

    var bEnd = Offset.Zero
    var fEnd = Offset.Zero

    //X轴边距
    var paddingX = 0f


    fun setSliderSize(size: Size) {
        if (this@ProgressBarState.size == size) return
        this@ProgressBarState.size = size

        val y = size.height / 2

        barHeight = size.height * 0.6f

        paddingX = barHeight / 2

        start = Offset(x = paddingX, y)

        val endX = size.width - paddingX

        bEnd = Offset(endX, y)

        barLength = endX - paddingX

        fEnd = Offset(
            x = paddingX + barLength * percentage,
            y
        )
    }
}

@Composable
fun ProgressBar(
    @FloatRange(0.0,1.0) value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    activeTrackColor: Color = MaterialTheme.colors.primary,
    inactiveTrackColor: Color = MaterialTheme.colors.onPrimary,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val state = remember { ProgressBarState(onValueChange) }

    state.percentage = value

    val pointerInput = if (enabled) {
        val scope = rememberCoroutineScope()
        Modifier.pointerInput(state, interactionSource) {
            detectTapGestures(
                onPress = { pos ->
                    val p = pos.x / state.size.width
                    onValueChange(p)
                    awaitRelease()
                }
            ) {
                scope.launch {
                    state.draggableState.drag(MutatePriority.UserInput) {
                        dragBy(0f)
                    }
                }
            }
        }
    } else Modifier

    Box(
        modifier
            .draggable(
                state.draggableState,
                Orientation.Horizontal,
                enabled,
                interactionSource = interactionSource,
            )
            .then(pointerInput)
            .focusable(enabled, interactionSource)
            .drawBehind {
                state.setSliderSize(size)

                if (value < 1f) drawLine(
                    inactiveTrackColor,
                    start = state.start,
                    end = state.bEnd,
                    strokeWidth = state.barHeight,
                    cap = StrokeCap.Round
                )

                if (value > 0f) drawLine(
                    activeTrackColor,
                    start = state.start,
                    end = state.fEnd,
                    strokeWidth = state.barHeight,
                    cap = StrokeCap.Round
                )
            }
            .defaultMinSize(80.dp, 10.dp)
    )
}