package cn.allin.repository

import cn.allin.table.FilePath
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

// 文件路径仓库
@Repository
interface FilePathRepository : CoroutineCrudRepository<FilePath, Int> {
    suspend fun findByParentId(parentId: Int?): List<FilePath>

    @Query(
        """
        WITH RECURSIVE path_tree AS (
            SELECT id, parent_id, path, create_time
            FROM file_path
            WHERE id = :pathId
            UNION ALL
            SELECT fp.id, fp.parent_id, fp.path, fp.create_time
            FROM file_path fp
            JOIN path_tree pt ON fp.parent_id = pt.id
        )
        DELETE FROM file_path
        USING path_tree
        WHERE file_path.id = path_tree.id;
        """,
    )
    suspend fun deletePathAndChildren(pathId: Int): Int
}
