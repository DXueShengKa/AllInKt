package cn.allin.utils

import kotlinx.datetime.*

actual enum class TimeTextStyle {
    SHORT_STANDALONE, NARROW, SHORT
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
