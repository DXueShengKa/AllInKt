package cn.allin.config

import org.springframework.security.core.GrantedAuthority

enum class UserRole : GrantedAuthority {
    ROLE_ADMIN {
        override fun getAuthority(): String {
            return name
        }
    },
    ROLE_USER {
        override fun getAuthority(): String {
            return name
        }
    }
}