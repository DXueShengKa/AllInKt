package cn.allin.ui

import cn.allin.ServerRoute
import cn.allin.VoFieldName
import cn.allin.net.PageParams
import cn.allin.net.Req
import cn.allin.net.getQandaPage
import cn.allin.utils.columnDefCell
import cn.allin.utils.columnDefHeader
import cn.allin.utils.queryFunction
import cn.allin.utils.queryKey
import cn.allin.utils.tanstackBody
import cn.allin.utils.tanstackHead
import cn.allin.vo.PageVO
import cn.allin.vo.QandaVO
import js.array.ReadonlyArray
import js.objects.jso
import mui.material.Checkbox
import mui.material.Paper
import mui.material.Table
import mui.material.TableContainer
import mui.material.TableFooter
import mui.material.TablePagination
import mui.material.TableRow
import react.FC
import react.useMemo
import react.useState
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tanstack.react.table.useReactTable
import tanstack.table.core.ColumnDef
import tanstack.table.core.RowSelectionState
import tanstack.table.core.StringOrTemplateHeader
import tanstack.table.core.getCoreRowModel

private val QaListColumnDef: ReadonlyArray<ColumnDef<QandaVO, String?>> = arrayOf(
    jso {
        id = "select"
        header = StringOrTemplateHeader(columnDefHeader {
            Checkbox {
                checked = it.table.getIsAllRowsSelected()
                onChange = { e, b ->
                    it.table.getToggleAllRowsSelectedHandler().invoke(e)
                }
            }
        })

        cell = columnDefCell {
            Checkbox {
                checked = it.row.getIsSelected()
                disabled = !it.row.getCanSelect()
                onChange = { e, b ->
                    it.row.getToggleSelectedHandler().invoke(e)
                }
            }
        }
    },
    jso {
        id = "id"
        header = StringOrTemplateHeader("ID")
        accessorFn = { qa, _ ->
            qa.id.toString()
        }
    },
    jso {
        id = VoFieldName.QandaVO_question
        header = StringOrTemplateHeader("问题")
        accessorFn = { qa, _ ->
            qa.question.toString()
        }
    },
    jso {
        id = VoFieldName.QandaVO_answer
        header = StringOrTemplateHeader("回答")
        accessorFn = { qa, _ ->
            qa.answer.toString()
        }
    },
//    jso {
//        id = VoFieldName.QandaVO_tagIds
//        header = StringOrTemplateHeader("标签")
//        accessorFn = { qa, _ ->
//            qa.tagIds?.joinToString(",")
//        }
//    }
)


private val QandaListFC = FC {

    var pageParams by useState(PageParams())
    var qaPage: PageVO<QandaVO>? by useState()
    var rowSelect: RowSelectionState by useState(jso())
//    val cs: CoroutineScope? by useCoroutineScope()
//    var showMessage by useState(false)

    val query = useQuery<PageVO<QandaVO>, Error, PageVO<QandaVO>, QueryKey>(options = jso {
        queryKey = queryKey(ServerRoute.Qanda, pageParams)
        queryFn = queryFunction {
            Req.getQandaPage(pageParams)
        }
    })


    val tableData: Array<QandaVO> = useMemo(query.data) {
        rowSelect = jso()
        qaPage = query.data
        query.data?.rows?.toTypedArray() ?: emptyArray()
    }

    val table = useReactTable<QandaVO>(jso {
        columns = QaListColumnDef
        data = tableData
        state = jso {
            rowSelection = rowSelect
        }
        onRowSelectionChange = {
            rowSelect = it.asDynamic()
        }
        this.getCoreRowModel = getCoreRowModel()
    })


    TableContainer {
        component = Paper
        Table {
            tanstackHead(table.getHeaderGroups())
            tanstackBody(table.getRowModel().rows)
            TableFooter {
                TableRow {
                    TablePagination {
                        rowsPerPageOptions = arrayOf(10, 20, 30)
                        count = qaPage?.totalRow ?: 0
                        rowsPerPage = pageParams.size
                        page = pageParams.index
                        onPageChange = { e, i ->
                            pageParams = pageParams.copy(
                                index = i.toInt()
                            )
                        }
                        onRowsPerPageChange = {
                            pageParams = PageParams(
                                size = it.target.asDynamic()?.value ?: 10,
                                index = 0
                            )
                        }
                    }
                }
            }
        }
    }
}


val RouteQandaList = routes(
    "list", "问题列表", QandaListFC
)
