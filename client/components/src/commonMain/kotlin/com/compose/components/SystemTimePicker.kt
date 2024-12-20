package com.compose.components

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDateTime

@Composable
expect fun SystemTimePicker(
    onCancel: () -> Unit,
    onSelect: (LocalDateTime) -> Unit
)