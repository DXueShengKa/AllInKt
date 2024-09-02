@file:JsModule("antd")

package ant

external val message: AntMessage


@JsName("Message")
external interface AntMessage {
    fun success(content: String, duration: Int = definedExternally, onClose: (() -> Unit)? = definedExternally)
    fun error(content: String, duration: Int = definedExternally, onClose: (() -> Unit)? = definedExternally)
    fun info(content: String, duration: Int = definedExternally, onClose: (() -> Unit)? = definedExternally)
    fun warning(content: String, duration: Int = definedExternally, onClose: (() -> Unit)? = definedExternally)
    fun loading(content: String, duration: Int = definedExternally, onClose: (() -> Unit)? = definedExternally)
}