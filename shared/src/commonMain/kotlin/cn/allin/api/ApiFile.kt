package cn.allin.api

import arrow.core.Either
import cn.allin.vo.FilePathVO
import kotlin.jvm.JvmStatic

interface ApiFile {

    suspend fun uploadUrl(filePath: String): Either<String, String>

    suspend fun list(pathId: Int?): FilePathVO

    suspend fun delete(pathId: Int)

    suspend fun newDir(pathVO: FilePathVO): Either<String, Int>


    companion object {
        const val FILE = "file"
        const val UPLOAD_URL = "uploadUrl"
        const val LIST = "list"

        const val NEW_DIR = "newDir"

        @JvmStatic
        fun pathList(pathId: Int?) = if (pathId == null) "$FILE/$LIST" else "$FILE/$LIST/$pathId"

        @JvmStatic
        fun pathNewDir() = "$FILE/$NEW_DIR"

    }
}
