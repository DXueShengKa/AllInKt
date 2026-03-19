package cn.allin.utils

import js.coroutines.internal.IsolatedCoroutineScope
import js.internal.InternalApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.promise
import kotlinx.coroutines.suspendCancellableCoroutine
import react.RefObject
import react.useEffect
import react.useEffectOnce
import react.useState
import kotlin.reflect.KProperty

@OptIn(InternalApi::class)
fun useCoroutineScope(): CoroutineScope {
    val coroutineScope = useRefInit { IsolatedCoroutineScope() }
    useEffectOnce {
        suspendCancellableCoroutine {
            coroutineScope.cancel()
        }
    }
    return coroutineScope
}

@OptIn(InternalApi::class)
fun asyncFunction(block: suspend CoroutineScope.() -> Unit): () -> dynamic =
    {
        IsolatedCoroutineScope().promise(
            start = CoroutineStart.UNDISPATCHED,
            block = block,
        )
    }

@OptIn(InternalApi::class)
fun <T> asyncFunction(block: suspend CoroutineScope.(T) -> Unit): (T) -> dynamic =
    { t ->
        IsolatedCoroutineScope().promise(
            start = CoroutineStart.UNDISPATCHED,
        ) {
            block(t)
        }
    }

@OptIn(InternalApi::class)
fun <T1, T2> asyncFunction(block: suspend CoroutineScope.(T1, T2) -> Unit): (T1, T2) -> dynamic =
    { t1, t2 ->
        IsolatedCoroutineScope().promise(
            start = CoroutineStart.UNDISPATCHED,
        ) {
            block(t1, t2)
        }
    }

operator fun <T : Any> RefObject<T>.getValue(
    thisRef: Nothing?,
    property: KProperty<*>,
): T = current!!

operator fun <T : Any> RefObject<T>.setValue(
    thisRef: Nothing?,
    property: KProperty<*>,
    value: T,
) {
    current = value
}

fun <T> Flow<T>.asState(init: T): T {
    var t by useState<T> { init }
    useEffect {
        collect {
            t = it
        }
    }
    return t
}
