package cn.allin.di

import dev.bluefalcon.BlueFalcon
import org.koin.dsl.module
import platform.UIKit.UIApplication

val iosModule = module {
    factory {
        BlueFalcon(
            log = null,
            context = UIApplication.sharedApplication
        )
    }
}
