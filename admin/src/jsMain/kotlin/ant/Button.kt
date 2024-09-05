@file:JsModule("antd/lib/button")

package ant

import react.FC
import react.PropsWithChildren

external interface ButtonProps : PropsWithChildren, react.dom.html.HTMLAttributes<web.html.HTMLButtonElement> {
    var type: ButtonType
    var htmlType: web.html.ButtonType
}

@JsName("default")
external val Button: FC<ButtonProps>


