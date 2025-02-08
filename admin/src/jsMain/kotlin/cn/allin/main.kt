package cn.allin

import cn.allin.ui.RouteUserListFC
import js.objects.jso
import mui.material.PaletteMode
import mui.material.styles.ThemeProvider
import mui.material.styles.createTheme
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
    val node = ThemeProvider.create {
        theme = createTheme(
            jso {
                palette = jso {
                    mode = PaletteMode.dark
                }
            },
            muiLocal.zhCN
        )
        QueryClientProvider {
            client = queryClient
//        MainUI()
            RouteUserListFC()
        }
    }
    createRoot(root)
        .render(node)
}