package com.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * 下拉刷新
 * [pullRefresh]
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeRefresh(
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    scale: Boolean = false,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable BoxScope.() -> Unit
) {

    val state = rememberPullRefreshState(refreshing, onRefresh)
    Box(modifier.pullRefresh(state,enabled)) {
        content()

        PullRefreshIndicator(
            refreshing,
            state,
            Modifier.align(Alignment.TopCenter),
            backgroundColor,
            contentColor,
            scale = scale
        )
    }
}