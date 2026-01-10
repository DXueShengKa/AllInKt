package cn.allin.config

import org.springframework.security.core.GrantedAuthority

enum class UserRole : GrantedAuthority {

//    @EnumItem(name = "Admin")
    ROLE_ADMIN {
        override fun getAuthority(): String {
            return name
        }
    },

//    @EnumItem(name = "User")
    ROLE_USER {
        override fun getAuthority(): String {
            return name
        }
    }
}
