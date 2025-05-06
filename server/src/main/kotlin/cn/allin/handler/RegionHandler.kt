package cn.allin.handler

import cn.allin.api.ApiRegion
import cn.allin.repository.RegionRepository
import cn.allin.vo.RegionVO
import org.springframework.stereotype.Component

@Component
class RegionHandler(
    private val regionRepository: RegionRepository,
) : ApiRegion {

    override suspend fun getAllProvince(): List<RegionVO> {
        return regionRepository.findAllProvince()
    }

    override suspend fun getCity(provinceId: Int): List<RegionVO> {
        return regionRepository.findCity(provinceId)
    }

    override suspend fun getCounty(cityId: Int): List<RegionVO> {
        return regionRepository.findCounty(cityId)
    }


}
