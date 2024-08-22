package cn.allin.net

import cn.allin.ui.AddUser
import cn.allin.vo.UserVO
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*


val http = HttpClient(Js) {
    defaultRequest {
        url(SERVER_BASE_URL)
        if (contentType() == null)
            contentType(ContentTypeXProtobuf)
    }
    
    contentConverter()
}

class ReqUser {
    suspend fun getUserAll(): List<UserVO> {
        return http.get("user").body()
    }

    suspend fun addUser(addUser: AddUser) {
        http.post("user") {
            headers.remove(HttpHeaders.ContentType)
            contentType(ContentType.Application.Json)
            setBody(JSON.stringify(addUser))
        }.body<Unit>()
    }
}