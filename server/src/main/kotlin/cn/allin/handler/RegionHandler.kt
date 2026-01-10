package cn.allin.handler

import cn.allin.api.ApiRegion
import cn.allin.vo.RegionVO
import org.springframework.stereotype.Component

@Component
class RegionHandler(
//    private val regionRepository: RegionRepository,
) : ApiRegion {

    override suspend fun getAllProvince(): List<RegionVO> {

        TODO()
//        return regionRepository.findByParentId(1).map { it.toRegionVO() }
    }

    override suspend fun getCity(provinceId: Int): List<RegionVO> {

        TODO()
//        return regionRepository.findByParentId(provinceId).map { it.toRegionVO() }
    }

    override suspend fun getCounty(cityId: Int): List<RegionVO> {
        TODO()
//        return regionRepository.findByParentId(cityId).map { it.toRegionVO() }
    }


}
