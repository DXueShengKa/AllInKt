@file:JsModule("antd")

package ant

import react.*

external interface ButtonProps : PropsWithChildren, react.dom.html.HTMLAttributes<web.html.HTMLButtonElement> {
    var type: ButtonType
    var htmlType: web.html.ButtonType
}

//@JsName("default")
external val Button: FC<ButtonProps>


