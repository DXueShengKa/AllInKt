package cn.allin.ui

import cn.allin.ServerRoute
import cn.allin.VoFieldName
import cn.allin.getValue
import cn.allin.net.PageParams
import cn.allin.net.ReqUser
import cn.allin.useCoroutineScope
import cn.allin.utils.columnDefCell
import cn.allin.utils.columnDefHeader
import cn.allin.utils.queryFunction
import cn.allin.utils.queryKey
import cn.allin.utils.tanstackBody
import cn.allin.utils.tanstackHead
import cn.allin.vo.Gender
import cn.allin.vo.PageVO
import cn.allin.vo.UserVO
import js.array.ReadonlyArray
import js.objects.jso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mui.material.Button
import mui.material.Checkbox
import mui.material.Paper
import mui.material.Snackbar
import mui.material.SnackbarOriginHorizontal
import mui.material.SnackbarOriginVertical
import mui.material.Stack
import mui.material.StackDirection
import mui.material.Table
import mui.material.TableContainer
import mui.material.TableFooter
import mui.material.TablePagination
import mui.material.TableRow
import mui.system.responsive
import react.FC
import react.create
import react.useMemo
import react.useState
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tanstack.react.table.useReactTable
import tanstack.table.core.ColumnDef
import tanstack.table.core.RowSelectionState
import tanstack.table.core.StringOrTemplateHeader
import tanstack.table.core.getCoreRowModel


const val RouteUserList = "UserList"

private val UserColumnDef: ReadonlyArray<ColumnDef<UserVO, String?>> = arrayOf(
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
        accessorFn = { user, _ ->
            user.id.toString()
        }
    },
    jso {
        id = VoFieldName.UserVO_name
        header = StringOrTemplateHeader("名字")
        accessorFn = { user, _ ->
            user.name
        }
    },
    jso {
        id = VoFieldName.UserVO_gender
        header = StringOrTemplateHeader("性别")
        accessorFn = { user, _ ->
            when (user.gender) {
                Gender.Female -> "女"
                Gender.Male -> "男"
                else -> "未设置"
            }
        }
    },
    jso {
        id = VoFieldName.UserVO_birthday
        header = StringOrTemplateHeader("生日")
        accessorFn = { user, _ ->
            user.birthday?.toString()
        }
    },
    jso {
        id = VoFieldName.UserVO_address
        header = StringOrTemplateHeader("地址")
        accessorFn = { user, _ ->
            user.address
        }
    },
)


val RouteUserListFC = FC {
    var pageParams by useState(PageParams())
    var userPage: PageVO<UserVO>? by useState()
    var rowSelect: RowSelectionState by useState(jso())
    val cs: CoroutineScope? by useCoroutineScope()
    var showMessage by useState(false)

    val query = useQuery<PageVO<UserVO>, Error, PageVO<UserVO>, QueryKey>(options = jso {
        queryKey = queryKey(ServerRoute.USER, pageParams)
        queryFn = queryFunction {
            ReqUser.getUserPage(pageParams)
        }
    })


    val tableData: Array<UserVO> = useMemo(query.data) {
        rowSelect = jso()
        userPage = query.data
        query.data?.rows?.toTypedArray() ?: emptyArray()
    }

    val table = useReactTable<UserVO>(jso {
        columns = UserColumnDef
        data = tableData
        state = jso {
            rowSelection = rowSelect
        }
        onRowSelectionChange = {
            rowSelect = it.asDynamic()
        }
        this.getCoreRowModel = getCoreRowModel()
    })

    Snackbar {
        open = showMessage
        message = FC {
            +"已删除"
        }.create()
        anchorOrigin = jso {
            vertical = SnackbarOriginVertical.top
            horizontal = SnackbarOriginHorizontal.center
        }
        autoHideDuration = 3000
        onClose = { _, _->
            showMessage = false
        }
    }

    Stack {
        direction = responsive(StackDirection.row)

        Button {
            onClick = {
                cs?.launch {
                    val ids = table.getSelectedRowModel().flatRows.map { it.original.id }
                    if (ReqUser.deleteUser(ids)) {
                        showMessage = true
                        query.refetch(jso())
                    }
                }
            }
            +"删除"
        }

        Button {
            onClick = {
                query.refetch(jso())
            }
            +"刷新"
        }
    }

    TableContainer {
        component = Paper
        Table {
            tanstackHead(table.getHeaderGroups())
            tanstackBody(table.getRowModel().rows)
            TableFooter {
                TableRow {
                    TablePagination {
                        rowsPerPageOptions = arrayOf(10, 20, 30)
                        count = userPage?.totalRow ?: 0
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

