package cn.allin.util

import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.io.Buffer
import kotlinx.io.RawSource

fun ByteArray.toReadChannel(onProgress: ((Int) -> Unit)? = null): ByteReadChannel? {
    val r = object : RawSource {
        override fun readAtMostTo(sink: Buffer, byteCount: Long): Long {
            sink.readAtMostTo(this@toReadChannel)
            return this@toReadChannel.size.toLong()
        }

        override fun close() {
        }

    }
    return r.toByteReadChannel(size.toLong(), Dispatchers.Default, onProgress)
}

