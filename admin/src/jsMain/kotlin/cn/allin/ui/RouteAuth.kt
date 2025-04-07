package cn.allin.ui

import SessionContextValue
import cn.allin.utils.getValue
import cn.allin.utils.useCoroutineScope
import js.objects.jso
import kotlinx.coroutines.delay
import kotlinx.coroutines.promise
import react.FC
import react.router.NavigateFunction
import react.router.useNavigate
import toolpad.core.AuthProvider
import toolpad.core.AuthProviderId
import toolpad.core.AuthResponse
import toolpad.core.SignInPage
import toolpad.core.slotProps
import useSession
import web.form.FormData

const val RouteAuth = "Auth"

private val providers: Array<AuthProvider> = arrayOf(
    jso {
        id = AuthProviderId.credentials
        name = "登录"
    }
)

private suspend fun login(nav: NavigateFunction, sessionContext: SessionContextValue, provider: AuthProvider, formData: FormData): AuthResponse {
    delay(2000)
//    console.log(provider, formData, formData["email"], formData["password"])
    sessionContext.set(jso())
    nav("/")
    return jso()
}

val RouteAuthFC = FC {
    val nav = useNavigate()
    var sessionContext = useSession()
    val cs  by useCoroutineScope()

    SignInPage {

        signIn = { provider, formData ->
            cs.promise {
                login(nav, sessionContext, provider, formData)
            }
        }

        providers = cn.allin.ui.providers

        slotProps(
            emailField = {
                autoFocus = false
            },
            form = {
                noValidate = true
            }
        )
    }
}
