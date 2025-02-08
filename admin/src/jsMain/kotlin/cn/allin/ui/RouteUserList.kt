package cn.allin.ui

import cn.allin.net.PageParams
import cn.allin.net.ReqUser
import cn.allin.utils.tanstackBody
import cn.allin.utils.tanstackHead
import cn.allin.vo.Gender
import cn.allin.vo.PageVO
import cn.allin.vo.UserVO
import js.array.ReadonlyArray
import js.objects.jso
import mui.material.*
import react.FC
import react.useEffect
import react.useState
import tanstack.react.table.useReactTable
import tanstack.table.core.ColumnDef
import tanstack.table.core.StringOrTemplateHeader
import tanstack.table.core.getCoreRowModel


const val RouteUserList = "UserList"

private val UserColumnDef: ReadonlyArray<ColumnDef<UserVO, String?>> = arrayOf(
    jso {
        id = "name"
        header = StringOrTemplateHeader("名字")
        accessorFn = { user, _ ->
            user.name
        }
    },
    jso {
        id = "gender"
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
        id = "birthday"
        header = StringOrTemplateHeader("生日")
        accessorFn = { user, _ ->
            user.birthday?.toString()
        }
    },
    jso {
        id = "address"
        header = StringOrTemplateHeader("地址")
        accessorFn = { user, _ ->
            user.address
        }
    },
)

val RouteUserListFC = FC {
    var pageParams by useState(PageParams())
    var userPage: PageVO<UserVO>? by useState()
    var tableData: Array<UserVO> by useState {
        userPage?.rows?.toTypedArray()?:emptyArray()
    }

//    val query = useQuery<PageVO<UserVO>, Error, PageVO<UserVO>, QueryKey>(options = jso {
//        queryKey = QueryKey(ServerRoute.USER, "page")
//        queryFn = createQueryFunction {
//            ReqUser.getUserPage(pageParams)
//        }
//    })

    useEffect(pageParams) {
        val up = ReqUser.getUserPage(pageParams)
        tableData = up.rows.toTypedArray()
        userPage = up
    }

    val table = useReactTable<UserVO>(jso {
        columns = UserColumnDef
        data = tableData
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

