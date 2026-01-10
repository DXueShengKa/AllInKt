package cn.allin.utils

import cn.allin.BuildConfig
import java.util.logging.Logger

actual class InLogger(
    private val logger: Logger,
) {
    constructor(tag: String) : this(Logger.getLogger(tag))

    actual fun info(message: String?) {
        if (BuildConfig.DEBUG && message != null) {
            logger.config(message)
        }
    }

    actual fun debug(message: String?) {
        if (BuildConfig.DEBUG && message != null) {
            logger.info(message)
        }
    }

    actual fun warning(message: String?) {
        if (BuildConfig.DEBUG && message != null) {
            logger.warning(message)
        }
    }

    actual fun error(message: String?) {
        if (BuildConfig.DEBUG && message != null) {
            logger.severe(message)
        }
    }
}
