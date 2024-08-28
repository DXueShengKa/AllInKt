package cn.allin.exposed.entity

import cn.allin.exposed.table.UserTable
import cn.allin.vo.UserVO
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDate
import java.time.LocalDateTime


class User(
    id: EntityID<Int>
) : IntEntity(id) {
    companion object : IntEntityClass<User>(UserTable)

    val birthday: LocalDate? by UserTable.birthday
    val name: String by UserTable.name
    val password: String by UserTable.password
    val updateTime: LocalDateTime by UserTable.updateTime
    val createTime: LocalDateTime by UserTable.createTime

    fun toVo(): UserVO {
        return UserVO(
            id.value, name,null, birthday?.toKotlinLocalDate()
        )
    }
}
