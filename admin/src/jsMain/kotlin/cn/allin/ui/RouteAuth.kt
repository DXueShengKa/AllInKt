package cn.allin.ui

import SessionContextValue
import cn.allin.VoFieldName
import cn.allin.net.Req
import cn.allin.net.auth
import cn.allin.net.currentUser
import cn.allin.utils.getValue
import cn.allin.utils.useCoroutineScope
import cn.allin.vo.UserVO
import js.objects.jso
import kotlinx.coroutines.promise
import react.FC
import react.router.NavigateFunction
import react.router.useNavigate
import toolpad.core.AuthProvider
import toolpad.core.AuthProviderId
import toolpad.core.AuthResponse
import toolpad.core.SignInPage
import toolpad.core.slotProps
import useSessionContext
import web.form.FormData
import web.html.InputType

const val RouteAuth = "Auth"

private val providers: Array<AuthProvider> = arrayOf(
    jso {
        id = AuthProviderId.credentials
        name = "登录"
    }
)

private suspend fun login(nav: NavigateFunction, sessionContext: SessionContextValue, provider: AuthProvider, formData: FormData): AuthResponse {
    console.log(provider)

    val vo = UserVO(
        name = formData.get(VoFieldName.UserVO_name)?.toString(),
        password = formData.get(VoFieldName.UserVO_password)?.toString(),
    )
    val result = Req.auth(vo)
    if (result.isSuccess){
        val u = Req.currentUser()
        sessionContext.set(jso {
            user = u
        })
        nav("/")
        return jso()
    } else {
        return jso {
            error = result.message
        }
    }

}

val RouteAuthFC = FC {
    val nav = useNavigate()
    var sessionContext = useSessionContext()
    val cs by useCoroutineScope()

    SignInPage {

        localeText = jso {
            password = "密码"
            email = "用户名"
            signInRememberMe = "记住我"
            signInTitle = "登录"
            to = "到"
            signInSubtitle = "请登录"
        }

        signIn = { provider, formData ->
            cs.promise {
                login(nav, sessionContext, provider, formData)
            }
        }

        providers = cn.allin.ui.providers

        slotProps(
            emailField = {
                autoFocus = false
                type = InputType.text
                name = VoFieldName.UserVO_name
            },
            passwordField = {
                name = VoFieldName.UserVO_password
            },
            form = {
                noValidate = true
            },
            rememberMe = {
                disabled = false
            }
        )
    }
}
