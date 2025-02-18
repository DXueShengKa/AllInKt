package cn.allin

import cn.allin.vo.UserVO
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlin.js.ExperimentalJsStatic
import kotlin.js.JsStatic
import kotlin.jvm.JvmStatic

class VoValidator(
    val field: String,
    val code: String,
    val message: String
) {

    @OptIn(ExperimentalJsStatic::class)
    companion object {

        @JvmStatic
        @JsStatic
        fun validator(obj: Any): VoValidator? {
            return when (obj) {
                is UserVO -> user(obj)
                else -> null
            }
        }

        @JvmStatic
        @JsStatic
        fun user(userVO: UserVO): VoValidator? {
            val descriptor: SerialDescriptor = UserVO.serializer().descriptor
            for (i in 0..<descriptor.elementsCount) {
                val validator = user(userVO, descriptor.getElementName(i))
                if (validator != null)
                    return validator
            }

            return null
        }

        @JvmStatic
        @JsStatic
        fun user(userVO: UserVO, field: String): VoValidator? {

            when (field) {
                VoFieldName.UserVO_name -> {
                    if (userVO.name.isNullOrEmpty())
                        return VoValidator(VoFieldName.UserVO_name, "不能为空", "名字")

                    if (userVO.name.length !in 1..15)
                        return VoValidator(VoFieldName.UserVO_name, "超出范围", "长度1-15")
                }

                VoFieldName.UserVO_birthday -> {
                    val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                    if (userVO.birthday != null && userVO.birthday !in LocalDate(1900, 1, 1)..now) {
                        return VoValidator(VoFieldName.UserVO_birthday, "超出范围", "生日范围超过1900到今天")
                    }
                }
            }


            return null
        }
    }
}
