package cn.allin.model

import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.GenerationType
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Table
import java.time.LocalDateTime


//@EntityToVo([QaTagVO::class])
@Entity
@Table(name = "qa_tag")
interface QaTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int
    val tagName: String
    val description: String?
    val createTime: LocalDateTime
}