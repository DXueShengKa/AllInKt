package cn.allin.ota

import jlLib.JLHashHandler
import jlLib.JL_OTAManager
import jlLib.JL_OTAManagerDelegateProtocol
import jlLib.JL_OTAResult
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSData
import platform.darwin.NSObject


@OptIn(ExperimentalForeignApi::class)
actual class JlOta {

    actual companion object {
        actual val version: String
            get() = JL_OTAManager.logSDKVersion()
    }

    private val otaDataSend = object : NSObject(), JL_OTAManagerDelegateProtocol {
        override fun otaDataSend(data: NSData) {

        }

        override fun otaUpgradeResult(result: JL_OTAResult, Progress: Float) {
        }

        override fun otaCancel() {
        }
    }

    private val hashHandler = JLHashHandler()
    private val otaManager = JL_OTAManager()

    init {
//        otaManager.delegate = otaDataSend
        JLHashHandler.sdkVersion()
        println("初始化ota sdk")
    }


    fun updateData(characteristic: CBCharacteristic) {

    }

    fun pair(peripheral: CBPeripheral, characteristicUuid: String){

    }
//
//    override fun hashOnPairOutputData(data: NSData) {
//
//    }

}
