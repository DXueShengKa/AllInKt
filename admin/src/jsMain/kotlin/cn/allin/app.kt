package cn.allin


import cn.allin.utils.SessionContext
import cn.allin.data.repository.UserRepository
import cn.allin.net.Req
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
import cn.allin.utils.asyncFunction
import cn.allin.utils.colorSchemes
import cn.allin.utils.cssVariables
import cn.allin.utils.getKoin
import js.objects.jso
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
import cn.allin.utils.useSessionContext

private val RootLayoutRoutes = arrayOf<RouteObject>(
    jso {
        path = "user"
        children = arrayOf(
            RouteUserAdd.routeObj,
            RouteUserList.routeObj
        )
    },
    jso {
        path = "qanda"
        children = arrayOf(
            RouteQandaList.routeObj,
            RouteQandaAdd.routeObj,
            RouteTagList.routeObj,
            RouteTagAdd.routeObj
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
    },
    jso {
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
    val userRepository: UserRepository = getKoin().get()

    useEffectOnce {
        Req.authToken()?.let {
            val u = userRepository.userSession()
            sessionContext.set(jso {
                user = u
            })
            navigate("/")
        }
    }

    SessionContext.Provider {
        value = sessionContext
        ReactRouterAppProvider {
            authentication = jso {
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

