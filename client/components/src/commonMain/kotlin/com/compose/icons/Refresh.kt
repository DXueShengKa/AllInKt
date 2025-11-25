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

public val Icons.Refresh: ImageVector
    get() {
        if (_refresh != null) {
            return _refresh!!
        }
        _refresh = Builder(name = "Refresh", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 960.0f, viewportHeight = 960.0f).apply {
            path(fill = SolidColor(Color(0xFF666666)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(483.08f, 760.0f)
                quadToRelative(-117.25f, 0.0f, -198.63f, -81.34f)
                quadToRelative(-81.37f, -81.34f, -81.37f, -198.54f)
                quadToRelative(0.0f, -117.2f, 81.37f, -198.66f)
                quadTo(365.83f, 200.0f, 483.08f, 200.0f)
                quadToRelative(71.3f, 0.0f, 133.54f, 33.88f)
                quadToRelative(62.23f, 33.89f, 100.3f, 94.58f)
                lineTo(716.92f, 220.0f)
                quadToRelative(0.0f, -8.5f, 5.76f, -14.25f)
                reflectiveQuadToRelative(14.27f, -5.75f)
                quadToRelative(8.51f, 0.0f, 14.24f, 5.75f)
                reflectiveQuadToRelative(5.73f, 14.25f)
                verticalLineToRelative(156.92f)
                quadToRelative(0.0f, 13.73f, -9.29f, 23.02f)
                quadToRelative(-9.28f, 9.29f, -23.01f, 9.29f)
                lineTo(567.69f, 409.23f)
                quadToRelative(-8.5f, 0.0f, -14.25f, -5.76f)
                quadToRelative(-5.75f, -5.75f, -5.75f, -14.27f)
                quadToRelative(0.0f, -8.51f, 5.75f, -14.24f)
                reflectiveQuadToRelative(14.25f, -5.73f)
                horizontalLineToRelative(128.0f)
                quadToRelative(-31.23f, -59.85f, -87.88f, -94.54f)
                quadTo(551.15f, 240.0f, 483.08f, 240.0f)
                quadToRelative(-100.0f, 0.0f, -170.0f, 70.0f)
                reflectiveQuadToRelative(-70.0f, 170.0f)
                quadToRelative(0.0f, 100.0f, 70.0f, 170.0f)
                reflectiveQuadToRelative(170.0f, 70.0f)
                quadToRelative(71.46f, 0.0f, 130.85f, -38.73f)
                quadToRelative(59.38f, -38.73f, 88.07f, -102.89f)
                quadToRelative(3.38f, -7.84f, 10.96f, -11.03f)
                quadToRelative(7.58f, -3.2f, 15.51f, -0.5f)
                quadToRelative(8.45f, 2.69f, 11.22f, 11.0f)
                quadToRelative(2.77f, 8.3f, -0.61f, 16.15f)
                quadToRelative(-33.31f, 75.38f, -102.39f, 120.69f)
                reflectiveQuadTo(483.08f, 760.0f)
                close()
            }
        }
        .build()
        return _refresh!!
    }

private var _refresh: ImageVector? = null
