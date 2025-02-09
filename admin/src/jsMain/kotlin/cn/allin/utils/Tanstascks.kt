package cn.allin.utils

import js.array.ReadonlyArray
import js.coroutines.internal.IsolatedCoroutineScope
import js.coroutines.promise
import kotlinx.coroutines.cancel
import mui.material.TableBody
import mui.material.TableCell
import mui.material.TableHead
import mui.material.TableProps
import mui.material.TableRow
import react.ChildrenBuilder
import react.FC
import react.ReactDsl
import tanstack.query.core.MutationFunction
import tanstack.query.core.QueryFunction
import tanstack.query.core.QueryFunctionContext
import tanstack.query.core.QueryKey
import tanstack.react.table.renderCell
import tanstack.react.table.renderHeader
import tanstack.table.core.CellContext
import tanstack.table.core.ColumnDefTemplate
import tanstack.table.core.HeaderContext
import tanstack.table.core.HeaderGroup
import tanstack.table.core.Row
import tanstack.table.core.RowData
import web.events.addHandler

fun <T, TQueryKey : QueryKey, TPageParam> queryFunction(
    block: suspend (QueryFunctionContext<TQueryKey, TPageParam>) -> T
): QueryFunction<T, TQueryKey, TPageParam> =
    QueryFunction { context ->
        val scope = IsolatedCoroutineScope()

        context.signal.abortEvent.addHandler {
            scope.cancel()
        }

        scope.promise { block(context) }
    }


fun <D, V> createMutationFunction(action: suspend (V) -> D): MutationFunction<D, V> = { variables ->
    IsolatedCoroutineScope()
        .promise { action(variables) }
}

fun queryKey(vararg key: Any): QueryKey {
    return key.asDynamic()
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
