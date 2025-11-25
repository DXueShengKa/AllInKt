package cn.allin.ui.fileMamager

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.allin.data.repository.FileRepository
import cn.allin.ui.Res
import cn.allin.ui.components.ImageUrl
import cn.allin.ui.fileManager.TransferItemData
import cn.allin.ui.fileManager.TransferListData
import cn.allin.ui.file_save
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class TransferManagerViewModel(
    private val fileRepository: FileRepository,
):ViewModel() {

    val l = Array(6) {
        TransferItemData(
            Random.nextInt().toString(),
            ImageUrl(Res.drawable.file_save),
            "下载"
        )
    }

    val upload = TransferListData(
        doingList = mutableStateListOf(*l),
        doneList = mutableStateListOf(*l),
    )

    val download = TransferListData(
        doingList = mutableStateListOf(*l),
        doneList = mutableStateListOf(*l),
    )

    init {
        viewModelScope.launch {
            while (isActive) {
                delay(500)
                l[Random.nextInt(0,l.size)].transferState = Random.nextInt().toString()
            }
        }
    }
}
