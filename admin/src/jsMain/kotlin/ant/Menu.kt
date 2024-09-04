@file:JsModule("antd")

package ant

import react.FC
import react.Props
import react.ReactNode

external interface MenuProps : Props {
    var defaultSelectedKeys: Array<String>
    var defaultOpenKeys: Array<String>
    var onSelect: (MenuItemType) -> Unit
    var onClick: (MenuOnClick) -> Unit
    var items: Array<MenuItemType>
    var mode: MenuModel
}



external interface MenuItemType {
    var title: String
    var key: String
    var icon: ReactNode
    var label: ReactNode
}


external interface SubMenuType : MenuItemType {
    var children: Array<SubMenuType>
}


external val Menu: FC<MenuProps>

