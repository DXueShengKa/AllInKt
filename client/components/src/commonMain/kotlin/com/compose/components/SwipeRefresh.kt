package com.compose.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * 下拉刷新
 * [pullRefresh]
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeRefresh(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
//    scale: Boolean = false,
//    enabled: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {

    val state = rememberPullToRefreshState()
    PullToRefreshBox(
        modifier = modifier,
        state = state,
        onRefresh = onRefresh,
        isRefreshing = refreshing,
        content = content,
    )
}
