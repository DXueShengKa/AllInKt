package cn.allin


import androidx.navigation.react.navController
import androidx.navigation.react.navHost
import androidx.navigation.react.react
import ant.Layout
import cn.allin.net.HeaderAuthorization
import cn.allin.ui.NavAuth
import cn.allin.ui.NavUserListFc
import cn.allin.ui.RouteAuth
import cn.allin.ui.RouteUserList
import js.objects.jso
import react.FC
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h3
import web.cssom.*
import kotlin.js.Date


val appNavController = navController()

val App = navHost(
    appNavController,
    if (HeaderAuthorization == null) RouteAuth else RouteUserList
) {
    react(RouteAuth, NavAuth)
    react(RouteUserList, NavUserListFc)

}



val App2 = FC {
    Layout {
        hasSider = true
        Layout.Sider {
            style = jso {
                overflow = Overflow.scroll
                width = 20.pct
                position = Position.fixed
            }

//            Menu {
//                items = a
//            }

//            repeat(50) {
//                p {
//                    key = it.toString()
//                    +"----> $it"
//                }
//            }
        }

        Layout {
            style = jso {
                marginInlineStart = 20.pct
            }

            Layout.Header {
                style = jso {
                    textAlign = TextAlign.start
                }
                +"Header"
            }

            Layout.Content {
                style = jso {
                    margin = Margin(20.px, 10.px)
                }
                div {
                    style = jso {
                        textAlign = TextAlign.start
                        padding = 20.px
                    }
                    h3 {
                        +"Content"
                    }
                }
            }

            Layout.Footer {
                style = jso {
                    textAlign = TextAlign.center
                }
                +Date().toString()
            }
        }
    }
}

