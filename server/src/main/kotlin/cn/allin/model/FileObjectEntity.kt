package cn.allin.model

import cn.allin.ksp.server.EntityToVo
import cn.allin.vo.FileVO
import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.GenerationType
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.Table
import java.time.LocalDateTime

@EntityToVo([FileVO::class])
@Entity
@Table(name = "file_object")
interface FileObjectEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long

    val name: String

    val size: Long

    val mimeType: String

    val md5: String

    val metadata: String?

    val pathId: Int

    val updateTime: LocalDateTime

}
