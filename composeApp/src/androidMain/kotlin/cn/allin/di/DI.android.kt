package cn.allin.di

import android.app.Application
import dev.bluefalcon.BlueFalcon
import org.koin.dsl.module

val androidModule = module {
    factory {
        val context: Application = get()
        BlueFalcon(
            log = null,
            context = context
        )
    }
}
