package cn.allin.vo

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 *
 * @param tagName 标签
 * @param description 标签介绍
 * @param createTime 创建时间
 */
@Serializable
data class QaTagVO(
    val id: Int = 0,
    val tagName: String,
    val description: String? = null,
    val createTime: LocalDateTime? = null,
)
