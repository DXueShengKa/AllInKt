package cn.allin.utils

import js.coroutines.internal.IsolatedCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import react.RefObject
import react.useEffect
import react.useEffectOnceWithCleanup
import react.useRef
import react.useState
import kotlin.reflect.KProperty


fun useCoroutineScope(): RefObject<CoroutineScope> {
    val c = useRef<CoroutineScope>(IsolatedCoroutineScope())
    useEffectOnceWithCleanup {
        onCleanup {
            c.current?.cancel()
        }
    }
    return c
}


operator fun <T : Any> RefObject<T>.getValue(
    thisRef: Nothing?,
    property: KProperty<*>,
): T {
    return current!!
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



