package cn.allin.ui

import cn.allin.ValidatorError
import cn.allin.VoField
import cn.allin.VoValidatorMessage
import cn.allin.api.ApiUser
import cn.allin.birthday
import cn.allin.components.SelectAddress
import cn.allin.email
import cn.allin.gender
import cn.allin.name
import cn.allin.password
import cn.allin.utils.dayjs
import cn.allin.utils.reactNode
import cn.allin.utils.toLocalDate
import cn.allin.utils.useCoroutineScope
import cn.allin.utils.useInject
import cn.allin.vo.Gender
import cn.allin.vo.UserVO
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
import mui.system.responsive
import mui.system.sx
import muix.pickers.DatePicker
import react.FC
import react.create
import react.dom.events.FormEvent
import react.dom.html.ReactHTML.form
import react.dom.onChange
import react.useState
import web.cssom.px
import web.html.ButtonType
import web.html.HTMLElement
import web.html.HTMLInputElement
import web.html.InputType
import web.html.password
import web.html.submit


private val AddUserFC = FC {
    val cs = useCoroutineScope()
    var userForm: UserVO by useState { UserVO() }
    var addResult: Pair<AlertColor, String>? by useState()
    var errorHelperText: VoValidatorMessage? by useState()

    val apiUser: ApiUser = useInject()

    val handle: (FormEvent<HTMLElement>) -> Unit = {
        val t = it.target as HTMLInputElement
        when (t.name) {
            UserVO.name.name -> {
                userForm = userForm.copy(name = t.value)
            }

            UserVO.email.name -> {
                userForm = userForm.copy(email = t.value)
            }

            UserVO.password.name -> userForm = userForm.copy(password = t.value)
        }

        errorHelperText = UserVO.valid(userForm).leftOrNull()
    }

    Stack {
        sx {
            width = 600.px
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
                        apiUser.add(it)
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
                name = UserVO.name.name
                onChange = handle
                validatorMessage(errorHelperText, UserVO.name)
            }
        }

        FormControl {
            TextField {
                label = reactNode {
                    +"邮箱"
                }
                name = UserVO.email.name
                onChange = handle
                validatorMessage(errorHelperText, UserVO.email)
            }
        }

        FormControl {
            TextField {
                label = reactNode {
                    +"密码"
                }
                name = UserVO.password.name
                type = InputType.password
                onChange = handle
                validatorMessage(errorHelperText, UserVO.password)
            }
        }

        FormControl {
            DatePicker {
                label = reactNode { +"生日" }
                name = UserVO.birthday.name
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
                name = UserVO.gender.name
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

private fun BaseTextFieldProps.validatorMessage(errorMsg: VoValidatorMessage?, field: VoField) {
    errorMsg?.also {
        error = if (it.field == field.name) {
            helperText = reactNode("${it.code},${it.message}")
            true
        } else {
            false
        }
    }
}

val RouteUserAdd = routes("add", "添加用户", AddUserFC)
