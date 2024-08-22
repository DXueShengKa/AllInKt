package cn.allin.net

import io.ktor.client.engine.*
import io.ktor.client.engine.okhttp.*


actual val ktorEngineFactory: HttpClientEngineFactory<*> = OkHttp
