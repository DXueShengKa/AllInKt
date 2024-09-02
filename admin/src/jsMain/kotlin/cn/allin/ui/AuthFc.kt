package cn.allin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.react.useViewModel
import androidx.navigation.react.NavProps
import ant.*
import cn.allin.appNavController
import cn.allin.net.ReqAuth
import cn.allin.vo.MsgVO
import cn.allin.vo.UserVO
import kotlinx.coroutines.launch
import react.FC
import react.dom.html.ReactHTML.div

const val RouteAuth = "auth"


val NavAuth = FC<NavProps> {
    val authVm: AuthViewModel = useViewModel()

    div {
        form<AuthVo> {

            onFinish = {
                authVm.login(it)
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

private class AuthViewModel : ViewModel() {
    fun login(authVo: AuthVo) {
        viewModelScope.launch {
            val msgVO = ReqAuth.auth(UserVO(name = authVo.name, password = authVo.password))
            if (msgVO.code == MsgVO.OK) {
                appNavController.navigate(RouteUserList)
            } else {
                message.error(msgVO.message)
            }
        }
    }
}

private external interface AuthVo {
    var name: String
    var password: String
}