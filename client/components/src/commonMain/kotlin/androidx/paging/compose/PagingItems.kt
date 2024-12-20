package androidx.paging.compose

import androidx.compose.runtime.Stable
import androidx.paging.CombinedLoadStates

@Stable
interface PagingItems<T : Any> {

    val itemCount:Int

    operator fun get(index: Int): T?

    fun peek(index: Int): T?

    val loadState: CombinedLoadStates

    fun refresh()
}