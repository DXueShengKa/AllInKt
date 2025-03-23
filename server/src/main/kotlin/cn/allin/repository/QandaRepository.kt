package cn.allin.repository

import cn.allin.model.QAndAEntity
import cn.allin.model.question
import cn.allin.utils.toPageVO
import cn.allin.utils.toQandaVO
import cn.allin.vo.PageVO
import cn.allin.vo.QandaVO
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.like

class QandaRepository(private val sqlClient: KSqlClient){

    fun findAnswer(quest: String): List<QandaVO> {
        return sqlClient.executeQuery(QAndAEntity::class){
            where(table.question like quest)
            select(table)
        }.map {
            it.toQandaVO()
        }
    }


    fun findPage(pageIndex: Int, size: Int): PageVO<QandaVO> {
        return sqlClient.createQuery(QAndAEntity::class){
            select(table)
        }.fetchPage(pageIndex, size)
            .toPageVO { it.toQandaVO() }
    }


}