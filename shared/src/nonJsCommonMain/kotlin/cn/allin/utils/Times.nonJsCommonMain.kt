package cn.allin.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

actual fun Instant.getDateTime(): LocalDateTime {
    return toLocalDateTime(TimeZone.currentSystemDefault())
}
