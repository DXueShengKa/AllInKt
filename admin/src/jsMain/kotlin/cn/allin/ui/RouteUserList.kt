package cn.allin.ui

import cn.allin.address
import cn.allin.api.ApiUser
import cn.allin.birthday
import cn.allin.gender
import cn.allin.name
import cn.allin.utils.invokeFn
import cn.allin.utils.selectColumnDef
import cn.allin.utils.setState
import cn.allin.utils.useCoroutineScope
import cn.allin.utils.useInject
import cn.allin.utils.useRowSelectionState
import cn.allin.vo.Gender
import cn.allin.vo.PageVO
import cn.allin.vo.UserVO
import js.array.ReadonlyArray
import js.objects.jso
import kotlinx.coroutines.launch
import mui.material.Button
import mui.material.Snackbar
import mui.material.SnackbarOriginHorizontal
import mui.material.SnackbarOriginVertical
import mui.material.Stack
import mui.material.StackDirection
import mui.system.responsive
import react.FC
import react.create
import react.useMemo
import react.useState
import tanstack.react.table.useReactTable
import tanstack.table.core.ColumnDef
import tanstack.table.core.StringOrTemplateHeader
import tanstack.table.core.TableOptions
import tanstack.table.core.getCoreRowModel


private val UserColumnDef: ReadonlyArray<ColumnDef<UserVO, String?>> = arrayOf(
    selectColumnDef(),
    jso {
        id = "id"
        header = StringOrTemplateHeader("ID")
        accessorFn = { user, _ ->
            user.id.toString()
        }
    },
    jso {
        id = UserVO.name.name
        header = StringOrTemplateHeader(UserVO.name.display)
        accessorFn = { user, _ ->
            user.name
        }
    },
    jso {
        id = UserVO.gender.name
        header = StringOrTemplateHeader(UserVO.gender.display)
        accessorFn = { user, _ ->
            when (user.gender) {
                Gender.Female -> "女"
                Gender.Male -> "男"
                else -> "未设置"
            }
        }
    },
    jso {
        id = UserVO.birthday.name
        header = StringOrTemplateHeader(UserVO.birthday.display)
        accessorFn = { user, _ ->
            user.birthday?.toString()
        }
    },
    jso {
        id = UserVO.address.name
        header = StringOrTemplateHeader(UserVO.address.display)
        accessorFn = { user, _ ->
            user.address
        }
    },
)


private val UserListFC = FC {
    val (pageParams, setPageParams) = useState(PageParams())
    var userPage: PageVO<UserVO>? by useState()
    val selectState = useRowSelectionState()
    val cs = useCoroutineScope()
    var showMessage by useState(false)

    val apiUser: ApiUser = useInject()

    val query = cn.allin.net.useQuery(pageParams) {
        apiUser.page(pageIndex =  it?.index, pageSize = it?.size)
    }

    val tableData: Array<UserVO> = useMemo(query.data) {
        selectState.clear()
        userPage = query.data
        query.data?.rows?.toTypedArray() ?: emptyArray()
    }

    val uTable = useReactTable<UserVO>(
        options = TableOptions(
            columns = UserColumnDef,
            data = tableData,
            onRowSelectionChange = selectState.onSelectChange,
            getCoreRowModel = getCoreRowModel(),
        ).setState(
            rowSelection = selectState.rows,
        )
    )

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
        onClose = { _, _ ->
            showMessage = false
        }
    }

    Stack {
        direction = responsive(StackDirection.row)

        Button {
            onClick = {
                cs.launch {
                    val ids = uTable.getSelectedRowModel().flatRows.map { it.original.id }
                    if (apiUser.deleteAll(ids) > 0) {
                        showMessage = true
                        query.refresh()
                    }
                }
            }
            +"删除"
        }

        Button {
            onClick = {
                query.refresh()
            }
            +"刷新"
        }
    }

    AdminPageTable {
        table = uTable
        pageCount = userPage?.totalRow
        page = pageParams
        onPage = { i ->
            setPageParams(pageParams.copy(
                    index = i
            ))
        }
        onPageParams = setPageParams.invokeFn
    }
}

val RouteUserList = routes("list", "用户列表", UserListFC)
