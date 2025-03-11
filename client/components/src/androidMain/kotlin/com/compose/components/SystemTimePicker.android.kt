package com.compose.components

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.node.Ref
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import cn.allin.utils.getDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.atTime

@Composable
actual fun SystemTimePicker(onCancel: () -> Unit, onSelect: (LocalDateTime) -> Unit) {

    var date: LocalDate? by remember { mutableStateOf(null) }

    val time = remember { Ref<LocalTime>() }

    AlertDialog(
        onDismissRequest = onCancel,
        title = {
            Text(if (date == null) "选择时间" else "选择日期")
        },
        text = {
            if (date == null) AndroidView(
                factory = {
                    DatePicker(it).apply {
                        setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                            date = LocalDate(year, monthOfYear+1, dayOfMonth)

                            if (time.value == null)
                                time.value = Clock.System.now().getDateTime().time
                        }
                    }
                }
            ) else AndroidView(
                factory = {
                    TimePicker(it).apply {
                        setOnTimeChangedListener { _, hourOfDay, minute ->
                            time.value = LocalTime(hourOfDay, minute, 0)
                        }
                    }
                }
            )
        },
        dismissButton = {
            if (date != null) TextButton({
                date = null
            }) {
                Text("重新选择时间")
            }
            else
                TextButton(onCancel) {
                    Text(stringResource(android.R.string.cancel))
                }
        },
        confirmButton = {
            TextButton({
                val d = date?.let {
                    val t = time.value
                    if (t != null)
                        it.atTime(t)
                    else
                        it.atTime(0, 0, 0)
                }
                if (d != null) {
                    onSelect(d)
                }
                onCancel()
            }) {
                Text(stringResource(android.R.string.ok))
            }
        },
        properties = DialogProperties()
    )
}
