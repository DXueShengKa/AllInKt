package cn.allin

import react.create
import react.dom.client.createRoot
import web.dom.document


fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find root container!")

    createRoot(container)
        .render(App.create())

}