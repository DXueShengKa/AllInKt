package cn.allin.ui

import cn.allin.VoFieldName
import cn.allin.api.ApiQandaTag
import cn.allin.utils.reactNode
import cn.allin.utils.useCoroutineScope
import cn.allin.utils.useInject
import cn.allin.vo.QaTagVO
import kotlinx.coroutines.launch
import mui.material.Button
import mui.material.FormControl
import mui.material.Stack
import mui.material.StackDirection
import mui.material.TextField
import mui.system.responsive
import mui.system.sx
import react.FC
import react.dom.html.ReactHTML.form
import toolpad.core.show
import toolpad.core.useNotifications
import web.cssom.px
import web.form.FormData
import web.html.ButtonType

private val TagAddFC = FC {
    val cs = useCoroutineScope()
    val apiQandaTag: ApiQandaTag = useInject()
    val notifications = useNotifications()

    Stack {
        sx {
            width = 600.px
        }
        spacing = responsive(2)
        direction = responsive(StackDirection.column)
        component = form

        onSubmit = {
            it.preventDefault()
            val fa = FormData(it.target)
            val tag = QaTagVO(
                tagName = fa.get(VoFieldName.QaTagVO_tagName).toString(),
                description = fa.get(VoFieldName.QaTagVO_description)?.toString()
            )
            cs.launch {
                apiQandaTag.add(tag)
                notifications.show("已添加")
            }
        }

        FormControl {
            TextField {
                label = reactNode {
                    +"标签名"
                }
                name = VoFieldName.QaTagVO_tagName
//                onChange = handle
            }
        }

        FormControl {
            TextField {
                label = reactNode {
                    +"介绍标签"
                }
                name = VoFieldName.QaTagVO_description
//                onChange = handle

            }
        }

        Button {
            type = ButtonType.submit
            +"添加"
        }

    }
}


val RouteTagAdd = routes("tag/add", "添加标签", TagAddFC)
