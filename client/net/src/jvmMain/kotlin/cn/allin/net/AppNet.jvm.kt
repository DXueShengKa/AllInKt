package cn.allin.net

import io.ktor.client.engine.*
import io.ktor.client.engine.java.*


actual val ktorEngineFactory: HttpClientEngineFactory<*>  = Java
