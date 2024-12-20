package com.compose.components

import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.IntrinsicMeasurable
import androidx.compose.ui.layout.IntrinsicMeasureScope
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.constrain
import androidx.compose.ui.unit.constrainHeight
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp

/**
 * 环绕的光环效果
 */
@Stable
fun Modifier.aureole(
    aureoleColor: Color,
    aureoleSize: Dp = 5.dp,
    shape: Shape = CircleShape,
) = this.shadow(aureoleSize, shape, true, aureoleColor, aureoleColor)
    .padding(aureoleSize)


@Stable
fun Modifier.aureole(
    background: Color,
    aureoleColor: Color,
    aureoleSize: Dp = 5.dp,
    shape: Shape = CircleShape,
) = this.shadow(aureoleSize, shape, true, aureoleColor, aureoleColor)
    .padding(aureoleSize)
    .background(background, shape)
    .clip(shape)


fun Modifier.clickableNoIndication(
    onClick: () -> Unit
) = clickable(null, null, true, null, null, onClick)



val DpSize.Companion.VisibilityThreshold
    get() = DpSizeToVector


private val DpSizeToVector: TwoWayConverter<DpSize, AnimationVector2D> =
    TwoWayConverter(
        convertToVector = { AnimationVector2D(it.width.value, it.height.value) },
        convertFromVector = { DpSize(Dp(it.v1), Dp(it.v2)) }
    )


inline fun Modifier.size(size: IntSize) = size(size.width, size.height)

inline fun Modifier.size(size: Int) = size(size, size)

inline fun Modifier.height(height: Int) = size(0, height)

inline fun Modifier.width(width: Int) = size(width, 0)


@Stable
fun Modifier.size(width: Int, height: Int) = then(SizeModifier(
    minWidth = width,
    maxWidth = width,
    minHeight = height,
    maxHeight = height,
    enforceIncoming = true,
    inspectorInfo = debugInspectorInfo {
        name = "size"
        properties["width"] = width
        properties["height"] = height
    }
))

@Stable
fun Modifier.sizeIn(
    minWidth: Int = 0,
    minHeight: Int = 0,
    maxWidth: Int = 0,
    maxHeight: Int = 0
) = this.then(
    SizeModifier(
        minWidth = minWidth,
        minHeight = minHeight,
        maxWidth = maxWidth,
        maxHeight = maxHeight,
        enforceIncoming = true,
        inspectorInfo = debugInspectorInfo {
            name = "sizeIn"
            properties["minWidth"] = minWidth
            properties["minHeight"] = minHeight
            properties["maxWidth"] = maxWidth
            properties["maxHeight"] = maxHeight
        }
    )
)


@Stable
fun Modifier.requiredSize(width: Int, height: Int) = this.then(
    SizeModifier(
        minWidth = width,
        maxWidth = width,
        minHeight = height,
        maxHeight = height,
        enforceIncoming = false,
        inspectorInfo = debugInspectorInfo {
            name = "requiredSize"
            properties["width"] = width
            properties["height"] = height
        }
    )
)


private class SizeModifier(
    private val minWidth: Int,
    private val minHeight: Int,
    private val maxWidth: Int,
    private val maxHeight: Int,
    private val enforceIncoming: Boolean,
    inspectorInfo: InspectorInfo.() -> Unit
) : LayoutModifier, InspectorValueInfo(inspectorInfo) {
    private val targetConstraints: Constraints
        get() {
            val maxWidth = if (maxWidth != 0) {
                maxWidth.coerceAtLeast(0)
            } else {
                Constraints.Infinity
            }
            val maxHeight = if (maxHeight != 0) {
                maxHeight.coerceAtLeast(0)
            } else {
                Constraints.Infinity
            }
            val minWidth = if (minWidth != 0) {
                minWidth.coerceAtMost(maxWidth).coerceAtLeast(0).let {
                    if (it != Constraints.Infinity) it else 0
                }
            } else {
                0
            }
            val minHeight = if (minHeight != 0) {
                minHeight.coerceAtMost(maxHeight).coerceAtLeast(0).let {
                    if (it != Constraints.Infinity) it else 0
                }
            } else {
                0
            }
            return Constraints(
                minWidth = minWidth,
                minHeight = minHeight,
                maxWidth = maxWidth,
                maxHeight = maxHeight
            )
        }

    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureResult {
        val wrappedConstraints = targetConstraints.let { targetConstraints ->
            if (enforceIncoming) {
                constraints.constrain(targetConstraints)
            } else {
                val resolvedMinWidth = if (minWidth != 0) {
                    targetConstraints.minWidth
                } else {
                    constraints.minWidth.coerceAtMost(targetConstraints.maxWidth)
                }
                val resolvedMaxWidth = if (maxWidth != 0) {
                    targetConstraints.maxWidth
                } else {
                    constraints.maxWidth.coerceAtLeast(targetConstraints.minWidth)
                }
                val resolvedMinHeight = if (minHeight != 0) {
                    targetConstraints.minHeight
                } else {
                    constraints.minHeight.coerceAtMost(targetConstraints.maxHeight)
                }
                val resolvedMaxHeight = if (maxHeight != 0) {
                    targetConstraints.maxHeight
                } else {
                    constraints.maxHeight.coerceAtLeast(targetConstraints.minHeight)
                }
                Constraints(
                    resolvedMinWidth,
                    resolvedMaxWidth,
                    resolvedMinHeight,
                    resolvedMaxHeight
                )
            }
        }
        val placeable = measurable.measure(wrappedConstraints)
        return layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ): Int {
        val constraints = targetConstraints
        return if (constraints.hasFixedWidth) {
            constraints.maxWidth
        } else {
            constraints.constrainWidth(measurable.minIntrinsicWidth(height))
        }
    }

    override fun IntrinsicMeasureScope.minIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ): Int {
        val constraints = targetConstraints
        return if (constraints.hasFixedHeight) {
            constraints.maxHeight
        } else {
            constraints.constrainHeight(measurable.minIntrinsicHeight(width))
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicWidth(
        measurable: IntrinsicMeasurable,
        height: Int
    ): Int {
        val constraints = targetConstraints
        return if (constraints.hasFixedWidth) {
            constraints.maxWidth
        } else {
            constraints.constrainWidth(measurable.maxIntrinsicWidth(height))
        }
    }

    override fun IntrinsicMeasureScope.maxIntrinsicHeight(
        measurable: IntrinsicMeasurable,
        width: Int
    ): Int {
        val constraints = targetConstraints
        return if (constraints.hasFixedHeight) {
            constraints.maxHeight
        } else {
            constraints.constrainHeight(measurable.maxIntrinsicHeight(width))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SizeModifier) return false
        return minWidth == other.minWidth &&
                minHeight == other.minHeight &&
                maxWidth == other.maxWidth &&
                maxHeight == other.maxHeight &&
                enforceIncoming == other.enforceIncoming
    }

    override fun hashCode() = (
            (
                    (((minWidth.hashCode() * 31 + minHeight.hashCode()) * 31) + maxWidth.hashCode()) *
                            31
                    ) + maxHeight.hashCode()
            ) * 31
}