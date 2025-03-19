package cn.allin


import SessionContext
import cn.allin.ui.RouteAuth
import cn.allin.ui.RouteAuthFC
import cn.allin.ui.RouteUserAdd
import cn.allin.ui.RouteUserList
import colorSchemes
import cssVariables
import js.objects.jso
import mui.material.PaletteMode
import mui.material.styles.createTheme
import react.FC
import react.create
import react.router.Navigate
import react.router.Outlet
import react.router.RouteObject
import react.router.dom.createBrowserRouter
import react.router.useNavigate
import react.router.useRouteError
import toolpad.core.DashboardLayout
import toolpad.core.Navigation
import toolpad.core.PageContainer
import toolpad.core.react_router.ReactRouterAppProvider
import useSession

private val RootLayoutRoutes = arrayOf<RouteObject>(
    jso {
        path = "user"
        children = arrayOf(
            RouteUserAdd.routeObj,
            RouteUserList.routeObj
        )
    },

    jso {
        path = "*"
        Component = FC {
            +"默认页面"
        }
    }
)

private val appNavigation: Navigation = arrayOf(
    jso {
        kind = "header"
        title = "首页"
    },
    jso {
        title = "用户管理"
        segment = "user"
        children = arrayOf(
            RouteUserAdd.navigation,
            RouteUserList.navigation
        )
    }
)

private val AppLayout = FC {
    val navigate = useNavigate()
    val sessionContext = useSession()
    SessionContext.Provider {
        value = sessionContext
        ReactRouterAppProvider {
            authentication = jso {
                signIn = {
                    navigate(RouteAuth)
                }
                signOut = {
                    sessionContext.set(null)
                    navigate(RouteAuth)
                }
            }
            theme = createTheme(
                jso {
                    palette = jso {
                        mode = PaletteMode.light
                    }
                    cssVariables(
                        colorSchemeSelector = "data-toolpad-color-scheme"
                    )
                    colorSchemes(true, true)
                },
                muiLocal.zhCN
            )

            session = sessionContext.session

            navigation = appNavigation

            branding = jso {
                title = "后台管理"
            }
            Outlet()
        }
    }
}

private val RootLayout = FC {
    val session = useSession()
    if (session.session != null) {
        Navigate {
            to = RouteAuth
            replace = true
        }
    } else {
        DashboardLayout {
            PageContainer {
                Outlet()
            }
        }
    }
}


val AppBrowserRouter = createBrowserRouter(
    arrayOf(jso {
        Component = AppLayout

        children = arrayOf(
            jso {
                path = "/"
                Component = RootLayout
                children = RootLayoutRoutes
                errorElement = FC {
                    +"/ 加载错误"
                }.create()
            },
            jso {
                path = RouteAuth
                Component = RouteAuthFC
            }
        )

        errorElement = FC {
            val e = useRouteError()
            console.log(e)
            +"错误 ${e.toString()}"
        }.create()
    })
)

