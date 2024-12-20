package com.compose.components

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.HardwareRenderer
import android.graphics.Picture
import android.graphics.PixelFormat
import android.graphics.RenderEffect
import android.graphics.RenderNode
import android.graphics.Shader
import android.hardware.HardwareBuffer
import android.media.ImageReader
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.PixelCopy
import android.view.View
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.RecomposeScope
import androidx.compose.runtime.Stable
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.createBitmap
import androidx.core.graphics.record
import androidx.core.graphics.withSave
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun rememberBlurBackgroundState(): BlurBackgroundState {
    val state = rememberOnDispose<BlurBackgroundState> {
        value = BlurBackgroundState(2f)
        onDispose {
            value.destroy()
        }
    }
    state.channel.trySend(Unit)
    return state
}


@Composable
fun BlurBackground(
    modifier: Modifier = Modifier,
    blurState: BlurBackgroundState,
    radius: Dp = 30.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val view = LocalView.current

    LaunchedEffect(blurState) {
        val channel = blurState.channel
        val iterator = channel.iterator()
        while (iterator.hasNext()) {
            blurState.snapshotFrom(view,0)
            blurState.recomposeScope.invalidate()
        }
    }

    Box(modifier.onGloballyPositioned {
        blurState.updateBounds(it.boundsInWindow())
    }) {
        blurState.recomposeScope = currentRecomposeScope

        blurState.inner?.also { bitmap ->
            Spacer(
                Modifier
                    .fillMaxSize()
                    .blur(radius)
                    .drawBehind {
                        drawContext.canvas.nativeCanvas.drawBitmap(
                            bitmap, null, android.graphics.Rect(0, 0, size.width.toInt(), size.height.toInt()), null
                        )
                    }
            )
        }

        content()
    }
}

@Stable
class BlurBackgroundState(
    private val downscale: Float
) {
    private var shooting: Boolean = false

    var inner: Bitmap? = null

    private val canvas = android.graphics.Canvas()
    private var originalBounds = Rect.Zero

    private var originalRect = android.graphics.Rect()

    lateinit var recomposeScope: RecomposeScope

    val channel = Channel<Unit>(Channel.CONFLATED)

    /**
     * Update the [bounds] of this bitmap, which will recreate the [inner] bitmap
     * with the new size.
     */
    fun updateBounds(bounds: Rect) {
        // There may not actually be anything to update
        if (inner != null && bounds == originalBounds || bounds.isEmpty) return
        originalBounds = bounds.apply {
            originalRect = android.graphics.Rect(top.toInt(),left.toInt(),right.toInt(),bottom.toInt())
        }

        val bitmap = createBitmap(bounds.width.downscale(), bounds.height.downscale())

        inner = bitmap
        canvas.setBitmap(bitmap)

    }

    private val handler = Handler(Looper.getMainLooper())

    suspend fun snapshotFrom(view: View):Int {
        val window = (view.context as Activity).window
        val bitmap = inner ?: return PixelCopy.ERROR_UNKNOWN

        return suspendCancellableCoroutine { continuation ->
            PixelCopy.request(
                window,
                originalRect,
                bitmap,
                { continuation.resume(it) },
                handler
            )
        }
    }



    @RequiresApi(Build.VERSION_CODES.P)
    fun snapshotFrom2(view: View) {
        val bitmap = inner ?: return

        val p = Picture()
        p.record(bitmap.width,bitmap.height){
            val (x, y) = view.coordinates()
            scale(bitmap.width / originalBounds.width, bitmap.height / originalBounds.height)
            translate(-x - originalBounds.left, -y - originalBounds.top)
            clipRect(originalBounds.left, originalBounds.top, originalBounds.right, originalBounds.bottom)
            view.draw(this)
        }

        inner = Bitmap.createBitmap(p,bitmap.width,bitmap.height,Bitmap.Config.ARGB_8888)
        println(inner)


    }


    fun snapshotFrom(view: View, retry: Byte) {
        val bitmap = inner ?: return

        try {
            shooting = true
            // Draw the new content into the bitmap
            canvas.withSave {
                // First we have to make sure that the subsequent drawing steps are
                //   done in the correct coordinates
                val (x, y) = view.coordinates()
                scale(bitmap.width / originalBounds.width, bitmap.height / originalBounds.height)
                translate(-x - originalBounds.left, -y - originalBounds.top)
                clipRect(originalBounds.left, originalBounds.top, originalBounds.right, originalBounds.bottom)

                // Then tell the others we're in a shooting state

                view.draw(this)
            }

        } catch (e: IllegalArgumentException) {
            // For possible 'Software rendering doesn't support hardware bitmaps',
            //   we need some fixed retries
            Log.e("snapshotFrom", "提取失败", e)
            when (retry) {
                in 1..3 -> inner = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                4.toByte() -> view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                else -> throw e
            }
            snapshotFrom(view, (retry + 1).toByte())
        } finally {
            // Regardless, we're done shooting the view
//            paint.shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            shooting = false
        }
    }

    fun destroy() {
        inner?.recycle()
        inner = null
        channel.close()
    }

    private fun Float.downscale() = kotlin.math.ceil(this / max(1f, downscale)).roundToInt()
    private fun View.coordinates() = IntArray(2).also(::getLocationOnScreen)
}

@RequiresApi(Build.VERSION_CODES.S)
private class Default(effect: RenderEffect) {

    private val renderNode: RenderNode = RenderNode("BlurVisualEffect").apply { setRenderEffect(effect) }
    private val hardwareRenderer: HardwareRenderer = HardwareRenderer().apply { setContentRoot(renderNode) }
    private var imageReader: ImageReader? = null

    fun render(bitmap: Bitmap): Bitmap? {
        ensureImageReaderValid(bitmap)


        renderNode.apply {
            beginRecording()
                .drawBitmap(bitmap, 0f, 0f, null)
            endRecording()
        }

        hardwareRenderer.createRenderRequest().setWaitForPresent(true).syncAndDraw()

        val image = imageReader?.acquireNextImage() ?: return null
        val hardwareBuffer = image.hardwareBuffer ?: return null
        val blurred = Bitmap.wrapHardwareBuffer(hardwareBuffer, null)

        hardwareBuffer.close()
        image.close()

        return blurred
    }

    fun destroy() = runCatching {
        imageReader?.close()
        hardwareRenderer.destroy()
        renderNode.discardDisplayList()

        imageReader = null
    }.getOrDefault(Unit)

    /**
     * This function ensures that the image reader instance is valid so that we can reuse it
     * to avoid frequent creation of new one.
     */
    @SuppressLint("WrongConstant")
    private fun ensureImageReaderValid(bitmap: Bitmap) {
        if (imageReader?.width != bitmap.width || imageReader?.height != bitmap.height) {
            imageReader?.close()
            imageReader = ImageReader.newInstance(
                bitmap.width, bitmap.height,
                PixelFormat.RGBA_8888, 1,
                HardwareBuffer.USAGE_GPU_SAMPLED_IMAGE or HardwareBuffer.USAGE_GPU_COLOR_OUTPUT
            ).apply {
                hardwareRenderer.setSurface(surface)
                renderNode.setPosition(0, 0, width, height)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.S)
private class Fallback(
    private val radiusX: Float,
    private val radiusY: Float,
    private val edgeTreatment: Shader.TileMode,
) {
    private val renderNode: RenderNode = RenderNode("BlurFallbackVisualEffect")

    private var outputBitmap: Bitmap? = null
    private lateinit var outputCanvas: Canvas

    fun render(bitmap: Bitmap): Bitmap? {
        ensureInstancesValid(bitmap)
        if (!outputCanvas.isHardwareAccelerated) return null

        val srcEffect = RenderEffect.createBitmapEffect(bitmap)

        RenderEffect.createBlurEffect(radiusX, radiusY, srcEffect, edgeTreatment)
            .also(renderNode::setRenderEffect)

        outputCanvas.drawRenderNode(renderNode)

        return outputBitmap
    }

    fun destroy() = runCatching {
        outputBitmap?.recycle()
        renderNode.discardDisplayList()

        outputBitmap = null
    }.getOrDefault(Unit)

    /**
     * This function ensures that the fallback instances are valid so that we can reuse them
     * to avoid frequent creation of new instances.
     */
    private fun ensureInstancesValid(bitmap: Bitmap) {
        // First erase the contents of the output bitmap
        outputBitmap?.eraseColor(0)

        if (outputBitmap?.width != bitmap.width || outputBitmap?.height != bitmap.height) {
            outputBitmap?.recycle()
            outputBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888).also {
                outputCanvas = Canvas(it)
            }
        }
    }
}