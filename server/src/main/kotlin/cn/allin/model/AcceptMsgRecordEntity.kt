package cn.allin.model

import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.GenerationType
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Table
import java.time.LocalDateTime


@Entity
@Table(name = "accept_msg_record")
interface AcceptMsgRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long
    val toUserName: String
    val fromUserName: String
    val msgType: String
    val event: String?
    val msgId: Long?
    val content: String?
    val picUrl: String?
    val mediaId: Long?
    val thumbMediaId: Long?
    val msgDataId: String?
    val idx:String?
    val createTime: LocalDateTime
}