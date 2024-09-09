package cn.allin

import kotlinx.serialization.json.Json


object ServerRoute {


    const val USER = "/user"

    const val AUTH = "/auth"
}

object ServerParams {
    const val PAGE_INDEX = "pageIndex"
    const val PAGE_SIZE = "pageSize"
}

val InJson = Json {
    ignoreUnknownKeys = true

}
