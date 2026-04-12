package cn.allin.table

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("file_path")
data class FilePath(
    @Id
    val id: Int? = null,
    val parentId: Int?,
    val path: String,
    val createTime: LocalDateTime = LocalDateTime.now()
)
