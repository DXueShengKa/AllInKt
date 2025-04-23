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

    object user : apiRoute {
        const val USER = "user"

        override val path: String = USER
        const val page = "$USER/${PAGE}"
        fun path(userId: String) = "$USER/$userId"
    }

    object auth : apiRoute {
        const val AUTH = "auth"
        override val path: String = AUTH
    }

    object offiAccount : apiRoute {
        const val OFFI_ACCOUNT = "offiaccount"
        override val path: String = OFFI_ACCOUNT
    }

    object region : apiRoute {
        const val REGION = "region"
        const val PROVINCE = "province"

        override val path: String = REGION

        val province = object : apiRoute {
            override val path: String = "$REGION/$PROVINCE"
        }

        class City : apiRoute {
            companion object {
                const val PROVINCE_ID = "provinceId"
                const val CITY = "city"
            }

            override val path: String = "$REGION/$CITY/{$PROVINCE_ID}"
            fun path(provinceId: Int) = "$REGION/$CITY/$provinceId"
        }

        val city = City()

        class Country : apiRoute {
            companion object {
                const val CITY_ID = "cityId"
                const val COUNTRY = "country"
            }
            override val path: String = "$REGION/$COUNTRY/{$CITY_ID}"
            fun path(cityId: Int) = "$REGION/$COUNTRY/$cityId"
        }

        val country = Country()

    }

    object qanda : apiRoute {
        const val QANDA = "qanda"

        const val page = "$QANDA/${PAGE}"
        override val path: String = QANDA
        fun path(id: Int) = "$QANDA/$id"


        object excel:apiRoute {
            const val EXCEL = "excel"
            override val path: String = "$QANDA/$EXCEL"
        }


        object tag : apiRoute {
            const val TAG = "tag"

            override val path: String = TAG

            object page : apiRoute {
                const val TAG_PAGE = "$TAG/${PAGE}"

                override val path: String = "$QANDA/$TAG_PAGE"
            }
        }
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

