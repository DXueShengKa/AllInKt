package cn.allin

import kotlinx.serialization.json.Json


object ServerRoute {


    const val USER = "/user"

    const val AUTH = "/auth"

    object Region {
        const val ROUTE = "/region"

        const val CITY = "/city"

        const val PROVINCE = "/province"

        const val COUNTY = "/county"
    }
}

object ServerParams {
    const val PAGE_INDEX = "pageIndex"
    const val PAGE_SIZE = "pageSize"
}

val InJson = Json {
    ignoreUnknownKeys = true

}
