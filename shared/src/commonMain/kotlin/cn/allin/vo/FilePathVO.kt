package cn.allin.vo

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import cn.allin.VoValidatorMessage
import cn.allin.path
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable


/**
 *
 * @param path 路径
 * @param parentId 父路径id
 *
 */
@Serializable
data class FilePathVO(
    val id: Int,
    val path: String,
    val parentId: Int? = null,
    val childs: List<FilePathVO>? = null,
    val fileList: List<FileVO>? = null,
    val createTime: LocalDateTime? = null,
) {
    companion object {
        fun valid(vo: FilePathVO): Either<VoValidatorMessage, FilePathVO> = either {
            ensure(vo.path.isNotEmpty()) {
                VoValidatorMessage(path, VoValidatorMessage.CodeNotNull)
            }
            vo
        }
    }
}
