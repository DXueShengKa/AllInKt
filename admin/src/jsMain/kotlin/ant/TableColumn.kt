package ant

import js.objects.JsoDsl
import js.objects.Object
import js.objects.jso
import react.ChildrenBuilder
import react.FC
import react.ReactNode
import react.create

external interface TableColumn<T> {
    var title: String
    var key: String
    var dataIndex: String
    var render: (T, dynamic, Int) -> ReactNode
}

fun <T> TableColumn<T>.renderKey(builder: ChildrenBuilder.(T) -> Unit) {
    render = { t, _, _ ->
        FC { builder(t) }.create()
    }
}

fun <T,D> TableColumn<T>.renderData(builder: ChildrenBuilder.(D) -> Unit) {
    render = { _ , d, _ ->
        FC { builder(d) }.create()
    }
}

fun <T> tableColumn(block: @JsoDsl TableColumn<T>.() -> Unit): TableColumn<T> = jso(block)


fun <T : Any> T.keyName(block: (T) -> dynamic): String {
    Object.entries(this).forEach { (key, v) ->
        if (v.toString() == block(this).toString()) return key
    }
    error("读取属性失败 ${block(this)}")
}

