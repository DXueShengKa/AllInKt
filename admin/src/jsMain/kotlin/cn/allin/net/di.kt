package cn.allin.net

import org.koin.dsl.module

val MainDI = module {
    single(createdAtStart = true) {
        Req.http
    }
}
