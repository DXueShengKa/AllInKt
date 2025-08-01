package cn.allin.model

import cn.allin.ksp.server.EntityToVo
import cn.allin.vo.QandaVO
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.GenerationType
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.JoinTable
import org.babyfish.jimmer.sql.Key
import org.babyfish.jimmer.sql.ManyToMany
import org.babyfish.jimmer.sql.Table
import java.time.LocalDateTime


@EntityToVo([QandaVO::class])
@Entity
@Table(name = "q_and_a")
interface QAndAEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int

    @Key
    val question: String
    val answer: String
    val createTime: LocalDateTime

    val updateTime: LocalDateTime

    @ManyToMany
    @JoinTable(
        name = "qa_tag_relation",
        joinColumnName = "qa_id",
        inverseJoinColumnName = "tag_id"
    )
    val tags:List<QaTagEntity>
}
