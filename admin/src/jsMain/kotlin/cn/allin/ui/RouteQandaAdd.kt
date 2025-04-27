package cn.allin.ui

import cn.allin.ValidatorError
import cn.allin.VoFieldName
import cn.allin.VoValidatorMessage
import cn.allin.api.ApiQanda
import cn.allin.api.ApiQandaTag
import cn.allin.net.useQuery
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
import mui.material.Alert
import mui.material.AlertColor
import mui.material.Button
import mui.material.Card
import mui.material.CardHeader
import mui.material.Divider
import mui.material.FormControl
import mui.material.Grid
import mui.material.List
import mui.material.ListItemButton
import mui.material.ListItemText
import mui.material.Stack
import mui.material.StackDirection
import mui.material.TextField
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props
import react.dom.aria.AriaRole
import react.dom.events.FormEvent
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.form
import react.dom.onChange
import react.useState
import web.cssom.Auto
import web.cssom.px
import web.html.ButtonType
import web.html.HTMLElement
import web.html.HTMLInputElement


private val AddUserFC = FC {
    val cs = useCoroutineScope()
    var userForm: QandaVO by useState { QandaVO(null, "", "") }
    var addResult: Pair<AlertColor, String>? by useState()
    var errorHelperText: VoValidatorMessage? by useState()
    val apiQanda: ApiQanda = useInject()

    val handle: (FormEvent<HTMLElement>) -> Unit = {
        val t = it.target as HTMLInputElement
        when (t.name) {
            VoFieldName.QandaVO_answer -> {
                userForm = userForm.copy(answer = t.value)
            }

            VoFieldName.QandaVO_question -> {
                userForm = userForm.copy(question = t.value)
            }
        }

        errorHelperText = QandaVO.valid(userForm).leftOrNull()
    }

    Stack {
        sx = jso {
            width = 600.px
        }

        spacing = responsive(2)

        direction = responsive(StackDirection.column)

        component = form

        onSubmit = submit@{
            it.preventDefault()
            val v = VoValidatorMessage.validator(userForm)
            if (v != null) {
                errorHelperText = v
                return@submit
            } else {
                errorHelperText = null
            }

            cs.launch(CoroutineExceptionHandler { _, t ->
                if (t is ValidatorError)
                    errorHelperText = t.validatorMessage
                console.error(t)
                addResult = AlertColor.error to "添加失败"
            }) {
                apiQanda.add(userForm)
                addResult = AlertColor.success to "已添加"
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
                onChange = handle

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
            TextField {
                label = reactNode {
                    +"回答"
                }
                name = VoFieldName.QandaVO_answer
                onChange = handle

                errorHelperText?.also {
                    if (it.field == VoFieldName.QandaVO_answer) {
                        error = true
                        helperText = reactNode("${it.code},${it.message}")
                    } else {
                        error = false
                    }
                }
            }
        }

        SelectTab {
            onTabs = {
                userForm = userForm.copy(tagIds = it)
            }
        }

        Button {
            type = ButtonType.submit
            +"添加"
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
    var onTabs: (List<Int>) -> Unit
}

private val SelectTab = FC<SelectTabProps> { props ->
    val apiQandaTag: ApiQandaTag = useInject()
    var tagSelect: List<QaTagVO> by useState(emptyList())

    val query = useQuery(null) {
        apiQandaTag.page(0, 100).rows
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
                    if (tag !in tagSelect) {
                        val tags = tagSelect + tag
                        props.onTabs(tags.map { it.id })
                        tagSelect = tags
                    }

                }
            }
        }

        Grid {
            item = true
            TagList {
                title = "已添加的标签"
                data = tagSelect
                onItem = { tag ->
                    val tags = tagSelect - tag
                    props.onTabs(tags.map { it.id })
                    tagSelect = tags
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

val RouteQandaAdd = routes("add", "添加问答", AddUserFC)
