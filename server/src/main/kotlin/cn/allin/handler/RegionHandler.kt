package cn.allin.handler

import cn.allin.api.ApiRegion
import cn.allin.repository.RegionRepository
import cn.allin.table.RegionTable
import cn.allin.vo.RegionVO
import org.springframework.stereotype.Component

@Component
class RegionHandler(
    private val regionRepository: RegionRepository,
) : ApiRegion {
    override suspend fun getAllProvince(): List<RegionVO> =
        regionRepository
            .findByParentId(1)
            .toVO()

    override suspend fun getCity(provinceId: Int): List<RegionVO> {
        return regionRepository.findByParentId(provinceId).toVO()
    }

    override suspend fun getCounty(cityId: Int): List<RegionVO> {
        return regionRepository.findByParentId(cityId).toVO()
    }

    private fun List<RegionTable>.toVO() = map { RegionVO(it.id, it.parentId, it.name) }
}
