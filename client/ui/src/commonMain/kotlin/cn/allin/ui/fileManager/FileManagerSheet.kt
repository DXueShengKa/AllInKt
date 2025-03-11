package cn.allin.ui.fileManager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.automirrored.sharp.More
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material.icons.sharp.Details
import androidx.compose.material.icons.sharp.DriveFileMoveRtl
import androidx.compose.material.icons.sharp.DriveFileRenameOutline
import androidx.compose.material.icons.sharp.FileDownload
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.allin.theme.MainIcons
import cn.allin.theme.MainIconsAutoMirrored
import cn.allin.ui.components.ImageUrl
import cn.allin.ui.components.NetImage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FileManagerOptions() {
    FlowRow(
        Modifier.fillMaxWidth(),
        maxItemsInEachRow = 5,
        maxLines = 2
    ) {
        OptionsItem(ImageUrl(MainIcons.FileDownload),"下载"){}
        OptionsItem(ImageUrl(MainIcons.DriveFileMoveRtl),"移动"){}
        OptionsItem(ImageUrl(MainIcons.Delete),"删除"){}
        OptionsItem(ImageUrl(MainIcons.DriveFileRenameOutline),"重命名"){}
        OptionsItem(ImageUrl(MainIcons.Details),"查看详情"){}
        OptionsItem(ImageUrl(MainIconsAutoMirrored.More),"其他应用打开"){}
        Spacer(Modifier.weight(4f))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRowScope.OptionsItem(
    image: ImageUrl,
    title: String,
    onClick: () -> Unit,
){
    Column(Modifier.weight(1f),horizontalAlignment = Alignment.CenterHorizontally) {
        NetImage(image,null,Modifier.size(58.dp).clickable(onClick = onClick))
        Text(title,style = MaterialTheme.typography.labelSmall)
    }
}
