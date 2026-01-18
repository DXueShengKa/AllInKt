package cn.allin.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import cn.allin.ksp.navigation.NavRoute
import com.compose.components.DatePicker
import com.compose.components.DatePickerColors
import com.compose.components.DatePickerState

const val RouteCalendar = "Calendar"

@Composable
fun RouteCalendarScreen() {
    DatePicker(
        remember { DatePickerState() },
        colors =
            DatePickerColors(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.onPrimary,
                MaterialTheme.colorScheme.primaryContainer,
                Color.Gray,
            ),
    )
}
