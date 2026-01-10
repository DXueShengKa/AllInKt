package cn.allin.model

import cn.allin.config.UserRole
import cn.allin.vo.Gender
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.datetime

object UserTable : LongIdTable("users") {
    val name = varchar("name", length = 255)
    val email = varchar("email", length = 255).nullable()
    val password = varchar("password", length = 255)
    val gender = enumerationByName<Gender>("gender", 10)
    val role = enumerationByName<UserRole>("role", 10)
    val birthday = date("birthday").nullable()
    val address = varchar("address", length = 255).nullable()
    val updateTime = datetime("update_time")
    val createTime = datetime("create_time")
}
