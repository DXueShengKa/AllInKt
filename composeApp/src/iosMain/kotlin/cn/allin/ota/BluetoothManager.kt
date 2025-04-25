package cn.allin.ota

import androidx.compose.runtime.mutableStateListOf
import cn.allin.utils.InLogger
import cn.allin.utils.toNsData
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreBluetooth.CBAdvertisementDataServiceUUIDsKey
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerOptionShowPowerAlertKey
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicWriteWithResponse
import platform.CoreBluetooth.CBCharacteristicWriteWithoutResponse
import platform.CoreBluetooth.CBConnectionEvent
import platform.CoreBluetooth.CBManagerStatePoweredOff
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBManagerStateUnsupported
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBUUID
import platform.CoreFoundation.CFAbsoluteTime
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSMakeRange
import platform.Foundation.NSNumber
import platform.Foundation.subdataWithRange
import platform.darwin.NSObject
import kotlin.math.min

internal class BluetoothManager(
    val jlOta: JlOta
)   {

    companion object {


        /**
         * 写特征
         */
        const val CHARACTERISTIC_WRITE = "2AF1"

        /**
         * 读特征
         */
        const val NOTIFICATION_READER = "2AF0"
    }

    private val logger = InLogger("BluetoothManager")


    private val peripheralList = mutableListOf<CBPeripheral>()

    var currentPeripheral: CBPeripheral? = null

    /**
     * 收到信息
     */
    val currentReceive = mutableStateListOf<String>()

    var onConnect: ((CBPeripheral?) -> Unit)? = null

    var characteristicWrite: CBCharacteristic? = null

//    private var characteristicReader: CBCharacteristic? = null


    private val peripheralDelegate = OtaPeripheralDelegate(logger, this)

    private val managerDelegate = object : NSObject(), CBCentralManagerDelegateProtocol {
        override fun centralManager(central: CBCentralManager, willRestoreState: Map<Any?, *>) {
            logger.debug("willRestoreState $willRestoreState")
        }

        override fun centralManager(
            central: CBCentralManager,
            didDisconnectPeripheral: CBPeripheral,
            timestamp: CFAbsoluteTime,
            isReconnecting: Boolean,
            error: NSError?
        ) {
            logger.debug("设备连接中断 ${didDisconnectPeripheral.name} timestamp:$timestamp isReconnecting:$isReconnecting error:$error")
            onConnect?.invoke(null)
            currentPeripheral = null
            characteristicWrite = null
            currentReceive.clear()
            didDisconnectPeripheral.delegate = null
            peripheralList.remove(didDisconnectPeripheral)
        }

        override fun centralManager(central: CBCentralManager, didFailToConnectPeripheral: CBPeripheral, error: NSError?) {
            logger.debug("didFailToConnectPeripheral ${didFailToConnectPeripheral.name} error:$error")
        }

        override fun centralManager(central: CBCentralManager, connectionEventDidOccur: CBConnectionEvent, forPeripheral: CBPeripheral) {
            logger.debug("connectionEventDidOccur $connectionEventDidOccur ${forPeripheral.name}")
        }

        override fun centralManager(central: CBCentralManager, didConnectPeripheral: CBPeripheral) {
            logger.debug("连接设备 $didConnectPeripheral ")
            didConnectPeripheral.delegate = peripheralDelegate
            currentPeripheral = didConnectPeripheral
            onConnect?.invoke(didConnectPeripheral)
            discoverServices(null)
//            jlOta.noteConnected()
        }

        override fun centralManager(central: CBCentralManager, didDiscoverPeripheral: CBPeripheral, advertisementData: Map<Any?, *>, RSSI: NSNumber) {
            didDiscoverPeripheral.name ?: return

//            logger.debug("发现设备: ${didDiscoverPeripheral.name} ${didDiscoverPeripheral.RSSI}")

            if (!peripheralList.contains(didDiscoverPeripheral)) {

                val s = StringBuilder()
                s.append('\n')
                val uuids = advertisementData[CBAdvertisementDataServiceUUIDsKey]
                if (uuids != null) {
                    (uuids as List<CBUUID>).forEach {
                        s.append("uuid=$it\n")
                    }
                }

                logger.debug("发现设备: ${didDiscoverPeripheral.name} $s")
                peripheralList.add(didDiscoverPeripheral)
                onPeripheral?.invoke(peripheralList.map {
                    """
                      name: ${it.name}  
                      identifier: ${it.identifier}
                    """.trimIndent()
                })
            }
        }

        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            logger.info("centralManagerDidUpdateState ${central.state} ${central.isScanning} ")
            when (central.state) {
                CBManagerStatePoweredOn -> {
                    startScan()
                }

                CBManagerStatePoweredOff -> {
                    stop()
                }

                CBManagerStateUnsupported -> {
                    println("CBManagerStateUnsupported")
                }
            }
        }
    }

     fun send(data: String) {
        val d = data.toNsData() ?: return

        currentPeripheral?.writeValue(d, characteristicWrite ?: return, CBCharacteristicWriteWithResponse)
        logger.debug("发送数据string：$data")
    }

    @OptIn(ExperimentalForeignApi::class)
    fun send2(data: NSData?) {
        data ?: return
        val write = characteristicWrite ?: return
        val peripheral = currentPeripheral ?: return

        var offset = 0uL

        println(data)

        while (offset < data.length) {
            val macCount = min(128uL, data.length - offset)
            peripheral.writeValue(
                data = data.subdataWithRange(range = NSMakeRange(offset, offset + macCount)),
                write,
                CBCharacteristicWriteWithoutResponse
            )
            offset += macCount
            println(offset)
        }

        logger.debug("发送数据：ns data")
    }

    @OptIn(ExperimentalForeignApi::class)
    fun send(data: NSData?) {
        data ?: return
        val write = characteristicWrite ?: return
        val peripheral = currentPeripheral ?: return
        peripheral.writeValue(
            data,
            write,
            CBCharacteristicWriteWithoutResponse
        )

        logger.debug("发送数据data：$data")
    }

    private val centralManager: CBCentralManager = CBCentralManager(
        delegate = managerDelegate,
        queue = null,
        options = mapOf(CBCentralManagerOptionShowPowerAlertKey to true)
    )

     var onPeripheral: ((List<String>) -> Unit)? = null

     fun startScan() {
        centralManager.scanForPeripheralsWithServices(
            serviceUUIDs = null,
            options = null
        )
        logger.debug("扫描设备")
    }

     fun stop() {
        centralManager.stopScan()
    }

     fun connect(index: Int) {
        val peripheral = peripheralList[index]

        stop()

        centralManager.connectPeripheral(
            peripheral, null
        )

        logger.info("连接 ${peripheral.name}")
    }

    fun disconnect() {
        currentPeripheral?.also {
            centralManager.cancelPeripheralConnection(it)
        }
    }


    //发现所有可用的服务
    fun discoverServices(serverId: String?) {
        val uuids = serverId?.let { listOf(CBUUID.UUIDWithString(it)) }
        currentPeripheral?.discoverServices(uuids)
        logger.info("查找服务：$uuids")
    }

}
