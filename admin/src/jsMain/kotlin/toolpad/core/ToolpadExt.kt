package toolpad.core

import js.objects.unsafeJso
import mui.material.FormControlLabelProps
import mui.material.TextFieldProps
import react.dom.html.FormHTMLAttributes
import seskar.js.JsValue
import web.html.HTMLFormElement


sealed external interface AuthProviderId {
    companion object {
        @JsValue("credentials")
        val credentials: AuthProviderId

        @JsValue("nodemailer")
        val nodemailer: AuthProviderId

        //...
    }
}


fun SignInProps.slotProps(
    emailField: (TextFieldProps.() -> Unit)? = null,
    passwordField: (TextFieldProps.() -> Unit)? = null,
    form: (FormHTMLAttributes<HTMLFormElement>.() -> Unit)? = null,
    rememberMe: (FormControlLabelProps.() -> Unit)? = null,
) {
    slotProps = unsafeJso {
        this.emailField = emailField?.let(::unsafeJso)
        this.passwordField = passwordField?.let(::unsafeJso)
        this.rememberMe = rememberMe?.let(::unsafeJso)
        this.form = form?.let(::unsafeJso)
    }
}


external interface Authentication {
    var signIn: () -> Unit
    var signOut: () -> Unit
}


sealed external interface SeverityMui {
    companion object {
        @JsValue("info")
        val info: SeverityMui

        @JsValue("warning")
        val warning: SeverityMui

        @JsValue("error")
        val error: SeverityMui

        @JsValue("success")
        val success: SeverityMui
    }
}


//(brandingTitle?: string) => string
fun SignInPageLocaleText.signInTitle(title: (String) -> String) {
    signInTitle = title.asDynamic()
}

fun Notifications.show(
    text: String,
    autoHideDuration: Int = 1500,
    severity: SeverityMui = SeverityMui.info,
) {
    show(text, unsafeJso {
        this.autoHideDuration = autoHideDuration
        this.severity = severity
    })
}
