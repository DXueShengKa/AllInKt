package cn.allin.utils

import cn.allin.BuildConfig

actual class InLogger() {

    actual fun info(message: String?) {
        if (BuildConfig.DEBUG && message != null) {
//            console.info(tag, message)
        }
    }

    actual fun debug(message: String?) {
        if (BuildConfig.DEBUG && message != null) {
//            console.log(tag, message)
        }
    }

    actual fun warning(message: String?) {
        if (BuildConfig.DEBUG && message != null) {
//            console.warn(tag, message)
        }
    }
    actual fun error(message: String?) {
        if (BuildConfig.DEBUG && message != null) {
//            console.error(tag, message)
        }
    }

}
