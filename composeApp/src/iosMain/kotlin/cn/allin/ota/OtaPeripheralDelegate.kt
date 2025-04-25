package cn.allin.ota

import cn.allin.ota.BluetoothManager.Companion.CHARACTERISTIC_WRITE
import cn.allin.utils.InLogger
import cn.allin.utils.byteArray
import cn.allin.utils.hasProperties
import cn.allin.utils.nsString
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCSignatureOverride
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicPropertyNotify
import platform.CoreBluetooth.CBCharacteristicPropertyRead
import platform.CoreBluetooth.CBCharacteristicPropertyWrite
import platform.CoreBluetooth.CBCharacteristicPropertyWriteWithoutResponse
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.CoreBluetooth.CBService
import platform.Foundation.NSError
import platform.darwin.NSObject

/**
 * app读写用服务
 */
internal const val SERVICE_RW = "18F0"

/**
 * 设备信息服务
 */
internal const val SERVICE_INFO = "180A"

/**
 * 升级模式的服务
 */
internal const val SERVICE_OTA = "66F0"

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class, ExperimentalStdlibApi::class)
internal class OtaPeripheralDelegate(
    private val logger: InLogger,
    private val bleManager: BluetoothManager
) : NSObject(), CBPeripheralDelegateProtocol {

    /**
     * 过滤只需要的服务
     */
    private val servicesFilter = arrayOf(SERVICE_RW, SERVICE_INFO, SERVICE_OTA)

    override fun peripheral(peripheral: CBPeripheral, didDiscoverServices: NSError?) {
        if (didDiscoverServices != null) {
            logger.error("发现服务失败 ${didDiscoverServices.localizedDescription}")
            return
        }
        logger.info("发现设备服务 ${peripheral.name}")

        peripheral.services?.forEach { service ->
            service as CBService
            //过滤只需要的服务
//                if (service.UUID.UUIDString in servicesFilter) {
            peripheral.discoverCharacteristics(null, service)
//                }


            logger.info("$service 服务uuid:${service.UUID.UUIDString}")
//                logger.info("service UUIDString = ${service.UUID.UUIDString} description = ${service.UUID.description}")
        }

    }


    override fun peripheral(peripheral: CBPeripheral, didDiscoverCharacteristicsForService: CBService, error: NSError?) {
        logger.info("发现设备特征 ${peripheral.name} 服务uuid:${didDiscoverCharacteristicsForService.UUID.UUIDString}")

        if (error != null) {
            logger.error(error.toString())
            return
        }

        logger.info("特征列表：")

        val characteristics = didDiscoverCharacteristicsForService.characteristics ?: return
        when (didDiscoverCharacteristicsForService.UUID.UUIDString) {
            SERVICE_OTA -> {
                characteristics.forEach { characteristic ->
                    discoverCharacteristics(peripheral, characteristic as CBCharacteristic)
                }
            }

            SERVICE_RW -> {
                characteristics.forEach { characteristic ->
                    discoverCharacteristics(peripheral, characteristic as CBCharacteristic)
                }
            }

            SERVICE_INFO -> {
                characteristics.forEach { characteristic ->
                    deviceInformation(characteristic as CBCharacteristic)
                }
            }

            else -> {
                characteristics.forEach { characteristic ->
                    characteristic as CBCharacteristic
                    logger.info("characteristic $characteristic uuid: ${characteristic.UUID.UUIDString}")
                }
            }
        }

    }


    override fun peripheral(peripheral: CBPeripheral, didModifyServices: List<*>) {
        logger.info("didModifyServices ${peripheral.name} ${didModifyServices.joinToString(", ")}")
    }

    @ObjCSignatureOverride
    override fun peripheral(peripheral: CBPeripheral, didWriteValueForCharacteristic: CBCharacteristic, error: NSError?) {
        if (error == null) {
            logger.info("发送成功 -> $didWriteValueForCharacteristic")
        } else {
            logger.error("发送失败 ${error.localizedDescription}")
        }
    }


    @ObjCSignatureOverride
    override fun peripheral(peripheral: CBPeripheral, didUpdateValueForCharacteristic: CBCharacteristic, error: NSError?) {
        val value = didUpdateValueForCharacteristic.value?.nsString() ?: return
        val bs = didUpdateValueForCharacteristic.value?.byteArray()?.joinToString(",0x", "[0x", "]") {
            it.toHexString()
        }
        bleManager.jlOta.updateData(didUpdateValueForCharacteristic)

        //更新特征值
        logger.info("接收信息 $didUpdateValueForCharacteristic value = $value $bs")
        bleManager.currentReceive.add("特征：${didUpdateValueForCharacteristic.UUID.UUIDString}\n值：$value")
    }

    @ObjCSignatureOverride
    override fun peripheral(peripheral: CBPeripheral, didUpdateNotificationStateForCharacteristic: CBCharacteristic, error: NSError?) {

        logger.info("更新通知状态特征 $didUpdateNotificationStateForCharacteristic")
        bleManager.jlOta.pair(peripheral,didUpdateNotificationStateForCharacteristic.UUID.UUIDString)
    }


    private fun deviceInformation(characteristic: CBCharacteristic) {
        val v = characteristic.value?.nsString() ?: return
        logger.debug("设备信息 $v")
        bleManager.currentReceive.add("特征：${characteristic.UUID.description}\n值：$v")
    }


    private fun discoverCharacteristics(peripheral: CBPeripheral, characteristic: CBCharacteristic) {
        characteristic.isNotifying

        if (characteristic.hasProperties(CBCharacteristicPropertyWriteWithoutResponse)){
            bleManager.characteristicWrite = characteristic
            logger.info("可写外设 WriteWithoutResponse $characteristic")
        }

        if (characteristic.hasProperties(CBCharacteristicPropertyRead)) {
            peripheral.readValueForCharacteristic(characteristic)
            logger.info("可读外设 $characteristic")
        }

        if (characteristic.hasProperties(CBCharacteristicPropertyWrite)) {
            if (characteristic.UUID.UUIDString == CHARACTERISTIC_WRITE)
                bleManager.characteristicWrite = characteristic

            logger.info("可写外设 $characteristic")
        }

        if (characteristic.hasProperties(CBCharacteristicPropertyNotify)) {
            peripheral.setNotifyValue(true, characteristic)
            logger.info("设备通知 $characteristic")
        }

        logger.info("uuid:${characteristic.UUID} description${characteristic.description} value:${characteristic.value} properties:${characteristic.properties}")
    }
}
