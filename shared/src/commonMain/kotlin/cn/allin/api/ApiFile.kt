package cn.allin.api

import arrow.core.Either

interface ApiFile {

    suspend fun uploadUrl(filePath: String): Either<String,String>

    companion object {
        const val FILE = "file"
        const val UPLOAD_URL = "uploadUrl"
    }
}
