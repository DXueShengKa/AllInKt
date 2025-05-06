package cn.allin

import cn.allin.vo.QandaVO
import cn.allin.vo.UserVO
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsStatic

class ValidatorError(val validatorMessage: VoValidatorMessage) :
    Exception(validatorMessage.code + validatorMessage.message)


/**
 * 邮箱
 */
val RegexValidatorEmail = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()

@Serializable
class VoValidatorMessage(
    val field: String,
    val code: String,
    val message: String
) {

    constructor(field: VoField,code: String):this(field.name,code,field.display)

    @OptIn(ExperimentalJsStatic::class)
    companion object : VoValidator<Any> {
        const val CodeNotNull = "不能为空"
        const val CodeOutOfRange = "超出范围"


        override fun validator(obj: Any): VoValidatorMessage? {
            return when (obj) {
                is UserVO -> UserVO.valid(obj).leftOrNull()
                is QandaVO -> QandaVO.valid(obj).leftOrNull()
                else -> null
            }
        }

    }
}

fun interface VoValidator<T> {
    fun validator(obj: T): VoValidatorMessage?
}

class PostUserValidator() : VoValidator<UserVO> {
    override fun validator(obj: UserVO): VoValidatorMessage? {
        return UserVO.valid(obj).leftOrNull()
    }
}

class PutUserValidator() : VoValidator<UserVO> {
    override fun validator(obj: UserVO): VoValidatorMessage? {
        return UserVO.validPut(obj).leftOrNull()
    }
}
