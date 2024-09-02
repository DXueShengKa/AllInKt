@file:JsModule("antd")

package ant

import react.FC
import react.Props
import react.PropsWithChildren
import react.dom.html.HTMLAttributes
import web.html.HTMLHtmlElement


external interface LayoutProps : AntChildrenProps {
}

external interface LayoutFC : FC<LayoutProps> {
    var Header: FC<AntChildrenProps>
    var Content: FC<AntChildrenProps>
}


external val Layout: LayoutFC


