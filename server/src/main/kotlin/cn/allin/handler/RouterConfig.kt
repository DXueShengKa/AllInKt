package cn.allin.handler

import cn.allin.api.ApiRegion
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfig {


    private suspend inline fun createResponse(body: Any): ServerResponse {
        return ServerResponse
            .ok()
            .bodyValueAndAwait(body)
    }


    @Bean
    fun region(handler: RegionHandler) = coRouter {
        contentType(MediaType.APPLICATION_JSON)
            .nest {
                GET(ApiRegion.pathProvince()) {
                    createResponse(handler.getAllProvince())
                }

                GET(ApiRegion.pathCity) {
                    createResponse(
                        handler.getCity(it.pathVariable(ApiRegion.PROVINCE_ID).toInt())
                    )
                }

                GET(ApiRegion.pathCountry) {
                    createResponse(
                        handler.getCounty(it.pathVariable(ApiRegion.CITY_ID).toInt())
                    )
                }
            }

    }
}
