package cn.allin.vo

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class FileVO(
    val id: Long,
    val name: String,
    val pathId: Int = 0,
    val md5: String? = null,
    val size: Long = 0,
    val createTime: LocalDateTime? = null,
)
