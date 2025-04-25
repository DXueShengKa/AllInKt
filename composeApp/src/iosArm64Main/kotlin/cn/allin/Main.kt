package cn.allin

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.window.ComposeUIViewController
import cn.allin.di.appModule
import cn.allin.di.iosModule
import cn.allin.navigation.appNavGraphs
import platform.UIKit.UIViewController

fun ViewController(): UIViewController = ComposeUIViewController {
    MaterialTheme {
        MainApp(
            application = {
                modules(AppKoinViewModel, appModule, iosModule)
            }
        ) {
            appNavGraphs()
        }
    }
}
