package cn.allin.ui.fileManager

import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Stable
class FileManagerState(
    val scaffoldState: BottomSheetScaffoldState,
    private val fileItemFlow: StateFlow<List<FileManagerItem>>,
    internal val cs: CoroutineScope,
    internal val onBack: () -> Unit,
    internal val onItemClick: (Int) -> Unit,
    private val onDelete: (Int) -> Unit,
    internal val onDown: (Int) -> Unit,
    internal val getDesc: suspend (Int) -> FileManagerDesc?,
    internal val onNewDir: (String) -> Unit,
) {


    companion object {
        const val SHEET_TYPE_OPTIONS = 0
        const val SHEET_TYPE_NEW_ADD = 1
        const val SHEET_TYPE_MOVE = 2
        const val SHEET_TYPE_RENAME = 3
        const val SHEET_TYPE_DESC = 4

        const val SHEET_TYPE_NEW_DIR = 5
    }


    val fileItems = mutableStateListOf<FileManagerItem>()

    init {
        val sheetStateFlow = snapshotFlow {
            scaffoldState.bottomSheetState.currentValue
        }
        cs.launch {
            sheetStateFlow.collect { sheetState ->
                selectItem == null
                fileManagerDesc = null
            }
        }

        val searchFlow = snapshotFlow {
            searchFile
        }

        cs.launch {
            searchFlow.map { searchName ->
                fileItemFlow.value.filter {
                    it.name.contains(searchName, ignoreCase = true)
                }
            }.collect {
                fileItems.clear()
                fileItems.addAll(it)
            }
        }

        cs.launch {
            fileItemFlow.collect {
                fileItems.clear()
                fileItems.addAll(it)
            }
        }
    }


    internal var sheetType by mutableIntStateOf(SHEET_TYPE_OPTIONS)

    internal var searchFile by mutableStateOf("")

    internal var selectItem: FileManagerItem? = null

    internal var fileManagerDesc: FileManagerDesc? = null

    private var selectIndex = -1

    internal fun onDelete() {
        onDelete(selectIndex)
    }

    internal fun onDown() {
        onDown(selectIndex)
    }

    internal fun openOption(index: Int) {
        selectItem = fileItems[index]
        selectIndex = index
        cs.launch {
            sheetType = SHEET_TYPE_OPTIONS
            scaffoldState.bottomSheetState.expand()
        }
    }

    internal fun openMove() {
        sheetType = SHEET_TYPE_MOVE
    }

    internal fun openRename() {
        sheetType = SHEET_TYPE_RENAME
    }


    internal fun openNewDir() {
        cs.launch {
            sheetType = SHEET_TYPE_NEW_DIR
            scaffoldState.bottomSheetState.expand()
        }
    }

    internal fun openAdd() {
        cs.launch {
            sheetType = SHEET_TYPE_NEW_ADD
            scaffoldState.bottomSheetState.expand()
        }
    }

    internal fun openDesc() {
        cs.launch {
            fileManagerDesc = getDesc(selectIndex)
            if (fileManagerDesc != null) {
                sheetType = SHEET_TYPE_DESC
                scaffoldState.bottomSheetState.expand()
            }
        }
    }

    internal val closeSheet: () -> Unit = {
        cs.launch {
            scaffoldState.bottomSheetState.hide()
        }
    }
}
