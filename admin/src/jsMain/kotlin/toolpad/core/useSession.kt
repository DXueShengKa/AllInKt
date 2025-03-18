//@file:JsModule("@toolpad/core/useSession")

package toolpad.core


//external fun useSession(): Session?


external interface Session {
    var user: UserSession?
}

external interface UserSession {
    var id: String
    var name: String
    var image: String
    var email: String
}



