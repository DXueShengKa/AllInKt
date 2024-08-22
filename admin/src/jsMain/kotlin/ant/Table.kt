@file:JsModule("antd")

package ant

import react.FC
import react.Props

external interface TableProps : Props {
    var dataSource: Array<dynamic>
    var columns: Array<TableColumn<dynamic>>
}

external val Table: FC<TableProps>
