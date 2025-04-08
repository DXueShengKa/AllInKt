package cn.allin

import cn.allin.utils.DayLocalZhCn
import cn.allin.utils.dayjs
import muix.pickers.AdapterDayjs
import muix.pickers.LocalizationProvider
import react.create
import react.dom.client.createRoot
import react.router.RouterProvider
import toolpad.core.NotificationsProvider
import web.dom.document
import web.html.HTML.div


fun main() {
    val root = document.createElement(div)
    document.body.append(root)
    dayjs.locale(DayLocalZhCn)

    val reactElement = LocalizationProvider.create {
        dateAdapter = AdapterDayjs
        adapterLocale = "zh-cn"
        NotificationsProvider {
            RouterProvider {
                router = AppBrowserRouter
            }
        }
    }

    createRoot(root)
        .render(reactElement)
}
