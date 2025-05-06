package cn.allin

import androidx.compose.ui.window.ComposeUIViewController
import cn.allin.data.DataDI
import cn.allin.navigation.appNavGraphs
import cn.allin.theme.MainTheme
import org.koin.ksp.generated.module
import platform.UIKit.UIViewController

fun ViewController(): UIViewController = ComposeUIViewController {
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
