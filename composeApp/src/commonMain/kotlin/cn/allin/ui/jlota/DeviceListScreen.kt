package cn.allin.ui.jlota

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cn.allin.LocalNavController
import org.koin.compose.getKoin


@Composable
internal fun DeviceListScreen() {
    val nav = LocalNavController.current
    val koin = getKoin()
    val state: DeviceListState = remember { koin.get() }
    Column {
        LazyColumn {
            itemsIndexed(state.devices.value) { i, s ->
                Text(s, Modifier.clickable {
                    if (state.onItem(i))
                        nav.navigate(RouteOta)
                })
                HorizontalDivider()
            }
        }
    }
}

internal class DeviceListState(
    val devices: State<List<String>>,
    val onItem: (Int) -> Boolean
)