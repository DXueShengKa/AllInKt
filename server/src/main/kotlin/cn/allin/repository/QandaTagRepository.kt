package cn.allin.repository

import cn.allin.model.QaTagEntity
import cn.allin.model.id
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.eq
import org.babyfish.jimmer.sql.kt.ast.expression.exists
import org.babyfish.jimmer.sql.kt.ast.table.source
import org.babyfish.jimmer.sql.kt.ast.table.sourceId

interface QandaTagRepository: KRepository<QaTagEntity, Int> {




    fun bindQa(tagId: Int): Boolean {
        return sql.queries
            .forList(QaTagEntity::qaList){
                where(table.sourceId eq tagId)
                select(table.sourceId)
            }.exists()
    }

    fun deleteTag(tagId: Int): Int {
       return sql.createDelete(QaTagEntity::class){
            where(table.id eq tagId, exists(subQueries.forReference(QaTagEntity::qaList){
                where(table.source.id eq tagId)
                select(table.source.id)
            }))
        }.execute()
    }

}
