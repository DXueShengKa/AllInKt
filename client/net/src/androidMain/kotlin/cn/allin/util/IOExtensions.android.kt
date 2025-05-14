package cn.allin.util

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.io.asSource
import kotlinx.io.files.SystemFileSystem

fun Uri.toReadChannel(context: Context, onProgress: ((Int) -> Unit)? = null): ByteReadChannel? {
    if (scheme == "file") {
        val file = path?.toKtIoPath() ?: return null
        val brc = SystemFileSystem.source(file).toByteReadChannel(
            file.fileSize,
            Dispatchers.IO,
            onProgress = onProgress
        )
        return brc
    }

    val contentResolver = context.contentResolver
    return contentResolver.query(
        this,
        arrayOf(MediaStore.Files.FileColumns.SIZE),
        null,
        null
    )?.use {
        check(it.moveToFirst()) {
            "查询文件失败"
        }
        val size = it.getLong(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
        contentResolver.openInputStream(this)?.asSource()?.toByteReadChannel(size, Dispatchers.IO, onProgress)
    }

}
