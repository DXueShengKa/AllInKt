package cn.allin.exposed.entity

import cn.allin.config.UserRole
import cn.allin.exposed.table.UserTable
import cn.allin.vo.UserVO
import kotlinx.datetime.toKotlinLocalDate
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDate
import java.time.LocalDateTime


class UserEntity(
    id: EntityID<Int>
) : IntEntity(id) {

    companion object : IntEntityClass<UserEntity>(UserTable)

    val birthday: LocalDate? by UserTable.birthday
    val name: String by UserTable.name
    val password: String by UserTable.password
    val role: UserRole by UserTable.role
    val updateTime: LocalDateTime by UserTable.updateTime
    val createTime: LocalDateTime by UserTable.createTime

    fun toVo(): UserVO {
        return UserVO(
            id.value, name, null, birthday?.toKotlinLocalDate()
        )
    }
}
