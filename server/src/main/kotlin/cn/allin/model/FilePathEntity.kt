package cn.allin.model

import org.babyfish.jimmer.sql.Entity
import org.babyfish.jimmer.sql.GeneratedValue
import org.babyfish.jimmer.sql.GenerationType
import org.babyfish.jimmer.sql.Id
import org.babyfish.jimmer.sql.IdView
import org.babyfish.jimmer.sql.ManyToOne
import org.babyfish.jimmer.sql.OneToMany
import org.babyfish.jimmer.sql.Table
import java.time.LocalDateTime

@Entity
@Table(name = "file_path")
interface FilePathEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int

    @IdView
    val parentId: Int?

    val path: String

    val createTime: LocalDateTime

    @ManyToOne
    val parent:FilePathEntity?


    @OneToMany(mappedBy = "parent")
    val childList:List<FilePathEntity>
}
