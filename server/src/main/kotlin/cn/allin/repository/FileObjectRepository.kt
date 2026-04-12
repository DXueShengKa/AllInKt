package cn.allin.repository

import cn.allin.table.FileObject
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

// 文件对象仓库
@Repository
interface FileObjectRepository : CoroutineCrudRepository<FileObject, Long> {
    suspend fun findByPathId(pathId: Int): List<FileObject>
}
