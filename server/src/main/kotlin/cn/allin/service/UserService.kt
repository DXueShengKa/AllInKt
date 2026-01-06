package cn.allin.service

import cn.allin.repository.UserRepository
import cn.allin.utils.DispatchersVirtual
import cn.allin.utils.SpringUser
import io.lettuce.core.KillArgs.Builder.id
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(
    private val userRepository: UserRepository
) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        if (username.isEmpty()) {
            return Mono.error(UsernameNotFoundException("没有账号"))
        }

        return mono(DispatchersVirtual) {
            val user = userRepository.findPasswordRole(username.toLong()) ?: throw UsernameNotFoundException("没有账号")
            val u = SpringUser(user.id.toString(), "{noop}"+user.password, listOf(user.role))
            u
        }
    }

}
