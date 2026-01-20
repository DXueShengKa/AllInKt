package cn.allin

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import cn.allin.net.WEKV
import cn.allin.net.initialize
import cn.allin.theme.MainTheme
import kotlinx.io.files.SystemTemporaryDirectory

private fun init(){
    WEKV.initialize(SystemTemporaryDirectory)
}

fun main() = application {
    val windowState = rememberWindowState(
        size = DpSize(375.dp, 812.dp)
    )

    init()

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "AllInKt",
    ) {
        MainTheme {
            Button({}){
                Text("ssss")
            }
//            MainApp(
//                application = {
//                    modules(DataDI.module, AppKoinViewModel)
//                }
//            ) {
//                appNavGraphs()
//            }
        }
    }
}

