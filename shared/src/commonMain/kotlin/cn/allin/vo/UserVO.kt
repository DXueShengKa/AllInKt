package cn.allin.vo

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * 用户数据
 */
@Serializable
data class UserVO(
    val userId: Long = 0,
    /**
     * 名字
     */
    val name: String,
    /**
     * 生日
     */
    val password: String? = null,
    val birthday: LocalDate? = null,
    val role: String? = null,
    val address: String? = null,
    val gender: Gender? = null
)