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

public val Icons.KeyboardArrowUp: ImageVector
    get() {
        if (_keyboardArrowUp != null) {
            return _keyboardArrowUp!!
        }
        _keyboardArrowUp = Builder(name = "KeyboardArrowUp", defaultWidth = 24.0.dp, defaultHeight =
                24.0.dp, viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFF666666)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(480.0f, 404.31f)
                lineTo(310.15f, 574.15f)
                quadToRelative(-5.61f, 5.62f, -13.77f, 6.0f)
                quadToRelative(-8.15f, 0.39f, -14.53f, -6.0f)
                quadToRelative(-6.39f, -6.38f, -6.39f, -14.15f)
                quadToRelative(0.0f, -7.77f, 6.39f, -14.15f)
                lineToRelative(175.53f, -175.54f)
                quadToRelative(9.7f, -9.69f, 22.62f, -9.69f)
                quadToRelative(12.92f, 0.0f, 22.62f, 9.69f)
                lineToRelative(175.53f, 175.54f)
                quadToRelative(5.62f, 5.61f, 6.0f, 13.77f)
                quadToRelative(0.39f, 8.15f, -6.0f, 14.53f)
                quadToRelative(-6.38f, 6.39f, -14.15f, 6.39f)
                quadToRelative(-7.77f, 0.0f, -14.15f, -6.39f)
                lineTo(480.0f, 404.31f)
                close()
            }
        }
        .build()
        return _keyboardArrowUp!!
    }

private var _keyboardArrowUp: ImageVector? = null
