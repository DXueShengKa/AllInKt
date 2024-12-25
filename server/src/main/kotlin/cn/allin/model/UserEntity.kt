package cn.allin.model

import cn.allin.config.UserRole
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.GenerationType
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Table
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
}