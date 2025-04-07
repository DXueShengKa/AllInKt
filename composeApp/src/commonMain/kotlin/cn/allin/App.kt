package cn.allin

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cn.allin.ksp.navigation.NavRoute
import cn.allin.ui.jlota.JlOtaScreen
import org.koin.compose.LocalKoinApplication
import org.koin.compose.LocalKoinScope
import org.koin.compose.application.rememberKoinApplication
import org.koin.core.annotation.KoinInternalApi
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.koinApplication

val LocalNavController = staticCompositionLocalOf<NavController> { error("未初始化导航") }

const val RouteApp = "MainApp"

@OptIn(ExperimentalMaterial3Api::class)
@NavRoute(routeString = RouteApp)
@Composable
fun App() {
    Scaffold(
        topBar = {
            Box(
                Modifier.statusBarsPadding().fillMaxWidth().height(56.dp).background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text("ota升级")
            }
        },
//        sheetContent = {
//
//        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {

            JlOtaScreen()
        }
    }
}


@OptIn(KoinInternalApi::class)
@Composable
fun MainApp(
    application: KoinAppDeclaration,
    builder: NavGraphBuilder.() -> Unit
) {
    val navController = rememberNavController()
    val koin = rememberKoinApplication(koinApplication(application))
    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalKoinApplication provides koin,
        LocalKoinScope provides koin.scopeRegistry.rootScope
    ) {
        NavHost(
            navController = navController,
            startDestination = RouteApp,
            modifier = Modifier.background(color = MaterialTheme.colorScheme.background),
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
            },
            builder = builder
        )
    }
}

