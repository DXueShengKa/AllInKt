package ant

import react.ReactNode


@JsName("ItemType")
interface MenuItem {
    var key: String
    var icon: ReactNode
    var label: String
}

interface MenuItemType : MenuItem {
    var title: String
}


interface SubMenuType : MenuItem {
    var children: Array<MenuItem>
}
