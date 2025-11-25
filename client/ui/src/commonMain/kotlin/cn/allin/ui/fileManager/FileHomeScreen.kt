package cn.allin.ui.fileManager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import cn.allin.ui.*
import cn.allin.ui.components.ImageUrl
import cn.allin.ui.components.NetImage
import cn.allin.ui.components.TopBar
import kotlinx.coroutines.flow.StateFlow
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberFileManagerState(
    fileListFlow: StateFlow<List<FileManagerItem>>,
    onBack: () -> Unit,
    onItemClick: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    onDown: (Int) -> Unit,
    getDesc: suspend (Int) -> FileManagerDesc?,
    onNewDir: (String) -> Unit,
): FileManagerState {

    val bottomSheetState = rememberStandardBottomSheetState(
        SheetValue.Hidden,
        skipHiddenState = false,
    )
    val scope = rememberCoroutineScope()
    return remember(bottomSheetState) {

        FileManagerState(
            BottomSheetScaffoldState(bottomSheetState, SnackbarHostState()),
            fileListFlow,
            scope,
            onBack,
            onItemClick,
            onDelete,
            onDown,
            getDesc,
            onNewDir,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileHomeScreen(
    fileManagerState: FileManagerState,
) {
    BottomSheetScaffold(
        scaffoldState = fileManagerState.scaffoldState,
        topBar = {
            FileTopBar(fileManagerState)
        },
        sheetTonalElevation = 1.dp,
        sheetDragHandle = null,
        sheetContent = {
            when (fileManagerState.sheetType) {
                FileManagerState.SHEET_TYPE_OPTIONS -> {
                    FileManagerOptions(
                        isDir = fileManagerState.selectItem?.type == FileManagerItem.FILE_TYPE_DIR,
                        onMove = fileManagerState::openMove,
                        onDelete = fileManagerState::onDelete,
                        onDesc = fileManagerState::openDesc,
                        onDown = fileManagerState::onDown,
                        onRename = fileManagerState::openRename,
                    )
                }

                FileManagerState.SHEET_TYPE_NEW_ADD -> {
                    FileManagerNewAdd(state = fileManagerState)
                }

                FileManagerState.SHEET_TYPE_NEW_DIR -> {
                    FileManagerNewDir(fileManagerState.closeSheet,fileManagerState.onNewDir)
                }

                FileManagerState.SHEET_TYPE_MOVE -> {
                    FileManagerMove(fileManagerState.fileItems)
                }

                FileManagerState.SHEET_TYPE_RENAME -> {
                    fileManagerState.selectItem?.also {
                        FileManagerRename(it,fileManagerState.closeSheet){}
                    }
                }

                FileManagerState.SHEET_TYPE_DESC -> {
                    fileManagerState.fileManagerDesc?.also {
                        FileManagerDesc(it)
                    }
                }

            }
            Spacer(Modifier.height(16.dp))
        }
    ) { innerPadding ->
        Box(Modifier.padding(innerPadding)) {
            FileManagerList(
                Modifier.fillMaxSize(),
                innerPadding,
                fileManagerState.fileItems,
                onOptionClick = fileManagerState::openOption,
                onClick = fileManagerState.onItemClick
            )

            FloatingActionButton(
                onClick = fileManagerState::openAdd,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 30.dp, bottom = 30.dp),
            ) {
                Icon(painterResource(Res.drawable.add), contentDescription = null)
            }
        }
    }
}


@Composable
private fun FileTopBar(state: FileManagerState) {
    Column {
        TopBar(state.onBack) {
            Text("文件管理")
        }
        OutlinedTextField(
            state.searchFile,
            { state.searchFile = it },
            Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            leadingIcon = {
                Icon(painterResource(Res.drawable.search), contentDescription = null)
            },
            placeholder = {
                Text("请输入查询文件的名称")
            },
            colors = TextFieldDefaults.colors()
        )
    }
}

@Immutable
class FileManagerItem(
    val img: ImageUrl,
    val type: Byte = FILE_TYPE_FILE,
    val name: String,
    val desc: String,
) {
    companion object {
        const val FILE_TYPE_DIR: Byte = 0
        const val FILE_TYPE_FILE: Byte = 1
        const val FILE_TYPE_PREVIEW: Byte = 2
    }
}

@Composable
fun FileManagerList(modifier: Modifier, innerPadding: PaddingValues, fileItems: List<FileManagerItem>, onOptionClick: (Int) -> Unit, onClick: (Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = innerPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(fileItems) { index, item ->
            FileManagerItem(
                item,
                onOptionClick = { onOptionClick(index) },
                onClick = {
                    when (item.type) {
                        FileManagerItem.FILE_TYPE_FILE -> onOptionClick(index)
                        else -> onClick(index)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FileManagerItem(data: FileManagerItem, onOptionClick: () -> Unit, onClick: () -> Unit) {

    Box(Modifier.fillMaxWidth()) {
        Column(
            Modifier
                .background(MaterialTheme.colorScheme.surface)
                .clip(MaterialTheme.shapes.small)
                .combinedClickable(
                    onLongClick = onOptionClick,
                    onClick = onClick
                )
        ) {
            Box(Modifier.fillMaxSize().height(140.dp).background(MaterialTheme.colorScheme.surfaceContainer)) {
                when (data.type) {
                    FileManagerItem.FILE_TYPE_DIR -> {
                        Image(painterResource(Res.drawable.folder), null, Modifier.fillMaxSize())
                    }

                    FileManagerItem.FILE_TYPE_FILE -> {
                        Image(painterResource(Res.drawable.files), null, Modifier.fillMaxSize())
                    }

                    else -> NetImage(
                        data.img,
                        null,
                        Modifier.fillMaxSize()
                    )
                }

            }
            val color = MaterialTheme.colorScheme.onSurface
            val style = MaterialTheme.typography.labelSmall
            val text = buildAnnotatedString {
                append(data.name)
                append('\n')
                val i = pushStyle(style = style.toParagraphStyle())
                pushStyle(SpanStyle(color = color))
                append(data.desc)
                pop(i)
            }
            Text(text, Modifier.padding(16.dp, 12.dp, 16.dp, 0.dp))
        }
        Checkbox(
            false,
            onCheckedChange = {},
            Modifier.size(24.dp).align(Alignment.TopEnd).padding(top = 8.dp, end = 8.dp),
        )
    }
}

