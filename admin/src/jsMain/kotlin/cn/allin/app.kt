package cn.allin


import cn.allin.ui.RouteAddUser
import cn.allin.ui.RouteAddUserFC
import cn.allin.ui.RouteUserList
import cn.allin.ui.RouteUserListFC
import js.objects.jso
import mui.material.PaletteMode
import mui.material.styles.createTheme
import react.FC
import react.create
import react.router.Outlet
import react.router.RouteObject
import react.router.dom.createBrowserRouter
import react.router.useRouteError
import toolpad.core.DashboardLayout
import toolpad.core.PageContainer
import toolpad.core.react_router.ReactRouterAppProvider


private val AppLayout = FC {
    ReactRouterAppProvider {
        theme = createTheme(
            jso {
                palette = jso {
                    mode = PaletteMode.light
                }
            },
            muiLocal.zhCN
        )

        navigation = arrayOf(
            jso {
                kind = "header"
                title = "首页"
//                icon = HomeMini.create()
            },
            jso {
                title = "添加用户"
                segment = RouteAddUser
//                icon = PersonAdd.create()
            },
            jso {
                title = "用户列表"
                segment = RouteUserList
//                icon = People.create()
            }
        )
        branding = jso {
            title = "后台管理"
        }
        Outlet()
    }
}

private val RootLayout = FC {
    DashboardLayout {
        PageContainer {
            Outlet()
        }
    }
}

private val RootRoutes = arrayOf<RouteObject>(
    jso {
        path = RouteAddUser
        Component = RouteAddUserFC
    },
    jso {
        path = RouteUserList
        Component = RouteUserListFC
    }
)

val AppBrowserRouter = createBrowserRouter(
    arrayOf(jso {
        Component = AppLayout

        children = arrayOf(jso {
            path = "/"
            Component = RootLayout
            children = RootRoutes
            errorElement = FC {
                +"/ 加载错误"
            }.create()
        })

        errorElement = FC {
            val a = useRouteError()
            console.error(a)
            +"App Error $a"
        }.create()
    })
)
