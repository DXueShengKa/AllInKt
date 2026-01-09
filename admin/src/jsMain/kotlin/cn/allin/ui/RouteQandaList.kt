package cn.allin.ui

import cn.allin.answer
import cn.allin.api.ApiQanda
import cn.allin.api.ApiQandaTag
import cn.allin.createTime
import cn.allin.net.Req
import cn.allin.net.uploadExcel
import cn.allin.net.useQuery
import cn.allin.question
import cn.allin.tagList
import cn.allin.utils.DATE_TIME_DEFAULT_FORMAT
import cn.allin.utils.asyncFunction
import cn.allin.utils.columnDefCell
import cn.allin.utils.invokeFn
import cn.allin.utils.reactNode
import cn.allin.utils.selectColumnDef
import cn.allin.utils.setState
import cn.allin.utils.useCoroutineScope
import cn.allin.utils.useInject
import cn.allin.utils.useRowSelectionState
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import cn.allin.vo.QandaVO
import js.array.ReadonlyArray
import js.objects.unsafeJso
import kotlinx.coroutines.launch
import kotlinx.datetime.format
import mui.material.Button
import mui.material.FormControl
import mui.material.FormControlLabel
import mui.material.IconButton
import mui.material.InputLabel
import mui.material.LabelPlacement
import mui.material.MenuItem
import mui.material.Select
import mui.material.Stack
import mui.material.StackDirection
import mui.material.Switch
import mui.system.responsive
import mui.system.sx
import muix.fileInput.MuiFileInputSingle
import muix.icons.IconsDelete
import muix.icons.IconsEdit
import react.FC
import react.Props
import react.create
import react.useMemo
import react.useState
import tanstack.react.table.useReactTable
import tanstack.table.core.ColumnDef
import tanstack.table.core.OnChangeFn
import tanstack.table.core.StringOrTemplateHeader
import tanstack.table.core.TableOptions
import tanstack.table.core.getCoreRowModel
import web.cssom.px
import web.dom.ElementId
import web.file.File

private fun qaListColumnDef(
    onEdit: (QandaVO) -> Unit,
    onDelete: (QandaVO) -> Unit,
): ReadonlyArray<ColumnDef<QandaVO, String?>> = arrayOf(
    selectColumnDef(),
    unsafeJso {
        id = "id"
        header = StringOrTemplateHeader(id)
        accessorFn = { qa, _ ->
            qa.id.toString()
        }
    },
    unsafeJso {
        id = QandaVO.question.name
        header = StringOrTemplateHeader(QandaVO.question.display)
        accessorFn = { qa, _ ->
            qa.question
        }
    },
    unsafeJso {
        id = QandaVO.answer.name
        header = StringOrTemplateHeader(QandaVO.answer.display)
        accessorFn = { qa, _ ->
            if (qa.answer.length > 9) {
                qa.answer + "..."
            } else {
                qa.answer
            }
        }
    },
    unsafeJso {
        id = QandaVO.tagList.name
        header = StringOrTemplateHeader(QandaVO.tagList.display)
        accessorFn = { qa, _ ->
            qa.tagList?.joinToString(","){ it.tagName }
        }
    },
    unsafeJso {
        id = QandaVO.createTime.name
        header = StringOrTemplateHeader(QandaVO.createTime.display)
        accessorFn = { qa, _ ->
            qa.createTime?.format(DATE_TIME_DEFAULT_FORMAT)
        }
    },
    unsafeJso {
        id = "操作"
        header = StringOrTemplateHeader(id)
        cell = columnDefCell { cellContext ->
            IconButton {
                onClick = {
                    onEdit(cellContext.row.original)
                }
                IconsEdit()
            }
            IconButton {
                onClick = {
                    onDelete(cellContext.row.original)
                }
                IconsDelete()
            }
        }
    }
)

private data class QaFilter(
    val page: PageParams,
    val isAsc: Boolean?,
    val tagId: Int?,
)

private val QandaListFC = FC {
    val (params, setParams) = useState {
        QaFilter(PageParams(), null, null)
    }
    var qaPage: PageVO<QandaVO>? by useState()
    val selectState = useRowSelectionState()
    val cs = useCoroutineScope()
//    val notifications = useNotifications()
    val apiQanda: ApiQanda = useInject()


    val query = useQuery(params) {
        apiQanda.page(params.page.index, params.page.size, params.isAsc, params.tagId)
    }

//    val reactNavigate = useNavigate()

    val onEdit: (QandaVO) -> Unit = { vo ->
//        reactNavigate("/qanda/add/${vo.id}")
    }

    val onDelete: (QandaVO) -> Unit = { vo ->
        cs.launch {
            apiQanda.delete(vo.id)
                .onLeft {
//                    notifications.show("${vo.question} $it", severity = SeverityMui.error)
                }.onRight {
                    query.refresh()
//                    notifications.show("${vo.question} 已删除")
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
                onEdit = onEdit,
                onDelete = onDelete
            ),
            data = tableData,
            onRowSelectionChange = OnChangeFn(selectState.onSelectChange),
            getCoreRowModel = getCoreRowModel(),
        ).setState(
            rowSelection = selectState.rows,
        )
    )


    TableMenu {
        onRefresh = query::refresh
        onDeleteSelect = {
            val ids = qaTable.getSelectedRowModel().flatRows.map { it.original.id }
            if (ids.isNotEmpty()) cs.launch {
                val count = apiQanda.delete(ids)
                query.refresh()
//                notifications.show("删了 $count 条数据")
            }
        }
        onTagId = {
            setParams(params.copy(tagId = it))
        }
        onAsc = {
            setParams(params.copy(isAsc = it))
        }
    }

    AdminPageTable {
        table = qaTable
        pageCount = qaPage?.totalRow
        page = params.page
        onPage = {
            setParams(
                params.copy(
                    page = params.page.copy(index = it),
                )
            )
        }
        onPageParams = {
            setParams(params.copy(page = it))
        }
    }
}


private external interface TableMenuProps : Props {
    var onRefresh: () -> Unit

    var onDeleteSelect: () -> Unit

    var onTagId: (Int?) -> Unit

    var onAsc: (Boolean?) -> Unit
}


private val TableMenu = FC<TableMenuProps> { props ->

    val (excelFile, setFile) = useState<File>()
//    val notifications = useNotifications()
//    val dialog = useDialogs()

    Stack {
        direction = responsive(StackDirection.row)
        spacing = responsive(2)

        MuiFileInputSingle {
            sx {
                width = 180.px
            }
            placeholder = "点击选择文件"
            valueFile = excelFile
            onChange = setFile.invokeFn
            clearIconButtonProps = unsafeJso {
                children = IconsDelete.create()
            }
        }

        Button {
            onClick = asyncFunction { event ->
                event.preventDefault()
                val f = excelFile ?: return@asyncFunction

                Req.uploadExcel(f)

//                notifications.show("已更新")

                props.onRefresh()
            }

            +"上传excel"
        }

        Button {

            onClick = asyncFunction {
//                val b = dialog.confirm(reactNode("是否删除选中"), unsafeJso {
//                    okText = reactNode("删除")
//                    cancelText = reactNode("取消")
//                }).await()

//                if (b) props.onDeleteSelect()

            }.asDynamic()

            IconsDelete()

            +"删除选中"
        }

        FormControlLabel {
            label = reactNode("倒序")
            labelPlacement = LabelPlacement.bottom
            control = Switch.create {
                onChange = { e, b ->
                    props.onAsc(!b)
                }
            }
        }

        FilterTag {
            onTagId = props.onTagId
        }

    }

}

external interface FilterTagProps : Props {
    var onTagId: (Int?) -> Unit
}


private val FilterTag: FC<FilterTagProps> = FC { props ->
    val apiQandaTag: ApiQandaTag = useInject()
    val tags = useQuery<List<QaTagVO>> { apiQandaTag.getAll() }
    var tagId: Any by useState("")

    FormControl {
        InputLabel {
            id = ElementId("")
            htmlFor = "select".asDynamic()
            +"过滤标签"
        }
        Select {
            sx = unsafeJso {
                minWidth = 200.px
            }
            value = tagId
            label = reactNode("过滤标签")
            onChange = { e, r ->
                val n: Any = e.target.value
                tagId = n
                if (n is Int) {
                    props.onTagId(n)
                } else {
                    props.onTagId(null)
                }
            }
            MenuItem {
                value = ""
                +"请选择标签"
            }
            tags.data?.forEach {
                MenuItem {
                    key = it.id.toString()
                    value = it.id
                    +it.tagName
                }
            }
        }

    }
}


//val RouteQandaList = routes(
//    "list", "问题列表", QandaListFC
//)
