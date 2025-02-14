package cn.allin

import cn.allin.vo.UserVO
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
            if (userVO.name.isEmpty())
                return VoValidator(UserVO.name, "不可空", "名字不能为空")

            if (userVO.name.length in 1..15)
                return VoValidator(UserVO.name, "超出范围", "长度1-15")


            val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
            if (userVO.birthday != null && userVO.birthday !in LocalDate(1900, 1, 1)..now) {
                return VoValidator(UserVO.birthday, "超出范围", "生日范围超过1900到今天")
            }

            return null
        }
    }
}
