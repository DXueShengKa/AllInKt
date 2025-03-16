package cn.allin.controller

import cn.allin.ServerRoute
import cn.allin.service.OffiaccountService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.security.MessageDigest

@RestController
@RequestMapping(ServerRoute.OFFI_ACCOUNT)
class OffiaccountController(
    private val service: OffiaccountService
) {

    companion object {
        //随机的token
        private const val WX_TOKEN = "b889a8e9af4bd"
    }

    private val logger = LoggerFactory.getLogger(OffiaccountController::class.java)

    //https://developers.weixin.qq.com/miniprogram/dev/framework/server-ability/message-push.html#%E6%B6%88%E6%81%AF%E6%8E%A8%E9%80%81
    @GetMapping
    fun get(
        signature: String,
        timestamp: String,
        nonce: String,
        echostr: String
    ): String {

        logger.info("微信验证请求:{},{},{}", timestamp, nonce, echostr)

        val k = arrayOf(WX_TOKEN, timestamp, nonce)
            .also { it.sort() }
            .joinToString("")

        val sha1 = MessageDigest.getInstance("SHA-1")
            .digest(k.toByteArray())
            .joinToString("") {
                "%02x".format(it)
            }

        logger.info("微信验证签名signature:{},sha1:{}", signature, sha1)

        return if (signature == sha1)
            echostr
        else
            "error"
    }

    @GetMapping("token")
    fun token(): Mono<String> {
        return service.accessToken()
    }
}