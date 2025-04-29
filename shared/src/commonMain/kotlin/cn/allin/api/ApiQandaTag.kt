package cn.allin.api

import arrow.core.Either
import cn.allin.apiRoute.Companion.PAGE
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO

interface ApiQandaTag {

    suspend fun page(pageIndex: Int?, pageSize: Int?): PageVO<QaTagVO>

    suspend fun getAll(): List<QaTagVO>

    suspend fun get(id: Int): QaTagVO

    suspend fun add(tag: QaTagVO)

    suspend fun update(tag: QaTagVO)

    suspend fun delete(id: Int): Either<String, Unit>


    companion object {
        const val QA_TAG = "qanda/tag"
        const val ALL = "all"

        const val pathPage = "$QA_TAG/$PAGE"

        const val pathAll = "$QA_TAG/$ALL"

        fun path(id: Int) = "$QA_TAG/$id"

    }
}
