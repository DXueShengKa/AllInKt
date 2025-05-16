package cn.allin.utils

import js.array.ReadonlyArray
import js.objects.unsafeJso
import mui.material.Checkbox
import mui.material.TableBody
import mui.material.TableCell
import mui.material.TableHead
import mui.material.TableProps
import mui.material.TableRow
import react.ChildrenBuilder
import react.FC
import react.ReactDsl
import react.useState
import tanstack.react.table.renderCell
import tanstack.react.table.renderHeader
import tanstack.table.core.CellContext
import tanstack.table.core.ColumnDef
import tanstack.table.core.ColumnDefTemplate
import tanstack.table.core.HeaderContext
import tanstack.table.core.HeaderGroup
import tanstack.table.core.Row
import tanstack.table.core.RowData
import tanstack.table.core.RowSelectionState
import tanstack.table.core.StringOrTemplateHeader
import tanstack.table.core.TableOptions
import tanstack.table.core.Updater


fun <T> Updater<T>.updaterFn(old: T): T {
    return unsafeCast<(T) -> T>()(old)
}


class SelectState(
    val rows: Updater<RowSelectionState>,
    val onSelectChange: (Updater<js.objects.ReadonlyRecord<String, Boolean>>) -> Unit,
) {
    fun clear() {
        onSelectChange(unsafeJso())
    }
}

fun useRowSelectionState(): SelectState {
    val (rowSelect, setRowSelect) = useState<Updater<RowSelectionState>>(unsafeJso())
    return SelectState(rowSelect, setRowSelect.invokeFn)
}

fun <T : Any> selectColumnDef(): ColumnDef<T, String?> {
    return unsafeJso {
        id = "select"
        header = StringOrTemplateHeader(columnDefHeader { context ->
            Checkbox {
                checked = context.table.getIsAllRowsSelected()
                onChange = { e, b ->
                    context.table.getToggleAllRowsSelectedHandler().invoke(e)
                }
            }
        })

        cell = columnDefCell { context ->
            Checkbox {
                checked = context.row.getIsSelected()
                disabled = !context.row.getCanSelect()
                onChange = { e, b ->
                    context.row.getToggleSelectedHandler().invoke(e)
                }
            }
        }
    }
}


/**
 * tanstack包装器缺少api手动增加
 */
fun <Data : Any> TableOptions<Data>.setState(
    rowSelection: Updater<RowSelectionState>
): TableOptions<Data> {
    asDynamic()["state"] = unsafeJso {
        this.rowSelection = rowSelection
    }
    return this
}

fun <TData : RowData, TValue> columnDefCell(
    block: @ReactDsl ChildrenBuilder.(props: CellContext<TData, TValue>) -> Unit
): ColumnDefTemplate<CellContext<TData, TValue>> {
    return tanstack.react.table.ColumnDefTemplate(FC(block))
}

fun <TData : RowData, TValue> columnDefHeader(
    block: @ReactDsl ChildrenBuilder.(props: HeaderContext<TData, TValue>) -> Unit
): ColumnDefTemplate<HeaderContext<TData, TValue>> {
    return tanstack.react.table.ColumnDefTemplate(FC(block))
}


fun <T : RowData> TableProps.tanstackHead(groups: ReadonlyArray<HeaderGroup<T>>) {
    TableHead {
        for (group in groups) {
            TableRow {
                key = group.id
                for (header in group.headers) {
                    TableCell {
                        key = header.id
                        +renderHeader(header)
                    }
                }
            }
        }
    }
}

fun <T : RowData> TableProps.tanstackBody(rows: ReadonlyArray<Row<T>>) {
    TableBody {
        for (row in rows) {
            TableRow {
                key = row.id
                for (cell in row.getVisibleCells()) {
                    TableCell {
                        key = cell.id
                        +renderCell(cell)
                    }
                }
            }
        }
    }
}
