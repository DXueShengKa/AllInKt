package cn.allin


import ant.*
import ant.icons.OutlinedBars
import ant.icons.OutlinedBook
import ant.icons.OutlinedUser
import cn.allin.ui.NavUserListFc
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
    menu {
        key = RouteUserList
        icon { OutlinedUser() }
        label { +"用户列表" }
    }

    repeat(30){
        menu {
            key = "it$it"
            icon { OutlinedBook() }
            label { + it.toString() }
        }
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

        var routePath by useState(RouteUserList)

        Layout.Sider {
            style = SiderStyle

            Menu {
                defaultSelectedKeys = arrayOf(menuItems[0].key)
                onClick = { item ->
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

