package cn.allin.exposed.entity

import cn.allin.vo.UserVO
import kotlinx.datetime.toKotlinLocalDate

fun UserEntity.toVo() = UserVO(
    id.value, name, null, birthday?.toKotlinLocalDate(), role.name
)