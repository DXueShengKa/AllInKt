package cn.allin.model

import cn.allin.config.UserRole
import cn.allin.vo.UserVO
import kotlinx.datetime.toKotlinLocalDate
import org.babyfish.jimmer.sql.*
import java.time.LocalDate
import java.time.LocalDateTime


@Entity
@Table(name = "users")
interface UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long

    val name: String
    val birthday: LocalDate?
    val password: String
    val role: UserRole
    val updateTime: LocalDateTime
    val createTime: LocalDateTime

    companion object {

        @JvmStatic
        fun UserEntity.toVo(): UserVO = UserVO(
            userId = id,
            name = name,
            birthday = birthday?.toKotlinLocalDate(),
            role = role.name
        )
    }
}