package cn.allin.utils

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


val Authentication.id: Int
    get() = principal as Int


typealias SpringUser = org.springframework.security.core.userdetails.User


val UserDetails.id: Int
    get() = password.toInt()


fun newAuthenticationToken(id: Int, password: String) =
    UsernamePasswordAuthenticationToken(id, password)


fun newAuthenticationToken(id: Int, password: String, authorities: Collection<GrantedAuthority>) =
    UsernamePasswordAuthenticationToken(id, password, authorities)
