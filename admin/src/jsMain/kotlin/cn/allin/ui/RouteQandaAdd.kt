package cn.allin.ui

import cn.allin.VoFieldName
import cn.allin.api.ApiQanda
import cn.allin.api.ApiQandaTag
import cn.allin.components.AdminForm
import cn.allin.components.useAdminForm
import cn.allin.net.useQuery
import cn.allin.utils.invokeFn
import cn.allin.utils.reactNode
import cn.allin.utils.rsv
import cn.allin.utils.useInject
import cn.allin.vo.QaTagVO
import cn.allin.vo.QandaVO
import js.objects.jso
import mui.material.Card
import mui.material.CardHeader
import mui.material.Divider
import mui.material.FormControl
import mui.material.FormHelperText
import mui.material.FormLabel
import mui.material.Grid
import mui.material.List
import mui.material.ListItemButton
import mui.material.ListItemText
import mui.material.TextField
import mui.material.TextareaAutosize
import mui.system.sx
import react.FC
import react.Props
import react.dom.aria.AriaRole
import react.dom.html.ReactHTML.div
import react.router.RouteObject
import react.useState
import toolpad.core.NavigationObj
import web.cssom.Auto
import web.cssom.pct
import web.cssom.px


private val AddQandaFC = FC {
    val apiQanda: ApiQanda = useInject()
    val (tags, setTags) = useState<List<QaTagVO>>(emptyList())
    var qaForm by useState {
        QandaVO(0, "", "")
    }

    val adminForm = useAdminForm()

    AdminForm {
        formState = adminForm
        dataId = "qaId"
        getData = { id ->
            val qa = apiQanda.get(id.toInt())
            setTags(qa.tagList ?: emptyList())
            qaForm = qa
        }
        onSubmit = {
            QandaVO.valid(qaForm.copy(tagList = tags))
                .onLeft {
                    adminForm.setErrorHelper(it)
                }
                .onRight { qa ->
                    adminForm.setErrorHelper(null)

                    if (qa.id > 0) {
                        apiQanda.update(qa)
                    } else {
                        apiQanda.add(qa)
                        qaForm = QandaVO(0, "", "")
                        setTags(emptyList())
                    }
                }

        }

        FormControl {
            TextField {
                label = reactNode {
                    +"问题"
                }
                adminForm.register(this, VoFieldName.QandaVO_question, qaForm.question) {
                    qaForm = qaForm.copy(question = it)
                }
            }
        }

        FormControl {
            FormLabel {
                +"回答"
            }
            TextareaAutosize {
                minRows = 3
                style = jso {
                    width = 100.pct
                }
                name = VoFieldName.QandaVO_answer
                value = qaForm.answer
                onChange = {
                    qaForm = qaForm.copy(
                        answer = it.target.value
                    )
                }
            }
            FormHelperText {
                +adminForm.register(this, VoFieldName.QandaVO_answer)
            }
        }

        SelectTab {
            tagSelect = tags
            onTags = setTags.invokeFn
        }
    }
}

private external interface SelectTabProps : Props {
    var tagSelect: List<QaTagVO>
    var onTags: (List<QaTagVO>) -> Unit
}

private val SelectTab = FC<SelectTabProps> { props ->
    val apiQandaTag: ApiQandaTag = useInject()

    val query = useQuery<List<QaTagVO>> {
        apiQandaTag.getAll()
    }

    Grid {
        container = true
        spacing = 2.rsv
        Grid {
            item = true
            TagList {
                title = "标题列表"
                data = query.data
                onItem = { tag ->
                    if (tag !in props.tagSelect) {
                        val tags = props.tagSelect + tag
                        props.onTags(tags)
                    }

                }
            }
        }

        Grid {
            item = true
            TagList {
                title = "已添加的标签"
                data = props.tagSelect
                onItem = { tag ->
                    val tags = props.tagSelect - tag
                    props.onTags(tags)
                }
            }
        }
    }
}

private external interface TagListProps : Props {
    var title: String
    var data: List<QaTagVO>?
    var onItem: (QaTagVO) -> Unit
}

private val TagList = FC<TagListProps> { props ->
    Card {
        CardHeader {
            title = reactNode(props.title)
        }
        Divider()
        List {
            sx {
                width = 200.px
                height = 230.px
                overflow = Auto.auto
            }

            component = div
            role = AriaRole.list
            dense = true

            props.data?.forEach { v ->
                ListItemButton {
                    key = v.id.toString()
                    role = AriaRole.listitem
                    onClick = {
                        props.onItem(v)
                    }
                    ListItemText {
                        primary = reactNode(v.tagName)
                    }
                }
            }
        }
    }
}

val RouteQandaAdd = object : Routes {

    override val navigation: NavigationObj = jso {
        title = "添加问答"
        segment = "add/-1"
    }

    override val routeObj: RouteObject = jso {
        Component = AddQandaFC
        path = "add/:qaId"
    }
}
