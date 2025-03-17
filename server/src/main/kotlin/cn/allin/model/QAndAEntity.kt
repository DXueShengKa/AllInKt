package cn.allin.model

import org.babyfish.jimmer.sql.Column
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.GenerationType
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Table
import java.time.LocalDateTime


//@EntityToVo([RegionVO::class])
@Entity
@Table(name = "q_and_a")
interface QAndAEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int
    val question: String
    val answer: String
    @Column(name = "tagids", sqlElementType = "integer")
    val tagIds: IntArray?
    val createTime: LocalDateTime
}