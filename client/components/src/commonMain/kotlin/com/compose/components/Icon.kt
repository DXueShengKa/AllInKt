package com.compose.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toolingGraphicsLayer
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics

/**
 * 可以使用brush的Icon
 * 如果不需要[brush]的话，那也不需要这个Icon函数
 */
@Composable
@NonRestartableComposable
fun Icon(
    painter: Painter,
    brush: Brush,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alpha: Float = DefaultAlpha,
    blendMode: BlendMode = BlendMode.DstIn
) {
    val semantics = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
            this.role = Role.Image
        }
    } else {
        Modifier
    }

    Canvas(
        modifier
            .toolingGraphicsLayer()
            .then(semantics)
    ) {
        if (size.isEmpty())
            error("Icon大小未设置")

        val canvas = drawContext.canvas

        val paint = Paint()
        paint.style = PaintingStyle.Fill
        brush.applyTo(size, paint, alpha)
        canvas.drawRect(size.toRect(),paint)
        paint.reset()
        paint.blendMode = blendMode
        drawContext.canvas.withSaveLayer(size.toRect(), paint) {
            painter.apply {
                draw(size)
            }
        }
    }
}

@Composable
@NonRestartableComposable
fun Icon(
    bitmap: ImageBitmap,
    brush: Brush,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    alpha: Float = DefaultAlpha,
) {
    val painter = remember(bitmap) { BitmapPainter(bitmap) }

    Icon(
        painter, brush, contentDescription, modifier, alpha
    )
}

/*TODO
@Composable
@NonRestartableComposable
fun Icon(
    @DrawableRes id: Int,
    brush: Brush,
    modifier: Modifier = Modifier,
    alpha: Float = DefaultAlpha,
) {

    Icon(
        painterResource(id), brush, null, modifier, alpha
    )
}

@Composable
@NonRestartableComposable
fun Icon(
    @DrawableRes drawableId: Int,
    brush: Brush,
    @StringRes contentDescriptionStringId: Int,
    modifier: Modifier = Modifier,
    alpha: Float = DefaultAlpha,
) {

    Icon(
        painterResource(drawableId),
        brush,
        stringResource(contentDescriptionStringId),
        modifier,
        alpha
    )
}
*/
