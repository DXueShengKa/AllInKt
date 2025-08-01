package cn.allin.utils

import js.coroutines.internal.IsolatedCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.promise
import react.RefObject
import react.useEffect
import react.useEffectWithCleanup
import react.useState
import kotlin.reflect.KProperty


fun useCoroutineScope(): CoroutineScope {
    val coroutineScope = useRefInit { IsolatedCoroutineScope() }
    useEffectWithCleanup(coroutineScope){
        onCleanup(coroutineScope::cancel)
    }
    return coroutineScope
}

fun asyncFunction(block: suspend CoroutineScope.() -> Unit): () -> dynamic {
    return {
        IsolatedCoroutineScope().promise(
            start = CoroutineStart.UNDISPATCHED,
            block = block
        )
    }
}

fun <T> asyncFunction(block: suspend CoroutineScope.(T) -> Unit): (T) -> dynamic {
    return { t ->
        IsolatedCoroutineScope().promise(
            start = CoroutineStart.UNDISPATCHED,
        ) {
            block(t)
        }
    }
}

fun <T1,T2> asyncFunction(block: suspend CoroutineScope.(T1, T2) -> Unit): (T1, T2) -> dynamic {
    return { t1,t2 ->
        IsolatedCoroutineScope().promise(
            start = CoroutineStart.UNDISPATCHED,
        ) {
            block(t1,t2)
        }
    }
}

operator fun <T : Any> RefObject<T>.getValue(
    thisRef: Nothing?,
    property: KProperty<*>,
): T {
    return current!!
}


operator fun <T : Any> RefObject<T>.setValue(thisRef: Nothing?, property: KProperty<*>, value: T) {
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



