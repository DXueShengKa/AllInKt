package cn.allin.vo

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.raise.ensureNotNull
import cn.allin.RegexValidatorEmail
import cn.allin.VoValidatorMessage
import cn.allin.birthday
import cn.allin.id
import cn.allin.name
import cn.allin.password
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsStatic
import kotlin.js.JsStatic
import kotlin.jvm.JvmStatic
import kotlin.time.Clock

/**
 * 系统用户
 *
 * @param id 用户id
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
                VoValidatorMessage(name, VoValidatorMessage.CodeNotNull)
            }

            ensure(vo.name.length in 1..15) {
                VoValidatorMessage(name.name, VoValidatorMessage.CodeOutOfRange, "长度1-15")
            }

            ensure(!vo.password.isNullOrEmpty()) { VoValidatorMessage(password, VoValidatorMessage.CodeNotNull) }

            if (vo.birthday != null) {
                val now = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
                ensure(vo.birthday in LocalDate(1900, 1, 1)..now) {
                    VoValidatorMessage(birthday.name, VoValidatorMessage.CodeOutOfRange, "生日范围超过1900到今天")
                }
            }

            if (!vo.email.isNullOrEmpty()) {
                ensure(vo.email matches RegexValidatorEmail) { VoValidatorMessage(birthday.name, "格式错误", "邮箱") }
            }

            vo
        }


        @JvmStatic
        @JsStatic
        fun validPut(vo: UserVO): Either<VoValidatorMessage, UserVO> = either {
            ensure(vo.id > 0) { VoValidatorMessage(id, VoValidatorMessage.CodeNotNull) }
            valid(vo).bind()
        }

    }
}
