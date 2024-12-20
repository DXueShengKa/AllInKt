package com.compose.components


import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.toAwtImage
import java.awt.image.BufferedImage


import kotlin.math.absoluteValue


/**
 * 获取裁剪之后的图片
 */
fun CropImageState.getCrop(): BufferedImage {

    //在ui的操作方式是放大图片，在原图则是缩小裁剪范围
    val shrink = 1 / zoom

    val x: Int = (offset.value.x / scale * shrink).absoluteValue.toInt()
    val y: Int = (offset.value.y / scale * shrink).absoluteValue.toInt()

    val w: Float
    val h: Float
    if (isBaseW) {
        w = imageBitmap.width * shrink
        h = w * aspect
    } else {
        h = imageBitmap.height * shrink
        w = h * aspect
    }

    val image = imageBitmap.toAwtImage()

    return try {
        image.getSubimage(x, y, w.toInt(), h.toInt())
    } catch (e: Throwable) {
        println(e)
        image
    }

}

actual fun Paint.reset() {
    asFrameworkPaint().reset()
}
