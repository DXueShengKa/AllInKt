package cn.allin.ui

import cn.allin.VoFieldName
import cn.allin.net.Req
import cn.allin.net.deleteQanda
import cn.allin.net.getQandaPage
import cn.allin.net.uploadExcel
import cn.allin.net.useQuery
import cn.allin.utils.DATE_TIME_DEFAULT_FORMAT
import cn.allin.utils.asyncFunction
import cn.allin.utils.columnDefCell
import cn.allin.utils.columnDefHeader
import cn.allin.utils.getValue
import cn.allin.utils.invokeFn
import cn.allin.utils.useCoroutineScope
import cn.allin.vo.PageVO
import cn.allin.vo.QandaVO
import js.array.ReadonlyArray
import js.objects.jso
import kotlinx.coroutines.launch
import kotlinx.datetime.format
import mui.material.Button
import mui.material.Checkbox
import mui.material.IconButton
import mui.material.Input
import mui.material.Stack
import mui.material.StackDirection
import mui.system.responsive
import muix.icons.IconsDelete
import react.FC
import react.dom.html.ReactHTML.form
import react.useMemo
import react.useRef
import react.useState
import tanstack.react.table.useReactTable
import tanstack.table.core.ColumnDef
import tanstack.table.core.RowSelectionState
import tanstack.table.core.StringOrTemplateHeader
import tanstack.table.core.TableOptions
import tanstack.table.core.getCoreRowModel
import toolpad.core.useNotifications
import web.file.File
import web.html.ButtonType
import web.html.InputType

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
    var rowSelect: RowSelectionState by useState(jso())
    val cs by useCoroutineScope()
    val notifications = useNotifications()
    val excelFile = useRef<File>()


    val query = useQuery(pageParams) {
        getQandaPage(pageParams)
    }

    val onDelete: (QandaVO) -> Unit = {
        cs.launch {
            val msg = Req.deleteQanda(it.id ?: return@launch)
            query.refresh()
            if (msg.isSuccess) {
                notifications.show("${it.question} 已删除", jso {
                    autoHideDuration = 2000
                })
            }
        }
    }

    val tableData: Array<QandaVO> = useMemo(query.data) {
        rowSelect = jso()
        qaPage = query.data
        query.data?.rows?.toTypedArray() ?: emptyArray()
    }


    val qaTable = useReactTable(
        TableOptions<QandaVO>(
            columns = qaListColumnDef(
                onDelete = onDelete
            ),
            data = tableData,
            onRowSelectionChange = {
                rowSelect = it.asDynamic()
            },
            getCoreRowModel = getCoreRowModel(),
        )
    )


    Stack {
        component = form
        direction = responsive(StackDirection.row)

        onSubmit = asyncFunction { event ->
            event.preventDefault()
            val f = excelFile.current ?: return@asyncFunction

            Req.uploadExcel(f)

            notifications.show("已更新", jso {
                autoHideDuration = 2000
            })
            query.refresh()
        }

        Input {
            id = "excelFile"
            type = InputType.file.toString()
            name = "file"
            onChange = { e ->
                val t = e.target as web.html.HTMLInputElement
                t.files?.also {
                    excelFile.current = it[0]
                }
            }
        }

        Button {
            type = ButtonType.submit
            +"上传"
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


val RouteQandaList = routes(
    "list", "问题列表", QandaListFC
)
