package com.compose.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CircularProgressDialog(
    onDismissRequest: () -> Unit = {},
    text: String? = null,
    properties: DialogProperties = DialogProperties(dismissOnClickOutside = false),
) {
    Dialog(onDismissRequest, properties) {
        Surface(
            shape = MaterialTheme.shapes.large
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    Modifier.padding(20.dp)
                )
                if (text != null) Text(text,Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
