package cn.allin.controller

import cn.allin.ServerParams
import cn.allin.apiRoute
import cn.allin.service.QandaService
import cn.allin.vo.MsgVO
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import cn.allin.vo.QandaVO
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = [apiRoute.qanda.QANDA])
class QandaController(
    val qandaService: QandaService
) {

    @GetMapping(apiRoute.PAGE)
    fun page(
        @RequestParam(ServerParams.PAGE_INDEX) pageIndex: Int?,
        @RequestParam(ServerParams.PAGE_SIZE) pageSize: Int?
    ): PageVO<QandaVO> {
        return qandaService.page(pageIndex ?: 0, pageSize ?: 10)
    }

    @PostMapping
    fun post(
        @Validated @RequestBody qandaVO: QandaVO,
    ): MsgVO<Int> {
        return MsgVO.success(qandaService.add(qandaVO))
    }

    @DeleteMapping(apiRoute.PATH_ID)
    fun delete(@PathVariable id: Int): MsgVO<String> {
        qandaService.delete(id)
        return MsgVO.success(MsgVO.delete)
    }

    @GetMapping(apiRoute.qanda.tag.page.TAG_PAGE)
    fun tagPage(
        @RequestParam(ServerParams.PAGE_INDEX) pageIndex: Int?,
        @RequestParam(ServerParams.PAGE_SIZE) pageSize: Int?
    ):PageVO<QaTagVO>{
        return qandaService.tagPage(pageIndex ?: 0, pageSize ?: 10)
    }
}
