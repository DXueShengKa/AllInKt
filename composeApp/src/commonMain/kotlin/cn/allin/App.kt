package cn.allin

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import cn.allin.ksp.navigation.NavRoute
import cn.allin.ui.Res
import cn.allin.ui.RouteCalendar
import cn.allin.ui.calendar_month
import cn.allin.ui.draft
import cn.allin.ui.fileMamager.RouteFileManager
import cn.allin.ui.fileMamager.RouteTransferManager
import cn.allin.ui.wifi_protected_setup
import eu.wewox.lazytable.LazyTable
import eu.wewox.lazytable.LazyTableItem
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.ComposeContextWrapper
import org.koin.compose.LocalKoinApplication
import org.koin.compose.LocalKoinScope
import org.koin.compose.application.rememberKoinApplication
import org.koin.core.annotation.KoinInternalApi
import org.koin.dsl.KoinAppDeclaration

internal val LocalNavController = staticCompositionLocalOf<NavController> { error("未初始化导航") }

internal const val RouteApp = "MainApp"

@NavRoute(routeString = RouteApp)
@Composable
internal fun App() {
    val nav = LocalNavController.current
    LazyVerticalGrid(
        columns = GridCells.Adaptive(100.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            AppItem(painterResource(Res.drawable.draft), "文件管理") { nav.navigate(RouteFileManager) }
        }
        item {
            AppItem(painterResource(Res.drawable.wifi_protected_setup), "传输管理") { nav.navigate(RouteTransferManager) }
        }
        item {
            AppItem(painterResource(Res.drawable.calendar_month), "日历") { nav.navigate(RouteCalendar) }
        }
    }
}

@Composable
private fun AppItem(img: Painter, text: String, onItemClicked: () -> Unit) {
    ElevatedCard(onClick = onItemClicked, Modifier.size(100.dp)) {
        Icon(img, text)
        Text(text)
    }
}

@OptIn(KoinInternalApi::class)
@Composable
internal fun MainApp(
    application: KoinAppDeclaration,
    builder: NavGraphBuilder.() -> Unit
) {
    val navController = rememberNavController()
    val koin = rememberKoinApplication(application)
    CompositionLocalProvider(
        LocalNavController provides navController,
        LocalKoinApplication provides ComposeContextWrapper(koin),
        LocalKoinScope provides ComposeContextWrapper(koin.scopeRegistry.rootScope),
        LocalContentColor provides MaterialTheme.colorScheme.onBackground
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


@Composable
fun LazyTableSimpleScreen(
    onBackClick: () -> Unit,
) {

    val columns = 10
    val rows = 30
    val cells = remember { createCells(columns, rows) }


    LazyTable(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(
            items = cells,
            layoutInfo = { LazyTableItem(it.first, it.second) }
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .border(Dp.Hairline, MaterialTheme.colorScheme.onSurface)
            ) {
                Text(text = "$it")
            }
        }

    }
}

fun createCells(columns: Int = COLUMNS, rows: Int = ROWS): List<Pair<Int, Int>> =
    buildList {
        repeat(rows) { row ->
            repeat(columns) { column ->
                add(column to row)
            }
        }
    }

private const val COLUMNS = 10
private const val ROWS = 30
