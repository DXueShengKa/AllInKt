package cn.allin.repository

import cn.allin.model.RegionEntity
import org.babyfish.jimmer.spring.repository.KRepository

interface RegionRepository : KRepository<RegionEntity, Int> {
    fun findByParentId(parentId: Int): List<RegionEntity>
}
