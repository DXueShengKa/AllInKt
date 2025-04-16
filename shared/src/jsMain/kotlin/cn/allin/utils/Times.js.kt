package cn.allin.utils

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.js.Date

actual enum class TimeTextStyle {
    SHORT_STANDALONE, NARROW, SHORT
}

//TimeZone在js中需要单独npm导入@js-joda/timezone
actual fun Instant.getDateTime(): LocalDateTime {
    return toLocalDateTime(TimeZone.currentSystemDefault())
}

actual fun Month.getDisplayName(textStyle: TimeTextStyle): String {
    TODO()
}

actual fun DayOfWeek.getDisplayName(textStyle: TimeTextStyle): String {

    TODO()

}

actual fun LocalDate.lastDayOfMonth(): LocalDate {

    TODO()

}

actual fun LocalDate.withDayOfMonth(dayOfMonth: Int): LocalDate {
    return LocalDate(year, month, dayOfMonth)
}


actual fun LocalDate.withYear(year: Int): LocalDate {
    return LocalDate(year, month, dayOfMonth)
}


actual fun LocalDate.withMonth(month: Int): LocalDate {
    return LocalDate(year, month, dayOfMonth)
}


fun Date.toLocalDate(): LocalDate {
    return toKotlinInstant().getDate()
}
