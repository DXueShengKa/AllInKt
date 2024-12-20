package com.compose.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset

@OptIn(ExperimentalFoundationApi::class)
@Stable
class HorizontalSwipeState(
    val swipeWidth: Dp,
    density: Density,
    swipeBackgroundColor: Color = Color.Unspecified,
    confirmStateChange: (newValue: Boolean) -> Boolean = { true }
) {
    val swipeOffset = with(density) { swipeWidth.toPx() }

    val swipeable = createAnchoredDraggable(
        false,
        anchors = DraggableAnchors {
            false at 0f
            true at -swipeOffset
        },
        confirmValueChange = confirmStateChange
    )


    val backgroundColorModifier = if (swipeBackgroundColor == Color.Unspecified)
        Modifier
    else
        Modifier.background(swipeBackgroundColor)

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalSwipeBox(
    modifier: Modifier = Modifier,
    state: HorizontalSwipeState,
    swipeContent: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier.anchoredDraggable(state.swipeable, Orientation.Horizontal)
    ) {
        val x = state.swipeable.offset
        Box(
            Modifier
                .offset {
                    IntOffset(x = x.toInt(), 0)
                }
                .fillMaxSize(),
            content = content
        )

        if (x < 0) {
            Box(
                Modifier
                    .offset {
                        IntOffset(x = (state.swipeOffset + x).toInt(), 0)
                    }
                    .then(state.backgroundColorModifier)
                    .fillMaxHeight()
                    .width(state.swipeWidth)
                    .align(Alignment.TopEnd),
                content = swipeContent
            )
        }
    }
}

@Composable
fun HorizontalSwipeBox(
    modifier: Modifier = Modifier,
    swipeWidth: Dp,
    swipeBackgroundColor: Color = Color.Unspecified,
    swipeContent: @Composable BoxScope.() -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    HorizontalSwipeBox(
        modifier,
        remember {
            HorizontalSwipeState(
                swipeWidth = swipeWidth,
                density = density,
                swipeBackgroundColor
            )
        },
        swipeContent,
        content
    )
}