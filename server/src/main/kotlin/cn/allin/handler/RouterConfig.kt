package cn.allin.handler

import cn.allin.api.ApiRegion
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.ContentDisposition.inline
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.json

@Configuration
class RouterConfig {
    private val logger = LoggerFactory.getLogger(RouterConfig::class.java)

    private suspend inline fun createResponse(body: Any): ServerResponse =
        ServerResponse
            .ok()
            .json()
            .bodyValueAndAwait(body)

    @Bean
    fun region(handler: RegionHandler): RouterFunction<ServerResponse> =
        coRouter {
            GET(ApiRegion.pathProvince()) {
                createResponse(handler.getAllProvince())
            }

            GET(ApiRegion.pathCity) {
                createResponse(
                    handler.getCity(it.pathVariable(ApiRegion.PROVINCE_ID).toInt()),
                )
            }
            GET(ApiRegion.pathCountry) {
                createResponse(
                    handler.getCounty(it.pathVariable(ApiRegion.CITY_ID).toInt()),
                )
            }
        }
}
