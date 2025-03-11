package com.compose.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap

import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.util.fastAny
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext


/**
 * 图片裁剪
 */
@Composable
fun CropImage(
    state: CropImageState,
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.primary
) {

    Canvas(
        modifier
            .fillMaxSize()
            .onSizeChanged {
                val imgWidth = state.imageBitmap.width
                val imgHeight = state.imageBitmap.height

                val bWidth: Float
                val bHeight: Float
                if (state.aspect > 1.5f) {
                    bWidth = it.width / state.aspect
                    bHeight = it.height.toFloat()
                } else {
                    bWidth = it.width.toFloat()
                    bHeight = it.width * state.aspect
                }

                state.isBaseW = bWidth * imgHeight > bHeight * imgWidth
                state.scale = bWidth / if (state.isBaseW) imgWidth else imgHeight
                state.borderSize = Size(bWidth, bHeight)
            }
            .pointerInput(state) {
                state.coroutineScope = CoroutineScope(coroutineContext)

                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    do {
                        val event = awaitPointerEvent()
                        val canceled = event.changes.fastAny { it.isConsumed }

                        if (!canceled) {
                            val zoomChange = event.calculateZoom()
                            val panChange = event.calculatePan()

                            if (state.isRotation) {
                                state.rotation += event.calculateRotation()
                            }

                            state.transform(zoomChange, panChange)
                        }
                    } while (!canceled && event.changes.fastAny { it.pressed })

                    state.offsetCorrection()
                }

            }
    ) {
        if (size.width > state.borderSize.width)
            drawContext.transform.translate((size.width - state.borderSize.width) / 2, 0f)

        withTransform({
            val offset = state.offset.value
            translate(offset.x, offset.y)
            rotate(state.rotation)
            val zoom = state.zoom
            scale(zoom, zoom, Offset.Zero)
        }) {
            val image = state.imageBitmap

            drawImage(
                image,
                dstSize = if (state.isBaseW) IntSize(size.width.toInt(), (image.height * state.scale).toInt()) else
                    IntSize((image.width * state.scale).toInt(), size.width.toInt()),
                dstOffset = IntOffset.Zero
            )
        }

        drawRect(
            borderColor,
            size = state.borderSize,
            style = Stroke(2f),
            alpha = 0.8f
        )
    }
}

@Immutable
class CropImageState(internal val imageBitmap: ImageBitmap, val aspect: Float = 1.0f) {


    var zoom by mutableFloatStateOf(1f)
    var rotation by mutableFloatStateOf(0f)
    val offset = Animatable(Offset.Zero, Offset.VectorConverter)

    internal lateinit var coroutineScope: CoroutineScope

    /**
     * 图片裁剪框的大小
     */
    internal var borderSize = Size.Zero

    private var scaleSize = Size.Zero

    /**
     * 是否允许旋转
     */
    var isRotation: Boolean = false
        set(value) {
            field = value
            if (!value) rotation = 0f
        }

    internal var isBaseW = true

    /**
     * 缩放图片，使图片完整显示到组件里
     */
    internal var scale: Float = 1f
        set(value) {
            field = value
            scaleSize = Size(imageBitmap.width * value, imageBitmap.height * value)
        }

    internal fun transform(zoomChange: Float, panChange: Offset) {
        val z = zoom * zoomChange
        //最大两倍
        if (z in 1f..2f) {
            zoom = z
        }

        coroutineScope.launch {
            val o: Offset = offset.value + panChange
            offset.snapTo(o)
        }
    }

    /**
     * 矫正位移
     */
    internal fun offsetCorrection() {
        var (x, y) = offset.value
        val minOffsetW = borderSize.width - scaleSize.width * zoom
        val minOffsetH = borderSize.height - scaleSize.height * zoom

        if (x > 0) {
            x = 0f
        } else if (x < minOffsetW) {
            x = minOffsetW
        }

        if (y > 0) {
            y = 0f
        } else if (y < minOffsetH) {
            y = minOffsetH
        }

        val target = Offset(x, y)
        if (target != offset.value)
            coroutineScope.launch { offset.animateTo(target) }
    }

}
