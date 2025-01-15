@file:JsModule("antd")

package ant

import web.html.HTMLElement
import kotlin.js.Promise


external val message: AntMessage


external interface AntMessage {

    fun success(content: String, duration: Int = definedExternally, onClose: (() -> Unit)? = definedExternally)
    fun useMessage(configOptions: AntMessageConfigOptions?=definedExternally): UseMessage
}

external interface AntMessageApi {

    fun success(type: AntMessageType): Promise<Unit>

    fun success(content: String, duration: Int = definedExternally, onClose: (() -> Unit)? = definedExternally)
    fun error(content: String, duration: Int = definedExternally, onClose: (() -> Unit)? = definedExternally)
    fun info(content: String, duration: Int = definedExternally, onClose: (() -> Unit)? = definedExternally)
    fun warning(content: String, duration: Int = definedExternally, onClose: (() -> Unit)? = definedExternally)
    fun loading(content: String, duration: Int = definedExternally, onClose: (() -> Unit)? = definedExternally)
}




external interface AntMessageConfigOptions {
    var top: Number
    var duration: Number
    var prefixCls: String
    var getContainer: HTMLElement
    var transitionName: String
    var maxCount: Number
    var rtl: Boolean
}

external interface AntMessageType{
    var content: String
    var duration: Int
    var onClose: (() -> Unit)?
}

