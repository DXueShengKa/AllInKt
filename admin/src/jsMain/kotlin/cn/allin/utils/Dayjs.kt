package cn.allin.utils


import kotlinx.datetime.LocalDate
import kotlin.js.Date


/**
 * @param date string | number | Date | Dayjs | LocalDate | LocalDateTime
 */
@JsModule("dayjs")
external fun dayjs(date: dynamic = definedExternally): DayJs

@JsModule("dayjs")
external val dayjs: DayConfig

@JsModule("dayjs/locale/zh-cn")
@JsNonModule
external val DayLocalZhCn: String

external interface DayJs {
    fun millisecond(): Long
    fun millisecond(millisecond: Long)
    fun toDate(): Date
    fun format(template: String): String
    fun locale(locale: String)
}

external interface DayConfig {
    fun locale(locale: String)
}

fun DayJs.toLocalDate(): LocalDate = toDate().toLocalDate()