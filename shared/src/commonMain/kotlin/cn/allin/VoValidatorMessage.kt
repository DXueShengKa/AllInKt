package cn.allin

import cn.allin.VoValidatorMessage.Companion.user
import cn.allin.vo.UserVO
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlin.js.ExperimentalJsStatic
import kotlin.js.JsStatic
import kotlin.jvm.JvmStatic

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

    @OptIn(ExperimentalJsStatic::class)
    companion object : VoValidator<Any> {

        override fun validator(obj: Any): VoValidatorMessage? {
            return when (obj) {
                is UserVO -> PostUserValidator().validator(obj)
                else -> null
            }
        }

        @JvmStatic
        @JsStatic
        fun user(userVO: UserVO, field: String, checkNull: Boolean): VoValidatorMessage? {

            when (field) {
                VoFieldName.UserVO_name -> {
                    if (checkNull && userVO.name.isNullOrEmpty())
                        return VoValidatorMessage(VoFieldName.UserVO_name, "不能为空", "名字")

                    if (userVO.name?.length !in 1..15)
                        return VoValidatorMessage(VoFieldName.UserVO_name, "超出范围", "长度1-15")
                }

                VoFieldName.UserVO_password -> {
                    if (checkNull && userVO.email.isNullOrEmpty())
                        return VoValidatorMessage(VoFieldName.UserVO_name, "不能为空", "密码")
                }

                VoFieldName.UserVO_birthday -> {
                    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                    if (userVO.birthday != null && userVO.birthday !in LocalDate(1900, 1, 1)..now) {
                        return VoValidatorMessage(VoFieldName.UserVO_birthday, "超出范围", "生日范围超过1900到今天")
                    }
                }

                VoFieldName.UserVO_email -> {
                    if (!userVO.email.isNullOrEmpty()) {
                        if (!(userVO.email matches RegexValidatorEmail)) {
                            return VoValidatorMessage(VoFieldName.UserVO_birthday, "格式错误", "邮箱")
                        }
                    }
                }
            }


            return null
        }
    }
}

fun interface VoValidator<T> {
    fun validator(obj: T): VoValidatorMessage?
}

class PostUserValidator() : VoValidator<UserVO> {
    override fun validator(obj: UserVO): VoValidatorMessage? {
        val descriptor: SerialDescriptor = UserVO.serializer().descriptor
        for (i in 0..<descriptor.elementsCount) {
            val validator = user(obj, descriptor.getElementName(i), true)
            if (validator != null)
                return validator
        }
        return null
    }
}

class PutUserValidator() : VoValidator<UserVO> {
    override fun validator(obj: UserVO): VoValidatorMessage? {
        if (obj.id < 1) return VoValidatorMessage(VoFieldName.UserVO_id, "不能为空", "用户id")

        val descriptor: SerialDescriptor = UserVO.serializer().descriptor
        for (i in 0..<descriptor.elementsCount) {
            val validator = user(obj, descriptor.getElementName(i), false)
            if (validator != null)
                return validator
        }
        return null
    }
}
