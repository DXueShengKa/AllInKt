package cn.allin.vo

import kotlinx.serialization.Serializable

@Serializable
class MsgVO<T>(
    val message: String,
    val code: Int = OK,
    val data: T? = null
) {
    companion object {
        const val OK = 200
        const val USER_NOT_FOUND = 100_001
        const val USER_AUTH_ERR = 100_002
    }

    val dataOrError: T
        get() = data ?: error(message)
}