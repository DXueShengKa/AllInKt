package cn.allin.data.repository

import arrow.core.Either
import cn.allin.ServerParams
import cn.allin.api.ApiQanda
import cn.allin.vo.PageVO
import cn.allin.vo.QandaVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.koin.core.annotation.Factory

@Factory([ApiQanda::class])
internal class QandaRepository(
    private val http: HttpClient,
) : ApiQanda {

    override suspend fun page(pageIndex: Int?, pageSize: Int?): PageVO<QandaVO> {
        return http.get(ApiQanda.pathPage) {
            parameter(ServerParams.PAGE_INDEX, pageIndex)
            parameter(ServerParams.PAGE_SIZE, pageSize)
        }.body()
    }

    override suspend fun add(qanda: QandaVO): Int {
        return http.post(ApiQanda.pathPage) {
            setBody(qanda)
        }.body()
    }

    override suspend fun delete(id: Int): Either<String, Unit> {
        return http.delete(ApiQanda.path(id)).body()
    }

    override suspend fun delete(ids: List<Int>): Int {
        return http.delete(ApiQanda.QANDA) {
            parameter("ids", ids.joinToString())
        }.body()
    }

}
