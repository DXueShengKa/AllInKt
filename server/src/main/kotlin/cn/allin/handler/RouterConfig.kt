package cn.allin.handler

import cn.allin.ServerRoute
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class RouterConfig {

    @Bean
    fun region(handler: RegionHandler) = coRouter {
        ServerRoute.Region.ROUTE.nest {
            GET(ServerRoute.Region.PROVINCE, handler::getAllProvince)
            GET(ServerRoute.Region.CITY + "/{provinceId}", handler::getCity)
            GET(ServerRoute.Region.COUNTY + "/{cityId}", handler::getCounty)
        }
    }
}