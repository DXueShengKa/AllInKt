package cn.allin

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cn.allin.data.dataModule
import cn.allin.navigation.appNavGraphs
import cn.allin.theme.MainTheme
import cn.allin.ui.fileMamager.RouteFileManager
import org.koin.compose.KoinApplication
import org.koin.ksp.generated.defaultModule


fun main() = application {
    val windowState = rememberWindowState(
        size = DpSize(375.dp, 812.dp)
    )
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "AllInKt",
    ) {
        KoinApplication(application = {
            modules(dataModule, defaultModule)
        }) {
            MainTheme {
                Scaffold {
                    MainApp()
                }
            }
        }
    }
}

@Composable
private fun MainApp(){
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = RouteFileManager,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start)
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End)
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
            }
        ) {
            appNavGraphs()
        }
    }
}

