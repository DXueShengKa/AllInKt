package cn.allin.vo

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsStatic
import kotlin.js.JsStatic
import kotlin.jvm.JvmStatic

@Serializable
data class MsgVO<T>(
    val message: String,
    val data: T? = null
) {

    @OptIn(ExperimentalJsStatic::class)
    companion object {
        const val success = "成功"
        const val fail = "失败"
        const val error = "错误"

        const val add = "添加"
        const val delete = "删除"
        const val auth = "认证"
        const val login = "登录"


        @JvmStatic
        @JsStatic
        fun <T> success(data: T?): MsgVO<T> {
            return MsgVO(success, data)
        }


        @JvmStatic
        @JsStatic
        fun <F> fail(msg: String, data: F? = null): MsgVO<F> {
            return MsgVO(msg + fail, data)
        }


        @JvmStatic
        @JsStatic
        fun <E> error(msg: String, data: E? = null): MsgVO<E> {
            return MsgVO(msg + error, data)
        }
    }

    val dataOrError: T
        get() = data ?: kotlin.error(message)
}
