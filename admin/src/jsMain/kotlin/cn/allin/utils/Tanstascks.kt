package cn.allin.utils

import js.array.ReadonlyArray
import js.coroutines.internal.IsolatedCoroutineScope
import js.coroutines.promise
import kotlinx.coroutines.cancel
import mui.material.*
import tanstack.query.core.MutationFunction
import tanstack.query.core.QueryFunction
import tanstack.query.core.QueryKey
import tanstack.react.table.renderCell
import tanstack.react.table.renderHeader
import tanstack.table.core.HeaderGroup
import tanstack.table.core.Row
import tanstack.table.core.RowData
import web.events.addHandler

fun <D> createQueryFunction(block: suspend () -> D): QueryFunction<D, QueryKey, Nothing> =
    QueryFunction { context ->
        val scope = IsolatedCoroutineScope()

        context.signal.abortEvent.addHandler {
            scope.cancel()
        }

        scope.promise { block() }
    }


fun <D, V> createMutationFunction(action: suspend (V) -> D): MutationFunction<D, V> = { variables ->
    IsolatedCoroutineScope()
        .promise { action(variables) }
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
