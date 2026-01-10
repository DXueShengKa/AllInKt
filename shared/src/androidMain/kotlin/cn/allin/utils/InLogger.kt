package cn.allin.utils

import android.util.Log
import cn.allin.BuildConfig

actual class InLogger(
    private val tag: String,
) {
    actual fun info(message: String?) {
        if (BuildConfig.DEBUG && message != null) {
            Log.i(tag, message)
        }
    }

    actual fun debug(message: String?) {
        if (BuildConfig.DEBUG && message != null) {
            Log.d(tag, message)
        }
    }

    actual fun warning(message: String?) {
        if (BuildConfig.DEBUG && message != null) {
            Log.w(tag, message)
        }
    }

    actual fun error(message: String?) {
        if (BuildConfig.DEBUG && message != null) {
            Log.e(tag, message)
        }
    }
}
