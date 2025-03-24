package cn.allin.ui

import cn.allin.ValidatorError
import cn.allin.VoFieldName
import cn.allin.VoValidatorMessage
import cn.allin.getValue
import cn.allin.net.Req
import cn.allin.net.addQanda
import cn.allin.useCoroutineScope
import cn.allin.utils.reactNode
import cn.allin.vo.QandaVO
import js.objects.jso
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mui.material.Alert
import mui.material.AlertColor
import mui.material.Button
import mui.material.FormControl
import mui.material.Stack
import mui.material.StackDirection
import mui.material.TextField
import mui.system.responsive
import org.w3c.dom.HTMLInputElement
import react.FC
import react.dom.events.FormEvent
import react.dom.html.ReactHTML.form
import react.dom.onChange
import react.useState
import web.cssom.px
import web.html.ButtonType
import web.html.HTMLElement


private val AddUserFC = FC {
    val cs by useCoroutineScope()
    var userForm: QandaVO by useState { QandaVO(null, "", "") }
    var addResult: Pair<AlertColor, String>? by useState()
    var errorHelperText: VoValidatorMessage? by useState()

    val handle: (FormEvent<HTMLElement>) -> Unit = {
        val t = it.target as HTMLInputElement
        when (t.name) {
            VoFieldName.QandaVO_answer -> {
                userForm = userForm.copy(answer = t.value).also {
                    errorHelperText = VoValidatorMessage.qanda(it, VoFieldName.QandaVO_answer)
                }
            }

            VoFieldName.QandaVO_question -> {
                userForm = userForm.copy(question = t.value).also {
                    errorHelperText = VoValidatorMessage.qanda(it, VoFieldName.QandaVO_question)
                }
            }

        }
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

            cs?.launch(CoroutineExceptionHandler { _, t ->
                if (t is ValidatorError)
                    errorHelperText = t.validatorMessage

                addResult = AlertColor.error to "添加失败"
            }) {
                Req.addQanda(userForm)
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

val RouteQandaAdd = routes("add", "添加问答", AddUserFC)
