package cn.allin.ui.fileMamager

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cn.allin.LocalNavController
import cn.allin.ksp.navigation.NavRoute
import cn.allin.ui.fileManager.FileHome
import cn.allin.ui.fileManager.rememberFileManagerState
import kotlinx.coroutines.delay

const val RouteFileManager = "RouteFileManager"


@NavRoute(
    routeString = RouteFileManager,
)
@Composable
internal fun RouteFileManager() {
    val nav = LocalNavController.current
    val state = rememberFileManagerState(
        onBack = {
            nav.navigateUp()
        }
    )
    FileHome(state)
    LaunchedEffect(state) {
        delay(2000)
        state.open()
    }
}
