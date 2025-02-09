package cn.allin

import js.coroutines.internal.IsolatedCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import react.RefObject
import react.useEffectWithCleanup
import react.useRef
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.createInstance

abstract class ViewModel {
    val viewModelScope = IsolatedCoroutineScope()

    open fun onCleared() {
        viewModelScope.cancel()
    }
}

fun useCoroutineScope(): RefObject<CoroutineScope> {
    val c = useRef<CoroutineScope>()
    useEffectWithCleanup {
        c.current = IsolatedCoroutineScope()
        onCleanup {
            c.current?.cancel()
        }
    }
    return c
}


inline operator fun <T: Any>  RefObject<T>.getValue(
    thisRef: Nothing?,
    property: KProperty<*>,
): T?{
    return current
}


@OptIn(ExperimentalJsReflectionCreateInstance::class)
fun <VM : ViewModel> createViewModel(clazz: KClass<VM>): RefObject<VM> {
    val v = useRef<VM>()
    useEffectWithCleanup {
        v.current = clazz.createInstance()
        onCleanup {
            v.current?.onCleared()
        }
    }
    return v
}


inline fun <reified VM : ViewModel> useViewModel() = createViewModel(VM::class)


