package cn.allin.ui

import ant.Button
import ant.ButtonType
import ant.DatePicker
import ant.Form
import ant.Input
import ant.Radio
import ant.form
import ant.radioOptions
import ant.rule
import cn.allin.ViewModel
import cn.allin.net.ReqUser
import cn.allin.viewModelFc
import cn.allin.vo.Gender
import emotion.react.css
import kotlinx.coroutines.launch
import react.dom.html.ReactHTML.div
import web.cssom.px


const val RouteAddUser = "addUser"

private class AddUserVM : ViewModel() {
    init {
        println("addUserVM")
    }

    fun add(addUser: AddUser) {
        viewModelScope.launch {
            console.log(addUser)
            ReqUser.addUser(addUser)
//            message.info("+1")
        }
    }
}

val AddUserFC = viewModelFc<AddUserVM> { vm ->


    div {
        form {
            css {
                width = 300.px
            }

            onFinish = vm::add

            name = "user"

            Form.Item {
                label = "名字"
                name = "name"
                rules = arrayOf(
                    rule {
                        type = String::class.simpleName
                        max = 28
                        min = 1
                        required = true
                    }
                )
                Input()
            }

            Form.Item {
                label = "密码"
                name = "password"
                Input.Password()
            }

            Form.Item {
                label = "年龄"
                name = "birthday"

                DatePicker {
                    onChange = { date, s ->
                        console.log(date)
                        console.log(s)
                    }
                }
            }

            Form.Item {
                label = "性别"
                name = "gender"
                Radio.Group {
                    value = Gender.Male
                    options = arrayOf(
                        radioOptions(Gender.Male) {
                            +"男"
                        },
                        radioOptions(Gender.Female) {
                            +"女"
                        }
                    )
                    onChange = {
                        console.log(it.target.value)
                    }
                }
            }

            Form.Item {
                Button {
                    type = ButtonType.primary
                    htmlType = web.html.ButtonType.submit

                    +"提交"
                }
            }
        }
    }
}

interface AddUser {
    var name: String
    var password: String
    var birthday: kotlin.js.Date
    var address: String
    var gender: Gender
}
