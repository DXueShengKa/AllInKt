@file:JsModule("antd")

package ant

import react.FC


external interface LayoutProps : AntChildrenProps {
    var hasSider: Boolean
}

external interface SiderProps : AntChildrenProps {
    var collapsed: Boolean
    var onCollapse: Boolean
}


external interface LayoutFC : FC<LayoutProps> {
    var Header: FC<AntChildrenProps>
    var Content: FC<AntChildrenProps>
    var Footer: FC<AntChildrenProps>
    var Sider: FC<SiderProps>
}


external val Layout: LayoutFC


