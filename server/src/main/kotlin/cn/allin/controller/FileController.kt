package cn.allin.controller

import arrow.core.Either
import arrow.core.raise.either
import cn.allin.api.ApiFile
import cn.allin.api.ApiFile.Companion.UPLOAD_URL
import cn.allin.service.FileService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiFile.FILE)
class FileController(
    private val fileService: FileService
): ApiFile {

    @GetMapping(UPLOAD_URL)
    override suspend fun uploadUrl(@RequestParam filePath: String): Either<String, String> {
        return either {
            try {
                fileService.createUpUrl(filePath)
            } catch (e: Exception){
                raise(e.toString())
            }
        }
    }


}
