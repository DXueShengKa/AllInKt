package cn.allin.handler

import cn.allin.apiRoute
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfig {

    @Bean
    fun region(handler: RegionHandler) = coRouter {
        apiRoute.region.apply {
            GET(province.path, handler::getAllProvince)
            GET(city.path, handler::getCity)
            GET(country.path, handler::getCounty)
        }
    }
}
