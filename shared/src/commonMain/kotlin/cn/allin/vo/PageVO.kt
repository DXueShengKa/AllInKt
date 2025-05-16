package cn.allin.vo

import kotlinx.serialization.Serializable

@Serializable
data class PageVO<T>(
    val rows: List<T>,
    val totalRow: Long,
    val totalPage: Int
)
