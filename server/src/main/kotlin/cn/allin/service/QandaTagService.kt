package cn.allin.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cn.allin.repository.QandaTagRepository
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import org.springframework.stereotype.Service

@Service
class QandaTagService(
    private val qandaTagRepository: QandaTagRepository,
) {


    fun findTagPage(index: Int?, size: Int?): PageVO<QaTagVO> {
        return qandaTagRepository.findTagPage(index ?: 0, size ?: 10)
    }

    fun addTag(tag: QaTagVO) {
        qandaTagRepository.addTag(tag)
    }


    fun deleteTag(tagId: Int): Either<String, Unit> {

        return if (qandaTagRepository.deleteTag(tagId) > 0) {
            Unit.right()
        } else {
            "这个标签已经绑定问题".left()
        }
    }

    fun findAllTag(): List<QaTagVO> {
        return qandaTagRepository.findAllTag().map {
            QaTagVO(it.id,it.tagName)
        }
    }
}
