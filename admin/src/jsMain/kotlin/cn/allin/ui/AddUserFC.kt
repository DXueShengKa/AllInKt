package cn.allin.ui

import ant.*
import cn.allin.ViewModel
import cn.allin.net.ReqUser
import cn.allin.viewModelFc
import emotion.react.css
import kotlinx.coroutines.launch
import react.dom.html.ReactHTML.div
import web.cssom.px
import kotlin.String


const val RouteAddUser = "addUser"

private class AddUserVM : ViewModel() {
    init {
        println("addUserVM")
    }

    fun add(addUser: AddUser) {
        viewModelScope.launch {
            ReqUser.addUser(addUser)
            message.info("+1")
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

                DatePicker{
                    onChange =  { date, s ->
                        console.log(date)
                        console.log(s)
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
}
