package ant

import react.ChildrenBuilder
import react.Props
import react.PropsWithChildren
import react.ReactDsl
import react.dom.html.HTMLAttributes
import web.html.HTMLHtmlElement


fun <T> ChildrenBuilder.form(block: @ReactDsl FormProps<T>.() -> Unit) {
    Form.invoke(block)
}

fun ChildrenBuilder.layout(block: @ReactDsl LayoutProps.(LayoutFC) -> Unit) {
    Layout {
        block(Layout)
    }
}


external interface AntChildrenProps : PropsWithChildren, HTMLAttributes<HTMLHtmlElement>

external interface AntProps : Props, HTMLAttributes<HTMLHtmlElement>
