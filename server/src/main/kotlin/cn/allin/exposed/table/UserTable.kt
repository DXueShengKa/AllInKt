package cn.allin.exposed.table

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime


object UserTable : IntIdTable("user") {
    val name = varchar("name", 30)
    val age = ubyte("age")
    val updateTime = datetime("updateTime").default(LocalDateTime.now())
    val createTime = datetime("createTime").defaultExpression(CurrentDateTime)
}