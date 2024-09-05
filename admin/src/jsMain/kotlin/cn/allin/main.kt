package cn.allin

import ant.ConfigProvider
import ant.LocaleProvider
import cn.allin.ui.NavAuth
import react.create
import react.dom.client.createRoot
import web.dom.document


//@JsModule("antd/locale")
//@JsName("default")
//@JsNonModule
//external val locale: dynamic




fun main() {
    val container = document.getElementById("root") ?: error("Couldn't find root container!")

//    println(dayjs.locale()

    LocaleProvider
//    println(locale.locale)
//    cnLocale.locale("zh-cn")
    createRoot(container)
        .render(mainFc)

}


private val mainFc = ConfigProvider.create {
//        locale.locale = "zh-CN"
//        locale = cnLocale

//        var auth: Boolean by useState(HeaderAuthorization != null)
var    auth = true
        if (auth) {
            NavApp()
        } else {
            NavAuth {
                auth = true
            }.invoke()
        }
    }
