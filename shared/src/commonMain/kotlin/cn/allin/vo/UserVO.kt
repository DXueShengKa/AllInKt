package cn.allin.vo

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserVO(
    val userId: Int = 0,
    val age: UByte,
    val name: String,
//    val updateTime: LocalDateTime
)
