package cn.allin.api

import arrow.core.Either
import cn.allin.apiRoute.Companion.PAGE
import cn.allin.vo.PageVO
import cn.allin.vo.QandaVO
import kotlin.jvm.JvmStatic

interface ApiQanda {

    suspend fun page(pageIndex: Int?, pageSize: Int?): PageVO<QandaVO>

    suspend fun add(qanda: QandaVO): Int

    suspend fun delete(id: Int): Either<String, Unit>

    suspend fun delete(ids: List<Int>): Int


    companion object {
        const val QANDA = "qanda"
        const val EXCEL = "excel"

        @JvmStatic
        fun path(id: Int) = "${QANDA}/$id"

        const val pathPage = "$QANDA/${PAGE}"
        const val pathExcel = "$QANDA/$EXCEL"

    }
}
