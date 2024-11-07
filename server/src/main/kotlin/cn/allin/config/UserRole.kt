package cn.allin.config

import org.babyfish.jimmer.sql.EnumItem
import org.babyfish.jimmer.sql.EnumType
import org.springframework.security.core.GrantedAuthority

@EnumType(EnumType.Strategy.NAME)
enum class UserRole : GrantedAuthority {

    @EnumItem(name = "Admin")
    ROLE_ADMIN {
        override fun getAuthority(): String {
            return name
        }
    },

    @EnumItem(name = "User")
    ROLE_USER {
        override fun getAuthority(): String {
            return name
        }
    }
}