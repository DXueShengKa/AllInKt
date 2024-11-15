package cn.allin.service

import cn.allin.repository.UserRepository
import cn.allin.utils.SpringUser
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

//@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(id: String?): UserDetails {
        if (id.isNullOrEmpty()) {
            throw UsernameNotFoundException("没有账号")
        }

        val user = userRepository.findById(id.toLong()) ?: throw UsernameNotFoundException("没有账号")

        return SpringUser(user.id.toString(), user.password, listOf(user.role))
    }
}