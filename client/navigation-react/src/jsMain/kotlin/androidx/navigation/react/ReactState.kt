package androidx.navigation.react

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import react.StateInstance
import react.useEffect
import react.useState

fun <T : Any> Flow<T>.asState(initialValue: T): StateInstance<T> {
    val state = useState(initialValue)
    useEffect(this) {
        val set = state.component2()
        collect {
            set(it)
        }
    }
    return state
}


fun <T : Any> Flow<T>.asState(): StateInstance<T?> {
    val state = useState<T>()
    useEffect(this) {
        val set = state.component2()
        collect {
            set(it)
        }
    }
    return state
}


fun <T> StateFlow<T>.collectAsState(): StateInstance<T> {
    val state = useState(value)
    useEffect(this) {
        val set = state.component2()
        collect {
            set(it)
        }
    }
    return state
}