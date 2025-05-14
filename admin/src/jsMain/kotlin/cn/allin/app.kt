package cn.allin


import cn.allin.api.ApiUser
import cn.allin.net.Req
import cn.allin.net.WEKV
import cn.allin.net.deleteAuth
import cn.allin.net.userSession
import cn.allin.ui.RouteAuth
import cn.allin.ui.RouteAuthFC
import cn.allin.ui.RouteQandaAdd
import cn.allin.ui.RouteQandaList
import cn.allin.ui.RouteTagAdd
import cn.allin.ui.RouteTagList
import cn.allin.ui.RouteUserAdd
import cn.allin.ui.RouteUserList
import cn.allin.utils.SessionContext
import cn.allin.utils.asyncFunction
import cn.allin.utils.colorSchemes
import cn.allin.utils.cssVariables
import cn.allin.utils.useInject
import cn.allin.utils.useSessionContext
import js.objects.unsafeJso
import mui.material.PaletteMode
import mui.material.styles.createTheme
import react.FC
import react.create
import react.router.Navigate
import react.router.Outlet
import react.router.RouteObject
import react.router.dom.createHashRouter
import react.router.useNavigate
import react.router.useRouteError
import react.useEffectOnce
import toolpad.core.DashboardLayout
import toolpad.core.Navigation
import toolpad.core.PageContainer
import toolpad.core.Session
import toolpad.core.react_router.ReactRouterAppProvider
import toolpad.core.useSession

private val RootLayoutRoutes = arrayOf<RouteObject>(
    unsafeJso {
        path = "user"
        children = arrayOf(
            RouteUserAdd.routeObj,
            RouteUserList.routeObj
        )
    },
    unsafeJso {

        path = "qanda"
        children = arrayOf(
            RouteQandaList.routeObj,
            RouteQandaAdd.routeObj,
            RouteTagList.routeObj,
            RouteTagAdd.routeObj
        )
    },

    unsafeJso {

        path = "*"
        Component = FC {
            +"默认页面"
        }
    }
)

private val appNavigation: Navigation = arrayOf(
    unsafeJso {

        kind = "header"
        title = "首页"
    },
    unsafeJso {

        title = "用户管理"
        segment = "user"
        children = arrayOf(
            RouteUserAdd.navigation,
            RouteUserList.navigation
        )
    },
    unsafeJso {
        title = "问答管理"
        segment = "qanda"
        children = arrayOf(
            RouteQandaList.navigation,
            RouteQandaAdd.navigation,
            RouteTagList.navigation,
            RouteTagAdd.navigation,
        )
    }
)

private val AppLayout = FC {
    val navigate = useNavigate()
    val sessionContext = useSessionContext()
    val apiUser: ApiUser = useInject()

    useEffectOnce {
        WEKV.authorization.getOrNull()?.let {
            val u = apiUser.userSession()
            sessionContext.set(unsafeJso {
                user = u
            })
            navigate("/")
        }
    }

    SessionContext.Provider {
        value = sessionContext
        ReactRouterAppProvider {
            authentication = unsafeJso {

                signIn = {
                    navigate(RouteAuth)
                }
                signOut = asyncFunction {
                    Req.deleteAuth()
                    sessionContext.set(null)
                    navigate(RouteAuth)
                }
            }

            theme = createTheme(
                unsafeJso {

                    palette = unsafeJso {

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

            branding = unsafeJso {

                title = "后台管理"
            }
            Outlet()
        }
    }
}

private val RootLayout = FC {
    val session = useSession<Session>()
    if (session?.user == null) {
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


val AppBrowserRouter = createHashRouter(
    arrayOf(unsafeJso {

        Component = AppLayout

        children = arrayOf(
            unsafeJso {

                path = "/"
                Component = RootLayout
                children = RootLayoutRoutes
                errorElement = FC {
                    +"/ 加载错误"
                }.create()
            },
            unsafeJso {

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

