package com.compose.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.window.DialogProperties

@Composable
expect fun BottomSheetDialog(
    onDismissRequest: () -> Unit,
    background: Color = MaterialTheme.colorScheme.background,
    shape: Shape = MaterialTheme.shapes.medium,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
)
