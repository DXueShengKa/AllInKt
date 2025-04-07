package cn.allin.ota

import com.jieli.jl_bt_ota.constant.JL_Constant

actual class JlOta {

    actual companion object {
        actual val version: String
            get() = JL_Constant.getLibVersionName()
    }

//    private val otaDataSend = object : NSObject(), JL_OTAManagerDelegateProtocol {
//        override fun otaDataSend(data: NSData) {
//
//        }
//
//        override fun otaUpgradeResult(result: JL_OTAResult, Progress: Float) {
//        }
//
//        override fun otaCancel() {
//        }
//    }
//
//    private val hashHandler = JLHashHandler()
//    private val otaManager = BleManager()

    init {

//        otaManager.delegate = otaDataSend
//        JLHashHandler.sdkVersion()
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
