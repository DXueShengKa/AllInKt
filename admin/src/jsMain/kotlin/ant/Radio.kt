@file:JsModule("antd")

package ant

import react.FC

external interface RadioProps : AntChildrenProps {
}

external interface RadioGroupProps : AntProps {
    var value: dynamic
    var options: Array<RadioOptions<dynamic>>
    var disabled: Boolean
    var onChange: (RadioChangeEvent<Any>) -> Unit
}

external interface RadioFC : FC<RadioProps> {
    val Group: FC<RadioGroupProps>
}


external val Radio: RadioFC
