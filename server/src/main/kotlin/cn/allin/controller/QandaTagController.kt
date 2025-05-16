package cn.allin.controller

import arrow.core.Either
import cn.allin.ServerParams
import cn.allin.api.ApiQandaTag
import cn.allin.apiRoute
import cn.allin.service.QandaTagService
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping(value = [ApiQandaTag.QA_TAG])
class QandaTagController(
    private val qandaTagService: QandaTagService
):ApiQandaTag {

    @GetMapping(apiRoute.PAGE)
    override suspend fun page(
        @RequestParam(ServerParams.PAGE_INDEX) pageIndex: Int?,
        @RequestParam(ServerParams.PAGE_SIZE) pageSize: Int?
    ): PageVO<QaTagVO> {
        return qandaTagService.findTagPage(pageIndex?:0, pageSize?:10)
    }

    @GetMapping(ApiQandaTag.ALL)
    override suspend fun getAll(): List<QaTagVO> {
        return qandaTagService.findAllTag()
    }

    @GetMapping(apiRoute.PATH_ID)
    override suspend fun get(@PathVariable id: Int): QaTagVO {
        return qandaTagService.getTag(id)
    }

    @PostMapping
    override suspend fun add(@RequestBody tag: QaTagVO) {
        qandaTagService.addTag(tag)
    }

    @PutMapping
    override suspend fun update(@RequestBody tag: QaTagVO) {
        qandaTagService.update(tag)
    }

    @DeleteMapping(apiRoute.PATH_ID)
    override suspend fun delete(@PathVariable id: Int): Either<String, Unit> {
        return qandaTagService.deleteTag(id)
    }

}

