package cn.allin.model

import cn.allin.config.UserRole
import cn.allin.ksp.server.EntityToVo
import cn.allin.vo.Gender
import cn.allin.vo.UserVO
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.GenerationType
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Table
import java.time.LocalDate
import java.time.LocalDateTime

@EntityToVo([UserVO::class])
@Entity
@Table(name = "users")
interface UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long

    val name: String
    val email: String?
    val password: String
    val gender: Gender?
    val role: UserRole
    val birthday: LocalDate?
    val address: String?
    val updateTime: LocalDateTime
    val createTime: LocalDateTime
}
