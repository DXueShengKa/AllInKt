package cn.allin.utils

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

const val DATE_TIME_DEFAULT_FORMAT = "YYYY-MM-DD HH:mm:ss"

fun Instant.getDateTime() = toLocalDateTime(TimeZone.currentSystemDefault())

fun Instant.getDate() = getDateTime().date

fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC)
}

fun isLeapYear(year: Int) = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0


expect fun Month.length(isLeap: Boolean): Int

expect enum class TimeTextStyle {
    SHORT_STANDALONE,
    NARROW,
    SHORT
}

expect fun Month.getDisplayName(textStyle: TimeTextStyle): String


expect fun DayOfWeek.getDisplayName(textStyle: TimeTextStyle): String


expect fun LocalDate.lastDayOfMonth(): LocalDate


expect fun LocalDate.withDayOfMonth(dayOfMonth: Int): LocalDate


expect fun LocalDate.withYear(year: Int): LocalDate


expect fun LocalDate.withMonth(month: Int): LocalDate