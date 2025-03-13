package cn.allin.ui.fileMamager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.allin.data.entity.WeFile
import cn.allin.data.repository.FileManagerRepository
import cn.allin.ui.components.ImageUrl
import cn.allin.ui.fileManager.FileManagerDesc
import cn.allin.ui.fileManager.FileManagerItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.io.files.Path


class FileManagerViewModel(
    private val repository: FileManagerRepository
) : ViewModel() {

    val fileListFlow = MutableStateFlow<List<FileManagerItem>>(emptyList())

    init {
        list(null)
    }

    private var paths: List<WeFile> = emptyList()

    var currentPath: Path? = null
        set(value) {
            list(value)
            field = value
        }

    private fun list(path: Path?) {
        viewModelScope.launch {
            paths = repository.paths(path)

            fileListFlow.value = paths.map {
                FileManagerItem(
                    img = ImageUrl.ImageUrlDefault,
                    type = if (it.isDir) FileManagerItem.FILE_TYPE_DIR else FileManagerItem.FILE_TYPE_FILE,
                    name = it.path.name,
                    desc = it.path.parent.toString()
                )
            }
        }
    }

    fun previous() {
        currentPath = currentPath?.parent
    }

    fun next(index: Int) {
        val f = paths[index]
        if (f.isDir) {
            currentPath = f.path
        }
    }

    val getDesc: (Int) -> FileManagerDesc? = desc@{ index ->
        val i = repository.info(paths[index].path) ?: return@desc null
        return@desc FileManagerDesc(
            name = i.name,
            info = i.size.toString(),
            path = i.path,
            createTime = i.createTime,
        )
    }
}
