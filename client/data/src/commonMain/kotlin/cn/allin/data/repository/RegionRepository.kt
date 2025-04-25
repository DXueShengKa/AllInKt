package cn.allin.data.repository

import cn.allin.api.ApiRegion
import cn.allin.vo.RegionVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.koin.core.annotation.Factory

@Factory
class RegionRepository(
    private val http: HttpClient
): ApiRegion {
    override suspend fun getAllProvince(): List<RegionVO> {
        return http.get(ApiRegion.pathProvince()).body()
    }

    override suspend fun getCity(provinceId: Int): List<RegionVO> {
        return http.get(ApiRegion.pathCity(provinceId)).body()
    }

    override suspend fun getCounty(cityId: Int): List<RegionVO> {
        return http.get(ApiRegion.pathCountry(cityId)).body()
    }
}
