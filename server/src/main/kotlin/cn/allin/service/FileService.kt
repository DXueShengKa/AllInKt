package cn.allin.service

import cn.allin.repository.FileObjectRepository
import cn.allin.repository.FilePathRepository
import cn.allin.table.FilePath
import cn.allin.vo.FilePathVO
import kotlinx.coroutines.future.await
import org.springframework.http.MediaTypeFactory
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

@Service
class FileService(
    private val s3Client: S3AsyncClient,
    private val s3PreSigner: S3Presigner,
    private val filePathRepository: FilePathRepository,
    private val fileObjectRepository: FileObjectRepository,
) {
    suspend fun listDir(pathId: Int?): List<FilePathVO> {
        val filePaths = filePathRepository.findByParentId(pathId)
        return filePaths.map {
            FilePathVO(
                id = it.id ?: 0,
                path = it.path,
            )
        }
    }

    suspend fun addPath(
        parentId: Int,
        path: String,
    ): Int {
        val filePath = FilePath(parentId = parentId, path = path)
        return filePathRepository.save(filePath).id!!
    }

    suspend fun listDir(
        bucket: String,
        path: String,
    ): List<String> {
        val r =
            s3Client
                .listObjectsV2 {
                    it
                        .bucket(bucket)
                        .prefix(if (path.endsWith("/")) path else "$path/")
                        .delimiter("/")
                }.await()

        return Stream
            .concat(
                r.contents().stream().map {
                    it.key()
                },
                r.commonPrefixes().stream().map {
                    it.prefix()
                },
            ).toList()
    }

    fun createUpUrl(
        bucket: String,
        filePath: String,
    ): String {
        val contentType =
            if (filePath.endsWith(".zip", true)) {
                "application/zip"
            } else {
                MediaTypeFactory
                    .getMediaType(filePath.substringAfter("/"))
                    .map {
                        it.toString()
                    }.getOrNull()
            }

        val presignRequest =
            PutObjectPresignRequest
                .builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest {
                    it
                        .bucket(bucket)
                        .key(filePath)
                    if (!contentType.isNullOrEmpty()) {
                        it.contentType(contentType)
                    }
                }.build()

        return s3PreSigner.presignPutObject(presignRequest).url().toString()
    }

    fun downloadUrl(
        bucket: String,
        filePath: String,
    ): String =
        s3PreSigner
            .presignGetObject {
                it
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest { request ->
                        request
                            .bucket(bucket)
                            .key(filePath)
                    }
            }.url()
            .toString()

    suspend fun objInfo(
        bucket: String,
        filePath: String,
    ): String {
        val tagging =
            s3Client.getObjectTagging {
                it
                    .bucket(bucket)
                    .key(filePath)
            }

        return s3Client
            .headObject {
                it
                    .bucket(bucket)
                    .key(filePath)
            }.thenCombine(tagging) { metadata, tags ->
                metadata.metadata()
                metadata.contentType()
            }.await()
    }

    suspend fun deleteAndChildren(pathId: Int): Int {
        return filePathRepository.deletePathAndChildren(pathId)
    }
}
