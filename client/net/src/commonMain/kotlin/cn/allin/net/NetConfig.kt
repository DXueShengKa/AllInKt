package cn.allin.net

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf

val ContentTypeXProtobuf = ContentType("application", "x-protobuf")

const val SERVER_BASE_URL = "http://localhost:8081/"


@OptIn(ExperimentalSerializationApi::class)
fun HttpClientConfig<*>.contentConverter() {
    install(ContentNegotiation) {
        register(ContentType.Application.Json, KotlinxSerializationConverter(Json))
        register(ContentTypeXProtobuf, KotlinxSerializationConverter(ProtoBuf))
    }
}