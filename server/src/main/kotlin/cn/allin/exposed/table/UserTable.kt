package cn.allin.exposed.table

import cn.allin.config.UserRole
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime


object UserTable : IntIdTable("user") {
    val name = varchar("name", 30)
    val birthday = date("birthday").nullable()
    val password = varchar("password",128)
    val role = enumeration<UserRole>("role").default(UserRole.ROLE_USER)
    val updateTime = datetime("updateTime").default(LocalDateTime.now())
    val createTime = datetime("createTime").defaultExpression(CurrentDateTime)
}