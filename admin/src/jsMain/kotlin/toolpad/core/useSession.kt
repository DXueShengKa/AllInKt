@file:JsModule("@toolpad/core/useSession")

package toolpad.core


external fun <S: Session> useSession(): S?


external interface Session {
    var user: UserSession?
}

external interface UserSession {
    var id: String?
    var name: String?
    var image: String?
    var email: String?
}



