package cn.allin.data

import cn.allin.net.commonConfig
import cn.allin.net.ktorEngineFactory
import io.ktor.client.*
import org.koin.dsl.module


val dataModule = module {
    single<HttpClient> {
        HttpClient(ktorEngineFactory){
            commonConfig()
        }
    }
}
