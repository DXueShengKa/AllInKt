package cn.allin.repository

import cn.allin.model.QAndAEntity
import cn.allin.model.QaTagEntity
import cn.allin.model.fetchBy
import cn.allin.model.question
import cn.allin.utils.toPageVO
import cn.allin.utils.toQaTagVO
import cn.allin.utils.toQandaVO
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import cn.allin.vo.QandaVO
import kotlinx.datetime.toKotlinLocalDateTime
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.like
import org.springframework.stereotype.Repository

@Repository
class QandaRepository(private val sqlClient: KSqlClient) {

    fun findAnswer(quest: String): List<QandaVO> {
        return sqlClient.executeQuery(QAndAEntity::class) {
            where(table.question like quest)
            select(table)
        }.map {
            it.toQandaVO()
        }
    }


    fun findPage(pageIndex: Int, size: Int): PageVO<QandaVO> {
        return sqlClient.createQuery(QAndAEntity::class) {
            select(table.fetchBy {
                allScalarFields()
                tags {
                    tagName()
                }
            })
        }
            .fetchPage(pageIndex, size)
            .toPageVO {
                QandaVO(
                    it.id,
                    it.question,
                    it.answer,
                    it.createTime.toKotlinLocalDateTime(),
                    it.tags.takeIf { it.isNotEmpty() }?.map { tag -> tag.tagName },
                )
            }
    }

    fun add(vo: QandaVO): Int {
        return sqlClient.saveCommand(
            QAndAEntity {
                question = vo.question
                answer = vo.answer
            }, SaveMode.INSERT_ONLY
        ).execute().modifiedEntity.id
    }


    fun delete(id: Int) {
        sqlClient.deleteById(QAndAEntity::class, id)
    }

    fun findTagPage(index: Int, size: Int): PageVO<QaTagVO> {
        return sqlClient.createQuery(QaTagEntity::class) {
            select(table)
        }.fetchPage(index, size)
            .toPageVO { it.toQaTagVO() }
    }
}
