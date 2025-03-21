package cn.allin

import androidx.compose.ui.window.ComposeUIViewController
import cn.allin.data.dataModule
import cn.allin.navigation.appNavGraphs
import cn.allin.theme.MainTheme
import org.koin.ksp.generated.defaultModule
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    MainTheme {
        MainApp(
            application = {
                modules(dataModule, defaultModule, AppKoinViewModel)
            }
        ) {
            appNavGraphs()
        }
    }
}
