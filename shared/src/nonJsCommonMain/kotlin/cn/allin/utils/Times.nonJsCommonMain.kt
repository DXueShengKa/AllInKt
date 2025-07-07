package cn.allin.utils

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

actual fun Instant.getDateTime(): LocalDateTime {
    return toLocalDateTime(TimeZone.currentSystemDefault())
}
