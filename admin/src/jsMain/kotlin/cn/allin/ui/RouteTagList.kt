package cn.allin.ui

import cn.allin.VoFieldName
import cn.allin.net.getQaTagPage
import cn.allin.net.useQuery
import cn.allin.vo.PageVO
import cn.allin.vo.QaTagVO
import js.array.ReadonlyArray
import js.objects.jso
import react.FC
import react.useMemo
import react.useState
import tanstack.react.table.useReactTable
import tanstack.table.core.ColumnDef
import tanstack.table.core.StringOrTemplateHeader
import tanstack.table.core.TableOptions
import tanstack.table.core.getCoreRowModel


private fun tagListColumnDef(

): ReadonlyArray<ColumnDef<QaTagVO, String?>> = arrayOf(
    jso {
        id = "id"
        header = StringOrTemplateHeader(id)
        accessorFn = { tag, _ ->
            tag.id.toString()
        }
    },
    jso {
        id = VoFieldName.QaTagVO_tagName
        header = StringOrTemplateHeader("标签名")
        accessorFn = { tag, _ ->
            tag.tagName
        }
    },
    jso {
        id = VoFieldName.QaTagVO_description
        header = StringOrTemplateHeader("介绍")
        accessorFn = { tag, _ ->
            tag.description
        }
    },
    jso {
        id = VoFieldName.QaTagVO_createTime
        header = StringOrTemplateHeader("创建时间")
        accessorFn = { tag, _ ->
            tag.createTime.toString()
        }
    }
)

private val TagListFC = FC {
    val (pageParams, setPageParams) = useState(PageParams())
    var userPage: PageVO<QaTagVO>? by useState()

    val query = useQuery(pageParams) {
        getQaTagPage(pageParams)
    }

    val tableData: Array<QaTagVO> = useMemo(query.data) {
        userPage = query.data
        query.data?.rows?.toTypedArray() ?: emptyArray()
    }

    val tagTable = useReactTable<QaTagVO>(TableOptions(
        columns = tagListColumnDef(),
        data = tableData,
        getCoreRowModel = getCoreRowModel()
    ))

    AdminPageTable {
        table = tagTable
        page = pageParams
        pageCount = userPage?.totalRow
    }

}

val RouteTagList = routes("tag/list", "标签列表", TagListFC)
