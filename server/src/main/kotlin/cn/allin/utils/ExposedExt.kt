package cn.allin.utils

import cn.allin.vo.PageVO
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.r2dbc.Query

suspend fun <T> Query.paginate(
    page: Int,
    size: Int,
    mapper: (ResultRow) -> T
): PageVO<T> {
    val total = this.count()
    val totalPage = (total + size - 1) / size

    val list = this.limit(size)
        .offset(((page - 1) * size).toLong())
        .map(mapper)
        .toList()

    return PageVO(list, total, totalPage.toInt())
}
