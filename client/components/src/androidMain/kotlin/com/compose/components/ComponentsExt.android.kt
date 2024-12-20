package com.compose.components

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.compose.ui.graphics.Paint

import androidx.compose.ui.graphics.asAndroidBitmap
import kotlin.math.absoluteValue


/**
 * 获取裁剪之后的图片
 */
fun CropImageState.getCrop(): Bitmap {

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

    val bitmap = imageBitmap.asAndroidBitmap()

    return try {

        val matrix: Matrix? = if (isRotation) {
            val imageMatrix = Matrix()
            imageMatrix.setRotate(rotation)
            imageMatrix
        } else null

        Bitmap.createBitmap(bitmap, x, y, w.toInt(), h.toInt(), matrix, true)
    } catch (e: IllegalArgumentException) {
        bitmap
    }

}

actual fun Paint.reset() {
    asFrameworkPaint().reset()
}