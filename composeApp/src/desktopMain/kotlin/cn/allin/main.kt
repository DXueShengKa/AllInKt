package cn.allin

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cn.allin.data.dataModule
import org.koin.compose.KoinApplication
import org.koin.ksp.generated.defaultModule

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AllInKt",
    ) {
        KoinApplication(application = {
            modules(dataModule, defaultModule)
        }) {
            App()
        }
    }
}