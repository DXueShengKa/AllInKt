@file:JsModule("@toolpad/core/SignInPage")

package toolpad.core

import react.FC
import react.Props
import web.form.FormData
import kotlin.js.Promise


external val SignInPage: FC<SignInProps>


external interface SignInProps : Props {
    var signIn: (AuthProvider, FormData) -> Promise<AuthResponse>
    var providers: Array<AuthProvider>
    var slotProps: SignInSlot
}



