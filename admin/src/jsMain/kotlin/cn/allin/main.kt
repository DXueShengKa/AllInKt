package cn.allin

import ant.App
import ant.ConfigProvider
import ant.DayLocalZhCn
import ant.dayjs
import ant.locale.AntLocaleZhCN
import cn.allin.net.HeaderAuthorization
import cn.allin.ui.NavAuth
import react.FC
import react.create
import react.dom.client.createRoot
import react.useState
import web.dom.document
import web.html.HTML.div


fun main() {
    val root = document.createElement(div)
    document.body.append(root)
    dayjs.locale(DayLocalZhCn)
    createRoot(root)
        .render(MainUI.create())

}


private val MainUI = FC {
    ConfigProvider {
        locale = AntLocaleZhCN
        App {

            var auth by useState(HeaderAuthorization != null)
            if (auth) {
                NavApp()
            } else {
                NavAuth {
                    auth = true
                }.invoke()
            }
        }
    }
}