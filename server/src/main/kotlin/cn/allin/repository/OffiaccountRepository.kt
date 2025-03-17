package cn.allin.repository

import cn.allin.model.AcceptMsgRecordEntity
import cn.allin.model.AutoAnswerRecordEntity
import cn.allin.model.QAndAEntity
import cn.allin.model.question
import cn.allin.vo.OffiAccoutMsgVO
import org.babyfish.jimmer.sql.ast.mutation.SaveMode
import org.babyfish.jimmer.sql.kt.KSqlClient
import org.babyfish.jimmer.sql.kt.ast.expression.`like?`
import org.springframework.stereotype.Repository
import java.time.Instant
import java.time.LocalDateTime

@Repository
class OffiaccountRepository(
    private val sqlClient: KSqlClient
) {

    fun acceptMsg(vo: OffiAccoutMsgVO): Long {
        return sqlClient.save(AcceptMsgRecordEntity {
            toUserName = vo.toUserName
            fromUserName = vo.fromUserName
            msgType = vo.msgType
            msgId = vo.msgId
            content = vo.content
            picUrl = vo.picUrl
            mediaId = vo.mediaId
            createTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(vo.createTime), java.time.ZoneId.systemDefault())
            idx = vo.idx
        }, SaveMode.INSERT_ONLY).modifiedEntity.id
    }

    fun findAnswer(question: String): List<QAndAEntity> {
        return sqlClient.createQuery(QAndAEntity::class) {
            where(table.question `like?` question)
            select(table)
        }.limit(1)
            .execute()
    }

    fun autoAnswer(qId: Int, msgId: Long) {
        sqlClient.save(AutoAnswerRecordEntity {
            qaId = qId
            msgRecordId = msgId
        }, SaveMode.INSERT_ONLY)
    }
}