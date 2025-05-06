package cn.allin

import arrow.core.serialization.ArrowModule
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf

/**
 * 小写是给route类型使用的，全大写是给注解使用的
 */
interface apiRoute {
    val path: String

    companion object {
        const val PAGE = "page"
        const val PATH_ID = "/{id}"
    }


    object auth : apiRoute {
        const val AUTH = "auth"
        override val path: String = AUTH
    }

    object offiAccount : apiRoute {
        const val OFFI_ACCOUNT = "offiaccount"
        override val path: String = OFFI_ACCOUNT
    }
}

object ServerParams {
    const val PAGE_INDEX = "pageIndex"
    const val PAGE_SIZE = "pageSize"
    const val ID = "id"
}

val AllJson = Json {
    ignoreUnknownKeys = true
    serializersModule = ArrowModule
}

@OptIn(ExperimentalSerializationApi::class)
val AllProtoBuf: BinaryFormat = ProtoBuf {
    serializersModule = ArrowModule
}

