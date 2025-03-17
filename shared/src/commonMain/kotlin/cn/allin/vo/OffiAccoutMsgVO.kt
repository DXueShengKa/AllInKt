package cn.allin.vo

import kotlinx.serialization.Serializable


@Serializable
data class OffiAccoutMsgVO(
    val toUserName: String,
    val fromUserName: String,
    val msgType: String,
    val content: String? = null,
    val msgId: Long,
    val picUrl: String? = null,
    val mediaId: String? = null,
    val createTime: Long,
)