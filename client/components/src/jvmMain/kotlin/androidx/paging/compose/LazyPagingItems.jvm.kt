package androidx.paging.compose

import java.io.Serializable


internal actual fun getPagingPlaceholderKey(index: Int): Any = PagingPlaceholderKey(index)


private data class PagingPlaceholderKey(private val index: Int) : Serializable