package cn.allin.data.repository

import cn.allin.api.ApiFile
import cn.allin.data.entity.WeFile
import cn.allin.data.entity.WeFileInfo
import kotlinx.io.files.Path
import org.koin.core.annotation.Factory

@Factory
class FileManagerRepository(
    private val api: ApiFile
) {
    suspend fun paths(parent: Path?): List<WeFile> {
        return api.list(parent?.toString())
            .map {
                WeFile(Path(it), it.endsWith('/'))
            }
    }

    fun delete(path: Path) {
//        fs.delete(path)
    }

    fun info(source: Path): WeFileInfo? {

//        fs.metadataOrNull(source)?.let { metadata ->
//            return WeFileInfo(
//                name = source.name,
//                path = source.toString(),
//                size = metadata.size,
//                createTime = "2020"
//            )
//        }
        return null
    }

}
