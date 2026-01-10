package cn.allin.utils

expect class InLogger {
    fun info(message: String?)

    fun debug(message: String?)

    fun warning(message: String?)

    fun error(message: String?)
}
