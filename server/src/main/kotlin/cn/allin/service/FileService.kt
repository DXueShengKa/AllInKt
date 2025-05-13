package cn.allin.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URI
import java.time.Duration

@Service
class FileService() {

    companion object{
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

    private var _s3: S3Client? = null

    fun s3Client(): S3Client {
        return _s3 ?: S3Client.builder()
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


    @Throws(Exception::class)
    fun createUpUrl(filePath: String): String {
        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(10))
            .putObjectRequest {
                it.bucket(archive)
                    .key(filePath)
                    .contentType("application/zip")
            }
            .build()

        return preSigner().presignPutObject(presignRequest).url().toString()
    }


}
