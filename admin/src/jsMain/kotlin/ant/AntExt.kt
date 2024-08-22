package ant

import react.ChildrenBuilder
import react.ReactDsl


fun <T> ChildrenBuilder.form(block: @ReactDsl FormProps<T>.() -> Unit) {
    Form.invoke(block)
}
