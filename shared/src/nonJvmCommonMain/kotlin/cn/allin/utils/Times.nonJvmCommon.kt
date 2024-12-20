package cn.allin.utils

import kotlinx.datetime.Month

actual fun Month.length(isLeap: Boolean): Int {
    return when (this) {
        Month.FEBRUARY -> if (isLeap) 29 else 28
        Month.APRIL, Month.JUNE, Month.SEPTEMBER, Month.NOVEMBER -> 30
        else -> 31
    }
}
