package cn.allin.handler

import cn.allin.repository.RegionRepository
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class RegionHandler(
    private val regionRepository: RegionRepository,
) {

    suspend fun getAllProvince(request: ServerRequest): ServerResponse {
        val v = regionRepository.findAllProvince()

        return ServerResponse
            .ok()
//            .contentType(MediaType.APPLICATION_PROTOBUF)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValueAndAwait(v)
    }

    suspend fun getCity(request: ServerRequest): ServerResponse {
        val v = regionRepository.findCity(
            provinceId = request.pathVariable("provinceId").toInt()
        )

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(v)
    }

    suspend fun getCounty(request: ServerRequest): ServerResponse {
        val v = regionRepository.findCounty(
            cityId = request.pathVariable("cityId").toInt()
        )

        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(v)
//        return ServerResponse.ok().contentType(MediaType.APPLICATION_PROTOBUF)
//            .bodyValueAndAwait(ProtoBuf.Default.encodeToByteArray(v))
    }



}
