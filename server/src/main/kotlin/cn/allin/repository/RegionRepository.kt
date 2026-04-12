package cn.allin.repository

import cn.allin.table.RegionTable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface RegionRepository : CoroutineCrudRepository<RegionTable, Int> {
    suspend fun findByParentId(parentId: Int): List<RegionTable>
}
