@file:JsModule("antd")

package ant

import react.FC

external interface SpaceProps : AntProps {
    var direction: LayoutDirection
    var size: SpaceSize
}

external val Space: FC<SpaceProps>

