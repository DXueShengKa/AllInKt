package cn.allin.data.repository

import arrow.core.Either
import cn.allin.api.ApiFile
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.*
import org.koin.core.annotation.Factory

@Factory([ApiFile::class])
class FileRepository(
    private val httpClient: HttpClient,
) : ApiFile {

    override suspend fun uploadUrl(filePath: String): Either<String, String> {
        return httpClient.get(ApiFile.UPLOAD_URL) {
            parameter("filePath", filePath)
        }.body()
    }

    override suspend fun list(path: String?): List<String> {
       return httpClient.get(ApiFile.pathList()){
            parameter("path", path)
        }.body()
    }

    suspend fun upload(url: String, channel: ByteReadChannel): String? {
        return httpClient.put(url) {
            setBody(channel)
        }.headers[HttpHeaders.ETag]
    }
}
