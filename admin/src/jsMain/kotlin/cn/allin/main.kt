package cn.allin

import cn.allin.net.HeaderAuthorization
import cn.allin.ui.NavAuth
import react.FC
import react.create
import react.dom.client.createRoot
import react.useState
import web.dom.document


fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find root container!")

    createRoot(container)
        .render(mainFc)

}

private val mainFc = FC {
    var auth:Boolean by useState(HeaderAuthorization != null)
    if (auth){
        NavApp()
    } else {
        NavAuth {
            auth = true
        }.invoke()
    }
}.create()