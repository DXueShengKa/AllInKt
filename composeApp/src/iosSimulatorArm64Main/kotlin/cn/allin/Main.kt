package cn.allin

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

fun ViewController(): UIViewController = ComposeUIViewController {
    App()
}
