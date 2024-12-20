package cn.allin.utils

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.*

actual typealias TimeTextStyle = TextStyle

actual fun Month.length(isLeap: Boolean): Int {
    return length(isLeap)
}

actual fun Month.getDisplayName(textStyle: TimeTextStyle): String {
    return getDisplayName(textStyle, Locale.getDefault())
}


actual fun DayOfWeek.getDisplayName(textStyle: TimeTextStyle): String {
    return getDisplayName(textStyle, Locale.getDefault())
}


actual fun LocalDate.lastDayOfMonth(): LocalDate {
    return toJavaLocalDate().with(TemporalAdjusters.lastDayOfMonth()).toKotlinLocalDate()
}


actual fun LocalDate.withDayOfMonth(dayOfMonth: Int): LocalDate {
    return toJavaLocalDate().withDayOfMonth(dayOfMonth).toKotlinLocalDate()
}

actual fun LocalDate.withYear(year: Int): LocalDate {
    return toJavaLocalDate().withYear(year).toKotlinLocalDate()
}

actual fun LocalDate.withMonth(month: Int): LocalDate {
    return toJavaLocalDate().withMonth(month).toKotlinLocalDate()
}