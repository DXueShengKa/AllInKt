package cn.allin.ui

import ant.*
import cn.allin.net.ReqUser
import emotion.react.css
import react.FC
import react.dom.html.ReactHTML.div
import react.useEffect
import react.useState
import web.cssom.px


val AddUserFC = FC {

    var r: AddUser? by useState(null)

    useEffect(r) {
        r?.also {
            ReqUser.addUser(it)
        }
    }


    div {
        form<AddUser> {
            css {
                width = 300.px
            }

            onFinish = {
                r = it
                console.log(it)
            }
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
                label = "年龄"
                name = "age"

                InputNumber {
                    max = 150
                    min = 1
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
    var age: Int
}
