package cn.allin.data.repository

import cn.allin.data.entity.WeFile
import cn.allin.data.entity.WeFileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.koin.core.annotation.Factory


@Factory
class FileManagerRepository(
) {
    private val fs = SystemFileSystem
    private val rootPath = Path("/Users/zbkandroid")

    suspend fun paths(parent: Path?): List<WeFile> {
        val paths = if (parent == null) fs.list(rootPath)
        else fs.list(parent)
        return withContext(Dispatchers.IO) {
            paths.map {
                WeFile(
                    path = it,
                    isDir = fs.metadataOrNull(it)?.isDirectory == true,
                )
            }
        }
    }


    fun move(source: Path, destination: Path) {
        fs.atomicMove(source, destination)
    }

    fun rename(path: Path, name: String, desc: String) {

    }

    fun delete(path: Path) {
        fs.delete(path)
    }

    fun info(source: Path): WeFileInfo? {

        fs.metadataOrNull(source)?.let { metadata ->
            return WeFileInfo(
                name = source.name,
                path = source.toString(),
                size = metadata.size,
                createTime = "2020"
            )
        }
        return null
    }

}
