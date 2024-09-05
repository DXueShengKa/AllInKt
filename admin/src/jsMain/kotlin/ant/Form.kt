@file:JsModule("antd")

package ant

import react.FC
import react.PropsWithChildren

external interface FormProps<T> : PropsWithChildren, react.dom.html.HTMLAttributes<web.html.HTMLFormElement> {
    var name: String
    var layout: LayoutDirection
    var labelAlign: LabelAlign
    var variant: Variant
    var onFinish: (T) -> Unit
    var onFinishFailed: (Error) -> Unit
    var onValuesChange: (T,dynamic) -> Unit
}

external interface FormItemProps<T> : PropsWithChildren {
    var label: String
    var name: String
    var help: String
    var rules: Array<Rule>
}


external interface FormFC<T> : FC<FormProps<T>> {
    val Item: FC<FormItemProps<T>>
}


external val Form: FormFC<dynamic>
