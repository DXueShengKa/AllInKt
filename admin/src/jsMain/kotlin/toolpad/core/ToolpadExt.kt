package toolpad.core

import js.objects.jso
import seskar.js.JsValue


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
    var emailField: dynamic
    var form: dynamic
}

external interface EmailField {
    var autoFocus: Boolean?
}

external interface SignInForm {
    var noValidate: Boolean?
}

inline fun SignInProps.slotProps(
    emailField: EmailField.() -> Unit,
    form: SignInForm.() -> Unit,
) {
    this.slotProps = jso<SignInSlot> {
        this.emailField = jso<EmailField>(emailField)
        this.form = jso<SignInForm>(form)
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
