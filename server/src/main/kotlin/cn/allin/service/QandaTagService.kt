package cn.allin.service

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cn.allin.model.QaTagEntity
import cn.allin.model.by
import cn.allin.repository.QandaTagRepository
import cn.allin.utils.toEntity
import cn.allin.utils.toPageVO
import cn.allin.utils.toQaTagVO
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.stereotype.Service

@Service
class QandaTagService(
    private val qandaTagRepository: QandaTagRepository,
) {

    fun findTagPage(index: Int, size: Int): PageVO<QaTagVO> {
        return qandaTagRepository.findAll(pageIndex = index, pageSize = size){
            asc(QaTagEntity::id)
        }.toPageVO {
            it.toQaTagVO()
        }
    }

    fun addTag(tag: QaTagVO) {
        qandaTagRepository.save(tag.toEntity(), SaveMode.INSERT_ONLY)
    }


    fun deleteTag(tagId: Int): Either<String, Unit> {

        return if (qandaTagRepository.deleteTag(tagId) > 0) {
            Unit.right()
        } else {
            "这个标签已经绑定问题".left()
        }
    }

    fun findAllTag(): List<QaTagVO> {
        return qandaTagRepository.findAll(newFetcher(QaTagEntity::class).by { tagName() }).map {
            QaTagVO(it.id, it.tagName)
        }
    }

    fun getTag(id: Int): QaTagVO {
        return qandaTagRepository.findById(id).get().toQaTagVO()
    }

    fun update(tag: QaTagVO) {
        qandaTagRepository.save(tag.toEntity(), SaveMode.UPDATE_ONLY)
    }
}
