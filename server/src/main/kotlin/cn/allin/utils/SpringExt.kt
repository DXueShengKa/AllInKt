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
