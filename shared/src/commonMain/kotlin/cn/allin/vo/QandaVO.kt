package cn.allin.vo

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import cn.allin.VoFieldName
import cn.allin.VoValidatorMessage
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * 问答
 * @param question 问题
 * @param answer 回答
 * @param createTime 创建时间
 * @param tagList 标签列表
 */
@Serializable
data class QandaVO(
    val id: Int = 0,
    val question: String,
    val answer: String,
    val createTime: LocalDateTime? = null,
    val tagList: List<QaTagVO>? = null,
) {
    companion object {
        fun valid(vo: QandaVO): Either<VoValidatorMessage, QandaVO> = either {

            ensure(vo.question.isNotEmpty()) {
                VoValidatorMessage(VoFieldName.QandaVO_question, VoValidatorMessage.CodeNotNull, "问题")
            }

            ensure(vo.answer.isNotEmpty()) {
                VoValidatorMessage(VoFieldName.QandaVO_answer, VoValidatorMessage.CodeNotNull, "回答")
            }
            vo
        }
    }
}
