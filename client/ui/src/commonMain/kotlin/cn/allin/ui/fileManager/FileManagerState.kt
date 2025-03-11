package cn.allin.ui.fileManager

import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Stable
class FileManagerState(
    val scaffoldState: BottomSheetScaffoldState,
    internal val cs: CoroutineScope,
    internal val onBack: () -> Unit,
) {

    companion object {
        const val SHEET_TYPE_OPTIONS = 0
    }

    internal var sheetType by mutableIntStateOf(SHEET_TYPE_OPTIONS)

    var searchFile by mutableStateOf("")

    fun open() {
        cs.launch {
            scaffoldState.bottomSheetState.expand()
        }
    }
}
