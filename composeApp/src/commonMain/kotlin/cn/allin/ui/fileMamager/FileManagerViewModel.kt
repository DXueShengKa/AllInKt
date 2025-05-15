package cn.allin.ui.fileMamager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.allin.api.ApiFile
import cn.allin.ui.components.ImageUrl
import cn.allin.ui.fileManager.FileManagerDesc
import cn.allin.ui.fileManager.FileManagerItem
import cn.allin.vo.FilePathVO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class FileManagerViewModel(
    private val repository: ApiFile
) : ViewModel() {

    val fileListFlow = MutableStateFlow<List<FileManagerItem>>(emptyList())

    init {
        list(null)
    }

    private var paths: List<FilePathVO> = emptyList()

    var currentPath: FilePathVO? = null

    private fun list(pathId: Int?) {
        viewModelScope.launch {
            val pathRepository = repository.list(pathId)

           currentPath = if (pathRepository.parentId == null) null else pathRepository

            val list = ArrayList<FileManagerItem>(paths.size)
            pathRepository.childs?.forEach {
                list += FileManagerItem(
                    img = ImageUrl.ImageUrlDefault,
                    type = FileManagerItem.FILE_TYPE_DIR,
                    name = it.path,
                    desc = "----"
                )
            }
            pathRepository.fileList?.forEach {
                list += FileManagerItem(
                    img = ImageUrl.ImageUrlDefault,
                    type = FileManagerItem.FILE_TYPE_FILE,
                    name = it.name,
                    desc = it.createTime.toString()
                )
            }

            paths = pathRepository.childs ?: emptyList()
            fileListFlow.value = list
        }
    }

    fun newDir(newPath: String){
        viewModelScope.launch {
            val pathId = currentPath?.id ?: return@launch
            repository.newDir(FilePathVO(
                id = 0,
                parentId = pathId,
                path = newPath,
            )).onRight {
                list(pathId)
            }
        }
    }

    fun previous() {

        list(currentPath?.parentId)
    }

    fun next(index: Int) {
        if (index < paths.size) {
            currentPath = paths[index]
            list(currentPath?.id)
        }
    }

    val getDesc: (Int) -> FileManagerDesc? = desc@{ index ->
        null
//        val i = repository.info(paths[index].path) ?: return@desc null
//        return@desc FileManagerDesc(
//            name = i.name,
//            info = i.size.toString(),
//            path = i.path,
//            createTime = i.createTime,
//        )
    }
}
