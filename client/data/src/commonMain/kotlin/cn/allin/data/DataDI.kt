package cn.allin.data

import cn.allin.net.commonConfig
import cn.allin.net.ktorEngineFactory
import io.ktor.client.*
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import kotlin.jvm.JvmStatic

@Module
@ComponentScan("cn.allin.data.repository")
object DataDI {

    @Single(createdAtStart = true)
    @JvmStatic
    fun httpClient(): HttpClient {
        return HttpClient(ktorEngineFactory) {
            commonConfig()
        }
    }
}
