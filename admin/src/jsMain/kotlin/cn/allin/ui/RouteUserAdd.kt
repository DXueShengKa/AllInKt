package cn.allin.ui

import cn.allin.ValidatorError
import cn.allin.VoFieldName
import cn.allin.VoValidatorMessage
import cn.allin.net.Req
import cn.allin.net.addUser
import cn.allin.utils.dayjs
import cn.allin.utils.getValue
import cn.allin.utils.reactNode
import cn.allin.utils.toLocalDate
import cn.allin.utils.useCoroutineScope
import cn.allin.vo.Gender
import cn.allin.vo.UserVO
import js.objects.jso
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mui.material.Alert
import mui.material.AlertColor
import mui.material.BaseTextFieldProps
import mui.material.Button
import mui.material.FormControl
import mui.material.FormControlLabel
import mui.material.FormLabel
import mui.material.Radio
import mui.material.RadioGroup
import mui.material.Stack
import mui.material.StackDirection
import mui.material.TextField
import mui.material.styles.Theme
import mui.material.styles.useTheme
import mui.system.responsive
import muix.pickers.DatePicker
import org.w3c.dom.HTMLInputElement
import react.FC
import react.create
import react.dom.events.FormEvent
import react.dom.html.ReactHTML.form
import react.dom.onChange
import react.useState
import web.cssom.Color
import web.cssom.px
import web.html.ButtonType
import web.html.HTMLElement
import web.html.InputType


private val AddUserFC = FC {
    val theme = useTheme<Theme>()
    val cs by useCoroutineScope()
    var userForm: UserVO by useState { UserVO() }
    var addResult: Pair<AlertColor, String>? by useState()
    var errorHelperText: VoValidatorMessage? by useState()

    val handle: (FormEvent<HTMLElement>) -> Unit = {
        val t = it.target as HTMLInputElement
        when (t.name) {
            VoFieldName.UserVO_name -> {
                userForm = userForm.copy(name = t.value)
            }

            VoFieldName.UserVO_email -> {
                userForm = userForm.copy(email = t.value)
            }

            VoFieldName.UserVO_password -> userForm = userForm.copy(password = t.value)
        }

        errorHelperText = UserVO.valid(userForm).leftOrNull()
    }

    Stack {
        sx = jso {
            width = 600.px
            backgroundColor = Color(theme.palette.background.default)
        }

        spacing = responsive(2)

        direction = responsive(StackDirection.column)

        component = form

        onSubmit = submit@{ fe ->
            fe.preventDefault()

            UserVO.valid(userForm)
                .onLeft {
                    errorHelperText = it
                }
                .onRight {
                    errorHelperText = null
                    cs.launch(CoroutineExceptionHandler { _, t ->
                        if (t is ValidatorError)
                            errorHelperText = t.validatorMessage

                        addResult = AlertColor.error to "添加失败"
                    }) {
                        Req.addUser(it)
                        addResult = AlertColor.success to "已添加"
                        delay(2000)
                        addResult = null
                    }
                }

        }

        FormControl {
            TextField {
                label = reactNode {
                    +"姓名"
                }
                name = VoFieldName.UserVO_name
                onChange = handle
                validatorMessage(errorHelperText, VoFieldName.UserVO_name)
            }
        }

        FormControl {
            TextField {
                label = reactNode {
                    +"邮箱"
                }
                name = VoFieldName.UserVO_email
                onChange = handle
                validatorMessage(errorHelperText, VoFieldName.UserVO_email)
            }
        }

        FormControl {
            TextField {
                label = reactNode {
                    +"密码"
                }
                name = VoFieldName.UserVO_password
                type = InputType.password
                onChange = handle
                validatorMessage(errorHelperText, VoFieldName.UserVO_password)
            }
        }

        FormControl {
            DatePicker {
                label = reactNode { +"生日" }
                name = VoFieldName.UserVO_birthday
                minDate = dayjs("1900-1-1")
                maxDate = dayjs()
                onChange = {
                    userForm = userForm.copy(
                        birthday = it.toLocalDate()
                    )
                }
            }
        }

        FormControl {
            FormLabel {
                +"性别"
            }
            RadioGroup {
                name = VoFieldName.UserVO_gender
                row = true
                value = userForm.gender
                onChange = { _, g ->
                    userForm = userForm.copy(
                        gender = Gender.valueOf(g)
                    )
                }

                FormControlLabel {
                    value = Gender.Male
                    control = Radio.create()
                    label = reactNode("男")
                }

                FormControlLabel {
                    value = Gender.Female
                    control = Radio.create()
                    label = reactNode("女")
                }
            }
        }

        FormControl {
            SelectAddress {
                onValue = {
                    userForm = userForm.copy(address = it)
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

private fun BaseTextFieldProps.validatorMessage(errorMsg: VoValidatorMessage?, field: String) {
    errorMsg?.also {
        error = if (it.field == field) {
            helperText = reactNode("${it.code},${it.message}")
            true
        } else {
            false
        }
    }
}

val RouteUserAdd = routes("add", "添加用户", AddUserFC)
