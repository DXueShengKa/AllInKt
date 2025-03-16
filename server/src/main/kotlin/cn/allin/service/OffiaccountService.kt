package cn.allin.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class OffiaccountService(
    private val webClient: WebClient,
    private val redisService: RedisService
) {
    companion object {
        const val WX_CACHE_NAME = "wx"

        /**
         * 微信accessToken缓存key
         */
        const val WX_ACCESS_TOKEN = "$WX_CACHE_NAME:accessToken"
    }

    @Value("\${wx.appid}")
    lateinit var wxAppid: String

    @Value("\${wx.secret}")
    lateinit var wxSecret: String

//        @Cacheable(cacheNames = [WX_CACHE_NAME], key = "#root.method.name")

    fun accessToken(): Mono<String> {

        return redisService.get(WX_ACCESS_TOKEN)
            .switchIfEmpty(
                webClient.get()
                    .uri("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=$wxAppid&secret=$wxSecret")
                    .retrieve()
                    .bodyToMono(WxAccessToken::class.java)
                    .flatMap {
                        redisService.set(WX_ACCESS_TOKEN, it.access_token, it.expires_in.toLong())
                            .thenReturn(it.access_token)
                    }
            )

    }

    class WxAccessToken(
        val access_token: String,
        val expires_in: Int
    )
}