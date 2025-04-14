package cn.allin.utils

import cn.allin.BuildConfig
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ptr
import platform.darwin.OS_LOG_TYPE_DEBUG
import platform.darwin.OS_LOG_TYPE_ERROR
import platform.darwin.OS_LOG_TYPE_FAULT
import platform.darwin.OS_LOG_TYPE_INFO
import platform.darwin.__dso_handle
import platform.darwin._os_log_internal
import platform.darwin.os_log_create


@OptIn(ExperimentalForeignApi::class)
actual class InLogger(
    tag: String
) {
    private val log = os_log_create("cn.allin.AllInKt", tag)


    actual fun info(message: String?) {
        if (BuildConfig.DEBUG && message != null)
            _os_log_internal(__dso_handle.ptr, log, OS_LOG_TYPE_INFO, message)
    }

    actual fun debug(message: String?) {
        if (BuildConfig.DEBUG && message != null)
            _os_log_internal(__dso_handle.ptr, log, OS_LOG_TYPE_DEBUG, message)
    }

    actual fun warning(message: String?) {
        if (BuildConfig.DEBUG && message != null)
            _os_log_internal(__dso_handle.ptr, log, OS_LOG_TYPE_FAULT, message)

    }

    actual fun error(message: String?) {
        if (BuildConfig.DEBUG && message != null)
            _os_log_internal(__dso_handle.ptr, log, OS_LOG_TYPE_ERROR, message)

    }

}
