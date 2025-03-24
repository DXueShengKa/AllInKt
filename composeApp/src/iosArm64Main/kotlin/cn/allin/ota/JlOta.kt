package cn.allin.ota

import jlLib.JL_OTAManager
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
fun show(){
    println("JL_OTAManager ${JL_OTAManager.new()?.version}")
    println(
        JL_OTAManager.logSDKVersion()
    )
}
