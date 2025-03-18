package cn.allin.controller

import cn.allin.ServerRoute
import cn.allin.service.OffiaccountService
import cn.allin.vo.OffiAccoutMsgVO
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.w3c.dom.Element
import org.w3c.dom.Node
import reactor.core.publisher.Mono
import java.security.MessageDigest
import javax.xml.parsers.DocumentBuilderFactory


@RestController
@RequestMapping(ServerRoute.OFFI_ACCOUNT)
class OffiaccountController(
    private val service: OffiaccountService,
) {

    private val xmlDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
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

        val k = arrayOf(service.wxToken, timestamp, nonce)
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


    @PostMapping
    fun post(@RequestBody body: String): Mono<String> {
        val p = body.byteInputStream()
        val xml = xmlDoc.parse(p).documentElement
        val vo = elementToMsgVO(xml)
        p.close()
        xmlDoc.reset()
//        logger.info("微信推送消息:{}", vo)

        return service.answer(vo,service.saveMsg(vo))
    }


    private fun elementToMsgVO(xml: Element): OffiAccoutMsgVO {
        val cn = xml.childNodes

        var toUserName = ""
        var fromUserName = ""
        var msgType = ""
        var content: String? = null
        var msgId: Long? = null
        var picUrl: String? = null
        var mediaId: Long? = null
        var createTime: Long = 0
        var idx: String? = null
        var event: String? = null

        for (i in 0..<cn.length) {
            val item = cn.item(i)
            if (item.nodeType == Node.ELEMENT_NODE) {
                item as Element
                when (item.nodeName) {
                    "ToUserName" -> {
                        toUserName = item.textContent
                    }

                    "FromUserName" -> {
                        fromUserName = item.textContent
                    }

                    "MsgType" -> {
                        msgType = item.textContent
                    }

                    "Content" -> {
                        content = item.textContent
                    }

                    "MsgId" -> {
                        msgId = item.textContent.toLong()
                    }

                    "PicUrl" -> {
                        picUrl = item.textContent
                    }

                    "MediaId" -> {
                        mediaId = item.textContent.toLong()
                    }

                    "CreateTime" -> {
                        createTime = item.textContent.toLong()
                    }

                    "Idx" -> {
                        idx = item.textContent
                    }

                    "Event" -> {
                        event = item.textContent
                    }
                }
            }
        }
        return OffiAccoutMsgVO(
            toUserName,
            fromUserName,
            msgType,
            content,
            msgId,
            picUrl,
            mediaId,
            idx,
            event,
            createTime,
        )
    }

    @GetMapping("token")
    fun token(): Mono<String> {
        return service.accessToken()
    }
}