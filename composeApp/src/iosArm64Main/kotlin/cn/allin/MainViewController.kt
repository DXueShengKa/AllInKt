package cn.allin

import androidx.compose.ui.window.ComposeUIViewController
import cn.allin.data.dataModule
import cn.allin.navigation.appNavGraphs
import cn.allin.theme.MainTheme
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.ksp.generated.defaultModule
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
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
