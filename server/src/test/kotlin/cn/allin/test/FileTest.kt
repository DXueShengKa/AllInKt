package cn.allin.test

import cn.allin.Application
import cn.allin.service.FileService
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.Test


@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [Application::class]
)
class FileTest {

    @Autowired
    lateinit var fileService: FileService

    @Test
    fun createBucket() {
        runBlocking {
            val b = fileService.s3Client().createBucket {
                it.bucket("archive")
            }.await()
            println(b.location())
        }
    }


    @Test
    fun showBuckets(){

        runBlocking {
            fileService.s3Client()
                .listBuckets()
                .await()
                .buckets()
                .forEach { bucket ->
                    println("桶 ${bucket.name()}")
                }
        }
    }

    @Test
    fun showObj(){
        runBlocking {
            println()
            fileService.listDir("")
                .forEach { s ->
                    println(s)
                }
        }
    }
}
