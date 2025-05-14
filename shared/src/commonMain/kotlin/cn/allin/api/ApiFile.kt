package cn.allin.api

import arrow.core.Either
import kotlin.jvm.JvmStatic

interface ApiFile {

    suspend fun uploadUrl(filePath: String): Either<String, String>

    suspend fun list(path: String?): List<String>

    companion object {
        const val FILE = "file"
        const val UPLOAD_URL = "uploadUrl"
        const val LIST = "list"

        @JvmStatic
        fun pathList() = "$FILE/$LIST"
    }
}
