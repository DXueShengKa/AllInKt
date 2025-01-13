@file:JsModule("antd/lib/card")
package ant

import react.FC
import react.PropsWithChildren

external interface CardProps : PropsWithChildren, react.dom.html.HTMLAttributes<web.html.HTMLButtonElement> {

}

@JsName("default")
external val Card : FC<CardProps>

