package cn.allin.vo

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class UserVO(
    val userId: Int = 0,
    val name: String,
    val password: String? = null,
    val birthday: LocalDate? = null,
//    val updateTime: LocalDateTime
)
