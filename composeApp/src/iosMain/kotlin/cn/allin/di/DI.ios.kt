package cn.allin.di

import androidx.compose.runtime.mutableStateOf
import cn.allin.ota.BluetoothManager
import cn.allin.ui.jlota.DeviceListState
import cn.allin.ui.jlota.OtaUIState
import org.koin.dsl.module

val iosModule = module {
    single {
        BluetoothManager(get())
    }
    factory {
        val d = mutableStateOf<List<String>>(emptyList())
        val bm: BluetoothManager = get()
        bm.onPeripheral = {
            d.value = it
        }
        DeviceListState(
            devices = d,
            onItem = {
                bm.connect(it)
                true
            }
        )
    }
    factory {
        val bm: BluetoothManager = get()
        OtaUIState()
    }
}
