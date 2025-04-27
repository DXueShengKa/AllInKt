package cn.allin.data.repository

import arrow.core.Either
import cn.allin.ServerParams
import cn.allin.api.ApiQandaTag
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.koin.core.annotation.Factory

@Factory([ApiQandaTag::class])
class QaTagRepository(
    private val http: HttpClient,
): ApiQandaTag {
    override suspend fun page(pageIndex: Int?, pageSize: Int?): PageVO<QaTagVO> {
        return http.get(ApiQandaTag.pathPage){
            parameter(ServerParams.PAGE_INDEX, pageIndex)
            parameter(ServerParams.PAGE_SIZE, pageSize)
        }.body()
    }

    override suspend fun add(tag: QaTagVO) {
        http.post(ApiQandaTag.QA_TAG){
            setBody(tag)
        }
    }

    override suspend fun delete(id: Int): Either<String, Unit> {
        return http.delete(ApiQandaTag.path(id)).body()
    }
}
