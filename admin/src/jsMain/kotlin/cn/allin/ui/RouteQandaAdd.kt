package cn.allin.ui

import cn.allin.ValidatorError
import cn.allin.VoFieldName
import cn.allin.VoValidatorMessage
import cn.allin.api.ApiQanda
import cn.allin.api.ApiQandaTag
import cn.allin.net.useQuery
import cn.allin.utils.invokeFn
import cn.allin.utils.reactNode
import cn.allin.utils.rsv
import cn.allin.utils.useCoroutineScope
import cn.allin.utils.useInject
import cn.allin.vo.QaTagVO
import cn.allin.vo.QandaVO
import js.objects.jso
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mui.base.Button
import mui.base.TextareaAutosize
import mui.material.*
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.dom.aria.AriaRole
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.onChange
import react.router.RouteObject
import react.router.useParams
import react.useEffectOnce
import react.useState
import toolpad.core.NavigationObj
import web.cssom.Auto
import web.cssom.pct
import web.cssom.px
import web.html.ButtonType


private val AddUserFC = FC {
    val cs = useCoroutineScope()
    var addResult: Pair<AlertColor, String>? by useState()
    var errorHelperText: VoValidatorMessage? by useState()
    val apiQanda: ApiQanda = useInject()
    val (tags, setTags) = useState<List<QaTagVO>>(emptyList())
    var qaForm by useState<QandaVO> {
        QandaVO(0, "", "")
    }

    val qaId = useParams()["qaId"]

    useEffectOnce {
        if (qaId.isNullOrBlank()) return@useEffectOnce
        val id = qaId.toInt()
        if (id < 1) return@useEffectOnce
        val qa = apiQanda.get(id)
        setTags(qa.tagList ?: emptyList())
        qaForm = qa
    }

    Stack {
        sx = jso {
            width = 600.px
        }

        spacing = responsive(2)

        direction = responsive(StackDirection.column)

        component = form

        onSubmit = submit@{ formEvent ->
            formEvent.preventDefault()
            val qa = qaForm.copy(tagList = tags)

            QandaVO.valid(qa).onLeft {
                errorHelperText = it
                return@submit
            }

            errorHelperText = null

            cs.launch(CoroutineExceptionHandler { _, t ->
                if (t is ValidatorError)
                    errorHelperText = t.validatorMessage
                console.error(t)
                addResult = AlertColor.error to "添加失败"
            }) {
                if (qa.id > 0) {
                    apiQanda.update(qa)
                    addResult = AlertColor.success to "已更新"
                } else {
                    apiQanda.add(qa)
                    addResult = AlertColor.success to "已添加"
                }
                delay(2000)
                addResult = null
            }
        }

        FormControl {
            TextField {
                label = reactNode {
                    +"问题"
                }
                name = VoFieldName.QandaVO_question
                value = qaForm.question
                onChange = {
                    qaForm = qaForm.copy(question = it.target.asDynamic().value)
                }
                errorHelperText?.also {
                    if (it.field == VoFieldName.QandaVO_question) {
                        error = true
                        helperText = reactNode("${it.code},${it.message}")
                    } else {
                        error = false
                    }
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
                errorHelperText?.also {
                    if (it.field == VoFieldName.QandaVO_answer) {
                        error = true
                        +"${it.code},${it.message}"
                    } else {
                        error = false
                    }
                }
            }
        }

        SelectTab {
            tagSelect = tags
            onTags = setTags.invokeFn
        }

        Button {
            type = ButtonType.submit

            +if (qaForm.id != null) "更新" else "添加"
        }

        addResult?.let { (color, str) ->
            Alert {
                severity = color.asDynamic()
                +str
            }
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

    override val routePath: String = "add/:qaId"

    override val navigation: NavigationObj = jso {
        title = "添加问答"
        segment = "add/-1"
    }

    override val routeObj: RouteObject = jso {
        Component = AddUserFC
        path = routePath
    }
}
