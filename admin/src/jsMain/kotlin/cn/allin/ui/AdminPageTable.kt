package cn.allin.ui

import js.objects.unsafeJso
import mui.material.Button
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
import react.Key
import react.Props
import tanstack.table.core.Table
import web.cssom.px


data class PageParams(
    val size: Int = 10,
    val index: Int = 0
)


external interface AdminTableProps : Props {
    var table: Table<*>
    var pageCount: Long?
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
                        key = Key(group.id)
                        for (header in group.headers) {
                            TableCell {
                                key = Key(header.id)
                                +renderHeader(header)
                            }
                        }
                    }
                }
            }

            TableBody {
                Button {
                    sx = unsafeJso {
                        width = 100.px
                    }

                }

                val bodyRows = props.table.getRowModel().rows
                for (row in bodyRows) {
                    TableRow {
                        key = Key(row.id)
                        for (cell in row.getVisibleCells()) {
                            TableCell {
                                key = Key(cell.id)
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
