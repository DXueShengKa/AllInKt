package cn.allin.controller

import cn.allin.ServerParams
import cn.allin.ServerRoute
import cn.allin.service.QandaService
import cn.allin.vo.PageVO
import cn.allin.vo.QandaVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = [ServerRoute.Qanda.ROUTE])
class QandaController(
    val qandaService: QandaService
) {

    @GetMapping(ServerRoute.PAGE)
    fun page(
        @RequestParam(ServerParams.PAGE_INDEX) pageIndex: Int?,
        @RequestParam(ServerParams.PAGE_SIZE) pageSize: Int?
    ): PageVO<QandaVO> {
        return qandaService.page(pageIndex ?: 0, pageSize ?: 10)
    }

    @PostMapping
    fun post(
        @RequestBody qandaVO: QandaVO,
    ): Int {
        return qandaService.add(qandaVO)
    }

}
