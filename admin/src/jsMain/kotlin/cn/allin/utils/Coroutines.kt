package cn.allin.utils

import js.coroutines.internal.IsolatedCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.promise
import react.RefObject
import react.useEffect
import react.useEffectOnceWithCleanup
import react.useRef
import react.useState
import kotlin.reflect.KProperty


fun useCoroutineScope(): RefObject<CoroutineScope> {
    val c = useRef(IsolatedCoroutineScope())
    useEffectOnceWithCleanup {
        onCleanup {
            c.current?.cancel()
        }
    }
    return c
}

fun asyncFunction(block: suspend CoroutineScope.() -> Unit): () -> dynamic {
    return {
        IsolatedCoroutineScope().promise(block = block)
    }
}

fun <T> asyncFunction(block: suspend CoroutineScope.(T) -> Unit): (T) -> dynamic {
    return { t ->
        IsolatedCoroutineScope().promise {
            block(t)
        }
    }
}

fun <T1,T2> asyncFunction(block: suspend CoroutineScope.(T1, T2) -> Unit): (T1, T2) -> dynamic {
    return { t1,t2 ->
        IsolatedCoroutineScope().promise {
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



