package cn.allin.vo

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import cn.allin.RegexValidatorEmail
import cn.allin.VoFieldName
import cn.allin.VoValidatorMessage
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsStatic
import kotlin.js.JsStatic
import kotlin.jvm.JvmStatic

/**
 * @param name 名字
 * @param email 电子邮箱
 * @param password 密码
 * @param birthday 生日
 * @param role 权限
 * @param address 地址
 * @param gender 性别
 */
@Serializable
data class UserVO(
    val id: Long = 0,
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val birthday: LocalDate? = null,
    val role: String? = null,
    val address: String? = null,
    val gender: Gender? = null
) {

    @OptIn(ExperimentalJsStatic::class)
    companion object {

        @JvmStatic
        @JsStatic
        fun valid(vo: UserVO): Either<VoValidatorMessage, UserVO> = either {
            ensureNotNull(vo.name) {
                VoValidatorMessage(VoFieldName.UserVO_name, VoValidatorMessage.CodeNotNull, "名字")
            }

            ensure(vo.name.length in 1..15) {
                VoValidatorMessage(VoFieldName.UserVO_name, VoValidatorMessage.CodeOutOfRange, "长度1-15")
            }

            ensure(!vo.password.isNullOrEmpty()) { VoValidatorMessage(VoFieldName.UserVO_password, VoValidatorMessage.CodeNotNull, "密码") }

            if (vo.birthday != null) {
                val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                ensure(vo.birthday in LocalDate(1900, 1, 1)..now) {
                    VoValidatorMessage(VoFieldName.UserVO_birthday, VoValidatorMessage.CodeOutOfRange, "生日范围超过1900到今天")
                }
            }

            if (!vo.email.isNullOrEmpty()) {
                ensure(vo.email matches RegexValidatorEmail) { VoValidatorMessage(VoFieldName.UserVO_birthday, "格式错误", "邮箱") }
            }

            vo
        }


        @JvmStatic
        @JsStatic
        fun validPut(vo: UserVO): Either<VoValidatorMessage, UserVO> = either {
            ensure(vo.id > 0) { VoValidatorMessage(VoFieldName.UserVO_id, VoValidatorMessage.CodeNotNull, "用户id") }
            valid(vo).bind()
        }

    }
}
