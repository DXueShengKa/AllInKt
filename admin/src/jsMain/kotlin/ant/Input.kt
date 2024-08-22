@file:JsModule("antd")

package ant

import react.FC
import react.Props


external interface InputProps : Props {
    var placeholder: String

    var variant: Variant

    var addonBefore: String

    var addonAfter: String

    var defaultValue: String
}

external interface PasswordProps : InputProps


external interface InputComponent : FC<InputProps> {
    val Password: FC<PasswordProps>
}

external val Input: InputComponent


external interface NumberProps : InputProps {
    var min: Int
    var max: Int
}

external val InputNumber: FC<NumberProps>


