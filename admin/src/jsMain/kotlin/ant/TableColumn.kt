package ant

import js.objects.JsoDsl
import js.objects.Object
import js.objects.jso
import react.ReactNode

external interface TableColumn<T> {
    var title: String
    var key: String
    var dataIndex: String
    var render: (T,dynamic,Int)->ReactNode
}

fun <T> tableColumn(block: @JsoDsl TableColumn<T>.() -> Unit): TableColumn<T> = jso(block)


fun <T : Any> T.keyName(block: (T) -> dynamic): String {
    Object.entries(this).forEach { (key, v) ->
        if (v.toString() == block(this).toString()) return key
    }
    error("读取属性失败 ${block(this)}")
}

