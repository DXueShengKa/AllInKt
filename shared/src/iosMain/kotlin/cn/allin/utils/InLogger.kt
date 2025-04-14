package cn.allin.utils

import cn.allin.BuildConfig
import platform.Foundation.NSLog

actual class InLogger(
    private val tag: String,
) {

    private fun log(level: String, message: String?) {
        if (BuildConfig.DEBUG && message != null) {
            println("[$level] $tag: $message")
        }
    }

    actual fun info(message: String?) {
        log("info", message)
    }

    actual fun debug(message: String?) {
        log("debug", message)
    }

    actual fun warning(message: String?) {
        log("warning", message)
    }

    actual fun error(message: String?) {
        NSLog("error", message)
    }

}
