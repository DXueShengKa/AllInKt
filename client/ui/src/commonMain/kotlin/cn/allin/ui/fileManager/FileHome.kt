package cn.allin.ui.fileManager

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.automirrored.sharp.InsertDriveFile
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.allin.theme.MainIcons
import cn.allin.theme.MainIconsAutoMirrored
import cn.allin.ui.components.TopBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberFileManagerState(
    onBack: () -> Unit,
): FileManagerState {
    val bottomSheetState = rememberStandardBottomSheetState(
        SheetValue.Hidden,
        skipHiddenState = false,
    )
    val scope = rememberCoroutineScope()
    return remember(bottomSheetState) {
        FileManagerState(
            BottomSheetScaffoldState(bottomSheetState,SnackbarHostState()),
            scope,
            onBack,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileHome(
    fileManagerState: FileManagerState
) {

    BottomSheetScaffold(
        scaffoldState = fileManagerState.scaffoldState,
        topBar = {
            FileTopBar(fileManagerState)
        },
        sheetContent = {
            when(fileManagerState.sheetType) {
                FileManagerState.SHEET_TYPE_OPTIONS -> {
                    FileManagerOptions()
                }

            }
        }
    ) { innerPadding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(10) {
                FileItem()
            }
        }
    }
}


@Composable
private fun FileTopBar(state:FileManagerState) {
    Column {
        TopBar(state.onBack) {
            Text("文件管理")
        }
        OutlinedTextField(
            state.searchFile,
            { state.searchFile = it },
            Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            leadingIcon = {
                Icon(MainIcons.Search, contentDescription = null)
            },
            placeholder = {
                Text("请输入查询文件的名称")
            },
            colors = TextFieldDefaults.colors()
        )
    }
}

@Composable
private fun FileItem() {
    Box(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column {
            Box(Modifier.fillMaxSize().height(140.dp)) {
                Image(
                    MainIconsAutoMirrored.InsertDriveFile, contentDescription = null,
                    Modifier.fillMaxSize()
                )
            }
            Text("照片")
            Text(
                "介绍",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelSmall
            )
        }
        Checkbox(
            false,
            onCheckedChange = {},
            Modifier.size(24.dp).align(Alignment.TopEnd)
        )
    }
}

