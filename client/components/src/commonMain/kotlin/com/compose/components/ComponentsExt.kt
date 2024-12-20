package com.compose.components

import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.util.fastRoundToInt


expect fun Paint.reset()


operator fun Constraints.times(float: Float) = Constraints(
    minWidth = if (minWidth == 0) 0 else (minWidth * float).fastRoundToInt(),
    maxWidth = if (maxWidth == Constraints.Infinity) Constraints.Infinity else (maxWidth * float).fastRoundToInt(),
    minHeight = if (minHeight == 0) 0 else (minHeight * float).fastRoundToInt(),
    maxHeight = if (maxHeight == Constraints.Infinity) Constraints.Infinity else (maxHeight * float).fastRoundToInt()
)

object EmptyPlaceable : Placeable() {
    override fun get(alignmentLine: AlignmentLine): Int {
        return 0
    }

    override fun placeAt(position: IntOffset, zIndex: Float, layerBlock: (GraphicsLayerScope.() -> Unit)?) {

    }
}