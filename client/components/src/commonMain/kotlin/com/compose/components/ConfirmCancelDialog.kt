package com.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties


@Composable
fun ConfirmCancelDialog(
    content: String,
    cancelText: String = "取消",
    confirmText: String = "确认",
    onDismissRequest: (() -> Unit)? = null,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    ConfirmCancelDialog(
        content = {
            Text(
                content,
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 30.dp),
                textAlign = TextAlign.Center
            )
        },
        cancelText,
        confirmText,
        onDismissRequest = onDismissRequest ?: onCancel,
        onCancel = onCancel,
        onConfirm = {
            if (onDismissRequest == null)
                onCancel()
            onConfirm()
        }
    )
}

@Composable
fun ConfirmCancelDialog(
    title: String,
    content: String,
    cancelText: String = "取消",
    confirmText: String = "确认",
    cancelBackground: Color = MaterialTheme.colorScheme.surface,
    confirmBackground: Color = MaterialTheme.colorScheme.primary,
    properties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit = {},
    onCancel: (() -> Unit)?,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest, properties) {
        ConfirmCancelContent(
            content = {
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append(title)
                        }
                        append("\n\n")
                        append(content)
                    },
                    Modifier
                        .fillMaxWidth()
                        .padding(20.dp, 30.dp),
                    textAlign = TextAlign.Center
                )
            },
            cancelText,
            confirmText,
            cancelBackground,
            confirmBackground,
            onCancel,
            onConfirm
        )
    }
}


@Composable
fun ConfirmCancelDialog(
    content: @Composable ColumnScope.() -> Unit,
    cancelText: String = "取消",
    confirmText: String = "确认",
    cancelBackground: Color = MaterialTheme.colorScheme.surface,
    confirmBackground: Color = MaterialTheme.colorScheme.primary,
    properties: DialogProperties = DialogProperties(),
    onDismissRequest: () -> Unit = {},
    onCancel: (() -> Unit)?,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest, properties) {
        ConfirmCancelContent(
            content,
            cancelText,
            confirmText,
            cancelBackground,
            confirmBackground,
            onCancel,
            onConfirm
        )
    }
}

@Composable
fun ConfirmCancelContent(
    content: @Composable ColumnScope.() -> Unit,
    cancelText: String,
    confirmText: String,
    cancelBackground: Color,
    confirmBackground: Color,
    onCancel: (() -> Unit)?,
    onConfirm: () -> Unit
) {
    val shape = MaterialTheme.shapes.medium
    Column(
        Modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.background)
    ) {
        content()
        Row(
            Modifier.padding(10.dp)
        ) {
            if (onCancel != null) {
                Box(
                    Modifier
                        .weight(1f)
                        .height(40.dp)
                        .background(cancelBackground, shape)
                        .clickable(onClick = onCancel),
                    Alignment.Center
                ) {
                    Text(
                        cancelText,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(Modifier.width(10.dp))
            }
            Box(
                Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(confirmBackground, shape)
                    .clickable(onClick = onConfirm),
                Alignment.Center
            ) {
                Text(
                    confirmText,
                    color = MaterialTheme.colorScheme.onError
                )
            }
        }
    }
}
