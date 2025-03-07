package cn.allin.net

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

actual val ktorEngineFactory: HttpClientEngineFactory<*> = Js
