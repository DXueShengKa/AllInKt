package com.logic.base

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


@Stable
class ComposeBaseState() {
    internal var _isShowLoading by mutableStateOf(false)

    private var loadingCount = 0

    fun showLoading() {
        loadingCount++
        _isShowLoading = loadingCount > 0
    }

    fun dismissLoading() {
        if (loadingCount > 0) loadingCount--
        _isShowLoading = loadingCount > 0
    }

    val onDismissLoading: () -> Unit = {
        _isShowLoading = false
    }




}
