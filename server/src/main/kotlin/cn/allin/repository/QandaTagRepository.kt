package cn.allin.repository

import cn.allin.model.QaTagEntity
import cn.allin.model.by
import cn.allin.model.id
import cn.allin.utils.toPageVO
import cn.allin.utils.toQaTagVO
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.asc
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.exists
import org.babyfish.jimmer.sql.kt.ast.table.source
import org.babyfish.jimmer.sql.kt.ast.table.sourceId
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.stereotype.Repository

@Repository
class QandaTagRepository(
    private val sqlClient: KSqlClient
) {
    fun findTagPage(index: Int, size: Int): PageVO<QaTagVO> {
        return sqlClient.createQuery(QaTagEntity::class) {
            orderBy(table.id.asc())
            select(table)
        }.fetchPage(index, size)
            .toPageVO { it.toQaTagVO() }
    }

    fun addTag(tag: QaTagVO) {
        sqlClient.save(QaTagEntity {
            tagName = tag.tagName
            description = tag.description
        }, SaveMode.INSERT_ONLY)
    }

    fun bindQa(tagId: Int): Boolean {
        return sqlClient.queries
            .forList(QaTagEntity::qaList){
                where(table.sourceId eq tagId)
                select(table.sourceId)
            }.exists()
    }

    fun deleteTag(tagId: Int): Int {
       return sqlClient.createDelete(QaTagEntity::class){
            where(table.id eq tagId, exists(subQueries.forReference(QaTagEntity::qaList){
                where(table.source.id eq tagId)
                select(table.source.id)
            }))
        }.execute()
    }

    fun findAllTag(): List<QaTagEntity> {
        return sqlClient.findAll(newFetcher(QaTagEntity::class).by {
            tagName()
        })
    }

    fun findTag(id: Int): QaTagVO {
      return  sqlClient.findOneById(QaTagEntity::class,id).toQaTagVO()
    }


    fun update(tag: QaTagVO) {
        sqlClient.save(QaTagEntity {
            id = tag.id
            tagName = tag.tagName
            description = tag.description
        }, SaveMode.UPDATE_ONLY)
    }
}
