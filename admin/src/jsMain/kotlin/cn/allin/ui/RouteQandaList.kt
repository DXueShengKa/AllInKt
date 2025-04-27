package cn.allin.ui

import cn.allin.VoFieldName
import cn.allin.api.ApiQanda
import cn.allin.api.ApiQandaTag
import cn.allin.net.Req
import cn.allin.net.uploadExcel
import cn.allin.net.useQuery
import cn.allin.utils.DATE_TIME_DEFAULT_FORMAT
import cn.allin.utils.asyncFunction
import cn.allin.utils.columnDefCell
import cn.allin.utils.getValue
import cn.allin.utils.invokeFn
import cn.allin.utils.reactNode
import cn.allin.utils.rsv
import cn.allin.utils.selectColumnDef
import cn.allin.utils.setState
import cn.allin.utils.useCoroutineScope
import cn.allin.utils.useInject
import cn.allin.utils.useRowSelectionState
import cn.allin.vo.PageVO
import cn.allin.vo.QandaVO
import js.array.ReadonlyArray
import js.objects.jso
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlinx.datetime.format
import mui.material.Button
import mui.material.Grid
import mui.material.GridDirection
import mui.material.IconButton
import mui.material.Input
import mui.material.List
import mui.material.ListItemButton
import mui.material.ListItemText
import mui.material.Stack
import mui.material.StackDirection
import mui.system.responsive
import mui.system.sx
import muix.icons.IconsDelete
import react.FC
import react.Props
import react.dom.aria.AriaRole
import react.dom.html.ReactHTML.div
import react.useMemo
import react.useRef
import react.useState
import tanstack.react.table.useReactTable
import tanstack.table.core.ColumnDef
import tanstack.table.core.StringOrTemplateHeader
import tanstack.table.core.TableOptions
import tanstack.table.core.getCoreRowModel
import toolpad.core.SeverityStr
import toolpad.core.show
import toolpad.core.useDialogs
import toolpad.core.useNotifications
import web.cssom.AlignItems
import web.cssom.JustifyContent
import web.file.File
import web.html.HTMLInputElement
import web.html.InputType

private fun qaListColumnDef(
    onDelete: (QandaVO) -> Unit,
): ReadonlyArray<ColumnDef<QandaVO, String?>> = arrayOf(
    selectColumnDef(),
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
            qa.question
        }
    },
    jso {
        id = VoFieldName.QandaVO_answer
        header = StringOrTemplateHeader("回答")
        accessorFn = { qa, _ ->
            qa.answer
        }
    },
    jso {
        id = VoFieldName.QandaVO_tagList
        header = StringOrTemplateHeader("标签")
        accessorFn = { qa, _ ->
            qa.tagList?.joinToString(",")
        }
    },
    jso {
        id = VoFieldName.QandaVO_createTime
        header = StringOrTemplateHeader("创建时间")
        accessorFn = { qa, _ ->
            qa.createTime?.format(DATE_TIME_DEFAULT_FORMAT)
        }
    },
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
    val (pageParams, setPageParams) = useState(PageParams())
    var qaPage: PageVO<QandaVO>? by useState()
    val selectState = useRowSelectionState()
    val cs by useCoroutineScope()
    val notifications = useNotifications()
    val apiQanda: ApiQanda = useInject()
    val apiQandaTag: ApiQandaTag = useInject()


    val query = useQuery(pageParams) {
        apiQanda.page(pageParams.index,pageParams.size)
    }

    val onDelete: (QandaVO) -> Unit = { vo ->
        cs.launch {
            apiQanda.delete(vo.id ?: return@launch)
                .onLeft {
                    notifications.show("${vo.question} $it", severity = SeverityStr.error)
                }.onRight {
                    query.refresh()
                    notifications.show("${vo.question} 已删除")
                }
        }
    }

    val tableData: Array<QandaVO> = useMemo(query.data) {
        selectState.clear()
        qaPage = query.data
        query.data?.rows?.toTypedArray() ?: emptyArray()
    }


    val qaTable = useReactTable(
        TableOptions<QandaVO>(
            columns = qaListColumnDef(
                onDelete = onDelete
            ),
            data = tableData,
            onRowSelectionChange = selectState.onSelectChange,
            getCoreRowModel = getCoreRowModel(),
        ).setState(
            rowSelection = selectState.rows,
        )
    )


    TableMenu {
        onRefresh = query::refresh
        onDeleteSelect = {
            val ids = qaTable.getSelectedRowModel().flatRows.mapNotNull { it.original.id }
            if (ids.isNotEmpty()) cs.launch {
                val count = apiQanda.delete(ids)
                query.refresh()
                notifications.show("删了 $count 条数据")
            }

        }
    }

    AdminPageTable {
        table = qaTable
        pageCount = qaPage?.totalRow
        page = pageParams
        onPage = {
            setPageParams(
                pageParams.copy(
                    index = it
                )
            )
        }
        onPageParams = setPageParams.invokeFn
    }
}


private external interface TableMenuProps : Props {
    var onRefresh: () -> Unit

    var onDeleteSelect: () -> Unit
}


private val TableMenu = FC<TableMenuProps> { props ->

    val excelFile = useRef<File>()
    val notifications = useNotifications()
    val dialog = useDialogs()

    Stack {
        direction = responsive(StackDirection.row)
        spacing = responsive(2)

        Input {
            type = InputType.file.toString()
            onChange = {
                val e = it.target as HTMLInputElement
                excelFile.current = e.files?.get(0)
            }
        }

        Button {
            onClick = asyncFunction { event ->
                event.preventDefault()
                val f = excelFile.current ?: return@asyncFunction

                Req.uploadExcel(f)

                notifications.show("已更新")

                props.onRefresh()
            }

            +"上传excel"
        }

        Button {

            onClick = asyncFunction {
                val b = dialog.confirm(reactNode("是否删除选中"), jso {
//                    title = reactNode("")
                    okText = reactNode("删除")
                    cancelText = reactNode("取消")
                }).await()

                if (b) props.onDeleteSelect()

            }.asDynamic()

            IconsDelete()

            +"删除选中"
        }

    }

}

private val SelectTab = FC {
    Grid {
        container = true
        spacing = 2.rsv
        sx {
            justifyContent = JustifyContent.center
            alignItems = AlignItems.center
        }
        Grid {
            item = true
            TagList{

            }
        }
        Grid {
            item = true

        }
        Grid {
            item = true
            TagList{

            }
        }
    }
}

private external interface TagListProps : Props {
    var items: List<String>
    var onItem: (String) -> Unit
}

private val TagList = FC<TagListProps> { props ->
    List {
        component = div
        role = AriaRole.list

        props.items.forEach { v ->
            ListItemButton {
                key = v
                role = AriaRole.listitem
                onClick = {
                    props.onItem(v)
                }
                ListItemText {
                    primary = reactNode(v)
                }
            }
        }
    }
}

private val G = FC {
    Grid {
        container = true
        direction = GridDirection.column.rsv
        sx {
            alignItems = AlignItems.center
        }
        Button {

        }
    }
}

val RouteQandaList = routes(
    "list", "问题列表", QandaListFC
)
