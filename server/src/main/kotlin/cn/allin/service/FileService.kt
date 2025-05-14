package cn.allin.service

import kotlinx.coroutines.future.await
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaTypeFactory
import org.springframework.stereotype.Service
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URI
import java.time.Duration
import java.util.stream.Stream
import kotlin.jvm.optionals.getOrNull

@Service
class FileService() {

    companion object {
        const val archive = "archive"
    }

    @Value("\${aws.endpoint}")
    private lateinit var endpoint: String

    @Value("\${aws.accessKey}")
    private lateinit var accessKey: String

    @Value("\${aws.secretKey}")
    private lateinit var secretKey: String

    @Value("\${aws.region}")
    private lateinit var region: String

    private var _s3: S3AsyncClient? = null

    fun s3Client(): S3AsyncClient {
        return _s3 ?: S3AsyncClient.builder()
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .region(Region.of(region))
            .build()
            .also { _s3 = it }
    }

    private var _presigner: S3Presigner? = null

    private fun preSigner(): S3Presigner {
        return _presigner ?: S3Presigner.builder()
            .endpointOverride(URI.create(endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
            .region(Region.of(region))
            .build()
            .also { _presigner = it }
    }


    suspend fun listDir(path: String): List<String> {
        val r = s3Client()
            .listObjectsV2 {
                it.bucket(archive)
                    .prefix(if (path.endsWith("/")) path else "$path/")
                    .delimiter("/")
            }
            .await()

        return Stream.concat(
            r.contents().stream().map {
                it.key()
            },
            r.commonPrefixes().stream().map {
                it.prefix()
            }
        ).toList()
    }


    fun createUpUrl(filePath: String): String {
        val contentType = if (filePath.endsWith(".zip", true)) {
            "application/zip"
        } else MediaTypeFactory.getMediaType(filePath.substringAfter("/")).map {
            it.toString()
        }.getOrNull()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .putObjectRequest {
                it.bucket(archive)
                    .key(filePath)
                if (!contentType.isNullOrEmpty())
                    it.contentType(contentType)
            }
            .build()

        return preSigner().presignPutObject(presignRequest).url().toString()
    }


    fun downloadUrl(filePath: String): String {
       return preSigner().presignGetObject {
            it.signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest { request ->
                    request.bucket(archive)
                        .key(filePath)
                }
        }.url().toString()
    }


    suspend fun objInfo(filePath: String): String {
       val tagging = s3Client().getObjectTagging {
            it.bucket(archive)
                .key(filePath)
        }

        return s3Client().headObject {
            it.bucket(archive)
            .key(filePath)
        }.thenCombine(tagging){ metadata,tags ->
            metadata.metadata()
            metadata.contentType()
            ""
        }.await()

    }

}
