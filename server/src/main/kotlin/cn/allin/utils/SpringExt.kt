package cn.allin.utils

import org.springframework.cache.Cache
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


val Authentication.id: Int
    get() = principal as Int


typealias SpringUser = org.springframework.security.core.userdetails.User


val UserDetails.id: Int
    get() = password.toInt()


inline fun <reified T> Cache.getValue(key: Any): T? {
    return this.get(key, T::class.java)
}


fun newAuthenticationToken(id: Int, password: String) =
    UsernamePasswordAuthenticationToken(id, password)


fun newAuthenticationToken(id: Int, password: String, authorities: Collection<GrantedAuthority>) =
    UsernamePasswordAuthenticationToken(id, password, authorities)


fun newAuthenticationToken(id: UInt, password: String) =
    UsernamePasswordAuthenticationToken(id, password)


fun newAuthenticationToken(id: UInt, password: String, authorities: Collection<GrantedAuthority>) =
    UsernamePasswordAuthenticationToken(id, password, authorities)



fun createTriggerUpdateTimestamp(tableName: String) =
    """
         create trigger "${tableName}_update_timestamp"
             before insert or update
             on "$tableName"
             for each row
         execute procedure update_timestamp_column();
    """.trimIndent()
