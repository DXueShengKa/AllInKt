package cn.allin.data

import cn.allin.net.appCommonConfig
import cn.allin.net.contentConverter
import cn.allin.net.ktorEngineFactory
import io.ktor.client.*
import org.koin.dsl.module


val dataModule = module {
    single<HttpClient> {
        HttpClient(ktorEngineFactory){
            appCommonConfig()
            contentConverter()
        }
    }
}