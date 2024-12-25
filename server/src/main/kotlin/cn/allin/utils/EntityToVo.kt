package cn.allin.utils

import cn.allin.model.RegionEntity
import cn.allin.model.UserEntity
import kotlinx.datetime.toKotlinLocalDate

fun UserEntity.toVO(): cn.allin.vo.UserVO = cn.allin.vo.UserVO(
    userId = id,
    name = name,
    birthday = birthday?.toKotlinLocalDate(),
    role = role.name
)

fun RegionEntity.toVO(): cn.allin.vo.RegionVO = cn.allin.vo.RegionVO(
    id = id,
    parentId = parentId,
    name = name
)