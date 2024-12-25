package cn.allin.vo

import kotlinx.serialization.Serializable

@Serializable
data class RegionVO(
    val id: Int,
    val parentId: Int,
    val name: String,
)