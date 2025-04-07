package cn.allin.ota

import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManagerStatePoweredOff
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBManagerStateUnsupported
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.Foundation.NSNumber
import platform.darwin.NSObject

class OtaManager() : NSObject(), CBCentralManagerDelegateProtocol, CBPeripheralDelegateProtocol {

    private val centralManager: CBCentralManager = CBCentralManager(
        delegate = this,
        queue = null
    )


    val peripheralList = mutableListOf<CBPeripheral>()

    override fun centralManagerDidUpdateState(central: CBCentralManager) {
        when (central.state) {
            CBManagerStatePoweredOn -> {
                start()
            }

            CBManagerStatePoweredOff -> {
                stop()
            }

            CBManagerStateUnsupported -> {
                println("未发现设备")
            }
        }
    }

    fun start() {
        centralManager.scanForPeripheralsWithServices(
            serviceUUIDs = null,
            options = null
        )
    }

    fun stop() {
        centralManager.stopScan()
    }

    override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
        if (!peripheralList.contains(didDiscoverPeripheral)) {
            peripheralList.add(didDiscoverPeripheral)
        }
    }

}
