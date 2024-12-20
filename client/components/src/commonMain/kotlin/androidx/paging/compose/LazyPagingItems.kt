package androidx.paging.compose


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.CombinedLoadStates
import androidx.paging.ItemSnapshotList
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.PagingDataEvent
import androidx.paging.PagingDataPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext



/**
 * The class responsible for accessing the data from a [Flow] of [PagingData].
 * In order to obtain an instance of [LazyPagingItems] use the [collectAsLazyPagingItems] extension
 * method of [Flow] with [PagingData].
 * This instance can be used for Lazy foundations such as [LazyListScope.items] to display data
 * received from the [Flow] of [PagingData].
 *
 * Previewing [LazyPagingItems] is supported on a list of mock data. See sample for how to preview
 * mock data.
 *
 * @sample androidx.paging.compose.samples.PagingPreview
 *
 * @param T the type of value used by [PagingData].
 */
public class LazyPagingItems<T : Any> internal constructor(
    /**
     * the [Flow] object which contains a stream of [PagingData] elements.
     */
    private val flow: Flow<PagingData<T>>
): PagingItems<T> {
    private val mainDispatcher = Dispatchers.Main

    /**
     * If the [flow] is a SharedFlow, it is expected to be the flow returned by from
     * pager.flow.cachedIn(scope) which could contain a cached PagingData. We pass the cached
     * PagingData to the presenter so that if the PagingData contains cached data, the presenter
     * can be initialized with the data prior to collection on pager.
     */
    private val pagingDataPresenter = object : PagingDataPresenter<T>(
        mainContext = mainDispatcher,
        cachedPagingData =
        if (flow is SharedFlow<PagingData<T>>) flow.replayCache.firstOrNull() else null
    ) {
        override suspend fun presentPagingDataEvent(
            event: PagingDataEvent<T>,
        ) {
            updateItemSnapshotList()
        }
    }

    /**
     * Contains the immutable [ItemSnapshotList] of currently presented items, including any
     * placeholders if they are enabled.
     * Note that similarly to [peek] accessing the items in a list will not trigger any loads.
     * Use [get] to achieve such behavior.
     */
    var itemSnapshotList by mutableStateOf(
        pagingDataPresenter.snapshot()
    )
        private set

    /**
     * The number of items which can be accessed.
     */
    override val itemCount: Int get() = itemSnapshotList.size

    private fun updateItemSnapshotList() {
        itemSnapshotList = pagingDataPresenter.snapshot()
    }

    /**
     * Returns the presented item at the specified position, notifying Paging of the item access to
     * trigger any loads necessary to fulfill prefetchDistance.
     *
     * @see peek
     */
    override operator fun get(index: Int): T? {
        pagingDataPresenter[index] // this registers the value load
        return itemSnapshotList[index]
    }

    /**
     * Returns the presented item at the specified position, without notifying Paging of the item
     * access that would normally trigger page loads.
     *
     * @param index Index of the presented item to return, including placeholders.
     * @return The presented item at position [index], `null` if it is a placeholder
     */
    override fun peek(index: Int): T? {
        return itemSnapshotList[index]
    }

    /**
     * Retry any failed load requests that would result in a [LoadState.Error] update to this
     * [LazyPagingItems].
     *
     * Unlike [refresh], this does not invalidate [PagingSource], it only retries failed loads
     * within the same generation of [PagingData].
     *
     * [LoadState.Error] can be generated from two types of load requests:
     *  * [PagingSource.load] returning [PagingSource.LoadResult.Error]
     *  * [RemoteMediator.load] returning [RemoteMediator.MediatorResult.Error]
     */
    fun retry() {
        pagingDataPresenter.retry()
    }

    /**
     * Refresh the data presented by this [LazyPagingItems].
     *
     * [refresh] triggers the creation of a new [PagingData] with a new instance of [PagingSource]
     * to represent an updated snapshot of the backing dataset. If a [RemoteMediator] is set,
     * calling [refresh] will also trigger a call to [RemoteMediator.load] with [LoadType] [REFRESH]
     * to allow [RemoteMediator] to check for updates to the dataset backing [PagingSource].
     *
     * Note: This API is intended for UI-driven refresh signals, such as swipe-to-refresh.
     * Invalidation due repository-layer signals, such as DB-updates, should instead use
     * [PagingSource.invalidate].
     *
     * @see PagingSource.invalidate
     */
    override fun refresh() {
        pagingDataPresenter.refresh()
    }

    /**
     * A [CombinedLoadStates] object which represents the current loading state.
     */
    public override var loadState: CombinedLoadStates by mutableStateOf(
        pagingDataPresenter.loadStateFlow.value
            ?: CombinedLoadStates(
                refresh = InitialLoadStates.refresh,
                prepend = InitialLoadStates.prepend,
                append = InitialLoadStates.append,
                source = InitialLoadStates
            )
    )
        private set

    internal suspend fun collectLoadState() {
        pagingDataPresenter.loadStateFlow.filterNotNull().collect {
            loadState = it
        }
    }

    internal suspend fun collectPagingData() {
        flow.collectLatest {
            pagingDataPresenter.collectFrom(it)
        }
    }

    companion object
}

private val IncompleteLoadState = LoadState.NotLoading(false)
private val InitialLoadStates = LoadStates(
    LoadState.Loading,
    IncompleteLoadState,
    IncompleteLoadState
)

@Composable
public fun <T : Any> Flow<PagingData<T>>.collectAsLazyPagingItems(
    context: CoroutineContext = EmptyCoroutineContext
): LazyPagingItems<T> {
    val lazyPagingItems = remember(this) { LazyPagingItems(this) }

    LaunchedEffect(lazyPagingItems) {

        launch(context) { lazyPagingItems.collectPagingData() }

        if (context == EmptyCoroutineContext){
            lazyPagingItems.collectLoadState()
        } else {
            withContext(context){
                lazyPagingItems.collectLoadState()
            }
        }
    }

    return lazyPagingItems
}

public inline fun <T : Any> LazyListScope.itemsNotNull(
    items: PagingItems<T>,
    noinline key: ((item: T) -> Any)? = null,
    crossinline itemContent: @Composable LazyItemScope.(value: T) -> Unit
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key)
    ) { index ->
        items[index]?.let {
            itemContent(it)
        }
    }
}

public inline fun <T : Any> LazyListScope.items(
    items: PagingItems<T>,
    noinline key: ((item: T) -> Any)? = null,
    crossinline itemContent: @Composable LazyItemScope.(value: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key)
    ) { index ->
        itemContent(items[index])
    }
}


public inline fun <T : Any> LazyListScope.itemsIndexed(
    items: PagingItems<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    crossinline itemContent: @Composable LazyItemScope.(index: Int, value: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key)
    ) { index ->
        itemContent(index, items[index])
    }
}


internal expect fun getPagingPlaceholderKey(index: Int): Any



fun <T : Any> Flow<PagingData<T>>.collectAsLazyPagingItems(coroutineScope: CoroutineScope): LazyPagingItems<T> {
    val lazyPagingItems  = LazyPagingItems(this)

    coroutineScope.launch {
        lazyPagingItems.collectPagingData()
    }

    coroutineScope.launch {
        lazyPagingItems.collectLoadState()
    }

    return lazyPagingItems
}


@ExperimentalFoundationApi
inline fun <T : Any> LazyStaggeredGridScope.items(
    items: PagingItems<T>,
    noinline key: ((item: T) -> Any)? = null,
    crossinline itemContent: @Composable LazyStaggeredGridItemScope.(value: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key)
    ) { index ->
        itemContent(items[index])
    }
}

@ExperimentalFoundationApi
inline fun <T : Any> LazyStaggeredGridScope.itemsIndexed(
    items: PagingItems<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    crossinline itemContent: @Composable LazyStaggeredGridItemScope.(index: Int, value: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key)
    ) { index ->
        itemContent(index, items[index])
    }
}


inline fun <T : Any> LazyGridScope.items(
    items: PagingItems<T>,
    noinline key: ((item: T) -> Any)? = null,
    crossinline itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key)
    ) { index ->
        itemContent(items[index])
    }
}

inline fun <T : Any> LazyGridScope.itemsIndexed(
    items: PagingItems<T>,
    noinline key: ((index: Int, item: T) -> Any)? = null,
    crossinline itemContent: @Composable LazyGridItemScope.(index: Int, value: T?) -> Unit
) {
    items(
        count = items.itemCount,
        key = items.itemKey(key)
    ) { index ->
        itemContent(index, items[index])
    }
}

/**
 * Returns a factory of stable and unique keys representing the item.
 *
 * Keys are generated with the key lambda that is passed in. If null is passed in, keys will
 * default to a placeholder key. If [PagingConfig.enablePlaceholders] is true,
 * LazyPagingItems may return null items. Null items will also automatically default to
 * a placeholder key.
 *
 * This factory can be applied to Lazy foundations such as [LazyGridScope.items] or Pagers.
 * Examples:
 * @sample androidx.paging.compose.samples.PagingWithHorizontalPager
 * @sample androidx.paging.compose.samples.PagingWithLazyGrid
 *
 * @param [key] a factory of stable and unique keys representing the item. Using the same key
 * for multiple items in the list is not allowed. Type of the key should be saveable
 * via Bundle on Android. When you specify the key the scroll position will be maintained
 * based on the key, which means if you add/remove items before the current visible item the
 * item with the given key will be kept as the first visible one.
 */
public fun <T : Any> PagingItems<T>.itemKey(
    key: ((item: T) -> Any)? = null
): ((index: Int) -> Any)? {
    if (key == null) return null
    return { index ->
        val item = peek(index)
        if (item == null) getPagingPlaceholderKey(index) else key(item)
    }
}

public fun <T : Any> PagingItems<T>.itemKey(
    key: ((index: Int, item: T) -> Any)? = null
): ((index: Int) -> Any)? {
    if (key == null) return null
    return { index ->
        val item = peek(index)
        if (item == null) getPagingPlaceholderKey(index) else key(index, item)
    }
}

/**
 * Returns a factory for the content type of the item.
 *
 * ContentTypes are generated with the contentType lambda that is passed in. If null is passed in,
 * contentType of all items will default to `null`.
 * If [PagingConfig.enablePlaceholders] is true, LazyPagingItems may return null items. Null
 * items will automatically default to placeholder contentType.
 *
 * This factory can be applied to Lazy foundations such as [LazyGridScope.items] or Pagers.
 * Examples:
 * @sample androidx.paging.compose.samples.PagingWithLazyGrid
 * @sample androidx.paging.compose.samples.PagingWithLazyList
 *
 * @param [contentType] a factory of the content types for the item. The item compositions of
 * the same type could be reused more efficiently. Note that null is a valid type and items of
 * such type will be considered compatible.
 */
public fun <T : Any> PagingItems<T>.itemContentType(
    contentType: ((item: T) -> Any?)? = null
): (index: Int) -> Any? {
    return { index ->
        if (contentType == null) {
            null
        } else {
            val item = peek(index)
            if (item == null) LazyPagingItems else contentType(item)
        }
    }
}

public fun <T : Any> PagingItems<T>.itemContentType(
    contentType: ((index: Int, item: T) -> Any?)? = null
): (index: Int) -> Any? {
    return { index ->
        if (contentType == null) {
            null
        } else {
            val item = peek(index)
            if (item == null) LazyPagingItems else contentType(index, item)
        }
    }
}