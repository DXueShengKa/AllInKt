package com.compose.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.compose.Icons

public val Icons.KeyboardArrowLeft: ImageVector
    get() {
        if (_keyboardArrowLeft != null) {
            return _keyboardArrowLeft!!
        }
        _keyboardArrowLeft = Builder(name = "KeyboardArrowLeft", defaultWidth = 24.0.dp,
                defaultHeight = 24.0.dp, viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFF666666)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveToRelative(404.31f, 480.0f)
                lineToRelative(169.84f, 169.85f)
                quadToRelative(5.62f, 5.61f, 6.0f, 13.77f)
                quadToRelative(0.39f, 8.15f, -6.0f, 14.53f)
                quadToRelative(-6.38f, 6.39f, -14.15f, 6.39f)
                quadToRelative(-7.77f, 0.0f, -14.15f, -6.39f)
                lineTo(370.31f, 502.62f)
                quadToRelative(-5.23f, -5.24f, -7.35f, -10.7f)
                quadToRelative(-2.11f, -5.46f, -2.11f, -11.92f)
                reflectiveQuadToRelative(2.11f, -11.92f)
                quadToRelative(2.12f, -5.46f, 7.35f, -10.7f)
                lineToRelative(175.54f, -175.53f)
                quadToRelative(5.61f, -5.62f, 13.77f, -6.0f)
                quadToRelative(8.15f, -0.39f, 14.53f, 6.0f)
                quadToRelative(6.39f, 6.38f, 6.39f, 14.15f)
                quadToRelative(0.0f, 7.77f, -6.39f, 14.15f)
                lineTo(404.31f, 480.0f)
                close()
            }
        }
        .build()
        return _keyboardArrowLeft!!
    }

private var _keyboardArrowLeft: ImageVector? = null
