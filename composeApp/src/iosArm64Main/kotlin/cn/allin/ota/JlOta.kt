package cn.allin.ota

import jlLib.JLHashHandler
import jlLib.JL_OTAManager
import jlLib.JL_OTAManagerDelegateProtocol
import jlLib.JL_OTAResult
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.darwin.NSObject


@OptIn(ExperimentalForeignApi::class)
actual class JlOta {

    actual companion object {
        actual val version: String
            get() = JL_OTAManager.logSDKVersion()
    }

    private val otaDataSend = object : JL_OTAManagerDelegateProtocol, NSObject(){
        override fun otaDataSend(data: NSData) {

        }

        override fun otaUpgradeResult(result: JL_OTAResult, Progress: Float) {
            super.otaUpgradeResult(result, Progress)
        }

        override fun otaCancel() {
            super.otaCancel()

        }
    }

    private val hashHandler = JLHashHandler()
    private val otaManager = JL_OTAManager()

    init {
        otaManager.delegate = otaDataSend
        JLHashHandler.sdkVersion()
        println("初始化ota sdk")
    }


//    override fun otaDataSend(data: NSData) {
//
//    }
//
//    override fun hashOnPairOutputData(data: NSData) {
//
//    }

}
