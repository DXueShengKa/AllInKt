package cn.allin.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cn.allin.ksp.navigation.NavRoute
import cn.allin.utils.ChineseDate
import com.compose.components.DatePicker
import com.compose.components.DatePickerColors
import com.compose.components.DatePickerState
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus

const val RouteCalendar = "Calendar"

@OptIn(ExperimentalMaterial3Api::class)
@NavRoute(routeString = RouteCalendar)
@Composable
fun RouteCalendarScreen() {


    DatePicker(
        remember { DatePickerState() },
        colors = DatePickerColors(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.onPrimary,
            MaterialTheme.colorScheme.primaryContainer,
            Color.Gray,
        )
    )

}
