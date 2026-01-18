package cn.allin.model

import cn.allin.config.UserRole

data class UserDTO(
    val id: Long,
    val name: String,
    val role: UserRole,
    val password: String?
)
