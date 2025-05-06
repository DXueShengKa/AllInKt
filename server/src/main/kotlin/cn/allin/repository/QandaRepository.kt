package cn.allin.repository

import cn.allin.model.AutoAnswerRecordEntity
import cn.allin.model.QAndAEntity
import cn.allin.model.QaTagEntity
import cn.allin.model.addBy
import cn.allin.model.answer
import cn.allin.model.by
import cn.allin.model.fetchBy
import cn.allin.model.id
import cn.allin.model.qaId
import cn.allin.model.question
import cn.allin.model.tags
import cn.allin.utils.substring
import cn.allin.utils.toPageVO
import cn.allin.utils.toQandaVO
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import cn.allin.vo.QandaVO
import kotlinx.datetime.toKotlinLocalDateTime
import org.babyfish.jimmer.sql.ast.mutation.AssociatedSaveMode
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.asc
import org.babyfish.jimmer.sql.kt.ast.expression.desc
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.`eq?`
import org.babyfish.jimmer.sql.kt.ast.expression.exists
import org.babyfish.jimmer.sql.kt.ast.expression.like
import org.babyfish.jimmer.sql.kt.ast.expression.not
import org.babyfish.jimmer.sql.kt.ast.expression.valueIn
import org.babyfish.jimmer.sql.kt.exists
import org.babyfish.jimmer.sql.kt.fetcher.newFetcher
import org.springframework.stereotype.Repository
import java.util.stream.Stream

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


    fun find(qaId: Int): QandaVO {
        return sqlClient.findOneById(
            newFetcher(QAndAEntity::class).by {
                allScalarFields()
                tags { tagName() }
            },
            qaId
        ).run {
            QandaVO(
                id,
                question,
                answer,
                createTime.toKotlinLocalDateTime(),
                tags.takeIf { it.isNotEmpty() }?.map { tag -> QaTagVO(tag.id, tag.tagName) },
            )
        }
    }

    fun findPage(pageIndex: Int, size: Int, isAsc: Boolean, tagId: Int?): PageVO<QandaVO> {
        return sqlClient.createQuery(QAndAEntity::class) {
            where(table.tags {
                id `eq?` tagId
            })
            orderBy(if (isAsc) table.id.asc() else table.id.desc())
            select(
                table.fetchBy {
                    allScalarFields()
                    tags {
                        tagName()
                    }
                },
                table.answer substring 1..10,
            )
        }
            .fetchPage(pageIndex, size)
            .toPageVO { (qa, a) ->
                QandaVO(
                    qa.id,
                    qa.question,
                    a,
                    qa.createTime.toKotlinLocalDateTime(),
                    qa.tags.takeIf { it.isNotEmpty() }?.map { tag -> QaTagVO(tagName = tag.tagName) },
                )
            }
    }

    fun add(vo: QandaVO): Int {
        return sqlClient.saveCommand(
            QAndAEntity {
                question = vo.question
                answer = vo.answer
                vo.tagList?.map {
                    QaTagEntity {
                        id = it.id
                    }
                }?.also {
                    tags = it
                }

            }, SaveMode.INSERT_ONLY
        ).execute().modifiedEntity.id
    }


    fun delete(deleteId: Int): Boolean {
        return sqlClient.createDelete(QAndAEntity::class) {
            where(
                table.id eq deleteId,
                exists(subQuery(AutoAnswerRecordEntity::class) {
                    where(table.qaId eq deleteId)
                    select(table.id)
                }).not()
            )
        }.execute() > 0
    }

    fun delete(ids: List<Int>): Int {

        if (
            sqlClient.exists(AutoAnswerRecordEntity::class) {
                where(table.qaId valueIn ids)
            }
        ) return -1

        return sqlClient.deleteByIds(QAndAEntity::class, ids).totalAffectedRowCount
    }

    fun add(qaList: List<QandaVO>): Int {
        val tagEntity = qaList.stream().flatMap {
            it.tagList?.stream() ?: Stream.empty()
        }
            .distinct()
            .map {
                QaTagEntity {
                    tagName = it.tagName
                }
            }
            .toList()

        sqlClient.saveEntitiesCommand(tagEntity, SaveMode.INSERT_IF_ABSENT).execute()

        val dbTags = sqlClient.findAll(newFetcher(QaTagEntity::class).by {
            tagName()
        })

        return sqlClient.saveEntitiesCommand(
            qaList.map { qa ->
                QAndAEntity {
                    question = qa.question
                    answer = qa.answer

                    qa.tagList?.forEach { t ->
                        val dbTagId = dbTags.find { it.tagName == t.tagName }?.id
                        if (dbTagId != null) {
                            tags().addBy {
                                id = dbTagId
                            }
                        }
                    }
                }
            },
            SaveMode.INSERT_ONLY
        )
            .execute().totalAffectedRowCount
    }

    fun update(qanda: QandaVO) {
        sqlClient.save(QAndAEntity {
            id = qanda.id
            question = qanda.question
            answer = qanda.answer
            qanda.tagList?.forEach {
                tags().addBy {
                    id = it.id
                }
            }
        }, SaveMode.UPDATE_ONLY, AssociatedSaveMode.REPLACE)
    }

}
