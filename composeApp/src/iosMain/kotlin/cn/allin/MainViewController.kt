package cn.allin

import androidx.compose.ui.window.ComposeUIViewController
import cn.allin.data.dataModule
import org.koin.compose.KoinApplication
import org.koin.ksp.generated.defaultModule

fun MainViewController() = ComposeUIViewController {
    KoinApplication(application = {
        modules(
            dataModule,
            defaultModule
        )
    }) {
        App()
    }
}