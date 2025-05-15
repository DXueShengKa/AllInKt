package cn.allin.controller

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import cn.allin.api.ApiFile
import cn.allin.service.FileService
import cn.allin.vo.FilePathVO
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiFile.FILE)
class FileController(
    private val fileService: FileService
) : ApiFile {

    @GetMapping(ApiFile.UPLOAD_URL)
    override suspend fun uploadUrl(@RequestParam filePath: String): Either<String, String> {
        return either {
            try {
                fileService.createUpUrl(
                    filePath.substringBefore('/'),
                    filePath.substringAfter('/')
                )
            } catch (e: Exception) {
                raise(e.toString())
            }
        }
    }

    @GetMapping(value = [ApiFile.LIST + "/{pathId}", ApiFile.LIST])
    override suspend fun list(@PathVariable(required = false) pathId: Int?): FilePathVO {
        return fileService.listDir(pathId)
    }

    override suspend fun delete(pathId: Int) {
        TODO("Not yet implemented")
    }


    @PostMapping(ApiFile.NEW_DIR)
    override suspend fun newDir(@Validated @RequestBody pathVO: FilePathVO): Either<String, Int> {
        return fileService.addPath(pathVO.parentId ?: return "parentId 不能为空".left(), pathVO.path).right()
    }


}
