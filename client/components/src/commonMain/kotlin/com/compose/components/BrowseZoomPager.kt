package com.compose.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroidSize
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputScope
import androidx.compose.ui.input.pointer.changedToUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChanged
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.util.fastForEach
import kotlin.math.abs


@Composable
fun <T> BrowseZoomPager(
    modifier: Modifier = Modifier,
    state: BrowseZoomState<T>,
    contentModifier: Modifier = Modifier,
    content: @Composable (contentModifier: Modifier, data: T) -> Unit,
) {

    remember(state.pagerState.currentPage) {
        state.flushed()
    }

    HorizontalPager(
        state = state.pagerState,
        modifier = modifier
            .onSizeChanged {
                state.pagerWidth = it.width.toFloat()
            }
    ) { page ->
        content(
            contentModifier.then(
                if (state.pagerState.currentPage == page) Modifier
                    .graphicsLayer(
                        scaleY = state.scale,
                        scaleX = state.scale,
                        translationX = state.offset.x,
                        translationY = state.offset.y
                    )
                    .pointerInput(page, state.detectZoom)
                else Modifier
            ),
            state[page]
        )
    }

}

@Stable
class BrowseZoomState<T>(
    currentPage: Int = 0,
    private val datas: MutableList<T> = mutableListOf()
) {

    val pagerState = PagerMutableState(currentPage).apply {
        count = datas.size
    }


    private val scaleState = mutableFloatStateOf(1f)
    internal val scale by scaleState
    internal var offset by mutableStateOf(Offset.Zero)

    internal var pagerWidth = 0f

    /**
     * 是否消耗触摸事件
     */
    private var isConsume = false

    internal operator fun get(index: Int): T {
        return datas[index]
    }

    fun flushed() {
        scaleState.floatValue = 1f
        offset = Offset.Zero
    }

    fun addAll(elements: Collection<T>) {
        datas.addAll(elements)
        pagerState.count = datas.size
    }

    internal val detectZoom: suspend PointerInputScope.() -> Unit = {
        //点击次数，用于判断双击
        var tapCount = 0
        //上次点击时间
        var tapUptime = 0L

        var zoom = 1f
        var pastTouchSlop = false
        //触摸无效值，废弃无效范围
        val touchSlop = viewConfiguration.touchSlop
        var pan = Offset.Zero
        awaitEachGesture {

            awaitFirstDown(requireUnconsumed = false)

            do {
                val event = awaitPointerEvent()
                val changes = event.changes

                //是否被取消
                val canceled = changes.fastAny { it.isConsumed }
                //正在按住
                val pressed = changes.fastAny { it.pressed }

                if (pressed) {
                    tapCount = 0
                } else if (changes.fastAny { it.changedToUp() }) { //手指抬起
                    //doubleTapTimeoutMillis：api提供的判断双击事件的时长
                    //本次抬起事件与上次的时间差是否超过双击时长
                    if (tapCount > 0 && changes[0].uptimeMillis - tapUptime <= viewConfiguration.doubleTapTimeoutMillis) {
                        scaleState.value = if (scale >= 2.4f) 1.0f else 2.4f
                        tapCount = 0
                        offset = Offset.Zero
                        break
                    }
                    //记录点击时间
                    tapUptime = changes[0].uptimeMillis
                    tapCount++
                }

                if (!canceled) {
                    val zoomChange = event.calculateZoom()
                    val panChange = event.calculatePan()

                    if (!pastTouchSlop) {
                        zoom *= zoomChange
                        pan += panChange

                        val centroidSize = event.calculateCentroidSize(useCurrent = false)
                        val zoomMotion = abs(1 - zoom) * centroidSize
                        val panMotion = pan.getDistance()

                        //事件变化范围大于废弃值
                        if (zoomMotion > touchSlop || panMotion > touchSlop) {
                            pastTouchSlop = true
                        }
                    }

                    if (pastTouchSlop) {
                        if (zoomChange != 1f || panChange != Offset.Zero)
                            onZoomChange(zoomChange, panChange)
                    }

                    //把事件消耗掉
                    if (isConsume)
                        changes.fastForEach {
                            if (it.positionChanged()) {
                                it.consume()
                            }
                        }

                }
            } while (!canceled && pressed)

        }
    }

    private fun onZoomChange(zoomChange: Float, offsetChange: Offset) {
        if (scale < 1.1f) {
            this.offset = Offset.Zero
        }

        val offset = this.offset + offsetChange * scale

        (scale * zoomChange)
            .takeIf { it in 1f..3f }
            ?.also {
                scaleState.floatValue = it

                //放大之后组件超出副组件的单边宽度
                val beyondWidth = (pagerWidth * it - pagerWidth) / 2
                //移动范围没有超过突出就消耗掉，因为手势已被应用
                isConsume = it > 1.1f && abs(offset.x) < beyondWidth


                if (isConsume) {
                    this.offset = offset
                }
            }
    }

}