package toolpad.core

import js.objects.jso
import mui.material.FormControlLabelProps
import mui.material.TextFieldProps
import react.dom.html.FormHTMLAttributes
import seskar.js.JsValue
import web.html.HTMLFormElement


external interface AuthProvider {
    var id: AuthProviderId
    var name: String
}

sealed external interface AuthProviderId {
    companion object {
        @JsValue("credentials")
        val credentials: AuthProviderId

        @JsValue("nodemailer")
        val nodemailer: AuthProviderId

        //...
    }
}


external interface SignInSlot {
    var emailField: TextFieldProps?
    var passwordField: TextFieldProps?
    var form: FormHTMLAttributes<HTMLFormElement>?
    var rememberMe: FormControlLabelProps?

}


fun SignInProps.slotProps(
    emailField: (TextFieldProps.() -> Unit) ? = null,
    passwordField: (TextFieldProps.() -> Unit) ? = null,
    form: (FormHTMLAttributes<HTMLFormElement>.() -> Unit)? = null,
    rememberMe: (FormControlLabelProps.() -> Unit)? = null,
) {
    slotProps = jso {
        this.emailField = emailField?.let(::jso)
        this.passwordField = passwordField?.let(::jso)
        this.rememberMe = rememberMe?.let(::jso)
        this.form = form?.let(::jso)
    }
}

external interface AuthResponse {
    var error: String?
    var type: String?
}


external interface Authentication {
    var signIn: () -> Unit
    var signOut: () -> Unit
}

external interface Notifications {
    fun show(text: String, config: NotificationsConfig = definedExternally): Any
    fun close(key: Any = definedExternally)
}


sealed external interface NCSeverity {
    companion object {
        @JsValue("info")
        val info: NCSeverity

        @JsValue("warning")
        val warning: NCSeverity

        @JsValue("error")
        val error: NCSeverity

        @JsValue("success")
        val success: NCSeverity
    }
}


external interface NotificationsConfig {
    var severity: NCSeverity
    var autoHideDuration: Int
}

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


//(brandingTitle?: string) => string
fun SignInPageLocaleText.signInTitle(title: (String) -> String) {
    signInTitle = title.asDynamic()
}
