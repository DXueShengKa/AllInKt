package cn.allin.repository

import cn.allin.model.FileObjectEntity
import cn.allin.model.pathId
import org.babyfish.jimmer.spring.repository.KRepository
import org.babyfish.jimmer.sql.kt.ast.expression.valueIn

interface FileObjectRepository : KRepository<FileObjectEntity, Long> {
    fun findMapByPathIds(pathIds: Collection<Int>) = sql.createQuery(FileObjectEntity::class) {
        groupBy(table.pathId)
        where(table.pathId valueIn pathIds)
        select(table.pathId, table)
    }.execute()


    fun findAllByPathId(pathId: Int): List<FileObjectEntity>

}
