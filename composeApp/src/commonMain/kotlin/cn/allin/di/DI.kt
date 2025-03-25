package cn.allin.di

import cn.allin.ota.JlOta
import org.koin.dsl.module

val appModule = module {
    single { JlOta() }
}
