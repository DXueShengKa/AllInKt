package cn.allin.ui

import cn.allin.api.ApiQandaTag
import cn.allin.createTime
import cn.allin.description
import cn.allin.net.useQuery
import cn.allin.tagName
import cn.allin.utils.DATE_TIME_DEFAULT_FORMAT
import cn.allin.utils.columnDefCell
import cn.allin.utils.useCoroutineScope
import cn.allin.utils.useInject
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import js.array.ReadonlyArray
import js.objects.unsafeJso
import kotlinx.coroutines.launch
import kotlinx.datetime.format
import mui.material.IconButton
import muix.icons.IconsDelete
import muix.icons.IconsEdit
import react.FC
import react.useMemo
import react.useState
import tanstack.react.table.useReactTable
import tanstack.table.core.ColumnDef
import tanstack.table.core.StringOrTemplateHeader
import tanstack.table.core.TableOptions
import tanstack.table.core.getCoreRowModel


private fun tagListColumnDef(
    onEdit: (QaTagVO) -> Unit,
    onDelete: (QaTagVO) -> Unit,
): ReadonlyArray<ColumnDef<QaTagVO, String?>> = arrayOf(
    unsafeJso {
        id = "id"
        header = StringOrTemplateHeader(id)
        accessorFn = { tag, _ ->
            tag.id.toString()
        }
    },
    unsafeJso {
        id = QaTagVO.tagName.name
        header = StringOrTemplateHeader(QaTagVO.tagName.display)
        accessorFn = { tag, _ ->
            tag.tagName
        }
    },
    unsafeJso {
        id = QaTagVO.description.name
        header = StringOrTemplateHeader(QaTagVO.description.display)
        accessorFn = { tag, _ ->
            tag.description
        }
    },
    unsafeJso {
        id = QaTagVO.createTime.name
        header = StringOrTemplateHeader(QaTagVO.createTime.display)
        accessorFn = { tag, _ ->
            tag.createTime?.format(DATE_TIME_DEFAULT_FORMAT)
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
    })

private val TagListFC = FC {
    val (pageParams, setPageParams) = useState(PageParams())
    var userPage: PageVO<QaTagVO>? by useState()
    val apiQandaTag: ApiQandaTag = useInject()
//    val notification = useNotifications()
//    val reactNavigate = useNavigate()
    val cs = useCoroutineScope()

    val query = useQuery(pageParams) {
        apiQandaTag.page(pageParams.index, pageParams.size)
    }

    val tableData: Array<QaTagVO> = useMemo(query.data) {
        userPage = query.data
        query.data?.rows?.toTypedArray() ?: emptyArray()
    }

    val tagTable = useReactTable<QaTagVO>(
        TableOptions(
            columns = tagListColumnDef(
                onEdit = { tag ->
//                    reactNavigate("/qanda/tag/add/${tag.id}")
                },
                onDelete = { tag ->
                    cs.launch {
                        apiQandaTag.delete(tag.id)
                            .onLeft {
//                                notification.show(it, severity = SeverityMui.error)
                            }
                            .onRight {
                                query.refresh()
//                                notification.show("删除${tag.tagName}")
                            }
                    }
                }),
            data = tableData,
            getCoreRowModel = getCoreRowModel()
        )
    )

    AdminPageTable {
        table = tagTable
        page = pageParams
        pageCount = userPage?.totalRow
    }

}

