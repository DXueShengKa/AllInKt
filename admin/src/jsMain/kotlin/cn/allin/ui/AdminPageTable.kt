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
import react.StateSetter
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
    
    // 更改每个页面的行数时触发
    var onPageParams: (PageParams) -> Unit
    var setOnPageParams: StateSetter<PageParams>?
}

val AdminPageTable = FC<AdminTableProps> {
    TableContainer {
        component = Paper
        Table {

            TableHead {
                val headerGroups = it.table.getHeaderGroups()
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
                val bodyRows = it.table.getRowModel().rows
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
                        count = it.pageCount ?: 0
                        rowsPerPage = it.page.size
                        page = it.page.index
                        onPageChange = { e, i ->
                            it.onPage(i.toInt())
                        }
                        onRowsPerPageChange = { e ->
                            val p = PageParams(
                                e.target.asDynamic()?.value ?: 10,
                                0
                            )
                            it.setOnPageParams?.invoke(p)?:it.onPageParams(p)
                        }
                    }
                }
            } // TableFooter



        }
    }
}
