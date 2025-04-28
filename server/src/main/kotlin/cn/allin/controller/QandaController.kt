package cn.allin.controller

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import cn.allin.api.ApiQanda
import cn.allin.apiRoute
import cn.allin.service.QandaService
import cn.allin.vo.MsgVO
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import cn.allin.vo.QandaVO
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.LoggerFactory
import org.springframework.http.codec.multipart.FilePart
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import java.nio.file.Paths
import kotlin.io.path.deleteExisting
import kotlin.io.path.div

@RestController
@RequestMapping(value = [ApiQanda.QANDA])
class QandaController(
    val qandaService: QandaService
) : ApiQanda {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @GetMapping(apiRoute.PAGE)
    override suspend fun page(
        pageIndex: Int?, pageSize: Int?,isAsc: Boolean?, tagId: Int?
    ): PageVO<QandaVO> {
        return qandaService.page(pageIndex, pageSize,isAsc,tagId)
    }

    @GetMapping(apiRoute.PATH_ID)
    override suspend fun get(@PathVariable id: Int): QandaVO {
        return qandaService.get(id)
    }

    @PostMapping
    override suspend fun add(
        @Validated @RequestBody qandaVO: QandaVO,
    ): Int {
        return qandaService.add(qandaVO)
    }

    @PutMapping
    override suspend fun update(@Validated @RequestBody qanda: QandaVO) {
        qandaService.update(qanda)
    }


    @DeleteMapping(apiRoute.PATH_ID)
    override suspend fun delete(@PathVariable id: Int): Either<String, Unit> {
        return if (qandaService.delete(id)) {
            Unit.right()
        } else {
            "该问题已存在回复记录".left()
        }
    }


    @DeleteMapping
    override suspend fun delete(@RequestParam ids: List<Int>): Int {
        return qandaService.delete(ids)
    }


    @PostMapping(ApiQanda.EXCEL)
    suspend fun excel(@RequestPart("file") file: FilePart): MsgVO<String> {

        val tmpdir = Paths.get(System.getProperty("java.io.tmpdir"))
        val path = tmpdir / file.filename()

        file.transferTo(path).awaitFirstOrNull()

        val xssf = XSSFWorkbook(path.toFile())

        val sheet = xssf.getSheetAt(0)

        val qaList = mutableListOf<QandaVO>()

        for (row in sheet) {
            if (row.rowNum == 0) continue

            var q: String? = null
            var a: String? = null
            var tags: List<QaTagVO>? = null

            for (cell in row) {
                when (cell.columnIndex) {
                    0 -> {
                        q = cell.value
                    }

                    1 -> {
                        a = cell.value
                    }

                    2 -> {
                        tags = cell.value?.let {
                            listOf(QaTagVO(tagName = it))
                        }
                    }

                    else -> {

                    }
                }
//                logger.warn("{}行{}列解析失败： {}", row.rowNum, cell.columnIndex, cell.cellType)
            }

            qaList.add(
                QandaVO(
                    question = q ?: continue,
                    answer = a ?: continue,
                    tagList = tags,
                )
            )
        }

        qandaService.addList(qaList)

        logger.info("解析完成 {}", path)
        path.deleteExisting()

        return MsgVO.success("解析完成")
    }

    private val Cell.value: String?
        get() = when (cellType) {
            CellType.NUMERIC -> numericCellValue.toString()
            CellType.STRING -> stringCellValue
            else -> null
        }
}
