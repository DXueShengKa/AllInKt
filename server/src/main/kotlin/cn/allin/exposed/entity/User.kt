package cn.allin.exposed.entity

import cn.allin.exposed.table.UserTable
import cn.allin.vo.UserVO
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime


class User(
    id: EntityID<Int>
) : IntEntity(id) {
    companion object : IntEntityClass<User>(UserTable)


    val age: UByte by UserTable.age
    val name: String by UserTable.name
    val updateTime: LocalDateTime by UserTable.updateTime
    val createTime: LocalDateTime by UserTable.createTime

    fun toVo(): UserVO {
        return UserVO(
            id.value, age, name,// updateTime.toKotlinLocalDateTime()
        )
    }
}
