package cn.allin.utils


import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.toCValues
import platform.CoreBluetooth.CBCharacteristic
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding


@OptIn(BetaInteropApi::class)
internal fun NSData.nsString() = NSString.create(this, NSUTF8StringEncoding)


@OptIn(ExperimentalForeignApi::class)
internal fun NSData.byteArray(): ByteArray? {

    val b = bytes ?: return null
    return b.readBytes(length.toInt())
}


@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
internal fun ByteArray.toNSData(): NSData? {
    val b = toCValues()
    return NSData.create(
        bytes = b as CPointer<*>,
        length = size.toULong(),
    )
}

@OptIn(ExperimentalForeignApi::class)
internal fun String.toNsData() = (this as NSString).dataUsingEncoding(NSUTF8StringEncoding)


fun CBCharacteristic.hasProperties(property: ULong) = properties and property > 0u
