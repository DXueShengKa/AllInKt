package cn.allin

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cn.allin.data.DataDI
import cn.allin.navigation.appNavGraphs
import cn.allin.theme.MainTheme
import org.koin.ksp.generated.module


fun main() = application {
    val windowState = rememberWindowState(
        size = DpSize(375.dp, 812.dp)
    )
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "AllInKt",
    ) {
        MainTheme {
            MainApp(
                application = {
                    modules(DataDI.module, AppKoinViewModel)
                }
            ) {
                appNavGraphs()
            }
        }
    }
}

