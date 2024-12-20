package com.compose.components

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class PagerState(
    initialPage: Int = 0,
    initialPageOffsetFraction: Float = 0f,
    override val pageCount: Int
) : PagerState(initialPage, initialPageOffsetFraction)


class PagerMutableState(
    initialPage: Int = 0,
    initialPageOffsetFraction: Float = 0f,
) : PagerState(initialPage, initialPageOffsetFraction) {

    var count by mutableIntStateOf(0)

    override val pageCount: Int
        get() = count
}
