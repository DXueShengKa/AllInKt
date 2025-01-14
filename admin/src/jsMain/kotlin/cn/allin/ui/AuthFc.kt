package cn.allin.ui

import ant.Button
import ant.ButtonType
import ant.Form
import ant.Input
import ant.form
import ant.rule
import cn.allin.ViewModel
import cn.allin.net.ReqAuth
import cn.allin.viewModelFc
import cn.allin.vo.MsgVO
import cn.allin.vo.UserVO
import kotlinx.coroutines.launch
import react.FC
import react.dom.html.ReactHTML.div

const val RouteAuth = "auth"


fun NavAuth(onLogin: () -> Unit): FC<*> {
    return viewModelFc<AuthViewModel> { vm ->
        div {
            form<AuthVo> {
                onFinish = {
                    vm.login(it, onLogin)
                }
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
                    rules = arrayOf(
                        rule {
                            required = true
                        }
                    )
                    Input.Password()
                }

                Form.Item {
                    Button {
                        type = ButtonType.primary
                        htmlType = web.html.ButtonType.submit
                        +"登录"
                    }
                }
            }
        }
    }
}

private class AuthViewModel : ViewModel() {
    fun login(authVo: AuthVo, onLogin: () -> Unit) {
        viewModelScope.launch {
            val msgVO = ReqAuth.auth(UserVO(name = authVo.name, password = authVo.password))
            if (msgVO.code == MsgVO.OK) {
                onLogin()
            } else {
                //todo
//                message.error(msgVO.message)
            }
        }
    }
}

private external interface AuthVo {
    var name: String
    var password: String
}