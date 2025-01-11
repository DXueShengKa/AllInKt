@file:JsModule("antd")

package ant

import react.FC
import react.Props
import kotlin.js.Date

external interface DatePickerProps : Props {
    var onChange: (Date, String) -> Unit

    var defaultValue:  DayJs
}

external val DatePicker: FC<DatePickerProps>


