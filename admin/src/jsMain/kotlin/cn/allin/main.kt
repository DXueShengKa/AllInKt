package cn.allin

import cn.allin.utils.DayLocalZhCn
import cn.allin.utils.dayjs
import muix.pickers.AdapterDayjs
import muix.pickers.LocalizationProvider
import react.create
import react.dom.client.createRoot
import react.router.RouterProvider
import tanstack.query.core.QueryClient
import tanstack.react.query.QueryClientProvider
import web.dom.document
import web.html.HTML.div

private val queryClient = QueryClient()

fun main() {
    val root = document.createElement(div)
    document.body.append(root)
    dayjs.locale(DayLocalZhCn)

    val reactElement = QueryClientProvider.create {
        client = queryClient
        LocalizationProvider {
            dateAdapter = AdapterDayjs
            adapterLocale = "zh-cn"
            RouterProvider {
                router = AppBrowserRouter
            }
        }
    }

    createRoot(root)
        .render(reactElement)
}
