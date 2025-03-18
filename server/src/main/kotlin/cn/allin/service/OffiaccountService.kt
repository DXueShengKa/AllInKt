package cn.allin.service

import cn.allin.repository.OffiaccountRepository
import cn.allin.vo.OffiAccoutMsgVO
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class OffiaccountService(
    private val webClient: WebClient,
    private val redisService: RedisService,
    private val repository: OffiaccountRepository
) {
    companion object {
        const val WX_CACHE_NAME = "wx"

        /**
         * 微信accessToken缓存key
         */
        const val WX_ACCESS_TOKEN = "$WX_CACHE_NAME:accessToken"
    }

    @Value("{wx.appid}")
    private lateinit var wxAppid: String

    @Value("{wx.secret}")
    private lateinit var wxSecret: String

    @Value("{wx.token}")
    lateinit var wxToken: String

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



    private fun createAnswer(toUser: String,formUser: String,content: String): String{
        val time = System.currentTimeMillis()
        return """
        <xml>
        <ToUserName>$toUser</ToUserName>
        <FromUserName>$formUser</FromUserName>
        <CreateTime>$time</CreateTime>
        <MsgType>text</MsgType>
        <Content><![CDATA[$content]]></Content>
        </xml>  
        """.trimIndent()
    }

    fun saveMsg(vo: OffiAccoutMsgVO): Long{
        return repository.acceptMsg(vo)
    }


    fun answer(vo: OffiAccoutMsgVO,msgId: Long): Mono<String>{

        val content = vo.content
        if (content.isNullOrEmpty())
            return Mono.empty()

        return Mono.just(repository.findAnswer(content))
            .filter { it.isNotEmpty() }
            .map {
                val a = it[0]
                repository.autoAnswer(a.id,msgId)
                createAnswer(vo.fromUserName, vo.toUserName, a.answer)
            }
    }

}