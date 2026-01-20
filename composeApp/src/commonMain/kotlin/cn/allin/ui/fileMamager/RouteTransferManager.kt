package cn.allin.ui.fileMamager

import androidx.compose.runtime.Composable
import cn.allin.ksp.navigation.NavRoute
import cn.allin.ui.fileManager.TransferManagerScreen
import org.koin.compose.viewmodel.koinViewModel

const val RouteTransferManager = "TransferManager"


@NavRoute(routeString = RouteTransferManager)
@Composable
fun RouteTransferManager() {
//    val nav = LocalNavController.current
    val vm: TransferManagerViewModel = koinViewModel()
    TransferManagerScreen(
        vm.upload,
        vm.download
    ) {
//        nav.navigateUp()
    }
}
