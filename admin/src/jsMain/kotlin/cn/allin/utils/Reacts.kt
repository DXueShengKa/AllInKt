package cn.allin.utils

import react.ChildrenBuilder
import react.FC
import react.ReactDsl
import react.ReactNode
import react.create


fun reactNode(block: @ReactDsl ChildrenBuilder.() -> Unit): ReactNode {
    return FC(block).create()
}

fun reactNode(string: String): ReactNode {
    return string.asDynamic()
}
