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

public val Icons.KeyboardArrowDown: ImageVector
    get() {
        if (_keyboardArrowDown != null) {
            return _keyboardArrowDown!!
        }
        _keyboardArrowDown = Builder(name = "KeyboardArrowDown", defaultWidth = 24.0.dp,
                defaultHeight = 24.0.dp, viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFF666666)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(480.0f, 575.15f)
                quadToRelative(-6.46f, 0.0f, -11.92f, -2.11f)
                quadToRelative(-5.46f, -2.12f, -10.7f, -7.35f)
                lineTo(281.85f, 390.15f)
                quadToRelative(-5.62f, -5.61f, -6.0f, -13.77f)
                quadToRelative(-0.39f, -8.15f, 6.0f, -14.53f)
                quadToRelative(6.38f, -6.39f, 14.15f, -6.39f)
                quadToRelative(7.77f, 0.0f, 14.15f, 6.39f)
                lineTo(480.0f, 531.69f)
                lineToRelative(169.85f, -169.84f)
                quadToRelative(5.61f, -5.62f, 13.77f, -6.0f)
                quadToRelative(8.15f, -0.39f, 14.53f, 6.0f)
                quadToRelative(6.39f, 6.38f, 6.39f, 14.15f)
                quadToRelative(0.0f, 7.77f, -6.39f, 14.15f)
                lineTo(502.62f, 565.69f)
                quadToRelative(-5.24f, 5.23f, -10.7f, 7.35f)
                quadToRelative(-5.46f, 2.11f, -11.92f, 2.11f)
                close()
            }
        }
        .build()
        return _keyboardArrowDown!!
    }

private var _keyboardArrowDown: ImageVector? = null
