package cn.allin.model

import kotlinx.datetime.LocalDateTime
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.GenerationType
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Table


@Entity
@Table(name = "auto_answer_record")
interface AutoAnswerRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long
    val qaId: Int
    val msgRecordId: Long
    val createTime: LocalDateTime
}