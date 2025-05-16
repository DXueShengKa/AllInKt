package cn.allin

import androidx.compose.ui.window.ComposeUIViewController
import cn.allin.navigation.appNavGraphs
import platform.UIKit.UIViewController

fun ViewController(): UIViewController = ComposeUIViewController {
    MainApp(
        application = {
            modules(AppKoinViewModel)
        }
    ) {
        appNavGraphs()
    }
}
