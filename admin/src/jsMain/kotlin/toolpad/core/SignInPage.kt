@file:JsModule("@toolpad/core/SignInPage")

package toolpad.core

import mui.material.FormControlLabelProps
import mui.material.TextFieldProps
import react.FC
import react.Props
import react.dom.html.FormHTMLAttributes
import web.form.FormData
import web.html.HTMLFormElement
import kotlin.js.Promise


external val SignInPage: FC<SignInProps>


external interface SignInPageLocaleText {
    var signInTitle: String
    var signInSubtitle: String
    var signInRememberMe: String
    // providerSignInTitle: (provider: string) => string;
    var providerSignInTitle: (String) -> String
    var email: String
    var password: String
    var or: String
    var with: String
    var passkey: String
    var to: String
}


external interface AuthProvider {
    var id: AuthProviderId
    var name: String
}

external interface AuthResponse {
    var error: String?
    var type: String?
}

external interface SignInProps : Props {
    var signIn: (AuthProvider, FormData) -> Promise<AuthResponse>
    var providers: Array<AuthProvider>
    var slotProps: SignInSlot

    var localeText: SignInPageLocaleText
}



external interface SignInSlot {
    var emailField: TextFieldProps?
    var passwordField: TextFieldProps?
    var form: FormHTMLAttributes<HTMLFormElement>?
    var rememberMe: FormControlLabelProps?
}

