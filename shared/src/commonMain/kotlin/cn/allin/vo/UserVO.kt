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
 * 用户信息
 *
 */
@Serializable
data class UserVO(
    /**
     * 用户ID
     */
    val id: Long = 0,
    /**
     * 用户名
     */
    val name: String? = null,
    /**
     * 邮箱地址
     */
    val email: String? = null,
    /**
     * 密码
     */
    val password: String? = null,
    /**
     * 生日
     */
    val birthday: LocalDate? = null,
    /**
     * 角色
     */
    val role: String? = null,
    /**
     * 地址
     */
    val address: String? = null,
    /**
     * 性别
     */
    val gender: Gender? = null,
) {
    /**
     * 用户数据验证伴生对象
     * 提供静态方法用于验证用户数据的合法性
     */
    @OptIn(ExperimentalJsStatic::class)
    companion object {
        /**
         * 验证用户基本信息
         * 验证规则:
         * - 用户名不能为空且长度在1-15之间
         * - 密码不能为空
         * - 生日范围需在1900年至今之间(如果提供)
         * - 邮箱格式需符合正则表达式(如果提供)
         *
         * @param vo 待验证的用户数据
         * @return Either<VoValidatorMessage, UserVO> 验证成功返回用户数据,失败返回错误信息
         */
        @JvmStatic
        @JsStatic
        fun valid(vo: UserVO): Either<VoValidatorMessage, UserVO> =
            either {
                ensureNotNull(vo.name) {
                    VoValidatorMessage(name, VoValidatorMessage.CodeNotNull)
                }

                ensure(vo.name.length in 1..15) {
                    VoValidatorMessage(name.name, VoValidatorMessage.CodeOutOfRange, "长度1-15")
                }

                ensure(!vo.password.isNullOrEmpty()) { VoValidatorMessage(password, VoValidatorMessage.CodeNotNull) }

                if (vo.birthday != null) {
                    val now =
                        Clock.System
                            .now()
                            .toLocalDateTime(TimeZone.UTC)
                            .date
                    ensure(vo.birthday in LocalDate(1900, 1, 1)..now) {
                        VoValidatorMessage(birthday.name, VoValidatorMessage.CodeOutOfRange, "生日范围超过1900到今天")
                    }
                }

                if (!vo.email.isNullOrEmpty()) {
                    ensure(vo.email matches RegexValidatorEmail) { VoValidatorMessage(birthday.name, "格式错误", "邮箱") }
                }

                vo
            }

        /**
         * 验证用户更新数据
         * 在 valid 基础上增加 ID 必须大于 0 的要求
         *
         * @param vo 待验证的用户数据
         * @return Either<VoValidatorMessage, UserVO> 验证成功返回用户数据,失败返回错误信息
         */
        @JvmStatic
        @JsStatic
        fun validPut(vo: UserVO): Either<VoValidatorMessage, UserVO> =
            either {
                ensure(vo.id > 0) { VoValidatorMessage(id, VoValidatorMessage.CodeNotNull) }
                valid(vo).bind()
            }
    }
}
