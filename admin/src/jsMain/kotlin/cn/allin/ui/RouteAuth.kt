package cn.allin.ui

import cn.allin.api.ApiUser
import cn.allin.name
import cn.allin.net.Req
import cn.allin.net.auth
import cn.allin.net.userSession
import cn.allin.password
import cn.allin.utils.SessionContextValue
import cn.allin.utils.getValue
import cn.allin.utils.setValue
import cn.allin.utils.useCoroutineScope
import cn.allin.utils.useInject
import cn.allin.utils.useSessionContext
import cn.allin.vo.UserVO
import js.objects.unsafeJso
import kotlinx.coroutines.promise
import react.FC
import react.router.NavigateFunction
import react.router.useNavigate
import react.useRef
import toolpad.core.AuthProvider
import toolpad.core.AuthProviderId
import toolpad.core.AuthResponse
import toolpad.core.SignInPage
import toolpad.core.slotProps
import web.form.FormData
import web.html.InputType

const val RouteAuth = "Auth"

private val providers: Array<AuthProvider> = arrayOf(
    unsafeJso {
        id = AuthProviderId.credentials
        name = "登录"
    }
)

private suspend fun login(
    nav: NavigateFunction,
    apiUser: ApiUser,
    sessionContext: SessionContextValue,
    formData: FormData,
    remember: Boolean
): AuthResponse {
    formData.forEach { a, s ->
        console.log(a, s)
    }
    val vo = UserVO(
        name = formData.get(UserVO.name.name)?.toString(),
        password = formData.get(UserVO.password.name)?.toString(),
    )
    val result = Req.auth(vo, remember)
    if (result.isSuccess) {
        val u = apiUser.userSession()
        sessionContext.set(unsafeJso {
            user = u
        })
        nav("/")
        return unsafeJso()
    } else {
        return unsafeJso {
            error = result.message
        }
    }

}

val RouteAuthFC = FC {
    val nav = useNavigate()
    var sessionContext = useSessionContext()
    val cs = useCoroutineScope()
    var remember by useRef(false)
    val apiUser: ApiUser = useInject()

    SignInPage {

        localeText = unsafeJso {
            password = UserVO.password.display
            email = "用户名"
            signInRememberMe = "记住我"
            signInTitle = "登录"
            to = "到"
            signInSubtitle = "请登录"
        }

        signIn = { provider, formData ->
            cs.promise {
                login(nav, apiUser, sessionContext, formData, remember)
            }
        }

        providers = cn.allin.ui.providers

        slotProps(
            emailField = {
                autoFocus = false
                type = InputType.text
                name = UserVO.name.name
            },
            passwordField = {
                name = UserVO.password.name
            },
            form = {
                noValidate = true
            },
            rememberMe = {
                disabled = false
                onChange = { e, b ->
                    remember = b
                }
            }
        )
    }
}
