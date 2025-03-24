package cn.allin.vo

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class QandaVO(
    val id: Int? = null,
    val question: String,
    val answer: String,
    val tagIds: IntArray? = null,
    val createTime: LocalDateTime? = null,
)
