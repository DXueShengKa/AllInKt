//@file:JsModule("dayjs")

package ant

import kotlin.js.Date

@JsModule("dayjs")
@JsNonModule
external object dayjs {
    fun apply(date: Date? = definedExternally): Dayjs
    fun apply(date: String? = definedExternally): Dayjs
    fun apply(date: Number? = definedExternally): Dayjs
    fun apply(date: Dayjs? = definedExternally): Dayjs
    fun apply(): Dayjs

    fun unix(timestamp: Number): Dayjs
    fun isDayjs(obj: Any?): Boolean
}

external interface Dayjs {
    fun format(pattern: String = definedExternally): String
    fun add(value: Number, unit: String): Dayjs
    fun subtract(value: Number, unit: String): Dayjs
    fun startOf(unit: String): Dayjs
    fun endOf(unit: String): Dayjs
    fun diff(date: Dayjs, unit: String = definedExternally): Number
    fun toDate(): Date
    fun toJSON(): String
    fun isBefore(date: Dayjs): Boolean
    fun isAfter(date: Dayjs): Boolean
    fun isSame(date: Dayjs): Boolean
    fun isValid(): Boolean
}

// 扩展函数，方便使用
fun Dayjs.formatDefault(): String = format("YYYY-MM-DD HH:mm:ss")