package cn.allin

import androidx.compose.ui.window.ComposeUIViewController
import cn.allin.di.appModule
import cn.allin.di.iosModule
import cn.allin.navigation.appNavGraphs
import platform.UIKit.UIViewController

fun ViewController(): UIViewController = ComposeUIViewController {
    MainApp(
        application = {
            modules(AppKoinViewModel, iosModule, appModule)
        }
    ) {
        appNavGraphs()
    }
}
