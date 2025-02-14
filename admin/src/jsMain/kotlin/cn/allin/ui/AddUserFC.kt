package cn.allin.ui

import cn.allin.VoValidator
import cn.allin.birthday
import cn.allin.gender
import cn.allin.getValue
import cn.allin.name
import cn.allin.net.ReqUser
import cn.allin.password
import cn.allin.useCoroutineScope
import cn.allin.utils.dayjs
import cn.allin.utils.reactNode
import cn.allin.utils.toLocalDate
import cn.allin.vo.Gender
import cn.allin.vo.UserVO
import js.objects.jso
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mui.material.Alert
import mui.material.AlertColor
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


const val RouteAddUser = "addUser"


val RouteAddUserFC = FC {
    val theme = useTheme<Theme>()
    val cs by useCoroutineScope()
    var userForm: UserVO by useState { UserVO(name = "") }
    var addResult by useState(false)
    var error: String? by useState(null)

    val handle: (FormEvent<HTMLElement>) -> Unit = {
        val t = it.target as HTMLInputElement
        when (t.name) {
            UserVO.name -> userForm = userForm.copy(name = t.value)
            UserVO.password -> userForm = userForm.copy(password = t.value)
        }
    }

    Stack {
        sx = jso {
            width = 600.px
            backgroundColor = Color(theme.palette.background.default)
        }

        spacing = responsive(2)

        direction = responsive(StackDirection.column)

        component = form

        onSubmit = submit@{
            it.preventDefault()
            val v = VoValidator.user(userForm)
            if (v != null) {
                error = v.message
                return@submit
            } else {
                error = null
            }

            cs?.launch {
                addResult = ReqUser.addUser(userForm)
                delay(2000)
                addResult = false
            }
        }

        FormControl {
            TextField {
                label = reactNode {
                    +"姓名"
                }
                name = UserVO.name
                onChange = handle
            }
        }
        FormControl {
            TextField {
                label = reactNode {
                    +"密码"
                }
                name = UserVO.password
                type = InputType.password
                onChange = handle
            }
        }
        FormControl {
            DatePicker {
                label = reactNode { +"生日" }
                name = UserVO.birthday
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
                name = UserVO.gender
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
                    println(it)
                    userForm = userForm.copy(address = it)
                }
            }
        }

        Button {
            type = ButtonType.submit
            +"添加"
        }

        if (addResult) Alert {
            severity = AlertColor.success.asDynamic()
            +"添加成功"
        }
        error?.also {
            Alert {
                severity = AlertColor.error.asDynamic()
                +it
            }
        }
    }

}
