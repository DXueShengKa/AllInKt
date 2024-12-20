package androidx.paging.compose

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates

class ListPagingItems<T : Any>(
    val list: List<T>
) : PagingItems<T> {

    private val incompleteLoadState = LoadState.NotLoading(false)

    private val initialLoadStates = LoadStates(
        LoadState.NotLoading(true),
        incompleteLoadState,
        incompleteLoadState
    )

    override val loadState: CombinedLoadStates = CombinedLoadStates(
        refresh = initialLoadStates.refresh,
        prepend = initialLoadStates.prepend,
        append = initialLoadStates.append,
        source = initialLoadStates
    )

    override fun refresh() { }

    override val itemCount: Int
        get() = list.size

    override fun get(index: Int): T {
        return list[index]
    }

    override fun peek(index: Int): T {
        return list[index]
    }
}