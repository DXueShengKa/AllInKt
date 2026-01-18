package cn.allin.table

import cn.allin.config.UserRole
import cn.allin.ksp.server.TableToVo
import cn.allin.model.UserDTO
import cn.allin.vo.Gender
import cn.allin.vo.UserVO
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.datetime

@TableToVo(
    [UserVO::class, UserDTO::class]
)
object UserTable : LongIdTable("users") {
    val name = varchar("name", length = 30)
    val email = varchar("email", length = 128).nullable()
    val password = varchar("password", length = 128)
    val gender =
        customEnumeration(
            name = "gender",
            sql = "smallint",
            fromDb = { Gender.entries[(it as Short).toInt()] },
            toDb = { it.ordinal.toShort() },
        )
    val role =
        customEnumeration(
            name = "role",
            sql = "user_eole",
            fromDb = { UserRole.valueOf(it.toString()) },
            toDb = { it.name },
        )
    val birthday = date("birthday").nullable()
    val address = varchar("address", length = 50).nullable()
    val updateTime = datetime("update_time")
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime)
}
