package com.compose.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Draggable2DState
import androidx.compose.foundation.gestures.draggable2D
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import com.compose.Icons
import com.compose.icons.KeyboardArrowRight
import com.compose.icons.Refresh
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Instant


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCaptcha(
    state: ImageCaptchaState
) {

    state.coroutineScope = rememberCoroutineScope()

    Layout(
        content = {
            Row(
                Modifier.clickable(onClick = state::onReload),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Refresh, null,
                    Modifier.rotate(state.reloadAnimatable.value),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("刷新图片", color = MaterialTheme.colorScheme.primary)
            }

            Image(
                state.backgroundImage, null,
                Modifier,
                contentScale = ContentScale.FillWidth
            )
            Image(
                state.sliderImage, null,
                Modifier,
                contentScale = ContentScale.FillHeight
            )
            SliderBar(state)
        }
    ) { measurables, constraints ->

        val fixeWidth =   constraints.copy(
            minWidth = constraints.maxWidth,
            maxWidth = constraints.maxWidth,
        )

        val reload = measurables[0].measure(constraints)
        val background = measurables[1].measure(fixeWidth)
        val foreground = measurables[2].measure(
            constraints.copy(
                minHeight = background.height,
                maxHeight = background.height
            )
        )
        val slider = measurables[3].measure(Constraints.fixed(background.width, 40.dp.roundToPx()))

        val spacing = 10.dp.roundToPx()

        state.backgroundSize = background.run { IntSize(width, height) }
        state.sliderSize = foreground.run { IntSize(width, height) }
        state.draggableMaxX = background.width - slider.height

        layout(
            width = background.width,
            height = reload.height + background.height + spacing + slider.height
        ) {
            reload.place(0,0)

            val offsetX = state.offset.x
            background.place(0, reload.height)
            foreground.place(offsetX, reload.height)
            slider.place(0, y = reload.height + background.height + spacing)
        }
    }
}

@ExperimentalFoundationApi
@Composable
private fun SliderBar(state: ImageCaptchaState) {
    val primary = MaterialTheme.colorScheme.primary
    val error = MaterialTheme.colorScheme.error
    val hint = if (state.resultState == CAPTCHA_ERROR) error else MaterialTheme.colorScheme.secondary
    val textMeasurer = rememberTextMeasurer()
    val textStyle = LocalTextStyle.current

    Box(
        Modifier
            .drawWithCache {
                val padding = 3.dp.toPx()
                val height = size.height - padding * 2
                val topLeft = Offset(0f, y = padding)
                val cornerRadius = CornerRadius(size.height / 2)
                val style = Stroke(1.dp.toPx())
                val textLayout = textMeasurer.measure(state.sliderHint, textStyle)
                val textTopLeft = Offset(
                    (size.width - textLayout.size.width) / 2,
                    (size.height - textLayout.size.height) / 2,
                )

                onDrawBehind {
                    drawRoundRect(
                        topLeft = topLeft,
                        size = size.copy(height = height),
                        cornerRadius = cornerRadius,
                        color = hint,
                        style = style
                    )

                    val offsetX = state.offset.x.toFloat()

                    drawRoundRect(
                        topLeft = topLeft,
                        size = Size(width = height + offsetX, height = height),
                        cornerRadius = cornerRadius,
                        color = primary.copy(0.25f)
                    )

                    drawRoundRect(
                        topLeft = topLeft,
                        size = Size(width = height + offsetX, height = height),
                        cornerRadius = cornerRadius,
                        color = primary,
                        style = style
                    )


                    val (left, top) = textTopLeft
                    val textClipX = if (offsetX > left) offsetX - left else 0f
                    clipRect(
                        top = top,
                        bottom = top + textLayout.size.height,
                        left = left + textClipX,
                        right = left + textLayout.size.width,
                    ) {
                        drawText(
                            textLayout,
                            if (state.resultState == CAPTCHA_ERROR) error else primary,
                            textTopLeft
                        )
                    }

                }
            }
            .draggable2D(
                state.draggable2DState,
                enabled = state.resultState == CAPTCHA_NOTHING,
                onDragStarted = state.onDragStarted,
                onDragStopped = state.onDragStopped
            )
    ) {

        Icon(
            Icons.KeyboardArrowRight, null,
            Modifier
                .offset { state.offset }
                .background(primary, CircleShape)
                .fillMaxHeight()
                .aspectRatio(1f),
            MaterialTheme.colorScheme.onPrimary
        )
    }
}

class CaptchaTrack(
    val x: Short,
    val y: Short,
    val t: Int,
    val actionType: Byte
) {
    companion object {
        const val UP: Byte = 1
        const val MOVE: Byte = 2
        const val DOWN: Byte = 3
        const val CLICK: Byte = 4
    }
}


private const val CAPTCHA_ERROR: Byte = -1
private const val CAPTCHA_SUCCESS: Byte = 1
private const val CAPTCHA_NOTHING: Byte = 0

@OptIn(ExperimentalFoundationApi::class)
@Stable
class ImageCaptchaState(
    backgroundImage: ImageBitmap,
    internal var sliderImage: ImageBitmap
) {
    //---------- 记录验证信息 --------------
    val data = ArrayList<CaptchaTrack>(200)
    var backgroundSize = IntSize.Zero
    var sliderSize = IntSize.Zero
    var startSlidingTime = Instant.DISTANT_PAST
    private val endSlidingTimeFlow = MutableSharedFlow<Instant>()

    /**
     * 滑动结束时触发
     */
    val endSlidingTime: Flow<Instant>
        get() = endSlidingTimeFlow
    //---------- 记录验证信息 --------------

    internal var backgroundImage: ImageBitmap by mutableStateOf(backgroundImage)

    val reload = Channel<Unit>()
    internal val reloadAnimatable = Animatable(0f)

    internal var draggableMaxX = 0
    internal var resultState by mutableStateOf<Byte>(CAPTCHA_NOTHING)
        private set

    internal var sliderHint by mutableStateOf("拖动完成上方拼图")

    private val offsetAnimatable = Animatable(IntOffset.Zero, IntOffset.VectorConverter)


    internal val offset: IntOffset
        get() = offsetAnimatable.value


    internal var coroutineScope: CoroutineScope? = null

    internal val draggable2DState = Draggable2DState {
        coroutineScope?.launch {
            val x = offset.x + it.x.toInt()
            if (x in 0..draggableMaxX) {
                offsetAnimatable.snapTo(IntOffset(x = x, 0))
            }
        }
    }

    private var recordJob: Job? = null
    private var recordTime = 0


    internal val onDragStarted: (startedPosition: Offset) -> Unit = Started@{
        if (resultState != CAPTCHA_NOTHING) return@Started
        nothing()

        data.clear()
        recordTime = 0
        data.add(
            CaptchaTrack(offset.x.toShort(), offset.y.toShort(), recordTime, CaptchaTrack.DOWN)
        )
        startSlidingTime = Clock.System.now()

        recordJob = coroutineScope?.launch {
            while (isActive) {
                //20毫秒记录一次
                delay(20)
                val (x, y) = offset
                recordTime += 20
                data.add(
                    CaptchaTrack(x.toShort(), y.toShort(), recordTime, CaptchaTrack.MOVE)
                )

            }
        }
    }

    internal val onDragStopped: (velocity: Velocity) -> Unit = {
        recordJob?.cancel()
        data.add(
            CaptchaTrack(offset.x.toShort(), offset.y.toShort(), recordTime, CaptchaTrack.UP)
        )
        coroutineScope?.launch {
            endSlidingTimeFlow.emit(Clock.System.now())
        }
    }

    fun error() {
        resultState = CAPTCHA_ERROR
        sliderHint = "再来一次"
        onReload()
    }

    fun success() {
        reload.cancel()
        resultState = CAPTCHA_SUCCESS
    }

    private fun nothing() {
        resultState = CAPTCHA_NOTHING
        sliderHint = "拖动完成上方拼图"
    }

    fun upload(backgroundImage: ImageBitmap, sliderImage: ImageBitmap) {
        this.backgroundImage = backgroundImage
        this.sliderImage = sliderImage
        nothing()
        coroutineScope?.launch {
            offsetAnimatable.animateTo(IntOffset.Zero)
            reloadAnimatable.snapTo(0f)
        }
    }

    fun onReload(){
        coroutineScope?.launch {
            reloadAnimatable.animateTo(
                targetValue = 359f,
                animationSpec = infiniteRepeatable(tween(1000))
            )
        }
        reload.trySend(Unit)
    }

}
