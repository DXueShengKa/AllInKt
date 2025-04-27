package cn.allin.vo

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class QaTagVO(
    val id: Int = 0,
    val tagName: String,
    val description: String? = null,
    val createTime: LocalDateTime? = null,
)
