package cn.allin.utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.toNSDateComponents
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.systemLocale

fun NSDateComponents.toLocalDate(): LocalDate {
    return LocalDate(year.toInt(), month.toInt(), day.toInt())
}

actual enum class TimeTextStyle {
    SHORT_STANDALONE,
    NARROW,
    SHORT
}


actual fun Month.getDisplayName(textStyle: TimeTextStyle): String {
    val format = NSDateFormatter().apply {
        dateFormat = when (textStyle) {
            TimeTextStyle.NARROW -> "MMMM"
            else -> "MMM"
        }
        locale = NSLocale.systemLocale
    }

    val date = NSDateComponents().also {
        it.setMonth(number.toLong())
    }.date

    return format.stringFromDate(date!!)
}

actual fun DayOfWeek.getDisplayName(textStyle: TimeTextStyle): String {
    val format = NSDateFormatter().apply {
        dateFormat = when (textStyle) {
            TimeTextStyle.SHORT -> "EEE"
            else -> "EEEE"
        }
        locale = NSLocale.systemLocale
    }

    val date = NSDateComponents().also {
        it.setWeekday(isoDayNumber.toLong())
    }.date

    return format.stringFromDate(date!!)
}

@OptIn(ExperimentalForeignApi::class)
actual fun LocalDate.lastDayOfMonth(): LocalDate {
    val calendar = NSCalendar.currentCalendar()
    val components = toNSDateComponents()
    calendar.rangeOfUnit(NSCalendarUnitDay, NSCalendarUnitMonth, components.date!!)
        .useContents {
            components.setDay(length.toLong())
        }
    return components.toLocalDate()
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