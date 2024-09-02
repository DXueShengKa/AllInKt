@file:JsModule("antd")

package ant

import react.FC
import react.Props

external interface MenuProps : Props {
    var defaultSelectedKeys: Array<String>
    var defaultOpenKeys: Array<String>
    var onSelect: (MenuItem, String) -> Unit
    var items: Array<MenuItem>
}


external var Menu: FC<MenuProps>

