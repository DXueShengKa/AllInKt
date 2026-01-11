package cn.allin.config

import org.springframework.security.core.GrantedAuthority

enum class UserRole : GrantedAuthority {
    Admin {
        override fun getAuthority(): String = "ROLE_ADMIN"
    },

    User {
        override fun getAuthority(): String = "ROLE_USER"
    },
}
