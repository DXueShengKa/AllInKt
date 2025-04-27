package cn.allin.ui

import mui.material.Paper
import mui.material.Table
import mui.material.TableBody
import mui.material.TableCell
import mui.material.TableContainer
import mui.material.TableFooter
import mui.material.TableHead
import mui.material.TablePagination
import mui.material.TableRow
import react.FC
import react.Props
import tanstack.react.table.renderCell
import tanstack.react.table.renderHeader
import tanstack.table.core.Table


data class PageParams(
    val size: Int = 10,
    val index: Int = 0
)


external interface AdminTableProps : Props {
    var table: Table<*>
    var pageCount: Int?
    var page: PageParams
    var onPage: (Int) -> Unit
    var onPageParams: (PageParams) -> Unit
}

val AdminPageTable = FC<AdminTableProps> { props ->
    TableContainer {
        component = Paper
        Table {
            stickyHeader = true
            ariaLabel = "sticky table"

            TableHead {
                val headerGroups = props.table.getHeaderGroups()
                for (group in headerGroups) {
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

            TableBody {
                val bodyRows = props.table.getRowModel().rows
                for (row in bodyRows) {
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


            TableFooter {
                TableRow {
                    TablePagination {
                        rowsPerPageOptions = arrayOf(10, 20, 30)
                        count = props.pageCount ?: 0
                        rowsPerPage = props.page.size
                        page = props.page.index
                        onPageChange = { e, i ->
                            props.onPage(i.toInt())
                        }
                        onRowsPerPageChange = { e ->
                            val p = PageParams(
                                e.target.asDynamic()?.value ?: 10,
                                0
                            )
                            props.onPageParams(p)
                        }
                    }
                }
            } // TableFooter



        }
    }
}
