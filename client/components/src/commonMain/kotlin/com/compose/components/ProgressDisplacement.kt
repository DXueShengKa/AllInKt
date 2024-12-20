package com.compose.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ApproachLayoutModifierNode
import androidx.compose.ui.layout.ApproachMeasureScope
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize

private class ProgressApproachModifierNode(
    var lookaheadScope: LookaheadScope,
    var progress: Float,
    var isRunning: Boolean
) : ApproachLayoutModifierNode, Modifier.Node() {

    override fun isMeasurementApproachInProgress(lookaheadSize: IntSize): Boolean {
        return false
    }

    override fun Placeable.PlacementScope.isPlacementApproachInProgress(
        lookaheadCoordinates: LayoutCoordinates
    ): Boolean {
        return isRunning
    }

    private var previous = Offset.Zero

    private var begin = Offset.Zero
    private var end = Offset.Zero


    private fun offsetToZero(): Offset {
        if (end == Offset.Zero) return Offset.Zero
        return if (previous == end)
            (begin - end) * (1f - progress)
        else
            (end - begin) * progress
    }


    override fun ApproachMeasureScope.approachMeasure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val placeable = measurable.measure(constraints)

        return layout(placeable.width, placeable.height) {
            val coordinates = coordinates
            if (coordinates != null) {
                val target = with(lookaheadScope) {
                    lookaheadScopeCoordinates.localLookaheadPositionOf(coordinates)
                }

                if (begin == Offset.Zero)
                    begin = target
                else if (end == Offset.Zero)
                    end = target

                if (previous != target && end != Offset.Zero) {
                    previous = target
                }

                val (x, y) = offsetToZero()
                placeable.place(x.toInt(), y.toInt())
            } else {
                placeable.place(0, 0)
            }
        }
    }
}


private data class ProgressApproachNodeElement(
    val lookaheadScope: LookaheadScope,
    val progress: Float,
    val isRunning: Boolean
) : ModifierNodeElement<ProgressApproachModifierNode>() {

    override fun update(node: ProgressApproachModifierNode) {
        node.lookaheadScope = lookaheadScope
        node.isRunning = isRunning
        node.progress = progress
    }

    override fun create(): ProgressApproachModifierNode {
        return ProgressApproachModifierNode(lookaheadScope, progress, isRunning)
    }
}

/**
 * [progress]进度
 * [isRunning]是否在运行中
 */
fun Modifier.progressDisplacement(
    lookaheadScope: LookaheadScope,
    progress: Float,
    isRunning: Boolean
) = then(ProgressApproachNodeElement(lookaheadScope, progress, isRunning))
