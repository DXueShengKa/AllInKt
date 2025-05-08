package cn.allin.ui.jlota

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cn.allin.ksp.navigation.NavRoute
import org.koin.compose.getKoin

internal const val RouteOta = "RouteOta"

@NavRoute(
    routeString = RouteOta
)
@Composable
internal fun OtaScreen() {

    val koin = getKoin()
    val state: OtaUIState = remember { koin.get() }
    Column {
        Row {

        }
        LazyColumn {

        }
    }
}

internal class OtaUIState(

)