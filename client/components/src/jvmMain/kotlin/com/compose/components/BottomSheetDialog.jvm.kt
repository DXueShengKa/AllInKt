package com.compose.components

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
actual fun BottomSheetDialog(
    onDismissRequest: () -> Unit,
    background: Color,
    shape: Shape,
    properties: DialogProperties,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest,properties){
        Surface(
            color = background,
            shape = shape,
            content = content
        )
    }
}