package cn.allin

import ant.ConfigProvider
import ant.localeZhCN
import cn.allin.ui.NavAuth
import react.create
import react.dom.client.createRoot
import web.dom.document


fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find root container!")

    createRoot(container)
        .render(mainFc)

}


private val mainFc = ConfigProvider.create {
    locale = localeZhCN

    var auth = true
    if (auth) {
        NavApp()
    } else {
        NavAuth {
            auth = true
        }.invoke()
    }
}
