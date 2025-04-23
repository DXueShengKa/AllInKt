package toolpad.core

import js.objects.jso
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
    slotProps = jso {
        this.emailField = emailField?.let(::jso)
        this.passwordField = passwordField?.let(::jso)
        this.rememberMe = rememberMe?.let(::jso)
        this.form = form?.let(::jso)
    }
}


external interface Authentication {
    var signIn: () -> Unit
    var signOut: () -> Unit
}


sealed external interface SeverityStr {
    companion object {
        @JsValue("info")
        val info: SeverityStr

        @JsValue("warning")
        val warning: SeverityStr

        @JsValue("error")
        val error: SeverityStr

        @JsValue("success")
        val success: SeverityStr
    }
}


//(brandingTitle?: string) => string
fun SignInPageLocaleText.signInTitle(title: (String) -> String) {
    signInTitle = title.asDynamic()
}

fun Notifications.show(
    text: String,
    autoHideDuration: Int = 1500,
    severity: SeverityStr = SeverityStr.info,
) {
    show(text, jso {
        this.autoHideDuration = autoHideDuration
        this.severity = severity
    })
}
