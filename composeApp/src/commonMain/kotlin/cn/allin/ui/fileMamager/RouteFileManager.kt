package cn.allin.ui.fileMamager

import androidx.compose.runtime.Composable
import cn.allin.LocalNavController
import cn.allin.ksp.navigation.NavRoute
import cn.allin.ui.fileManager.FileHomeScreen
import cn.allin.ui.fileManager.rememberFileManagerState
import org.koin.compose.viewmodel.koinViewModel

const val RouteFileManager = "RouteFileManager"


@NavRoute(
    routeString = RouteFileManager,
)
@Composable
internal fun RouteFileManager() {
    val nav = LocalNavController.current
    val vm = koinViewModel<FileManagerViewModel>()

    val state = rememberFileManagerState(
        vm.fileListFlow,
        onBack = {
            if (vm.currentPath == null)
                nav.navigateUp()
            else
                vm.previous()
        },
        onItemClick = {
            vm.next(it)
        },
        onDelete = {

        },
        onDown = {

        },
        getDesc = vm.getDesc
    )

    FileHomeScreen(state)
}
