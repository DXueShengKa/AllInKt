package cn.allin.ui.fileManager

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cn.allin.ui.components.ImageUrl
import cn.allin.ui.components.NetImage
import cn.allin.ui.components.TopBar
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferManagerScreen(
    upload: TransferListData,
    download: TransferListData,
    onBack: () -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        TopBar(onBack) { Text("传输管理") }
        val cs = rememberCoroutineScope()
        val pagerState = rememberPagerState { 2 }
        SecondaryTabRow(
            pagerState.currentPage,
            Modifier.height(60.dp),
        ) {
            Tab(
                pagerState.currentPage == 0,
                onClick = {
                    cs.launch {
                        pagerState.animateScrollToPage(0)
                    }
                }
            ) {
                Text("上传")
            }
            Tab(
                pagerState.currentPage == 1,
                onClick = {
                    cs.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }
            ) {
                Text("下载")
            }
        }

        HorizontalPager(pagerState) { page ->
            when (page) {
                1 -> ManagerList("正在下载","下载完成",download)
                else -> ManagerList("正在上传", "上传完成", upload)
            }
        }
    }
}

@Stable
class TransferListData(
    val doingList: SnapshotStateList<TransferItemData>,
    val doneList: SnapshotStateList<TransferItemData>,
)


@Immutable
class TransferItemData(
    val name: String,
    val imageUrl: ImageUrl,
    transferState: String
) {
    var transferState by mutableStateOf(transferState)
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ManagerList(doing: String, done: String, listData: TransferListData) {
    LazyColumn {
        stickyHeader {
            Text(doing, color = MaterialTheme.colorScheme.onBackground)
        }
        itemsIndexed(listData.doingList) { index, item ->
            ManagerItem(item) {

            }
        }
        stickyHeader {
            Text(done, color = MaterialTheme.colorScheme.onBackground)
        }
        itemsIndexed(listData.doneList) { index, item ->
            ManagerItem(item) {

            }
        }
    }
}


@Composable
private fun ManagerItem(
    itemData: TransferItemData,
    onClick: () -> Unit,
) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = {
            Text(itemData.name)
        },
        leadingContent = {
            NetImage(itemData.imageUrl, null, Modifier.size(40.dp))
        },
        supportingContent = {
            Text(itemData.transferState)
        }
    )
}



