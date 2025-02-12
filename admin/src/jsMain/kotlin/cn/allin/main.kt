package cn.allin

import cn.allin.ui.RouteAddUserFC
import cn.allin.utils.DayLocalZhCn
import cn.allin.utils.dayjs
import js.objects.jso
import mui.material.PaletteMode
import mui.material.styles.ThemeProvider
import mui.material.styles.createTheme
import muix.pickers.AdapterDayjs
import muix.pickers.LocalizationProvider
import react.create
import react.dom.client.createRoot
import tanstack.query.core.QueryClient
import tanstack.react.query.QueryClientProvider
import web.dom.document
import web.html.HTML.div

private val queryClient = QueryClient()

fun main() {
    val root = document.createElement(div)
    document.body.append(root)
    dayjs.locale(DayLocalZhCn)
    val node = ThemeProvider.create {
        theme = createTheme(
            jso {
                palette = jso {
                    mode = PaletteMode.light
                }
            },
            muiLocal.zhCN
        )
        QueryClientProvider {
            client = queryClient
//            RouteUserListFC()
            LocalizationProvider {
                dateAdapter = AdapterDayjs
                adapterLocale = "zh-cn"
                RouteAddUserFC()
            }
        }
    }
    createRoot(root)
        .render(node)
}