package cn.allin.ui

import cn.allin.ViewModel
import cn.allin.net.ReqAuth
import cn.allin.vo.MsgVO
import cn.allin.vo.UserVO
import kotlinx.coroutines.launch
import react.FC

const val RouteAuth = "auth"


fun NavAuth(onLogin: () -> Unit): FC<*> {
    return FC {

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