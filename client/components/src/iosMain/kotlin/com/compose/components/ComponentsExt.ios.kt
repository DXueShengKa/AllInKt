package com.compose.components

import androidx.compose.ui.graphics.Paint

actual fun Paint.reset() {
    asFrameworkPaint().reset()
}