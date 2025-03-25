package cn.allin

import androidx.compose.ui.window.ComposeUIViewController
import cn.allin.data.dataModule
import cn.allin.di.appModule
import cn.allin.navigation.appNavGraphs
import cn.allin.theme.MainTheme
import org.koin.ksp.generated.defaultModule
import platform.UIKit.UIViewController

fun ViewController(): UIViewController = ComposeUIViewController {
    MainTheme {
        MainApp(
            application = {
                modules(dataModule, defaultModule, AppKoinViewModel, appModule)
            }
        ) {
            appNavGraphs()
        }
    }
}
