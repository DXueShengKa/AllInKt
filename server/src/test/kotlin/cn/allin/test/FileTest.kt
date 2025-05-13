package cn.allin.test

import cn.allin.Application
import cn.allin.service.FileService
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
        val b = fileService.s3Client().createBucket {
            it.bucket("archive")
        }
        println(b.location())
    }


    @Test
    fun showBuckets(){
        fileService.s3Client()
            .listBuckets()
            .buckets()
            .forEach { bucket ->
                println("桶 ${bucket.name()}")
            }
    }

    @Test
    fun showObj(){
        fileService.s3Client()
            .listObjects {
                it.bucket("myzip")
            }
            .contents()
            .forEach {
                println(it)
            }
    }
}
