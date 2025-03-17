package cn.allin.vo

import kotlinx.serialization.Serializable


@Serializable
data class OffiAccoutMsgVO(
    val toUserName: String,
    val fromUserName: String,
    val msgType: String,
    val content: String? = null,
    val msgId: Long? = null,
    val picUrl: String? = null,
    val mediaId: Long? = null,
    val idx: String? = null,
    val event: String? = null,
    val createTime: Long,
)