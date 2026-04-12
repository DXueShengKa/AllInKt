package cn.allin.table

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

// 文件对象实体
@Table("file_object")
data class FileObject(
    @Id
    val id: Long? = null,
    val pathId: Int,
    val name: String,
    val size: Long,
    val mimeType: String,
    val md5: String,
    val metadata: Map<String, Any>? = null,
    val updateTime: LocalDateTime = LocalDateTime.now(),
    val createTime: LocalDateTime = LocalDateTime.now()
)
