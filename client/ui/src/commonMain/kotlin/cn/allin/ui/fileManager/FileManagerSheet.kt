@file:OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)

package cn.allin.ui.fileManager

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.automirrored.sharp.More
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material.icons.sharp.Details
import androidx.compose.material.icons.sharp.DriveFileMoveRtl
import androidx.compose.material.icons.sharp.DriveFileRenameOutline
import androidx.compose.material.icons.sharp.FileDownload
import androidx.compose.material.icons.sharp.FileUpload
import androidx.compose.material.icons.sharp.Image
import androidx.compose.material.icons.sharp.MyLocation
import androidx.compose.material.icons.sharp.PostAdd
import androidx.compose.material.icons.sharp.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import cn.allin.theme.MainIcons
import cn.allin.theme.MainIconsAutoMirrored
import cn.allin.ui.components.FillScreenButton
import cn.allin.ui.components.ImageUrl
import cn.allin.ui.components.NetImage

@Composable
fun FileManagerOptions(
    isDir: Boolean,
    onDown: () -> Unit,
    onMove: () -> Unit,
    onDelete: () -> Unit,
    onRename: () -> Unit,
    onDesc: () -> Unit,
) {
    FlowRow(
        Modifier.fillMaxWidth().padding(16.dp),
        maxItemsInEachRow = 5,
        maxLines = 2
    ) {
        OptionsItem(ImageUrl(MainIcons.FileDownload), "下载", onDown)
        OptionsItem(ImageUrl(MainIcons.DriveFileMoveRtl), "移动", onMove)
        OptionsItem(ImageUrl(MainIcons.Delete), "删除", onDelete)
        OptionsItem(ImageUrl(MainIcons.DriveFileRenameOutline), "重命名", onRename)
        OptionsItem(ImageUrl(MainIcons.Details), "查看详情", onDesc)
        if (isDir) {
            OptionsItem(ImageUrl(MainIconsAutoMirrored.More), "其他应用打开") {}
            Spacer(Modifier.weight(4f))
        }
    }
}

@Composable
private fun FlowRowScope.OptionsItem(
    image: ImageUrl,
    title: String,
    onClick: () -> Unit,
) {
    Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
        NetImage(image, null, Modifier.size(58.dp).clickable(onClick = onClick))
        Text(title, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
internal fun FileManagerNewAdd(
    state: FileManagerState,
) {
    FlowRow(
        Modifier.fillMaxWidth().padding(16.dp),
        maxItemsInEachRow = 3,
        maxLines = 1
    ) {
        OptionsItem(ImageUrl(MainIcons.Image), "上传照片") {}
        OptionsItem(ImageUrl(MainIcons.FileUpload), "选择文件") {}
        OptionsItem(ImageUrl(MainIcons.PostAdd), "新建文件夹", state::openNewDir)
    }
}


@Composable
internal fun ColumnScope.FileManagerMove(moveList: List<FileManagerItem>) {
    Text("移动至", Modifier.padding(start = 16.dp))
    FileManagerList(
        Modifier.height(500.dp),
        PaddingValues(16.dp),
        moveList,
        onOptionClick = {},
        onClick = {}
    )
    FillScreenButton("确认", {})
}

@Composable
internal fun ColumnScope.FileManagerRename(
    item: FileManagerItem,
    hide: () -> Unit,
    onValue: (String) -> Unit
) {

    if (item.type == FileManagerItem.FILE_TYPE_DIR) {
        FileManagerEdit(
            title = "重命名文件夹",
            name = "文件夹名称",
            description = "文件夹简介",
            hide = hide,
            onValue = onValue,
        )
    } else {
        FileManagerEdit(
            title = "重命名文件",
            name = "文件名称",
            description = "文件简介",
            hide = hide,
            onValue = onValue,
        )
    }
}


@Composable
internal fun ColumnScope.FileManagerNewDir(
    hide: () -> Unit,
    onValue: (String) -> Unit
) {

    FileManagerEdit(
        title = "新建文件夹",
        name = "文件夹名称",
        description = "文件夹简介",
        hide = hide,
        onValue = onValue,
    )

}

@Composable
private fun FileManagerEdit(
    title: String,
    name: String,
    description: String,
    hide: () -> Unit,
    onValue: (String) -> Unit,
) {
    val nameValue = remember { mutableStateOf("") }
    DisposableEffect(title){
        onDispose {
            println("DisposableEffect")
        }
    }
    Text(title, Modifier.padding(start = 16.dp))
//    Text(name, style = MaterialTheme.typography.labelSmall)
    OutlinedTextField(
        nameValue.value, { nameValue.value = it },
        Modifier.fillMaxWidth(),
        label = { Text(name) },
    )
    Text(description, style = MaterialTheme.typography.labelSmall)
    OutlinedTextField(
        "", {},
        Modifier.fillMaxWidth(),
    )
    FillScreenButton("确认", {
        onValue(nameValue.value)
        hide()
    })
}

@Immutable
class FileManagerDesc(
    val name: String,
    val info: String,
    val path: String,
    val createTime: String,
)

@Composable
internal fun FileManagerDesc(
    desc: FileManagerDesc,
) {
    DescItem(MainIcons.Image, desc.path, desc.info)
    DescItem(MainIcons.MyLocation, "位置", desc.path)
    DescItem(MainIcons.Timer, "创建时间", desc.createTime)
    DescItem(MainIcons.Timer, "最后修改时间", desc.createTime)
}

@Composable
internal fun DescItem(
    image: ImageVector,
    title: String,
    subtitle: String,
) {
    Row(
        Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(image, null, Modifier.size(30.dp))
        val color = MaterialTheme.colorScheme.onSurface
        val style = MaterialTheme.typography.labelSmall
        Text(buildAnnotatedString {
            append(title)
            append('\n')
            val i = pushStyle(style.toParagraphStyle())
            pushStyle(SpanStyle(color))
            append(subtitle)
            pop(i)
        })
    }
}
