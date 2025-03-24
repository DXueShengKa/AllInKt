package cn.allin.vo

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class QaTagVO(
    val id: Int,
    val tagName: String,
    val description: String?,
    val createTime: LocalDateTime,
)
