package com.compose.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max


@Composable
fun HorizonEquidistant(
    fixed: Int = 2,
    modifier: Modifier = Modifier,
    weights: FloatArray = FloatArray(fixed) { 1f },
    oneHeight: Dp = 0.dp,
    color: Color = Color.Unspecified,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
  /*  if (BuildConfig.DEBUG) require(
        fixed > 1
                && weights.size == fixed
                && weights.find { it < 0.1f } == null
    ) {
        "fixed固定总列数必须大于1；weights数量要与fixed对于；单个weight不能小于0.1，太小没有意义"
    }*/

    HorizonEquidistant(
        fixed, modifier, weights, oneHeight, verticalArrangement,
        if (color == Color.Unspecified) null else { start, end ->
            drawLine(color, start, end)
        },
        content
    )
}


@Composable
fun HorizonEquidistant(
    fixed: Int = 2,
    modifier: Modifier = Modifier,
    weights: FloatArray = FloatArray(fixed) { 1f },
    oneHeight: Dp = 0.dp,
    brush: Brush,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    /*if (BuildConfig.DEBUG) require(
        fixed > 1
                && weights.size == fixed
                && weights.find { it < 0.1f } == null
    ) {
        "fixed固定总列数必须大于1；weights数量要与fixed对于；单个weight不能小于0.1，太小没有意义"
    }*/

    HorizonEquidistant(
        fixed, modifier, weights, oneHeight, verticalArrangement,
        onDrawLine = { start, end ->
            drawLine(brush, start, end)
        },
        content
    )
}

/**
 * 把组件按照这个样式摆设，每个组件相对垂直分割线水平居中
 *
 *   - | - | -
 * -------------
 *   - | - | -
 * -------------
 *   - | - | -
 *
 * [fixed] 固定总列数
 * [weights] 每列的宽度占比权重
 * [onDrawLine] 绘制分割线
 * [oneHeight] 单行的高度，如果子组件高度大于该值就按照最大值算
 */
@Composable
fun HorizonEquidistant(
    fixed: Int,
    modifier: Modifier,
    weights: FloatArray,
    oneHeight: Dp,
    verticalArrangement: Arrangement.Vertical,
    onDrawLine: (DrawScope.(Offset, Offset) -> Unit)?,
    content: @Composable () -> Unit
) {

    val oneHeightPx = with(LocalDensity.current) { oneHeight.roundToPx() }

    //每行的高度
    var heights = IntArray(0)
    //总的权重和
    val weightSum = weights.sum()

    Layout(
        content,
        if (onDrawLine != null) modifier.drawBehind {
            //根据权重绘制垂直分割线
            for (i in weights.indices) {
                val x = size.width * weights.sum(i) / weightSum
                onDrawLine(Offset(x, 0f), Offset(x, size.height))
            }

            if (heights.size > 1) {
                var y = 0f
                //绘制水平分割线
                for (i in 0..heights.size - 2) {
                    y += heights[i]
                    onDrawLine(Offset(0f, y), Offset(size.width, y))
                }
            }
        } else modifier
    ) { measurables, layoutConstraints ->

        val width = layoutConstraints.run { if (hasFixedWidth) minWidth else maxWidth }

//        if (BuildConfig.DEBUG && layoutConstraints.hasFixedHeight) error("高不能设置固定值，应该通过测量决定")

        val 行数 = if (measurables.size % fixed == 0) measurables.size / fixed else measurables.size / fixed + 1
        heights = IntArray(行数)

        //minWidth会影响测量，设为0
        val constraints = layoutConstraints.copy(
            minWidth = 0, maxWidth = width / fixed,
            maxHeight = if (oneHeightPx > 0) oneHeightPx else layoutConstraints.maxHeight
        )

        val placeables = Array(measurables.size) {
            val placeable = measurables[it].measure(constraints)
            val i = it / fixed
            //记录每行高度最高的那个
            heights[i] = max( oneHeightPx, max(heights[i], placeable.height) )
            placeable
        }

        layout(width, heights.sum()) {
            //根据权重计算结尾的分割位置
            val xEnds = IntArray(fixed) {
                (width * weights.sum(it) / weightSum).toInt()
            }

            placeables.forEachIndexed { i, placeable ->
                //行从0开始
                val 行 = i / fixed
                val 列 = (i + 1).列(行 + 1, fixed)

                val xEnd = xEnds[列]
                val xStart = if (列 == 0) 0 else xEnds[列 - 1]
                //计算控件x轴起始位置
                val x = xStart + (xEnd - xStart - placeable.width) / 2

                //计算控件x轴起始位置
                val y = when (verticalArrangement) {
                    Arrangement.Center -> { //垂直居中
                        val yEnd = heights.sum(行)
                        val yStart = if (行 == 0) 0 else heights.sum(行 - 1)
                        yStart + (yEnd - yStart - placeable.height) / 2
                    }
                    Arrangement.Bottom -> { //置底
                        heights.sum(行) - placeable.height
                    }
                    else -> { //置顶
                        if (行 == 0) 0 else heights.sum(行 - 1)
                    }
                }

                placeable.placeRelative(x, y)
            }
        }
    }
}

/**
 * | 1 | 2 | 3 |
 * -------------
 * | 4 | 5 | 6 |
 * -------------
 * | 7 | 8 | 9 |
 *
 * 根据表格中具体数值和所在行得到它是第几列，返回值从0开始
 * 例如: 6.列(2,3) = 2，得6位于第三列
 */
private fun Int.列(行: Int, 列数: Int) = this - (行 + (行 - 1) * (列数 - 1))

private fun FloatArray.sum(index: Int): Float {
    var s = 0f
    for (i in 0..index)
        s += this[i]
    return s
}

private fun IntArray.sum(index: Int): Int {
    var s = 0
    for (i in 0..index)
        s += this[i]
    return s
}