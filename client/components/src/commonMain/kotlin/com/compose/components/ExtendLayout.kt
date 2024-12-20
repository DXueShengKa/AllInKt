package com.compose.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ExtendLayout(modifier: Modifier, content: @Composable () -> Unit) {
    val cs = rememberCoroutineScope()
    val state = remember {
        ExtendState(cs).also {
            it.extend()
        }
    }
    Layout(content, modifier.nestedScroll(state), state.measurePolicy)
}


@Stable
private class ExtendState(
    val coroutineScope: CoroutineScope
) : NestedScrollConnection {

    val bodyOffset = Animatable(0, Int.VectorConverter)

    fun extend() {
        coroutineScope.launch {
            bodyOffset.animateTo(headHeight)
        }
    }

    fun close() {
        coroutineScope.launch {
            bodyOffset.animateTo(0)
        }
    }

    var headHeight = 0

    val measurePolicy = MeasurePolicy { measurableList, constraints ->

        val fixed = constraints.copy(minHeight = 0)
        val topBar = measurableList[0].measure(fixed)
        val head = measurableList[1].measure(fixed)
        val h = constraints.maxHeight - topBar.height
        val body = measurableList[2].measure(constraints.copy(minHeight = h, maxHeight = h))
        headHeight = head.height

        layout(constraints.maxWidth, constraints.maxHeight) {
            var y = 0
            topBar.place(0, 0)
            y += topBar.height
            head.place(0, y)

            body.place(0, y + bodyOffset.value, 1f)
        }
    }

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        if (source == NestedScrollSource.UserInput) {
            //y轴滑动距离
            val delta = available.y

            val offset = bodyOffset.value
            if (delta < 0 && offset > 0) {
                coroutineScope.launch {
                    val targetValue = bodyOffset.value + delta.toInt()
                    bodyOffset.snapTo(if (targetValue < 0) 0 else targetValue)
                }
                return available
            } else if (delta > 0 && offset < headHeight) {
                coroutineScope.launch {
                    val targetValue = bodyOffset.value + delta.toInt()
                    bodyOffset.snapTo(if (targetValue > headHeight) headHeight else targetValue)
                }
                return available
            }
        }
        return Offset.Zero
    }

    override suspend fun onPreFling(available: Velocity): Velocity {
        val offset = bodyOffset.value
        if (offset == 0 || offset == headHeight) return Velocity.Zero

        if (offset < headHeight / 2)
            bodyOffset.animateTo(0)
        else
            bodyOffset.animateTo(headHeight)

        return available
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        return available.copy(y = 0f)
    }

    override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
        return when (source) {
            NestedScrollSource.SideEffect -> available.copy(y = 0f)
            else -> Offset.Zero
        }
    }
}
