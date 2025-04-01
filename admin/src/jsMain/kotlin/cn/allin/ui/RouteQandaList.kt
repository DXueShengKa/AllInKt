package cn.allin.ui

import cn.allin.ServerRoute
import cn.allin.VoFieldName
import cn.allin.getValue
import cn.allin.net.Req
import cn.allin.net.deleteQanda
import cn.allin.net.getQandaPage
import cn.allin.useCoroutineScope
import cn.allin.utils.columnDefCell
import cn.allin.utils.columnDefHeader
import cn.allin.utils.queryFunction
import cn.allin.utils.queryKey
import cn.allin.vo.PageVO
import cn.allin.vo.QandaVO
import js.array.ReadonlyArray
import js.objects.jso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mui.material.Checkbox
import mui.material.IconButton
import muix.icons.IconsDelete
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

private fun qaListColumnDef(
    onDelete: (QandaVO) -> Unit,
): ReadonlyArray<ColumnDef<QandaVO, String?>> = arrayOf(
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
        header = StringOrTemplateHeader(id)
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

    jso {
        id = "操作"
        header = StringOrTemplateHeader(id)
        cell = columnDefCell { cellContext ->
            IconButton {
                onClick = {
                    onDelete(cellContext.row.original)
                }
                IconsDelete()
            }
        }
    }
)


private val QandaListFC = FC {
    var (pageParams, setPageParams) = useState(PageParams())
    var qaPage: PageVO<QandaVO>? by useState()
    var rowSelect: RowSelectionState by useState(jso())
    val cs: CoroutineScope? by useCoroutineScope()
//    var showMessage by useState(false)


    val query = useQuery<PageVO<QandaVO>, Error, PageVO<QandaVO>, QueryKey>(options = jso {
        queryKey = queryKey(ServerRoute.Qanda, pageParams)
        queryFn = queryFunction {
            Req.getQandaPage(pageParams)
        }
    })

    val onDelete: (QandaVO) -> Unit = {
        cs?.launch {
            val msg = Req.deleteQanda(it.id ?: return@launch)
            query.refetch(jso())
        }
    }

    val tableData: Array<QandaVO> = useMemo(query.data) {
        rowSelect = jso()
        qaPage = query.data
        query.data?.rows?.toTypedArray() ?: emptyArray()
    }

    val qaTable = useReactTable<QandaVO>(jso {
        columns = qaListColumnDef(
            onDelete = onDelete
        )
        data = tableData
        state = jso {
            rowSelection = rowSelect
        }
        onRowSelectionChange = {
            rowSelect = it.asDynamic()
        }
        this.getCoreRowModel = getCoreRowModel()
    })

    AdminPageTable {
        table = qaTable
        pageCount = qaPage?.totalRow
        page = pageParams
        onPage = {
            pageParams = pageParams.copy(
                index = it.toInt()
            )
        }
        setOnPageParams = setPageParams
    }
}


val RouteQandaList = routes(
    "list", "问题列表", QandaListFC
)
