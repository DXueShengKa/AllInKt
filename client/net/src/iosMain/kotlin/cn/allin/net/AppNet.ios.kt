package cn.allin.net

import io.ktor.client.engine.*
import io.ktor.client.engine.darwin.*


actual val ktorEngineFactory: HttpClientEngineFactory<*> = Darwin
