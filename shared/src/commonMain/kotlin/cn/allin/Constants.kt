package cn.allin

import arrow.core.serialization.ArrowModule
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf


object ServerRoute {


    const val USER = "user"

    const val PAGE = "page"

    const val AUTH = "auth"


    /**
     * 公众号
     */
    const val OFFI_ACCOUNT = "offiaccount"

    object Region {
        const val ROUTE = "region"

        const val CITY = "/city"

        const val PROVINCE = "/province"

        const val COUNTY = "/county"
    }

    object Qanda {
        const val ROUTE = "qanda"
    }
}

object ServerParams {
    const val PAGE_INDEX = "pageIndex"
    const val PAGE_SIZE = "pageSize"
}

val AllJson = Json {
    ignoreUnknownKeys = true
    serializersModule = ArrowModule
}

@OptIn(ExperimentalSerializationApi::class)
val AllProtoBuf:BinaryFormat = ProtoBuf {
    serializersModule = ArrowModule
}

