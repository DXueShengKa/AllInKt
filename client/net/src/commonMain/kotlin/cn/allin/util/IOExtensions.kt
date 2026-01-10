package cn.allin.util

import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.close
import io.ktor.utils.io.writeBuffer
import io.ktor.utils.io.writer
import kotlinx.coroutines.CoroutineScope
import kotlinx.io.Buffer
import kotlinx.io.RawSink
import kotlinx.io.RawSource
import kotlinx.io.files.FileMetadata
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlin.coroutines.CoroutineContext

fun RawSource.toByteReadChannel(
    fileLength: Long = 0,
    context: CoroutineContext,
    onProgress: ((Int) -> Unit)? = null,
): ByteReadChannel =
    CoroutineScope(context)
        .writer(autoFlush = true) {
            val buffer = Buffer()
            val byteCount = 1024L * 8
            try {
                var totalBytesWrite = 0L

                while (true) {
                    val readCount = readAtMostTo(buffer, byteCount)
                    if (readCount == -1L) break

                    channel.writeBuffer(buffer)

                    if (onProgress != null) {
                        totalBytesWrite += readCount
                        onProgress((totalBytesWrite * 100 / fileLength).toInt())
                    }
                }

                channel.flush()
            } catch (cause: Throwable) {
                channel.close(cause)
            } finally {
                buffer.close()
                close()
            }
        }.channel

fun RawSource.copyTo(
    target: RawSink,
    sourceLength: Long,
    onProgress: (Byte) -> Unit,
) {
    val buffer = Buffer()
    val byteCount = 1024L * 8
    target.use { out ->
        var totalBytesWrite = 0L

        while (true) {
            val readCount = readAtMostTo(buffer, byteCount)
            if (readCount == -1L) break

            out.write(buffer, readCount)
            totalBytesWrite += readCount
            onProgress((totalBytesWrite * 100 / sourceLength).toByte())
        }
    }
}

fun RawSource.copyTo(
    target: Path,
    sourceLength: Long,
    onProgress: (Byte) -> Unit,
) {
    copyTo(SystemFileSystem.sink(target), sourceLength, onProgress)
}

val Path.fileSize: Long
    get() = metadata?.size ?: 0

inline val Path.metadata: FileMetadata?
    get() = SystemFileSystem.metadataOrNull(this)

fun String.toKtIoPath(): Path = Path(path = this)

fun Path.deleteAll() {
    SystemFileSystem.apply {
        list(this@deleteAll).forEach {
            if (metadataOrNull(it)?.isDirectory == true) {
                it.deleteAll()
            }
            delete(it)
        }
    }
}
