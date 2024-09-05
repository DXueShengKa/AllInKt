package cn.allin


import ant.*
import ant.icons.OutlinedBars
import ant.icons.OutlinedUser
import cn.allin.ui.AddUserFC
import cn.allin.ui.NavUserListFc
import cn.allin.ui.RouteAddUser
import cn.allin.ui.RouteUserList
import js.objects.jso
import react.CSSProperties
import react.FC
import react.useState
import web.cssom.*
import kotlin.js.Date


private val SiderStyle: CSSProperties = jso {
    height = 100.vh
    overflow = Auto.auto
}

private val HeaderStyle: CSSProperties = jso {
    padding = 0.px
    height = 64.px
    paddingInline = 48.px
    color = Color("#fff")
}

private val ContentStyle: CSSProperties = jso {
    minHeight = 120.px
}


private val menuItems: Array<MenuItemType> = createMenuItems {

    subMenu {
        key = "user"
        icon { OutlinedUser() }
        label { +"用户" }
        items(
            {
                key = RouteUserList
                label { +"列表" }
            },
            {
                key = RouteAddUser
                label { +"添加用户" }
            }
        )
    }

    subMenu {
        key = "1"
        icon { OutlinedBars() }
        label {
            +"用户"
        }
        items(
            {
                key = "3"
                label { +"3333" }
                children = createMenuItems {
                    menu {
                        key = "4"
                        label { +"33322" }
                    }
                }
            },
            {
                key = "5"
                label { +"4443" }
            }
        )
    }
}


val NavApp = FC {
    Layout {
        hasSider = true

        var routePath by useState("空")

        Layout.Sider {
            style = SiderStyle

            Menu {
//                defaultSelectedKeys = arrayOf(menuItems[0].key)
                onClick = { item ->
                    console.log(item)
                    routePath = item.key

                }
                mode = MenuModel.inline
                items = menuItems
            }
        }

        Layout {

            Layout.Header {
                style = HeaderStyle
                +"Header"
            }

            Layout.Content {
                style = ContentStyle
                when (routePath) {
                    RouteUserList -> {
                        NavUserListFc {
                            key = RouteUserList
                        }
                    }
                    RouteAddUser -> {
                        AddUserFC {
                            key = RouteAddUser
                        }
                    }
                    else -> {
                        + routePath
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

