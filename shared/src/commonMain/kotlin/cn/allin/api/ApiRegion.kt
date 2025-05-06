package cn.allin.api

import cn.allin.vo.RegionVO
import kotlin.jvm.JvmStatic

interface ApiRegion {
    suspend fun getAllProvince(): List<RegionVO>
    suspend fun getCity(provinceId: Int): List<RegionVO>
    suspend fun getCounty(cityId: Int): List<RegionVO>

    companion object {
        const val PROVINCE = "province"
        const val PROVINCE_ID = "provinceId"
        const val CITY = "city"
        const val CITY_ID = "cityId"
        const val COUNTRY = "country"

        @JvmStatic
        fun pathProvince() = "region/$PROVINCE"

        const val pathCity = "region/$CITY/{$PROVINCE_ID}"

        @JvmStatic
        fun pathCity(provinceId: Int) = "region/$CITY/$provinceId"

        const val pathCountry = "region/$COUNTRY/{$CITY_ID}"

        @JvmStatic
        fun pathCountry(cityId: Int) = "region/$COUNTRY/$cityId"
    }
}
