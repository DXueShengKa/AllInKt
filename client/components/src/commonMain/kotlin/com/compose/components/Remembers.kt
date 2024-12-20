package com.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.RememberObserver
import androidx.compose.runtime.SnapshotMutationPolicy
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * 对[mutableStateOf]封装了一层回调
 */
fun <T> callbackStateOf(value: T, callback: (T, T)->Boolean): MutableState<T> {
    return mutableStateOf(value, object : SnapshotMutationPolicy<T> {
        override fun equivalent(a: T, b: T): Boolean {
            return callback(a, b)
        }
    })
}

@Composable
@NonRestartableComposable
fun <T : Any> rememberOnDispose(scope: ValueDisposableScope<T>.() -> OnDispose): T {
    return remember { RememberOnDispose(scope) }.value
}

@Composable
@NonRestartableComposable
fun <T : Any> rememberOnDispose(key: Any?, scope: ValueDisposableScope<T>.() -> OnDispose): T {
    return remember(key) { RememberOnDispose(scope) }.value
}

@Composable
@NonRestartableComposable
fun <T : Any> rememberOnDispose(
    key1: Any?,
    key2: Any?,
    scope: ValueDisposableScope<T>.() -> OnDispose
): T {
    return remember(key1, key2) { RememberOnDispose(scope) }.value
}

@Composable
@NonRestartableComposable
fun <T : Any> rememberOnDispose(
    vararg keys: Any?,
    scope: ValueDisposableScope<T>.() -> OnDispose
): T {
    return remember(*keys) { RememberOnDispose(scope) }.value
}

class ValueDisposableScope<T : Any> {
    lateinit var value: T

    fun onDispose(
        onDispose: OnDispose
    ) = onDispose
}

private typealias OnDispose = () -> Unit

private class RememberOnDispose<T : Any>(
    scope: ValueDisposableScope<T>.() -> OnDispose
) : RememberObserver {
    val value: T
    val onDispose: OnDispose

    init {
        ValueDisposableScope<T>().apply {
            onDispose = scope(this)
            this@RememberOnDispose.value = value
        }
    }

    override fun onRemembered() {}

    override fun onForgotten() {
        onDispose()
    }

    override fun onAbandoned() {}
}
