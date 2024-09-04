package cn.allin.exposed.table

import cn.allin.config.UserRole
import cn.allin.utils.createTriggerUpdateTimestamp
import org.jetbrains.exposed.dao.id.UIntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime


object UserTable : UIntIdTable("user") {
    val name = varchar("name", 30)
    val birthday = date("birthday").nullable()
    val password = varchar("password", 128)
    val role = enumeration<UserRole>("role").default(UserRole.ROLE_USER)
    val updateTime = datetime("update_time")
    val createTime = datetime("create_time").defaultExpression(CurrentDateTime)

    override fun createStatement(): List<String> {
        val sqlList = super.createStatement().toMutableList()
        sqlList.add(createTriggerUpdateTimestamp(tableName))
        return sqlList
    }
}