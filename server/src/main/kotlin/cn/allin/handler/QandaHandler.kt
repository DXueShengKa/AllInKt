package cn.allin.handler

import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

class QandaHandler() {

    suspend fun list(request: ServerRequest): ServerResponse{
        return ServerResponse.ok().bodyValueAndAwait("")
    }
}