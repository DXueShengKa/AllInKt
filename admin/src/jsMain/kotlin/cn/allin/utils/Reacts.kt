package cn.allin.utils

import react.ChildrenBuilder
import react.FC
import react.ReactDsl
import react.ReactNode
import react.StateSetter
import react.create

/**
 * react的state set函数转kt的(T) -> Unit
 */
val <T> StateSetter<T>.invokeFn: (T) -> Unit
    get() = unsafeCast<(T) -> Unit>()


fun reactNode(block: @ReactDsl ChildrenBuilder.() -> Unit): ReactNode {
    return FC(block).create()
}

fun reactNode(string: String): ReactNode {
    return string.asDynamic()
}
