package cn.allin.ui.jlota

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cn.allin.ota.JlOta
import dev.bluefalcon.AdvertisementDataRetrievalKeys
import dev.bluefalcon.BlueFalcon
import dev.bluefalcon.BlueFalconDelegate
import dev.bluefalcon.BluetoothPeripheral
import org.koin.compose.getKoin
import org.koin.core.Koin


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun JlOtaScreen(){
    val koin: Koin = getKoin()
    val state = remember{ OtaState(koin.get(),koin.get()) }


    Column (modifier = Modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Button({
                state.scan()
            }){
                Text("扫描")
            }
            Button({
                state.stop()
            }){
                Text("停止")
            }
        }
        Text(text = JlOta.version)
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.deviceList){
                Text(it?:"null")
            }
        }
    }

}

internal class OtaState(
    private val jlOta: JlOta,
    private val blueFalcon: BlueFalcon
){
    private val p = hashMapOf<String, BluetoothPeripheral>()

    private val bleDelegate = object: BlueFalconDelegate {
        override fun didConnect(bluetoothPeripheral: BluetoothPeripheral) {

        }

        override fun didDisconnect(bluetoothPeripheral: BluetoothPeripheral) {
            println("${bluetoothPeripheral.name} Disconnect")
        }

        override fun didDiscoverServices(bluetoothPeripheral: BluetoothPeripheral) {
            println("${bluetoothPeripheral.name} DiscoverServices")
        }

        override fun didDiscoverDevice(bluetoothPeripheral: BluetoothPeripheral, advertisementData: Map<AdvertisementDataRetrievalKeys, Any>) {
            println("${bluetoothPeripheral.name} DiscoverDevice advertisementData")
            println(advertisementData.entries.joinToString("\n"){ "${it.key}: ${it.value}" })
            if (!p.containsKey(bluetoothPeripheral.uuid)){
                p[bluetoothPeripheral.uuid] = bluetoothPeripheral
                deviceList.add(bluetoothPeripheral.name)
            }
        }

        override fun didDiscoverCharacteristics(bluetoothPeripheral: BluetoothPeripheral) {
            println("${bluetoothPeripheral.name} DiscoverCharacteristics")
        }

    }

    init {
        blueFalcon.delegates.add(bleDelegate)
    }

    val deviceList = mutableStateListOf<String?>()

    fun scan(){
        blueFalcon.scan()
    }

    fun stop(){
        blueFalcon.stopScanning()
    }

}
